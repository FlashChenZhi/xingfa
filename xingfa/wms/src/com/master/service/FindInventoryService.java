package com.master.service;

import com.util.common.LogMessage;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 23:48 2018/3/4
 * @Description:
 * @Modified By:
 */
@Service
public class FindInventoryService {

    /*
     * @author：ed_chen
     * @date：2018/3/5 10:01
     * @description：获取库存信息
     * @param startIndex 开始记录数
     * @param defaultPageSize 页面展示数据最大值
     * @param containerNo 托盘号
     * @param locationNo 位置id
     * @param productId 商品代码
     * @return：com.util.common.PagerReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     */
    public PagerReturnObj<List<Map<String,Object>>> findInventory(int startIndex, int defaultPageSize,
                                         String containerNo,String locationNo,String productId){
        PagerReturnObj<List<Map<String,Object>>> returnObj = new PagerReturnObj<List<Map<String,Object>>>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            StringBuffer sb1 = new StringBuffer("select max(a.id) as id,a.skuCode as skuCode,a.skuName as skuName," +
                    "SUM(a.qty) as sumQty,max(a.storeDate+' '+a.storeTime) as dateTime from Inventory a where 1=1 ");
            StringBuffer sb2 = new StringBuffer("select count(*) from Inventory where id in (select max(id) from Inventory a where 1=1 ");
            if(StringUtils.isNotBlank(containerNo)){
                sb1.append("and a.container.id = :containerNo ");
                sb2.append("and a.container.id = :containerNo ");
            }
            if(StringUtils.isNotBlank(locationNo)){
                sb1.append("and a.container.location.locationNo = :locationNo ");
                sb2.append("and a.container.location.locationNo = :locationNo ");
            }
            if(StringUtils.isNotBlank(productId)){
                sb1.append("and a.skuCode = :skuCode ");
                sb2.append("and a.skuCode = :skuCode ");
            }
            sb1.append(" group by a.skuCode,a.skuName");
            sb2.append(" group by a.skuCode)");

            Query query1 = session.createQuery( sb1.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Query query2 = session.createQuery(sb2.toString());

            query1.setFirstResult(startIndex);
            query1.setMaxResults(defaultPageSize);

            if(StringUtils.isNotBlank(containerNo)){
                query1.setString("containerNo",containerNo);
                query2.setString("containerNo",containerNo);
            }
            if(StringUtils.isNotBlank(locationNo)){
                query1.setString("locationNo",locationNo);
                query2.setString("locationNo",locationNo);
            }
            if(StringUtils.isNotBlank(productId)){
                query1.setString("skuCode",productId);
                query2.setString("skuCode",productId);
            }
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
    /*
     * @author：ed_chen
     * @date：2018/3/6 23:53
     * @description：查找库存详情
     * @param skuCode
     * @param startIndex
     * @param defaultPageSize
     * @param containerNo
     * @param locationNo
     * @return：com.util.common.PagerReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     */
    public PagerReturnObj<List<Map<String,Object>>> findInventoryDetails(String skuCode,int startIndex, int defaultPageSize,
                                                  String containerNo,String locationNo){
        PagerReturnObj<List<Map<String,Object>>> returnObj = new PagerReturnObj<List<Map<String,Object>>>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            StringBuffer sb1 = new StringBuffer("select a.id as id,a.skuCode as skuCode,a.skuName as skuName,a.qty as qty,a.storeDate+' '+a.storeTime as dateTime, " +
                    "a.container.location.bank as bank,a.container.location.bay as bay,a.container.location.level as level," +
                    "a.container.id as containerId from Inventory a where 1=1 ");
            StringBuffer sb2 = new StringBuffer("select count(*) from Inventory a where 1=1 ");
            if(StringUtils.isNotBlank(containerNo)){
                sb1.append("and a.container.id = :containerNo ");
                sb2.append("and a.container.id = :containerNo ");
            }
            if(StringUtils.isNotBlank(locationNo)){
                sb1.append("and a.container.location.locationNo = :locationNo ");
                sb2.append("and a.container.location.locationNo = :locationNo ");
            }
            sb1.append(" and a.skuCode=:skuCode");
            sb2.append(" and a.skuCode =:skuCode");

            Query query1 = session.createQuery(sb1.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Query query2 = session.createQuery(sb2.toString());

            query1.setFirstResult(startIndex);
            query1.setMaxResults(defaultPageSize);

            query1.setString("skuCode",skuCode);
            query2.setString("skuCode",skuCode);
            if(StringUtils.isNotBlank(containerNo)){
                query1.setString("containerNo",containerNo);
                query2.setString("containerNo",containerNo);
            }
            if(StringUtils.isNotBlank(locationNo)){
                query1.setString("locationNo",locationNo);
                query2.setString("locationNo",locationNo);
            }
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


    /*
     * @author：ed_chen
     * @date：2018/3/5 10:02
     * @description： 获取商品代码
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    public ReturnObj<List<Map<String,String>>> getCommodityCode() throws IOException {
        ReturnObj<List<Map<String, String>>> s = new ReturnObj();
        System.out.println("进入获取商品代码方法！");
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("select skuCode,skuName from Inventory group by skuCode,skuName");
            List<Object[]> retList = query.list();
            List<Map<String,String>> mapList = new ArrayList<>();
            for (Object[] objects: retList) {
                Map<String, String> map = new HashMap();
                map.put("skuCode", objects[0].toString() );
                map.put("skuName",objects[1].toString() );
                mapList.add(map);
            }
            s.setSuccess(true);
            s.setRes(mapList);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            s.setSuccess(false);
            s.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return s;
    }
}
