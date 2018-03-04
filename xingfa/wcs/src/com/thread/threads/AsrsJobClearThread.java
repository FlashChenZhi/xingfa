package com.thread.threads;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.domain.AsrsJob;
import com.thread.blocks.Block;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;

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
            Transaction.commit();
        } catch (Exception ex) {
            Transaction.rollback();
            ex.printStackTrace();

        } finally {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    }
}
