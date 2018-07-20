package com.master.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.util.common.Const;
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

import java.util.*;

/**
 * @Author: ed_chen
 * @Date: Create in 10:05 2018/6/5
 * @Description:
 * @Modified By:
 */
@Service
public class SampleSurveyService {
    /*
     * @author：ed_chen
     * @date：2018/4/10 16:16
     * @description：初始化map
     * @param productId
     * @param tier
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> getStorageLocationData(String productId, String tier){
        ReturnObj<Map<String, Object>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            //查询总货位
            /*Query query1 = session.createQuery("select convert(varchar,l.bank)+'_'+convert(varchar,l.bay) as coordinate " +
                    "from Location l where not exists (select 1 from Location lo where l.id=lo.id and " +
                    "lo.putawayRestricted = true and lo.retrievalRestricted = true) and l.level=:level " +
                    "order by l.bank , l.bay ");
            query1.setString("level", tier);*/
            List<Integer> bayslist = new ArrayList<>();
            bayslist.add(8);
            bayslist.add(18);
            bayslist.add(28);
            bayslist.add(39);
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

            //判断可以抽检的货位
            StringBuffer sb = new StringBuffer(
                    " select convert(varchar,e.bank)+'_'+convert(varchar,e.bay) as coordinate from xingfa.Inventory i , xingfa.CONTAINER d " +
                            ",( " +
                            "select a.id as id ,a.BANK,a.BAY from xingfa.location a , " +
                            "(select max(seq2) as seq2,bay,POSITION,AREA,outPosition " +
                            "from xingfa.LOCATION where empty=0 and lev=:level and PUTAWAYRESTRICTED=0 and " +
                            "RETRIEVALRESTRICTED=0 and ABNORMAL =0 group by bay,POSITION,AREA,outPosition " +
                            ") b " +
                            "where a.seq2<b.seq2 and a.bay = b.bay and a.empty=0 and " +
                            "a.position = b.position and a.position = 2 and a.outPosition = b.outPosition and a.outPosition=1 " +
                            "and a.area = b.area and a.lev=:level and PUTAWAYRESTRICTED=0 and " +
                            "RETRIEVALRESTRICTED=0 and ABNORMAL =0 and  not exists( " +
                            "select 1 from xingfa.Location l where l.bay=a.bay and " +
                            "l.area=a.area and l.lev =a.lev " +
                            "and  l.position=a.position and l.seq2 < a.seq2 and " +
                            " l.seq > a.seq and l.reserved = 1 " +
                            "  ) " +
                            ") e  where i.CONTAINERID=d.ID and e.ID = d.LOCATIONID and d.RESERVED=0 ");
            if(StringUtils.isNotBlank(productId)){
                sb.append(" and i.skucode=:skucode ");
            }
            Query query3 = session.createSQLQuery(sb.toString());

            if(StringUtils.isNotBlank(productId)){
                query3.setString("skucode", productId);
            }
            query3.setString("level", tier);

            //查询已经有出库任务的货位
            Query query4 = session.createQuery("select convert(varchar,c.location.bank)+'_'+convert(varchar,c.location.bay) as coordinate from Container c where " +
                    " c.location.level = :level and c.reserved = true  ");
            query4.setString("level", tier);

            //查询已经有入库任务的货位
            Query query5 = session.createQuery("select convert(varchar,l.bank)+'_'+convert(varchar,l.bay) as coordinate from Location l where " +
                    " l.level = :level and l.reserved = true  ");
            query5.setString("level", tier);

            //查询已经有出库任务的此列其他货位
            Query query6 = session.createQuery("select convert(varchar,l.bank)+'_'+convert(varchar,l.bay) as coordinate " +
                    "from Location l where exists (select 1 from Container c where c.location.level = :level " +
                    "and c.reserved = true and c.location.position=l.position and c.location.actualArea =l.actualArea and " +
                    "c.location.bay=l.bay) and l.level=:level");
            query6.setString("level", tier);

            //查询已经有入库任务的货位
            Query query7 = session.createQuery("select convert(varchar,l.bank)+'_'+convert(varchar,l.bay) as coordinate " +
                    " from Location l where exists (select 1 from Location lo where " +
                    " lo.level = :level and lo.reserved = true and lo.position=l.position and lo.actualArea =l.actualArea and " +
                    " lo.bay=l.bay) and l.level=:level ");
            query7.setString("level", tier);

            //查询有抽检任务的货位列
            Query query8 = session.createQuery("select convert(varchar,l.bank)+'_'+convert(varchar,l.bay) as coordinate from Location l where " +
                    " l.level = :level and exists (select 1 from TransportOrderLog t where t.fromLocation.locationNo=l.locationNo  )  ");
            query8.setString("level", tier);

            Query query9 = session.createQuery("select convert(varchar,l.bank)+'_'+convert(varchar,l.bay) as coordinate from Location l where " +
                    " l.level = :level and exists (select 1 from TransportOrderLog t where t.toLocation.locationNo=l.locationNo  )  ");
            query9.setString("level", tier);

