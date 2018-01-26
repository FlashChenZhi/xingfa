package com.asrs.thread;

import com.util.common.*;
import com.util.hibernate.HibernateUtil;
import com.wms.domain.*;
import com.wms.domain.blocks.*;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangfan
 * Created on 2017/3/11.
 */
public class JobDoHelp {

    /**
     * 入库完成
     *
     * @param mckey
     */
    public static void finishPutaway(String mckey) {
        LogWriter.info(LoggerType.WMS, "强制完成入库任务mckey" + mckey);
        Job job = Job.getByMcKey(mckey);
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
        Srm crane = (Srm) Block.getByBlockNo("ML01");
        SCar sCar = (SCar) Block.getByBlockNo("SC01");
        StationBlock station = (StationBlock) Block.getByBlockNo("0001");


        if (job != null) {

            Location toLocation = job.getToLocation();
            List<JobDetail> details = new ArrayList<JobDetail>(job.getJobDetails());
            for (JobDetail detail : details) {

                Container container = detail.getInventory().getContainer();
                container.setLocation(toLocation);
                toLocation.setEmpty(false);
                toLocation.setReserved(false);
                Inventory inventory = detail.getInventory();

                StoreResult storeResult = new StoreResult();
                storeResult.setQty(inventory.getQty());
                storeResult.setSyncFlag("1");
                storeResult.setLocationNo(job.getToLocation().getLocationNo());
                storeResult.setPalletBarcode(container.getBarcode());
                storeResult.setStoreDate(DateFormat.format(new Date(), DateFormat.YYYYMMDD));
                storeResult.setStoreTime(DateFormat.format(new Date(), DateFormat.HHMMSS));
                HibernateUtil.getCurrentSession().saveOrUpdate(storeResult);

            }
        }

        if (mckey.equals(station.getMcKey())) {
            station.setMcKey(null);
        }

        if (mckey.equals(sCar.getMcKey())
                || mckey.equals(sCar.getReservedMcKey())) {

            sCar.setMcKey(null);
            sCar.setOnMCar("ML01");
            sCar.setWaitingResponse(false);
        }

        if (mckey.equals(crane.getMcKey())
                || mckey.equals(crane.getReservedMcKey())) {

            crane.setsCarBlockNo("SC01");
            crane.setWaitingResponse(false);

        }

        HibernateUtil.getCurrentSession().delete(job);
        HibernateUtil.getCurrentSession().delete(asrsJob);

    }

    /**
     * 入库取消
     *
     * @param mckey
     */
    public static void cancelPutaway(String mckey) {
        LogWriter.info(LoggerType.WMS, "强制取消入库任务mckey" + mckey);
        Srm crane = (Srm) Block.getByBlockNo("ML01");
        SCar sCar = (SCar) Block.getByBlockNo("SC01");
        StationBlock station = (StationBlock) Block.getByBlockNo("0001");

        if (mckey != null) {


            sCar.setMcKey(null);
            sCar.setReservedMcKey(null);

            if (mckey.equals(sCar.getMcKey())
                    || mckey.equals(sCar.getReservedMcKey())) {
                sCar.setMcKey(null);
                sCar.setReservedMcKey(null);

                sCar.setOnMCar("ML01");
                sCar.setWaitingResponse(false);


            }
            if (mckey.equals(crane.getMcKey())
                    || mckey.equals(crane.getReservedMcKey())) {

                crane.setMcKey(null);
                crane.setReservedMcKey(null);

                crane.setsCarBlockNo("SC01");
                crane.setWaitingResponse(false);

                crane.setBay(2);
                crane.setLevel(0);

            }

            if (mckey.equals(station.getMcKey())) {
                station.setMcKey(null);
            }

            Query query = HibernateUtil.getCurrentSession().createQuery("from Job where mcKey=:mckey");
            query.setMaxResults(1);
            query.setParameter("mckey", mckey);
            Job job = (Job) query.uniqueResult();

            if (job != null) {

                List<JobDetail> detailSet = new ArrayList<JobDetail>(job.getJobDetails());
                for (JobDetail jobDetail : detailSet) {
                    Inventory inventory = jobDetail.getInventory();
                    HibernateUtil.getCurrentSession().delete(inventory);

//                    if (inventory.getSku() != null) {
//                        ReceivingPlan receivingPlan = ReceivingPlan.getByLotNum(inventory.getBatchNo(), inventory.getSku().getSkuCode());
//                        if (receivingPlan != null) {
//                            receivingPlan.setRecvedQty(receivingPlan.getRecvedQty().subtract(inventory.getQty()));
//                            HibernateUtil.getCurrentSession().update(receivingPlan);
//                        }
//                    }
                }

                Location location = job.getToLocation();
                if (location != null) {
                    location.setReserved(false);
                    location.setEmpty(true);
                }
                HibernateUtil.getCurrentSession().delete(job);

            }

            Query asrsJobQ = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where mcKey=:mckey");
            asrsJobQ.setParameter("mckey", mckey);
            asrsJobQ.setMaxResults(1);
            AsrsJob asrsJob = (AsrsJob) asrsJobQ.uniqueResult();
            if (asrsJob != null)
                HibernateUtil.getCurrentSession().delete(asrsJob);

        }


    }

