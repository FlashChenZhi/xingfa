package com.master.service;

import com.util.common.LogMessage;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Sku;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 排序去重查询
     *
     */
    public PagerReturnObj<List<Map<String,Object>>> findOrder(String orderNo, int currentPage, String productId, String shipperId, int PageSize){
//      System.out.println(orderNo+";"+currentPage+";"+productId+";"+shipperId+";"+PageSize);
        Session session= HibernateUtil.getCurrentSession();
        Query query = session.createQuery(" from Sku s  group by s.skuCode,s.huozhudaima");
        query.setString("skuCode",productId).setString("huozhudaima",shipperId).setResultTransformer(DISTINCT_ROOT_ENTITY);

//      Query query=session.createQuery(" from Sku s where s.huozhudaima= :huozhudaima or s.skuCode=:s.skuCode " +
//                "and not exists( from Sku where s.huozhudaima = :huozhudaima and s.skuCode = :skuCode ");

        List<Sku> skuList=query.list();

        PagerReturnObj<List<Map<String,Object>>> s = new PagerReturnObj<List<Map<String,Object>>>();
        List<Map<String,Object>> maplist = new ArrayList<Map<String,Object>>();
        for (Sku sku:skuList) {
            Map<String,Object> map =  new HashMap<String,Object>();
            map.put("shouhuodanhao;",orderNo);
            map.put("skuCode",productId);
            map.put("skuName",sku.getSkuName());
            map.put("huozhudaima;",shipperId);
            map.put("huozhumingcheng;",sku.getHuozhumingcheng());
            map.put("cangkudaima;",sku.getCangkudaima());
            map.put("shouhuoleixing;",sku.getShouhuoleixing());
            map.put("hanghao;",sku.getHanghao());
            map.put("dingdanshuliang;",sku.getDingdanshuliang());
            map.put("danwei;",sku.getDanwei());
            map.put("cunfangquyu;",sku.getCunfangquyu());
            maplist.add(map);
        }
        s.setSuccess(true);
        s.setCount(10);
        s.setRes(maplist);

        return s;
    }

    /**
     * 获取货主代码
     * @return
     * @throws IOException
     */
    public ReturnObj<List<Map<String,Object>>> getshipperId() {
        ReturnObj<List<Map<String,Object>>> s = new ReturnObj<List<Map<String,Object>>>();
        System.out.println("进入获取货主代码方法！");
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            List<Object[]> skuList = session.createQuery(" select distinct huozhudaima,huozhumingcheng from Sku").list();
            List<Map<String, Object>> MapList = null;
            for (Object[] object : skuList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", object[0]);
                map.put("name", object[1]);
                MapList.add(map);
            }
                s.setSuccess(true);
                s.setRes(MapList);
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
    public ReturnObj<List<Map<String,Object>>> getCommodityCode() throws IOException{
        ReturnObj<List<Map<String,Object>>> s = new ReturnObj<List<Map<String,Object>>>();
        System.out.println("进入获取商品代码方法！");
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        for (int i = 0; i <10 ; i++) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id",i);
            map.put("name","商品代码"+i);
            mapList.add(map);
        }
        s.setSuccess(true);
        s.setRes(mapList);
        return s;
    }
}
