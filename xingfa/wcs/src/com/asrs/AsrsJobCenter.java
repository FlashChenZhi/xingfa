package com.asrs;


import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.StationMode;
import com.asrs.business.msgProc.WcsMsgProcCenter;
import com.asrs.business.xmlProc.WmsMsgProcCenter;
import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.Plc;
import com.asrs.domain.Station;
import com.asrs.message.Message01;
import com.asrs.message.Message40;
import com.thread.blocks.StationBlock;
import com.util.common.Const;
import com.util.common.LogWriter;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-3-11
 * Time: 12:44:01
 * Copyright Dsl.Worgsoft.
 */
public class AsrsJobCenter {
    public static XmlProxy _wmsproxy;
    public static MessageProxy _wcsproxy;

    public static void main(String[] args) {
//        System.setSecurityManager(new RMISecurityManager());
        try {
//            _wmsproxy = (XmlProxy) Naming.lookup(Const.WMSPROXY);
            _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
        } catch (Exception e) {
            LogWriter.writeError(AsrsJobCenter.class, "初始化MessageProxy时发生错误");
            e.printStackTrace();
            System.exit(-1);
        }

//        Thread thread1 = new Thread(new WmsMsgProcCenter(_wmsproxy));
//        thread1.setName("WmsMsgProcCenter");
//        thread1.start();


        Thread thread2 = new Thread(new WcsMsgProcCenter(_wmsproxy, _wcsproxy));
        thread2.setName("WcsMsgProcCenter");
        thread2.start();

        try {
            Transaction.begin();

            List<String> list = new ArrayList<String>();
            list.add("00");
            List<Plc> plcs = HibernateUtil.getCurrentSession().createCriteria(Plc.class).list();
            for (Plc plc : plcs) {
                Message01 message01 = new Message01();
                message01.setPlcName(plc.getPlcName());
                message01.DataCount = "1";
                message01.MachineNos = list;
                _wcsproxy.addSndMsg(message01);
            }
            Query q = HibernateUtil.getCurrentSession().createQuery("from Station");

            List<Station> stationList = q.list();

            for (Station station : stationList) {
                StationBlock stationBlock = StationBlock.getByStationNo(station.getStationNo());
                Message40 message40 = new Message40();
                message40.setPlcName(stationBlock.getPlcName());
                message40.Mode = station.getMode().equals(AsrsJobType.PUTAWAY) ? "01" : "02";
                message40.Station = station.getStationNo();
                _wcsproxy.addSndMsg(message40);
            }

            Transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            Transaction.rollback();
        }

    }

}
