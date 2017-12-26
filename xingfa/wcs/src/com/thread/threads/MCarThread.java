package com.thread.threads;

import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message03;
import com.thread.blocks.*;
import com.thread.utils.MsgSender;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;


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
//                Message26Proc(mCar);
                if (mCar.isWaitingResponse()) {

                } else if (!mCar.getStatus().equals("1")) {

                } else {

                    if (StringUtils.isBlank(mCar.getReservedMcKey()) && StringUtils.isBlank(mCar.getMcKey())) {

                        AsrsJob asrsJob = null;

                        //**以下为优化程序***
                        boolean hasRvMckey = false;
                        if (mCar.getPosition().equals(Location.LEFT)) {
                            //左边母车
                            SCar sCar = (SCar) session.createQuery("from SCar s where s.level=:lv").setParameter("lv", mCar.getLevel()).setMaxResults(1).uniqueResult();
                            if (sCar != null) {
                                if (StringUtils.isNotEmpty(sCar.getReservedMcKey())) {
                                    //reservedMckey is not null
                                    AsrsJob job = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                                    //fromLocation is not null
                                    if (job.getFromStation().equals(mCar.getBlockNo())) {
                                        //出库货位和母车在同一个出库区域.子车reservedmckey不为空，还未取货，需要上母车取货，母车需要reservedmckey
                                        if (!job.getType().equals(AsrsJobType.CHANGELEVEL)) {
                                            if (job.getType().equals(AsrsJobType.RECHARGED)) {
                                                if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
                                                    HibernateUtil.getCurrentSession().createQuery("update MCar set reservedMcKey=:mckey where blockNo=:blockNo")
                                                            .setString("mckey", job.getMcKey()).setString("blockNo", mCar.getBlockNo()).executeUpdate();
                                                    hasRvMckey = true;
                                                }
                                            } else {
                                                HibernateUtil.getCurrentSession().createQuery("update MCar set reservedMcKey=:mckey where blockNo=:blockNo")
                                                        .setString("mckey", job.getMcKey()).setString("blockNo", mCar.getBlockNo()).executeUpdate();
                                                hasRvMckey = true;
                                            }
                                        }
                                    } else {
                                        //出库货位在母车的另一边，子车不在母车上，子车刚下母车，通过通道往右边来。
                                        if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
                                            if (!job.getType().equals(AsrsJobType.CHANGELEVEL)) {
                                                HibernateUtil.getCurrentSession().createQuery("update MCar set reservedMcKey=:mckey where blockNo=:blockNo")
                                                        .setString("mckey", job.getMcKey()).setString("blockNo", mCar.getBlockNo()).executeUpdate();
                                                hasRvMckey = true;
                                            }
                                        }
                                    }

                                    if (job.getType().equals(AsrsJobType.CHANGELEVEL)) {
                                        MCar fromMcar = (MCar) Block.getByBlockNo(job.getFromStation());
                                        Lift lift = (Lift) mCar.getNextBlock(job.getType(), job.getToStation());
                                        if (!fromMcar.getBlockNo().equals(mCar.getBlockNo())) {
                                            if (lift == null || StringUtils.isNotEmpty(sCar.getOnMCar()) &&
                                                    (sCar.getOnMCar().equals(mCar.getBlockNo()) || sCar.getOnMCar().equals(lift.getBlockNo()))) {
                                                //子车已经上提升机了,或者子车在木车上。母车没有reservedmckey。其实母车已经卸车完成了
                                                //如果电梯为空，表示不是其实层的母车，是目标层的母车
                                            } else {
                                                HibernateUtil.getCurrentSession().createQuery("update MCar set reservedMcKey=:mckey where blockNo=:blockNo")
                                                        .setString("mckey", job.getMcKey()).setString("blockNo", mCar.getBlockNo()).executeUpdate();
                                                hasRvMckey = true;

                                            }
                                        }
                                    }

                                    //toLocation is not null
                                    if (job.getToStation() != null) {
                                        if (job.getToStation().equals(mCar.getBlockNo())) {
                                            //入库货位区域和母车在一个区域，子车需要上母车入库，母车需要reservedmckey
                                            if (!hasRvMckey) {
                                                if (!job.getType().equals(AsrsJobType.CHANGELEVEL)) {
                                                    HibernateUtil.getCurrentSession().createQuery("update MCar set reservedMcKey=:mckey where blockNo=:blockNo")
                                                            .setString("mckey", job.getMcKey()).setString("blockNo", mCar.getBlockNo()).executeUpdate();
                                                    hasRvMckey = true;
                                                }
                                            }
                                        } else {
                                            //入库货位和母车不在同一个区域，母车reservedmckey
                                            if (!hasRvMckey) {
                                                if (!job.getType().equals(AsrsJobType.CHANGELEVEL) && StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
                                                    HibernateUtil.getCurrentSession().createQuery("update MCar set reservedMcKey=:mckey where blockNo=:blockNo")
                                                            .setString("mckey", job.getMcKey()).setString("blockNo", mCar.getBlockNo()).executeUpdate();
                                                    hasRvMckey = true;
                                                }
                                            }
                                        }
                                    }

                                } else if (StringUtils.isNotEmpty(sCar.getMcKey()) && sCar.getBank() == 15 && sCar.getBay() == Integer.parseInt(Const.TEMP_BAY)) {
                                    //mckey is not null
                                    AsrsJob job = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
                                    //子车上已经载货
                                    if (job.getToStation().equals(mCar.getBlockNo())
                                            && !asrsJob.getType().equals(AsrsJobType.RECHARGED)
                                            && !asrsJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                                        HibernateUtil.getCurrentSession().createQuery("update MCar set reservedMcKey=:mckey where blockNo=:blockNo")
                                                .setString("mckey", job.getMcKey()).setString("blockNo", mCar.getBlockNo()).executeUpdate();
                                        hasRvMckey = true;
                                    }

                                } else {
                                    //reservedmcy && mckey is null

                                }

                            }

                        } else {

                            //右边母车
                            SCar sCar = (SCar) session.createQuery("from SCar s where s.level=:lv").setParameter("lv", mCar.getLevel()).setMaxResults(1).uniqueResult();
                            if (sCar != null) {

                                if (StringUtils.isNotEmpty(sCar.getMcKey()) || StringUtils.isNotEmpty(sCar.getReservedMcKey())) {
                                    String mckey = (StringUtils.isEmpty(sCar.getMcKey()) ? sCar.getReservedMcKey() : sCar.getMcKey());
                                    AsrsJob scarJob = AsrsJob.getAsrsJobByMcKey(mckey);
                                    if (sCar.getPosition().equals(Location.MISS) && scarJob.getType().equals(AsrsJobType.RECHARGED) || scarJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                                        Thread.sleep(500);
                                        hasRvMckey = true;
                                    }
                                }

                                if (StringUtils.isNotEmpty(sCar.getReservedMcKey())) {
                                    AsrsJob aj = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                                    if (StringUtils.isEmpty(mCar.getsCarBlockNo()) && (aj.getType().equals(AsrsJobType.RECHARGED)
                                            || aj.getType().equals(AsrsJobType.CHANGELEVEL))) {
                                        //子车上得任务是充电任务，母车上没有子车，母车不招任务
                                        //子车充电，下了母车进入输送机

                                    } else {
                                        AsrsJob job = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                                        //reservedMckey is not null
                                        if (job.getFromStation().equals(mCar.getBlockNo())) {
                                            //子车没载荷，
                                            HibernateUtil.getCurrentSession().createQuery("update MCar set reservedMcKey=:mckey where blockNo=:blockNo")
                                                    .setString("mckey", job.getMcKey()).setString("blockNo", mCar.getBlockNo()).executeUpdate();
                                            hasRvMckey = true;
                                        } else {
                                            if (aj.getType().equals(AsrsJobType.RETRIEVAL)) {
                                                //母车在左边，子车还没去托盘。
                                                if (!hasRvMckey) {
                                                    HibernateUtil.getCurrentSession().createQuery("update MCar set reservedMcKey=:mckey where blockNo=:blockNo")
                                                            .setString("mckey", job.getMcKey()).setString("blockNo", mCar.getBlockNo()).executeUpdate();
                                                    hasRvMckey = true;
                                                }
                                            }
                                        }

                                        if (job.getToStation().equals(mCar.getBlockNo())) {
                                            if (!hasRvMckey) {
                                                if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
                                                    if (!job.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                                                        HibernateUtil.getCurrentSession().createQuery("update MCar set reservedMcKey=:mckey where blockNo=:blockNo")
                                                                .setString("mckey", job.getMcKey()).setString("blockNo", mCar.getBlockNo()).executeUpdate();
                                                        hasRvMckey = true;
                                                    }
                                                } else {
                                                    HibernateUtil.getCurrentSession().createQuery("update MCar set reservedMcKey=:mckey where blockNo=:blockNo")
                                                            .setString("mckey", job.getMcKey()).setString("blockNo", mCar.getBlockNo()).executeUpdate();
                                                    hasRvMckey = true;

                                                }
                                            }
                                        }
                                    }

                                } else if (StringUtils.isNotEmpty(sCar.getMcKey()) && sCar.getBank() == 15) {
                                    //mckey is not null
                                    //子车有载荷
                                    AsrsJob job = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
                                    if (job.getFromStation().equals(mCar.getBlockNo())) {

                                    }

                                    if (job.getToStation().equals(mCar.getBlockNo())) {
                                        if (!hasRvMckey) {
                                            HibernateUtil.getCurrentSession().createQuery("update MCar set reservedMcKey=:mckey where blockNo=:blockNo")
                                                    .setString("mckey", job.getMcKey()).setString("blockNo", mCar.getBlockNo()).executeUpdate();
                                            hasRvMckey = true;
                                        }
                                    }
                                }

                                if (!hasRvMckey) {
                                    //出库的母车顺序，如果子车上有任务，原站台左边的母车，右边的母车也必须要有mckey任务对应接车，出库
                                    if (StringUtils.isNotEmpty(sCar.getReservedMcKey()) || StringUtils.isNotEmpty(sCar.getMcKey())) {
                                        String mckey = StringUtils.isNotEmpty(sCar.getReservedMcKey()) ? sCar.getReservedMcKey() : sCar.getMcKey();
                                        AsrsJob job = AsrsJob.getAsrsJobByMcKey(mckey);
                                        Block block = Block.getByBlockNo(job.getFromStation());
                                        if (block instanceof MCar) {
                                            MCar mc = (MCar) block;
                                            if (mc.getPosition().equals(Location.LEFT) && !job.getType().equals(AsrsJobType.CHANGELEVEL)) {
                                                //原站台号在左边。
                                                //子车只有reservedmckey，子车有mckey，但是子车载中间通道，或者子车位置和母车不在一边，右边母车都需要reservedmckey
                                                if (StringUtils.isNotEmpty(sCar.getReservedMcKey())
                                                        || !sCar.getPosition().equals(mCar.getPosition())
                                                        || (StringUtils.isNotEmpty(sCar.getMcKey()) && sCar.getBay() == Integer.parseInt(Const.TEMP_BAY) && sCar.getBank() == 15)) {
                                                    if (StringUtils.isNotEmpty(sCar.getOnMCar()) && sCar.getOnMCar().equals(mCar.getBlockNo())) {

                                                    } else {
                                                        if (!sCar.getPosition().equals(Location.MISS)) {
                                                            HibernateUtil.getCurrentSession().createQuery("update MCar set reservedMcKey=:mckey where blockNo=:blockNo")
                                                                    .setString("mckey", job.getMcKey()).setString("blockNo", mCar.getBlockNo()).executeUpdate();
                                                            hasRvMckey = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (!hasRvMckey) {
                                //子车上没有其他的任务
                                //查找入库，电梯口的托盘，准备入库
                                asrsJob = (AsrsJob) session.createQuery("select aj from AsrsJob aj,Lift d,Location l where aj.toLocation = l.locationNo and aj.mcKey=d.mcKey and l.level=:level and aj.type  in(:tp) ")
                                        .setParameterList("tp", new String[]{AsrsJobType.PUTAWAY, AsrsJobType.COUNT}).setInteger("level", mCar.getLevel()).setMaxResults(1).uniqueResult();

                                if (asrsJob != null) {
                                    HibernateUtil.getCurrentSession().createQuery("update MCar set reservedMcKey=:mckey where blockNo=:blockNo")
                                            .setString("mckey", asrsJob.getMcKey()).setString("blockNo", mCar.getBlockNo()).executeUpdate();
                                    hasRvMckey = true;
                                }
                            }

                        }

                        if (!hasRvMckey) {
                            if (StringUtils.isEmpty(mCar.getsCarBlockNo())) {
                                SCar sCar = (SCar) session.createQuery("from SCar s where s.level=:lv").setParameter("lv", mCar.getLevel()).setMaxResults(1).uniqueResult();
                                if (sCar != null && sCar.getPosition().equals(mCar.getPosition())) {
                                    if (sCar.getActualArea().equals(mCar.getActualArea()) && sCar.getBay() == mCar.getBay() && StringUtils.isEmpty(sCar.getMcKey()) && StringUtils.isEmpty(sCar.getReservedMcKey())) {
                                        MsgSender.send03(Message03._CycleOrder.loadCar, "9999", mCar, "", sCar.getBlockNo(), sCar.getBay() + "", "");
                                    } else {
                                        if (StringUtils.isEmpty(sCar.getMcKey()) && StringUtils.isEmpty(sCar.getReservedMcKey())) {
                                            Location location = Location.getByBankBayLevel(sCar.getBank(), sCar.getBay(), sCar.getLevel(),sCar.getPosition());
                                            if (location != null) {
                                                MsgSender.send03(Message03._CycleOrder.move, "9999", mCar, location.getLocationNo(), "", "" + "", "");
                                            }
                                        }
                                    }
                                }
                            } else {
                                Location toLocation = Location.getByLocationNo("00900600" + mCar.getLevel());
                                if (toLocation.getPosition().equals(mCar.getPosition())) {
                                    if (!toLocation.getActualArea().equals(mCar.getActualArea()) || toLocation.getBay() != mCar.getBay()) {
                                        MsgSender.send03(Message03._CycleOrder.move, "9999", mCar, toLocation.getLocationNo(), "", "" + "", "");
                                    }
                                }
                            }

                        }

                    } else if (StringUtils.isNotBlank(mCar.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
                        if (AsrsJobType.RETRIEVAL.equals(asrsJob.getType())) {
                            if (mCar.getPosition().equals(Location.LEFT) && StringUtils.isNotBlank(mCar.getsCarBlockNo())) {

                                SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                if (StringUtils.isNotEmpty(sCar.getOnMCar())) {

                                    Location lc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());

                                    if (mCar.getBay() != Integer.parseInt(Const.TEMP_BAY) || !lc.getActualArea().equals(mCar.getActualArea())) {

                                        MsgSender.send03(Message03._CycleOrder.move, mCar.getMcKey(), mCar, lc.getLocationNo(), "", "", "");

                                    } else {
                                        if (mCar.getCheckLocation() == true) {
                                            MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, lc.getLocationNo(), sCar.getBlockNo(), "", "");
                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, mCar.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                        }
                                    }
                                }
                            } else {
                                Lift block = (Lift) mCar.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                                String mcarDocm = mCar.getDock(mCar.getBlockNo(), block.getBlockNo());
                                if (mCar.isLiftSide() && mcarDocm.equals(mCar.getDock())) {
                                    //移栽取货，已在卸货
                                    Block nextBlock = mCar.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                                    Lift lift = (Lift) nextBlock;
                                    if (!nextBlock.isWaitingResponse()
                                            && (mCar.getLevel() == lift.getLevel() || (mCar.getLevel() == 1 && lift.getLevel() == 0))) {
                                        if (StringUtils.isEmpty(lift.getMcKey())) {
                                            MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mCar.getMcKey(), mCar, "", nextBlock.getBlockNo(), "", "");
                                            Thread.sleep(200);
                                            MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mCar.getMcKey(), nextBlock, "", mCar.getBlockNo(), "", "");
                                        }
                                    } else {
//                                        if (!lift.isWaitingResponse())
//                                            MsgSender.send03(Message03._CycleOrder.move, mCar.getMcKey(), lift, "", mcarDocm, "", "");
                                    }
                                } else {

                                    if (StringUtils.isEmpty(mCar.getsCarBlockNo())) {
                                        Lift lift = (Lift) mCar.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                                        MsgSender.send03(Message03._CycleOrder.move, mCar.getMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                    } else {

                                        SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                        if (StringUtils.isEmpty(sCar.getOnMCar())) {

                                        } else {

                                            if (StringUtils.isNotEmpty(sCar.getReservedMcKey())) {
                                                AsrsJob aj = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                                                if (aj.getType().equals(AsrsJobType.RETRIEVAL)) {
                                                    Location toLocation = Location.getByLocationNo(aj.getFromLocation());
                                                    if (mCar.getPosition().equals(Location.RIGHT) && toLocation.getOutPosition().equals(Location.RIGHT)) {
                                                        if (toLocation.getOutPosition().equals(mCar.getPosition()) && toLocation.getBay() == mCar.getBay() && toLocation.getActualArea().equals(mCar.getActualArea())) {
                                                            //子车上存在新的作业,作业在右边出库区域
                                                            if (mCar.getCheckLocation() == true) {
                                                                MsgSender.send03(Message03._CycleOrder.unloadCar, mCar.getMcKey(), mCar, toLocation.getLocationNo(), mCar.getsCarBlockNo(), "", "");
                                                            } else {
                                                                MsgSender.send03(Message03._CycleOrder.move, mCar.getMcKey(), mCar, toLocation.getLocationNo(), "", "", "");
                                                            }
                                                        } else {
                                                            Lift lift = (Lift) mCar.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                                                            if (!mCar.isLiftSide() || !mcarDocm.equals(mCar.getDock())) {
                                                                MsgSender.send03(Message03._CycleOrder.move, mCar.getMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                                            }
                                                        }
                                                    } else {
                                                        if (mCar.getPosition().equals(Location.RIGHT) && toLocation.getOutPosition().equals(Location.LEFT)) {
                                                            Location lc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());
                                                            if (mCar.getBay() == lc.getBay() && mCar.getActualArea().equals(lc.getActualArea())) {
                                                                //子车上存在新的作业，作业在左边出库区域，母车在固定通道口，发起卸车命令，
                                                                if (mCar.getCheckLocation() == true) {
                                                                    MsgSender.send03(Message03._CycleOrder.unloadCar, mCar.getMcKey(), mCar, lc.getLocationNo(), mCar.getsCarBlockNo(), "", "");
                                                                } else {
                                                                    MsgSender.send03(Message03._CycleOrder.move, mCar.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                                                }
                                                            }else{
                                                                MsgSender.send03(Message03._CycleOrder.move, mCar.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    Lift lift = (Lift) mCar.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                                                    if (!mCar.isLiftSide() || !mcarDocm.equals(mCar.getDock())) {
                                                        MsgSender.send03(Message03._CycleOrder.move, mCar.getMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                                    }
                                                }

                                            } else {
                                                Query query = session.createQuery("from AsrsJob s where s.fromStation=:station and s.status='1' and s.statusDetail=:statusDt").setMaxResults(1);
                                                query.setParameter("statusDt", AsrsJobStatusDetail.WAITING);
                                                query.setParameter("station", mCar.getBlockNo());
                                                AsrsJob newJob = (AsrsJob) query.uniqueResult();
                                                Location oldLocation = Location.getByLocationNo(asrsJob.getFromLocation());
                                                if (newJob != null) {
                                                    Location newLocation = Location.getByLocationNo(newJob.getFromLocation());
                                                    if (newLocation.getActualArea().equals(oldLocation.getActualArea())
                                                            && newLocation.getBay() == oldLocation.getBay()
                                                            && newLocation.getLevel() == oldLocation.getLevel()) {
                                                        if (sCar.getPower() > 30) {
                                                            if (mCar.getCheckLocation() == true) {
                                                                MsgSender.send03(Message03._CycleOrder.unloadCar, mCar.getMcKey(), mCar, newLocation.getLocationNo(), mCar.getsCarBlockNo(), "", "");
                                                            } else {
                                                                MsgSender.send03(Message03._CycleOrder.move, mCar.getMcKey(), mCar, newLocation.getLocationNo(), "", "", "");

                                                            }
                                                        }
                                                    } else {
                                                        Lift lift = (Lift) mCar.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                                                        MsgSender.send03(Message03._CycleOrder.move, mCar.getMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                                    }
                                                } else {
                                                    Lift lift = (Lift) mCar.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                                                    MsgSender.send03(Message03._CycleOrder.move, mCar.getMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                                }

                                            }
                                        }

                                    }
                                }
                            }
                        } else if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {

                            if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                                Location location = Location.getByLocationNo(asrsJob.getToLocation());
                                if (location.getPosition().equals(mCar.getPosition())) {

                                    if (location.getBay() == mCar.getBay() && location.getActualArea().equals(mCar.getActualArea())) {

                                        SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                        if (mCar.getCheckLocation() == true) {
                                            MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, location.getLocationNo(), sCar.getBlockNo(), "", "");
                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, location.getLocationNo(), "", "", "");
                                        }

                                    } else {
//                                        SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
//                                        //母车移动
//                                        if (StringUtils.isNotEmpty(sCar.getOnMCar()))
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, asrsJob.getToLocation(), "", "", "");
                                    }

                                } else {

                                    Location lc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());

                                    if (mCar.getBay() != Integer.parseInt(Const.TEMP_BAY) || !mCar.getActualArea().equals(lc.getActualArea())) {
                                        //移动到指定的通道
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "01500" + Const.TEMP_BAY + "00" + mCar.getLevel(), "", "", "");

                                    } else {

//                                        if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                                        SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                        if (mCar.getCheckLocation() == true) {
                                            MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, lc.getLocationNo(), sCar.getBlockNo(), "", "");
                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                        }

//                                        } else {
//                                            if (mCar.getCheckLocation() == true) {
//                                                Query q = HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:lv and s.position=:po ");
//                                                q.setParameter("lv", mCar.getLevel());
//                                                q.setParameter("po", mCar.getPosition());
//                                                SCar sCar = (SCar) q.uniqueResult();
//                                                MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
//                                            } else {
//                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
//                                            }
//                                        }

                                    }
                                }
                            } else {

                                SCar sCar = (SCar) HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:level").setParameter("level", mCar.getLevel()).uniqueResult();
                                if (sCar.getPosition().equals(mCar.getPosition())) {

                                    if (sCar.getBay() == mCar.getBay() && sCar.getActualArea().equals(mCar.getActualArea())) {
                                        if (mCar.getCheckLocation() == true) {
                                            MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                        } else {
                                            if (sCar.getBank() == 15 && sCar.getBay() == Integer.parseInt(Const.TEMP_BAY)) {
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "01500" + Const.TEMP_BAY + "00" + sCar.getLevel(), "", "", "");
                                            } else {
                                                // TODO: 2017/5/20 此处修改了逻辑
                                                String scLocation = StringUtils.leftPad("" + sCar.getBank(), 3, "0") + StringUtils.leftPad("" + sCar.getBay(), 3, "0") + "00" + sCar.getLevel();
                                                Location lo = Location.getByLocationNo(scLocation);
                                                if (lo != null) {
                                                    MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, scLocation, "", "", "");
                                                }

                                            }
                                        }

                                    } else {
                                        if (sCar.getBank() == 15 && sCar.getBay() == Integer.parseInt(Const.TEMP_BAY)) {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "01500" + Const.TEMP_BAY + "00" + sCar.getLevel(), "", "", "");
                                        } else {
                                            // TODO: 2017/5/20 此处修改了逻辑
                                            String scLocation = StringUtils.leftPad("" + sCar.getBank(), 3, "0") + StringUtils.leftPad("" + sCar.getBay(), 3, "0") + "00" + sCar.getLevel();
                                            Location lo = Location.getByLocationNo(scLocation);
                                            if (lo != null) {
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, scLocation, "", "", "");
                                            }
                                        }
                                    }

                                } else {
                                    Location loc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + sCar.getLevel());

                                    if (mCar.getBay() != Integer.parseInt(Const.TEMP_BAY) || !loc.getActualArea().equals(mCar.getActualArea())) {
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, loc.getLocationNo(), "", "", "");
                                    } else {

                                    }

                                }
                            }
                        } else if (AsrsJobType.COUNT.equals(asrsJob.getType())) {


                            if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                                Location location = Location.getByLocationNo(asrsJob.getToLocation());
                                if (location.getOutPosition().equals(mCar.getPosition())) {

                                    if (location.getBay() == mCar.getBay() && location.getActualArea().equals(mCar.getActualArea())) {

                                        SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                        if (mCar.getCheckLocation() == true) {
                                            MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, location.getLocationNo(), sCar.getBlockNo(), "", "");

                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, location.getLocationNo(), "", "", "");

                                        }

                                    } else {
//                                        SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
//                                        //母车移动
//                                        if (StringUtils.isNotEmpty(sCar.getOnMCar()))
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, asrsJob.getToLocation(), "", "", "");
                                    }

                                } else {

                                    Location lc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());

                                    if (mCar.getBay() != Integer.parseInt(Const.TEMP_BAY) || !mCar.getActualArea().equals(lc.getActualArea())) {
                                        //移动到指定的通道
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");

                                    } else {

//                                        if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                                        SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                        if (mCar.getCheckLocation() == true) {
                                            MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, lc.getLocationNo(), sCar.getBlockNo(), "", "");
                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                        }

