package com.wms;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import org.hibernate.Query;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * seq2此货位距离出库升降机的距离，越小越近
 * 各个组seq2最小值降序排列，将所剩货物最小的组，先出库，尽快腾出空间。
 * 将本组seq2升序排列，由近到远依次出库。
 */
public class StockOutThread {

  public static void main(String[] args) {
      while (true){
          try {
              Transaction.begin();
              Session session = HibernateUtil.getCurrentSession();
              Query query = HibernateUtil.getCurrentSession().createQuery("from RetrievalOrderLine r " +
                      "where r.dingdanshuliang > isnull(r.wanchengdingdanshuliang,0) ");
              List<RetrievalOrderLine> rolList = query.list();

              for (RetrievalOrderLine rol : rolList) {

                  int dingdanshuliang = rol.getDingdanshuliang();//获取订单数量

                  System.out.println(rol.getShouhuodanhao());
                  String shangpindaima = rol.getShangpindaima();
                  Query query2 = HibernateUtil.getCurrentSession().createQuery("from Inventory i where " +
                          "i.skuCode = :skuCode and i.container.reserved = false and i.container.location.retrievalRestricted = false and i.container.location.abnormal = false and not exists (select 1 from Location l where l.bay=i.container.location.bay and l.actualArea=i.container.location.actualArea " +
                          " and l.level =i.container.location.level and  l.position=i.container.location.position and l.seq2 < i.container.location.seq2 and l.seq > i.container.location.seq and l.reserved = true )");
                  query2.setParameter("skuCode",shangpindaima);
                  List<Inventory>  inventoryList = query2.list();
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
                              return o2 - o1;
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
                              int qty = inventory.getQty().intValue();//货品数量
                              dingdanshuliang = dingdanshuliang - qty;

                              String position = inventory.getContainer().getLocation().getOutPosition();

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
                              //修改订单表中的数据
                              int wancheng = rol.getWanchengdingdanshuliang()==null?0:rol.getWanchengdingdanshuliang();
                              rol.setWanchengdingdanshuliang(wancheng+qty);
                              session.saveOrUpdate(rol);
                              //修改此托盘
                              Container container = inventory.getContainer();
                              container.setReserved(true);
                              session.saveOrUpdate(container);

                              if(dingdanshuliang<=0){
                                  break;
                              }
                          }
                          if(dingdanshuliang<=0){
                              break;
                          }
                      }
                  }
              }
              Transaction.commit();
          }catch (Exception e){
              Transaction.rollback();
              e.printStackTrace();
          }

          try {
              Thread.sleep(10000);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      }
  }

}

