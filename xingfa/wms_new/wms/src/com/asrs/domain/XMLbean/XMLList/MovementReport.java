package com.asrs.domain.XMLbean.XMLList;

import com.opple.WebService;
import com.util.common.DateFormat;
import com.wms.domain.*;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.asrs.domain.XMLbean.XMLProcess;
import com.asrs.business.consts.ReasonCode;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "MovementReport")
public class MovementReport extends XMLProcess {
    @XStreamAlias("version")
    @XStreamAsAttribute
    private String version = XMLConstant.COM_VERSION;

    @Column(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private MovementReportDA dataArea;

    @OneToOne(targetEntity = ControlArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ControlAreaID", updatable = true)
    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }

    @OneToOne(targetEntity = MovementReportDA.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "MovementReportDAID", updatable = true)
    public MovementReportDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(MovementReportDA dataArea) {
        this.dataArea = dataArea;
    }

    @XStreamOmitField
    private int id;


    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "MOVEMENTREPORT_SEQ", allocationSize = 1)
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
            String mcKey = controlArea.getRefId().getReferenceId();
            Job j = Job.getByMcKey(mcKey);
            Container c = j.getContainer();
            if (dataArea.getReasonCode().equals(ReasonCode.PUTAWAYFINISHED)) {
                Location l = j.getToLocation();
                l.setReserved(false);
                l.setEmpty(false);
                session.update(l);
                c.setLocation(l);
                session.update(c);
                //// TODO: 放在后台
                WebService.finishPutaway(c.getBarcode());

            } else if (dataArea.getReasonCode().equals(ReasonCode.RETRIEVALFINISHED)) {

                Location location = j.getFromLocation();
                location.setEmpty(true);
                location.setRetrievalRestricted(false);
                session.update(location);
                session.delete(c);

                //// TODO: 放在后台
                WebService.finishOrder(j);


            }
            session.delete(j);
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }

    }
}
