package com.inventory.action;

import com.inventory.service.InventoryService;
import com.inventory.vo.InventoryLogVo;
import com.inventory.vo.InventoryVo;
import com.inventory.vo.SearchInvLogVo;
import com.inventory.vo.SearchInventoryVo;
import com.util.common.PagerReturnObj;
import com.util.pages.GridPages;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by van on 2018/1/14.
 */
@Controller
@RequestMapping(value = "/inventory")
public class InventoryAction {

    @Resource
    private InventoryService inventoryService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<InventoryVo>> list(SearchInventoryVo searchInventoryVo, GridPages pages) {
        return inventoryService.list(searchInventoryVo, pages);
    }


    @RequestMapping(value = "/searchLog", method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<InventoryLogVo>> searchLog(SearchInvLogVo vo, GridPages pages) {
        return inventoryService.searchLog(vo, pages);
    }


}
