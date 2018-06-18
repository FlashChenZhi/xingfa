package com.master.action;

import com.master.service.MoveStorageService;
import com.master.service.PutInStorageService;
import com.master.vo.SkuVo2;
import com.util.common.BaseReturnObj;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 12:50 2018/6/10
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/master/moveStorageAction")
public class MoveStorageAction {

    @Resource
    private MoveStorageService moveStorageService;
    /*
     * @author：ed_chen
     * @date：2018/4/10 18:23
     * @description：初始化map
     * @param productId
     * @param tier
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @RequestMapping(value = "/getStorageLocationData",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getStorageLocationData(String productId,String tier,String selectStatus,String fromLocationNo){

        return moveStorageService.getStorageLocationData(productId,tier,selectStatus,fromLocationNo);
    }
    /*
     * @author：ed_chen
     * @date：2018/4/10 18:23
     * @description：获取库位信息
     * @param bank
     * @param bay
     * @param level
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @RequestMapping(value = "/getLocationInfo",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getLocationInfo(String bank,String bay,String level){

        return moveStorageService.getLocationInfo(bank,bay,level);
    }
    /*
     * @author：ed_chen
     * @date：2018/4/10 19:26
     * @description：获取下一位货位代码
     * @param bank
     * @param bay
     * @param level
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @RequestMapping(value = "/getNextAvailableLocation",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getNextAvailableLocation(String bank,String bay,String level){

        return moveStorageService.getNextAvailableLocation(bank,bay,level);
    }
    /*
     * @author：ed_chen
     * @date：2018/4/10 19:26
     * @description：获取里面的货位代码
     * @param bank
     * @param bay
     * @param level
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @RequestMapping(value = "/getAgoUnavailableLocation",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getAgoUnavailableLocation(String bank,String bay,String level){

        return moveStorageService.getAgoUnavailableLocation(bank,bay,level);
    }



    /*
     * @author：ed_chen
     * @date：2018/4/10 19:26
     * @description：设定出库任务
     * @param bank
     * @param bay
     * @param level
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @RequestMapping(value = "/assignsTheStorehouse",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> assignsTheStorehouse(String fromLocationNos,String toLocationNos){

        return moveStorageService.assignsTheStorehouse(fromLocationNos,toLocationNos);
    }

}
