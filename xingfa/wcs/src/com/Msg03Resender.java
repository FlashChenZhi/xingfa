package com;

import com.asrs.communication.MessageProxy;
import com.asrs.domain.WcsMessage;
import com.asrs.message.Message03;
import com.thread.blocks.Block;
import com.util.common.Const;
import com.util.common.DateFormat;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.rmi.Naming;
import java.util.*;

/**
 * Created by Administrator on 2016/12/13.
 */
public class Msg03Resender {
    public static void main(String[] args) {
        while (true) {
            try {
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();

                long nowSecond = (new Date()).getTime();
                int overSeconds = 3000;
                long overTimeSecond = nowSecond - overSeconds;
                Date overTime = new Date(overTimeSecond);

                Query q = session.createQuery("from WcsMessage m where m.received = false and m.lastSendDate <:overTime order by m.lastSendDate")
                        .setDate("overTime", overTime);

                List<WcsMessage> msg03s = q.list();
                for (WcsMessage msg03 : msg03s) {
                    Message03 m3 = new Message03();
                    m3.setPlcName(msg03.getPlcName());
                    m3.IdClassification = "1";
                    m3.JobType = msg03.getJobType();
                    m3.McKey = msg03.getMcKey();
                    m3.MachineNo = msg03.getMachineNo();
                    m3.CycleOrder = msg03.getCycleOrder();
                    m3.Height = msg03.getHeight();
                    m3.Width = msg03.getWidth();
                    m3.Station = msg03.getStation();
                    m3.Bank = msg03.getBank();
                    m3.Bay = msg03.getBay();
                    m3.Level = msg03.getLevel();
                    m3.Dock = msg03.getDock();

                    MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
                    _wcsproxy.addSndMsg(m3);
                    System.out.println("resendId: " + msg03.getId());

                    msg03.setLastSendDate(new Date());

                    Thread.sleep(500);
                }

                Transaction.commit();
            } catch (Exception ex) {
                Transaction.rollback();
                ex.printStackTrace();

            } finally {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }


    }
}
