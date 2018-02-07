package com;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import org.hibernate.Session;

public class Test2 {
    public static void main(String[] args) {
            try {
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();

                Location location = Location.getByLocationNo("001033001");
                Container container = location.getContainers().iterator().next();
                Inventory inventory = container.getInventories().iterator().next();
                String position = inventory.getContainer().getLocation().getPosition();

                JobDetail jobDetail = new JobDetail();
                Job job = new Job();
                //session准备存入job，commit时才会执行sql
                session.save(job);
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
                job.setOrderNo("123");
                job.setSendReport(false);
                job.setStatus("1");
                job.setToStation(toStation);
                job.setType(type);
                job.addJobDetail(jobDetail);
                job.setFromLocation(location);

                //修改此托盘
                container.setReserved(true);

                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                e.printStackTrace();
            }


    }
}
