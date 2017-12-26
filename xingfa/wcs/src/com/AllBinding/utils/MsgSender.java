package com.AllBinding.utils;

import com.AllBinding.blocks.Block;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.communication.MessageProxy;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Msg03;
import com.asrs.message.Message03;
import com.util.common.Const;
import com.util.common.LogWriter;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang.StringUtils;

import java.rmi.Naming;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/1.
 */
public class MsgSender {

    public static void send03(String cycleOrder, String mcKey, Block block, String locationNo, String blockNo, String bay, String level) throws Exception {
        Message03 message03 = new Message03();
        message03.setPlcName(block.getPlcName());
        AsrsJob aj = AsrsJob.getAsrsJobByMcKey(mcKey);
        if (aj != null) {
            message03.JobType = aj.getType();
        } else {
            message03.JobType = AsrsJobType.PUTAWAY;
        }
        message03.McKey = mcKey;
        message03.MachineNo = block.getBlockNo();
        message03.CycleOrder = cycleOrder;
        if (StringUtils.isNotBlank(locationNo)) {
            message03.Bank = locationNo.substring(0, 2);
            message03.Bay = locationNo.substring(2, 4);
            message03.Level = locationNo.substring(4, 6);
        }
        message03.Station = blockNo;
        if (StringUtils.isNotBlank(bay))
            message03.Bay = bay;
        if (StringUtils.isNotBlank(level))
            message03.Level = level;
        message03.Dock = HibernateUtil.nextSeq("seq_dock") + "";

        Msg03 msg03 = new Msg03();

        msg03.setPlcName(message03.getPlcName());
        msg03.setJobType(message03.JobType);
        msg03.setMachineNo(message03.MachineNo);
        msg03.setMcKey(message03.McKey);
        msg03.setCycleOrder(message03.CycleOrder);
        msg03.setHeight(message03.Height);
        msg03.setWidth(message03.Width);
        msg03.setStation(message03.Station);
        msg03.setBank(message03.Bank);
        msg03.setBay(message03.Bay);
        msg03.setLevel(message03.Level);
        msg03.setDock(StringUtils.leftPad(message03.Dock, 4, '0'));
        msg03.setLastSendDate(new Date());
        msg03.setCreateDate(new Date());
        msg03.setReceived(false);
        msg03.setMsgType("03");

        HibernateUtil.getCurrentSession().save(msg03);
        MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
        _wcsproxy.addSndMsg(message03);

        HibernateUtil.getCurrentSession().flush();

        HibernateUtil.getCurrentSession().createQuery("update Block set waitingResponse = true where blockNo=:blockNo").setParameter("blockNo", block.getBlockNo()).executeUpdate();

//        block.setWaitingResponse(true);
    }
}

