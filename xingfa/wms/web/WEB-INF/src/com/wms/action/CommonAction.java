package com.wms.action;

import com.wms.service.CommonService;
import com.util.common.HttpMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/11/24.
 */
@Controller
@RequestMapping("/common")
public class CommonAction {

    @Resource
    private CommonService commonService;

    @RequestMapping("searchStation.do")
    @ResponseBody
    public HttpMessage searchStation(String type) {
        HttpMessage httpMessage = commonService.searchStation(type);
        return httpMessage;

    }


}
