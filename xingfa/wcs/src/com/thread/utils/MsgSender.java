package com.thread.utils;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.ReasonCode;
import com.asrs.communication.MessageProxy;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.domain.WcsMessage;
import com.asrs.message.Message03;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.Receiver;
import com.domain.XMLbean.XMLList.ControlArea.RefId;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.domain.XMLbean.XMLList.MovementReport;
import com.domain.consts.xmlbean.XMLConstant;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.Srm;
import com.util.common.Const;
import com.util.common.LogWriter;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            m3.JobType = AsrsJobType.RETRIEVAL;
        }
        m3.McKey = mcKey;
        m3.MachineNo = block.getBlockNo();
        m3.CycleOrder = cycleOrder;

        if (StringUtils.isNotBlank(locationNo)) {
            Location location = Location.getByLocationNo(locationNo);
            m3.Bank = StringUtils.leftPad(location.getBank() + "", 2, '0');
            m3.Bay = StringUtils.leftPad(location.getBay() + "", 2, '0');
            m3.Level = StringUtils.leftPad(location.getLevel() + "", 2, '0');
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
        Location location = null;
        if(aj != null) {
            if (aj.getType().equals(AsrsJobType.PUTAWAY)) {
                location = Location.getByLocationNo(aj.getToLocation());
            } else {
                location = Location.getByLocationNo(aj.getFromLocation());
            }
            if (location != null) {
                m3.Height = String.valueOf(Integer.parseInt(location.getPositionType()));
            }
        }

        WcsMessage msg03 = new WcsMessage();

        String dock = StringUtils.leftPad(HibernateUtil.nextSeq("seq_xml") + "", 4, '0');

        m3.Dock = dock;
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
        msg03.setDock(dock);
        msg03.setLastSendDate(new Date());
        msg03.setCreateDate(new Date());
        msg03.setReceived(false);

        msg03.setMsgType(WcsMessage.MSGTYPE_03);

        HibernateUtil.getCurrentSession().save(msg03);

        //LogWriter.writeInfo("WMS_INFO", m3.toString());
        MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
        _wcsproxy.addSndMsg(m3);
        block.setWaitingResponse(true);

        if (block instanceof Srm) {
            HibernateUtil.getCurrentSession().createQuery("update Srm set waitingResponse=true,checkLocation = false where blockNo =:blockNo ")
                    .setParameter("blockNo", block.getBlockNo()).executeUpdate();
        }else if (block instanceof MCar) {
            HibernateUtil.getCurrentSession().createQuery("update MCar set waitingResponse=true,checkLocation = false where blockNo =:blockNo ")
                    .setParameter("blockNo", block.getBlockNo()).executeUpdate();
        } else {

            HibernateUtil.getCurrentSession().createQuery("update Block set waitingResponse=true where blockNo =:blockNo ")
                    .setParameter("blockNo", block.getBlockNo()).executeUpdate();
        }
    }
}

