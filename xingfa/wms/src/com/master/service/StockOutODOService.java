package com.master.service;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.StationMode;
import com.util.common.LogMessage;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: ed_chen
 * @Date: Create in 18:10 2018/7/13
 * @Description:
 * @Modified By:
 */
@Service
public class StockOutODOService {

    /*
     * @author：ed_chen
     * @date：2018/7/14 20:54
     * @description：查询批次号
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    public ReturnObj<List<Map<String,String>>> getLotNums(String skuCode){
        ReturnObj<List<Map<String, String>>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("select lotNum from Inventory i where i.skuCode=:skuCode group by lotNum");
            query.setParameter("skuCode",skuCode );
            List<String> retList = query.list();
            List<Map<String,String>> mapList = new ArrayList<>();
            for (String object: retList) {
                Map<String, String> map = new HashMap();
                map.put("lotNum", object );
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
     * @date：2018/7/14 20:54
     * @description：根据批次号和货品skucode查询在各个巷道各有几板货
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    public ReturnObj<List<Map<String,Object>>> findNumBySkuAndLotNum(String skuCode, String lotNum){
        ReturnObj<List<Map<String,Object>>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("select sum(i.qty) as count,i.container.location.position as position " +
                    "from Inventory i where i.lotNum=:lotNum and i.skuCode=:skuCode and i.container.reserved = false " +
                    "and not exists (select 1 from Location l where l.bay=i.container.location.bay and " +
                    "l.actualArea=i.container.location.actualArea and l.level =i.container.location.level and " +
                    "l.position=i.container.location.position and  l.seq > i.container.location.seq and " +
                    "l.reserved = true ) group by i.container.location.position ").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            query.setParameter("skuCode", skuCode);
            query.setParameter("lotNum", lotNum);
            List<Map<String,Object>> retList = query.list();

            s.setSuccess(true);
            s.setRes(retList);
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
     * @date：2018/7/14 20:54
     * @description：初始化车辆信息
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    public ReturnObj<List<Map<String,Object>>> getCar(){
        ReturnObj<List<Map<String,Object>>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("select e.eId as eid ,e.mark as mark " +
                    " from ETruck e where e.isDel=0 ").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

            List<Map<String,Object>> retList = query.list();

            s.setSuccess(true);
            s.setRes(retList);
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
     * @date：2018/7/14 20:54
     * @description：根据批次号和货品skucode查询在各个巷道各有几板货
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    public ReturnObj<String> getOrderNo(){
        ReturnObj<String> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String orderNo = sdf.format(date);
            Query query = session.createQuery("select count(*) as count from RetrievalOrderLine r where CONVERT(Nvarchar, r.chuangjianshijian, 111) = CONVERT(Nvarchar, GETDATE(), 111)  ");

            long count = (long) query.uniqueResult();
            count++;
            String count2 = StringUtils.leftPad(count+"", 4, '0') ;
            orderNo=orderNo+count2;
            s.setSuccess(true);
            s.setRes(orderNo);
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
     * @date：2018/8/20 9:47
     * @description：生成订单
     * @param orderNo
     * @param zhantai
     * @param data
     * @return：com.util.common.ReturnObj<java.lang.String>
     */
    public ReturnObj<String> addOrder(String orderNo, String zhantai, String data){
        ReturnObj<String> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Map<String,String> stations = new HashMap<>();
            stations.put("1","1202");
            stations.put("2","1204");
            stations.put("3","1206");

            orderNo= URLDecoder.decode(orderNo,"utf-8"); //订单号
            zhantai= URLDecoder.decode(zhantai,"utf-8"); //站台信息

            RetrievalOrderLine retrievalOrderLine2 = RetrievalOrderLine.getRetrievalOrderLineByjinhuodanhao(orderNo);
            if(retrievalOrderLine2==null && !orderNo.equals("4200026559")){
                if("1301".equals(zhantai)){
                    Station station = Station.getStation(zhantai);
                    if (!"03".equals(station.getMode())) {
                        Transaction.rollback();
                        s.setSuccess(false);
                        s.setMsg("1301的站台模式不是出库!");
                        return s;
                    }
                }

                JSONArray jsonArray = JSONArray.fromObject(data);
                List<Map<String,Object>> datalist = (List<Map<String,Object>>) JSONArray.toCollection(jsonArray,Map.class);
                for(int i=0;i<datalist.size();i++){
                    Map<String,Object> map=datalist.get(i);
                    String skuCode = map.get("skuCode").toString();//出库商品代码
                    String lotNum = map.get("lotNum").toString();//出库商品批次
                    Sku sku=Sku.getByCode(skuCode);
                    int qty = Integer.parseInt(map.get("qty").toString());//出库商品数量

                    int count = Inventory.getNumsBySkuCodeAndLotNum(skuCode,lotNum,zhantai );//仓库拥有总库存
                    if(qty>count){
                        Transaction.rollback();
                        s.setSuccess(false);
                        s.setMsg("出库数量不能大于所能出货位的存储数量！");
                        return s;
                    }
                    RetrievalOrderLine rol = new RetrievalOrderLine();

                    rol.setShouhuodanhao(orderNo);
                    rol.setJinhuodanhao(orderNo);
                    rol.setHuozhudaima("");
                    rol.setHuozhumingcheng("");
                    rol.setCangkudaima("");
                    rol.setShouhuoleixing("");
                    rol.setHanghao(i+1+"");
                    rol.setShangpindaima(skuCode);
                    rol.setShangpinmingcheng(sku.getSkuName());
                    rol.setDingdanshuliang(qty);
                    rol.setDanwei(sku.getDanwei());
                    rol.setLotNo(lotNum);
                    rol.setWanchengdingdanshuliang(0);
                    rol.setChuangjianshijian(new Date());
                    rol.setFromStation(zhantai);
                    session.save(rol);

                }
            }else{
                Transaction.rollback();
                s.setSuccess(false);
                s.setMsg("已存在此订单号!");
                return s;
            }
            s.setSuccess(true);
            s.setMsg("设定出库单成功！");
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
