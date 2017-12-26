package com.AllBinding.threads;


import com.AllBinding.blocks.Block;
import com.AllBinding.blocks.Crane;
import com.AllBinding.blocks.Dock;
import com.AllBinding.blocks.StationBlock;
import com.AllBinding.utils.BlockStatus;
import com.AllBinding.utils.MsgSender;
import com.asrs.domain.AsrsJob;
import com.asrs.message.Message03;
import com.util.common.LogWriter;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;

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

                //Message26Proc(station);
                if (!station.getStatus().equals(BlockStatus.STATUS_RUN)) {

//                    System.out.println(String.format("Station %s status is not run", station.getBlockNo()));
                } else if (station.isWaitingResponse()) {
//                    System.out.println(String.format("station %s waiting for response", station.getBlockNo()));
                } else {
                    if (StringUtils.isNotBlank(station.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(station.getMcKey());
                        Block nextBlock = station.getNextBlock(asrsJob.getFromStation(), asrsJob.getToStation());
                        if (nextBlock == null) {
                            //出库到达
                        } else {
                            //入库
                            boolean flag = false;
                            if (nextBlock instanceof Dock) {
                                Dock dock = (Dock) nextBlock;
                                if (!dock.isWaitingResponse() && StringUtils.isBlank(dock.getMcKey())) {
                                    flag = true;
                                }
                            } else if (nextBlock instanceof Crane) {
                                Crane crane = (Crane) nextBlock;
                                if (crane.isOkToGo(station.getMcKey(), 1, 0)) {
                                    flag = true;
                                }

                            }
                            if (flag) {
                                MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, station.getMcKey(), station, "", nextBlock.getBlockNo(), "", "");
                                MsgSender.send03(Message03._CycleOrder.moveCarryGoods, station.getMcKey(), nextBlock, "", station.getBlockNo(), "", "");
                            } else {
                                //等待可以移栽
                            }
                        }
                    }
                }
                Transaction.commit();
                Thread.sleep(300);
            } catch (Exception e) {
                Transaction.rollback();
                LogWriter.writeError(StationThread.class, e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
