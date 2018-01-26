package com.order.action;

import com.inventory.vo.RetrievalOrderIds;
import com.order.service.OrderService;
import com.order.vo.OrderDetailVo;
import com.order.vo.OrderVo;
import com.order.vo.SearchOrderVo;
import com.util.common.BaseReturnObj;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.util.pages.GridPages;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by van on 2018/1/13.
 */
@Controller
@RequestMapping(value = "/order")
public class OrderAction {

    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<OrderVo>> list(SearchOrderVo searchOrderVo, GridPages pages) {
        return orderService.list(searchOrderVo, pages);
    }

    @RequestMapping(value = "/searchDetail", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<OrderDetailVo>> searchDetail(Integer orderId, String keyword) {
        return orderService.searchDetail(orderId, keyword);
    }

    @RequestMapping(value = "/retrieval", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj retrieval(@RequestBody List<RetrievalOrderIds> orderIds) {
        return orderService.retrieval(orderIds);
    }

    @RequestMapping(value = "/close", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj close(String orderNo) {
        return orderService.close(orderNo);
    }


}
