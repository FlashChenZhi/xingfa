package com.master.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.util.common.Const;
import com.util.common.LogMessage;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.domain.blocks.SCar;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 17:41 2018/7/20
 * @Description:
 * @Modified By:
 */
@Service
public class TrimStorageService {

    public ReturnObj<Map<String, Object>> addTrimStorage(String bay2, String level2){
        ReturnObj<Map<String, Object>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            //查询货位
            int bay = Integer.parseInt(bay2);
            int level = Integer.parseInt(level2);
            if(bay==8||bay==18||bay==28||bay==39){
                Transaction.rollback();
                s.setSuccess(false);
                s.setMsg("不可理货有柱子的列!");
                return s;
            }
            String locationNo = StringUtils.leftPad("3", 3,"0" )+
                    StringUtils.leftPad(bay2,3 ,"0" )+
                    StringUtils.leftPad(level2,3 ,"0" );

            Location location = Location.getByLocationNo(locationNo);
            if(location!=null){
                SCar sCar = (SCar) SCar.getByBlockNo("SC01");
                if(sCar.getPower()> Const.LHLOW_POWER){
                    Query query = session.createQuery("select count(*) as count from Job j where j.type=:type");
                    query.setParameter("type", AsrsJobType.MOVESTORAGE);
                    long count = (long) query.uniqueResult();
                    if(count>0){
                        Transaction.rollback();
                        s.setSuccess(false);
                        s.setMsg("存在其他理货任务!");
                        return s;
                    }else{
                        if(AsrsJob.findAsrsJobByLocation(location, AsrsJobType.MOVESTORAGE)){
                            createMoveStorageJob(session, location);
                        }else{
                            Transaction.rollback();
                            s.setSuccess(false);
                            s.setMsg("理货列存在其他任务!");
                            return s;
                        }
                    }

                }else{
                    Transaction.rollback();
                    s.setSuccess(false);
                    s.setMsg("一号小车电量不足!");
                    return s;
                }
            }else{
                Transaction.rollback();
                s.setSuccess(false);
                s.setMsg("货位不存在!");
                return s;
            }

            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            s.setSuccess(false);
            s.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        s.setSuccess(true);
        s.setMsg("创建任务成功!");
        return s;
    }

    public void createMoveStorageJob(Session session,Location fromLocation){
        Query query2 = session.createQuery("from Inventory i where i.container.location.bay = :bay " +
                "and i.container.location.level = :level and i.container.location.position = :position " +
                "and i.container.location.actualArea = :actualArea");
        query2.setParameter("bay", fromLocation.getBay());
        query2.setParameter("level", fromLocation.getLevel());
        query2.setParameter("position", fromLocation.getPosition());
        query2.setParameter("actualArea", fromLocation.getActualArea());
        List<Inventory> inventoryList = query2.list();

        JobDetail jobDetail = new JobDetail();
        Job job = new Job();
        //session准备存入job，commit时才会执行sql
        session.save(job);
        session.save(jobDetail);
        //数据准备

        String mckey = Mckey.getNext();
        String toStation = "ML01" ;//到达站台
        String fromStation ="ML01" ;//出发地点
        String type = AsrsJobType.MOVESTORAGE; //理货
        //存入jobDetail
        //jobDetail.setInventory(inventory);
        //jobDetail.setQty(inventory.getQty());
        //存入job
        //job.setContainer(inventory.getContainer().getBarcode());
        job.setFromStation(fromStation);
        job.setMcKey(mckey);
        //job.setOrderNo("4200026559");
        job.setSendReport(false);
        job.setStatus("1");
        job.setToStation(toStation);
        job.setType(type);
        job.addJobDetail(jobDetail);
        job.setCreateDate(new Date());
        job.setFromLocation(fromLocation);
        //要比出库多一个toLocation
        job.setToLocation(fromLocation);

        //修改此托盘
        for(Inventory inventory:inventoryList){
            Container container = inventory.getContainer();
            container.setReserved(true);
        }
        session.saveOrUpdate(fromLocation);
    }

}
