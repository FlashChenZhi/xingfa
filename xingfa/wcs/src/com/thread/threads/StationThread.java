package com.thread.threads;


import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message03;
import com.thread.blocks.*;
import com.thread.threads.service.StationService;
import com.thread.threads.service.impl.putaway.StationPutawayService;
import com.thread.utils.MsgSender;
import com.util.common.StringUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;

/**
 * Created by Administrator on 2016/10/28.
 */
public class StationThread extends BlockThread<StationBlock> {

    public static void main(String[] args) {
        Thread thread = new Thread(new StationThread("0002"));
        thread.start();
    }

    public StationThread(String blockNo) {
        super(blockNo);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Transaction.begin();
                StationBlock station = getBlock();
                if (station.isWaitingResponse()) {
                    System.out.println(String.format("station %s waiting for response", station.getBlockNo()));
                } else {
                    if (StringUtils.isNotEmpty(station.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(station.getMcKey());
                        if (AsrsJobType.RETRIEVAL.equals(asrsJob.getType())) {
                            //出库到达
                        } else if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {
                            StationService service = new StationPutawayService(station);
                            service.withMckey();
                        }
                    }
                }
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                e.printStackTrace();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
