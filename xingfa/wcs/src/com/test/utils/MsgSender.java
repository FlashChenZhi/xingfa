package com.test.utils;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.communication.MessageProxy;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Msg03;
import com.asrs.message.Message03;
import com.test.blocks.Block;
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
        Message03 m3 = new Message03();
        m3.setPlcName(block.getPlcName());
        AsrsJob aj = AsrsJob.getAsrsJobByMcKey(mcKey);
        if (aj != null) {
            m3.JobType = aj.getType();
        } else {
            m3.JobType = AsrsJobType.ST2ST;
        }
        m3.McKey = mcKey;
        m3.MachineNo = block.getBlockNo();
        m3.CycleOrder = cycleOrder;

        if (StringUtils.isNotBlank(locationNo)) {
            m3.Bank = locationNo.substring(0, 2);
            m3.Bay = locationNo.substring(2, 4);
            m3.Level = locationNo.substring(4, 6);
        }
        if (StringUtils.isNotBlank(blockNo)) {
            m3.Station = blockNo;
        }
        if (StringUtils.isNotBlank(bay)) {
            m3.Bay = bay;
        }
        if (StringUtils.isNotBlank(level)) {
            m3.Level = level;
        }

        Msg03 msg03 = new Msg03();

        msg03.setPlcName(m3.getPlcName());
        msg03.setJobType(m3.JobType);
        msg03.setMachineNo(m3.MachineNo);
        msg03.setMcKey(m3.McKey);
        msg03.setCycleOrder(m3.CycleOrder);
        msg03.setHeight(m3.Height);
        msg03.setWidth(m3.Width);
        msg03.setStation(m3.Station);
        msg03.setBank(m3.Bank);
        msg03.setBay(m3.Bay);
        msg03.setLevel(m3.Level);
        msg03.setDock(m3.Dock);
        msg03.setLastSendDate(new Date());
        msg03.setCreateDate(new Date());
        msg03.setReceived(false);

        HibernateUtil.getCurrentSession().save(msg03);
        System.out.println(m3.toString());
        LogWriter.writeInfo("WMS_INFO", m3.toString());
        MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
        _wcsproxy.addSndMsg(m3);
        block.setWaitingResponse(true);
    }

//    public static void main(String[] args) {
//        try {
//            Transaction.begin();
//            Block block = Block.getByBlockNo("SC01");
//            Block block1 = Block.getByBlockNo("MC02");
////            MsgSender.send03(Message03._CycleOrder.offCar, "0000", block, "", "MC01", "", "");
//            MsgSender.send03(Message03._CycleOrder.move, "0080", block1, "", "", "4", "");
//            Transaction.commit();
//        } catch (Exception e) {
//            Transaction.rollback();
//            e.printStackTrace();
//        }
//    }
}

