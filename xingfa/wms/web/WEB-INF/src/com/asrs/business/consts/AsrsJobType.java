package com.asrs.business.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Zhouyue
 * Date: 2008-10-16
 * Time: 14:52:54
 * Copyright Daifuku Shanghai Ltd.
 */
public class AsrsJobType {
    public static final String PUTAWAY = "01";//入库
    public static final String ST2ST = "02";//直行
    public static final String RETRIEVAL = "03";//出库
    public static final Map<String, String> map = new HashMap<String, String>();

    static {
        map.put(PUTAWAY, "入库");
        map.put(ST2ST, "直行");
        map.put(RETRIEVAL, "出库");
    }
}