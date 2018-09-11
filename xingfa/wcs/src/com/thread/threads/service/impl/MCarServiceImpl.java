package com.thread.threads.service.impl;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Station;
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

        Block block = mCar.getPreBlockByJobType(AsrsJobType.RETRIEVAL);
        if(StringUtils.isNotBlank(block.getMcKey())){
            //若上一个block上的mckey和母车刚做的mckey一样，则不接受mckey
            if(!block.getMcKey().equals(mCar.getPreviousMcKey())){

                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                if(AsrsJobType.RETRIEVAL.equals(asrsJob.getType())){
                    mCar.setReservedMcKey(asrsJob.getMcKey());
                    return;
                }
            }else{
                return;
            }

        }
        block = mCar.getPreBlockByJobType(AsrsJobType.PUTAWAY);
        if(StringUtils.isNotBlank(block.getMcKey())){

            //若上一个block上的mckey和母车刚做的mckey一样，则不接受mckey
            if(!block.getMcKey().equals(mCar.getPreviousMcKey())) {

                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(block.getMcKey());
                if (AsrsJobType.PUTAWAY.equals(asrsJob.getType())) {
                    mCar.setReservedMcKey(asrsJob.getMcKey());
                    return;
                }
            }else{
                return;
            }

        }
        //若没任务回到指定位置
        Station station = Station.getStation("1301");
        if(station.getMode().equals(AsrsJobType.PUTAWAY)){
            if(StringUtils.isNotBlank(mCar.getDock()) && "0004".equals(mCar.getDock())){

            }else{
                MCarOperator operator = new MCarOperator(mCar, "9999");
                operator.move("0004");
            }

        }else if(station.getMode().equals(AsrsJobType.RETRIEVAL)){
            if(StringUtils.isNotBlank(mCar.getDock()) && "0005".equals(mCar.getDock())){

            }else{
                MCarOperator operator = new MCarOperator(mCar, "9999");
                operator.move("0005");
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
