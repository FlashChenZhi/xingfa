package com.asrs.business.msgProc;

import com.asrs.business.consts.StationMode;
import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.Station;
import com.asrs.message.Message42;
import com.asrs.message.MessageBuilder;
import com.asrs.message.MsgException;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.TransportModeChangeDA;
import com.domain.XMLbean.XMLList.TransportModeChange;
import com.domain.XMLbean.XMLList.TransportModeChangeReport;
import com.domain.consts.xmlbean.XMLConstant;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-3-19
 * Time: 20:44:42
 * Copyright Dsl.Worgsoft.
 */
public class Msg42Proc implements MsgProcess {
    public void Do(MessageBuilder msg) throws MsgException {
        Message42 message42 = new Message42(msg.DataString);
        message42.setPlcName(msg.PlcName);
        Do(message42);
    }

    @Override
    public void setProxy(XmlProxy wmsProxy, MessageProxy wcsProxy) {
        this._wmsProxy = wmsProxy;
        this._wcsProxy = wcsProxy;
    }

    XmlProxy _wmsProxy;
    MessageProxy _wcsProxy;


    public void Do(Message42 message42) {
//        Station station=Station.getStation(message42.Station);
//            if (StationMode.UNKNOWN.equals(message42.Mode)) {
//                station.setMode(station.getOldMode());
//            } else {
//                station.setMode(message42.Mode);
//            }
        if (StationMode.RETRIEVAL.equals(message42.Action)) {
            Sender sender = new Sender();
            sender.setDivision(XMLConstant.COM_DIVISION);
            ControlArea controlArea = new ControlArea();
            controlArea.setSender(sender);
            TransportModeChangeDA transportModeChangeDA = new TransportModeChangeDA();
            transportModeChangeDA.setMha(message42.Station);
            transportModeChangeDA.setTransportType(message42.Mode);
            TransportModeChange transportModeChange = new TransportModeChange();
            transportModeChange.setDate(new Date());
            transportModeChange.setControlArea(controlArea);
            transportModeChange.setDataArea(transportModeChangeDA);
            Envelope envelope = new Envelope();
            envelope.setTransportModeChange(transportModeChange);
            try {
                XMLUtil.sendEnvelope(envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Station station = Station.getStation(message42.Station);
            station.setOldMode(station.getMode());
            station.setMode(StationMode.UNKNOWN);
            Sender sender = new Sender();
            sender.setDivision(XMLConstant.COM_DIVISION);
            ControlArea controlArea = new ControlArea();
            controlArea.setSender(sender);
            TransportModeChangeDA transportModeChangeDA = new TransportModeChangeDA();
            transportModeChangeDA.setMha(message42.Station);
            transportModeChangeDA.setTransportType(message42.Mode);
            TransportModeChangeReport transportModeChangeReport = new TransportModeChangeReport();
            transportModeChangeReport.setDate(new Date());
            transportModeChangeReport.setControlArea(controlArea);
            transportModeChangeReport.setDataArea(transportModeChangeDA);
            Envelope envelope = new Envelope();
            envelope.setTransportModeChangeReport(transportModeChangeReport);
            try {
                XMLUtil.sendEnvelope(envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
