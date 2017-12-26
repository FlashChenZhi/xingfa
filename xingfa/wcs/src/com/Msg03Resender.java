package com;

import com.asrs.communication.MessageProxy;
import com.asrs.domain.Msg03;
import com.asrs.message.Message03;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.rmi.Naming;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */
public class Msg03Resender {

    public static void main(String[] args) {
        while (true) {
            try {

                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();

                Query q = session.createQuery("from Msg03 m where m.sendStatus=:status and m.msgType = '03' order by m.lastSendDate")
                        .setString("status", "1");
                List<Msg03> msg03s = q.list();
                for (Msg03 msg03 : msg03s) {
                    Message03 m3 = new Message03();
                    m3.setPlcName(msg03.getPlcName());
                    m3.IdClassification = "1";
                    m3.JobType = msg03.getJobType();
                    m3.McKey = msg03.getMcKey();
                    m3.MachineNo = msg03.getMachineNo();
                    m3.CycleOrder = msg03.getCycleOrder();
                    m3.Height = StringUtils.isBlank(msg03.getHeight()) ? "0" : msg03.getHeight();
                    m3.Width = StringUtils.isBlank(msg03.getWidth()) ? "0" : msg03.getWidth();
                    m3.Station = StringUtils.isBlank(msg03.getStation()) ? "0000" : msg03.getStation();
                    m3.Bank = StringUtils.isBlank(msg03.getBank()) ? "00" : msg03.getBank();
                    m3.Bay = StringUtils.isBlank(msg03.getBay()) ? "00" : msg03.getBay();
                    m3.Level = StringUtils.isBlank(msg03.getLevel()) ? "00" : msg03.getLevel();
                    m3.Dock = StringUtils.isBlank(msg03.getDock()) ? "0000" : msg03.getDock();
                    MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);


                    Msg03 msg = new Msg03();
                    msg.setMsgType(msg03.getMsgType());
                    msg.setMcKey(msg03.getMcKey());
                    msg.setJobType(msg03.getJobType());
                    msg.setMachineNo(msg03.getMachineNo());
                    msg.setCycleOrder(msg03.getCycleOrder());
                    msg.setHeight(msg03.getHeight());
                    msg.setWidth(msg03.getWidth());
                    msg.setStation(msg03.getStation());
                    msg.setBank(msg03.getBank());
                    msg.setBay(msg03.getBay());
                    msg.setLevel(msg03.getLevel());
                    msg.setDock(msg03.getDock());
                    msg.setLastSendDate(new Date());
                    msg.setReceived(false);
                    msg.setSendStatus("2");

                    HibernateUtil.getCurrentSession().save(msg);

                    _wcsproxy.addSndMsg(m3);

                    session.createQuery("update Msg03 set lastSendDate=:lastDate,sendStatus=:status where id=:msgId").setParameter("lastDate", new Date())
                            .setParameter("status", "2").setParameter("msgId", msg03.getId()).executeUpdate();

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

//    public static void main(String[] args) {
//        while (true) {
//            try {
//                Transaction.begin();
//                Session session = HibernateUtil.getCurrentSession();
//                long nowSecond = (new Date()).getTime();
//                int overSeconds = 15000;
//                long overTimeSecond = nowSecond - overSeconds;
//                Date overTime = new Date(overTimeSecond);
//                org.hibernate.Query q = session.createQuery("from Msg03 m where m.received = false and m.lastSendDate >:overTime order by m.lastSendDate")
//                        .setDate("overTime", overTime);
//                List<Msg03> msg03s = q.list();
//                for (Msg03 msg03 : msg03s) {
//                    Message03 m3 = new Message03();
//                    m3.setPlcName(msg03.getPlcName());
//                    m3.IdClassification = "1";
//                    m3.JobType = msg03.getJobType();
//                    m3.McKey = msg03.getMcKey();
//                    m3.MachineNo = msg03.getMachineNo();
//                    m3.CycleOrder = msg03.getCycleOrder();
//                    m3.Height = StringUtils.isBlank(msg03.getHeight()) ? "0":msg03.getHeight();
//                    m3.Width = StringUtils.isBlank(msg03.getWidth())?"0":msg03.getWidth();
//                    m3.Station = StringUtils.isBlank(msg03.getStation()) ? "0000":msg03.getStation();
//                    m3.Bank = StringUtils.isBlank(msg03.getBank())?"00":msg03.getBank();
//                    m3.Bay = StringUtils.isBlank(msg03.getBay())?"00":msg03.getBay();
//                    m3.Level = StringUtils.isBlank(msg03.getLevel())?"00":msg03.getLevel();
//                    m3.Dock = StringUtils.isBlank(msg03.getDock())?"0000":msg03.getDock();
//                    MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
//                    _wcsproxy.addSndMsg(m3);
//
////                    msg03.setLastSendDate(new Date());
//                    session.createQuery("update Msg03 set lastSendDate=:lastDate where id=:msgId").setParameter("lastDate", new Date())
//                            .setParameter("msgId", msg03.getId()).executeUpdate();
//
//                }
//
//                Transaction.commit();
//            } catch (Exception ex) {
//                Transaction.rollback();
//                ex.printStackTrace();
//            }
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }
}
