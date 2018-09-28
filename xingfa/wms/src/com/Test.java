package com;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.SkuType;
import com.asrs.business.consts.TransportType;
import com.asrs.domain.XMLbean.Envelope;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.ControlArea.Receiver;
import com.asrs.domain.XMLbean.XMLList.ControlArea.RefId;
import com.asrs.domain.XMLbean.XMLList.ControlArea.Sender;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.TransportOrderDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.asrs.domain.XMLbean.XMLList.DataArea.StUnit;
import com.asrs.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.asrs.domain.XMLbean.XMLList.TransportOrder;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.asrs.xml.util.XMLUtil;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.util.common.DateFormat;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.domain.blocks.Srm;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: ed_chen
 * @Date: Create in 18:23 2018/3/10
 * @Description:
 * @Modified By:
 */
public class Test {
    public static void main(String[] args) {
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();

            String stationNo = "1301";
            org.hibernate.Query jobQuery = HibernateUtil.getCurrentSession().createQuery("from Job j where j.fromStation = :station and j.status = :waiting order by j.createDate")
                    .setString("station",stationNo)
                    .setString("waiting", AsrsJobStatus.WAITING)
                    .setMaxResults(1);

            Job j = (Job) jobQuery.uniqueResult();
            if(j == null) {
                SystemLog.error("入库站台" + stationNo + "不存在任务");
                InMessage.error(stationNo, "入库站台" + stationNo + "不存在任务");
            }else{
                InventoryView view = InventoryView.getByPalletNo(j.getContainer());
                Station station = Station.getStation(stationNo);

                Location newLocation=null;

                if(AsrsJobType.PUTAWAY.equals(j.getType())){
                    WholeInStorageStrategy wholeInStorageStrategy =WholeInStorageStrategy.getWholeInStorageStrategyById(1);
                    if(wholeInStorageStrategy.isStatus()){
                        //若是入库分配货位
                        newLocation = Location.getEmptyLocation(view.getSkuCode(),view.getLotNum(),station.getPosition(),"02",j.getBay(),j.getLevel());
                    }else{
                        //若全局入库策略是手动的，则只能入有入库策略的货物
                        if(!(j.getBay()==0 && j.getLevel()==0 )){
                            newLocation = Location.getEmptyLocation(view.getSkuCode(),view.getLotNum(),station.getPosition(),"02",j.getBay(),j.getLevel());
                        }else{
                            j.setError("全局入库策略为手动，不可入没有入库策略的货物");
                        }
                    }

                }else if(AsrsJobType.CHECKINSTORAGE.equals(j.getType())){
                    //若是抽检入库，则回原位
                    newLocation=j.getToLocation();
                }

            }
            Transaction.commit();
        }catch (Exception e){
            Transaction.rollback();
            e.printStackTrace();
        }
    }

    /*
     * @author：ed_chen
     * @date：2018/8/22 17:38
     * @description：ML01的出库任务
     * @param rol
     * @param orderposition
     * @param dingdanshuliang
     * @param outType
     * @return：int
     */
    public static int ML01ArrayOut(RetrievalOrderLine rol,String orderposition,int dingdanshuliang ,int outType) throws Exception{
        Session session = HibernateUtil.getCurrentSession();

        String shangpindaima = rol.getShangpindaima();

        List<Inventory> inventoryList = new ArrayList<>();
        if(outType==1){
            //存在此列有其他货物,并且靠近出库堆垛机的列
            Query query2 = HibernateUtil.getCurrentSession().createQuery("from Inventory i where " +
                    " i.skuCode = :skuCode " + (StringUtils.isBlank(rol.getLotNo()) ? " " : " and i.lotNum = :lotNum ") +
                    " and i.container.reserved = false and i.container.location.retrievalRestricted = false and " +
                    " (i.container.location.position =:position or i.container.location.outPosition =:outPosition) " +
                    " and i.container.location.abnormal = false and not exists (select 1 from Location l where " +
                    " l.bay=i.container.location.bay and l.actualArea=i.container.location.actualArea " +
                    " and l.level =i.container.location.level and  l.position=i.container.location.position and " +
                    " l.reserved = true ) " +
                    " and exists(select 1 from Inventory i2 where i2.container.location.bay=i.container.location.bay and " +
                    " i2.container.location.actualArea=i.container.location.actualArea " +
                    " and i2.container.location.level =i.container.location.level and " +
                    " i2.container.location.position=i.container.location.position and (i2.skuCode!=i.skuCode or i2.lotNum!=i.lotNum) )"+
                    " and not exists(select 1 from Inventory i3 where i3.container.location.bay=i.container.location.bay and " +
                    " i3.container.location.actualArea=i.container.location.actualArea " +
                    " and i3.container.location.level =i.container.location.level and " +
                    " i3.container.location.position=i.container.location.position and (i3.skuCode!=i.skuCode or i3.lotNum!=i.lotNum) " +
                    " and i3.container.location.seq2<i.container.location.seq2 ) ");
            query2.setParameter("skuCode",shangpindaima);
            query2.setParameter("position",orderposition);
            query2.setParameter("outPosition",orderposition);
            if(StringUtils.isNotBlank(rol.getLotNo())){
                query2.setString("lotNum",rol.getLotNo());
            }
            inventoryList = query2.list();
        }else if(outType==2){
            //不存在此列有其他货物
            Query query2 = HibernateUtil.getCurrentSession().createQuery("from Inventory i where " +
                    " i.skuCode = :skuCode " + (StringUtils.isBlank(rol.getLotNo()) ? " " : " and i.lotNum = :lotNum ") +
                    " and i.container.reserved = false and i.container.location.retrievalRestricted = false and " +
                    " (i.container.location.position =:position or i.container.location.outPosition =:outPosition) and i.container.location.abnormal = false and not exists (select 1 from Location l where " +
                    " l.bay=i.container.location.bay and l.actualArea=i.container.location.actualArea " +
                    " and l.level =i.container.location.level and  l.position=i.container.location.position and " +
                    " l.reserved = true ) " +
                    " and not exists(select 1 from Inventory i2 where i2.container.location.bay=i.container.location.bay and " +
                    " i2.container.location.actualArea=i.container.location.actualArea " +
                    " and i2.container.location.level =i.container.location.level and " +
                    " i2.container.location.position=i.container.location.position and (i2.skuCode!=i.skuCode or i2.lotNum!=i.lotNum) )");
            query2.setParameter("skuCode",shangpindaima);
            query2.setParameter("position",orderposition);
            query2.setParameter("outPosition",orderposition);
            if(StringUtils.isNotBlank(rol.getLotNo())){
                query2.setString("lotNum",rol.getLotNo());
            }
            inventoryList = query2.list();
        }else if(outType==3){
            //存在此列有其他货物,并且靠近出库堆垛机的列
            Query query2 = HibernateUtil.getCurrentSession().createQuery("from Inventory i where " +
                    " i.skuCode = :skuCode " + (StringUtils.isBlank(rol.getLotNo()) ? " " : " and i.lotNum = :lotNum ") +
                    " and i.container.reserved = false and i.container.location.retrievalRestricted = false and " +
                    " (i.container.location.position =:position or i.container.location.outPosition =:outPosition) and i.container.location.abnormal = false and not exists (select 1 from Location l where " +
                    " l.bay=i.container.location.bay and l.actualArea=i.container.location.actualArea " +
                    " and l.level =i.container.location.level and  l.position=i.container.location.position and " +
                    " l.reserved = true ) " +
                    " and exists(select 1 from Inventory i2 where i2.container.location.bay=i.container.location.bay and " +
                    " i2.container.location.actualArea=i.container.location.actualArea " +
                    " and i2.container.location.level =i.container.location.level and " +
                    " i2.container.location.position=i.container.location.position and (i2.skuCode!=i.skuCode or i2.lotNum!=i.lotNum) )"+
                    " and not exists(select 1 from Inventory i3 where i3.container.location.bay=i.container.location.bay and " +
                    " i3.container.location.actualArea=i.container.location.actualArea " +
                    " and i3.container.location.level =i.container.location.level and " +
                    " i3.container.location.position=i.container.location.position and (i3.skuCode!=i.skuCode or i3.lotNum!=i.lotNum) " +
                    " and i3.container.location.seq2>i.container.location.seq2 )");
            query2.setParameter("skuCode",shangpindaima);
            query2.setParameter("position",orderposition);
            query2.setParameter("outPosition",orderposition);
            if(StringUtils.isNotBlank(rol.getLotNo())){
                query2.setString("lotNum",rol.getLotNo());
            }
            inventoryList = query2.list();
        }

        if(inventoryList.size()!=0 && inventoryList!=null){
            //所有单元格货品位置的集合，四坐标为值
            Map<String,Map<Integer,Inventory>> map = new HashMap<>();
            //四坐标为key，每个四坐标的最小seq2为值的map
            Map<String,Integer> sortMap = new HashMap<>();

            for (int i = 0; i < inventoryList.size(); i++) {
                Inventory inventory = inventoryList.get(i);
                Container container = inventory.getContainer();
                Location location = container.getLocation();
                String key = location.getBay() + "," + location.getLevel() + "," + location.getPosition() + "," + location.getActualArea();
                Map<Integer,Inventory> map2 = map.get(key);
                if(map2 == null){
                    map2 = new HashMap<>();
                }
                //存放seq2，位置类
                map2.put(location.getSeq2(),inventory);
                map.put(key,map2);
                Integer seq2 = sortMap.get(key);
                if(seq2 == null ||seq2 > location.getSeq2()){
                    sortMap.put(key,location.getSeq2());
                }

            }
            //将获取到的最小值seq2放入set集合
            Set<Integer> seq2Set = new HashSet<>();
            for(Map.Entry<String,Integer> entry:sortMap.entrySet()){
                seq2Set.add(entry.getValue());
            }
            //将set里的值排序，放入list中
            List<Integer> seq2List = new ArrayList<>();
            Collections.addAll(seq2List,seq2Set.toArray(new Integer[seq2Set.size()]));
            Collections.sort(seq2List, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    /**
                     * 升序排的话就是第一个参数.compareTo(第二个参数);
                     * 降序排的话就是第二个参数.compareTo(第一个参数);
                     */
                    return o2 - o1;//降序
                }
            });

            List<String> list = new ArrayList<>();
            //将排序好的seq2 的值和原本sortmap中的值比较，将其放入list
            for(int seq2:seq2List){
                for(Map.Entry<String,Integer> entry:sortMap.entrySet()){
                    //若有重复的就add两次
                    if(seq2 == entry.getValue()){
                        list.add(entry.getKey());
                    }
                }
            }
            //将各个组排好序（降序），将组内排序
            //                  OUT:
            for(String key :list){
                if(outType!=3){
                    Map<Integer,Inventory> map2 = map.get(key);
                    List<Integer> sortMap2List = new ArrayList<>();
                    //将组内单元格按照seq2排序，升序
                    Collections.addAll(sortMap2List,map2.keySet().toArray(new Integer[map2.keySet().size()]));
                    Collections.sort(sortMap2List);
                    //将组内单元格循环，生成任务发送给设备，并将已完成数量+
                    /**
                     * 在发送任务前需不需要将货位锁定？出库：retrievalRestricted，入库：reserved
                     * inventory里的货品数量是哪个字段？qty
                     * 接下来保存任务到数据库，供另一个线程将任务发动给设备
                     */
                    for(int seq2 : sortMap2List){
                        Inventory inventory = map2.get(seq2);
                        //创建JOB
                        createJob(orderposition,inventory,rol);

                        int qty = inventory.getQty().intValue();//货品数量
                        dingdanshuliang = dingdanshuliang - qty;
                        //修改订单表中的数据
                        int wancheng = rol.getWanchengdingdanshuliang()==null?0:rol.getWanchengdingdanshuliang();
                        rol.setWanchengdingdanshuliang(wancheng+qty);
                        session.saveOrUpdate(rol);

                        if(dingdanshuliang<=0){
                            break;
                        }
                    }
                }else{
                    //type==3，不靠近堆垛机的货位有货，需要把前面的货出库或移库
                    String bay = key.split(",")[0];
                    String level = key.split(",")[1];
                    String position = key.split(",")[2];
                    String actualArea = key.split(",")[3];
                    //查询前面的货位
                    Query query = session.createQuery("from Inventory i where i.container.location.bay=:bay and " +
                            " i.container.location.level=:level and i.container.location.position=:position and " +
                            " i.container.location.actualArea=:actualArea order by seq2 asc ");
                    query.setParameter("bay", Integer.parseInt(bay));
                    query.setParameter("level", Integer.parseInt(level));
                    query.setParameter("position", position);
                    query.setParameter("actualArea", actualArea);
                    List<Inventory> inventoryList1 =query.list();
                    for(Inventory inventory:inventoryList1){
                        //创建JOB
                        createJob(orderposition,inventory,rol);
                        if(inventory.getSkuCode().equals(rol.getShangpindaima()) && inventory.getLotNum().equals(rol.getLotNo())){
                            int qty = inventory.getQty().intValue();//货品数量
                            dingdanshuliang = dingdanshuliang - qty;
                            //修改订单表中的数据
                            int wancheng = rol.getWanchengdingdanshuliang()==null?0:rol.getWanchengdingdanshuliang();
                            rol.setWanchengdingdanshuliang(wancheng+qty);
                            session.saveOrUpdate(rol);
                        }

                        if(dingdanshuliang<=0){
                            break;
                        }
                    }
                }

                if(dingdanshuliang<=0){
                    break;
                }
            }
        }
        return dingdanshuliang;
    }

    /*
     * @author：ed_chen
     * @date：2018/8/22 17:38
     * @description：ML02的出库任务
     * @param rol
     * @param orderposition
     * @param dingdanshuliang
     * @param outType
     * @return：int
     */
    public static int ML02ArrayOut(RetrievalOrderLine rol,String orderposition,int dingdanshuliang ,int outType) throws Exception{
        Session session = HibernateUtil.getCurrentSession();

        String shangpindaima = rol.getShangpindaima();

        List<Inventory> inventoryList = new ArrayList<>();
        if(outType==1){
            //存在此列有其他货物,并且靠近出库堆垛机的列
            Query query2 = HibernateUtil.getCurrentSession().createQuery("from Inventory i where " +
                    " i.skuCode = :skuCode " + (StringUtils.isBlank(rol.getLotNo()) ? " " : " and i.lotNum = :lotNum ") +
                    " and i.container.reserved = false and i.container.location.retrievalRestricted = false and " +
                    " (i.container.location.position =:position or i.container.location.outPosition =:outPosition) and i.container.location.abnormal = false and not exists (select 1 from Location l where " +
                    " l.bay=i.container.location.bay and l.actualArea=i.container.location.actualArea " +
                    " and l.level =i.container.location.level and  l.position=i.container.location.position and " +
                    " l.reserved = true ) " +
                    " and exists(select 1 from Inventory i2 where i2.container.location.bay=i.container.location.bay and " +
                    " i2.container.location.actualArea=i.container.location.actualArea " +
                    " and i2.container.location.level =i.container.location.level and " +
                    " i2.container.location.position=i.container.location.position and (i2.skuCode!=i.skuCode or i2.lotNum!=i.lotNum) )"+
                    " and not exists(select 1 from Inventory i3 where i3.container.location.bay=i.container.location.bay and " +
                    " i3.container.location.actualArea=i.container.location.actualArea " +
                    " and i3.container.location.level =i.container.location.level and " +
                    " i3.container.location.position=i.container.location.position and (i3.skuCode!=i.skuCode or i3.lotNum!=i.lotNum) " +
                    " and i3.container.location.seq2>i.container.location.seq2 )");
            query2.setParameter("skuCode",shangpindaima);
            query2.setParameter("position",orderposition);
            query2.setParameter("outPosition",orderposition);
            if(StringUtils.isNotBlank(rol.getLotNo())){
                query2.setString("lotNum",rol.getLotNo());
            }
            inventoryList = query2.list();
        }else if(outType==2){
            //不存在此列有其他货物
            Query query2 = HibernateUtil.getCurrentSession().createQuery("from Inventory i where " +
                    " i.skuCode = :skuCode " + (StringUtils.isBlank(rol.getLotNo()) ? " " : " and i.lotNum = :lotNum ") +
                    " and i.container.reserved = false and i.container.location.retrievalRestricted = false and " +
                    " (i.container.location.position =:position or i.container.location.outPosition =:outPosition) and i.container.location.abnormal = false and not exists (select 1 from Location l where " +
                    " l.bay=i.container.location.bay and l.actualArea=i.container.location.actualArea " +
                    " and l.level =i.container.location.level and  l.position=i.container.location.position and " +
                    " l.reserved = true ) " +
                    " and not exists(select 1 from Inventory i2 where i2.container.location.bay=i.container.location.bay and " +
                    " i2.container.location.actualArea=i.container.location.actualArea " +
                    " and i2.container.location.level =i.container.location.level and " +
                    " i2.container.location.position=i.container.location.position and (i2.skuCode!=i.skuCode or i2.lotNum!=i.lotNum) )");
            query2.setParameter("skuCode",shangpindaima);
            query2.setParameter("position",orderposition);
            query2.setParameter("outPosition",orderposition);
            if(StringUtils.isNotBlank(rol.getLotNo())){
                query2.setString("lotNum",rol.getLotNo());
            }
            inventoryList = query2.list();
        }else if(outType==3){
            //存在此列有其他货物,并且靠近出库堆垛机的列
            Query query2 = HibernateUtil.getCurrentSession().createQuery("from Inventory i where " +
                    " i.skuCode = :skuCode " + (StringUtils.isBlank(rol.getLotNo()) ? " " : " and i.lotNum = :lotNum ") +
                    " and i.container.reserved = false and i.container.location.retrievalRestricted = false and " +
                    " (i.container.location.position =:position or i.container.location.outPosition =:outPosition) and i.container.location.abnormal = false and not exists (select 1 from Location l where " +
                    " l.bay=i.container.location.bay and l.actualArea=i.container.location.actualArea " +
                    " and l.level =i.container.location.level and l.position=i.container.location.position and " +
                    " l.reserved = true ) " +
                    " and exists(select 1 from Inventory i2 where i2.container.location.bay=i.container.location.bay and " +
                    " i2.container.location.actualArea=i.container.location.actualArea " +
                    " and i2.container.location.level =i.container.location.level and " +
                    " i2.container.location.position=i.container.location.position and (i2.skuCode!=i.skuCode or i2.lotNum!=i.lotNum) )"+
                    " and not exists(select 1 from Inventory i3 where i3.container.location.bay=i.container.location.bay and " +
                    " i3.container.location.actualArea=i.container.location.actualArea " +
                    " and i3.container.location.level =i.container.location.level and " +
                    " i3.container.location.position=i.container.location.position and (i3.skuCode!=i.skuCode or i3.lotNum!=i.lotNum) " +
                    " and i3.container.location.seq2<i.container.location.seq2 )");
            query2.setParameter("skuCode",shangpindaima);
            query2.setParameter("position",orderposition);
            query2.setParameter("outPosition",orderposition);
            if(StringUtils.isNotBlank(rol.getLotNo())){
                query2.setString("lotNum",rol.getLotNo());
            }
            inventoryList = query2.list();
        }

        if(inventoryList.size()!=0 && inventoryList!=null){
            //所有单元格货品位置的集合，四坐标为值
            Map<String,Map<Integer,Inventory>> map = new HashMap<>();
            //四坐标为key，每个四坐标的最小seq2为值的map
            Map<String,Integer> sortMap = new HashMap<>();

            for (int i = 0; i < inventoryList.size(); i++) {
                Inventory inventory = inventoryList.get(i);
                Container container = inventory.getContainer();
                Location location = container.getLocation();
                String key = location.getBay() + "," + location.getLevel() + "," + location.getPosition() + "," + location.getActualArea();
                Map<Integer,Inventory> map2 = map.get(key);
                if(map2 == null){
                    map2 = new HashMap<>();
                }
                //存放seq2，位置类
                map2.put(location.getSeq(),inventory);
                map.put(key,map2);
                Integer seq = sortMap.get(key);
                if(seq == null ||seq < location.getSeq()){
                    sortMap.put(key,location.getSeq());
                }

            }
            //将获取到的最小值seq2放入set集合
            Set<Integer> seqSet = new HashSet<>();
            for(Map.Entry<String,Integer> entry:sortMap.entrySet()){
                seqSet.add(entry.getValue());
            }
            //将set里的值排序，放入list中
            List<Integer> seqList = new ArrayList<>();
            Collections.addAll(seqList,seqSet.toArray(new Integer[seqSet.size()]));
            Collections.sort(seqList, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    /**
                     * 升序排的话就是第一个参数.compareTo(第二个参数);
                     * 降序排的话就是第二个参数.compareTo(第一个参数);
                     */
                    return o1 - o2;//升序
                }
            });

            List<String> list = new ArrayList<>();
            //将排序好的seq2 的值和原本sortmap中的值比较，将其放入list
            for(int seq:seqList){
                for(Map.Entry<String,Integer> entry:sortMap.entrySet()){
                    //若有重复的就add两次
                    if(seq == entry.getValue()){
                        list.add(entry.getKey());
                    }
                }
            }
            //将各个组排好序（降序），将组内排序
            //                  OUT:
            for(String key :list){
                if(outType!=3){
                    Map<Integer,Inventory> map2 = map.get(key);
                    List<Integer> sortMap2List = new ArrayList<>();
                    //将组内单元格按照seq2排序，升序
                    Collections.addAll(sortMap2List,map2.keySet().toArray(new Integer[map2.keySet().size()]));
                    Collections.sort(sortMap2List, new Comparator<Integer>() {
                        @Override
                        public int compare(Integer o1, Integer o2) {
                            return o2 - o1;//降序
                        }
                    });
                    //将组内单元格循环，生成任务发送给设备，并将已完成数量+
                    /**
                     * 在发送任务前需不需要将货位锁定？出库：retrievalRestricted，入库：reserved
                     * inventory里的货品数量是哪个字段？qty
                     * 接下来保存任务到数据库，供另一个线程将任务发动给设备
                     */
                    for(int seq2 : sortMap2List){
                        Inventory inventory = map2.get(seq2);
                        //创建JOB
                        createJob(orderposition,inventory,rol);

                        int qty = inventory.getQty().intValue();//货品数量
                        dingdanshuliang = dingdanshuliang - qty;
                        //修改订单表中的数据
                        int wancheng = rol.getWanchengdingdanshuliang()==null?0:rol.getWanchengdingdanshuliang();
                        rol.setWanchengdingdanshuliang(wancheng+qty);
                        session.saveOrUpdate(rol);

                        if(dingdanshuliang<=0){
                            break;
                        }
                    }
                }else{
                    //type==3，不靠近堆垛机的货位有货，需要把前面的货出库或移库
                    String bay = key.split(",")[0];
                    String level = key.split(",")[1];
                    String position = key.split(",")[2];
                    String actualArea = key.split(",")[3];
                    //查询前面的货位
                    Query query = session.createQuery("from Inventory i where i.container.location.bay=:bay and " +
                            " i.container.location.level=:level and i.container.location.position=:position and " +
                            " i.container.location.actualArea=:actualArea order by seq desc ");
                    query.setParameter("bay", Integer.parseInt(bay));
                    query.setParameter("level", Integer.parseInt(level));
                    query.setParameter("position", position);
                    query.setParameter("actualArea", actualArea);
                    List<Inventory> inventoryList1 =query.list();
                    for(Inventory inventory:inventoryList1){
                        //创建JOB
                        createJob(orderposition,inventory,rol);
                        if(inventory.getSkuCode().equals(rol.getShangpindaima()) && inventory.getLotNum().equals(rol.getLotNo())) {
                            int qty = inventory.getQty().intValue();//货品数量
                            dingdanshuliang = dingdanshuliang - qty;
                            //修改订单表中的数据
                            int wancheng = rol.getWanchengdingdanshuliang() == null ? 0 : rol.getWanchengdingdanshuliang();
                            rol.setWanchengdingdanshuliang(wancheng + qty);
                            session.saveOrUpdate(rol);
                        }
                        if(dingdanshuliang<=0){
                            break;
                        }
                    }
                }

                if(dingdanshuliang<=0){
                    break;
                }
            }
        }
        return dingdanshuliang;
    }

    /*
     * @author：ed_chen
     * @date：2018/8/22 17:38
     * @description：创建JOB
     * @param position
     * @param inventory
     * @param rol
     * @return：void
     */
    public static void createJob(String position,Inventory inventory,RetrievalOrderLine rol) throws Exception{
        Session session =HibernateUtil.getCurrentSession();
        JobDetail jobDetail = new JobDetail();
        Job job = new Job();
        //session准备存入job，commit时才会执行sql
        session.save(job);
        session.save(jobDetail);
        //数据准备
        String mckey = Mckey.getNext();
        String toStation = position.equals("1") ? "1201" : "1301";//到达站台
        String fromStation = position.equals("1") ? "ML01" : "ML02";//出发地点
        String type = AsrsJobType.RETRIEVAL; //出库
        //存入jobDetail
        jobDetail.setInventory(inventory);
        jobDetail.setQty(inventory.getQty());
        //存入job
        job.setContainer(inventory.getContainer().getBarcode());
        job.setFromStation(fromStation);
        job.setMcKey(mckey);
        job.setOrderNo(rol.getJinhuodanhao());
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
