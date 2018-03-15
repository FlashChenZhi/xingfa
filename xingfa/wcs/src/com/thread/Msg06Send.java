package com.thread;

import com.asrs.communication.MessageProxy;
import com.asrs.domain.Plc;
import com.asrs.message.Message06;
import com.asrs.message.Message10;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.rmi.Naming;
import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */
public class Msg06Send {
    public static void main(String[] args) {
        while (true) {
            try {
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();
                List<Plc> plcs = session.createCriteria(Plc.class).list();
                MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
                for (Plc plc : plcs) {
                    Message06 message06 = new Message06();
                    message06.setPlcName(plc.getPlcName());
                    message06.Status = "0";
                    _wcsproxy.addSndMsg(message06);

                }
                Transaction.commit();

            } catch (Exception e) {
                Transaction.rollback();
                e.printStackTrace();
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
