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
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 14:12 2018/3/31
 * @Description:
 * @Modified By:
 */
@Service
public class FindOutOrInWarehouseService {
    /*
     * @author：ed_chen
     * @date：2018/3/31 14:37
     * @description：获取商品代码
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    public ReturnObj<List<Map<String, String>>> getSkuCode(){
        ReturnObj<List<Map<String, String>>> s = new ReturnObj();
        System.out.println("进入获取Sku代码方法！");
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("select skuCode,skuName from Sku");
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
    /*
     * @author：ed_chen
     * @date：2018/3/31 14:39
     * @description：查找出入库详情
     * @param startIndex
     * @param defaultPageSize
     * @param productId
     * @param beginDate
     * @param endDate
     * @return：com.util.common.PagerReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     */
    public PagerReturnObj<List<Map<String,Object>>> findOutOrInWarehouse(int startIndex, int defaultPageSize,
                                                                   String productId, String beginDate, String endDate){
        PagerReturnObj<List<Map<String,Object>>> returnObj = new PagerReturnObj<List<Map<String,Object>>>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            StringBuffer sb1 = new StringBuffer("select * from (select a.id as id,a.skuCode as skuCode,a.skuName as skuName,a.qty as qty,a.qty as qty2," +
                    "a.STORE_DATE+' '+a.STORE_TIME as dateTime,'入库' as type from xingfa.INVENTORY a where 1=1  ");
            StringBuffer sb2 = new StringBuffer("select count(*) from xingfa.INVENTORY  a where  1=1 ");
            StringBuffer sb3 = new StringBuffer("select count(*) from xingfa.RETRIEVAL_ORDER_LINE b  where  b.dingdanshuliang <= b.wanchengdingdanshuliang ");
            sb1 = getSqlAfter(sb1, productId, beginDate, endDate);
            sb2 = getSqlAfter(sb2, productId, beginDate, endDate);

            sb1.append(" union all");
            sb1.append(" select b.rid as id,b.shangpindaima as skuCode,b.shangpinmingcheng as skuName," +
                    "b.dingdanshuliang as qty,b.wanchengdingdanshuliang as qty2,  b.chuangjianshijian as dateTime," +
                    "'出库' as type from xingfa.RETRIEVAL_ORDER_LINE b where b.dingdanshuliang <= b.wanchengdingdanshuliang  ");
            sb1 = getSql2After(sb1, productId, beginDate, endDate);
            sb3 = getSql2After(sb3, productId, beginDate, endDate);
            sb1.append(" ) c order by c.dateTime desc ");
            Query query1 = session.createSQLQuery( sb1.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Query query2 = session.createSQLQuery(sb2.toString());
            Query query3 = session.createSQLQuery(sb3.toString());
            query1.setFirstResult(startIndex);
            query1.setMaxResults(defaultPageSize);


            if(StringUtils.isNotBlank(productId)){
                query1.setString("skuCode",productId);
                query1.setString("skuCode2",productId);
                query2.setString("skuCode",productId);
                query3.setString("skuCode2",productId);
            }
            if (StringUtils.isNotBlank(beginDate)) {
                query1.setString("beginDate",beginDate);
                query1.setString("beginDate2",beginDate);
                query2.setString("beginDate",beginDate);
                query3.setString("beginDate2",beginDate);
            }
            if (StringUtils.isNotBlank(endDate)) {
                query1.setString("endDate",endDate);
                query1.setString("endDate2",endDate);
                query2.setString("endDate",endDate);
                query3.setString("endDate2",endDate);
            }
            List<Map<String,Object>> jobList = query1.list();
            int count = (int)query2.uniqueResult();
            int count2 = (int)query3.uniqueResult();
            returnObj.setSuccess(true);
            returnObj.setRes(jobList);
            returnObj.setCount(count+count2);
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

    public StringBuffer getSqlAfter(StringBuffer sql,String productId, String beginDate, String endDate){
        if(StringUtils.isNotBlank(productId)){
            sql.append(" and a.skuCode = :skuCode ");
        }
        if (StringUtils.isNotBlank(beginDate)) {
            sql.append(" and a.STORE_DATE+' '+a.STORE_TIME >= :beginDate ");
        }
        if (StringUtils.isNotBlank(endDate)) {
            sql.append(" and a.STORE_DATE+' '+a.STORE_TIME <= :endDate ");
        }
        return sql;
    }
    public StringBuffer getSql2After(StringBuffer sql,String productId, String beginDate, String endDate){
        if(StringUtils.isNotBlank(productId)){
            sql.append(" and b.shangpindaima = :skuCode2 ");
        }
        if (StringUtils.isNotBlank(beginDate)) {
            sql.append(" and b.chuangjianshijian >= :beginDate2 ");
        }
        if (StringUtils.isNotBlank(endDate)) {
            sql.append(" and b.chuangjianshijian <= :endDate2 ");
        }
        return sql;
    }

}
