package com.web.action;

import com.util.common.HttpMessage;
import com.util.common.ReturnObj;
import com.web.service.PlatformSwitchService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Controller
@RequestMapping("/platformSwitch")
public class PlatformSwitchAction {

    @Resource
    private PlatformSwitchService platformSwitchService;
    /**
     * 进入页面查询站台模式，默认下拉框选择该模式
     * @param zhantai 站台
     * @return 模式编号
     */
    @RequestMapping(value = "findPlatformSwitch.do",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<String> findPlatformSwitch(String zhantai){
        System.out.println(zhantai);

        return platformSwitchService.findPlatformSwitch(zhantai);
    }

    /**
     * 站台模式切换更新
     * @param pattern 模式
     * @param zhantai 站台ID
     * @return "0"设定成功，"1"设定失败
     */
    @RequestMapping(value = "updatePlatformSwitch.do",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<String> updatePlatformSwitch(String pattern,String zhantai){
        System.out.println(pattern+";"+zhantai);
        return platformSwitchService.updatePlatformSwitch(pattern,zhantai);
    }
}
