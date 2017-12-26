package com.asrs.business.msgProc;

import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.message.Message35;
import com.asrs.message.MessageBuilder;
import com.asrs.message.MsgException;
import com.AllBinding.Msg35;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-3-19
 * Time: 20:44:42
 * Copyright Dsl.Worgsoft.
 */
public class Msg35Proc implements MsgProcess {
    public void Do(MessageBuilder msg) throws MsgException {
        Message35 message35 = new Message35(msg.DataString);
        message35.setPlcName(msg.PlcName);
        Do(message35);
    }

    @Override
    public void setProxy(XmlProxy wmsProxy, MessageProxy wcsProxy) {
        this._wmsProxy = wmsProxy;
        this._wcsProxy = wcsProxy;
    }

    XmlProxy _wmsProxy;
    MessageProxy _wcsProxy;

    public void Do(Message35 message35) {
        Msg35.proc(message35);
    }
}
