package com.thread.threads.service.impl;

import com.thread.blocks.MCar;
import com.thread.threads.service.MCarService;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by van on 2017/10/31.
 */
public class MCarServiceImpl implements MCarService {
    private MCar mCar;

    public MCarServiceImpl(MCar mCar) {
        this.mCar = mCar;
    }

    @Override
    public void withOutJob() throws Exception {

        if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {

        } else {

        }

    }

    @Override
    public void withReserveMckey() throws Exception {

    }

    @Override
    public void withMckey() throws Exception {

    }
}
