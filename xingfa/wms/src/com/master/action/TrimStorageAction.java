package com.master.action;

import com.master.service.TrimStorageService;
import com.util.common.ReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 17:40 2018/7/20
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/master/trimStorageAction")
public class TrimStorageAction {
    @Resource
    private TrimStorageService trimStorageService;

    @RequestMapping(value = "/addTrimStorage",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> addTrimStorage(String bay, String level){

        return trimStorageService.addTrimStorage(bay,level);
    }
}
