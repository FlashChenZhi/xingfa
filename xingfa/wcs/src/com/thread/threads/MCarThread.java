package com.thread.threads;

import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message03;
import com.thread.blocks.*;
import com.thread.threads.service.MCarService;
import com.thread.threads.service.impl.MCarServiceImpl;
import com.thread.threads.service.impl.putaway.MCarSinglePutawayService;
import com.thread.threads.service.impl.retrieval.MCarSingleRetrievalService;
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

                MCar mCar = getBlock();
                if (mCar.isWaitingResponse()) {
                    //移动提升机等待回复

                } else if (!mCar.getStatus().equals("1")) {
                    //移动提升机状态不可用

                } else {

                    if (com.util.common.StringUtils.isEmpty(mCar.getReservedMcKey()) && com.util.common.StringUtils.isEmpty(mCar.getMcKey())) {
                        //移动提升机无任务
                        MCarService srmService = new MCarServiceImpl(mCar) {
                            @Override
                            public void withReserveMckey() throws Exception {

                            }

                            @Override
                            public void withMckey() throws Exception {

                            }
                        };

                        srmService.withOutJob();

                    } else if (com.util.common.StringUtils.isNotEmpty(mCar.getReservedMcKey())) {
                        //移动提升机有预约任务
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
                        MCarService service = null;
                        if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                            service = new MCarSinglePutawayService(mCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                            service = new MCarSingleRetrievalService(mCar);
                        }

                        service.withReserveMckey();

                    } else if (com.util.common.StringUtils.isNotEmpty(mCar.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
                        MCarService service = null;
                        if (asrsJob.getType().equals(AsrsJobType.PUTAWAY)) {
                            service = new MCarSinglePutawayService(mCar);
                        } else if (asrsJob.getType().equals(AsrsJobType.RETRIEVAL)) {
                            service = new MCarSingleRetrievalService(mCar);
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
