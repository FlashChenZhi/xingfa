package com.AllBinding.threads;

import com.AllBinding.blocks.Block;
import com.AllBinding.blocks.Crane;
import com.AllBinding.blocks.SCar;
import com.AllBinding.utils.BlockStatus;
import com.AllBinding.utils.MsgSender;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.StatusDetail;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message03;
import com.thoughtworks.xstream.mapper.Mapper;
import com.util.common.LogWriter;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Created by Administrator on 2016/12/12.
 */
public class SCarThread extends BlockThread<SCar> {

    public SCarThread(String blockNo) {
        super(blockNo);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();
                SCar sCar = getBlock();
                if (!sCar.getStatus().equals(BlockStatus.STATUS_RUN) && !sCar.getStatus().equals(BlockStatus.STATUS_CHARING)) {
                    System.out.println(String.format("SCar %s status is not run", sCar.getBlockNo()));
                } else if (sCar.isWaitingResponse()) {
//                    System.out.println(String.format("SCar %s is waitingResponse", sCar.getBlockNo()));
                } else {

                    Message26Proc(sCar);

                    if (StringUtils.isBlank(sCar.getReservedMcKey()) && StringUtils.isBlank(sCar.getMcKey())) {

                        if (sCar.getStatus().equals(BlockStatus.STATUS_RUN)) {
                            if (StringUtils.isNotBlank(sCar.getOnCarNo())) {
                                //出库任务
//                            AsrsJob asrsJob = (AsrsJob) session.createQuery("select aj from AsrsJob aj,Crane c where " +
//                                    "aj.fromStation=c.blockNo and aj.statusDetail=:statusDetail order by aj.generateTime")
//                                    .setString("statusDetail", StatusDetail.WAITING).setMaxResults(1).uniqueResult();
                                AsrsJob asrsJob = (AsrsJob) session.createQuery("select aj from AsrsJob aj,Crane c,Location l where l.locationNo = aj.fromLocation and " +
                                        "aj.fromStation=c.blockNo and aj.statusDetail=:statusDetail and aj.status<>'3' order by l.seq desc ")
                                        .setString("statusDetail", StatusDetail.WAITING).setMaxResults(1).uniqueResult();


                                if (asrsJob != null) {
//                                sCar.setReservedMcKey(asrsJob.getMcKey());
                                    HibernateUtil.getCurrentSession().createQuery("update SCar set reservedMcKey=:mckey where blockNo=:blockNo").setParameter("blockNo", sCar.getBlockNo())
                                            .setParameter("mckey", asrsJob.getMcKey()).executeUpdate();

                                } else {
                                    //没有出库任务
                                }


                            } else {
                                //入库任务
                                AsrsJob asrsJob = null;
                                Block block;
                                asrsJob = (AsrsJob) session.createQuery("select aj from AsrsJob aj,Crane c where " +
                                        "aj.toStation=c.blockNo and (aj.mcKey=c.mcKey or aj.mcKey=c.reservedMcKey)").uniqueResult();
                                block = (Block) session.createCriteria(Crane.class).uniqueResult();

                                if (asrsJob != null) {
//                                sCar.setReservedMcKey(asrsJob.getMcKey());
                                    HibernateUtil.getCurrentSession().createQuery("update SCar set reservedMcKey=:mckey where blockNo=:blockNo").setParameter("blockNo", sCar.getBlockNo())
                                            .setParameter("mckey", asrsJob.getMcKey()).executeUpdate();
                                } else {
                                    AsrsJob asrsJob1 = (AsrsJob) session.createQuery("from AsrsJob aj where aj.toStation=:blockNo and " +
                                            "aj.statusDetail=:statusDetail").setString("blockNo", block.getBlockNo())
                                            .setString("statusDetail", StatusDetail.DONE).uniqueResult();
                                    //没有入库任务，子车上车
                                    if (asrsJob1 != null)
                                        MsgSender.send03(Message03._CycleOrder.onCar, asrsJob1.getMcKey(), sCar, "", block.getBlockNo(), "", "");
                                }
                            }
                        } else {

                            AsrsJob asrsJob = (AsrsJob) session.createQuery("select aj from AsrsJob aj,Crane c,Location l where l.locationNo = aj.fromLocation and " +
                                    "aj.fromStation=c.blockNo and aj.statusDetail=:statusDetail and aj.status<>'8' and aj.type='08' order by l.seq desc ")
                                    .setString("statusDetail", StatusDetail.WAITING).setMaxResults(1).uniqueResult();

                            if (asrsJob != null) {
//                                sCar.setReservedMcKey(asrsJob.getMcKey());
                                HibernateUtil.getCurrentSession().createQuery("update SCar set reservedMcKey=:mckey where blockNo=:blockNo").setParameter("blockNo", sCar.getBlockNo())
                                        .setParameter("mckey", asrsJob.getMcKey()).executeUpdate();

                            } else {
                                //没有出库任务
                            }


                        }


                    } else if (StringUtils.isNotBlank(sCar.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
                        if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {
                            //入库子车载荷，子母车在目标bay，子车卸货，母车卸车
                            MsgSender.send03(Message03._CycleOrder.unloadGoods, sCar.getMcKey(), sCar, asrsJob.getToLocation(), "", "", "");
                        } else if (AsrsJobType.RECHARGE_OVER.equals(asrsJob.getType())) {
                            //子车mckey,子车充电完成到巷道口
                            MsgSender.send03(Message03._CycleOrder.onCar, sCar.getMcKey(), sCar, "", "ML01", "", "");
                        } else if (AsrsJobType.RECHARGE.equals(asrsJob.getType())) {
                            //子车有mckey，发充电命令
                            MsgSender.send03(Message03._CycleOrder.recharege, sCar.getMcKey(), sCar, asrsJob.getToLocation(), "", "", "");
                        }
                    } else if (StringUtils.isNotBlank(sCar.getReservedMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                        if (AsrsJobType.RETRIEVAL.equals(asrsJob.getType())) {
                            Location location = Location.getByLocationNo(asrsJob.getFromLocation());
                            if (location.getBay() == sCar.getBay() && location.getLevel() == sCar.getLevel()) {
                                if (StringUtils.isNotBlank(sCar.getOnCarNo())) {
                                    //子车在目标货位，且在母车上，子车下车
                                    MsgSender.send03(Message03._CycleOrder.offCar, sCar.getReservedMcKey(), sCar, location.getLocationNo(), sCar.getOnCarNo(), "", "");
                                } else {
                                    //子车在目标货位，且不在母车上，子车取货
                                    MsgSender.send03(Message03._CycleOrder.pickUpGoods, sCar.getReservedMcKey(), sCar, asrsJob.getFromLocation(), "", "", "");
                                }
                            } else {
                                //母车/堆垛机带子车移动到目标货位
                            }
                        } else if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {
                            Location location = Location.getByLocationNo(asrsJob.getToLocation());

                            if (StringUtils.isBlank(sCar.getOnCarNo())) {
                                Block block = Block.getByBlockNo(asrsJob.getToStation());
                                //子车不在目标货位，子车不在木车上，子车上车
                                MsgSender.send03(Message03._CycleOrder.onCar, sCar.getReservedMcKey(), sCar, "", block.getBlockNo(), "", "");

                            } else {

                            }
                        } else if (AsrsJobType.RECHARGE.equals(asrsJob.getType())) {
                            //子车有reservedmckey，子车在堆垛机上
                            Location location = Location.getByLocationNo(asrsJob.getToLocation());
                            if (location.getBay() == sCar.getBay() && location.getLevel() == sCar.getLevel()) {
                                if (StringUtils.isNotBlank(sCar.getOnCarNo())) {
                                    //子车在目标货位，且在母车上，子车下车
                                    MsgSender.send03(Message03._CycleOrder.offCar, sCar.getReservedMcKey(), sCar, location.getLocationNo(), sCar.getOnCarNo(), "", "");
                                } else {

                                }
                            } else {
                                //母车/堆垛机带子车移动到目标货位
                            }
                        } else if (AsrsJobType.RECHARGE_OVER.equals(asrsJob.getType())) {
                            //子车和堆垛机上有reservedmckey，直接发充电完成
                            Crane block = (Crane) Block.getByBlockNo("ML01");
                            if (StringUtils.isNotEmpty(block.getReservedMcKey())) {
                                MsgSender.send03(Message03._CycleOrder.rechargeOver, sCar.getReservedMcKey(), sCar, asrsJob.getFromLocation(), "", "", "");
                            }

                        }
                    }
                }
                Transaction.commit();
                Thread.sleep(300);
            } catch (Exception e) {
                Transaction.rollback();
                LogWriter.writeError(SCarThread.class, e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
