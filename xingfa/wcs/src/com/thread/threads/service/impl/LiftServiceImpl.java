package com.thread.threads.service.impl;

import com.thread.blocks.Lift;
import com.thread.threads.service.LiftService;

/**
 * Created by van on 2017/10/31.
 */
public class LiftServiceImpl implements LiftService {

    private Lift lift;

    public LiftServiceImpl(Lift lift) {
        this.lift = lift;
    }

    @Override
    public void withOutJob() throws Exception {

    }

    @Override
    public void withReserveMckey() throws Exception {

    }

    @Override
    public void withMckey() throws Exception {

    }
}
