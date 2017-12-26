package com.test.threads;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.message.Message03;
import com.test.blocks.Block;
import com.test.blocks.Conveyor;
import com.test.blocks.Lift;
import com.test.blocks.StationBlock;
import com.test.utils.MsgSender;
import com.util.hibernate.Transaction;


/**
 * Created by Administrator on 2016/10/12.
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
                Conveyor conveyor = getBlock();
                //该位置正在等待响应
                if (conveyor.isWaitingResponse()) {
                    System.out.println(String.format("Conveyor %s waiting for response", conveyor.getBlockNo()));
                } else {
                    //该位置载货了
                    if (conveyor.isLoaded()) {
                        System.out.println(String.format("Conveyor %s loaded", conveyor.getBlockNo()));
                        AsrsJob aj = AsrsJob.getAsrsJobByMcKey(conveyor.getMcKey());
                        Block block;
                        if (AsrsJobType.RETRIEVAL.equals(aj.getType())) {
                            block = conveyor.getNextBlock(aj.getType(), aj.getToStation());
                        } else {
                            block = conveyor.getNextBlock(aj.getType(), aj.getToStation());
                        }
                        if (block instanceof Conveyor) {
                            Conveyor conveyor2 = (Conveyor) block;
                            if (!conveyor2.isLoaded()) {
                                MsgSender.send03(Message03._CycleOrder.move, conveyor.getMcKey(), conveyor, "", conveyor2.getBlockNo(), "", "");
                            } else {
                                System.out.println(String.format("Conveyor %s waiting for ok to go", conveyor.getBlockNo()));
                            }

                        } else if (block instanceof StationBlock) {
                            StationBlock stationBlock = (StationBlock) block;
                            if (!stationBlock.isLoaded()) {
                                MsgSender.send03(Message03._CycleOrder.move, conveyor.getMcKey(), conveyor, "", stationBlock.getBlockNo(), "", "");
                            } else {
                                System.out.println(String.format("Conveyor %s waiting for ok to go", conveyor.getBlockNo()));
                            }
                        } else if (block instanceof Lift) {
                            Lift lift = (Lift) block;
                            if (lift.isOkToGo(conveyor.getMcKey(), 0)) {
//                                com.test.utils.MessageSender.send("03", conveyor.getBlockNo(), lift.getBlockNo());
//                                conveyor.setWaitingResponse(true);
                                MsgSender.send03(Message03._CycleOrder.move, conveyor.getMcKey(), conveyor, "", lift.getBlockNo(), "", "");
                            } else {
                                System.out.println(String.format("Conveyor %s waiting for ok to go", conveyor.getBlockNo()));
                            }
                        }
                    }
                    //该位置没有载货
                    else {
                        System.out.println(String.format("Conveyor %s not loaded", conveyor.getBlockNo()));
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
