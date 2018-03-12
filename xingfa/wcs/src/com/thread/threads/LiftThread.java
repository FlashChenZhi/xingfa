package com.thread.threads;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.RouteDetail;
import com.asrs.message.Message03;
import com.thread.blocks.*;
import com.thread.utils.MsgSender;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

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

                } else if(lift.getStatus().equals("2")){

                }else {
                    if (StringUtils.isBlank(lift.getReservedMcKey()) && StringUtils.isBlank(lift.getMcKey())) {


//                        Query query = HibernateUtil.getCurrentSession().createQuery("from RouteDetail r,Block b,AsrsJob a where r.currentBlockNo = b.blockNo and " +
//                                " b.mcKey = a.mcKey and r.nextBlockNo=:nB order by a.id asc").setParameter("nB", lift.getBlockNo());
                        Query query = HibernateUtil.getCurrentSession().createQuery("select r from RouteDetail r,Block b,AsrsJob a where r.currentBlockNo = b.blockNo and " +
                                " b.mcKey = a.mcKey and r.nextBlockNo=:nB order by a.id asc").setParameter("nB", lift.getBlockNo());
                        List<RouteDetail> details = query.list();
                        boolean hasjob = false;
                        for (RouteDetail detail : details) {
                            Block block = Block.getByBlockNo(detail.getCurrentBlockNo());
                            if (StringUtils.isNotEmpty(block.getMcKey()) || StringUtils.isNotEmpty(block.getReservedMcKey())) {
                                String mckey = StringUtils.isNotEmpty(block.getMcKey()) ? block.getMcKey() : block.getReservedMcKey();
                                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
                                if (asrsJob != null && detail.getRoute().getType().equals(asrsJob.getType())
                                        && detail.getRoute().getFromStation().equals(asrsJob.getFromStation())
                                        && detail.getRoute().getToStation().equals(asrsJob.getToStation())) {
                                    if (asrsJob.getType().equals(AsrsJobType.RECHARGED)) {
                                        if (block instanceof MCar) {
                                            HibernateUtil.getCurrentSession().createQuery("update Lift set reservedMcKey=:mckey where blockNo=:blockNo").setParameter("blockNo", lift.getBlockNo())
                                                    .setParameter("mckey", block.getMcKey()).executeUpdate();
                                            hasjob = true;
                                            break;
                                        }
                                    } else if (asrsJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                                        if (block instanceof Conveyor) {
                                            HibernateUtil.getCurrentSession().createQuery("update Lift set reservedMcKey=:mckey where blockNo=:blockNo").setParameter("blockNo", lift.getBlockNo())
                                                    .setParameter("mckey", block.getMcKey()).executeUpdate();
                                            hasjob = true;
                                            break;
                                        }
                                    } else {

                                        Query jq = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type =:tp1 or type =:tp2");
                                        jq.setParameter("tp1", AsrsJobType.RECHARGED);
                                        jq.setParameter("tp2", AsrsJobType.RECHARGEDOVER);
                                        List<AsrsJob> jobs = jq.list();

                                        if (!jobs.isEmpty()) {
                                            Transaction.commit();
                                            Thread.sleep(500);
                                            break;
                                        }

                                        Conveyor block1 = (Conveyor) Block.getByBlockNo("0003");
                                        if (StringUtils.isNotEmpty(block1.getOnCar())) {
                                            Transaction.commit();
                                            Thread.sleep(500);
                                            break;
                                        }

                                        HibernateUtil.getCurrentSession().createQuery("update Lift set reservedMcKey=:mckey where blockNo=:blockNo").setParameter("blockNo", lift.getBlockNo())
                                                .setParameter("mckey", block.getMcKey()).executeUpdate();
                                        hasjob = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!hasjob) {
                            if (lift.getLevel() != 1) {
                                Query dockQ = HibernateUtil.getCurrentSession().createQuery("from Dock where liftNo=:liftNo and level=1");
                                dockQ.setParameter("liftNo", lift.getBlockNo());
                                dockQ.setMaxResults(1);
                                Dock dock = (Dock) dockQ.uniqueResult();
                                MsgSender.send03(Message03._CycleOrder.move, "9999", lift, "", dock.getDockNo(), "", "");
                            }
                        }

                    } else if (StringUtils.isNotBlank(lift.getReservedMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(lift.getReservedMcKey());
                        if (AsrsJobType.RECHARGED.equals(asrsJob.getType())) {

                            MCar mCar = (MCar) lift.getPreBlock(asrsJob.getMcKey(), asrsJob.getType());
                            if (mCar.getLevel() == lift.getLevel()
                                    && mCar.getDock().equals(mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()))
                                    && StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
                                MsgSender.send03(Message03._CycleOrder.loadCar, lift.getReservedMcKey(), lift, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                            } else {
                                if (mCar.getLevel() != lift.getLevel()) {
                                    MsgSender.send03(Message03._CycleOrder.move, lift.getReservedMcKey(), lift, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                }
                            }

                        } else {

                            Block block = (Block) session.createCriteria(Block.class).add(Restrictions.eq("mcKey", lift.getReservedMcKey())).setMaxResults(1).uniqueResult();
                            if (block instanceof MCar) {
                                MCar dock = (MCar) block;
                                String dock1 = dock.getDock(dock.getBlockNo(), lift.getBlockNo());
                                if (lift.getLevel() == dock.getLevel() || (lift.getLevel() == 0 && dock.getLevel() == 1) && dock.getDock().equals(dock1)) {
                                    //dock移栽卸货
                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, lift.getReservedMcKey(), lift, "", dock.getDock(dock.getBlockNo(), lift.getBlockNo()), "", "");
                                }
                            } else if (block instanceof Conveyor) {
                                Conveyor conveyor = (Conveyor) block;
                                if (lift.getLevel() == 1) {

                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, lift.getReservedMcKey(), lift, "", conveyor.getDock(), "", "");

                                }
                            }

                        }

                    } else if (StringUtils.isNotBlank(lift.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(lift.getMcKey());
                        if (AsrsJobType.RETRIEVAL.equals(asrsJob.getType())) {
                            Block nextBlock = lift.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                            if (nextBlock instanceof StationBlock) {
                                if (lift.getLevel() == 1) {
                                    if (!nextBlock.isWaitingResponse()) {
                                        MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, lift.getMcKey(), lift, "", nextBlock.getBlockNo(), "", "");
                                        Thread.sleep(200);
                                        MsgSender.send03(Message03._CycleOrder.moveCarryGoods, lift.getMcKey(), nextBlock, "", lift.getBlockNo(), "", "");
                                    } else {
                                        //等待可以移栽
                                    }
                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, lift.getReservedMcKey(), lift, "", nextBlock.getBlockNo(), "", "");
                                }
                            } else if (nextBlock instanceof Conveyor) {
                                if (lift.getLevel() == 1) {
                                    if (!nextBlock.isWaitingResponse() && StringUtils.isEmpty(nextBlock.getMcKey())) {
                                        MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, lift.getMcKey(), lift, "", nextBlock.getBlockNo(), "", "");
                                        Thread.sleep(200);
                                        MsgSender.send03(Message03._CycleOrder.moveCarryGoods, lift.getMcKey(), nextBlock, "", lift.getBlockNo(), "", "");
                                    } else {
                                        //等待可以移栽
                                    }

                                } else {
                                    Conveyor conveyor = (Conveyor) nextBlock;
                                    MsgSender.send03(Message03._CycleOrder.move, lift.getMcKey(), lift, "", conveyor.getDock(), "", "");
                                }
                            }
                        } else if (AsrsJobType.PUTAWAY.equals(asrsJob.getType()) || AsrsJobType.COUNT.equals(asrsJob.getType())) {

                            Block nextBlock = lift.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                            if (nextBlock instanceof MCar) {
                                MCar dock = (MCar) nextBlock;
                                String dock1 = dock.getDock(dock.getBlockNo(), lift.getBlockNo());
                                if (lift.getLevel() == dock.getLevel() || (lift.getLevel() == 0 && dock.getLevel() == 1) && dock.getDock().equals(dock1)) {
                                    if (!nextBlock.isWaitingResponse()
                                            && StringUtils.isNotEmpty(dock.getReservedMcKey())
                                            && dock1.equals(dock.getDock())) {
                                        MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, lift.getMcKey(), lift, "", nextBlock.getBlockNo(), "", "");
                                        MsgSender.send03(Message03._CycleOrder.moveCarryGoods, lift.getMcKey(), nextBlock, "", lift.getBlockNo(), "", "");
                                    } else {
                                        //等待可以移栽

                                    }
                                } else {
                                    if (lift.getLevel() == dock.getLevel() || (lift.getLevel() == 0 && dock.getLevel() == 1)) {
                                        // 提升机不移动
                                    } else {
                                        MsgSender.send03(Message03._CycleOrder.move, lift.getMcKey(), lift, "", dock1, "", "");
                                    }
                                }
                            }
                        } else if (AsrsJobType.RECHARGEDOVER.equals(asrsJob.getType())) {
                            Block nextBlock = lift.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                            if (nextBlock instanceof MCar) {
                                MCar mCar = (MCar) nextBlock;
                                if (lift.getLevel() == mCar.getLevel()
                                        && mCar.getDock().equals(mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()))) {
                                    MsgSender.send03(Message03._CycleOrder.unloadCar, lift.getMcKey(), lift, "", mCar.getDock(), "", "");
                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), lift, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");

                                }
                            }
                        } else if (AsrsJobType.RECHARGED.equals(asrsJob.getType())) {
                            Block nextBlock = lift.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                            if (nextBlock instanceof Conveyor) {
                                if (lift.getLevel() == 1) {
                                    if (!nextBlock.isWaitingResponse()) {
                                        MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), lift, "", "0101", "", "");
                                        MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), nextBlock, "", lift.getOnCar(), "", "");
                                    }
                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), lift, "", "0101", "", "");

                                }
                            }
                        } else if (AsrsJobType.CHANGELEVEL.equals(asrsJob.getType())) {
                            Block nextBlock = lift.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                            if (nextBlock instanceof MCar) {
                                MCar mCar = (MCar) nextBlock;
                                if (lift.getLevel() == mCar.getLevel()) {
                                    if (mCar.isLiftSide()) {
                                        if (StringUtils.isNotEmpty(mCar.getDock()))
                                            MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), lift, "", mCar.getDock(), "", "");
                                    }
                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, lift.getMcKey(), lift, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
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
