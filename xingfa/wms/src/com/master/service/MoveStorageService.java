package com.master.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.master.vo.SkuVo2;
import com.util.common.*;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: ed_chen
 * @Date: Create in 12:50 2018/6/10
 * @Description:
 * @Modified By:
 */
@Service
public class MoveStorageService {
    /*
     * @author：ed_chen
     * @date：2018/4/10 16:16
     * @description：初始化map
     * @param productId
     * @param tier
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> getStorageLocationData(String productId,String tier,String selectStatus,String fromLocationNo){
        ReturnObj<Map<String, Object>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            List<Integer> bayslist = new ArrayList<>();
            bayslist.add(8);
            bayslist.add(18);
            bayslist.add(28);
            bayslist.add(39);

            //查询总货位
            /*Query query1 = session.createQuery("select convert(varchar,bank)+'_'+convert(varchar,bay) as coordinate from Location where " +
                    "putawayRestricted = false and retrievalRestricted = false and level=:level  order by bank , bay ");
            query1.setString("level", tier);*/
            Query query1 = session.createQuery("select convert(varchar,l.bank)+'_'+convert(varchar,l.bay) as coordinate " +
                    "from Location l where not exists (select 1 from Location lo where l.id=lo.id and " +
                    "lo.putawayRestricted = true and lo.retrievalRestricted = true) and l.level=:level and l.bank!=26 and " +
                    "not exists (select 1 from Location ll where l.id=ll.id and ( (ll.bay in (:bays) and ll.bank=:bank) or " +
                    "(ll.locationNo = :locationNo)))" +
                    "order by l.bank , l.bay ");
            query1.setString("level", tier);
            query1.setParameterList("bays", bayslist);
            query1.setParameter("bank", 12);
            query1.setParameter("locationNo", "001013001");
            List<String> list = query1.list();
            int bankCount = Const.bankCount;
            int bayCount = Const.bayCount;
            List<String> LocationList = new ArrayList<>();
            Map<String,Object> map = new HashMap<>();
            for(int i = 1;i<=bankCount;i++){
                StringBuffer sb = new StringBuffer();
                for(int j =1; j<=bayCount;j++){
                    if(list.contains(i+"_"+j)){
                        sb.append("a");
                    }else{
                        sb.append("_");
                    }
                }
                LocationList.add(sb.toString());
            }

            //查询空货位
            Query query2 = session.createQuery("select convert(varchar,a.bank)+'_'+convert(varchar,a.bay) as coordinate from Location a where " +
                    "a.empty = true and a.level = :level ");
            query2.setString("level", tier);

