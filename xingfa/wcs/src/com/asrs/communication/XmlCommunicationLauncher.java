package com.asrs.communication;

import com.asrs.business.xmlProc.WmsMsgProcCenter;
import com.asrs.domain.SystemLog;
import com.asrs.domain.XMLMessage;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.TransportOrder;
import com.domain.XMLbean.XMLProcess;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;


/**
 * Author: Zhouyue Created by IntelliJ IDEA.
 * Date: 2008-2-19
 * Time: 12:31:30
 * Copyright Dsl.Worgsoft.
 */
public class XmlCommunicationLauncher {

    public static void main(String[] args) {

        while (true) {
            try {
                Transaction.begin();
                Query query = HibernateUtil.getCurrentSession().createQuery("from XMLMessage where status='1' and recv =:recv order by id asc")
                        .setParameter("recv", "WCS").setMaxResults(1);

                XMLMessage message = (XMLMessage) query.uniqueResult();

                if (message != null) {

                    String xml = message.getMessageInfo();

                    Envelope e = XMLUtil.getEnvelope(xml);

                    XMLProcess xmlProcess = WmsMsgProcCenter.getOrder(e);

                    try {

                        xmlProcess.execute();
                        message.setStatus("2");

                    } catch (Exception ee) {
                        SystemLog.error(ee.getMessage());
                        message.setStatus("3");
//                        ((TransportOrder) xmlProcess).sendReport("03");
                    }


                }
//                else{
//                    query = HibernateUtil.getCurrentSession().createQuery("from XMLMessage where status='3' and recv =:recv order by id asc")
//                            .setParameter("recv", "WCS").setMaxResults(1);
//
//                    message = (XMLMessage) query.uniqueResult();
//
//                    if (message != null) {
//
//                        String xml = message.getMessageInfo();
//
//                        Envelope e = XMLUtil.getEnvelope(xml);
//
//                        XMLProcess xmlProcess = WmsMsgProcCenter.getOrder(e);
//
//                        try {
//
//                            xmlProcess.execute();
//                            message.setStatus("2");
//
//                        } catch (Exception ee) {
//                            System.out.println(ee.getMessage());
//                        }
//
//
//                    }
//                }


                Transaction.commit();

            } catch (Exception e) {
                Transaction.rollback();
                e.printStackTrace();
            } finally {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


//    public static void main(String[] args) {
//        try {
//            final Registry registry = LocateRegistry.getRegistry();
//            registry.rebind("WmsXmlProxy", XmlCenter.instance());
//
//            Runtime.getRuntime().addShutdownHook(new Thread() {
//                public void run() {
//                    try {
//                        registry.unbind("WmsXmlProxy");
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    } catch (NotBoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            System.exit(-1);
//        }

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
//
//        Thread thread1 = new Thread(new XmlServer());
//        thread1.setName("XmlServer");
//        thread1.start();
//    }
}
