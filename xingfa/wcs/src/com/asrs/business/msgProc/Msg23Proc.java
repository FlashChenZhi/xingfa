package com.asrs.business.msgProc;

import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.Msg03;
import com.asrs.message.Message23;
import com.asrs.message.MessageBuilder;
import com.asrs.message.MsgException;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-3-19
 * Time: 20:44:42
 * Copyright Dsl.Worgsoft.
 */
public class Msg23Proc implements MsgProcess {
    public void Do(MessageBuilder msg) throws MsgException {
        Message23 message23 = new Message23(msg.DataString);
        message23.setPlcName(msg.PlcName);
        Do(message23);
    }

    @Override
    public void setProxy(XmlProxy wmsProxy, MessageProxy wcsProxy) {
        this._wmsProxy = wmsProxy;
        this._wcsProxy = wcsProxy;
    }

    XmlProxy _wmsProxy;
    MessageProxy _wcsProxy;


    public void Do(Message23 message23) {
        String mcKey = message23.McKey;
        Transaction.begin();
        Session session = HibernateUtil.getCurrentSession();
        Query q = session.createQuery("from Msg03 m where m.mcKey = :mcKey and m.machineNo = :machineNo and m.cycleOrder = :cycleOrder")
                .setString("mcKey", mcKey)
                .setString("machineNo", message23.MachineNo)
                .setString("cycleOrder", message23.CycleOrder);
        List<Msg03> msg03s = q.list();
        for (Msg03 msg03 : msg03s) {
            msg03.setReceived(true);
        }
        Transaction.commit();
    }
}