            Query query3=null;
            Query query11=null;
            if(selectStatus.equals("1")){
                //判断可以定点出库的货位
                StringBuffer sb = new StringBuffer(
                        " select convert(varchar,e.bank)+'_'+convert(varchar,e.bay) as coordinate from xingfa.Inventory i , xingfa.CONTAINER d " +
                                ",( " +
                                "select a.id as id ,a.BANK,a.BAY from xingfa.location a , " +
                                "(select min(seq2) as seq2,bay,POSITION,AREA " +
                                "from xingfa.LOCATION where empty=0 and lev=:level   and PUTAWAYRESTRICTED=0 and " +
                                "RETRIEVALRESTRICTED=0 and ABNORMAL =0 group by bay,POSITION,AREA " +
                                ") b " +
                                "where a.seq2=b.seq2 and a.bay = b.bay and " +
                                "a.position = b.position and a.area = b.area and a.lev=:level and PUTAWAYRESTRICTED=0 and " +
                                "RETRIEVALRESTRICTED=0 and ABNORMAL =0 and  not exists( " +
                                "select 1 from xingfa.Location l where l.bay=a.bay and " +
                                "l.area=a.area and l.lev =a.lev " +
                                "and  l.position=a.position and l.seq2 < a.seq2 and " +
                                " l.seq > a.seq and l.reserved = 1 " +
                                "  ) " +
                                ") e  where i.CONTAINERID=d.ID and e.ID = d.LOCATIONID and d.RESERVED=0 ");
                if(org.apache.commons.lang3.StringUtils.isNotBlank(productId)){
                    sb.append(" and i.skucode=:skucode ");
                }
                query3 = session.createSQLQuery(sb.toString());

                if(StringUtils.isNotBlank(productId)){
                    query3.setString("skucode", productId);
                }
                query3.setString("level", tier);
            }else if(selectStatus.equals("2")){
                //若状态为2，则查询可移入的货位
                if(!fromLocationNo.equals("")){
                    Query query10 = session.createQuery("from Inventory i where i.container.location.locationNo = :locationNo");
                    query10.setParameter("locationNo", fromLocationNo);
                    query10.setMaxResults(1);
                    Inventory inventory = (Inventory) query10.uniqueResult();
                    //查询一号堆垛机所在的空货位
                    query3 = session.createQuery("select convert(varchar,l.bank)+'_'+convert(varchar,l.bay) " +
                            "as coordinate from Location l where not exists(select 1 " +
                            "from Location lo where l.position=lo.position and l.level=lo.level and " +
                            "l.bay=lo.bay and l.actualArea=lo.actualArea and (lo.empty=false or " +
                            "lo.reserved=true or lo.putawayRestricted=true or lo.retrievalRestricted =true ) )  " +
                            "and l.position =1 and l.level=:level");
                    query3.setString("level", tier);
                    if(inventory!=null){
                        //查询与此货位相同货品相同批次的列所在的空货位
                        StringBuffer sb = new StringBuffer("select convert(varchar,h.bank)+'_'+convert(varchar,h.bay) as coordinate from (" +
                                " select e.bank,e.bay,e.outposition,e.area,e.seq2 from xingfa.Inventory i , xingfa.CONTAINER d " +
                                "  ,( " +
                                " select a.id as id ,a.BANK,a.BAY,a.outposition,a.seq2,a.area from xingfa.location a , " +
                                " (select min(seq2) as seq2,bay,OUTPOSITION,AREA " +
                                " from xingfa.LOCATION where empty=0 and lev=:level  and PUTAWAYRESTRICTED=0 and " +
                                " RETRIEVALRESTRICTED=0 and ABNORMAL =0 group by bay,OUTPOSITION,AREA " +
                                " ) b " +
                                " where a.seq2=b.seq2 and a.bay = b.bay and " +
                                " a.outposition = b.outposition and a.area = b.area and a.lev=:level and PUTAWAYRESTRICTED=0 and " +
                                " RETRIEVALRESTRICTED=0 and ABNORMAL =0 and  not exists( " +
                                " select 1 from xingfa.Location l where l.bay=a.bay and " +
                                " l.area=a.area and l.lev =a.lev " +
                                " and  l.outposition=a.outposition and l.seq2 < a.seq2 and " +
                                " l.seq > a.seq and l.reserved = 1 " +
                                " ) " +
                                " ) e  where i.CONTAINERID=d.ID and e.ID = d.LOCATIONID  and d.RESERVED=0 and i.LOT_NUM=:lotNum and i.skuCode=:skuCode" +
                                " ) f ,xingfa.location h where f.bay=h.bay and h.bay!=:bay and h.lev=:level and f.outposition =h.outposition and f.area=h.area and f.seq2>h.seq2 ");
                        query11 = session.createSQLQuery(sb.toString());
                        query11.setString("skuCode", inventory.getSkuCode());
                        query11.setString("level", tier);
                        query11.setString("lotNum", inventory.getLotNum());
                        query11.setParameter("bay", inventory.getContainer().getLocation().getBay());


                    }

                }

            }

            //查询已经有出库任务的货位
            Query query4 = session.createQuery("select convert(varchar,c.location.bank)+'_'+convert(varchar,c.location.bay) as coordinate from Container c where " +
                    " c.location.level = :level and c.reserved = true  ");
            query4.setString("level", tier);

            //查询已经有入库任务的货位
            Query query5 = session.createQuery("select convert(varchar,l.bank)+'_'+convert(varchar,l.bay) as coordinate from Location l where " +
                    " l.level = :level and l.reserved = true  ");
            query5.setString("level", tier);

            //查询有抽检任务的货位列
            Query query6 = session.createQuery("select convert(varchar,l.bank)+'_'+convert(varchar,l.bay) as coordinate from Location l where " +
                    " l.level = :level and exists (select 1 from TransportOrderLog t where t.fromLocation.bay=l.bay and  " +
                    " t.fromLocation.level=l.level and  t.fromLocation.position=l.position and  t.fromLocation.actualArea=l.actualArea)  ");
            query6.setString("level", tier);

