package com.AllBinding;

import com.AllBinding.blocks.*;
import com.AllBinding.utils.BlockStatus;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.ReasonCode;
import com.asrs.business.consts.StatusDetail;
import com.asrs.communication.MessageProxy;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.domain.Msg03;
import com.asrs.message.Message05;
import com.asrs.message.Message35;
import com.asrs.message.MsgException;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.RefId;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.domain.XMLbean.XMLList.DataArea.StUnit;
import com.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.domain.XMLbean.XMLList.MovementReport;
import com.domain.consts.xmlbean.XMLConstant;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */
public class Msg35 {

    public static void proc(Message35 message35) {
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(message35.McKey);
            Block block = Block.getByBlockNo(message35.MachineNo);

            Query query35 = session.createQuery("from Msg03 where msgType='35' and mcKey=:mckey and dock=:dock")
                    .setParameter("dock", message35.Dock).setParameter("mckey", message35.McKey);
            List<Msg03> msg35 = query35.list();
            if (msg35.isEmpty()) {
                //mckey和dock 查找35消息，没有找到，新消息，处理，否则不处理，直接回复 05

                Msg03 msg03 = new Msg03();

                msg03.setPlcName(message35.getPlcName());
                msg03.setJobType(message35.JobType);
                msg03.setMachineNo(message35.MachineNo);
                msg03.setMcKey(message35.McKey);
                msg03.setCycleOrder(message35.CycleOrder);
                msg03.setHeight(message35.Height);
                msg03.setWidth(message35.Width);
                msg03.setStation(message35.Station);
                msg03.setBank(message35.Bank);
                msg03.setBay(message35.Bay);
                msg03.setLevel(message35.Level);
                msg03.setDock(message35.Dock);
                msg03.setLastSendDate(new Date());
                msg03.setCreateDate(new Date());
                msg03.setReceived(true);
                msg03.setMsgType("35");

                HibernateUtil.getCurrentSession().save(msg03);


                if (asrsJob != null) {
                    if (block instanceof StationBlock) {
                        if (Message35._CycleOrder.moveUnloadGoods.equals(message35.CycleOrder)) {
                            block.setMcKey(null);
                            Block nextBlock = Block.getByBlockNo(message35.Station);
                            nextBlock.setMcKey(message35.McKey);
                            nextBlock.setReservedMcKey(null);
                        }
                    } else if (block instanceof Dock) {
                        if (Message35._CycleOrder.moveUnloadGoods.equals(message35.CycleOrder)) {
                            block.setMcKey(null);
                            Block nextBlock = Block.getByBlockNo(message35.Station);
                            nextBlock.setMcKey(message35.McKey);
                            nextBlock.setReservedMcKey(null);
                        }
                    } else if (block instanceof Crane) {
                        Crane crane = (Crane) block;
                        if (Message35._CycleOrder.moveUnloadGoods.equals(message35.CycleOrder)) {
                            crane.setMcKey(null);
                            Block nextBlock = Block.getByBlockNo(message35.Station);
                            nextBlock.setMcKey(message35.McKey);
                        } else if (Message35._CycleOrder.loadCar.equals(message35.CycleOrder)) {
                            crane.setsCarNo(message35.Station);
                            SCar sCar = (SCar) Block.getByBlockNo(message35.Station);
                            sCar.setOnCarNo(crane.getBlockNo());
                            if (AsrsJobType.RETRIEVAL.equals(message35.JobType)) {
                                crane.setMcKey(message35.McKey);
                                crane.setReservedMcKey(null);
                                sCar.setReservedMcKey(null);
//                            AsrsJob asrsJob1 = (AsrsJob) session.createQuery("select aj from AsrsJob aj,Crane c where aj.mcKey!=:mcKey and " +
//                                    "aj.fromStation=c.blockNo and aj.statusDetail=:statusDetail order by aj.generateTime").setString("mcKey", message35.McKey)
//                                    .setString("statusDetail", StatusDetail.WAITING).setMaxResults(1).uniqueResult();

                                AsrsJob asrsJob1 = (AsrsJob) session.createQuery("select aj from AsrsJob aj,Crane c,Location l where l.locationNo=aj.fromLocation and aj.mcKey!=:mcKey and " +
                                        "aj.fromStation=c.blockNo and aj.statusDetail=:statusDetail order by l.seq desc").setString("mcKey", message35.McKey)
                                        .setString("statusDetail", StatusDetail.WAITING).setMaxResults(1).uniqueResult();

                                if (asrsJob1 != null) {
                                    sCar.setReservedMcKey(asrsJob1.getMcKey());
                                }
                            } else if (AsrsJobType.PUTAWAY.equals(message35.JobType) && StringUtils.isNotBlank(crane.getMcKey())) {
                                asrsJob.setStatusDetail(StatusDetail.NEARDONE);
                                crane.setMcKey(null);
                                sCar.setReservedMcKey(null);
                                sCar.setMcKey(message35.McKey);
                            } else if (AsrsJobType.RECHARGE_OVER.equals(message35.JobType)) {
                                //充电完成接车
                                crane.setMcKey(null);
                                crane.setReservedMcKey(null);
                                sCar.setStatus(BlockStatus.STATUS_RUN);
                                sCar.setMcKey(null);
                                sCar.setReservedMcKey(null);
                                if (!asrsJob.getStatus().equals(AsrsJobStatus.DONE)) {
                                    asrsJob.setStatus(AsrsJobStatus.DONE);
                                } else {
                                    session.delete(asrsJob);
                                }
                            }

                        } else if (Message35._CycleOrder.unloadCar.equals(message35.CycleOrder)) {
                            crane.setsCarNo(null);
                            SCar sCar = (SCar) Block.getByBlockNo(message35.Station);
                            sCar.setOnCarNo(null);
                            if (AsrsJobType.RECHARGE.equals(asrsJob.getType())) {
                                crane.setMcKey(null);
                                crane.setReservedMcKey(null);
                            }
                        } else if (Message35._CycleOrder.move.equals(message35.CycleOrder)) {
                            crane.setBay(Integer.parseInt(message35.Bay));
                            crane.setLevel(Integer.parseInt(message35.Level));
                            if (StringUtils.isNotBlank(crane.getsCarNo())) {
                                SCar sCar = (SCar) Block.getByBlockNo(crane.getsCarNo());
                                sCar.setBay(Integer.parseInt(message35.Bay));
                                sCar.setLevel(Integer.parseInt(message35.Level));
                                if (AsrsJobType.PUTAWAY.equals(asrsJob.getType()) && StringUtils.isNotBlank(crane.getMcKey())) {
                                    asrsJob.setStatusDetail(StatusDetail.NEARDONE);
                                    crane.setMcKey(null);
                                    sCar.setReservedMcKey(null);
                                    sCar.setMcKey(message35.McKey);
                                }
                            }
                        }
                    } else if (block instanceof SCar) {
                        SCar sCar = (SCar) block;
                        if (Message35._CycleOrder.pickUpGoods.equals(message35.CycleOrder)) {
                            asrsJob.setStatusDetail(StatusDetail.START);
                        } else if (Message35._CycleOrder.onCar.equals(message35.CycleOrder)) {
                            Crane crane = (Crane) Block.getByBlockNo(message35.Station);
                            sCar.setOnCarNo(crane.getBlockNo());
                            crane.setsCarNo(sCar.getBlockNo());
                            if (StatusDetail.DONE.equals(asrsJob.getStatusDetail())) {
                                session.delete(asrsJob);
                                sendTransportOrder(message35, asrsJob);
                            }
                        } else if (Message35._CycleOrder.unloadGoods.equals(message35.CycleOrder)) {
                            sCar.setMcKey(null);
                            List<AsrsJob> asrsJobs = session.createQuery("from AsrsJob aj where aj.toStation=:blockNo and aj.mcKey!=:mcKey")
                                    .setString("blockNo", asrsJob.getToStation()).setString("mcKey", message35.McKey).list();
                            if (asrsJobs.isEmpty()) {
                                asrsJob.setStatusDetail(StatusDetail.DONE);
                            } else {
                                session.delete(asrsJob);
                                sendTransportOrder(message35, asrsJob);
                            }
                        } else if (Message35._CycleOrder.offCar.equals(message35.CycleOrder)) {
                            sCar.setOnCarNo(null);
                            if (AsrsJobType.RECHARGE.equals(asrsJob.getType())) {
                                //充电任务
                                sCar.setMcKey(message35.McKey);
                                sCar.setReservedMcKey(null);
                            }
                        } else if (Message35._CycleOrder.onCar.equals(message35.CycleOrder)) {
                            sCar.setOnCarNo(message35.Station);

                            if (AsrsJobType.RECHARGE_OVER.equals(asrsJob.getType())) {
                                sCar.setStatus(BlockStatus.STATUS_RUN);
                                sCar.setMcKey(null);
                                sCar.setReservedMcKey(null);
                                //充电完成上车
                                if (asrsJob.getStatus().equals(AsrsJobStatus.DONE)) {
                                    session.delete(asrsJob);
                                } else {
                                    asrsJob.setStatus(AsrsJobStatus.DONE);
                                }
                            }
                        } else if (Message35._CycleOrder.recharege.equals(message35.CycleOrder)) {
                            sCar.setStatus(BlockStatus.STATUS_CHARING);//子车充电中
                            sCar.setMcKey(null);
                            session.delete(asrsJob);
                        } else if (Message35._CycleOrder.rechargeOver.equals(message35.CycleOrder)) {
                            sCar.setMcKey(message35.McKey);
                            sCar.setReservedMcKey(null);
                        }
                    }
                } else {
                    if (block instanceof SCar) {
                        SCar sCar = (SCar) block;
                        //子车上车
                        if (Message35._CycleOrder.onCar.equals(message35.CycleOrder)) {
                            sCar.setOnCarNo(message35.Station);
                            Crane crane = (Crane) Block.getByBlockNo(message35.Station);
                            crane.setsCarNo(sCar.getBlockNo());
                        }

                    } else if (block instanceof Crane) {

                        Crane crane = (Crane) block;
                        //堆垛机移动
                        if (Message35._CycleOrder.move.equals(message35.CycleOrder)) {
                            crane.setBay(Integer.parseInt(message35.Bank));
                            crane.setLevel(Integer.parseInt(message35.Level));
                            if (StringUtils.isNotEmpty(crane.getsCarNo())) {
                                SCar sCar = (SCar) Block.getByBlockNo(crane.getsCarNo());
                                sCar.setBay(crane.getBay());
                                sCar.setLevel(crane.getLevel());
                            }
                        } else if (Message35._CycleOrder.loadCar.equals(message35.CycleOrder)) {
                            crane.setsCarNo(message35.Station);
                            SCar sCar = (SCar) Block.getByBlockNo(message35.Station);
                            sCar.setOnCarNo(crane.getBlockNo());
                        }

                    }
                }
                block.setWaitingResponse(false);

            }

            Msg03.clearMsg03(message35.McKey);

            Transaction.commit();
            Message05 message05 = new Message05();
            message05.setPlcName(message35.getPlcName());
            message05.McKey = message35.McKey;
            message05.Response = "0";
            MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
            _wcsproxy.addSndMsg(message05);

        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();

        }

    }

    private static void sendTransportOrder(Message35 message35, AsrsJob asrsJob) throws Exception {
        //入库完成
        Sender sd = new Sender();
        sd.setDivision(XMLConstant.COM_DIVISION);
        RefId ri = new RefId();
        ri.setReferenceId(message35.McKey);
        //创建ControlArea控制域对象
        ControlArea ca = new ControlArea();
        ca.setSender(sd);
        ca.setRefId(ri);
        ca.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        StUnit stUnit = new StUnit();
        stUnit.setStUnitID(asrsJob.getBarcode());
        Location location = Location.getByLocationNo(asrsJob.getToLocation());
        ToLocation toLocation = new ToLocation();
        toLocation.setRack(asrsJob.getToLocation());
        toLocation.setMHA(asrsJob.getFromStation());
        toLocation.setX(String.valueOf(location.getBank()));
        toLocation.setY(String.valueOf(location.getBay()));
        toLocation.setZ(String.valueOf(location.getLevel()));
        //创建MovementReportDA数据域对象
        MovementReportDA mrd = new MovementReportDA();
        mrd.setReasonCode(ReasonCode.PUTAWAYFINISHED);
        mrd.setStUnit(stUnit);
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
}

