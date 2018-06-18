package com.thread.threads.service.impl.locationtolocation;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.threads.operator.SrmOperator;
import com.thread.threads.service.impl.SrmAndScarServiceImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: ed_chen
 * @Date: Create in 21:41 2018/6/6
 * @Description:
 * @Modified By:
 */
public class SrmLocationToLocationService extends SrmAndScarServiceImpl {
    public Srm srm;

    public SrmLocationToLocationService(Srm block) {
        super(block);
        this.srm = block;
    }

    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getReservedMcKey());
        SrmOperator operator = new SrmOperator(srm, asrsJob.getMcKey());

        Location toLocation = Location.getByLocationNo(asrsJob.getFromLocation());

        if (StringUtils.isNotEmpty(srm.getsCarBlockNo())) {
            //提升机是否到达指定位置
            if (srm.arrive(toLocation)) {
                //移动提升机移动到指定层列，开始卸子车
                operator.unLoadCar(srm.getsCarBlockNo(), asrsJob.getMcKey(), toLocation);
            } else {
                operator.move(toLocation);
            }
        } else {
            operator.tryLoadCar();
        }

    }

    @Override
    public void withMckey() throws Exception {
        //移动提升机正在执行任务
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getMcKey());

        SrmOperator operator = new SrmOperator(srm, asrsJob.getMcKey());

        //去任务的终点货位
        Location toLocation = Location.getByLocationNo(asrsJob.getToLocation());

        if (com.util.common.StringUtils.isNotEmpty(srm.getsCarBlockNo())) {

            SCar sCar=(SCar) SCar.getByBlockNo(srm.getsCarBlockNo());

            if(com.util.common.StringUtils.isNotEmpty(sCar.getOnMCar())){
                //移动提升机上有子车,并且小车上面有提升机
                //判断移动提升机的位置排列层，实绩位置，和目标货位是否一致
                if (srm.arrive(toLocation)) {
                    //移动提升机移动到指定层列，开始卸子车
                    operator.unLoadCar(srm.getsCarBlockNo(), asrsJob.getMcKey(), toLocation);
                } else {
                    //移动提升机移动到指定的层，列
                    operator.move(toLocation);
                }
            }
        } else {
            //提升机去找子车，接子车
            operator.tryLoadCar();
        }
    }
}
