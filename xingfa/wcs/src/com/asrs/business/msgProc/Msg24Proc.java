
package com.asrs.business.msgProc;

import com.AllBinding.blocks.*;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.ReasonCode;
import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.domain.Msg03;
import com.asrs.message.Message24;
import com.asrs.message.MessageBuilder;
import com.asrs.message.MsgException;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.RefId;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.domain.XMLbean.XMLList.DataArea.StUnit;
import com.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.domain.XMLbean.XMLList.MovementReport;
import com.domain.consts.xmlbean.XMLConstant;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-3-19
 * Time: 20:44:42
 * Copyright Dsl.Worgsoft.
 */
public class Msg24Proc implements MsgProcess {
    public void Do(MessageBuilder msg) throws MsgException {
        Message24 message24 = new Message24(msg.DataString);
        message24.setPlcName(msg.PlcName);
        Do(message24);
    }

    @Override
    public void setProxy(XmlProxy wmsProxy, MessageProxy wcsProxy) {
        this._wmsProxy = wmsProxy;
        this._wcsProxy = wcsProxy;
    }

    XmlProxy _wmsProxy;
    MessageProxy _wcsProxy;

    public void Do(Message24 message24) {

        try {

            Transaction.begin();

            String mcKey = message24.mcKey;
            String type = message24.type;

            Query query = HibernateUtil.getCurrentSession().createQuery("from Block where mcKey=:mcKey or reservedMcKey=:resvMcKey");
            query.setParameter("mcKey", mcKey);
            query.setParameter("resvMcKey", mcKey);

            List<Block> blockList = query.list();

            for (Block block : blockList) {
                if (!block.getStatus().equals("0")) {
                    Transaction.rollback();
                    return;
                }

                if (block instanceof StationBlock) {
                    StationBlock station = (StationBlock) block;
                    station.setMcKey(null);
                    station.setWaitingResponse(false);
                }else if (block instanceof Dock) {
                    Dock dock = (Dock) block;
                    dock.setMcKey(null);
                    dock.setWaitingResponse(false);
                } else if (block instanceof SCar) {
                    SCar sCar = (SCar) block;
                    sCar.setReservedMcKey(null);
                    sCar.setMcKey(null);
                    sCar.setWaitingResponse(false);
                } else if (block instanceof Crane) {
                    Crane crane = (Crane) block;
                    crane.setMcKey(null);
                    crane.setReservedMcKey(null);
                    crane.setWaitingResponse(false);
                }
            }

            //删除03重发任务
            Msg03.clearMsg03(mcKey);

            Transaction.commit();

        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }


    }

}


