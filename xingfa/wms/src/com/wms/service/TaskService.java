package com.wms.service;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.thread.JobDoHelp;
import com.util.common.HttpMessage;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.AsrsJob;
import com.wms.domain.Job;
import com.wms.domain.blocks.Block;
import com.wms.domain.blocks.SCar;
import com.wms.domain.blocks.Srm;
import org.hibernate.Query;
import org.springframework.stereotype.Service;


/**
 * Created by wangfan
 * Created on 2017/3/11.
 */
@Service
public class TaskService {

    public HttpMessage cancelJob(String mcKey) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();

            Job job = Job.getByMcKey(mcKey);
            if (job == null) {
                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mcKey);
                if(asrsJob == null) {
                    httpMessage.setMsg("作业不存在");
                }else{
                    JobDoHelp.cancelPutaway(mcKey);
                    asrsJob.delete();
                }

            } else {

                if (job.getType().equals(AsrsJobType.PUTAWAY)) {
                    JobDoHelp.cancelPutaway(mcKey);
                } else if (job.getType().equals(AsrsJobType.RETRIEVAL)) {
                    JobDoHelp.retirevalCancel(mcKey);
                }

                httpMessage.setMsg("作业取消成功");

            }

            Transaction.commit();
            httpMessage.setSuccess(true);

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("系统异常");
            e.printStackTrace();

        }
        return httpMessage;
    }

    public HttpMessage finishJob(String mcKey) {
        HttpMessage httpMessage = new HttpMessage();
        try {

            Transaction.begin();

            Job job = Job.getByMcKey(mcKey);
            if (job == null) {

                httpMessage.setMsg("作业不存在");

            } else {

                if (job.getType().equals(AsrsJobType.PUTAWAY)) {
                    JobDoHelp.finishPutaway(mcKey);
                } else if (job.getType().equals(AsrsJobType.RETRIEVAL)) {
                    JobDoHelp.retrievalFinish(mcKey);
                }

                httpMessage.setMsg("作业强制完成");

            }

            Transaction.commit();
            httpMessage.setSuccess(true);

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("系统异常");
            e.printStackTrace();

        }
        return httpMessage;
    }

    public HttpMessage finishAsrsJob(String mcKey) {
        HttpMessage httpMessage = new HttpMessage();

        try {

            Transaction.begin();

            Query query = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where mcKey=:mckey");
            query.setParameter("mckey", mcKey);
            AsrsJob job = (AsrsJob) query.uniqueResult();

            if (job == null) {

                httpMessage.setMsg("作业不存在");

            } else {

                if (job.getType().equals(AsrsJobType.RECHARGE) || job.getType().equals(AsrsJobType.RECHARGEOVER)) {

                    HibernateUtil.getCurrentSession().delete(job);
                    SCar sCar = (SCar) Block.getByBlockNo("SC01");
                    if (sCar != null) {
                        if (mcKey.equals(sCar.getMcKey())) {
                            sCar.setMcKey(null);
                        }
                        if (mcKey.equals(sCar.getReservedMcKey())) {
                            sCar.setReservedMcKey(null);
                        }
                    }
                    Srm crane = (Srm) Block.getByBlockNo("ML01");
                    if (crane != null) {
                        if(mcKey.equals(crane.getMcKey())){
                            crane.setMcKey(null);
                        }
                        if(mcKey.equals(crane.getReservedMcKey())){
                            crane.setReservedMcKey(null);
                        }
                    }
                } else {
                    httpMessage.setMsg("只可结束充电作业，非充电作业请在在线任务查询删除完成");
                }

                httpMessage.setMsg("作业删除");

            }

            Transaction.commit();
            httpMessage.setSuccess(true);

        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("系统异常");
            e.printStackTrace();

        }
        return httpMessage;

    }
}
