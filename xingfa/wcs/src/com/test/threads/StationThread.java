package com.test.threads;


import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.message.Message03;
import com.test.blocks.Block;
import com.test.blocks.Crane;
import com.test.blocks.Lift;
import com.test.blocks.StationBlock;
import com.test.utils.MsgSender;
import com.util.hibernate.Transaction;

/**
 * Created by Administrator on 2016/10/28.
 */
public class StationThread extends BlockThread<StationBlock> {
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
                    if (station.isLoaded()) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(station.getMcKey());
                        if (AsrsJobType.RETRIEVAL.equals(asrsJob.getType())) {
                            //出库到达
                        } else if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {
                            Block nextBlock = station.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                            if (nextBlock instanceof Lift) {
                                Lift lift = (Lift) nextBlock;
                                if (lift.isOkToGo(asrsJob.getMcKey(), 0)) {
                                    MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, station.getMcKey(), station, "", lift.getBlockNo(), "", "");
                                    MsgSender.send03(Message03._CycleOrder.moveCarryGoods, station.getMcKey(), lift, "", station.getBlockNo(), "", "");
                                } else {
                                    //等待电梯可以移栽
                                }
                            } else if (nextBlock instanceof Crane) {
                                Crane crane = (Crane) nextBlock;
                                if (crane.getBay() == 0 && crane.getLevel() == 0 && station.getMcKey().equals(crane.getReservedMcKey())) {
                                    MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, station.getMcKey(), station, "", crane.getBlockNo(), "", "");
                                    MsgSender.send03(Message03._CycleOrder.moveCarryGoods, station.getMcKey(), crane, "", station.getBlockNo(), "", "");
                                } else {
                                    //等待升降机可以移栽
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
