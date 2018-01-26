package com.common;


import com.util.common.ComboBoxVo;
import com.util.common.ReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/common")
public class CommonAction {
    @Resource(name = "commonService")
    private CommonService commonService;

    @RequestMapping(value = "/getData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, ReturnObj<List<ComboBoxVo>>> getData() {
        Map<String, ReturnObj<List<ComboBoxVo>>> map = new HashMap<String, ReturnObj<List<ComboBoxVo>>>();

        map.put("skuCodes", commonService.getSkuCodes());
        map.put("roleIds", commonService.getRoleIds());
        map.put("firstMenuIds", commonService.getFirstMenuIds());
        map.put("users", commonService.getUser());
        return map;
    }

}
