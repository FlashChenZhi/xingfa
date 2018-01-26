package com.wms.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.util.common.HttpMessage;
import com.util.common.LogWriter;
import com.util.common.LoggerType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.AsrsJob;
import com.wms.domain.blocks.Block;
import com.wms.domain.blocks.SCar;
import org.hibernate.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangfan
 * Created on 2017/3/11.
 */
@Service
public class BlockQueryService {


    public HttpMessage onLine(String blockNo, boolean b) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Query query = HibernateUtil.getCurrentSession().createQuery("from Block where blockNo=:blockNo");
            query.setParameter("blockNo", blockNo);
            query.setMaxResults(1);
            Block block = (Block) query.uniqueResult();
            if (b) {
                block.setStatus("1");
            } else {
                block.setStatus("9");
            }
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg("设定成功");
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS,e);

        }
        return httpMessage;
    }


    public HttpMessage cancelWaiting(String blockNo) {
        LogWriter.info(LoggerType.WMS, "取消等待：" + blockNo);

        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Query query = HibernateUtil.getCurrentSession().createQuery("from Block where blockNo=:blockNo");
            query.setParameter("blockNo", blockNo);
            query.setMaxResults(1);
            Block block = (Block) query.uniqueResult();
            block.setWaitingResponse(false);
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg("设定成功");
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS,e);

        }
        return httpMessage;
    }

    public HttpMessage charge(String blockNo) {
        LogWriter.info(LoggerType.WMS, "充电：" + blockNo);

        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            if (blockNo.equals("SC01")) {

                SCar sCar = (SCar) Block.getByBlockNo(blockNo);
                if(sCar.getStatus().equals("6")){
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("子车充电中");

                }else{

                    Query q = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type =:jtype or  type =:jobtype");
                    q.setParameter("jtype", AsrsJobType.RECHARGE);
                    q.setParameter("jobtype", AsrsJobType.RECHARGEOVER);
                    List<AsrsJob> jobs = q.list();
                    if (jobs.isEmpty()) {

                        AsrsJob job = new AsrsJob();
                        job.setType(AsrsJobType.RECHARGE);
                        job.setFromStation("ML01");
                        job.setToStation("ML01");
                        job.setToLocation("120101");
                        job.setFromLocation("120101");
                        job.setStatus("1");
                        job.setStatusDetail("0");
                        job.setMcKey(Mckey.getNext());
                        HibernateUtil.getCurrentSession().save(job);

                        httpMessage.setSuccess(true);
                        httpMessage.setMsg("设定成功");

                    } else {
                        httpMessage.setSuccess(false);
                        httpMessage.setMsg("系统存在充电任务");

                    }

                }

            } else {
                httpMessage.setSuccess(false);
                httpMessage.setMsg("非SC01不能充电");
            }
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS,e);

        }
        return httpMessage;

    }

    public HttpMessage chargeOver(String blockNo) {
        LogWriter.info(LoggerType.WMS, "充电结束：" + blockNo);

        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            if (blockNo.equals("SC01")) {


                SCar sCar = (SCar) Block.getByBlockNo(blockNo);
                if(!sCar.getStatus().equals("6")){
                    httpMessage.setSuccess(false);
                    httpMessage.setMsg("子车非充电中");

                }else{

                    Query q = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type =:jtype or  type =:jobtype");
                    q.setParameter("jtype", AsrsJobType.RECHARGE);
                    q.setParameter("jobtype", AsrsJobType.RECHARGEOVER);
                    List<AsrsJob> jobs = q.list();
                    if (jobs.isEmpty()) {

                        AsrsJob job = new AsrsJob();
                        job.setType(AsrsJobType.RECHARGEOVER);
                        job.setFromStation("ML01");
                        job.setToStation("ML01");
                        job.setFromLocation("120101");
                        job.setStatus("1");
                        job.setStatusDetail("0");
                        job.setMcKey(Mckey.getNext());
                        HibernateUtil.getCurrentSession().save(job);

                        httpMessage.setSuccess(true);
                        httpMessage.setMsg("设定成功");

                    } else {
                        httpMessage.setSuccess(false);
                        httpMessage.setMsg("存在充电任务");

                    }
                }


            } else {
                httpMessage.setSuccess(false);
                httpMessage.setMsg("非SC01不能充电");
            }
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
            LogWriter.error(LoggerType.WMS,e);

        }
        return httpMessage;

    }
}
