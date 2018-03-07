package com.master.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
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
    public ReturnObj<List<Map<String,String>>> getCommodityCode() {
        System.out.println("进入获取商品代码方法！");
        ReturnObj<List<Map<String, String>>> returnObj = new ReturnObj<List<Map<String, String>>>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("from Sku");
            List<Sku> skuList = query.list();
            List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
            for (Sku sku : skuList) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", sku.getSkuCode());
                map.put("name", sku.getSkuName());
                mapList.add(map);

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

    public BaseReturnObj addTask(String tuopanhao, String zhantai, String commodityCode, int num) {
        BaseReturnObj returnObj = new BaseReturnObj();
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
        System.out.println("进入获取商品代码方法！");
        PagerReturnObj<List<Map<String,Object>>> returnObj = new PagerReturnObj<List<Map<String,Object>>>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query1 = session.createQuery("select j.id as id,j.createDate as createDate,j.mcKey as mcKey,j.container as containerId,b.qty as qty, " +
                    "b.skuCode as skuCode,b.skuName as skuName,j.fromStation as fromStation,j.toStation as toStation, " +
                    "case j.type when '01' then '入库' when '03' then '出库' else '其他' end as type,j.status as status " +
                    "from Job j, InventoryView b where j.container=b.palletCode order by j.createDate desc,j.id desc").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Query query2 = session.createQuery("select count(1) from Job j, InventoryView b where  j.container=b.palletCode");
            query1.setFirstResult(startIndex);
            query1.setMaxResults(defaultPageSize);
            List<Map<String,Object>> jobList = query1.list();
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

    public BaseReturnObj deleteTask(String selectedRowKeysString) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            String[] sId = selectedRowKeysString.split(",");
            for(int i = 0;i<sId.length;i++){
                int id = Integer.valueOf(sId[i]);
                Job job = (Job) session.get(Job.class,id);
                Query query = session.createQuery("delete from InventoryView i where i.palletCode = :palletCode ");
                query.setString("palletCode",job.getContainer());
                query.executeUpdate();
                session.delete(job);
            }
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
}



