package com.thread.threads;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.message.Message03;
import com.thread.blocks.*;
import com.thread.threads.service.SrmService;
import com.thread.threads.service.impl.SrmAndScarServiceImpl;
import com.thread.threads.service.impl.charage.SrmCharageService;
import com.thread.threads.service.impl.charageover.SrmCharageOverService;
import com.thread.threads.service.impl.locationtolocation.SrmLocationToLocationService;
import com.thread.threads.service.impl.movestorage.SrmMoveStorageService;
import com.thread.threads.service.impl.putaway.SrmAndScarPutawayService;
import com.thread.threads.service.impl.retrieval.SrmAndScarRetrievalService;
import com.thread.utils.MsgSender;
import com.util.common.StringUtils;
import com.util.hibernate.Transaction;

/**
 * Created by van on 2017/8/29.
 */
public class SrmThread extends BlockThread<Srm> {

    public static void main(String[] args) throws Exception {

        Transaction.begin();

        Srm srm = (Srm) Block.getByBlockNo("ML02");
        SCar sCar = (SCar) Block.getByBlockNo("SC02");
        StationBlock conveyor1 = (StationBlock) Block.getByBlockNo("1001");
        Conveyor conveyor2 = (Conveyor) Block.getByBlockNo("1002");

//        MsgSender.send03(Message03._CycleOrder.move, "9999", srm, "208002001", "", "", "");
//
//
//        MsgSender.send03(Message03._CycleOrder.moveUnloadGoods  , "9999", conveyor2, "", conveyor1.getBlockNo(), "", "");
//
//        MsgSender.send03(Message03._CycleOrder.moveCarryGoods, "9999", conveyor1, "", conveyor2.getBlockNo(), "", "");
//
        MsgSender.send03(Message03._CycleOrder.loadCar, "9999", srm, "208002001", sCar.getBlockNo(), "", "");

        MsgSender.send03(Message03._CycleOrder.onCar, "9999", sCar, "208002001", srm.getBlockNo(), "", "");
//

        Transaction.commit();

    }

    public SrmThread(String blockNo) {
        super(blockNo);
    }

    @Override
    public void run() {

        while (true) {

            try {

                Transaction.begin();

                Srm srm = getBlock();
                if (srm.isWaitingResponse()) {
                    //移动提升机等待回复

                } else if (!srm.getStatus().equals("1")) {
                    //移动提升机状态不可用

                } else {

                    if (StringUtils.isEmpty(srm.getReservedMcKey()) && StringUtils.isEmpty(srm.getMcKey())) {
                        //移动提升机无任务
                        SrmService srmService = new SrmAndScarServiceImpl(srm) {
                            @Override
                            public void withReserveMckey() throws Exception {

                            }

                            @Override
                            public void withMckey() throws Exception {

                            }
                        };

                        srmService.withOutJob();

                    } else if (StringUtils.isNotEmpty(srm.getReservedMcKey())) {
                        //移动提升机有预约任务
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getReservedMcKey());
                        SrmService service = null;
                        if (asrsJob.getType().equals(AsrsJobType.PUTAWAY) || asrsJob.getType().equals(AsrsJobType.CHECKINSTORAGE)) {
                            service = new SrmAndScarPutawayService(srm);
                        } else if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL) || asrsJob.getType().equals(AsrsJobType.CHECKOUTSTORAGE) ) {
                            service = new SrmAndScarRetrievalService(srm);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGED)) {
                            service = new SrmCharageService(srm);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                            service = new SrmCharageOverService(srm);
                        } else if(asrsJob.getType().equals(AsrsJobType.LOCATIONTOLOCATION)||asrsJob.getType().equals(AsrsJobType.BACK_PUTAWAY)){
                            service = new SrmLocationToLocationService(srm);
                        }else if( asrsJob.getType().equals(AsrsJobType.MOVESTORAGE) ){
                            service = new SrmMoveStorageService(srm);
                        }

                        service.withReserveMckey();

                    } else if (StringUtils.isNotEmpty(srm.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getMcKey());
                        SrmService service = null;
                        if (asrsJob.getType().equals(AsrsJobType.PUTAWAY) || asrsJob.getType().equals(AsrsJobType.CHECKINSTORAGE)) {
                            service = new SrmAndScarPutawayService(srm);
                        } else if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL) || asrsJob.getType().equals(AsrsJobType.CHECKOUTSTORAGE) ) {
                            service = new SrmAndScarRetrievalService(srm);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGED)) {
                            service = new SrmCharageService(srm);
                        } else if (asrsJob.getType().equals(AsrsJobType.RECHARGEDOVER)) {
                            service = new SrmCharageOverService(srm);
                        }else if(asrsJob.getType().equals(AsrsJobType.LOCATIONTOLOCATION)||asrsJob.getType().equals(AsrsJobType.BACK_PUTAWAY)){
                            service = new SrmLocationToLocationService(srm);
                        }else if( asrsJob.getType().equals(AsrsJobType.MOVESTORAGE) ){
                            service = new SrmMoveStorageService(srm);
                        }
                        service.withMckey();
                    }

                }

                Transaction.commit();

            } catch (Exception e) {
                Transaction.rollback();
                e.printStackTrace();
            } finally {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
