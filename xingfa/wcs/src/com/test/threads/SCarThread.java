package com.test.threads;

import com.asrs.business.consts.StatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message03;
import com.test.blocks.Crane;
import com.test.blocks.MCar;
import com.test.blocks.SCar;
import com.test.utils.MsgSender;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

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
                if (sCar.isWaitingResponse()) {
                    System.out.println(String.format("SCar %s is waitingResponse", sCar.getBlockNo()));
                } else {
                    if (StringUtils.isBlank(sCar.getReservedMcKey()) && StringUtils.isBlank(sCar.getMcKey())) {
                        if (!sCar.isOnMCar()) {
                            List<AsrsJob> asrsJobs = session.createQuery("select aj from AsrsJob aj,Location l where aj.fromLocation=l.locationNo " +
                                    "    and l.level=:level and aj.statusDetail=:statusDetail order by aj.generateTime")
                                    .setString("statusDetail", StatusDetail.WAITING).setInteger("level", sCar.getLevel()).list();
                            AsrsJob asrsJob = (AsrsJob) session.createQuery("select aj from AsrsJob aj,MCar mc,Crane c where aj.type=:type and ((mc.level=:level " +
                                    "and aj.mcKey=mc.mcKey and aj.toStation=mc.blockNo)or (aj.mcKey=c.mcKey and aj.toStation=c.blockNo)) ").setString("type", AsrsJobType.PUTAWAY).setInteger("level", sCar.getLevel()).uniqueResult();
                            if (asrsJobs.isEmpty()) {
                                if (asrsJob != null) {
                                    sCar.setReservedMcKey(asrsJob.getMcKey());
                                } else {
                                    //没有任务要做
                                }
                            } else {
                                if (asrsJob != null) {
                                    if (asrsJobs.get(0).getGenerateTime().compareTo(asrsJob.getGenerateTime()) > 0) {
                                        sCar.setReservedMcKey(asrsJob.getMcKey());
                                    } else {
                                        sCar.setReservedMcKey(asrsJobs.get(0).getMcKey());
                                    }
                                } else {
                                    sCar.setReservedMcKey(asrsJobs.get(0).getMcKey());
                                }
                            }
                        } else {
                            MCar mCar = (MCar) session.createCriteria(MCar.class).add(Restrictions.eq("sCarBlockNo", sCar.getBlockNo())).uniqueResult();
                            if (mCar == null) {
                                Crane crane = (Crane) session.createCriteria(MCar.class).add(Restrictions.eq("sCarBlockNo", sCar.getBlockNo())).uniqueResult();
                                MsgSender.send03(Message03._CycleOrder.offCar, crane.getMcKey(), sCar, "", crane.getBlockNo(), "", "");
                            } else {
                                MsgSender.send03(Message03._CycleOrder.offCar, mCar.getMcKey(), sCar, "", mCar.getBlockNo(), "", "");
                            }
                        }
                    } else if (StringUtils.isNotBlank(sCar.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
                        if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {
                            //移动到目标bay，卸货
                            MsgSender.send03(Message03._CycleOrder.unloadGoods, sCar.getMcKey(), sCar, asrsJob.getToLocation(), "", "", "");
                        }
                    } else if (StringUtils.isNotBlank(sCar.getReservedMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                        if (AsrsJobType.RETRIEVAL.equals(asrsJob.getType())) {
                            Location location = Location.getByLocationNo(asrsJob.getFromLocation());
                            MCar mCar = (MCar) session.createCriteria(MCar.class).add(Restrictions.eq("reservedMcKey", sCar.getReservedMcKey())).uniqueResult();
                            if (mCar != null) {
                                if (sCar.getBay() == location.getBay()) {
                                    if (sCar.isOnMCar()) {
                                        MsgSender.send03(Message03._CycleOrder.offCar, sCar.getReservedMcKey(), sCar, "", mCar.getBlockNo(), "", "");
                                    } else {
                                        MsgSender.send03(Message03._CycleOrder.pickUpGoods, sCar.getReservedMcKey(), sCar, location.getLocationNo(), "", "", "");
                                    }
                                } else {
                                    if (sCar.getBay() == mCar.getBay()) {
                                        if (sCar.isOnMCar()) {
                                            //让母车带子车移动到目标bay
                                        } else {
                                            MsgSender.send03(Message03._CycleOrder.onCar, sCar.getReservedMcKey(), sCar, "", mCar.getBlockNo(), "", "");
                                        }
                                    } else {
                                        //让母车去子车所在bay
                                    }
                                }
                            } else {
                                Crane crane = (Crane) session.createCriteria(Crane.class).add(Restrictions.eq("reservedMcKey", sCar.getReservedMcKey())).uniqueResult();
                                if (crane != null) {
                                    if (sCar.isOnMCar()) {
                                        if (sCar.getBay() == location.getBay()) {
                                            MsgSender.send03(Message03._CycleOrder.offCar, sCar.getReservedMcKey(), sCar, "", crane.getBlockNo(), "", "");
                                        } else {
                                            //升降机带子车移动到目标bay
                                        }
                                    } else {
                                        if (sCar.getBay() == location.getBay()) {
                                            MsgSender.send03(Message03._CycleOrder.pickUpGoods, sCar.getReservedMcKey(), sCar, location.getLocationNo(), "", "", "");
                                        } else {
                                            if (sCar.getBay() == crane.getBay() && sCar.getLevel() == crane.getLevel()) {
                                                MsgSender.send03(Message03._CycleOrder.onCar, sCar.getReservedMcKey(), sCar, "", crane.getBlockNo(), "", "");
                                            } else {
                                                //升降机移动到子车所在位置
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {
                            MCar mCar = (MCar) session.createCriteria(MCar.class).add(Restrictions.eq("mcKey", sCar.getReservedMcKey())).uniqueResult();
                            if (mCar != null) {
                                if (sCar.getBay() == mCar.getBay()) {
                                    if (sCar.isOnMCar()) {
                                        //让母车带子车移动到目标bay
                                    } else {
                                        MsgSender.send03(Message03._CycleOrder.onCar, sCar.getReservedMcKey(), sCar, "", mCar.getBlockNo(), "", "");
                                    }
                                } else {
                                    //让母车去子车所在bay
                                }
                            } else {
                                Crane crane = (Crane) session.createCriteria(Crane.class).add(Restrictions.eq("mcKey", sCar.getReservedMcKey())).uniqueResult();
                                if (crane != null) {
                                    if (sCar.isOnMCar()) {
                                        //升降机带子车移动
                                    } else {
                                        if (sCar.getLevel() == crane.getLevel() && sCar.getBay() == crane.getBay()) {
                                            MsgSender.send03(Message03._CycleOrder.onCar, sCar.getReservedMcKey(), sCar, "", crane.getBlockNo(), "", "");
                                        } else {
                                            //升降机移动到子车位置
                                        }
                                    }
                                }
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
