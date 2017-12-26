package com.test.threads;

import com.test.blocks.Block;

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
