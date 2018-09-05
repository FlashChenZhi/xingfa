package com.master.action;

import com.master.service.StockOutODOService;
import com.util.common.ReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 18:10 2018/7/13
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/master/StockOutODOAction")
public class StockOutODOAction {

    @Resource
    private StockOutODOService stockOutODOService;

    /*
     * @description： 查询批次号
     */
    @RequestMapping(value = "/getLotNums",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<Map<String,String>>> getLotNums(String skuCode){
        return stockOutODOService.getLotNums(skuCode);
    }

    /*
     * @description： 根据批次号和货品skucode查询在各个巷道各有几板货
     */
    @RequestMapping(value = "/findNumBySkuAndLotNum",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<Map<String,Object>>> findNumBySkuAndLotNum(String skuCode, String lotNum){
        return stockOutODOService.findNumBySkuAndLotNum(skuCode,lotNum);
    }
    /*
     * @description： 初始化车辆信息
     */
    @RequestMapping(value = "/getCar",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<Map<String,Object>>> getCar(){
        return stockOutODOService.getCar();
    }
    /*
     * @description： 初始化订单号
     */
    @RequestMapping(value = "/getOrderNo",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<String> getOrderNo(){
        return stockOutODOService.getOrderNo();
    }

    /*
     * @author：ed_chen
     * @date：2018/8/20 9:46
     * @description：生成订单
     * @param orderNo
     * @param zhantai
     * @param data
     * @return：com.util.common.ReturnObj<java.lang.String>
     */
    @RequestMapping(value = "/addOrder",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<String> addOrder(String orderNo, String zhantai, String data){
        return stockOutODOService.addOrder(orderNo,zhantai,data);
    }
}
