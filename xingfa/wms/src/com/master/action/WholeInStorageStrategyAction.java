package com.master.action;

import com.master.service.WholeInStorageStrategyService;
import com.util.common.ReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 10:45 2018/9/14
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/master/WholeInStorageStrategyAction")
public class WholeInStorageStrategyAction {
    @Resource
    private WholeInStorageStrategyService wholeInStorageStrategyService;
    /*
     * @author：ed_chen
     * @date：2018/8/10 11:06
     * @description：
     * @param skuCode
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @RequestMapping(value = "/getWholeInStorageStrategy",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getWholeInStorageStrategy(){
        return wholeInStorageStrategyService.getWholeInStorageStrategy();
    }

    /*
     * @author：ed_chen
     * @date：2018/8/10 11:06
     * @description：
     * @param skuCode
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @RequestMapping(value = "/updateWholeInStorageStrategy",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> updateWholeInStorageStrategy(String status){
        return wholeInStorageStrategyService.updateWholeInStorageStrategy(status);
    }
}
