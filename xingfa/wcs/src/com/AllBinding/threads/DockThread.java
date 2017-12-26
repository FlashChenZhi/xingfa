package com.AllBinding.threads;

import com.AllBinding.blocks.Crane;
import com.AllBinding.blocks.StationBlock;
import com.AllBinding.utils.BlockStatus;
import com.asrs.domain.AsrsJob;
import com.asrs.message.Message03;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.util.common.LogWriter;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import com.AllBinding.blocks.Block;
import com.AllBinding.blocks.Dock;
import com.AllBinding.utils.MsgSender;

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
                if (!dock.getStatus().equals(BlockStatus.STATUS_RUN)) {
                    System.out.println(String.format("Dock %s status is not run", dock.getBlockNo()));
                } else if (dock.isWaitingResponse()) {
//                    System.out.println(String.format("Dock %s waiting for response", dock.getBlockNo()));
                } else {
                    if (StringUtils.isNotBlank(dock.getMcKey())) {
                        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(dock.getMcKey());
                        Block nextBlock = dock.getNextBlock(asrsJob.getFromStation(), asrsJob.getToStation());
                        boolean flag = false;
                        if (nextBlock instanceof Crane) {
                            Crane crane = (Crane) nextBlock;
                            if (crane.isOkToGo(dock.getMcKey(), 2, 0)) {
                                //升降机 在1层，0bay
                                flag = true;
                            }
                        } else if (nextBlock instanceof StationBlock) {
                            StationBlock stationBlock = (StationBlock) nextBlock;
                            if (StringUtils.isBlank(stationBlock.getMcKey())) {
                                flag = true;
                            }
                        }
                        if (flag) {
                            //移栽卸货
                            MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, dock.getMcKey(), dock, "", nextBlock.getBlockNo(), "", "");
                            Thread.sleep(100);
                            MsgSender.send03(Message03._CycleOrder.moveCarryGoods, dock.getMcKey(), nextBlock, "", dock.getBlockNo(), "", "");
                        } else {
                            //等待可以移栽卸货
                        }
                    }
                }
                Transaction.commit();
                Thread.sleep(300);
            } catch (Exception e) {
                Transaction.rollback();
                LogWriter.writeError(DockThread.class, e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
