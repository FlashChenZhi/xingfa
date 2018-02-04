package com.master.action;

import com.master.service.OrderInquiryService;
import com.master.service.PutInStorageService;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/master/OrderInquiry")
public class OrderInquiryAction {

    @Resource
    private OrderInquiryService orderInquiryService;
    private PutInStorageService putInStorageService;
    /**
     * 查询定单
     * @param orderNo 订单号（条件）
     * @param currentPage 当前页码
     * @param productId 商品代码（条件）
     * @param shipperId 货主代码（条件）
     * @param PageSize 每页条数
     * @return
     */
    @RequestMapping(value = "/findOrder",method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<Map<String,Object>>> findOrder(String orderNo,int currentPage, String productId,String shipperId, int PageSize){

        return orderInquiryService.findOrder(orderNo,currentPage,productId,shipperId,PageSize);
    }
    /**
     * 获取货主代码
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/getshipperId",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<Map<String, Object>>> getshipperId() throws IOException{
        return orderInquiryService.getshipperId();
    }
    /**
     * 获取商品代码
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/getCommodityCode",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<Map<String,Object>>> getCommodityCode() throws IOException{

        return orderInquiryService.getCommodityCode();
    }

}
