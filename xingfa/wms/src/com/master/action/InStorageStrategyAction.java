package com.master.action;

import com.master.service.InStorageStrategyService;
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
 * @Date: Create in 15:25 2018/9/12
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/master/InStorageStrategyAction")
public class InStorageStrategyAction {

    @Resource
    private InStorageStrategyService inStorageStrategyService;
    /**
     * 获取商品代码
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/getCommodityCode",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<SkuVo2>> getCommodityCode() throws IOException{

        return inStorageStrategyService.getCommodityCode();
    }
    /*
     * @author：ed_chen
     * @date：2018/9/13 9:51
     * @description：设定任务
     * @param commodityCode
     * @param lotNo
     * @param level
     * @param bay
     * @return：com.util.common.BaseReturnObj
     */
    @RequestMapping(value = "/addTask",method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj addTask( String commodityCode, String lotNo, int level,int bay) throws IOException{
        return inStorageStrategyService.addTask(commodityCode,lotNo,level,bay);
    }
    /*
     * @author：ed_chen
     * @date：2018/3/4 17:48
     * @description：查询入库设定任务记录
     * @param
     * @return：com.util.common.BaseReturnObj
     */
    @RequestMapping(value = "/findPutInStorageOrder",method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<Map<String,Object>>> findPutInStorageOrder(int current, int defaultPageSize,
                                                 String skuCode,String lotNum,String bay,String level) throws IOException{
        int startIndex = (current-1)*defaultPageSize;
        return  inStorageStrategyService.findPutInStorageOrder(startIndex,defaultPageSize,skuCode,lotNum,bay,level);
    }
    /*
     * @author：ed_chen
     * @date：2018/3/10 18:34
     * @description： 删除入库任务
     * @param selectedRowKeysString
     * @return：com.util.common.BaseReturnObj
     */
    @RequestMapping(value = "/deleteTask",method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj deleteTask(String selectedRowKeysString) throws IOException{
        System.out.println(selectedRowKeysString);
        return inStorageStrategyService.deleteTask(selectedRowKeysString);
    }

    @RequestMapping(value = "/delAndAddTask",method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj delAndAddTask(String commodityCode, String lotNo, int level,int bay,int delId) throws IOException{

        return inStorageStrategyService.delAndAddTask(commodityCode,lotNo,level,bay,delId);
    }

}
