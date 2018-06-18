package com.master.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.XMLbean.XMLList.TransportOrder;
import com.master.vo.SkuVo;
import com.master.vo.SkuVo2;
import com.util.common.BaseReturnObj;
import com.util.common.LogMessage;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PutInStorageService {
    /**
     * 获取商品代码
     *
     * @return
     * @throws IOException
     */
    public ReturnObj<List<SkuVo2>> getCommodityCode() {
        System.out.println("进入获取商品代码方法！");
        ReturnObj<List<SkuVo2>> returnObj = new ReturnObj<>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("from Sku");
            List<Sku> skuList = query.list();
            List<SkuVo2> mapList = new ArrayList<>();
            for (Sku sku : skuList) {
                SkuVo2  vo= new SkuVo2();
                vo.setId(sku.getSkuCode());
                vo.setName(sku.getSkuName());
                mapList.add(vo);

            }
            returnObj.setSuccess(true);
            returnObj.setRes(mapList);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return returnObj;
    }

    /**
     * 设定任务
     * @param tuopanhao     托盘号
     * @param zhantai       站台
     * @param commodityCode 货品代码
     * @param num           数量
     * @return "0"设定成功，"1"设定失败
     * @throws IOException
     */

    public BaseReturnObj addTask(String tuopanhao, String zhantai, String commodityCode,String lotNo, int num) {
        BaseReturnObj returnObj = new BaseReturnObj();
        if(tuopanhao.length()!=10){
            returnObj.setSuccess(false);
            returnObj.setMsg("托盘号不正确!");
            Transaction.rollback();
            return returnObj;
        }
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Sku sku = Sku.getByCode(commodityCode);
            if (sku == null) {
                returnObj.setSuccess(false);
                returnObj.setMsg("商品不存在!");
                Transaction.rollback();
                return returnObj;
            }
            Container container = Container.getByBarcode(tuopanhao);
            if (container != null) {
                returnObj.setSuccess(false);
                returnObj.setMsg("托盘号已存在!");
                Transaction.rollback();
                return returnObj;
            }
            Query query = HibernateUtil.getCurrentSession().createQuery("from InventoryView iv where iv.palletCode = :palletCode")
                    .setString("palletCode", tuopanhao);
            InventoryView inventoryView = (InventoryView) query.uniqueResult();
            if (inventoryView != null) {
                returnObj.setSuccess(false);
                returnObj.setMsg("托盘号已存在");
                Transaction.rollback();
                return returnObj;
            }
            Job job = new Job();
            session.save(job);
            job.setFromStation(zhantai);
            job.setContainer(tuopanhao);
            job.setSendReport(false);
            job.setCreateDate(new Date());
            if (zhantai.equals("1101")) {
                job.setToStation("ML01");
            }
            if (zhantai.equals("1301")) {
                job.setToStation("ML02");
            }
            job.setType(AsrsJobType.PUTAWAY);
            job.setMcKey(Mckey.getNext());
            job.setStatus(AsrsJobStatus.WAITING);

            JobDetail jobDetail = new JobDetail();
            session.save(jobDetail);
            jobDetail.setJob(job);
            jobDetail.setQty(new BigDecimal(num));

            inventoryView = new InventoryView();
            session.save(inventoryView);
            inventoryView.setPalletCode(tuopanhao);
            inventoryView.setQty(new BigDecimal(num));
            inventoryView.setSkuCode(commodityCode);
            inventoryView.setSkuName(sku.getSkuName());
            inventoryView.setWhCode(sku.getCangkudaima());
            inventoryView.setLotNum(lotNo);

            returnObj.setSuccess(true);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return returnObj;
    }
    /*
     * @author：ed_chen
     * @date：2018/3/4 17:49
     * @description： 查询入库设定任务记录
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    public PagerReturnObj<List<Map<String,Object>>> findPutInStorageOrder(int startIndex, int defaultPageSize) {
        PagerReturnObj<List<Map<String,Object>>> returnObj = new PagerReturnObj<List<Map<String,Object>>>();
        try {
            Transaction.begin();
            List<String> list = new ArrayList<>();
            list.add("01");
            list.add("14");
            Session session = HibernateUtil.getCurrentSession();
            Query query1 = session.createQuery("select j.id as id,j.createDate as createDate,j.mcKey as mcKey,j.container as containerId,b.qty as qty, " +
                    "b.skuCode as skuCode,b.skuName as skuName,j.fromStation as fromStation,j.toStation as toStation,b.lotNum as lotNo, " +
                    "case j.type when '01' then '入库' when '03' then '出库' when '14' then '抽检入库' else '其他' end as type,j.status as status " +
                    "from Job j, InventoryView b where j.container=b.palletCode order by j.createDate desc,j.id desc").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Query query2 = session.createQuery("select count(1) from Job j, InventoryView b where  j.container=b.palletCode and j.type in (:tp)");
            query1.setFirstResult(startIndex);
            query1.setMaxResults(defaultPageSize);
            List<Map<String,Object>> jobList = query1.list();
            query2.setParameterList("tp", list);
            Long count = (Long) query2.uniqueResult();
            returnObj.setSuccess(true);
            returnObj.setRes(jobList);
            returnObj.setCount(count);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return returnObj;
    }
    /*
     * @author：ed_chen
     * @date：2018/3/10 18:34
     * @description：删除入库任务
     * @param selectedRowKeysString
     * @return：com.util.common.BaseReturnObj
     */
    public BaseReturnObj deleteTask(String selectedRowKeysString) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            String[] sId = selectedRowKeysString.split(",");
            boolean flag = true;
            for(int i = 0;i<sId.length;i++){
                int id = Integer.valueOf(sId[i]);
                Job job = Job.getById(id);
                AsrsJob asrsJob= AsrsJob.getAsrsJobByMcKey(job.getMcKey());
                if(asrsJob==null && job.getType().equals("01")){
                    Query query = session.createQuery("delete from InventoryView i where i.palletCode = :palletCode ");
                    query.setString("palletCode",job.getContainer());
                    query.executeUpdate();
                    job.getJobDetails().clear();
                    session.delete(job);
                }else{
                    flag= false;
                    break;
                }
            }
            if(flag){
                returnObj.setSuccess(true);
                Transaction.commit();
            }else{
                Transaction.rollback();
                returnObj.setSuccess(false);
                returnObj.setMsg("请查验所删入库任务是否在执行状态！或此任务是否是入库任务");
            }
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return returnObj;
    }

    /*
     * @author：ed_chen
     * @date：2018/6/9 14:49
     * @description：获取抽检入库货品信息
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     */
    public ReturnObj<Map<String,Object>> getCheckStorageData() {
        ReturnObj<Map<String,Object>> returnObj = new ReturnObj<>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Map<String,Object> map = new HashMap<>();
            int count = TransportOrderLog.getTransportOrderCountByType();
            if(count==1){
                TransportOrderLog transportOrderLog = TransportOrderLog.getTransportOrderByType();
                Inventory inventory = transportOrderLog.getContainer().getInventories().iterator().next();
                map.put("tuopanhao", transportOrderLog.getContainer().getBarcode());
                map.put("Num", inventory.getQty());
                map.put("commodityCode", Sku.getByCode(inventory.getSkuCode()).getSkuName());
                map.put("lotNo", inventory.getLotNum());
                map.put("station", "1101");
                returnObj.setSuccess(true);
                returnObj.setRes(map);
            }else if(count==0){
                returnObj.setSuccess(true);
                returnObj.setRes(map);
                returnObj.setMsg("不存在任务");
            }else{
                returnObj.setSuccess(false);
                returnObj.setMsg("存在多个抽检任务！");
            }
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return returnObj;
    }

    /*
     * @author：ed_chen
     * @date：2018/6/9 16:24
     * @description：设定抽检入库任务
     * @param num
     * @return：com.util.common.BaseReturnObj
     */
    public BaseReturnObj addCheckInStorage(int num) {
        BaseReturnObj returnObj = new BaseReturnObj();

        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            int count = TransportOrderLog.getTransportOrderCountByType();
            if(count==1) {
                TransportOrderLog transportOrderLog = TransportOrderLog.getTransportOrderByType();

                if (transportOrderLog == null) {
                    returnObj.setSuccess(false);
                    returnObj.setMsg("不存在待抽检入库任务!");
                    Transaction.rollback();
                    return returnObj;
                }

                Query query = session.createQuery("from Job j where j.fromLocation.locationNo=:fromLocation");
                query.setParameter("fromLocation", transportOrderLog.getFromLocation().getLocationNo());
                query.setMaxResults(1);
                Job job = (Job) query.uniqueResult();
                if(job!=null){
                    returnObj.setSuccess(false);
                    returnObj.setMsg("存在此货位的抽检出库任务!");
                    Transaction.rollback();
                    return returnObj;
                }

                Query query2 = session.createQuery("from Job j where j.toLocation.locationNo =:fromLocation");
                query2.setParameter("fromLocation", transportOrderLog.getFromLocation().getLocationNo());
                query2.setMaxResults(1);
                Job job2 = (Job) query2.uniqueResult();
                if(job2!=null){
                    returnObj.setSuccess(false);
                    returnObj.setMsg("存在此货位的抽检入库任务!");
                    Transaction.rollback();
                    return returnObj;
                }
                //生成抽检入库任务
                createRuKu(transportOrderLog, num);

                returnObj.setSuccess(true);
                Transaction.commit();
            }else if(count==0){
                returnObj.setSuccess(true);
                returnObj.setMsg("不存在任务");
            }else{
                returnObj.setSuccess(false);
                returnObj.setMsg("存在多个抽检任务！");
            }
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return returnObj;
    }

    /*
     * @author：ed_chen
     * @date：2018/6/9 16:24
     * @description：抽检回库取消任务
     * @param num
     * @return：com.util.common.BaseReturnObj
     */
    public BaseReturnObj submitCancle() {
        BaseReturnObj returnObj = new BaseReturnObj();

        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            int count = TransportOrderLog.getTransportOrderCountByType();
            if(count==1) {
                TransportOrderLog transportOrderLog = TransportOrderLog.getTransportOrderByType();

                if (transportOrderLog == null) {
                    returnObj.setSuccess(false);
                    returnObj.setMsg("不存在待抽检入库任务!");
                    Transaction.rollback();
                    return returnObj;
                }

                Query query = session.createQuery("from Job j where j.fromLocation.locationNo=:fromLocation");
                query.setParameter("fromLocation", transportOrderLog.getFromLocation().getLocationNo());
                query.setMaxResults(1);
                Job job = (Job) query.uniqueResult();
                if(job!=null){
                    returnObj.setSuccess(false);
                    returnObj.setMsg("存在此货位的抽检出库任务!");
                    Transaction.rollback();
                    return returnObj;
                }

                Query query2 = session.createQuery("from Job j where j.toLocation.locationNo =:fromLocation");
                query2.setParameter("fromLocation", transportOrderLog.getFromLocation().getLocationNo());
                query2.setMaxResults(1);
                Job job2 = (Job) query2.uniqueResult();
                if(job2!=null){
                    returnObj.setSuccess(false);
                    returnObj.setMsg("存在此货位的抽检入库任务!");
                    Transaction.rollback();
                    return returnObj;
                }
                //所有完成后，若任务为抽检入库
                List<TransportOrderLog> transportOrderLogList = TransportOrderLog.getTransportOrderByType2();
                //生成回库任务
                if(transportOrderLogList.size()!=0){
                    for(TransportOrderLog transportOrderLog1 :transportOrderLogList){
                        yiku(session,transportOrderLog1.getToLocation().getLocationNo(),transportOrderLog1.getFromLocation());
                    }
                }
                //删除抽检的库存
                Container container=transportOrderLog.getContainer();


                session.delete(container);
                session.delete(transportOrderLog);

                returnObj.setSuccess(true);
                Transaction.commit();
            }else if(count==0){
                returnObj.setSuccess(true);
                returnObj.setMsg("不存在任务");
            }else{
                returnObj.setSuccess(false);
                returnObj.setMsg("存在多个抽检任务！");
            }
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return returnObj;
    }


    public void createRuKu(TransportOrderLog transportOrderLog,int num){
        Session session = HibernateUtil.getCurrentSession();
        Inventory inventory = transportOrderLog.getContainer().getInventories().iterator().next();
        Job job = new Job();
        session.save(job);
        job.setFromStation("1101");
        job.setContainer(transportOrderLog.getContainer().getBarcode());
        job.setSendReport(false);
        job.setCreateDate(new Date());
        job.setToStation("ML01");
        //在job中存入此抽检货物原货位
        job.setToLocation(transportOrderLog.getFromLocation());

        job.setType(AsrsJobType.CHECKINSTORAGE);
        job.setMcKey(Mckey.getNext());
        job.setStatus(AsrsJobStatus.WAITING);

        JobDetail jobDetail = new JobDetail();
        session.save(jobDetail);
        jobDetail.setJob(job);
        jobDetail.setQty(new BigDecimal(num));
        jobDetail.setInventory(inventory);

        InventoryView inventoryView = new InventoryView();
        session.save(inventoryView);
        inventoryView.setPalletCode(transportOrderLog.getContainer().getBarcode());
        inventoryView.setQty(new BigDecimal(num));
        inventoryView.setSkuCode(inventory.getSkuCode());
        inventoryView.setSkuName(inventory.getSkuName());
        inventoryView.setWhCode(inventory.getWhCode());
        inventoryView.setLotNum(inventory.getLotNum());
    }

    public void yiku(Session session,String locationNo,Location toLocation){
        Query query2 = session.createQuery("from Inventory i where i.container.location.locationNo = :locationNo");
        query2.setString("locationNo",locationNo);
        List<Inventory> inventoryList = query2.list();
        Inventory inventory =inventoryList.get(0);
        int qty = inventory.getQty().intValue();//货品数量

        String outPosition = inventory.getContainer().getLocation().getOutPosition();

        JobDetail jobDetail = new JobDetail();
        Job job = new Job();
        //session准备存入job，commit时才会执行sql
        session.save(job);
        session.save(jobDetail);
        //数据准备

        String mckey = Mckey.getNext();
        String toStation = "ML01" ;//到达站台
        String fromStation =  "ML01" ;//出发地点
        String type = AsrsJobType.BACK_PUTAWAY; //抽检回库
        //存入jobDetail
        jobDetail.setInventory(inventory);
        jobDetail.setQty(inventory.getQty());
        //存入job
        job.setContainer(inventory.getContainer().getBarcode());
        job.setFromStation(fromStation);
        job.setMcKey(mckey);
        job.setOrderNo("4200026559");
        job.setSendReport(false);
        job.setStatus("1");
        job.setToStation(toStation);
        job.setType(type);
        job.addJobDetail(jobDetail);
        job.setCreateDate(new Date());
        job.setFromLocation(inventory.getContainer().getLocation());
        //要比出库多一个toLocation
        job.setToLocation(toLocation);

        //修改此托盘
        Container container = inventory.getContainer();
        container.setReserved(true);
        session.saveOrUpdate(container);
    }

}



