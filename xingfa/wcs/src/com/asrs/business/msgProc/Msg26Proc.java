package com.asrs.business.msgProc;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.AsrsJob;
import com.asrs.message.Message26;
import com.asrs.message.MessageBuilder;
import com.asrs.message.MsgException;
import com.thread.blocks.Block;
import com.thread.blocks.Lift;
import com.thread.blocks.SCar;
import com.util.common.JedisUtil;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Date;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-3-19
 * Time: 20:44:42
 * Copyright Dsl.Worgsoft.
 */
public class Msg26Proc implements MsgProcess {
    public void Do(MessageBuilder msg) throws MsgException {
        Message26 message26 = new Message26(msg.DataString);
        message26.setPlcName(msg.PlcName);
        Do(message26);
    }

    @Override
    public void setProxy(XmlProxy wmsProxy, MessageProxy wcsProxy) {
        this._wmsProxy = wmsProxy;
        this._wcsProxy = wcsProxy;
    }

    XmlProxy _wmsProxy;
    MessageProxy _wcsProxy;


    public void Do(Message26 message26) {
        try {
            for (Map.Entry<String, Message26.Block> entry : message26.Blocks.entrySet()) {
                Message26.Block block = entry.getValue();
                String value = JSONObject.fromObject(entry.getValue()).toString();

                Transaction.begin();
                Block block1 = (Block) HibernateUtil.getCurrentSession().createQuery("from Block where blockNo=:blocnNo").setParameter("blocnNo",entry.getKey().toString()).uniqueResult();
                block1.setError(block.getErrorCode());
                Transaction.commit();
            }
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }

    }
}
