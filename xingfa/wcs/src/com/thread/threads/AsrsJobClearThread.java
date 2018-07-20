package com.thread.threads;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.WcsMessage;
import com.thread.blocks.Block;
import com.util.common.LogWriter;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;
import org.hibernate.exception.LockAcquisitionException;

import java.util.Date;
import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 14:53 2018/3/4
 * @Description:
 * @Modified By:
 */
public class AsrsJobClearThread {
    public static void main(String[] args) {
        while (true){
        try {
            Transaction.begin();

            long nowSecond = (new Date()).getTime();
            int overSeconds = 3600*1*1000;
            long overTimeSecond = nowSecond - overSeconds;
            Date overTime = new Date(overTimeSecond);

            Query jobQuery = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where status=:status").setParameter("status", AsrsJobStatus.DONE);
            List<AsrsJob> jobs = jobQuery.list();
            for (AsrsJob job : jobs) {
                Query query = HibernateUtil.getCurrentSession().createQuery("from Block where reservedMcKey=:mckey or mcKey=:mckey");
                query.setParameter("mckey", job.getMcKey());
                List<Block> blocks = query.list();
                if (blocks.isEmpty()) {
                    job.delete();
                }
            }

            Query msgQuery = HibernateUtil.getCurrentSession().createQuery("from WcsMessage wm where not exists(select aj.id from AsrsJob aj where aj.mcKey = wm.mcKey) and wm.received=true and lastSendDate <:overtime ");
            msgQuery.setTimestamp("overtime", overTime);
            List<WcsMessage> wms = msgQuery.list();
            for(WcsMessage wm : wms){
                HibernateUtil.getCurrentSession().delete(wm);
            }
            Transaction.commit();
        } catch (LockAcquisitionException e) {
            Transaction.rollback();
            e.printStackTrace();
            LogWriter.writeError(AsrsJobClearThread.class, "AsrsJobClearThread LockAcquisitionException "+e.getMessage());
            try {
                Thread.sleep(7000);
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }  catch (Exception ex) {
            Transaction.rollback();
            LogWriter.writeError(AsrsJobClearThread.class, "AsrsJobClearThread Exception "+ex.getMessage());
            ex.printStackTrace();

        } finally {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    }
}
