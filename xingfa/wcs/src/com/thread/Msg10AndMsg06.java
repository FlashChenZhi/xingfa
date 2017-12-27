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
public class Msg10AndMsg06 {
    public static void main(String[] args) throws Exception {
        while (true) {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            List<Plc> plcs = session.createCriteria(Plc.class).add(Restrictions.eq("status", "1")).list();
            MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
            for (Plc plc : plcs) {
                Message10 message10 = new Message10();
                message10.ConsoleNo = plc.getPlcName();
                message10.WcsNo = "1";
                message10.setPlcName(plc.getPlcName());
                message10.HeartBeatCycle = "5";

                Message06 message06 = new Message06();
                message06.setPlcName(plc.getPlcName());
                message06.Status = "0";
                _wcsproxy.addSndMsg(message10);
                _wcsproxy.addSndMsg(message06);
            }
            Thread.sleep(10000);
        }
    }
}