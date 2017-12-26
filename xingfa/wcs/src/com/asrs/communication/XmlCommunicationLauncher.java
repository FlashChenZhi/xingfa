package com.asrs.communication;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Author: Zhouyue Created by IntelliJ IDEA.
 * Date: 2008-2-19
 * Time: 12:31:30
 * Copyright Dsl.Worgsoft.
 */
public class XmlCommunicationLauncher {
    public static void main(String[] args) {
        try {

            System.setProperty("java.rmi.server.hostname","127.0.0.1");
            Naming.bind("rmi://127.0.0.1:1089/WmsXmlProxy",XmlCenter.instance());

//            final Registry registry = LocateRegistry.getRegistry();
//            registry.rebind("WmsXmlProxy", XmlCenter.instance());

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        Naming.unbind("rmi://127.0.0.1:1089/WmsXmlProxy");
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

//        List<Wms> wmss = new ArrayList<Wms>();
//        try {
//            Transaction.begin();
//            Session session = HibernateUtil.getCurrentSession();
//            wmss = session.createCriteria(Wms.class).list();
//            Transaction.commit();
//        } catch (RuntimeException ex) {
//            try {
//                Transaction.rollback();
//            } catch (RuntimeException rbEx) {
//            }
//            System.out.println("Database Error, Can't startup the Application.");
//            LogWriter.writeError(XmlCommunicationLauncher.class, ex.getMessage());
//            System.exit(-1);
//        }

        Thread thread1 = new Thread(new XmlServer());
        thread1.setName("XmlServer");
        thread1.start();
    }
}
