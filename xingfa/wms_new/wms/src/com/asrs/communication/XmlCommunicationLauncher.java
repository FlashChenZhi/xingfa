package com.asrs.communication;

import com.util.common.LogWriter;
import com.util.common.LoggerType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Wcs;
import org.hibernate.Session;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Zhouyue Created by IntelliJ IDEA.
 * Date: 2008-2-19
 * Time: 12:31:30
 * Copyright Dsl.Worgsoft.
 */
public class XmlCommunicationLauncher {
    public static void main(String[] args) {
        try {
//            final Registry registry = LocateRegistry.getRegistry();
//            registry.rebind("XmlProxy", XmlCenter.instance());

            System.setProperty("java.rmi.server.hostname","127.0.0.1");
            Naming.bind("rmi://127.0.0.1:1089/XmlProxy",XmlCenter.instance());


            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        Naming.unbind("rmi://127.0.0.1:1089/XmlProxy");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (NotBoundException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

        List<Wcs> wcses = new ArrayList<Wcs>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            wcses = session.createCriteria(Wcs.class).list();
            Transaction.commit();
        } catch (RuntimeException ex) {
            try {
                Transaction.rollback();
            } catch (RuntimeException rbEx) {
            }
            System.out.println("Database Error, Can't startup the Application.");
            LogWriter.error(LoggerType.XMLMessageInfo, ex.getMessage());
            System.exit(-1);
        }

        XmlConnectionManager xmlConectionManager = new XmlConnectionManager();
        for (Wcs wcs : wcses) {
            xmlConectionManager.connectXML(wcs.getWcsName(), wcs.getIpAddress(), wcs.getPort());
        }

        Thread thread = new Thread(xmlConectionManager);
        thread.setName("XmlConnectionManager");
        thread.start();
    }
}
