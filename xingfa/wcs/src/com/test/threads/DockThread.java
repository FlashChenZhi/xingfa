package com.test.threads;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.message.Message03;
import com.test.blocks.Block;
import com.test.blocks.Dock;
import com.test.blocks.Lift;
import com.test.blocks.MCar;
import com.test.utils.MsgSender;
import com.util.hibernate.Transaction;

/**
 * Created by Administrator on 2016/12/14.
 */
public class DockThread extends BlockThread<Dock> {
    public DockThread(String blockNo) {
        super(blockNo);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Transaction.begin();
                Dock dock = getBlock();
                if (dock.isWaitingResponse()) {
                    System.out.println(String.format("Dock %s waiting for response", dock.getBlockNo()));
                } else {
                    if (dock.isLoaded()) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(dock.getMcKey());
                        if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {
                            Block block = dock.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                            if (block instanceof MCar) {
                                MCar mCar = (MCar) block;
                                if (mCar.isLiftSide() && dock.getMcKey().equals(mCar.getReservedMcKey())) {
                                    MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, dock.getMcKey(), dock, "", mCar.getBlockNo(), "", "");
                                    MsgSender.send03(Message03._CycleOrder.moveCarryGoods, dock.getMcKey(), mCar, "", dock.getBlockNo(), "", "");
                                } else {
                                    //等待母车到位
                                }
                            }
                        } else if (AsrsJobType.RETRIEVAL.equals(asrsJob.getType())) {
                            Block block = dock.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
                            if (block instanceof Lift) {
                                Lift lift = (Lift) block;
                                if (lift.isOkToGo(dock.getMcKey(), dock.getLevel())) {
                                    MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, dock.getMcKey(), dock, "", lift.getBlockNo(), "", "");
                                    MsgSender.send03(Message03._CycleOrder.moveCarryGoods, dock.getMcKey(), lift, "", dock.getBlockNo(), "", "");
                                } else {
                                    //等待电梯到位
                                }
                            }
                        }
                    } else {
                        System.out.println(String.format("Dock %s is not loaded", dock.getBlockNo()));
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
