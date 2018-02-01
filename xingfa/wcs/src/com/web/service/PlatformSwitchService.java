package com.web.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public class PlatformSwitchService {
    /**
     * 进入页面查询站台模式，默认下拉框选择该模式
     * @param zhantai 站台
     * @return 模式编号
     */
    public String findPlatformSwitch(String zhantai){
        System.out.println(zhantai);
        return "03";
    }
    /**
     * 站台模式切换更新
     * @param pattern 模式
     * @param zhantai 站台ID
     * @return "0"设定成功，"1"设定失败
     */
    public String updatePlatformSwitch(String pattern,String zhantai){
        System.out.println(pattern+";"+zhantai);
        return "0";
    }
}
