package com.thread.threads.service.impl;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
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

        Block block = mCar.getPreBlockByJobType(AsrsJobType.RETRIEVAL);
        if(StringUtils.isNotBlank(block.getMcKey())){
            AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
            if(AsrsJobType.RETRIEVAL.equals(asrsJob.getType())){
                mCar.setReservedMcKey(asrsJob.getMcKey());
                return;
            }
        }
        block = mCar.getPreBlockByJobType(AsrsJobType.PUTAWAY);
        if(StringUtils.isNotBlank(block.getMcKey())){
            AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
            if(AsrsJobType.PUTAWAY.equals(asrsJob.getType())){
                mCar.setReservedMcKey(asrsJob.getMcKey());
                return;
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
