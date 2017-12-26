package com.asrs.domain.XMLbean.XMLList;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.TransportType;
import com.asrs.domain.XMLbean.Envelope;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.ControlArea.RefId;
import com.asrs.domain.XMLbean.XMLList.ControlArea.Sender;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.LoadUnitAtIdDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.TransportOrderDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.StUnit;
import com.asrs.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.asrs.domain.XMLbean.XMLProcess;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.asrs.xml.util.XMLUtil;
import com.opple.WebService;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.common.Const;
import com.util.common.DateFormat;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import javafx.print.PrinterJob;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "LoadUnitAtID")
public class LoadUnitAtID extends XMLProcess {

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

    @XStreamAlias("height")
    @XStreamAsAttribute
    private String _height;

    @Column(name = "HEIGHT")
    public String getHeight() {
        return _height;
    }

    public void setHeight(String height) {
        this._height = height;
    }

    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private LoadUnitAtIdDA dataArea;

    @OneToOne(targetEntity = ControlArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ControlAreaID", updatable = true)
    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }

    @OneToOne(targetEntity = LoadUnitAtIdDA.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "LoadUnitAtIdDAID", updatable = true)
    public LoadUnitAtIdDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(LoadUnitAtIdDA dataArea) {
        this.dataArea = dataArea;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "LOADUNITATID_SEQ", allocationSize = 1)
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
        //To change body of implemented methods use File | Settings | File Templates.
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();

            InventoryView view = WebService.getPutawayInfo(dataArea.getScanData().getItemData().getValue());
            if (view != null) {

                Location newLocation = Location.getEmptyLocation(view.getSkuCode(), this.getHeight(), view.getLotNum());
                newLocation.setReserved(true);
                //开始
                //创建ControlArea控制域对象
                ControlArea ca = new ControlArea();
                Sender sd = new Sender();
                sd.setDivision(XMLConstant.COM_DIVISION);
                ca.setSender(sd);

                RefId ri = new RefId();
                ri.setReferenceId(Mckey.getNext());
                ca.setRefId(ri);
                ca.setCreationDateTime(new DateFormat().format(new Date(), DateFormat.YYYYMMDDHHMMSS));

                //创建TransportOrderDA数据域对象
                TransportOrderDA toa = new TransportOrderDA();

                toa.setTransportType(TransportType.PUTAWAY);
                ToLocation toLocation = new ToLocation();
                toLocation.setMHA(dataArea.getXMLLocation().getMHA());
                toLocation.setRack(newLocation.getLocationNo());
                toLocation.setX(String.valueOf(newLocation.getBank()));
                toLocation.setY(String.valueOf(newLocation.getBay()));
                toLocation.setZ(String.valueOf(newLocation.getLevel()));
                toa.setToLocation(toLocation);
                StUnit su = new StUnit();
                su.setStUnitID(view.getPalletCode());
                toa.setStUnit(su);
                toa.setToLocation(toLocation);

                //创建TransportOrder核心对象
                TransportOrder to = new TransportOrder();
                to.setControlArea(ca);
                to.setDataArea(toa);
                //结束
                Envelope el = new Envelope();
                el.setTransportOrder(to);
                XMLUtil.sendEnvelope(el);

            } else {
                throw new Exception("托盘数据不存在" );
            }

            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }

    }
}