    /**
     * 出库取消
     *
     * @param mckey
     */
    public static void retirevalCancel(String mckey) {
        LogWriter.info(LoggerType.WMS, "强制取消出库任务mckey" + mckey);
        Job job = Job.getByMcKey(mckey);
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);

        Srm crane = (Srm) Block.getByBlockNo("ML01");
        SCar sCar = (SCar) Block.getByBlockNo("SC01");
        StationBlock station = (StationBlock) Block.getByBlockNo("0003");

        if (mckey.equals(station.getMcKey()) || mckey.equals(station.getReservedMcKey())) {
            station.setWaitingResponse(false);
            station.setMcKey(null);
            station.setReservedMcKey(null);
        }
        if (mckey.equals(sCar.getMcKey()) || mckey.equals(sCar.getReservedMcKey())) {
            sCar.setMcKey(null);
            sCar.setReservedMcKey(null);
            sCar.setWaitingResponse(false);
            sCar.setOnMCar("ML01");
        }
        if (mckey.equals(crane.getMcKey()) || mckey.equals(crane.getReservedMcKey())) {
            crane.setWaitingResponse(false);
            crane.setMcKey(null);
            crane.setReservedMcKey(null);
            crane.setsCarBlockNo("SC01");
        }
        if (job != null)
            HibernateUtil.getCurrentSession().delete(job);
        if (asrsJob != null)
            HibernateUtil.getCurrentSession().delete(asrsJob);

    }

    /**
     * 出库完成
     *
     * @param mckey
     */
    public static void retrievalFinish(String mckey) {
        LogWriter.info(LoggerType.WMS, "强制完成出库任务mckey" + mckey);

        Job job = Job.getByMcKey(mckey);
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);

        Srm crane = (Srm) Block.getByBlockNo("ML01");
        SCar sCar = (SCar) Block.getByBlockNo("SC01");
        StationBlock station = (StationBlock) Block.getByBlockNo("0003");

        if (mckey.equals(station.getMcKey()) || mckey.equals(station.getReservedMcKey())) {
            station.setReservedMcKey(null);
            station.setMcKey(null);
            station.setWaitingResponse(false);
        }

        Container c = Container.getByBarcode(job.getContainer());

        List<JobDetail> details = new ArrayList<JobDetail>(job.getJobDetails());
        for (JobDetail detail : details) {
            Inventory inventory = detail.getInventory();

            org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from ReceivingPlan where batchNo =:batchNo").setMaxResults(1);
//            query.setParameter("batchNo", inventory.getBatchNo());
            ReceivingPlan receivingPlan = (ReceivingPlan) query.uniqueResult();
            receivingPlan.setRecvedQty(receivingPlan.getRecvedQty().subtract(inventory.getQty()));
            HibernateUtil.getCurrentSession().update(receivingPlan);

            Location location = job.getFromLocation();
            location.setEmpty(true);
            location.setRetrievalRestricted(false);
            HibernateUtil.getCurrentSession().update(location);

            HibernateUtil.getCurrentSession().delete(inventory);
            HibernateUtil.getCurrentSession().delete(c);


        }

        if (mckey.equals(sCar.getMcKey()) || mckey.equals(sCar.getReservedMcKey())) {
            sCar.setOnMCar("ML01");
            crane.setsCarBlockNo("SC01");
            sCar.setReservedMcKey(null);
            sCar.setMcKey(null);
            crane.setReservedMcKey(null);
            crane.setMcKey(null);
            sCar.setWaitingResponse(false);

        }

        if (mckey.equals(crane.getMcKey()) || mckey.equals(crane.getReservedMcKey())) {
            crane.setMcKey(null);
            crane.setReservedMcKey(null);
            crane.setWaitingResponse(false);

            crane.setBay(2);
            crane.setLevel(0);

        }

        if (job != null) {
            HibernateUtil.getCurrentSession().delete(job);
        }

        if (asrsJob != null) {
            HibernateUtil.getCurrentSession().delete(asrsJob);
        }


    }
}
