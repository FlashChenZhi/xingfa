package com.test.threads;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message03;
import com.test.blocks.Block;
import com.test.blocks.MCar;
import com.test.blocks.SCar;
import com.test.utils.MsgSender;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Created by Administrator on 2016/11/1.
 */
public class MCarThread extends BlockThread<MCar> {
    public MCarThread(String blockNo) {
        super(blockNo);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();
                MCar mCar = getBlock();
                if (mCar.isWaitingResponse()) {
                    System.out.println(String.format("MCar %s is waitingResponse", mCar.getBlockNo()));
                } else {
                    if (StringUtils.isBlank(mCar.getReservedMcKey()) && StringUtils.isBlank(mCar.getMcKey())) {
                        if (!mCar.isCarExist()) {
                            AsrsJob asrsJob = (AsrsJob) session.createQuery("select aj from AsrsJob aj,SCar sc where aj.mcKey=sc.reservedMcKey" +
                                    " and sc.level=:level and aj.type=:type and aj.fromStation=:blockNo")
                                    .setInteger("level", mCar.getLevel()).setString("type", AsrsJobType.RETRIEVAL)
                                    .setString("blockNo", mCar.getBlockNo()).uniqueResult();
                            if (asrsJob != null) {
                                mCar.setReservedMcKey(asrsJob.getMcKey());
                            } else {
                                asrsJob = (AsrsJob) session.createQuery("select aj from AsrsJob aj,Dock d where aj.mcKey=d.mcKey and d.level=:level and aj.type=:type ")
                                        .setString("type", AsrsJobType.PUTAWAY).setInteger("level", mCar.getLevel()).uniqueResult();
                                if (asrsJob != null) {
                                    mCar.setReservedMcKey(asrsJob.getMcKey());
                                } else {
                                    //没有任务要做
                                }
                            }
                        } else {
                            SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                            MsgSender.send03(Message03._CycleOrder.unloadCar, sCar.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                        }
                    } else if (StringUtils.isNotBlank(mCar.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
                        if (AsrsJobType.RETRIEVAL.equals(asrsJob.getType())) {
                            if (mCar.isLiftSide()) {
                                //移栽取货，已在卸货
                                Block nextBlock = mCar.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                                if (!nextBlock.isLoaded() && !nextBlock.isWaitingResponse()) {
                                    MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mCar.getMcKey(), mCar, "", nextBlock.getBlockNo(), "", "");
                                    MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mCar.getMcKey(), nextBlock, "", mCar.getBlockNo(), "", "");
                                }
                            } else {
                                if (mCar.isCarExist()) {
                                    MsgSender.send03(Message03._CycleOrder.unloadCar, mCar.getMcKey(), mCar, "", mCar.getsCarBlockNo(), "", "");
                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, mCar.getMcKey(), mCar, "", mCar.getDock(), "", "");

                                }
                            }
                        } else if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {
                            if (mCar.isCarExist()) {
                                //若bay已是目标bay，小车上来，mcKey移栽到小车上
                                Location location = Location.getByLocationNo(asrsJob.getToLocation());
                                MsgSender.send03(Message03._CycleOrder.move, mCar.getMcKey(), mCar, "", "", String.valueOf(location.getBay()), "");
                            } else {
                                SCar sCar = (SCar) session.createCriteria(SCar.class).add(Restrictions.eq("reservedMcKey", mCar.getMcKey())).uniqueResult();
                                if (sCar.getBay() == mCar.getBay()) {
                                    MsgSender.send03(Message03._CycleOrder.loadCar, mCar.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, mCar.getMcKey(), mCar, "", "", String.valueOf(sCar.getBay()), "");
                                }
                            }
                        }
                    } else if (StringUtils.isNotBlank(mCar.getReservedMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
                        if (AsrsJobType.RETRIEVAL.equals(asrsJob.getType())) {
                            Location location = Location.getByLocationNo(asrsJob.getFromLocation());
                            SCar sCar = (SCar) session.createCriteria(SCar.class).add(Restrictions.eq("reservedMcKey", mCar.getReservedMcKey())).uniqueResult();
                            if (sCar != null) {
                                if (mCar.isCarExist()) {
                                    if (mCar.getBay() == location.getBay()) {
                                        MsgSender.send03(Message03._CycleOrder.unloadCar, mCar.getReservedMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                    } else {
                                        MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, "", "", String.valueOf(location.getBay()), "");
                                    }
                                } else {
                                    if (sCar.getBay() == mCar.getBay()) {
                                        MsgSender.send03(Message03._CycleOrder.loadCar, mCar.getReservedMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                    } else {
                                        MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, "", "", String.valueOf(sCar.getBay()), "");
                                    }
                                }
                            }
                        } else if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {
                            if (mCar.isLiftSide()) {
                                //移栽接货
                            } else {
                                MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, "", mCar.getDock(), "", "");
                            }
                        }
                    }
                }
                Transaction.commit();
            } catch (Exception ex) {
                Transaction.rollback();
                ex.printStackTrace();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
