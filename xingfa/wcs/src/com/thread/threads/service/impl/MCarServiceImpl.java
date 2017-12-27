package com.thread.threads.service.impl;

import com.asrs.business.consts.AsrsJobType;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.threads.operator.MCarOperator;
import com.thread.threads.service.MCarService;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by van on 2017/10/31.
 */
public class MCarServiceImpl implements MCarService {

    private MCar mCar;

    public MCarServiceImpl(MCar mCar) {
        this.mCar = mCar;
    }

    @Override
    public void withOutJob() throws Exception {

        //湖北兴发母车寻找任务。此处母车只做搬运，出入库只按照前后端输送机的mckey找任务
        //出库任务
        boolean hasJob = false;
        Block retrievalPreBlock = mCar.getPreBlockByJobType(AsrsJobType.RETRIEVAL);
        if (retrievalPreBlock != null) {
            if (StringUtils.isNotBlank(retrievalPreBlock.getMcKey()) && !retrievalPreBlock.isWaitingResponse()) {
                mCar.setReservedMcKey(retrievalPreBlock.getMcKey());
                hasJob = true;
            }
        }

        if (!hasJob) {
            Block putawayPreBlock = mCar.getPreBlockByJobType(AsrsJobType.PUTAWAY);
            if (putawayPreBlock != null) {
                if (StringUtils.isNotBlank(putawayPreBlock.getMcKey()) && !putawayPreBlock.isWaitingResponse()) {
                    mCar.setReservedMcKey(putawayPreBlock.getMcKey());
                    hasJob = true;
                }
            }
        }

        if (!hasJob) {
            if (mCar.getCycle().equals(mCar.getDock())) {
                MCarOperator mCarOperator = new MCarOperator(mCar, "9999");
                mCarOperator.cycle();
            }
        }

    }

    @Override
    public void withReserveMckey() throws Exception {

    }

    @Override
    public void withMckey() throws Exception {

    }
}
