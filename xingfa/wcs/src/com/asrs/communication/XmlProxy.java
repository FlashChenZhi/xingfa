package com.asrs.communication;

import com.asrs.message.Message;
import com.asrs.message.MessageBuilder;
import com.asrs.xml.Xml;
import com.asrs.xml.XmlBuilder;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-20
 * Time: 13:29:52
 * Copyright Dsl.Worgsoft.
 */
public interface XmlProxy extends Remote
{
    public void addRcvdXML(String envelope) throws RemoteException;

    public void addSendXML(String envelope) throws RemoteException;

    public String getRcvdXML() throws InterruptedException,RemoteException;

    public String getSendXML() throws InterruptedException,RemoteException;
}
