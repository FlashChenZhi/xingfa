package com.master.action;

import com.master.service.FindOutOrInWarehouseService;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 14:12 2018/3/31
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/master/FindOutOrInWarehouseAction")
public class FindOutOrInWarehouseAction {
    @Resource
    private FindOutOrInWarehouseService findOutOrInWarehouseService;

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
        return findOutOrInWarehouseService.getSkuCode();
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
    public PagerReturnObj<List<Map<String,Object>>> findOutOrInWarehouse(int current, int defaultPageSize,
                                                                   String productId, String beginDate, String endDate){
        int startIndex = (current-1) * defaultPageSize;
        return findOutOrInWarehouseService.findOutOrInWarehouse(startIndex,defaultPageSize,productId,beginDate,endDate);
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
    public void exportReport( String beginDate, String endDate,HttpServletResponse response){



        findOutOrInWarehouseService.exportReport(beginDate, endDate,response);
    }



}
