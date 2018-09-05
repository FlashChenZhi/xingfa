package com.thread.threads;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message03;
import com.thread.blocks.*;
import com.thread.threads.service.ScarService;
import com.thread.threads.service.impl.ScarAndSrmServiceImpl;
import com.thread.threads.service.impl.charage.ScarCharageService;
import com.thread.threads.service.impl.charageover.ScarCharageOverService;
import com.thread.threads.service.impl.locationtolocation.ScarLocationToLocationService;
import com.thread.threads.service.impl.movestorage.ScarMoveStorageService;
import com.thread.threads.service.impl.putaway.ScarAndSrmPutawayServcie;
import com.thread.threads.service.impl.retrieval.ScarAndSrmRetrievalService;
import com.thread.utils.MsgSender;
import com.util.common.Const;
import com.util.common.StringUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */
public class SCarThread extends BlockThread<SCar> {

    public SCarThread(String blockNo) {
        super(blockNo);
    }

    public static void main(String[] args) {
        SCar scar = (SCar) SCar.getByBlockNo("SC07");
    }

    @Override
    public void run() {
        while (true) {
            try {
                Transaction.begin();
                SCar sCar = getBlock();

                if (sCar.isWaitingResponse()) {

                }else if(sCar.getStatus().equals("2")){

                } else if (!sCar.getStatus().equals("1")) {
                    Location location = Location.getByLocationNo(sCar.getChargeLocation());

                    if(sCar.getBank() != location.getBank() ||sCar.getBay() != location.getBay() || sCar.getLevel() != location.getLevel()){
                        sCar.setStatus(SCar.STATUS_RUN);

                    }else if(sCar.getPower() >= 94){
                        Srm fromSrm = Srm.getSrmByPosition(location.getPosition());
                        Srm toSrm = Srm.getSrmByGroupNo(sCar.getGroupNo());

                        if (fromSrm.getBlockNo().equals(toSrm.getBlockNo())) {
//                            sCar.setStatus(SCar.STATUS_RUN);
                            //欧普适用
                            MsgSender.send03(Message03._CycleOrder.chargeFinish, "9999", sCar, sCar.getChargeLocation(), "", String.valueOf(location.getBay()),String.valueOf(location.getLevel()));
                        } else {

                            AsrsJob newJob = new AsrsJob();

                            newJob.setWareHouse(sCar.getWareHouse());
                            newJob.setType(AsrsJobType.RECHARGEDOVER);
                            newJob.setMcKey(Mckey.getNext());
                            newJob.setGenerateTime(new Date());
                            newJob.setStatus(AsrsJobStatus.RUNNING);
                            newJob.setStatusDetail(AsrsJobStatusDetail.WAITING);
                            newJob.setFromLocation(sCar.getChargeLocation());
                            newJob.setFromStation(fromSrm.getBlockNo());
                            newJob.setToStation(toSrm.getBlockNo());

                            HibernateUtil.getCurrentSession().save(newJob);
                            sCar.setMcKey(newJob.getMcKey());
                            sCar.setStatus(SCar.STATUS_RUN);

                            MsgSender.send03(Message03._CycleOrder.chargeFinish, newJob.getMcKey(), sCar, sCar.getChargeLocation(), "", String.valueOf(location.getBay()),String.valueOf(location.getLevel()));
                        }
                    }
                }  else {
                    if (StringUtils.isEmpty(sCar.getReservedMcKey()) && StringUtils.isEmpty(sCar.getMcKey())) {

                        ScarAndSrmServiceImpl service = new ScarAndSrmServiceImpl(sCar);
                        service.withOutJob();

                    } else if (StringUtils.isNotEmpty(sCar.getMcKey())) {

                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
                        ScarService service = null;
                        if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)|| asrsJob.getType().equals(AsrsJobType.CHECKINSTORAGE)) {
                            service = new ScarAndSrmPutawayServcie(sCar);

                        } else if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL) || asrsJob.getType().equals(AsrsJobType.CHECKOUTSTORAGE)) {
                            service = new ScarAndSrmRetrievalService(sCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGED)) {
                            service = new ScarCharageService(sCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                            service = new ScarCharageOverService(sCar);
                        }else if (asrsJob.getType().equals(AsrsJobType.LOCATIONTOLOCATION)||asrsJob.getType().equals(AsrsJobType.BACK_PUTAWAY)) {
                            service = new ScarLocationToLocationService(sCar);
                        }else if (asrsJob.getType().equals(AsrsJobType.MOVESTORAGE)) {
                            service = new ScarMoveStorageService(sCar);
                        }
                        service.withMckey();

                    } else if (StringUtils.isNotEmpty(sCar.getReservedMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                        ScarService service = null;
                        if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)|| asrsJob.getType().equals(AsrsJobType.CHECKINSTORAGE)) {
                            service = new ScarAndSrmPutawayServcie(sCar);

                        } else if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL) || asrsJob.getType().equals(AsrsJobType.CHECKOUTSTORAGE)) {
                            service = new ScarAndSrmRetrievalService(sCar);

                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGED)) {
                            service = new ScarCharageService(sCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                            service = new ScarCharageOverService(sCar);
                        }else if (asrsJob.getType().equals(AsrsJobType.LOCATIONTOLOCATION)||asrsJob.getType().equals(AsrsJobType.BACK_PUTAWAY)) {
                            service = new ScarLocationToLocationService(sCar);
                        }else if (asrsJob.getType().equals(AsrsJobType.MOVESTORAGE)) {
                            service = new ScarMoveStorageService(sCar);
                        }
                        service.withReserveMckey();

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
