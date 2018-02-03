package com;

import com.asrs.business.consts.ReasonCode;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.Receiver;
import com.domain.XMLbean.XMLList.ControlArea.RefId;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.domain.XMLbean.XMLList.MovementReport;
import com.domain.consts.xmlbean.XMLConstant;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by van on 2017/6/6.
 */
public class CreateLocation {
    public static void main(String[] args) throws Exception {

        Transaction.begin();

        for (int level = 1; level <= 4; level++) {
            //level
            for (int bay = 1; bay <= 47; bay++) {
                //bay
                for (int bank = 3; bank <= 26; bank++) {
                    if(level != 4) {
                        if(bank == 13){
                            if(bay == 8 || bay == 18 || bay == 28 || bay == 39){
                                continue;
                            }
                        }
                    }else {
                        if(9 <= bay && bay <= 38){
                            continue;
                        }else if(bank == 13 && (bay == 39 || bay == 8)){
                            continue;
                        }
                    }

//                    if(level == 1){
//                        if(1<= bay && bay <= 13){
//                            continue;
//                        }else if(34<= bay && bay <= 40){
//                            continue;
//                        }
//                    }else if(level == 2){
//                        if(1<= bay && bay <= 12){
//                            continue;
//                        }else if(34<= bay && bay <= 40){
//                            continue;
//                        }
//                    }else{
//                        if(9<= bay && bay <= 12){
//                            continue;
//                        }else if(34<= bay && bay <= 40){
//                            continue;
//                        }
//                    }
                    //bank
                    String locationNo = StringUtils.leftPad(bank + "", 3, '0')
                            + StringUtils.leftPad(bay + "", 3, '0')
                            + StringUtils.leftPad(level + "", 3, '0');

                    Location location = new Location();
                    location.setLocationNo(locationNo);
                    location.setActualArea("2");
                    if(bank < 13 && (bay == 8 || bay == 18 || bay == 28 || bay == 39)){
                        location.setPosition("1");
                    }else {
                        location.setPosition("2");
                    }
                    if(bank > 13 && (bay == 8 || bay == 18 || bay == 28 || bay == 39)){
                        location.setOutPosition("2");
                    }else {
                        location.setOutPosition("1");
                    }
//                    location.setActualArea("1");
//                    location.setPosition("1");
//                    location.setOutPosition("1");
                    location.setBank(bank);
                    location.setBay(bay);
                    location.setLevel(level);
                    if(bay != 8 && bay != 18 && bay != 28 && bay != 39) {
                        location.setSeq(bank - 2);
                        location.setSeq2(bank - 2);
                    }else{
                        if(bank < 13){
                            location.setSeq(13 - bank);
                            location.setSeq2(bank - 2);
                        }else{
                            location.setSeq(bank - 13);
                            location.setSeq2(27 - bank);
                        }
                    }
//                    location.setSeq(bank);
//                    location.setSeq2(3 - bank);
                    location.setVersion(0);
                    location.setEmpty(true);
                    location.setSystem(true);
                    location.setAsrsFlag(true);
                    location.setCapacity(1);
                    HibernateUtil.getCurrentSession().save(location);
                }
            }
        }

        Transaction.commit();

    }

    public static void finishRetrieval(AsrsJob asrsJob) throws Exception{
        Sender sd = new Sender();
        sd.setDivision(XMLConstant.COM_DIVISION);

        Receiver receiver = new Receiver();
        receiver.setDivision(XMLConstant.WMS_DIVISION);
        RefId ri = new RefId();
        ri.setReferenceId(asrsJob.getWmsMckey());

        ControlArea ca = new ControlArea();
        ca.setSender(sd);
        ca.setReceiver(receiver);
        ca.setRefId(ri);
        ca.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        Location location = Location.getByLocationNo(asrsJob.getFromLocation());
        FromLocation fromLocation = new FromLocation();
        fromLocation.setMHA(asrsJob.getFromStation());
        List<String> rack = new ArrayList<>(3);
        rack.add(String.valueOf(location.getBank()));
        rack.add(String.valueOf(location.getBay()));
        rack.add(String.valueOf(location.getLevel()));
        fromLocation.setRack(rack);

        ToLocation toLocation = new ToLocation();
        List<String> list = new ArrayList<>(3);
        list.add("");
        list.add("");
        list.add("");
        toLocation.setMHA("");
        toLocation.setRack(list);


        //创建MovementReportDA数据域对象
        MovementReportDA mrd = new MovementReportDA();
        mrd.setReasonCode(ReasonCode.RETRIEVALFINISHED);
        mrd.setStUnitId(asrsJob.getBarcode());
        mrd.setFromLocation(fromLocation);
        mrd.setToLocation(toLocation);
        //创建MovementReport响应核心对象
        MovementReport mr = new MovementReport();
        mr.setControlArea(ca);
        mr.setDataArea(mrd);
        //将MovementReport发送给wms
        Envelope el = new Envelope();
        el.setMovementReport(mr);
        XMLUtil.sendEnvelope(el);
    }

