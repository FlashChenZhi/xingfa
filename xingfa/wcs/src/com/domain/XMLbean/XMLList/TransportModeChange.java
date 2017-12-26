package com.domain.XMLbean.XMLList;

import com.asrs.business.consts.StationMode;
import com.asrs.communication.MessageProxy;
import com.asrs.domain.Station;
import com.asrs.message.Message40;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.DataArea.DAList.TransportModeChangeDA;
import com.domain.XMLbean.XMLList.DataArea.DAList.WorkStartEndDA;
import com.domain.XMLbean.XMLProcess;
import com.domain.consts.xmlbean.XMLConstant;
import com.test.blocks.StationBlock;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Session;

import javax.persistence.*;
import java.rmi.Naming;

/**
 * Created by Administrator on 2016/10/26.
 */
@Entity
@Table(name = "TransportModeChange")
public class TransportModeChange extends XMLProcess {
    @XStreamAsAttribute
    @XStreamAlias("version")
    private String version = XMLConstant.COM_VERSION;

    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private TransportModeChangeDA dataArea;

    @OneToOne(targetEntity = ControlArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ControlAreaID", updatable = true)
    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }

    @OneToOne(targetEntity = WorkStartEndDA.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "TransportModeChangeDAID", updatable = true)
    public TransportModeChangeDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(TransportModeChangeDA dataArea) {
        this.dataArea = dataArea;
    }

    @Column(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "WORKSTARTEND_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void execute() {
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Station station = Station.getStation(dataArea.getMha());
            station.setOldMode(station.getMode());
            station.setMode(StationMode.UNKNOWN);
            session.saveOrUpdate(station);
            StationBlock block = (StationBlock) session.createQuery("from StationBlock sb where sb.stationNo = :stationNo").uniqueResult();
            Message40 message40 = new Message40();
            message40.setPlcName(block.getPlcName());
            message40.Action = "01";
            message40.Station = dataArea.getMha();
            message40.Mode = dataArea.getTransportType();
            MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
            _wcsproxy.addSndMsg(message40);
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }
    }
}
