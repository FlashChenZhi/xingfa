package com.thread.threads.service.impl.locationtolocation;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.threads.operator.ScarOperator;
import com.thread.threads.service.impl.ScarAndSrmServiceImpl;
import com.thread.threads.service.impl.SrmAndScarServiceImpl;
import org.apache.commons.lang.StringUtils;

/**
 * @Author: ed_chen
 * @Date: Create in 10:45 2018/6/7
 * @Description:
 * @Modified By:
 */
public class ScarLocationToLocationService extends ScarAndSrmServiceImpl {

    private SCar sCar;

    public ScarLocationToLocationService(SCar sCar) {
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
        }/*else if(asrsJob.getStatus().equals(AsrsJobStatus.PICKING)){
            //如果任务位PICKING状态就尝试向最终货位下车
            Location location = Location.getByLocationNo(asrsJob.getToLocation());
            if (srm != null)
                operator.tryOffSrm(srm.getBlockNo(), location);
        }*/
    }

    @Override
    public void withMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
        ScarOperator operator = new ScarOperator(sCar, asrsJob.getMcKey());
        if(asrsJob.getStatus().equals(AsrsJobStatus.ACCEPT)){
            //如果任务位accept状态就尝试取货
            operator.tryPickingGoods(asrsJob.getFromLocation());
        }else if(asrsJob.getStatus().equals(AsrsJobStatus.PICKING)){
            //如果任务位PICKING状态就尝试卸货
            if (StringUtils.isNotBlank(sCar.getOnMCar())) {
                Srm srm = Srm.getSrmByGroupNo(sCar.getGroupNo());
                operator.tryUnloadGoods(srm);
            }
        }

    }
}
