package com.thread.threads.service.impl.movestorage;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.Block;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.threads.operator.ScarOperator;
import com.thread.threads.service.impl.ScarAndSrmServiceImpl;

/**
 * @Author: ed_chen
 * @Date: Create in 12:02 2018/8/8
 * @Description:
 * @Modified By:
 */
public class ScarMoveStorageService extends ScarAndSrmServiceImpl {
    private SCar sCar;

    public ScarMoveStorageService(SCar sCar) {
        super(sCar);
        this.sCar = sCar;
    }

    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
        ScarOperator operator = new ScarOperator(sCar, asrsJob.getMcKey());
        Srm srm = (Srm) Block.getByBlockNo(sCar.getOnMCar());
        if(asrsJob.getStatus().equals(AsrsJobStatus.ACCEPT)) {
            //如果任务位accept状态就尝试向源货位下车
            Location location = Location.getByLocationNo(asrsJob.getFromLocation());
            if (srm != null)
                operator.tryOffSrm(srm.getBlockNo(), location);
        }
    }

    @Override
    public void withMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
        ScarOperator operator = new ScarOperator(sCar, asrsJob.getMcKey());

        operator.tryMoveGoods(asrsJob.getFromLocation());
    }
}
