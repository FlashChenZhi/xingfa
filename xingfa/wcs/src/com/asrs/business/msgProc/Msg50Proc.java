
package com.asrs.business.msgProc;

import com.AllBinding.blocks.Block;
import com.AllBinding.blocks.SCar;
import com.AllBinding.blocks.StationBlock;
import com.AllBinding.utils.BlockStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.ReasonCode;
import com.asrs.business.consts.StationMode;
import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.domain.Station;
import com.asrs.message.Message50;
import com.asrs.message.MessageBuilder;
import com.asrs.message.MsgException;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.RefId;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.LoadUnitAtIdDA;
import com.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.domain.XMLbean.XMLList.DataArea.*;
import com.domain.XMLbean.XMLList.LoadUnitAtID;
import com.domain.XMLbean.XMLList.MovementReport;
import com.domain.consts.xmlbean.XMLConstant;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-3-19
 * Time: 20:44:42
 * Copyright Dsl.Worgsoft.
 */
public class Msg50Proc implements MsgProcess {
    public void Do(MessageBuilder msg) throws MsgException {
        Message50 message50 = new Message50(msg.DataString);
        message50.setPlcName(msg.PlcName);
        Do(message50);
    }

    @Override
    public void setProxy(XmlProxy wmsProxy, MessageProxy wcsProxy) {
        this._wmsProxy = wmsProxy;
        this._wcsProxy = wcsProxy;
    }

    XmlProxy _wmsProxy;
    MessageProxy _wcsProxy;

    public void Do(Message50 message50) {
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();

            for (Map.Entry<String, Message50.Block> entry : message50.MachineNos.entrySet()) {
                String blockNo = entry.getKey();
                Block block1 = Block.getByBlockNo(blockNo);
                if (block1 instanceof StationBlock) {
                    Station station = Station.getStation(((StationBlock) block1).getStationNo());
                    if (StationMode.PUTAWAY.equals(station.getMode()) && "1".equals(entry.getValue().Load)) {
                        //test without wms begin
//                        block1.setLoaded(true);
//                        block1.setMcKey("0003");
                        //test without wms end
                        Query q = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type in(:jType)");
                        q.setParameterList("jType", new String[]{AsrsJobType.RECHARGE, AsrsJobType.RECHARGE_OVER});
                        List<AsrsJob> asrsJobs = q.list();
                        if (asrsJobs.isEmpty()) {
                            SCar sCar = (SCar) Block.getByBlockNo("SC01");
                            if (sCar.getStatus().equals(BlockStatus.STATUS_RUN)) {
                                for (Map.Entry<Integer, Map<String, String>> entry1 : entry.getValue().McKeysAndBarcodes.entrySet()) {
                                    for (Map.Entry<String, String> entry2 : entry1.getValue().entrySet()) {
                                        Sender sender = new Sender();
                                        sender.setDivision(XMLConstant.COM_DIVISION);

                                        ControlArea controlArea = new ControlArea();
                                        controlArea.setSender(sender);
                                        controlArea.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                                        XMLLocation xmlLocation = new XMLLocation();
                                        xmlLocation.setMHA(station.getStationNo());

                                        ItemData itemData = new ItemData();
                                        itemData.setValue(entry2.getValue());

                                        ScanData scanData = new ScanData();
                                        scanData.setItemData(itemData);

                                        LoadUnitAtIdDA loadUnitAtIdDA = new LoadUnitAtIdDA();
                                        loadUnitAtIdDA.setXMLLocation(xmlLocation);
                                        loadUnitAtIdDA.setScanData(scanData);

                                        LoadUnitAtID loadUnitAtID = new LoadUnitAtID();
                                        loadUnitAtID.setControlArea(controlArea);
                                        loadUnitAtID.setDataArea(loadUnitAtIdDA);
                                        loadUnitAtID.setHeight(entry.getValue().height);

                                        Envelope envelope = new Envelope();
                                        envelope.setLoadUnitAtID(loadUnitAtID);
                                        XMLUtil.sendEnvelope(envelope);
                                    }
                                }
                            } else {

                            }
                        }

                    } else if (StationMode.RETRIEVAL.equals(station.getMode()) && "0".equals(entry.getValue().Load)) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block1.getMcKey());
                        block1.setMcKey(null);
                        //创建ControlArea控制域对象
                        ControlArea ca = new ControlArea();
                        Sender sd = new Sender();
                        sd.setDivision(XMLConstant.COM_DIVISION);
                        ca.setSender(sd);
                        RefId ri = new RefId();
                        ri.setReferenceId(asrsJob.getMcKey());
                        ca.setRefId(ri);
                        ca.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        //创建MovementReportDA数据域对象
                        MovementReportDA mrd = new MovementReportDA();
                        mrd.setReasonCode(ReasonCode.RETRIEVALFINISHED);
                        StUnit stUnit = new StUnit();
                        stUnit.setStUnitID(asrsJob.getBarcode());
                        mrd.setStUnit(stUnit);
                        Location location = Location.getByLocationNo(asrsJob.getFromLocation());
                        FromLocation fromLocation = new FromLocation();
                        fromLocation.setRack(asrsJob.getFromLocation());
                        fromLocation.setMHA(asrsJob.getFromStation());
                        fromLocation.setX(String.valueOf(location.getBank()));
                        fromLocation.setY(String.valueOf(location.getBay()));
                        fromLocation.setZ(String.valueOf(location.getLevel()));
                        mrd.setFromLocation(fromLocation);
                        //创建MovementReport响应核心对象
                        MovementReport mr = new MovementReport();
                        mr.setControlArea(ca);
                        mr.setDataArea(mrd);
                        //将MovementReport发送给wms
                        Envelope el = new Envelope();
                        el.setMovementReport(mr);
                        XMLUtil.sendEnvelope(el);
                        session.delete(asrsJob);
                    }
                }
            }
