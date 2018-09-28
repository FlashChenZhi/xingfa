package com.master.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.master.vo.SkuVo2;
import com.util.common.BaseReturnObj;
import com.util.common.LogMessage;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: ed_chen
 * @Date: Create in 15:26 2018/9/12
 * @Description:
 * @Modified By:
 */
@Service
public class InStorageStrategyService {
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

    /*
     * @author：ed_chen
     * @date：2018/9/12 16:32
     * @description：设定入库策略
     * @param commodityCode
     * @param lotNo
     * @param level
     * @param bay
     * @return：com.util.common.BaseReturnObj
     */
    public BaseReturnObj addTask(String commodityCode, String lotNo, int level,int bay) {
        ReturnObj returnObj = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();

            Map<String,Object> map = new HashMap<>();
            if(StringUtils.isBlank(commodityCode) || StringUtils.isBlank(lotNo) || level == 0 || bay==0){
                map.put("status", false);
                returnObj.setRes(map);
                returnObj.setSuccess(false);
                returnObj.setMsg("商品不存在!");
                Transaction.rollback();
                return returnObj;
            }
            Sku sku = Sku.getByCode(commodityCode);

            if (sku == null) {
                map.put("status", false);
                returnObj.setRes(map);
                returnObj.setSuccess(false);
                returnObj.setMsg("商品不存在!");
                Transaction.rollback();
                return returnObj;
            }
            if(bay==8 ||bay==18||bay==28||bay==39){
                map.put("status", false);
                returnObj.setRes(map);
                returnObj.setSuccess(false);
                returnObj.setMsg("不能设定有柱子的列!");
                Transaction.rollback();
                return returnObj;
            }
            InStorageStrategy inStroageStrategy = InStorageStrategy.findInStroageStrategy(commodityCode, lotNo, bay, level);
            if(inStroageStrategy!=null){
                map.put("status", false);
                returnObj.setRes(map);
                returnObj.setSuccess(false);
                returnObj.setMsg("此条入库策略已存在!");
                Transaction.rollback();
                return returnObj;
            }
            List<InStorageStrategy> inStroageStrategyList = InStorageStrategy.findInStroageStrategyByBayLev(bay,level );
            if(inStroageStrategyList.size()>2){
                map.put("status", false);
                returnObj.setRes(map);
                returnObj.setSuccess(false);
                returnObj.setMsg("同一列存在两条以上入库策略，请联系系统员!");
                Transaction.rollback();
                return returnObj;
            }else if(inStroageStrategyList.size()==2){

                for( int i=0;i<inStroageStrategyList.size();i++){
                    InStorageStrategy inStroageStrategy1=inStroageStrategyList.get(i);
                    //"0"此入库策略可以删除,"1"此入库策略不可以删除
                    String flag=findInventoryByBayLevel(inStroageStrategy1.getSkuCode(),inStroageStrategy1.getLotNum(),
                            inStroageStrategy1.getBay(), inStroageStrategy1.getLevel());

                    Sku sku1= Sku.getByCode(inStroageStrategy1.getSkuCode());
                    if("0".equals(flag)){
                        map.put("status", true);
                        map.put("skuCode", inStroageStrategy1.getSkuCode());
                        map.put("skuName", sku1.getSkuName());
                        map.put("lotNum", inStroageStrategy1.getLotNum());
                        map.put("bay", inStroageStrategy1.getBay());
                        map.put("level", inStroageStrategy1.getLevel());
                        map.put("id", inStroageStrategy1.getId());
                        break;
                    }else{
                        map.put("status", false);
                        if(i==inStroageStrategyList.size()-1){
                            returnObj.setMsg("此列已存在两个入库策略，均不可删除！");
                        }
                    }

                }
                returnObj.setSuccess(false);
                returnObj.setRes(map);
                Transaction.rollback();
                return returnObj;
            }else if(inStroageStrategyList.size()==1){
                InStorageStrategy inStorageStrategy2 = inStroageStrategyList.get(0);
                boolean flag=findOtherCountByBayLevel(commodityCode,lotNo,inStorageStrategy2.getSkuCode(),inStorageStrategy2.getLotNum(),bay, level);
                if(!flag){
                    boolean flag2=true;
                    Query query = session.createQuery("select count(*) as count from Inventory i where i.skuCode=:skuCode and i.lotNum=:lotNum ");
                    query.setParameter("skuCode",inStorageStrategy2.getSkuCode() );
                    query.setParameter("lotNum",inStorageStrategy2.getLotNum() );
                    long count = (long)query.uniqueResult();
                    if(count==0){
                        //没有库存，判断有没有到这的job
                        query = session.createQuery("select count(*) as count from Job i,InventoryView iv where " +
                                "i.container=iv.palletCode and i.toLocation.bay=:bay and i.type='01' " +
                                "and i.toLocation.level=:level and i.toLocation.position='2' and " +
                                "i.toLocation.actualArea='2' and iv.skuCode=:skuCode and iv.lotNum=:lotNum ");
                        query.setParameter("bay",bay );
                        query.setParameter("level",level );
                        query.setParameter("skuCode",inStorageStrategy2.getSkuCode() );
                        query.setParameter("lotNum",inStorageStrategy2.getLotNum() );
                        long count2 = (long)query.uniqueResult();
                        //group by 之后count(*) 得到的值是null不能直接用long类型接收
                        if(count2==0){
                            flag2=false;
                        }
                    }
                    if (flag2){
                        map.put("status", false);
                        returnObj.setRes(map);
                        returnObj.setSuccess(false);
                        returnObj.setMsg("此列存在两种货物，不允许设置入库策略!");
                        Transaction.rollback();
                        return returnObj;
                    }else{
                        Sku sku1=Sku.getByCode(inStorageStrategy2.getSkuCode());
                        map.put("status", true);
                        map.put("skuCode", inStorageStrategy2.getSkuCode());
                        map.put("skuName", sku1.getSkuName());
                        map.put("lotNum", inStorageStrategy2.getLotNum());
                        map.put("bay", inStorageStrategy2.getBay());
                        map.put("level", inStorageStrategy2.getLevel());
                        map.put("id", inStorageStrategy2.getId());
                        returnObj.setSuccess(false);
                        returnObj.setRes(map);
                        Transaction.rollback();
                        return returnObj;
                    }

                }else{
                    InStorageStrategy inStorageStrategy = new InStorageStrategy();
                    session.save(inStorageStrategy);
                    inStorageStrategy.setSkuCode(commodityCode);
                    inStorageStrategy.setLotNum(lotNo);
                    inStorageStrategy.setBay(bay);
                    inStorageStrategy.setLevel(level);
                    inStorageStrategy.setBayLevel(bay+"_"+level);
                    inStorageStrategy.setDate(new Date());
                }
            }else{
                boolean flag=findCountByBayLevel(commodityCode,lotNo,bay, level);
                if(!flag){
                    map.put("status", false);
                    returnObj.setRes(map);
                    returnObj.setSuccess(false);
                    returnObj.setMsg("此列存在两种货物，不允许设置入库策略!");
                    Transaction.rollback();
                    return returnObj;
                }else{
                    InStorageStrategy inStorageStrategy = new InStorageStrategy();
                    session.save(inStorageStrategy);
                    inStorageStrategy.setSkuCode(commodityCode);
                    inStorageStrategy.setLotNum(lotNo);
                    inStorageStrategy.setBay(bay);
                    inStorageStrategy.setLevel(level);
                    inStorageStrategy.setBayLevel(bay+"_"+level);
                    inStorageStrategy.setDate(new Date());
                }
            }
            returnObj.setSuccess(true);
            returnObj.setMsg("设定成功！");
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
    public PagerReturnObj<List<Map<String,Object>>> findPutInStorageOrder(int startIndex, int defaultPageSize,
                                               String skuCode,String lotNum,String bay,String level) {
        PagerReturnObj<List<Map<String,Object>>> returnObj = new PagerReturnObj<List<Map<String,Object>>>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query1 = session.createQuery("select i.id as id ,s.skuName as skuName,i.lotNum as lotNum," +
                    "i.bay as bay,i.level as level,i.date as date from InStorageStrategy i,Sku s " +
                    "where i.skuCode=s.skuCode "+
                    (StringUtils.isNotBlank(skuCode)?" and i.skuCode=:skuCode ":"")+
                    (StringUtils.isNotBlank(lotNum)?" and i.lotNum=:lotNum ":"")+
                    (StringUtils.isNotBlank(bay)?" and i.bay=:bay ":"")+
                    (StringUtils.isNotBlank(level)?" and i.level=:level ":"")+" order by i.date desc").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Query query2 = session.createQuery("select count(1) from InStorageStrategy i where 1=1 "+
                    (StringUtils.isNotBlank(skuCode)?" and i.skuCode=:skuCode ":"")+
                    (StringUtils.isNotBlank(lotNum)?" and i.lotNum=:lotNum ":"")+
                    (StringUtils.isNotBlank(bay)?" and i.bay=:bay ":"")+
                    (StringUtils.isNotBlank(level)?" and i.level=:level ":""));
            query1.setFirstResult(startIndex);
            query1.setMaxResults(defaultPageSize);

            if(StringUtils.isNotBlank(skuCode)){
                query1.setParameter("skuCode", skuCode);
                query2.setParameter("skuCode", skuCode);
            }
            if(StringUtils.isNotBlank(lotNum)){
                query1.setParameter("lotNum", lotNum);
                query2.setParameter("lotNum", lotNum);
            }
            if(StringUtils.isNotBlank(bay)){
                query1.setParameter("bay", Integer.parseInt(bay));
                query2.setParameter("bay", Integer.parseInt(bay));
            }
            if(StringUtils.isNotBlank(level)){
                query1.setParameter("level", Integer.parseInt(level));
                query2.setParameter("level",  Integer.parseInt(level));
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
                InStorageStrategy inStorageStrategy =InStorageStrategy.findInStroageStrategyById(id);
                session.delete(inStorageStrategy);

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

    public BaseReturnObj delAndAddTask(String commodityCode, String lotNo, int level,int bay,int delId) {
        ReturnObj returnObj = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();

            Map<String,Object> map = new HashMap<>();
            if(StringUtils.isBlank(commodityCode) || StringUtils.isBlank(lotNo) || level == 0 || bay==0){
                map.put("status", false);
                returnObj.setRes(map);
                returnObj.setSuccess(false);
                returnObj.setMsg("商品不存在!");
                Transaction.rollback();
                return returnObj;
            }
            Sku sku = Sku.getByCode(commodityCode);

            if (sku == null) {
                map.put("status", false);
                returnObj.setRes(map);
                returnObj.setSuccess(false);
                returnObj.setMsg("商品不存在!");
                Transaction.rollback();
                return returnObj;
            }
            if(bay==8 ||bay==18||bay==28||bay==39){
                map.put("status", false);
                returnObj.setRes(map);
                returnObj.setSuccess(false);
                returnObj.setMsg("不能设定有柱子的列!");
                Transaction.rollback();
                return returnObj;
            }
            InStorageStrategy inStroageStrategy = InStorageStrategy.findInStroageStrategy(commodityCode, lotNo, bay, level);
            if(inStroageStrategy!=null){
                map.put("status", false);
                returnObj.setRes(map);
                returnObj.setSuccess(false);
                returnObj.setMsg("此条入库策略已存在!");
                Transaction.rollback();
                return returnObj;
            }
            InStorageStrategy delInStorageStrategy = InStorageStrategy.findInStroageStrategyById(delId);
            session.delete(delInStorageStrategy);

            InStorageStrategy inStorageStrategy = new InStorageStrategy();
            session.save(inStorageStrategy);
            inStorageStrategy.setSkuCode(commodityCode);
            inStorageStrategy.setLotNum(lotNo);
            inStorageStrategy.setBay(bay);
            inStorageStrategy.setLevel(level);
            inStorageStrategy.setBayLevel(bay+"_"+level);
            inStorageStrategy.setDate(new Date());

            returnObj.setSuccess(true);
            returnObj.setMsg("设定成功！");
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
     * @date：2018/9/12 17:31
     * @description：查询此列是否存在此入库策略的货物或任务
     * @param skuCode
     * @param lotNum
     * @param bay
     * @param level
     * @return：java.lang.String
     */
    public String findInventoryByBayLevel(String skuCode,String lotNum,int bay,int level) throws Exception{

        Session session = HibernateUtil.getCurrentSession();
        String s ="0";//此入库策略可以删除
        Query query = session.createQuery("select count(*) as count from Inventory i where i.container.location.bay=:bay " +
                "and i.container.location.level=:level and i.skuCode=:skuCode and i.lotNum=:lotNum and " +
                "i.container.location.position='2' and i.container.location.actualArea='2' ");
        query.setParameter("bay",bay );
        query.setParameter("level",level );
        query.setParameter("skuCode",skuCode );
        query.setParameter("lotNum",lotNum );
        long count = (long)query.uniqueResult();
        if(count!=0){
            s="1";//此入库策略不可以删除
        }

        if("0".equals(s)){
            query = session.createQuery("select count(*) as count from Job i,InventoryView iv where " +
                    "i.container=iv.palletCode and i.toLocation.bay=:bay and i.type='01' " +
                    "and i.toLocation.level=:level and iv.skuCode=:skuCode and iv.lotNum=:lotNum and " +
                    "i.toLocation.position='2' and i.toLocation.actualArea='2' ");
            query.setParameter("bay",bay );
            query.setParameter("level",level );
            query.setParameter("skuCode",skuCode );
            query.setParameter("lotNum",lotNum );
            count = (long)query.uniqueResult();
            if(count!=0){
                s="1";//此入库策略不可以删除
            }
        }

        return s;
    }

    /*
     * @author：ed_chen
     * @date：2018/9/13 17:27
     * @description：查询此入库策略是否可设定
     * @param skuCode
     * @param lotNum
     * @param bay
     * @param level
     * @return：boolean
     */
    public boolean findCountByBayLevel(String skuCode,String lotNum,int bay,int level) throws Exception{

        Session session = HibernateUtil.getCurrentSession();
        boolean s =true;//可以设定入库策略
        Query query = session.createQuery("select i.skuCode as skuCode,i.lotNum as lotNum from Inventory i " +
                "where not exists(select 1 from Inventory i2 where i.container.location.bay=i2.container.location.bay " +
                "and i.container.location.level=i2.container.location.level and i.container.location.position=i2.container.location.position and " +
                "i.container.location.actualArea=i2.container.location.actualArea and i2.skuCode=:skuCode and i2.lotNum=:lotNum ) and " +
                "i.container.location.bay=:bay and i.container.location.level=:level and i.container.location.position='2' and " +
                "i.container.location.actualArea='2' group by i.skuCode,i.lotNum ").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("bay",bay );
        query.setParameter("level",level );
        query.setParameter("skuCode",skuCode );
        query.setParameter("lotNum",lotNum );
        List<Map<String,Object>> mapList= query.list();
        if(mapList.size()>=2){
            s=false;//不能设定入库策略
            return s;
        }else{
            if(mapList.size()==1){
                Map<String,Object> map = mapList.get(0);
                String skuCode2 = map.get("skuCode").toString();
                String lotNum2 =map.get("lotNum").toString();
                query = session.createQuery("select count(*) as count from Job i,InventoryView iv where " +
                        "i.container=iv.palletCode and i.toLocation.bay=:bay and i.type='01' " +
                        "and i.toLocation.level=:level and i.toLocation.position='2' and " +
                        "i.toLocation.actualArea='2' and not exists (select 1 from InventoryView iv2 " +
                        "where i.container=iv2.palletCode and iv2.skuCode=:skuCode and iv2.lotNum=:lotNum) and " +
                        "not exists (select 1 from InventoryView iv3 where i.container=iv3.palletCode and " +
                        "iv3.skuCode=:skuCode2 and iv3.lotNum=:lotNum2) " +
                        "group by iv.skuCode,iv.lotNum ");
                query.setParameter("bay",bay );
                query.setParameter("level",level );
                query.setParameter("skuCode",skuCode2 );
                query.setParameter("lotNum",lotNum2);
                query.setParameter("skuCode2",skuCode );
                query.setParameter("lotNum2",lotNum );
                Object count2 = (Object)query.uniqueResult();
                //group by 之后count(*) 得到的值是null不能直接用long类型接收
                long count=0;
                if(count2!=null){
                    count=(long)count2;
                }
                if(count>=1){
                    s=false;//不能设定入库策略
                    return s;
                }
            }else{
                query = session.createQuery("select count(*) as count from Job i,InventoryView iv where " +
                        "i.container=iv.palletCode and i.toLocation.bay=:bay and i.type='01' " +
                        "and i.toLocation.level=:level and i.toLocation='2' and " +
                        "i.toLocation.actualArea='2' and not exists (select 1 from InventoryView iv3 " +
                        "where i.container=iv3.palletCode and iv3.skuCode=:skuCode2 and iv3.lotNum=:lotNum2) " +
                        "group by iv.skuCode,iv.lotNum ");
                query.setParameter("bay",bay );
                query.setParameter("level",level );
                query.setParameter("skuCode2",skuCode );
                query.setParameter("lotNum2",lotNum );
                Object count2 = (Object)query.uniqueResult();
                long count=0;
                if(count2!=null){
                    count=(long)count2;
                }
                if(count>=2){
                    s=false;//不能设定入库策略
                    return s;
                }
            }
        }

        return s;
    }

    /*
     * @author：ed_chen
     * @date：2018/9/13 17:27
     * @description：查询除此入库策略外是否有其他库存
     * @param skuCode
     * @param lotNum
     * @param bay
     * @param level
     * @return：boolean
     */
    public boolean findOtherCountByBayLevel(String skuCode,String lotNum,String skuCode2,String lotNum2,int bay,int level) throws Exception{

        Session session = HibernateUtil.getCurrentSession();
        boolean s =true;//可以设定入库策略
        Query query = session.createQuery("select i.skuCode as skuCode,i.lotNum as lotNum from Inventory i " +
                "where not exists(select 1 from Inventory i2 where i.container.location.bay=i2.container.location.bay " +
                "and i.container.location.level=i2.container.location.level and i.container.location.position=i2.container.location.position and " +
                "i.container.location.actualArea=i2.container.location.actualArea and i2.skuCode=:skuCode and i2.lotNum=:lotNum ) and " +
                "not exists(select 1 from Inventory i3 where i.container.location.bay=i3.container.location.bay " +
                "and i.container.location.level=i3.container.location.level and i.container.location.position=i3.container.location.position and " +
                "i.container.location.actualArea=i3.container.location.actualArea and i3.skuCode=:skuCode2 and i3.lotNum=:lotNum2 ) and " +
                "i.container.location.bay=:bay and i.container.location.level=:level and i.container.location.position='2' and " +
                "i.container.location.actualArea='2' group by i.skuCode,i.lotNum ").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("bay",bay );
        query.setParameter("level",level );
        query.setParameter("skuCode",skuCode );
        query.setParameter("lotNum",lotNum );
        query.setParameter("skuCode2",skuCode2 );
        query.setParameter("lotNum2",lotNum2 );
        List<Map<String,Object>> mapList= query.list();
        if(mapList.size()>=1){
            s=false;//不能设定入库策略
            return s;
        }else{
            //Map<String,Object> map = mapList.get(0);
            query = session.createQuery("select count(*) as count from Job i,InventoryView iv where " +
                    "i.container=iv.palletCode and i.toLocation.bay=:bay and i.type='01' " +
                    "and i.toLocation.level=:level and i.toLocation.position='2' and " +
                    "i.toLocation.actualArea='2' and not exists (select 1 from InventoryView iv2 " +
                    "where i.container=iv2.palletCode and iv2.skuCode=:skuCode and iv2.lotNum=:lotNum) and " +
                    "not exists (select 1 from InventoryView iv3 where i.container=iv3.palletCode and " +
                    "iv3.skuCode=:skuCode2 and iv3.lotNum=:lotNum2) " +
                    "group by iv.skuCode,iv.lotNum ");
            query.setParameter("bay",bay );
            query.setParameter("level",level );
            query.setParameter("skuCode",skuCode2 );
            query.setParameter("lotNum",lotNum2);
            query.setParameter("skuCode2",skuCode );
            query.setParameter("lotNum2",lotNum );
            Object count2 = (Object)query.uniqueResult();
            //group by 之后count(*) 得到的值是null不能直接用long类型接收
            long count=0;
            if(count2!=null){
                count=(long)count2;
            }
            if(count>=1){
                s=false;//不能设定入库策略
                return s;
            }
        }

        return s;
    }

}
