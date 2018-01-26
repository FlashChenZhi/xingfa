package com.asrs.thread;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.TransportType;
import com.asrs.domain.XMLbean.Envelope;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.ControlArea.RefId;
import com.asrs.domain.XMLbean.XMLList.ControlArea.Sender;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.TransportOrderDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.asrs.domain.XMLbean.XMLList.DataArea.StUnit;
import com.asrs.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.asrs.domain.XMLbean.XMLList.TransportOrder;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.asrs.xml.util.XMLUtil;
import com.util.common.DateFormat;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */
public class ThreadMain {
    public static void main(String[] args) {
        while (true){

            try {
                Transaction.begin();
                Query query = HibernateUtil.getCurrentSession().createQuery("from Job  where sendReport=false and type=:tp order by fromLocation.position asc, fromLocation.bay asc,fromLocation.level asc,fromLocation.seq desc")
                        .setParameter("tp", AsrsJobType.RETRIEVAL).setMaxResults(1);

                Job job = (Job) query.uniqueResult();
                if (job != null) {
                    createOutJob(job);
                    job.setSendReport(true);
                }

                Transaction.commit();

            } catch (Exception e) {
                Transaction.rollback();

            } finally {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private static void createOutJob(Job job) throws Exception {
        Location location = job.getFromLocation();
        location.setRetrievalRestricted(true);
        //获取容器Container
        Container container = Container.getByBarcode(job.getContainer());
        container.setReserved(true);
        HibernateUtil.getCurrentSession().update(container);

        //创建FromLocation对象
        FromLocation fromLocation = new FromLocation();
        List<String> locations = new ArrayList<>();
        locations.add(location.getBank() + "");
        locations.add(location.getBay() + "");
        locations.add(location.getLevel() + "");
        fromLocation.setRack(locations);

        fromLocation.setMHA(job.getFromStation());

        ToLocation toLocation = new ToLocation();
        toLocation.setMHA(job.getToStation());

        StUnit stUnit = new StUnit();

        stUnit.setStUnitID(container.getBarcode());
        //创建TransportOrderDA对象
        TransportOrderDA transportOrderDA = new TransportOrderDA();
        transportOrderDA.setTransportType(TransportType.RETRIEVAL);
        transportOrderDA.setFromLocation(fromLocation);
        transportOrderDA.setToLocation(toLocation);
        transportOrderDA.setStUnit(stUnit);

        //创建sender对象
        Sender sender = new Sender();
        sender.setDivision(XMLConstant.COM_DIVISION);

        RefId refId = new RefId();
        refId.setReferenceId(job.getMcKey());

        //创建ControlArea对象
        ControlArea controlArea = new ControlArea();
        controlArea.setRefId(refId);
        controlArea.setCreationDateTime(DateFormat.format(new Date(), DateFormat.YYYYMMDDHHMMSS));
        controlArea.setSender(sender);

        //wms发送给wcs的运输命令
        TransportOrder transportOrder = new TransportOrder();
        transportOrder.setDataArea(transportOrderDA);
        transportOrder.setControlArea(controlArea);

        Envelope envelope = new Envelope();
        envelope.setTransportOrder(transportOrder);

        XMLMessage xmlMessage = new XMLMessage();
        xmlMessage.setRecv("WCS");
        xmlMessage.setStatus("1");
        xmlMessage.setMessageInfo(XMLUtil.getSendXML(envelope));

        HibernateUtil.getCurrentSession().save(xmlMessage);

    }

}
