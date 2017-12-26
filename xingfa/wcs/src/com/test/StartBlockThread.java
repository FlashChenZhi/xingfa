package com.test;

import com.test.blocks.Block;
import com.test.threads.BlockThread;
import com.test.utils.ThreadFactory;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by Administrator on 2016/12/18.
 */
public class StartBlockThread {
    public static void main(String[] args) {
        Transaction.begin();
        Session session = HibernateUtil.getCurrentSession();
        List<Block> blocks = session.createCriteria(Block.class).list();
        Transaction.commit();
        for (Block block : blocks) {
                BlockThread blockThread = ThreadFactory.getThread(block);
                if (blockThread != null) {
                    blockThread.start();
            }
        }
    }
}
