package com.opple;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Job;
import com.wms.domain.Location;
import org.hibernate.Query;

/**
 * Created by van on 2018/1/9.
 */
public class PutawayeEngine {
    public static void main(String[] args) {
        while (true) {
            try {

                Transaction.begin();

                Query query = HibernateUtil.getCurrentSession().createQuery("from Job where status=:st");
                query.setParameter("st", AsrsJobStatus.DONE);
                query.setMaxResults(1);
                Job job = (Job) query.uniqueResult();
                if (job != null) {
                    if(job.getType().equals(AsrsJobType.PUTAWAY)){
                        WebService webService = new WebService();
                        webService.finishPutaway(job.getContainer());
                        Location location = job.getToLocation();
                        location.setEmpty(false);
                        location.setReserved(false);
                        HibernateUtil.getCurrentSession().delete(job);

                    }else if(job.getType().equals(AsrsJobType.RETRIEVAL)){
                        WebService webService = new WebService();
                        webService.finishOrder(job);
                        Location location = job.getFromLocation();
                        location.setEmpty(true);
                        location.setReserved(false);
                        HibernateUtil.getCurrentSession().delete(job);

                    }
                }

                Transaction.commit();

            } catch (Exception e) {
                Transaction.rollback();
                e.printStackTrace();

            } finally {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
