package com.thread.threads;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.domain.AsrsJob;
import com.asrs.message.Message26;
import com.thread.blocks.*;
import com.util.common.JedisUtil;
import com.util.hibernate.HibernateUtil;
import net.sf.json.JSONObject;
import org.hibernate.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public abstract class BlockThread<T> extends Thread {
    protected String _blockNo;

    public BlockThread(String blockNo) {
        _blockNo = blockNo;
    }

    protected T getBlock() {
        return (T) Block.getByBlockNo(_blockNo);
    }

    @Override
    public abstract void run();

}
