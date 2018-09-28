package com.asrs.business.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 10:04 2018/9/12
 * @Description:
 * @Modified By:
 */
public class SkuType {
    public static final String FINISHED_PRODUCT = "1";//产成品
    public static final String RAW_MATERIAL= "2";//原材料
    public static final String PACKING_BAG = "3";//包装袋
    public static final Map<String, String> map = new HashMap<String, String>();
    static {
        map.put(FINISHED_PRODUCT, "产成品");
        map.put(RAW_MATERIAL, "原材料");
        map.put(PACKING_BAG, "包装袋");
    }
}
