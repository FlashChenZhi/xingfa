package com.master.service;

import com.util.common.LogMessage;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.RetrievalOrderLine;
import com.wms.domain.Sku;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.Convert;
import java.io.IOException;
import java.util.*;

import static org.hibernate.criterion.CriteriaSpecification.DISTINCT_ROOT_ENTITY;
import static org.hibernate.criterion.CriteriaSpecification.PROJECTION;

@Service
public class OrderInquiryService {
    /**
     * 查询定单
     * @param orderNo 订单号（条件）
     * @param currentPage 当前页码
     * @param productId 商品代码（条件）
     * @param shipperId 货主代码（条件）
     * @param PageSize 每页条数
     * @return
     * 排序查询
     */
    public PagerReturnObj<List<Map<String,Object>>> findOrder(String orderNo, int currentPage, String productId, String shipperId, int PageSize){
       System.out.println(orderNo+";"+currentPage+";"+productId+";"+shipperId+";"+PageSize);
        PagerReturnObj<List<Map<String,Object>>> s = new PagerReturnObj();
        try {
            Session session = HibernateUtil.getCurrentSession();
            Transaction.begin();
            Criteria criteria = session.createCriteria(RetrievalOrderLine.class);
            if(!orderNo.equals("")&&orderNo!=null){
                criteria.add(Restrictions.eq("jinhuodanhao",orderNo));
            }
            if(!productId.equals("")&&productId!=null){
                criteria.add(Restrictions.eq("shangpindaima",productId));
            }
            if(!shipperId.equals("")&&shipperId!=null){
                criteria.add(Restrictions.eq("huozhudaima",shipperId));
            }
            Long count = (Long)  criteria.setProjection(Projections.rowCount()).uniqueResult();
            criteria.setProjection(null);
            criteria.addOrder(Order.desc("rid"));
            criteria.setFirstResult((currentPage-1)*PageSize);
            criteria.setMaxResults(PageSize);
            List<RetrievalOrderLine> maplist =criteria.list();
            List<Map<String, Object>> list=new ArrayList<>();
            for (int i=0;i<maplist.size();i++) {
                Map<String, Object> map = new HashMap();
                map.put("orderNo", maplist.get(i).getJinhuodanhao());
                map.put("productId", maplist.get(i).getShangpindaima());
                map.put("productName",maplist.get(i).getShangpinmingcheng());
                map.put("shipperId", maplist.get(i).getHuozhudaima());
                map.put("shipperName",maplist.get(i).getHuozhudaima());
                map.put("productNum", maplist.get(i).getDingdanshuliang());
                map.put("allcatedNum", maplist.get(i).getDingdanshuliang());
                list.add(map);
            }
                s.setSuccess(true);
                s.setRes(list);
                s.setCount(count);
                Transaction.commit();
        }catch (JDBCConnectionException ex) {
            s.setSuccess(false);
            s.setMsg(LogMessage.DB_DISCONNECTED.getName());
        } catch (Exception ex) {
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
            return s;
    }

    /**
     * 获取货主代码
     * @return
     * @throws IOException
     */
    public ReturnObj<List<Map<String,Object>>> getshipperId() {
        ReturnObj<List<Map<String,Object>>> s = new ReturnObj();
        System.out.println("进入获取货主代码方法！");
        try {
            Session session = HibernateUtil.getCurrentSession();
            Transaction.begin();
            List<Object[]> skuList = session.createQuery(" select distinct huozhudaima,huozhumingcheng from RetrievalOrderLine").list();
            List<Map<String, Object>> mapList =new ArrayList<>();
            for (Object[] object : skuList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", object[0]);
                map.put("name", object[1]);
                System.out.println("第一个："+object[0]+"第二个："+object[1]);
                mapList.add(map);
            }
                s.setSuccess(true);
                s.setRes(mapList);
                Transaction.commit();
        }catch (JDBCConnectionException ex) {
            s.setSuccess(false);
            s.setMsg(LogMessage.DB_DISCONNECTED.getName());
        } catch (Exception ex) {
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return s;
 }
    /**
     * 获取商品代码
     * @return
     * @throws IOException
     */
    public ReturnObj<List<Map<String,String>>> getCommodityCode() throws IOException {
        ReturnObj<List<Map<String, String>>> s = new ReturnObj();
        System.out.println("进入获取商品代码方法！");
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("from RetrievalOrderLine");
            List<RetrievalOrderLine> retList = query.list();
            List<Map<String,String>> mapList = new ArrayList<>();
            for (RetrievalOrderLine retrievalOrderLine : retList) {
                Map<String, String> map = new HashMap();
                map.put("id", retrievalOrderLine.getShangpindaima());
                map.put("name", retrievalOrderLine.getShangpinmingcheng());
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
    @Test
    public  void test(){
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("from RetrievalOrderLine where chuangjianshijian <:chuangjianshijian");
            query.setParameter("chuangjianshijian",new Date());
            List<RetrievalOrderLine> retList = query.list();
            List<Map<String,String>> mapList = new ArrayList<>();
            for (RetrievalOrderLine retrievalOrderLine : retList) {
                Map<String, String> map = new HashMap();
                map.put("id", retrievalOrderLine.getShangpindaima());
                map.put("name", retrievalOrderLine.getShangpinmingcheng());
                mapList.add(map);
            }
            Transaction.commit();
        }catch (Exception ex) {
            Transaction.rollback();
            ex.printStackTrace();
        }
    }
}