    public static void finish(AsrsJob aj) throws Exception {
        Location location = Location.getByLocationNo(aj.getToLocation());
        //创建ControlArea控制域对象
        Sender sd = new Sender();
        sd.setDivision(XMLConstant.COM_DIVISION);

        Receiver receiver = new Receiver();
        receiver.setDivision(XMLConstant.WMS_DIVISION);

        RefId ri = new RefId();
        ri.setReferenceId(aj.getWmsMckey());

        ControlArea ca = new ControlArea();
        ca.setSender(sd);
        ca.setReceiver(receiver);
        ca.setRefId(ri);
        ca.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        FromLocation fromLocation = new FromLocation();
        List<String> list = new ArrayList<>(3);
        fromLocation.setMHA("");
        list.add("");
        list.add("");
        list.add("");
        fromLocation.setRack(list);

        ToLocation toLocation = new ToLocation();
        // TODO: 2017/5/1 修改货位   1.173
        toLocation.setMHA(aj.getToStation());
        List<String> rack = new ArrayList<>(3);
        rack.add(String.valueOf(location.getBank()));
        rack.add(String.valueOf(location.getBay()));
        rack.add(String.valueOf(location.getLevel()));
        toLocation.setRack(rack);

        //创建MovementReportDA数据域对象
        MovementReportDA mrd = new MovementReportDA();
        mrd.setFromLocation(fromLocation);
//                                mrd.setStUnit(stUnit);
        mrd.setStUnitId(aj.getBarcode());
        mrd.setReasonCode(ReasonCode.PUTAWAYFINISHED);
        mrd.setInformation("00");
        mrd.setToLocation(toLocation);
        //创建MovementReport响应核心对象
        MovementReport mr = new MovementReport();
        mr.setControlArea(ca);
        mr.setDataArea(mrd);
        //将MovementReport发送给wms
        Envelope el = new Envelope();
        el.setMovementReport(mr);
        XMLUtil.sendEnvelope(el);

    }


    //1到8排
    public static void oneToEight(String area) {
        for (int level = 1; level <= 5; level++) {
            //level
            for (int bay = 1; bay <= 45; bay++) {
                //bay
                for (int bank = 1; bank <= 11; bank++) {
                    //bank
                    String locationNo = area + StringUtils.leftPad(bank + "", 2, '0')
                            + StringUtils.leftPad(bay + "", 3, '0')
                            + StringUtils.leftPad(level + "", 3, '0');

                    Location location = new Location();
                    location.setLocationNo(locationNo);
                    location.setActualArea("1");
                    location.setPosition("2");
                    location.setOutPosition("1");
                    location.setBank(bank);
                    location.setBay(bay);
                    location.setLevel(level);
                    location.setSeq(1);
                    HibernateUtil.getCurrentSession().save(location);
                }
            }
        }
    }

    //9到28排
    public static void nightToTE() {

        for (int level = 1; level <= 5; level++) {
            //level
            for (int bay = 1; bay <= 47; bay++) {
                //bay
                for (int bank = 5; bank <= 11; bank++) {
                    //bank
                    String locationNo = StringUtils.leftPad(bank + "", 3, '0')
                            + StringUtils.leftPad(bay + "", 3, '0')
                            + StringUtils.leftPad(level + "", 3, '0');

                    Location location = new Location();
                    location.setLocationNo(locationNo);
                    location.setActualArea("2");
                    location.setPosition("1");
                    location.setOutPosition("2");
                    location.setBank(bank);
                    location.setBay(bay);
                    location.setLevel(level);
                    location.setSeq(1);
                    HibernateUtil.getCurrentSession().save(location);

                }
            }
        }

    }

    //29到31
    public static void TNToEng() {
        for (int level = 1; level <= 4; level++) {
            //level
            for (int bay = 1; bay <= 32; bay++) {
                //bay
                for (int bank = 29; bank <= 31; bank++) {
                    //bank
                    String locationNo = StringUtils.leftPad(bank + "", 3, '0')
                            + StringUtils.leftPad(bay + "", 3, '0')
                            + StringUtils.leftPad(level + "", 3, '0');

                    Location location = new Location();
                    location.setLocationNo(locationNo);
                    location.setActualArea("1");
                    location.setPosition("1");
                    location.setOutPosition("1");
                    location.setBank(bank);
                    location.setBay(bay);
                    location.setLevel(level);
                    location.setSeq(1);
                    location.setVersion(0);
                    HibernateUtil.getCurrentSession().save(location);
                }
            }
        }

    }

}
