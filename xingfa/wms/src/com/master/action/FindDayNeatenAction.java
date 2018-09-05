package com.master.action;

import com.master.service.FindDayNeatenService;
import com.master.service.FindOutOrInWarehouseService;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 14:12 2018/3/31
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/master/FindDayNeatenAction")
public class FindDayNeatenAction {
    @Resource
    private FindDayNeatenService findDayNeatenService;

    /*
     * @author：ed_chen
     * @date：2018/3/31 14:35
     * @description： 初始化页面商品代码
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    @RequestMapping(value = "/getSkuCode",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<Map<String,String>>> getSkuCode(){
        return findDayNeatenService.getSkuCode();
    }
    /*
     * @author：ed_chen
     * @date：2018/3/31 14:38
     * @description： 查找出入库信息
     * @param current
     * @param defaultPageSize
     * @param productId
     * @param beginDate
     * @param endDate
     * @return：com.util.common.PagerReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     */
    @RequestMapping(value = "/findOutOrInWarehouse",method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<Map<String,Object>>> findOutOrInWarehouse(int current, int defaultPageSize,String productId,
                                                                         String dayDate, String monthDate,String type){
        int startIndex = (current-1) * defaultPageSize;
        return findDayNeatenService.findOutOrInWarehouse(startIndex,defaultPageSize,productId,dayDate,monthDate,type);
    }

    /*
     * @author：ed_chen
     * @date：2018/5/4 19:17
     * @description： 导出报表
     * @param beginDate
     * @param endDate
     * @return：com.util.common.PagerReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     */
    @RequestMapping(value = "/exportReport",method = RequestMethod.GET)
    public void exportReport(  String date,String type,HttpServletResponse response,HttpServletRequest request){

        findDayNeatenService.exportReport(date,type,response,request);
    }



}