//            boolean isLoad = false;
//            for (Map.Entry<String, Message50.Block> entry : message50.MachineNos.entrySet()) {
//                String blockNo = entry.getKey();
//                Message50.Block block = entry.getValue();
//                BlockData bd = (BlockData) session.get(BlockData.class, blockNo);
//                Map<Integer, BlockDataDetail> mapp = new HashMap<Integer, BlockDataDetail>();
//                if (block.Load.equals("1"))
//                    isLoad = true;
//                if (bd != null) {
//                    Collection<BlockDataDetail> bddList = bd.getBlockDataDetails();
//                    for (BlockDataDetail bdd : bddList) {
//                        mapp.put(bdd.getSerialNo(), bdd);
//                    }
//                } else {
//                    bd = new BlockData();
//                    bd.setBlockNo(blockNo);
//                }
//                bd.setLoad(isLoad);
//                session.saveOrUpdate(bd);
//                for (Map.Entry<Integer, Map<String, String>> map : block.McKeysAndBarcodes.entrySet()) {
//                    int serialNo = map.getKey();
//                    BlockDataDetail bdd = mapp.get(serialNo);
//                    if (bdd == null) {
//                        bdd = new BlockDataDetail();
//                        bdd.setSerialNo(serialNo);
//                        bdd.setBlockData(bd);
//                    }
//                    for (Map.Entry<String, String> mcBar : map.getValue().entrySet()) {
//                        bdd.setMcKey(mcBar.getKey());
//                        bdd.setBarCode(mcBar.getValue());
//                    }
//                    session.saveOrUpdate(bdd);
//                }
//            }
            Transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            Transaction.rollback();
        }
    }

    public static void main(String[] args) throws Exception {
        Message50 m50 = new Message50("10003100010000000000020");
        Msg50Proc pro = new Msg50Proc();
        pro.Do(m50);
    }

}


