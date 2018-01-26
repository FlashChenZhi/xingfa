package com.asrs.domain.XMLTransportOrder;

import com.asrs.xml.util.MsgUtil;
import com.asrs.domain.XMLbean.XMLList.TransportOrder;
import com.util.hibernate.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator Date: 13-8-30 Time:
 * 下午1:37 To change this template use File | Settings | File Templates.
 */
public class XMLTransportOrderUtil {
    /**
     * 添加 @param xmlTransportOrder
     */
    public static void add(XMLTransportOrder xmlTransportOrder) {
        Session session = HibernateUtil.getCurrentSession();
        session.save(xmlTransportOrder);
    }

    /**
     * 修改 @param xmlTransportOrder
     */
    public static void modify(XMLTransportOrder xmlTransportOrder) {
        Session session = HibernateUtil.getCurrentSession();
        session.update(xmlTransportOrder);
    }

    /**
     * 删除 @param xmlTransportOrder
     */
    public static void delete(XMLTransportOrder xmlTransportOrder) {
        Session session = HibernateUtil.getCurrentSession();
        session.delete(xmlTransportOrder);
    }

    public static XMLTransportOrder getTransportOrder(TransportOrder order) {
        XMLTransportOrder xmlTransportOrder = new XMLTransportOrder();
        xmlTransportOrder.setDivision(order.getControlArea().getSender().getDivision());
        xmlTransportOrder.setConfirmation(order.getControlArea().getSender().getConfirmation());
        xmlTransportOrder.setCreationDateTime(getTime(order.getControlArea().getCreationDateTime()));
        xmlTransportOrder.setRequestId(order.getControlArea().getRefId().getReferenceId());
        xmlTransportOrder.setRequestId(order.getDataArea().getRequestId());

        xmlTransportOrder.setPriority(getTime(order.getDataArea().getPriority()));
        xmlTransportOrder.setTransportType(order.getDataArea().getTransportType());
        xmlTransportOrder.setIdentiffiedBy(order.getDataArea().getIdentifiedBy());
        if (order.getDataArea().getPart() != null) {
            xmlTransportOrder.setPartid(order.getDataArea().getPart().getPartId());
            xmlTransportOrder.setPartRev(order.getDataArea().getPart().getPartRev());
            xmlTransportOrder.setPartDivision(order.getDataArea().getPart().getDivision());
            xmlTransportOrder.setZone(order.getDataArea().getPart().getZone());
        }
        String fromMHA = order.getDataArea().getFromLocation().getMHA();
        xmlTransportOrder.setFromMHA(fromMHA);
//        xmlTransportOrder.setFromRACK(order.getDataArea().getFromLocation());
        List<String> ranks = order.getDataArea().getFromLocation().getRack();
        xmlTransportOrder.setFromX(ranks.get(0));
        xmlTransportOrder.setFromY(ranks.get(1));
        xmlTransportOrder.setFromZ(ranks.get(2));
        String fromLocationNo = null;
        if (!ranks.isEmpty()) {
//            fromLocationNo = MsgUtil.getLocFromXML(order.getDataArea().getFromLocation().getRack(),
//                    order.getDataArea().getFromLocation().getX(),
//                    order.getDataArea().getFromLocation().getY());
        }

        xmlTransportOrder.setFromLocationNo(fromLocationNo);
        xmlTransportOrder.setStunitId(order.getDataArea().getStUnit().getStUnitID());
        xmlTransportOrder.setStunitType(order.getDataArea().getStUnit().getStUnitType());
        xmlTransportOrder.setBlockCode(order.getDataArea().getStUnit().getBlockCode());
        xmlTransportOrder.setRegDate(order.getDataArea().getStUnit().getRegDate());
        String toMHA = order.getDataArea().getToLocation().getMHA();
        xmlTransportOrder.setToMHA(toMHA);

        List<String> toRanks = order.getDataArea().getFromLocation().getRack();

        xmlTransportOrder.setToX(toRanks.get(0));
        xmlTransportOrder.setToY(toRanks.get(1));
        xmlTransportOrder.setToZ(toRanks.get(2));
        String toLocationNo = null;
        if (!toRanks.isEmpty()) {
//            toLocationNo = MsgUtil.getLocFromXML(order.getDataArea().getToLocation().getRack(),
//                    order.getDataArea().getToLocation().getX(),
//                    order.getDataArea().getToLocation().getY());
        }

        xmlTransportOrder.setToLocationNo(toLocationNo);
        xmlTransportOrder.setCalcWeight(order.getDataArea().getCalcWeight());
        xmlTransportOrder.setErrorCode(order.getDataArea().getErrorCode());
        xmlTransportOrder.setTreatmentCode(order.getDataArea().getTreatmentCode());
        xmlTransportOrder.setInformation(order.getDataArea().getInformation());


        return xmlTransportOrder;
    }

    public static String getTime(String date){

        return  date.replaceAll("-","").replaceAll(":","").replaceAll("T","").replaceAll("Z","");

    }
}