            //查询有抽检任务的货位列
            Query query8 = session.createQuery("select convert(varchar,l.bank)+'_'+convert(varchar,l.bay) as coordinate from Location l where " +
                    " l.level = :level and exists (select 1 from TransportOrderLog t where t.fromLocation.locationNo=l.locationNo  )  ");
            query8.setString("level", tier);

            Query query9 = session.createQuery("select convert(varchar,l.bank)+'_'+convert(varchar,l.bay) as coordinate from Location l where " +
                    " l.level = :level and exists (select 1 from TransportOrderLog t where t.toLocation.locationNo=l.locationNo  )  ");
            query9.setString("level", tier);

            map.put("map", LocationList); //总货位
            map.put("emptyList", query2.list()); //空货位
            List<String> list1 =query3.list();
            if(query11!=null){
                List<String> list2 =query11.list();
                if(list2.size()!=0){
                   for(String s2 : list2){
                       list1.add(s2);
                   }
                }
                /*map.put("availableList11", query11.list()); //可被选择的货位*/
            }
            map.put("availableList", list1); //可被选择的货位
            map.put("unavailableList4", query9.list());//已有抽检任务的货位
            map.put("reservedOutList", query4.list()); //已经有任务的货位
            map.put("reservedInList", query5.list()); //已经有任务的货位
            map.put("unavailableList", list);
            map.put("unavailableList1", query6.list());//已有抽检任务的列
            map.put("unavailableList3", query8.list());//已有抽检任务的货位
            map.put("unavailableList4", query9.list());//已有抽检任务的货位
            s.setRes(map);
            s.setSuccess(true);
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
     * @date：2018/4/10 16:16
     * @description：查询货位货物信息
     * @param productId
     * @param tier
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> getLocationInfo(String bank,String bay,String level){
        ReturnObj<Map<String, Object>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            //查询货位
            Query query = session.createQuery("select i.skuCode as skuCode,i.skuName as skuName, " +
                    "i.lotNum as lotNum,i.qty as qty,i.container.barcode as barcode, " +
                    "i.container.location.bank as bank,i.container.location.bay as bay, " +
                    "i.container.location.level as level from Inventory i where i.container.location.bank " +
                    "= :bank and i.container.location.bay = :bay and i.container.location.level = :level").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            query.setInteger("bank", Integer.parseInt(bank));
            query.setInteger("bay", Integer.parseInt(bay));
            query.setInteger("level", Integer.parseInt(level));

            List<Map<String,Object>> mapList = query.list();
            Map<String,Object> map = new HashMap<>();
            if(mapList.size()==0){
                map.put("bank", bank);
                map.put("bay", bay);
                map.put("level", level);
                map.put("status", false);
                map.put("msg", "空货位");
            }else{
                map = mapList.get(0);
                map.put("status", true);
                map.put("msg", "有货");
            }
            s.setRes(map);
            s.setSuccess(true);
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
     * @date：2018/4/10 18:27
     * @description：查询下一个可选货位信息
     * @param bank
     * @param bay
     * @param level
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> getNextAvailableLocation(String bank,String bay,String level){
        ReturnObj<Map<String, Object>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            //查询货位
            Query query = session.createQuery("select convert(varchar,a.bank)+'_'" +
                    "+convert(varchar,a.bay) as coordinate from Location a " +
                    "where a.position=(select position from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.actualArea = (select actualArea from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.seq2=(select seq2+1 from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.level = :level  and a.bay=:bay and a.empty = false");
            query.setString("bank", bank);
            query.setString("bay", bay);
            query.setString("level", level);



            List<String> list = query.list();
            Map<String,Object> map = new HashMap<>();
            if(list.size()==0){
                map.put("status", false);
            }else{
                map.put("status", true);
            }
            Location location = Location.getByLocationNo(StringUtils.leftPad(bank, 3, "0")+StringUtils.leftPad(bay, 3, "0")+StringUtils.leftPad(level, 3, "0"));
            //查询非此列的其他货位
            Query query2 = session.createQuery("select convert(varchar,a.bank)+'_'+convert(varchar,a.bay) as coordinate from Location a " +
                    "where not exists (select 1 from Location b where a.id=b.id and b.position =:position " +
                    "and b.bay=:bay and b.level=:level and b.actualArea=:actualArea) and a.level=:level");
            query2.setString("position", location.getPosition());
            query2.setString("bay", bay);
            query2.setString("level", level);
            query2.setString("actualArea", location.getActualArea());
            map.put("location", list);
            map.put("otherlocation", query2.list());
            s.setRes(map);
            s.setSuccess(true);
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
     * @date：2018/4/10 19:27
     * @description：获取里面的货位代码
     * @param bank
     * @param bay
     * @param level
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> getAgoUnavailableLocation(String bank,String bay,String level){
        ReturnObj<Map<String, Object>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            //查询货位
            Query query = session.createQuery("select convert(varchar,a.bank)+'_'" +
                    "+convert(varchar,a.bay) as coordinate from Location a " +
                    "where a.position=(select position from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.actualArea = (select actualArea from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.seq2>(select seq2 from Location c where c.bank=:bank and c.bay=:bay and c.level=:level) " +
                    "and a.level = :level  and a.bay=:bay and a.empty = false");
            query.setString("bank", bank);
            query.setString("bay", bay);
            query.setString("level", level);

            List<String> list = query.list();
            Map<String,Object> map = new HashMap<>();
            if(list.size()==0){
                map.put("status", false);
            }else{
                map.put("status", true);
                map.put("location", list);
            }
            s.setRes(map);
            s.setSuccess(true);
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
     * @date：2018/4/11 10:28
     * @description：设定出库任务
     * @param selectLocation
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> assignsTheStorehouse(String fromLocationNos,String toLocationNos){

        ReturnObj<Map<String, Object>> s = new ReturnObj();
        //要移货位
        JSONArray fromjsonArray = JSONArray.fromObject(fromLocationNos);
        List<String> fromlist = (List<String>) JSONArray.toCollection(fromjsonArray,String.class);
        //移至货位
        JSONArray tojsonArray = JSONArray.fromObject(toLocationNos);
        List<String> tolist = (List<String>) JSONArray.toCollection(tojsonArray,String.class);
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            boolean flag = true;
            String location="";
            Query query11 = session.createQuery("from Job a where type=:type");
            query11.setParameter("type", AsrsJobType.LOCATIONTOLOCATION);
            query11.setMaxResults(1);
            Job job = (Job)query11.uniqueResult();
            if(job==null){
                Query query = session.createQuery("select count(1) as count from Location l where (l.locationNo in (:locationNos) " +
                        "or l.locationNo in (:locationNos2)) and ( exists " +
                        "(select 1 from Location lo where lo.position=l.position and lo.bay=l.bay and " +
                        "lo.actualArea =l.actualArea and lo.level=l.level and lo.reserved=true) or exists ( " +
                        "select 1 from Container c where c.location.position=l.position and c.location.bay=l.bay and " +
                        "c.location.actualArea=l.actualArea and c.location.level=l.level and c.reserved=true) )");
                query.setParameterList("locationNos", tolist);
                query.setParameterList("locationNos2", fromlist);
                int count = ((Long)query.uniqueResult()).intValue();
                if(count==0){
                    Query query1= session.createQuery("from Location l where l.locationNo in (:fromLocationNos) order by l.seq2");
                    query1.setParameterList("fromLocationNos", fromlist);
                    List<Location> fromLocations = query1.list();

                    Query query2= session.createQuery("from Location l where l.locationNo in (:toLocationNos) order by l.level,l.bay,l.actualArea,l.position,l.seq");
                    query2.setParameterList("toLocationNos", tolist);
                    List<Location> toLocations = query2.list();
                    if(fromLocations.size()==toLocations.size()){
                        for(int i =0;i<fromLocations.size();i++){
                            yiku(session,fromLocations.get(i).getLocationNo(),toLocations.get(i));
                        }
                    }else{
                        flag=false;
                        Transaction.rollback();
                        s.setSuccess(false);
                        s.setMsg("要移货位和移动货位不一致！");
                    }

                }else{
                    flag=false;
                    Transaction.rollback();
                    s.setSuccess(false);
                    s.setMsg("已选货位列存在出库任务！");
                }
            }else{
                flag=false;
                Transaction.rollback();
                s.setSuccess(false);
                s.setMsg("已存在移库任务！");
            }

            if(flag){
                s.setMsg("设定移库成功");
                s.setSuccess(true);
                Transaction.commit();
            }
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
        String fromStation ="ML01" ;//出发地点
        String type = AsrsJobType.LOCATIONTOLOCATION; //移库
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
        toLocation.setReserved(true);
        session.saveOrUpdate(container);
    }


}
