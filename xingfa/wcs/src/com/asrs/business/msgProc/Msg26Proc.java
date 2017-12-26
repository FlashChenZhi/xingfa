package com.asrs.business.msgProc;

import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.message.Message26;
import com.asrs.message.MessageBuilder;
import com.asrs.message.MsgException;
import com.util.common.JedisUtil;
import com.util.hibernate.Transaction;
import net.sf.json.JSONObject;

import javax.sound.midi.Soundbank;
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
                JSONObject jsonObject = JSONObject.fromObject(block);
                String value = JSONObject.fromObject(entry.getValue()).toString();
                JedisUtil.add(entry.getKey(), value);
            }
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }
    }
}
