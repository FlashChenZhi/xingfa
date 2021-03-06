package com.master.action;

import com.master.service.AssignsTheStorehouseService;
import com.master.service.SampleSurveyService;
import com.util.common.ReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 10:03 2018/6/5
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/master/SampleSurveyAction")
public class SampleSurveyAction {
    @Resource
    private SampleSurveyService sampleSurveyService;
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
    public ReturnObj<Map<String, Object>> getStorageLocationData(String productId, String tier){

        return sampleSurveyService.getStorageLocationData(productId,tier);
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

        return sampleSurveyService.getLocationInfo(bank,bay,level);
    }
  /*  *//*
     * @author：ed_chen
     * @date：2018/4/10 19:26
     * @description：获取下一位货位代码
     * @param bank
     * @param bay
     * @param level
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     *//*
    @RequestMapping(value = "/getNextAvailableLocation",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getNextAvailableLocation(String bank,String bay,String level){

        return sampleSurveyService.getNextAvailableLocation(bank,bay,level);
    }
    *//*
     * @author：ed_chen
     * @date：2018/4/10 19:26
     * @description：获取里面的货位代码
     * @param bank
     * @param bay
     * @param level
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     *//*
    @RequestMapping(value = "/getAgoUnavailableLocation",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getAgoUnavailableLocation(String bank,String bay,String level){

        return sampleSurveyService.getAgoUnavailableLocation(bank,bay,level);
    }*/

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
    public ReturnObj<Map<String, Object>> assignsTheStorehouse(String selectLocation,String level){

        return sampleSurveyService.assignsTheStorehouse(selectLocation,level);
    }
}
