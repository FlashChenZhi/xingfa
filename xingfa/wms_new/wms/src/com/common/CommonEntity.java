package com.common;


import com.util.common.ComboBoxVo;
import com.util.common.ReturnObj;

import java.util.List;

/**
 * Created by zhangming on 2014/10/22.
 */
public class CommonEntity {
    private String key;
    private ReturnObj<List<ComboBoxVo>> data;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ReturnObj<List<ComboBoxVo>> getData() {
        return data;
    }

    public void setData(ReturnObj<List<ComboBoxVo>> data) {
        this.data = data;
    }
}
