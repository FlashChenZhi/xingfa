package com.AllBinding.threads;

import com.AllBinding.blocks.Block;
import com.AllBinding.blocks.Crane;
import com.AllBinding.blocks.SCar;
import com.AllBinding.utils.BlockStatus;
import com.AllBinding.utils.MsgSender;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.StatusDetail;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message03;
import com.util.common.LogWriter;
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
//                Message26Proc(crane);
                if (!crane.getStatus().equals(BlockStatus.STATUS_RUN)) {
                    System.out.println(String.format("Crane %s status is not run", crane.getBlockNo()));
                } else if (crane.isWaitingResponse()) {
//                    System.out.println(String.format("MCar %s is waitingResponse", crane.getBlockNo()));
                } else {
                    if (StringUtils.isBlank(crane.getReservedMcKey()) && StringUtils.isBlank(crane.getMcKey())) {
                        //出库任务
                        AsrsJob asrsJob = (AsrsJob) session.createQuery("select aj from AsrsJob aj,SCar sc where aj.mcKey=sc.reservedMcKey and " +
                                "aj.fromStation=:blockNo ").setString("blockNo", crane.getBlockNo()).uniqueResult();
                        if (asrsJob != null) {
//                            crane.setReservedMcKey(asrsJob.getMcKey());
                            if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                                HibernateUtil.getCurrentSession().createQuery("update Crane set reservedMcKey=:mckey where blockNo=:blockNo").setParameter("blockNo", crane.getBlockNo())
                                        .setParameter("mckey", asrsJob.getMcKey()).executeUpdate();
                            } else if (asrsJob.getType().equals(AsrsJobType.RECHARGE_OVER)) {
                                if (StringUtils.isEmpty(crane.getsCarNo())) {
                                    //堆垛机上有子车，领取任务
                                    HibernateUtil.getCurrentSession().createQuery("update Crane set reservedMcKey=:mckey where blockNo=:blockNo").setParameter("blockNo", crane.getBlockNo())
                                            .setParameter("mckey", asrsJob.getMcKey()).executeUpdate();
                                }

                            } else {
                                if (StringUtils.isNotEmpty(crane.getsCarNo())) {
                                    //堆垛机上有子车，领取任务
                                    HibernateUtil.getCurrentSession().createQuery("update Crane set reservedMcKey=:mckey where blockNo=:blockNo").setParameter("blockNo", crane.getBlockNo())
                                            .setParameter("mckey", asrsJob.getMcKey()).executeUpdate();
                                }
                            }


                        } else {
                            //入库任务
                            asrsJob = (AsrsJob) session.createQuery("select aj from AsrsJob aj ,StationBlock sb where " +
                                    "aj.mcKey=sb.mcKey and aj.toStation=:blockNo order by aj.id asc ").setString("blockNo", crane.getBlockNo())
                                    .uniqueResult();

//                            asrsJob = (AsrsJob) session.createQuery("select aj from ２AsrsJob aj where aj.statusDetail=:statusDetail and aj.toStation=:blockNo" +
//                                    " order by aj.generateTime").setString("statusDetail", StatusDetail.START).setString("blockNo", crane.getBlockNo())
//                                    .setMaxResults(1).uniqueResult();

                            //有小车
                            if (StringUtils.isNotBlank(crane.getsCarNo())) {
                                SCar sCar = (SCar) Block.getByBlockNo(crane.getsCarNo());
                                //小车上有货，卸货
                                if (StringUtils.isNotBlank(sCar.getMcKey())) {
                                    AsrsJob asrsJob1 = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
                                    Location toLocation = Location.getByLocationNo(asrsJob1.getToLocation());
                                    if (crane.getBay() == toLocation.getBay() && crane.getLevel() == toLocation.getLevel())
                                        MsgSender.send03(Message03._CycleOrder.unloadCar, sCar.getMcKey(), crane, asrsJob1.getToLocation(), sCar.getBlockNo(), "", "");
                                    else
                                        MsgSender.send03(Message03._CycleOrder.move, sCar.getMcKey(), crane, asrsJob1.getToLocation(), sCar.getBlockNo(), "", "");

                                } else {
                                    //小车上无货，找任务
                                    if (asrsJob != null) {
//                                        crane.setReservedMcKey(asrsJob.getMcKey());
                                        HibernateUtil.getCurrentSession().createQuery("update Crane set reservedMcKey=:mckey where blockNo=:blockNo").setParameter("blockNo", crane.getBlockNo())
                                                .setParameter("mckey", asrsJob.getMcKey()).executeUpdate();
                                    } else {
                                        //找充电任务
                                        asrsJob = (AsrsJob) session.createQuery("select aj from AsrsJob aj where " +
                                                "  aj.toStation=:blockNo and aj.type=:ajtype order by aj.id asc ").setString("blockNo", crane.getBlockNo())
                                                .setString("ajtype", AsrsJobType.RECHARGE).uniqueResult();
                                        if (asrsJob != null) {
                                            HibernateUtil.getCurrentSession().createQuery("update Crane set reservedMcKey=:mckey where blockNo=:blockNo").setParameter("blockNo", crane.getBlockNo())
                                                    .setParameter("mckey", asrsJob.getMcKey()).executeUpdate();
                                        }

                                    }
                                }
                            } else {
                                //无小车，找任务
                                if (asrsJob != null) {
//                                    crane.setReservedMcKey(asrsJob.getMcKey());
                                    Location toLocation = Location.getByLocationNo(asrsJob.getToLocation());
                                    SCar sCar = (SCar) Block.getByBlockNo("SC01");
                                    if(toLocation.getBay() == sCar.getBay()){
                                        HibernateUtil.getCurrentSession().createQuery("update Crane set reservedMcKey=:mckey where blockNo=:blockNo").setParameter("blockNo", crane.getBlockNo())
                                                .setParameter("mckey", asrsJob.getMcKey()).executeUpdate();
                                    }else{
                                        if (sCar.getStatus().equals(BlockStatus.STATUS_RUN) && StringUtils.isEmpty(sCar.getMcKey()) && StringUtils.isEmpty(sCar.getReservedMcKey())){
                                            MsgSender.send03(Message03._CycleOrder.loadCar, "9999", crane, "", sCar.getBlockNo(), "", "");
                                            Thread.sleep(1000);
                                            MsgSender.send03(Message03._CycleOrder.onCar, "9999", sCar, "", crane.getBlockNo(), "", "");

                                        }
                                    }

                                } else {
                                    //没有任务，子车上车
                                    SCar sCar = (SCar) session.createCriteria(SCar.class).add(Restrictions.eq("bindingCrane", true)).uniqueResult();
                                    if (StringUtils.isBlank(sCar.getMcKey())) {
                                        AsrsJob asrsJob1 = (AsrsJob) session.createQuery("from AsrsJob aj where aj.toStation=:blockNo and " +
                                                "aj.statusDetail=:statusDetail").setString("blockNo", crane.getBlockNo())
                                                .setString("statusDetail", StatusDetail.DONE).uniqueResult();
                                        if (asrsJob1 != null) {
                                            MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob1.getMcKey(), crane, "", sCar.getBlockNo(), "", "");
                                        } else {
                                            if (sCar.getStatus().equals(BlockStatus.STATUS_RUN) && StringUtils.isEmpty(sCar.getMcKey()) && StringUtils.isEmpty(sCar.getReservedMcKey())){
                                                MsgSender.send03(Message03._CycleOrder.loadCar, "9999", crane, "", sCar.getBlockNo(), "", "");
                                                Thread.sleep(1000);
                                                MsgSender.send03(Message03._CycleOrder.onCar, "9999", sCar, "", crane.getBlockNo(), "", "");

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (StringUtils.isNotBlank(crane.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(crane.getMcKey());
                        if (AsrsJobType.RETRIEVAL.equals(asrsJob.getType())) {
                            Block block = crane.getNextBlock(asrsJob.getFromStation(), asrsJob.getToStation());
                            if (crane.getBay() == 2 && crane.getLevel() == 0) {
                                //堆垛机载货在站台口，移载卸货，移载取货
                                if (StringUtils.isBlank(block.getMcKey()) && !block.isWaitingResponse()) {
                                    MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, crane.getMcKey(), crane, "", block.getBlockNo(), "", "");
                                    MsgSender.send03(Message03._CycleOrder.moveCarryGoods, crane.getMcKey(), block, "", crane.getBlockNo(), "", "");
                                }
                            } else {
                                //堆垛机不在站台口
                                if (StringUtils.isNotBlank(crane.getsCarNo())) {
                                    //堆垛机上有小车
                                    SCar sCar = (SCar) Block.getByBlockNo(crane.getsCarNo());
                                    if (StringUtils.isNotBlank(sCar.getReservedMcKey())) {
                                        Location newLocaiton = Location.getByLocationNo(asrsJob.getFromLocation());
                                        Location location = Location.getByLocationNo(AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey()).getFromLocation());
                                        if (location.getBay() == sCar.getBay() && location.getLevel() == sCar.getLevel() && location.getBank() != 9
                                                && location.getBank() != 8 && newLocaiton.getOrientation().equals(location.getOrientation())) {
                                            //下次还出当前层、列的货物，卸子车，货位8，9是巷道口两边的货位，如果子车的作业是在巷道口的，那么下个巷道需要重新移动，
                                            MsgSender.send03(Message03._CycleOrder.unloadCar, sCar.getReservedMcKey(), crane, location.getLocationNo(), crane.getsCarNo(), "", "");
                                        } else {
                                            //下次不出当前层、列的货物，不卸子车,移动到站台口
                                            MsgSender.send03(Message03._CycleOrder.move, crane.getMcKey(), crane, "", block.getBlockNo(), "2", "0");
                                        }
                                    } else {
                                        //没有当前层、列的出库任务，不卸子车,移动到站台口
                                        MsgSender.send03(Message03._CycleOrder.move, crane.getMcKey(), crane, "", block.getBlockNo(), "2", "0");
                                    }
                                } else {
                                    //堆垛机上没有小车
                                    MsgSender.send03(Message03._CycleOrder.move, crane.getMcKey(), crane, "", block.getBlockNo(), "2", "0");
                                }
                            }
                        } else if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {
                            if (StringUtils.isBlank(crane.getsCarNo())) {
                                Location location = Location.getByLocationNo(asrsJob.getToLocation());
                                if (crane.getBay() != location.getBay() || crane.getLevel() != location.getLevel()) {
                                    //堆垛机上无子车，且堆垛机在站台口，移动到目标层、列
                                    MsgSender.send03(Message03._CycleOrder.move, crane.getMcKey(), crane, location.getLocationNo(), "", "", "");
                                } else {
                                    //堆垛机在移动到目标层、列，接子车
                                    SCar sCar = (SCar) session.createCriteria(SCar.class).add(Restrictions.eq("reservedMcKey", crane.getMcKey())).uniqueResult();
                                    if (sCar.getBay() == crane.getBay() && sCar.getLevel() == crane.getLevel()) {
                                        MsgSender.send03(Message03._CycleOrder.loadCar, crane.getMcKey(), crane, location.getLocationNo(), sCar.getBlockNo(), "", "");
                                    }
                                }
                            } else {
                                //堆垛机上有子车，且堆垛机在站台口，堆垛机带子车、货物移动到目标层、列
                                Location location = Location.getByLocationNo(asrsJob.getToLocation());
                                MsgSender.send03(Message03._CycleOrder.move, crane.getMcKey(), crane, location.getLocationNo(), "", "", "");
                            }
                        }
                    } else if (StringUtils.isNotBlank(crane.getReservedMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(crane.getReservedMcKey());
                        if (AsrsJobType.RETRIEVAL.equals(asrsJob.getType())) {
                            Location location = Location.getByLocationNo(asrsJob.getFromLocation());
                            if (location.getBay() == crane.getBay() && location.getLevel() == crane.getLevel()) {
                                if (StringUtils.isNotBlank(crane.getsCarNo())) {
                                    //堆垛机在目标层、列,且子车在堆垛机上，卸子车
                                    MsgSender.send03(Message03._CycleOrder.unloadCar, crane.getReservedMcKey(), crane, location.getLocationNo(), crane.getsCarNo(), "", "");
                                } else {
                                    //堆垛机在目标层、列，子车不在堆垛机上，接子车
                                    SCar sCar = (SCar) session.createCriteria(SCar.class).add(Restrictions.eq("reservedMcKey", crane.getReservedMcKey())).uniqueResult();
                                    MsgSender.send03(Message03._CycleOrder.loadCar, crane.getReservedMcKey(), crane, location.getLocationNo(), sCar.getBlockNo(), "", "");
                                }
                            } else {
                                //堆垛机不在目标bay，移动到目标bay  包含有子车 无子车两种情况
                                MsgSender.send03(Message03._CycleOrder.move, crane.getReservedMcKey(), crane, location.getLocationNo(), "", "", "");
                            }
                        } else if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {
                            if (crane.getBay() == 1 && crane.getLevel() == 0) {
                                //堆垛机在站台口，station移栽接货
                            } else {
//                                Block block = (Block) session.createCriteria(Block.class).add(Restrictions.eq("mcKey", crane.getReservedMcKey())).uniqueResult();
                                //查找本block的上一个block地址
                                Block block = crane.getPreBlock(asrsJob.getFromStation(), asrsJob.getToStation());
                                if (StringUtils.isBlank(crane.getsCarNo())) {
                                    //子车不在堆垛机上
                                    SCar sCar = (SCar) session.createCriteria(SCar.class).uniqueResult();
                                    Location location = Location.getByLocationNo(asrsJob.getToLocation());
                                    if (StringUtils.isNotBlank(sCar.getMcKey())) {
                                        AsrsJob sCarJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
                                        Location sCarLocation = Location.getByLocationNo(sCarJob.getToLocation());
                                        if (crane.getBay() == location.getBay() && crane.getLevel() == location.getLevel() && sCarLocation.getBank() != 8
                                                && sCarLocation.getBank() != 9 && sCarLocation.getOrientation().equals(location.getOrientation())) {
                                            //若这次入库位置仍为当前层、列，堆垛机去取货，小车不上车
                                            MsgSender.send03(Message03._CycleOrder.move, crane.getReservedMcKey(), crane, "", block.getBlockNo(), "1", "0");
                                        } else {
                                            // 若这次入库位置不是当前层、列，小车卸完货后上车
                                            MsgSender.send03(Message03._CycleOrder.loadCar, crane.getReservedMcKey(), crane, location.getLocationNo(), sCar.getBlockNo(), "", "");
                                        }
                                    } else {
                                        //若这次入库位置仍为当前层、列，堆垛机去取货，小车不上车
                                        MsgSender.send03(Message03._CycleOrder.move, crane.getReservedMcKey(), crane, "", block.getBlockNo(), "1", "0");
                                    }
                                } else {
                                    //子车在堆垛机上 前往站台
                                    MsgSender.send03(Message03._CycleOrder.move, crane.getReservedMcKey(), crane, "", block.getBlockNo(), "1", "0");
                                }
                            }
                        } else if (AsrsJobType.RECHARGE.equals(asrsJob.getType())) {

                            Location location = Location.getByLocationNo(asrsJob.getToLocation());
                            //母车上有子车
                            if (StringUtils.isNotEmpty(crane.getsCarNo())) {
                                if (crane.getBay() == location.getBay() && crane.getLevel() == location.getLevel()) {
                                    MsgSender.send03(Message03._CycleOrder.unloadCar, crane.getReservedMcKey(), crane, location.getLocationNo(), crane.getsCarNo(), "", "");
                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, crane.getReservedMcKey(), crane, location.getLocationNo(), "", "", "");
                                }
                            }

                        } else if (AsrsJobType.RECHARGE_OVER.equals(asrsJob.getType())) {
                            SCar sCar = (SCar) HibernateUtil.getCurrentSession().createQuery("from SCar ").setMaxResults(1).uniqueResult();
                            if (StringUtils.isNotEmpty(sCar.getMcKey())) {
                                //子车有mckey，子车到巷道口了。接车
                                if (crane.getBay() == sCar.getBay() && crane.getLevel() == sCar.getLevel()) {
                                    MsgSender.send03(Message03._CycleOrder.loadCar, crane.getReservedMcKey(), crane, asrsJob.getFromLocation(), sCar.getBlockNo(), "", "");
                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, crane.getReservedMcKey(), crane, "120101", "", "", "");

                                }

                            }

                        }
                    }
                }
                Transaction.commit();
                Thread.sleep(300);
            } catch (Exception e) {
                Transaction.rollback();
                LogWriter.writeError(CraneThread.class, e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