            map.put("map", LocationList); //总货位
            map.put("emptyList", query2.list()); //空货位
            map.put("availableList", query3.list()); //可被选择的货位
            map.put("reservedOutList", query4.list()); //已经有任务的货位
            map.put("reservedInList", query5.list()); //已经有任务的货位
            map.put("unavailableList", list);
            map.put("unavailableList1", query6.list());
            map.put("unavailableList2", query7.list());
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
     *//*
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
    *//*
     * @author：ed_chen
     * @date：2018/4/10 19:27
     * @description：获取里面的货位代码
     * @param bank
     * @param bay
     * @param level
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     *//*
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
    }*/

    /*
     * @author：ed_chen
     * @date：2018/4/11 10:28
     * @description：设定出库任务
     * @param selectLocation
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> assignsTheStorehouse(String selectLocation,String level){

        ReturnObj<Map<String, Object>> s = new ReturnObj();
        JSONArray jsonArray = JSONArray.fromObject(selectLocation);
        List<String> list = (List<String>) JSONArray.toCollection(jsonArray,String.class);
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            boolean flag = true;
            String msg="";
            String locationNo="";
            if(list.size()==1){
                locationNo = list.get(0);
                String[] s2 =locationNo.split("_");
                locationNo= StringUtils.leftPad(s2[0],3 ,"0" )+StringUtils.leftPad(s2[1],3 ,"0" )+StringUtils.leftPad(level,3 ,"0" );
                Location location =Location.getByLocationNo(locationNo);

                //查询此货位所在列是否已经有出库任务
                Query query4 = session.createQuery("select count(*) as coordinate from Container c where " +
                        " c.location.level = :level and c.reserved = true and c.location.position=:position and " +
                        " c.location.actualArea =:actualArea and c.location.bay=:bay");
                query4.setParameter("level", location.getLevel());
                query4.setParameter("position", location.getPosition());
                query4.setParameter("actualArea", location.getActualArea());
                query4.setParameter("bay", location.getBay());

                //查询已经有入库任务的货位
                Query query5 = session.createQuery("select count(*) as coordinate from Location l where " +
                        " l.level = :level and l.reserved = true and l.position=:position and " +
                        " l.actualArea =:actualArea and l.bay=:bay ");
                query5.setParameter("level", location.getLevel());
                query5.setParameter("position", location.getPosition());
                query5.setParameter("actualArea", location.getActualArea());
                query5.setParameter("bay", location.getBay());

                int count1 =((Long) query4.uniqueResult()).intValue();
                int count2 =((Long) query5.uniqueResult()).intValue();
                if(count1==0 && count2==0){
                    Query query1=session.createQuery("from TransportOrderLog ");
                    List<TransportOrderLog> transportOrderLogList = query1.list();

                    Query query2= session.createQuery("from Location l where l.bay=18 and l.empty=:empty and l.position='1' and l.actualArea='2' ");
                    query2.setParameter("empty", false);
                    List<Location> locationList2=query2.list();

                    if(transportOrderLogList.size()==0 && locationList2.size()==0){
                        //查询在此货位之前的有货货位
                        Query query = session.createQuery("from Location l where l.level = :level and " +
                                "l.position=:position and l.actualArea =:actualArea and " +
                                "l.bay=:bay and l.seq2<:seq2 and l.empty=false order by l.seq2 asc ");

                        query.setParameter("level", location.getLevel());
                        query.setParameter("position", location.getPosition());
                        query.setParameter("actualArea", location.getActualArea());
                        query.setParameter("bay", location.getBay());
                        query.setParameter("seq2", location.getSeq2());

                        List<Location> locationList = query.list();

                        for(Location location1 :locationList){

                            //为移库货物分配货位
                            Query query3 =HibernateUtil.getCurrentSession().createQuery("from Location l where l.bay=18 and l.empty=true and l.reserved=false " +
                                    "and l.position='1' and l.actualArea='2' order by level asc,seq2 desc").setMaxResults(1);
                            Location toLocation = (Location) query3.uniqueResult();

                            yiku(session,location1.getLocationNo(),toLocation);

                            TransportOrderLog transportOrderLog = new TransportOrderLog();
                            transportOrderLog.setFromLocation(location1);
                            transportOrderLog.setToLocation(toLocation);
                            transportOrderLog.setType("2");//移库
                            transportOrderLog.setContainer(Container.getByLocationId(location1.getId()));
                            transportOrderLog.setCreateDate(new Date());
                            session.save(transportOrderLog);
                            toLocation.setReserved(true);
                        }

                        outKu(session,location.getLocationNo());

                        TransportOrderLog transportOrderLog = new TransportOrderLog();
                        transportOrderLog.setFromLocation(location);
                        transportOrderLog.setType("1");//抽检出库
                        transportOrderLog.setContainer(Container.getByLocationId(location.getId()));
                        transportOrderLog.setCreateDate(new Date());
                        session.save(transportOrderLog);

                        //将此列货位的retrievalrestrited 设为true
                        Query query3=session.createQuery("update Location l set l.retrievalRestricted=true " +
                                "where l.level=:level and l.bay=:bay and l.position=:position and l.actualArea=:actualArea");
                        query3.setParameter("level", location.getLevel());
                        query3.setParameter("position", location.getPosition());
                        query3.setParameter("bay", location.getBay());
                        query3.setParameter("actualArea", location.getActualArea());
                        query3.executeUpdate();


                    }else{
                        flag=false;
                        msg="有尚未完成的抽检任务！";
                    }
                }else{
                    flag=false;
                    msg="此列存在出入库任务！";
                }
            }else{
                flag=false;
                msg="抽检所选多于一个货位！";
            }
            if(flag){
                s.setMsg("设定抽检成功");
                s.setSuccess(true);
                Transaction.commit();
            }else{
                Transaction.rollback();
                s.setSuccess(false);
                s.setMsg(msg);
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
        session.saveOrUpdate(container);
    }

    public void outKu(Session session,String locationNo){
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
        String toStation = outPosition.equals("1") ? "1201" : "1301";//到达站台
        String fromStation = outPosition.equals("1") ? "ML01" : "ML02";//出发地点
        String type = AsrsJobType.CHECKOUTSTORAGE; //抽检出库
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

        //修改此托盘
        Container container = inventory.getContainer();
        container.setReserved(true);
        session.saveOrUpdate(container);
    }
}
