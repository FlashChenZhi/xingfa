package com.thread.threads.service.impl.movestorage;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.Block;
import com.thread.blocks.Srm;
import com.thread.threads.operator.SrmOperator;
import com.thread.threads.service.impl.SrmAndScarServiceImpl;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: ed_chen
 * @Date: Create in 12:02 2018/8/8
 * @Description:
 * @Modified By:
 */
public class SrmMoveStorageService extends SrmAndScarServiceImpl {
    public Srm srm;

    public SrmMoveStorageService(Srm block) {
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

    }
}
