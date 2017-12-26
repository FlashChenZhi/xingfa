package com.test.threads;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.RouteDetail;
import com.asrs.message.Message03;
import com.test.blocks.*;
import com.test.utils.MsgSender;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */
public class LiftThread extends BlockThread<Lift> {

    public LiftThread(String blockNo) {
        super(blockNo);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();
                Lift lift = getBlock();
                if (lift.isWaitingResponse()) {
                    System.out.println(String.format("Lift %s is waitingResponse", lift.getBlockNo()));
                } else {
                    if (StringUtils.isBlank(lift.getReservedMcKey()) && StringUtils.isBlank(lift.getMcKey())) {
                        List<AsrsJob> asrsJobs = session.createQuery("select aj from AsrsJob aj,Dock d where aj.mcKey=d.mcKey and aj.type=:type order by aj.generateTime").setString("type", AsrsJobType.RETRIEVAL).list();

                        Query q = session.createQuery("from RouteDetail rd where rd.nextBlockNo = :blockNo and rd.route.type = :putaway")
                                .setString("blockNo", lift.getBlockNo())
                                .setString("putaway", "01");
                        List<RouteDetail> routeDetails = q.list();

                        List<String> mcKeys = new ArrayList<String>();

                        for (RouteDetail routeDetail : routeDetails) {
                            Block block = Block.getByBlockNo(routeDetail.getCurrentBlockNo());
                            if (StringUtils.isNotBlank(block.getMcKey())) {
                                mcKeys.add(block.getMcKey());
                            }
                        }

                        AsrsJob asrsJob = null;
                        if (!mcKeys.isEmpty()) {
                            asrsJob = (AsrsJob) session.createQuery("from AsrsJob aj where aj.mcKey in (:mcKeys) and aj.type=:type order by aj.generateTime")
                                    .setString("type", AsrsJobType.PUTAWAY)
                                    .setParameterList("mcKeys", mcKeys)
                                    .setMaxResults(1)
                                    .uniqueResult();
                        }


                        if (asrsJobs.isEmpty()) {
                            if (asrsJob != null) {
                                lift.setReservedMcKey(asrsJob.getMcKey());
                            } else {
                                //没有任务要做
                            }
                        } else {
                            if (asrsJob != null) {
                                if (asrsJobs.get(0).getGenerateTime().compareTo(asrsJob.getGenerateTime()) > 0) {
                                    lift.setReservedMcKey(asrsJob.getMcKey());
                                } else {
                                    lift.setReservedMcKey(asrsJobs.get(0).getMcKey());
                                }
                            } else {
                                lift.setReservedMcKey(asrsJobs.get(0).getMcKey());
                            }
                        }
                    } else if (StringUtils.isNotBlank(lift.getReservedMcKey())) {
                        Block block = (Block) session.createCriteria(Block.class).add(Restrictions.eq("mcKey", lift.getReservedMcKey())).uniqueResult();
                        if (block instanceof Dock) {
                            Dock dock = (Dock) block;
                            if (lift.getLevel() == dock.getLevel()) {
                                //dock移栽卸货
                            } else {
                                MsgSender.send03(Message03._CycleOrder.move, lift.getReservedMcKey(), lift, "", dock.getBlockNo(), "", "");
                            }
                        } else if (block instanceof StationBlock || block instanceof Conveyor) {
                            if (lift.getLevel() == 0) {
                                //stationBlock移栽卸货
                            } else {
                                MsgSender.send03(Message03._CycleOrder.move, lift.getReservedMcKey(), lift, "", block.getBlockNo(), "", "");
                            }
                        } else if (block instanceof MCar) {
                            MCar mCar = (MCar) block;
                            if (lift.getLevel() == mCar.getLevel()) {
                                //mCar移栽卸货
                            } else {
                                MsgSender.send03(Message03._CycleOrder.move, lift.getReservedMcKey(), lift, "", mCar.getBlockNo(), "", "");
                            }
                        }
                    } else if (StringUtils.isNotBlank(lift.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(lift.getMcKey());
                        if (AsrsJobType.RETRIEVAL.equals(asrsJob.getType())) {
                            Block nextBlock = lift.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                            if (nextBlock instanceof StationBlock) {
                                if (lift.getLevel() == 0) {
                                    if (!nextBlock.isWaitingResponse() && !nextBlock.isLoaded()) {
                                        MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, lift.getMcKey(), lift, "", nextBlock.getBlockNo(), "", "");
                                        MsgSender.send03(Message03._CycleOrder.moveCarryGoods, lift.getMcKey(), nextBlock, "", lift.getBlockNo(), "", "");
                                    } else {
                                        //等待可以移栽
                                    }
                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, lift.getReservedMcKey(), lift, "", nextBlock.getBlockNo(), "", "");
                                }
                            }
                        } else if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {
                            Block nextBlock = lift.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                            if (nextBlock instanceof Dock) {
                                Dock dock = (Dock) nextBlock;
                                if (lift.getLevel() == dock.getLevel()) {
                                    if (!nextBlock.isWaitingResponse() && !nextBlock.isLoaded()) {
                                        MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, lift.getMcKey(), lift, "", nextBlock.getBlockNo(), "", "");
                                        MsgSender.send03(Message03._CycleOrder.moveCarryGoods, lift.getMcKey(), nextBlock, "", lift.getBlockNo(), "", "");
                                    } else {
                                        //等待可以移栽
                                    }
                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, lift.getMcKey(), lift, "", dock.getBlockNo(), "", "");
                                }
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