//                                        } else {
//                                            Query q = HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:lv and s.position=:po ");
//                                            q.setParameter("lv", mCar.getLevel());
//                                            q.setParameter("po", mCar.getPosition());
//                                            SCar sCar = (SCar) q.uniqueResult();
//                                            MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
//
//                                        }

                                    }
                                }
                            } else {

                                SCar sCar = (SCar) HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:level").setParameter("level", mCar.getLevel()).uniqueResult();
                                if (sCar.getPosition().equals(mCar.getPosition())) {

                                    if (sCar.getBay() == mCar.getBay() && sCar.getActualArea().equals(mCar.getActualArea())) {

                                        if (mCar.getCheckLocation() == true) {
                                            MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                        } else {
                                            if (sCar.getBank() == 15 && sCar.getBay() == Integer.parseInt(Const.TEMP_BAY)) {
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "01500" + Const.TEMP_BAY + "00" + sCar.getLevel(), "", "", "");
                                            } else {
                                                String scLocation = StringUtils.leftPad("" + sCar.getBank(), 3, "0") + StringUtils.leftPad("" + sCar.getBay(), 3, "0") + "00" + sCar.getLevel();
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, scLocation, "", "", "");
                                            }
                                        }

                                    } else {
                                        if (sCar.getBank() == 15 && sCar.getBay() == Integer.parseInt(Const.TEMP_BAY)) {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "01500" + Const.TEMP_BAY + "00" + sCar.getLevel(), "", "", "");
                                        } else {
                                            String scLocation = StringUtils.leftPad("" + sCar.getBank(), 3, "0") + StringUtils.leftPad("" + sCar.getBay(), 3, "0") + "00" + sCar.getLevel();
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, scLocation, "", "", "");
                                        }
                                    }

                                } else {
                                    Location loc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + sCar.getLevel());

                                    if (mCar.getBay() != Integer.parseInt(Const.TEMP_BAY) || !loc.getActualArea().equals(mCar.getActualArea())) {
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, loc.getLocationNo(), "", "", "");
                                    } else {

                                    }

                                }
                            }

                        } else if (AsrsJobType.RECHARGED.equals(asrsJob.getType())) {
                            if (mCar.isLiftSide()) {
                                SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                Lift lift = (Lift) mCar.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                                if (mCar.getLevel() == lift.getLevel()) {
                                    if (mCar.getCheckLocation() == true) {
                                        MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                    } else {
                                        String dock = mCar.getDock(mCar.getBlockNo(), lift.getBlockNo());
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "", dock, "", "");
                                    }

                                }
                            }
                        } else if (AsrsJobType.CHANGELEVEL.equals(asrsJob.getType())) {

                            SCar sCar = (SCar) HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:level and status = '1'").setParameter("level", mCar.getLevel()).uniqueResult();

                            if (sCar != null && sCar.getLevel() == mCar.getLevel()) {
                                if (mCar.getPosition().equals(Location.LEFT)) {
                                    if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                                        Lift lift = (Lift) mCar.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                                        if (mCar.isLiftSide()) {
                                            if (lift.getLevel() == mCar.getLevel()) {
                                                if (mCar.getCheckLocation() == true) {
                                                    MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                                } else {
                                                    MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                                }
                                            } else {
                                                if (!lift.isWaitingResponse()) {
                                                    MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), lift, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                                }
                                            }
                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                        }
                                    }
                                } else {
                                    if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
                                        Location location = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());
                                        if (mCar.getBay() == Integer.parseInt(Const.TEMP_BAY) && mCar.getActualArea().equals(location.getActualArea())) {
                                            if (mCar.getCheckLocation() == true) {
                                                MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, location.getLocationNo(), sCar.getBlockNo(), "", "");
                                            } else {
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, location.getLocationNo(), "", "", "");
                                            }

                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, location.getLocationNo(), "", "", "");
                                        }
                                    } else {

                                    }
                                }
                            }
                        } else if (AsrsJobType.ST2ST.equals(asrsJob.getType())) {
                            if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
                                Location toLocation = Location.getByLocationNo(asrsJob.getToLocation());
                                SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                if (mCar.getPosition().equals(toLocation.getPosition())) {
                                    //当前母车和入库货位在同一区域
                                    if (mCar.getBay() != toLocation.getBay() || !mCar.getActualArea().equals(toLocation.getActualArea())) {
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, asrsJob.getToLocation(), "", "", "");
                                    } else {
                                        if (mCar.getCheckLocation() == true) {
                                            MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, toLocation.getLocationNo(), sCar.getBlockNo(), "", "");
                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, asrsJob.getToLocation(), "", "", "");
                                        }
                                    }
                                } else {
                                    //当前母车和入库货位不在同一区域
                                    Location temLoc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());
                                    if (temLoc.getBay() != mCar.getBay() || !mCar.getActualArea().equals(temLoc.getActualArea())) {
                                        if (StringUtils.isNotBlank(sCar.getMcKey())) {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, temLoc.getLocationNo(), "", "", "");
                                        }
                                    } else {
                                        if (mCar.getCheckLocation() == true) {
                                            MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, temLoc.getLocationNo(), sCar.getBlockNo(), "", "");
                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, temLoc.getLocationNo(), "", "", "");
                                        }

                                    }
                                }
                            }
                        } else if (AsrsJobType.BACK_PUTAWAY.equals(asrsJob.getType())) {
                            Location toLocation = Location.getByLocationNo(asrsJob.getToLocation());
                            //移库回库
                            if (mCar.getBlockNo().equals(asrsJob.getToStation())) {
                                //母车和作业的目标站台一致
                                if (toLocation.getBay() == mCar.getBay() && toLocation.getActualArea().equals(mCar.getActualArea())) {
                                    if (mCar.getCheckLocation() == true) {
                                        MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, toLocation.getLocationNo(), mCar.getsCarBlockNo(), "", "");
                                    } else {
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, toLocation.getLocationNo(), "", "", "");
                                    }
                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, toLocation.getLocationNo(), "", "", "");
                                }

                            } else {
                                //和目标站台不一致
                                Location temLoc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());
                                if (mCar.getBay() == temLoc.getBay() && mCar.getActualArea().equals(temLoc.getActualArea())) {
                                    SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                    if (StringUtils.isNotEmpty(sCar.getMcKey())) {
                                        if (mCar.getCheckLocation() == true) {
                                            MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, temLoc.getLocationNo(), mCar.getsCarBlockNo(), "", "");
                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, temLoc.getLocationNo(), "", "", "");
                                        }

                                    }
                                } else {
                                    MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, temLoc.getLocationNo(), "", "", "");
                                }

                            }


                        }

                    } else if (StringUtils.isNotBlank(mCar.getReservedMcKey())) {

                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
                        if (AsrsJobType.RETRIEVAL.equals(asrsJob.getType())) {
                            Location location = Location.getByLocationNo(asrsJob.getFromLocation());
//                            SCar sCar = (SCar) session.createCriteria(SCar.class).add(Restrictions.eq("reservedMcKey", mCar.getReservedMcKey())).uniqueResult();

                            Query q = session.createQuery("from SCar s where s.mcKey=:mck or s.reservedMcKey=:mck").setParameter("mck", mCar.getReservedMcKey());
                            SCar sCar = (SCar) q.uniqueResult();

                            if (!location.getOutPosition().equals(mCar.getPosition())) {

                                Location mvLoca = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + sCar.getLevel());
                                //母车和要出库的货位不在一个区域
                                if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                                    //母车上有子车
                                    if (mCar.getBay() != Integer.parseInt(Const.TEMP_BAY) || !mCar.getActualArea().equals(mvLoca.getActualArea())) {
                                        //移动到通道上去
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, mvLoca.getLocationNo(), "", "", "");

                                    } else {
                                        if (mCar.getCheckLocation() == true) {
                                            MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, mvLoca.getLocationNo(), sCar.getBlockNo(), "", "");
                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, mvLoca.getLocationNo(), "", "", "");
                                        }

                                    }
                                } else {

                                    if (mCar.getBay() != Integer.parseInt(Const.TEMP_BAY) || !mCar.getActualArea().equals(mvLoca.getActualArea())) {

                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, mvLoca.getLocationNo(), "", "", "");

                                    } else {
                                        if (sCar.getPosition().equals(Location.RIGHT)) {
                                            //子车在通道中
                                            if (mCar.getCheckLocation() == true) {
                                                MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                            } else {
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, mvLoca.getLocationNo(), "", "", "");
                                            }

                                        }
                                    }
                                }

                            } else {

                                //母车和要出库的货位在同个区域
                                if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                                    SCar loadScar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                    if (StringUtils.isNotEmpty(loadScar.getOnMCar())) {
                                        Location loc = Location.getByLocationNo(asrsJob.getFromLocation());
                                        if (mCar.getBay() == location.getBay() && loc.getActualArea().equals(mCar.getActualArea())) {
                                            //卸车
                                            if (mCar.getCheckLocation() == true) {
                                                MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, asrsJob.getFromLocation(), loadScar.getBlockNo(), "", "");
                                            } else {
                                                    MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, asrsJob.getFromLocation(), "", "", "");
                                            }

                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, loc.getLocationNo(), "", "", "");
                                        }

                                    }
                                } else {
                                    //母车上没子车
                                    if (sCar != null) {
                                        if (mCar.getPosition().equals(sCar.getPosition())) {
                                            if (sCar.getBay() != mCar.getBay() && sCar.getBank() != 15) {
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, asrsJob.getFromLocation(), "", "", "");
                                            } else {
                                                if (mCar.getCheckLocation() == true) {
                                                    MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                                } else {
                                                    String scLocation = StringUtils.leftPad("" + sCar.getBank(), 3, "0") + StringUtils.leftPad("" + sCar.getBay(), 3, "0") + "00" + sCar.getLevel();
                                                    if (Location.getByLocationNo(scLocation) != null) {
                                                        MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, scLocation, "", "", "");
                                                    }
                                                }

                                            }
                                        } else {
                                            Location loc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + sCar.getLevel());
                                            if (mCar.getBay() != Integer.parseInt(Const.TEMP_BAY) || !mCar.getActualArea().equals(loc.getActualArea())) {
                                                //移动到通道上去
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, loc.getLocationNo(), "", "", "");
                                            } else {

                                            }
                                        }

                                    }
                                }
                            }

                        } else if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {

                            if (mCar.getPosition().equals(Location.RIGHT)) {
                                if (StringUtils.isEmpty(mCar.getsCarBlockNo())) {
                                    //母车上没子车
                                    AsrsJob job = AsrsJob.getAsrsJobByMcKey(asrsJob.getMcKey());
//                                    Location newLocation = Location.getByLocationNo(job.getToLocation());
//                                    if (newLocation.getActureArea().equals(mCar.getActuaneArea()) && newLocation.getBay() == mCar.getBay()) {
                                    Lift dock = (Lift) mCar.getPreBlock(asrsJob.getMcKey(), asrsJob.getType());
                                    String mCarDock = mCar.getDock(mCar.getBlockNo(), dock.getBlockNo());
                                    if (mCar.isLiftSide() && mCarDock.equals(mCar.getDock())) {
                                        //移栽接货
                                    } else {
                                        Lift lift = (Lift) mCar.getPreBlock(asrsJob.getMcKey(), asrsJob.getType());
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                    }
//                                    } else {
//                                        SCar sCar = (SCar) HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:level and status = '1'").setParameter("level", mCar.getLevel()).uniqueResult();
//                                        if (sCar.getBay() == mCar.getBay())
//                                            MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
//                                    }
                                } else {
                                    //子车上有母车，移动到提升机
                                    Lift dock = (Lift) mCar.getPreBlock(asrsJob.getMcKey(), asrsJob.getType());
                                    String mCarDock = mCar.getDock(mCar.getBlockNo(), dock.getBlockNo());
                                    if (mCar.isLiftSide() && mCarDock.equals(mCar.getDock())) {
                                        //移栽接货
                                    } else {
                                        Lift lift = (Lift) mCar.getPreBlock(asrsJob.getMcKey(), asrsJob.getType());
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                    }
                                }
                            } else {

                                Location lc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());

                                SCar sCar = (SCar) HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:level").setParameter("level", mCar.getLevel()).uniqueResult();

                                if (mCar.getBay() == Integer.parseInt(Const.TEMP_BAY)
                                        && lc.getActualArea().equals(mCar.getActualArea())) {
                                    if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                                        //如果左边母车上有子车，子车应该到通道里面
                                        if (mCar.getCheckLocation() == true) {
                                            MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, lc.getLocationNo(), sCar.getBlockNo(), "", "");
                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                        }


                                    } else {
                                        if (sCar.getBay() == mCar.getBay() && sCar.getPosition().equals(mCar.getPosition())) {
                                            if (StringUtils.isNotBlank(sCar.getMcKey())) {
                                                //子车载通道中，并且子车已经有了mckey了
                                                if (mCar.getCheckLocation() == true) {
                                                    MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                                } else {
                                                    MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                                }

                                            }
                                        }
                                    }
                                } else {
                                    if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
                                        //子车上有母车
                                        MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, "01500" + Const.TEMP_BAY + "00" + mCar.getLevel(), "", "", "");
                                    } else {
                                        if (StringUtils.isNotEmpty(sCar.getReservedMcKey()) ||
                                                (sCar.getReservedMcKey() == null && sCar.getMcKey() == null)
                                                        && mCar.getBay() == Integer.parseInt(Const.TEMP_BAY)) {
                                            //子车上有mck
                                            if (mCar.getCheckLocation() == true) {
                                                MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                            } else {
                                                MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, "01500" + Const.TEMP_BAY + "00" + mCar.getLevel(), "", "", "");
                                            }


                                        } else {
                                            if (mCar.getBay() != Integer.parseInt(Const.TEMP_BAY)) {
                                                MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, "01500" + Const.TEMP_BAY + "00" + mCar.getLevel(), "", "", "");
                                            }
                                        }
                                    }
                                }
                            }

                        } else if (AsrsJobType.COUNT.equals(asrsJob.getType())) {

                            if (mCar.getPosition().equals(Location.RIGHT)) {
                                if (StringUtils.isEmpty(mCar.getsCarBlockNo())) {
                                    //母车上没子车
                                    AsrsJob job = AsrsJob.getAsrsJobByMcKey(asrsJob.getMcKey());
//                                    Location newLocation = Location.getByLocationNo(job.getToLocation());
//                                    if (newLocation.getActureArea().equals(mCar.getActuaneArea()) && newLocation.getBay() == mCar.getBay()) {
                                    Lift dock = (Lift) mCar.getPreBlock(asrsJob.getMcKey(), asrsJob.getType());
                                    String mCarDock = mCar.getDock(mCar.getBlockNo(), dock.getBlockNo());
                                    if (mCar.isLiftSide() && mCarDock.equals(mCar.getDock())) {
                                        //移栽接货
                                    } else {
                                        Lift lift = (Lift) mCar.getPreBlock(asrsJob.getMcKey(), asrsJob.getType());
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                    }
//                                    } else {
//                                        SCar sCar = (SCar) HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:level and status = '1'").setParameter("level", mCar.getLevel()).uniqueResult();
//                                        if (sCar.getBay() == mCar.getBay())
//                                            MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
//                                    }
                                } else {
                                    //子车上有母车，移动到提升机
                                    Lift dock = (Lift) mCar.getPreBlock(asrsJob.getMcKey(), asrsJob.getType());
                                    String mCarDock = mCar.getDock(mCar.getBlockNo(), dock.getBlockNo());
                                    if (mCar.isLiftSide() && mCarDock.equals(mCar.getDock())) {
                                        //移栽接货
                                    } else {
                                        Lift lift = (Lift) mCar.getPreBlock(asrsJob.getMcKey(), asrsJob.getType());
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                    }
                                }
                            } else {

                                Location lc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());

                                SCar sCar = (SCar) HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:level").setParameter("level", mCar.getLevel()).uniqueResult();

                                if (mCar.getBay() == Integer.parseInt(Const.TEMP_BAY)
                                        && lc.getActualArea().equals(mCar.getActualArea())) {
                                    if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                                        //如果左边母车上有子车，子车应该到通道里面
                                        if (mCar.getCheckLocation() == true) {
                                            MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, lc.getLocationNo(), sCar.getBlockNo(), "", "");
                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                        }


                                    } else {
                                        if (sCar.getBay() == mCar.getBay() && sCar.getPosition().equals(mCar.getPosition())) {
                                            if (StringUtils.isNotBlank(sCar.getMcKey())) {
                                                //子车载通道中，并且子车已经有了mckey了
                                                if (mCar.getCheckLocation() == true) {
                                                    MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                                } else {
                                                    MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                                }

                                            }
                                        }
                                    }
                                } else {
                                    if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
                                        //子车上有母车
                                        MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, "01500" + Const.TEMP_BAY + "00" + mCar.getLevel(), "", "", "");
                                    } else {
                                        if (StringUtils.isNotEmpty(sCar.getReservedMcKey()) ||
                                                (sCar.getReservedMcKey() == null && sCar.getMcKey() == null)
                                                        && mCar.getBay() == Integer.parseInt(Const.TEMP_BAY)) {
                                            //子车上有mck
                                            if (mCar.getCheckLocation() == true) {
                                                MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                            } else {
                                                MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, "01500" + Const.TEMP_BAY + "00" + mCar.getLevel(), "", "", "");
                                            }

                                        } else {
                                            if (mCar.getBay() != Integer.parseInt(Const.TEMP_BAY)) {
                                                MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, "01500" + Const.TEMP_BAY + "00" + mCar.getLevel(), "", "", "");
                                            }
                                        }
                                    }
                                }
                            }

                        } else if (AsrsJobType.CHANGELEVEL.equals(asrsJob.getType())) {

                            SCar sCar = (SCar) HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:level and s.status = '1'").setParameter("level", mCar.getLevel()).uniqueResult();

                            if (sCar != null && sCar.getLevel() == mCar.getLevel()) {
                                if (mCar.getPosition().equals(Location.LEFT)) {
                                    if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {

                                    } else {

                                        if (mCar.getBlockNo().equals(asrsJob.getToStation())) {
                                            Lift lift = (Lift) mCar.getPreBlock(asrsJob.getMcKey(), asrsJob.getType());
                                            if (!mCar.isLiftSide()) {
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                            } else {
                                                if (mCar.getCheckLocation() == true) {
                                                    MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                                } else {
                                                    MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                                }

                                            }

                                        } else {

                                            if (sCar.getPosition().equals(Location.LEFT)) {

                                                if (mCar.getBay() == sCar.getBay()) {
                                                    if (mCar.getCheckLocation() == true) {
                                                        MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                                    } else {
                                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "01500" + Const.TEMP_BAY + "00" + mCar.getLevel(), "", "", "");
                                                    }

                                                } else {

                                                }

                                            } else if (sCar.getPosition().equals(Location.RIGHT) || sCar.getPosition().equals(Location.MISS)) {
                                                if (mCar.getBay() != Integer.parseInt(Const.TEMP_BAY)) {
                                                    MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "01500" + Const.TEMP_BAY + "00" + mCar.getLevel(), "", "", "");
                                                } else {
                                                    if (mCar.getCheckLocation() == true) {
                                                        MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                                    } else {
                                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, "01500" + Const.TEMP_BAY + "00" + mCar.getLevel(), "", "", "");
                                                    }

                                                }
                                            }

                                        }
                                    }
                                } else {
                                    if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {

                                    } else {
                                        if (mCar.getBay() == sCar.getBay()) {
                                            if (mCar.getCheckLocation() == true) {
                                                MsgSender.send03(Message03._CycleOrder.loadCar, sCar.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                            } else {
                                                String scLocation = StringUtils.leftPad("" + sCar.getBank(), 3, "0") + StringUtils.leftPad("" + sCar.getBay(), 3, "0") + "00" + sCar.getLevel();
                                                if (Location.getByLocationNo(scLocation) != null) {
                                                    MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, scLocation, "", "", "");
                                                }
                                            }

                                        } else {
                                            String scLocation = StringUtils.leftPad("" + sCar.getBank(), 3, "0") + StringUtils.leftPad("" + sCar.getBay(), 3, "0") + "00" + sCar.getLevel();
                                            if (Location.getByLocationNo(scLocation) != null) {
                                                MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, scLocation, "", "", "");
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (AsrsJobType.RECHARGED.equals(asrsJob.getType())) {
                            if (mCar.getPosition().equals(Location.LEFT)) {

                                if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                                    SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                    if (StringUtils.isNotEmpty(sCar.getOnMCar())) {
                                        Location location = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());
                                        if (mCar.getBay() != Integer.parseInt(Const.TEMP_BAY) || !mCar.getActualArea().equals(location.getActualArea())) {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, location.getLocationNo(), "", "", "");
                                        } else {
                                            if (mCar.getCheckLocation() == true) {
                                                MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                            } else {
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, location.getLocationNo(), "", "", "");

                                            }

                                        }
                                    }
                                } else {

                                }
                            } else {
                                if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                                    Lift lift = (Lift) mCar.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                                    SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                    if (!mCar.isLiftSide() || !mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()).equals(mCar.getDock())) {
                                        if (StringUtils.isNotBlank(sCar.getOnMCar()))
                                            MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                    } else {
                                        mCar.setReservedMcKey(null);
                                        mCar.setMcKey(asrsJob.getMcKey());
                                    }
                                } else {
                                    //如果母车上没有子车，那么子车载另外一边，移动到固定通道
                                    Location location = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());
                                    if (mCar.getBay() != Integer.parseInt(Const.TEMP_BAY) || !mCar.getActualArea().equals(location.getActualArea())) {
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, location.getLocationNo(), "", "", "");
                                    } else {
                                        SCar sCar = (SCar) HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:level and s.status = '1'").setParameter("level", mCar.getLevel()).uniqueResult();
                                        if (sCar.getPosition().equals(mCar.getPosition())) {
                                            //子车移动到通道的另外一边
                                            if(mCar.getCheckLocation() == true){
                                                MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                            }else{
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, location.getLocationNo(), "", "", "");
                                            }
                                        }
                                    }

                                }
                            }
                        } else if (AsrsJobType.RECHARGEDOVER.equals(asrsJob.getType())) {
                            if (mCar.getPosition().equals(Location.RIGHT)) {
                                Lift lift = (Lift) mCar.getPreBlock(asrsJob.getMcKey(), asrsJob.getType());
                                if (!mCar.isLiftSide() || !mCar.getDock().equals(mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()))) {
                                    MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                } else {
                                    if (StringUtils.isNotEmpty(lift.getMcKey())
                                            && lift.getLevel() == mCar.getLevel())
                                        if(mCar.getCheckLocation() == true){
                                            MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", lift.getOnCar(), "", "");
                                        }else{
                                            MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, "", mCar.getDock(mCar.getBlockNo(), lift.getBlockNo()), "", "");
                                        }

                                }
                            }
                        } else if (AsrsJobType.ST2ST.equals(asrsJob.getType())) {

                            if (mCar.getBlockNo().equals(asrsJob.getFromStation())) {

                                Location fromLocation = Location.getByLocationNo(asrsJob.getFromLocation());
                                if (fromLocation.getOutPosition().equals(mCar.getPosition())) {
                                    //出库货位和母车在同一区域
                                    if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
                                        //子车在当前母车上
                                        if (!fromLocation.getActualArea().equals(mCar.getActualArea()) || fromLocation.getBay() != mCar.getBay()) {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, asrsJob.getFromLocation(), "", "", "");
                                        } else {
                                            if (mCar.getCheckLocation() == true) {
                                                MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, fromLocation.getLocationNo(), mCar.getsCarBlockNo(), "", "");
                                            } else {
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, asrsJob.getFromLocation(), "", "", "");
                                            }

                                        }
                                    } else {
                                        //子车不在当前母车上
                                        SCar sCar = (SCar) HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:level and s.status = '1'").setParameter("level", mCar.getLevel()).uniqueResult();
//                                        Location lc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());
                                        String scLocation = StringUtils.leftPad("" + sCar.getBank(), 3, "0") + StringUtils.leftPad("" + sCar.getBay(), 3, "0") + "00" + sCar.getLevel();
                                        Location lc = Location.getByLocationNo(scLocation);
                                        if (!sCar.getPosition().equals(mCar.getPosition())) {
                                            //母车不在中间货位的固定通道上
                                            if ((!mCar.getActualArea().equals(lc.getActualArea()) && mCar.getBay() != lc.getBay())
                                                    || (sCar.getPosition().equals(mCar.getPosition()) && sCar.getBay() == lc.getBay()))
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                        } else {
                                            if (sCar.getPosition().equals(mCar.getPosition()) && sCar.getBay() == mCar.getBay()) {
                                                if(mCar.getCheckLocation() == true){
                                                    MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                                }else{
                                                    MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                                }

                                            } else {
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                            }
                                        }
                                    }
                                } else {
                                    //出库货位和母车不在同一区域
                                    if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
                                        Location lc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());
                                        if (!mCar.getActualArea().equals(lc.getActualArea()) || mCar.getBay() != Integer.parseInt(Const.TEMP_BAY)) {
                                            //往中间通道卸子车
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                        } else {
                                            //往中间通道卸子车
                                            if (mCar.getCheckLocation() == true) {
                                                MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, lc.getLocationNo(), mCar.getsCarBlockNo(), "", "");
                                            } else {
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                            }

                                        }
                                    }
                                }

                            } else {

                                if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
                                    SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                    //母车上有子车的话,到指定通道卸子车，去另外一台母车上取货。
                                    Location lc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());
                                    if (mCar.getBay() != lc.getBay() || !mCar.getActualArea().equals(lc.getActualArea())) {
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                    } else {
                                        if (mCar.getCheckLocation() == true) {
                                            MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, lc.getLocationNo(), sCar.getBlockNo(), "", "");
                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                        }

                                    }

                                } else {
                                    //母车上没有子车，检查母车上是否有子车，没有子车，移动到指定巷道，等待接子车
                                    Location toLocation = Location.getByLocationNo(asrsJob.getToLocation());
                                    if (toLocation.getPosition().equals(mCar.getPosition())) {
                                        if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {

                                        } else {

                                            SCar sCar = (SCar) HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:level and s.status = '1'").setParameter("level", mCar.getLevel()).uniqueResult();

                                            if (sCar.getPosition().equals(mCar.getPosition())) {

                                                if (sCar.getBank() == 15) {
                                                    Location lc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());

                                                    if (lc.getActualArea().equals(mCar.getActualArea()) && lc.getBay() == mCar.getBay()) {
                                                        if(mCar.getCheckLocation() == true){
                                                            MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                                        }else{
                                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                                        }

                                                    } else {
                                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }


                            }
                        } else if (AsrsJobType.BACK_PUTAWAY.equals(asrsJob.getType())) {
                            Location fromLocation = Location.getByLocationNo(asrsJob.getFromLocation());

                            if (mCar.getBlockNo().equals(asrsJob.getFromStation())) {
                                //母车是作业的起始station
                                if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
                                    //如果母车上有子车
                                    if (mCar.getBay() == fromLocation.getBay() && mCar.getActualArea().equals(fromLocation.getActualArea())) {
                                        //母车位置和子车位置在同一列
                                        if (mCar.getCheckLocation() == true) {
                                            MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, fromLocation.getLocationNo(), mCar.getsCarBlockNo(), "", "");
                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, fromLocation.getLocationNo(), "", "", "");
                                        }

                                    } else {
                                        //母车移动到指定的列
                                        MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, fromLocation.getLocationNo(), "", "", "");
                                    }
                                } else {
                                    //母车上没子车
                                    SCar sCar = (SCar) HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:level and s.status = '1'").setParameter("level", mCar.getLevel()).uniqueResult();

                                    if (sCar.getBank() == 15) {
                                        Location lc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());

                                        if (lc.getActualArea().equals(mCar.getActualArea()) && lc.getBay() == mCar.getBay()) {
                                            //接子车
                                            if(mCar.getCheckLocation() == true){
                                                MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                            }else{
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                            }

                                        } else {
                                            //移动
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                        }
                                    } else {
                                        if (sCar.getBay() == mCar.getBay() && sCar.getActualArea().equals(mCar.getActualArea())) {
                                            if(mCar.getCheckLocation() == true){
                                                MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                            }else{
                                                String scLocation = StringUtils.leftPad("" + sCar.getBank(), 3, "0") + StringUtils.leftPad("" + sCar.getBay(), 3, "0") + "00" + sCar.getLevel();
                                                if (Location.getByLocationNo(scLocation) != null) {
                                                    MsgSender.send03(Message03._CycleOrder.move, mCar.getReservedMcKey(), mCar, scLocation, "", "", "");
                                                }
                                            }

                                        }
                                    }
                                }

                            } else {

                                //母车不是起始station
                                if (StringUtils.isNotEmpty(mCar.getsCarBlockNo())) {
                                    //母车上有子车
                                    SCar sCar = (SCar) HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:level and s.status = '1'").setParameter("level", mCar.getLevel()).uniqueResult();

                                    if (sCar.getPosition().equals(mCar.getPosition())) {
                                        Location lc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());

                                        if (lc.getActualArea().equals(mCar.getActualArea()) && lc.getBay() == mCar.getBay()) {
                                            //卸子车
                                            if (mCar.getCheckLocation() == true) {
                                                MsgSender.send03(Message03._CycleOrder.unloadCar, asrsJob.getMcKey(), mCar, lc.getLocationNo(), sCar.getBlockNo(), "", "");
                                            } else {
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                            }

                                        } else {
                                            //移动
                                            MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                        }


                                    }
                                } else {

                                    Location toLocation = Location.getByLocationNo(asrsJob.getToLocation());
                                    if (toLocation.getOutPosition().equals(mCar.getPosition())) {
                                        SCar sCar = (SCar) HibernateUtil.getCurrentSession().createQuery("from SCar s where s.level=:level and s.status = '1'").setParameter("level", mCar.getLevel()).uniqueResult();

                                        if (sCar.getBank() == 15) {
                                            Location lc = Location.getByLocationNo("01500" + Const.TEMP_BAY + "00" + mCar.getLevel());

                                            if (lc.getActualArea().equals(mCar.getActualArea()) && lc.getBay() == mCar.getBay()) {
                                                //装子车
                                                if(mCar.getCheckLocation() == true){
                                                    MsgSender.send03(Message03._CycleOrder.loadCar, asrsJob.getMcKey(), mCar, "", sCar.getBlockNo(), "", "");
                                                }else{
                                                    MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                                }

                                            } else {
                                                //移动
                                                MsgSender.send03(Message03._CycleOrder.move, asrsJob.getMcKey(), mCar, lc.getLocationNo(), "", "", "");
                                            }
                                        }
                                    }

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
