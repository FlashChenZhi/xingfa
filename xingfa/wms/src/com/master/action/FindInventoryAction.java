package com.master.action;

import com.master.service.FindInventoryService;
import com.util.common.PagerReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 23:47 2018/3/4
 * @Description: 查询库存
 * @Modified By:
 */
@Controller
@RequestMapping("/master/FindInventoryAction")
public class FindInventoryAction {
    @Resource
    private FindInventoryService findInventoryService;

    @RequestMapping(value = "/findInventory",method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<Map<String,Object>>> findInventory(int current, int defaultPageSize){
        int startIndex = (current-1) * defaultPageSize;
        return findInventoryService.findInventory(startIndex,defaultPageSize);
    }

}
