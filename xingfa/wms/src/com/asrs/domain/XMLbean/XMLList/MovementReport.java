package com.asrs.domain.XMLbean.XMLList;

import com.asrs.business.consts.AsrsJobStatus;
import com.opple.WebService;
import com.wms.domain.*;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.asrs.domain.XMLbean.XMLProcess;
import com.asrs.business.consts.ReasonCode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.hibernate.HibernateUtil;
import com.wms.domain.Location;
import com.wms.domain.blocks.Block;
import com.wms.domain.blocks.StationBlock;
import org.apache.http.client.utils.DateUtils;
import org.hibernate.*;
import org.hibernate.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
        Session session = HibernateUtil.getCurrentSession();
        String mcKey = controlArea.getRefId().getReferenceId();
        Job j = Job.getByMcKey(mcKey);
        if (dataArea.getReasonCode().equals(ReasonCode.PUTAWAYFINISHED)) {
            Location l = j.getToLocation();
            l.setReserved(false);
            l.setEmpty(false);
            session.update(l);

            Container container = null;

            Query query = HibernateUtil.getCurrentSession().createQuery("from InventoryView where palletCode=:palletNo");
            query.setParameter("palletNo", j.getContainer());
            List<InventoryView> views = query.list();


            container = Container.getByBarcode(j.getContainer());

            if (container == null) {
                container = new Container();
                container.setBarcode(j.getContainer());
                container.setLocation(l);
                container.setCreateDate(new Date());
                container.setCreateUser("sys");
                container.setReserved(true);
                HibernateUtil.getCurrentSession().save(container);
            }
            InventoryLog inventoryLog = new InventoryLog();
            inventoryLog.setQty(BigDecimal.ZERO);
            inventoryLog.setType(InventoryLog.TYPE_IN);
            for (InventoryView view : views) {

                if (view != null) {
                    Inventory inventory = new Inventory();
                    inventory.setWhCode(view.getWhCode());
                    inventory.setSkuName(view.getSkuName());
                    inventory.setLotNum(view.getLotNum());
                    inventory.setQty(view.getQty());
                    inventory.setSkuCode(view.getSkuCode());
                    inventory.setContainer(container);
                    inventory.setStoreDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                    inventory.setStoreTime(DateUtils.formatDate(new Date(), "HH:mm:ss"));
                    HibernateUtil.getCurrentSession().save(inventory);
                    inventoryLog.setQty(inventoryLog.getQty().add(inventory.getQty()));
                    inventoryLog.setSkuCode(inventory.getSkuCode());
                    inventoryLog.setWhCode(inventory.getWhCode());
                    inventoryLog.setToLocation(container.getLocation().getLocationNo());
                    inventoryLog.setLotNum(inventory.getLotNum());
                    inventoryLog.setSkuName(inventory.getSkuName());

                    session.delete(view);
                }

            }
            inventoryLog.setContainer(container.getBarcode());
            inventoryLog.setCreateDate(new Date());
            session.save(inventoryLog);


            // TODO: 放在后台
//                WebService.finishPutaway(j.getContainer());

        } else if (dataArea.getReasonCode().equals(ReasonCode.RETRIEVALFINISHED)) {

            Location location = j.getFromLocation();
            location.setEmpty(true);
            location.setRetrievalRestricted(false);
            session.update(location);
            j.setStatus(AsrsJobStatus.DONE);
//            OutMessage.info(j.getToStation(), j.getOrderNo(), j.getContainer());


            //// TODO: 放在后台
//                WebService.finishOrder(j);
        } else if (dataArea.getReasonCode().equals(ReasonCode.SLOCATIONTOLOCATION)) {
            Location location = j.getFromLocation();
            Location toLocation = j.getToLocation();
            location.setEmpty(true);
            location.setRetrievalRestricted(false);

            toLocation.setEmpty(false);
            toLocation.setRetrievalRestricted(false);

            session.update(location);
            session.update(toLocation);

        }
        j.asrsDone();

    }
}
