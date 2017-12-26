package com.AllBinding;

import com.AllBinding.blocks.Block;
import com.AllBinding.threads.BlockThread;
import com.AllBinding.utils.ThreadFactory;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by Administrator on 2016/12/18.
 */
public class StartBlockThread {
    public static void main(String[] args) {
        /**
         * 1、入库时，子母车到目标货位，子车载荷，子车卸货，母车入库任务完成。若下次入库货位仍为当前子母车位置，子车不上车，母车去接货，否则，子车上车
         * 2、出库时，子车取货且上到母车上，取货完成，母车载荷，若下次出库货位仍为当前子母车位置，子车下车，母车去卸货，否则，子车不下车
         */
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
