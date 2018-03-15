package com.thread.threads.service.impl.putaway;

import com.asrs.domain.AsrsJob;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.threads.operator.ScarOperator;
import com.thread.threads.service.impl.ScarAndSrmServiceImpl;
import org.apache.commons.lang.StringUtils;

/**
 * Created by van on 2017/11/9.
 */
public class ScarAndSrmPutawayServcie extends ScarAndSrmServiceImpl {

    private SCar sCar;

    public ScarAndSrmPutawayServcie(SCar sCar) {
        super(sCar);
        this.sCar = sCar;
    }


    @Override
    public void withOutJob()throws Exception {

     }


    @Override
    public void withReserveMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
        ScarOperator operator = new ScarOperator(sCar, asrsJob.getMcKey());
        Srm srm = Srm.getSrmByGroupNo(sCar.getGroupNo());
        if (StringUtils.isBlank(sCar.getOnMCar())) {
            operator.tryOnSrm(srm);
        }else{
            operator.tryUnloadGoods(srm);
        }
    }


    @Override
    public void withMckey() throws Exception {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(sCar.getMcKey());
        ScarOperator operator = new ScarOperator(sCar, asrsJob.getMcKey());
        if (StringUtils.isNotBlank(sCar.getOnMCar())) {
            Srm srm = Srm.getSrmByGroupNo(sCar.getGroupNo());
            operator.tryUnloadGoods(srm);
        }
    }
}
