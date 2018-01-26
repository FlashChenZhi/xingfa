package com.thread.threads.service.impl.retrieval;

import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.thread.blocks.Block;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.thread.threads.operator.SrmOperator;
import com.thread.threads.service.impl.SrmAndScarServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;

/**
 * Created by van on 2017/11/2.
 */
public class SrmAndScarRetrievalService extends SrmAndScarServiceImpl {

    private Srm srm;

    public SrmAndScarRetrievalService(Block block) {
        super(block);
        srm = (Srm) block;
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
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(srm.getMcKey());
        SrmOperator operator = new SrmOperator(srm, asrsJob.getMcKey());

        if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
            SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());

            if (StringUtils.isNotBlank(sCar.getReservedMcKey())
                    && !sCar.getReservedMcKey().equals(srm.getMcKey())) {

                AsrsJob newJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                Location newLocation = Location.getByLocationNo(newJob.getFromLocation());

                if (newLocation.getLevel() == srm.getLevel()
                        && newLocation.getBay() == srm.getBay()) {
                    //优先卸子车
                    operator.unLoadCarFirst(sCar, sCar.getReservedMcKey());
                } else {
                    //是否先卸子车
                    boolean flag = unLoadCarFirst();
                    if (flag) {
                        operator.unLoadCarFirst(sCar, sCar.getReservedMcKey());
                    } else {
                        //有限卸货
                        operator.tryUnloadGoods();
                    }
                }
            } else {

                if (sCar.isWaitingResponse()) {
                    if (StringUtils.isNotBlank(sCar.getMcKey())) {

                    } else {
                        AsrsJob newJob = AsrsJob.getAsrsJobByMcKey(sCar.getReservedMcKey());
                        Location newLocation = Location.getByLocationNo(newJob.getFromLocation());

                        if (newLocation.getLevel() == srm.getLevel()
                                && newLocation.getBay() == srm.getBay()) {
                            //优先卸子车
                            operator.unLoadCarFirst(sCar, sCar.getReservedMcKey());
                        } else {
                            //是否先卸子车
                            boolean flag = unLoadCarFirst();
                            if (flag) {
                                operator.unLoadCarFirst(sCar, sCar.getReservedMcKey());
                            } else {
                                //有限卸货
                                operator.tryUnloadGoods();
                            }
                        }

                    }
                } else {
                    //移动升降机上没有子车
                    operator.tryUnloadGoods();
                }
            }


        } else {
            //移动升降机上没有子车
            operator.tryUnloadGoods();
        }
    }

    private boolean unLoadCarFirst() {
        int t1_1 = 0;//   升降机移动到出库货格时间 t1ˊ=行走距离÷160m/min+1秒
        int t1_2 = 0;//  升降机移动到出库货格时间  t1〞=升降距离÷40m/min+1秒
        int t1 = t1_1 > t1_2 ? t1_1 : t1_2;
        int t2 = 5;
        int t3_1 = 0;//  升降机移动到出库货格时间  t3ˊ=行走距离÷160m/min+1秒
        int t3_2 = 0;// 升降机移动到出库货格时间   t3〞=升降距离÷40m/min+1秒
        int t3 = t3_1 > t3_2 ? t3_1 : t3_2;
        int t4 = 4;
        int t5 = 0;//子车往返时间  t5=往返距离÷60m/min+1秒
        int t6 = 5;
        int time1 = t1 + t2 + t3 + t4 + t5 + t6;

        int T1_1 = 0;//  升降机移动到出库货格时间  t1ˊ=行走距离÷160m/min+1秒
        int T1_2 = 0;//   升降机移动到出库货格时间 t1〞=升降距离÷40m/min+1秒
        int T1 = T1_1 > T1_2 ? T1_1 : T1_2;
        int T2 = 5;
        int T3 = 0;//子车往返时间 t3=往返距离÷60m/min+1秒
        int T4_1 = 0;//升降机移动到出库站台时间    t4ˊ=行走距离÷160m/min+1秒
        int T4_2 = 0;//    t4〞=升降距离÷40m/min+1秒
        int T4 = T4_1 > T4_2 ? T4_1 : T4_2;
        int T5 = 7;
        int T6_1 = 0;//  升降机移动到出库货格时间  t3ˊ=行走距离÷160m/min+1秒
        int T6_2 = 0;// 升降机移动到出库货格时间   t3〞=升降距离÷40m/min+1秒
        int T6 = T6_1 > T6_2 ? T6_1 : T6_2;
        int T7 = 7;
        int time2 = T1 + T2 + ((T3 + T4 + T5) > T6 ? (T3 + T4 + T5) : T6) + T7;


        return time1 > time2;
    }

}
