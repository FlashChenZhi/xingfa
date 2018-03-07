package com.wms;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Container;
import com.wms.domain.Inventory;
import com.wms.domain.Job;
import com.wms.domain.JobDetail;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

public class TestOutKu {

    public static final String LocationNo = "007001001";

    public static void main(String[] args) {
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query2 = HibernateUtil.getCurrentSession().createQuery("from Inventory i where i.container.location.locationNo = :locationNo");
            query2.setString("locationNo",LocationNo);
            List<Inventory> inventoryList = query2.list();
            Inventory inventory =inventoryList.get(0);
            int qty = inventory.getQty().intValue();//货品数量

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
            Transaction.commit();
        }catch (Exception e){
            Transaction.rollback();
            e.printStackTrace();
        }

    }
}
