package com.wms;

import com.sun.scenario.effect.impl.prism.ps.PPSDisplacementMapPeer;
import org.apache.http.impl.cookie.PublicSuffixDomainFilter;

/**
 * @Author: ed_chen
 * @Date: Create in 11:02 2018/9/4
 * @Description:
 * @Modified By:
 */
public class NeatenThread {
    public static void main(String[] args) {
        Thread thread2 = new Thread(new DayNeatenThread());
        thread2.setName("DayNeatenThread");
        thread2.start();

        /*Thread thread1 = new Thread(new MonthNeatenThread());
        thread1.setName("MonthNeatenThread");
        thread1.start();*/
    }
}
