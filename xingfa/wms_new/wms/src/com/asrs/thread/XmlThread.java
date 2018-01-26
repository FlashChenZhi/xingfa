package com.asrs.thread;

import com.asrs.business.xmlProc.WmsMsgProcCenter;
import com.asrs.domain.XMLbean.Envelope;
import com.asrs.domain.XMLbean.XMLProcess;
import com.asrs.xml.util.XMLUtil;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.XMLMessage;
import org.hibernate.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */
public class XmlThread implements Runnable {

    public static void main(String[] args) {
        XmlThread thread = new XmlThread();
        thread.run();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Transaction.begin();

                Query query = HibernateUtil.getCurrentSession().createQuery("from XMLMessage where status='1' and recv =:recv")
                        .setParameter("recv", "WMS").setMaxResults(1);

                XMLMessage message = (XMLMessage) query.uniqueResult();
                if (message != null) {

                    String xml = message.getMessageInfo();

                    Envelope e = XMLUtil.getEnvelope(xml);
                    XMLProcess xmlProcess = WmsMsgProcCenter.getOrder(e);
                    xmlProcess.execute();
                    message.setStatus("2");
                }

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
}
