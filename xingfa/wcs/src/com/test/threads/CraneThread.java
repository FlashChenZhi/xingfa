package com.test.threads;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message03;
import com.test.blocks.Block;
import com.test.blocks.Crane;
import com.test.blocks.SCar;
import com.test.utils.MsgSender;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Created by Administrator on 2016/12/16.
 */
public class CraneThread extends BlockThread<Crane> {
    public CraneThread(String blockNo) {
        super(blockNo);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();
                Crane crane = getBlock();
                if (crane.isWaitingResponse()) {
                    System.out.println(String.format("crane %s waiting for response", crane.getBlockNo()));
                } else {
                    if (StringUtils.isBlank(crane.getReservedMcKey()) && StringUtils.isBlank(crane.getMcKey())) {
                        if (!crane.isCarExist()) {
                            AsrsJob asrsJob = (AsrsJob) session.createQuery("select aj from AsrsJob aj,SCar sc where aj.mcKey=sc.reservedMcKey" +
                                    " and aj.fromStation=:blockNo and aj.type=:type order by aj.generateTime").setString("blockNo", crane.getBlockNo())
                                    .setString("type", AsrsJobType.RETRIEVAL).setMaxResults(1).uniqueResult();
                            if (asrsJob != null) {
                                crane.setReservedMcKey(asrsJob.getMcKey());
                            } else {
                                asrsJob = (AsrsJob) session.createQuery("select aj from AsrsJob aj,StationBlock sb where aj.mcKey=sb.mcKey and aj.toStation=:blockNo and aj.type=:type ")
                                        .setString("type", AsrsJobType.PUTAWAY).setString("blockNo", crane.getBlockNo()).uniqueResult();
                                if (asrsJob != null) {
                                    crane.setReservedMcKey(asrsJob.getMcKey());
                                } else {
                                    //没有任务要做
                                }
                            }
                        } else {
                            SCar sCar = (SCar) Block.getByBlockNo(crane.getsCarBlockNo());
                            MsgSender.send03(Message03._CycleOrder.unloadCar, sCar.getMcKey(), crane, "", sCar.getBlockNo(), "", "");
                        }
                    } else if (StringUtils.isNotBlank(crane.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(crane.getMcKey());
                        if (AsrsJobType.RETRIEVAL.equals(asrsJob.getType())) {
                            if (crane.getBay() == 1 && crane.getLevel() == 0) {
                                Block nextBlock = crane.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                                if (!nextBlock.isLoaded() && !nextBlock.isWaitingResponse()) {
                                    MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, crane.getMcKey(), crane, "", nextBlock.getBlockNo(), "", "");
                                    MsgSender.send03(Message03._CycleOrder.moveCarryGoods, crane.getMcKey(), nextBlock, "", crane.getBlockNo(), "", "");
                                }
                            } else {
                                if (crane.isCarExist()) {
                                    MsgSender.send03(Message03._CycleOrder.unloadCar, crane.getMcKey(), crane, "", crane.getsCarBlockNo(), "", "");
                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, crane.getMcKey(), crane, "", "", "0", "0");

                                }
                            }
                        } else if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {
                            Location location = Location.getByLocationNo(asrsJob.getToLocation());
                            if (crane.isCarExist()) {
                                //若bay已是目标bay，小车上来，mcKey移栽到小车上
                                MsgSender.send03(Message03._CycleOrder.move, crane.getMcKey(), crane, "", "", String.valueOf(location.getBay()), "");
                            } else {
                                SCar sCar = (SCar) session.createCriteria(SCar.class).add(Restrictions.eq("reservedMcKey", crane.getMcKey())).uniqueResult();
                                if (sCar.getBay() == crane.getBay() && location.getLevel() == crane.getLevel()) {
                                    MsgSender.send03(Message03._CycleOrder.loadCar, crane.getMcKey(), crane, location.getLocationNo(), sCar.getBlockNo(), "", "");
                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, crane.getMcKey(), crane, "", "", String.valueOf(sCar.getBay()), String.valueOf(location.getLevel()));
                                }
                            }
                        }
                    } else if (StringUtils.isNotBlank(crane.getReservedMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(crane.getReservedMcKey());
                        if (AsrsJobType.RETRIEVAL.equals(asrsJob.getType())) {
                            Location location = Location.getByLocationNo(asrsJob.getFromLocation());
                            SCar sCar = (SCar) session.createCriteria(SCar.class).add(Restrictions.eq("reservedMcKey", crane.getReservedMcKey())).uniqueResult();
                            if (sCar != null) {
                                if (crane.isCarExist()) {
                                    if (crane.getBay() == location.getBay()) {
                                        MsgSender.send03(Message03._CycleOrder.unloadCar, crane.getReservedMcKey(), crane, "", sCar.getBlockNo(), "", "");
                                    } else {
                                        MsgSender.send03(Message03._CycleOrder.move, crane.getReservedMcKey(), crane, "", "", String.valueOf(location.getBay()), "");
                                    }
                                } else {
                                    if (sCar.getBay() == crane.getBay() && location.getLevel() == crane.getLevel()) {
                                        MsgSender.send03(Message03._CycleOrder.loadCar, crane.getReservedMcKey(), crane, location.getLocationNo(), sCar.getBlockNo(), "", "");
                                    } else {
                                        MsgSender.send03(Message03._CycleOrder.move, crane.getReservedMcKey(), crane, "", "", String.valueOf(sCar.getBay()), String.valueOf(location.getBay()));
                                    }
                                }
                            }
                        } else if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {
                            if (crane.getLevel() == 0 && crane.getBay() == 0) {
                                //移栽接货
                            } else {
                                MsgSender.send03(Message03._CycleOrder.move, crane.getReservedMcKey(), crane, "", "", "0", "0");
                            }
                        }
                    }
                }
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                e.printStackTrace();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
