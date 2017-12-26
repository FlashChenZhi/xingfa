package com;

import com.asrs.domain.Msg03;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */
public class Msg03AutoResender {

    public static void main(String[] args) {
        while (true) {
            try {

                Transaction.begin();

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) - 10);
                Date gateDate = cal.getTime();

                Query msgQ = HibernateUtil.getCurrentSession().createQuery("from Msg03 where received = false and createDate <:createDate");
                msgQ.setParameter("createDate", gateDate);
                List<Msg03> msg03s = msgQ.list();
                for (Msg03 msg : msg03s) {
                    msg.setSendStatus("1");
                }
                Transaction.commit();

            } catch (Exception e) {
                Transaction.rollback();
                e.printStackTrace();
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
