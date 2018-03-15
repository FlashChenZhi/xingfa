package com.thread.threads.service.impl.retrieval;

import com.asrs.domain.AsrsJob;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.thread.blocks.MCar;
import com.thread.threads.operator.MCarOperator;
import com.thread.threads.service.impl.MCarServiceImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by van on 2017/12/26.
 * 单独母车用于搬运托盘的实例，前后端都为输送机可参照此例
 */
public class MCarSingleRetrievalService extends MCarServiceImpl {

    private MCar mCar;

    public MCarSingleRetrievalService(MCar mCar) {
        super(mCar);
        this.mCar = mCar;
    }

    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getReservedMcKey());
        MCarOperator operator = new MCarOperator(mCar, asrsJob.getMcKey());
        if (asrsJob.getToStation().equals(mCar.getBlockNo())) {
            //如果母车是目标站台

        } else {
            //如果母车而不是目标站台
            Block block = mCar.getPreBlock(mCar.getReservedMcKey(), asrsJob.getType());
            if (block instanceof Conveyor) {
                //如果上一段是输送机
                operator.tryLoadGoodsFromConveryor(block);
            }
        }
    }

    @Override
    public void withMckey() throws Exception {

        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mCar.getMcKey());
        MCarOperator operator = new MCarOperator(mCar, asrsJob.getMcKey());
        if (asrsJob.getToStation().equals(mCar.getBlockNo())) {
            //如果母车是目标站台
        } else {
            //如果母车而不是目标站台
            Block block = mCar.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
            if (block instanceof Conveyor) {
                //下一段是输送机
                if (!block.isWaitingResponse() && StringUtils.isBlank(block.getMcKey()) && StringUtils.isBlank(block.getReservedMcKey())) {

                    operator.tryUnloadGoodToConveryor(block);
                }

            }
        }
    }
}
