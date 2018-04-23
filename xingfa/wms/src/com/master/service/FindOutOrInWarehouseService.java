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

            StringBuffer sb = new StringBuffer("select a.id as id,a.skuCode as skuCode, " +
                    "a.skuName as skuName,a.num as num,a.dateTime as dateTime,a.type as type " +
                    "from (select max(b.id) as id,b.skuCode as skuCode, " +
                    "b.skuName as skuName,count(*) as num, max(b.createDate) as dateTime," +
                    "case type when '01' then '入库' else '出库' end  as type from xingfa.JOBLOG b where 1=1 ");
            StringBuffer sb1 = new StringBuffer("select count(*) from (select b.skuCode from xingfa.JOBLOG b where  1=1 ");
            if(StringUtils.isNotBlank(productId)){
                sb.append("and b.skuCode =:productId ");
                sb1.append("and b.skuCode =:productId ");
            }
            if (StringUtils.isNotBlank(beginDate)) {
                sb.append("and b.createDate >= :beginDate ");
                sb1.append("and b.createDate >= :beginDate ");
            }
            if (StringUtils.isNotBlank(endDate)) {
                sb.append("and b.createDate <= :endDate ");
                sb1.append("and b.createDate <= :endDate ");
            }
            sb.append(" group by skuCode,skuName,type ) a  order by a.dateTime desc ");
            sb1.append("group by skuCode,skuName,type)a ");
            Query query = session.createSQLQuery(sb.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Query query1 = session.createSQLQuery(sb1.toString());

            query.setFirstResult(startIndex);
            query.setMaxResults(defaultPageSize);

            if(StringUtils.isNotBlank(productId)){
                query.setString("productId",productId);
                query1.setString("productId",productId);
            }
            if (StringUtils.isNotBlank(beginDate)) {
                query.setString("beginDate",beginDate);
                query1.setString("beginDate",beginDate);
            }
            if (StringUtils.isNotBlank(endDate)) {
                query.setString("endDate",endDate);
                query1.setString("endDate",endDate);
            }
            List<Map<String,Object>> jobList = query.list();
            int count = (int)query1.uniqueResult();

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

}
