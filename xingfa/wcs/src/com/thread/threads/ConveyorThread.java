package com.thread.threads;


import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.thread.blocks.*;
import com.thread.threads.service.Service;
import com.thread.threads.service.impl.putaway.ConveyorPutawayService;
import com.thread.threads.service.impl.retrieval.ConveyorRetrievalService;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;

/**
 * Created by van on 2017/5/2.
 */
public class ConveyorThread extends BlockThread<Conveyor> {

    public ConveyorThread(String blockNo) {
        super(blockNo);
    }

    @Override
    public void run() {

        while (true) {
            try {

                Transaction.begin();
                Conveyor crane = getBlock();

                if (crane.isWaitingResponse()) {

                    //输送机等待35回复

                } else {
                    if (StringUtils.isBlank(crane.getMcKey()) && StringUtils.isBlank(crane.getReservedMcKey())) {

                    } else if (StringUtils.isNotEmpty(crane.getMcKey())) {
                        AsrsJob aj = AsrsJob.getAsrsJobByMcKey(crane.getMcKey());
                        Service service = null;
                        if (aj.getType().equals(AsrsJobType.PUTAWAY)) {
                            service = new ConveyorPutawayService(crane);

                        } else if (aj.getType().equals(AsrsJobType.RETRIEVAL)) {
                            service = new ConveyorRetrievalService(crane);

                        } else if (aj.getType().equals(AsrsJobType.RECHARGED)) {
                        }
                        service.withMckey();

                    } else if (StringUtils.isNotBlank(crane.getReservedMcKey())) {

                        AsrsJob aj = AsrsJob.getAsrsJobByMcKey(crane.getReservedMcKey());
                        Service service = null;
                        if (aj.getType().equals(AsrsJobType.RETRIEVAL)) {
                            service = new ConveyorRetrievalService(crane);

                        } else if (aj.getType().equals(AsrsJobType.RECHARGED)) {

                        }

                        service.withReserveMckey();
                    }

                }

                Transaction.commit();

            } catch (Exception e) {
                e.printStackTrace();
                Transaction.rollback();
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
