package com.master.action;

import com.master.service.PutInStorageService;
import com.master.vo.SkuVo2;
import com.util.common.BaseReturnObj;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.wms.domain.Job;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.awt.AWTCharset;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/master/putInStorage")
public class PutInStorageAction {

    @Resource
    private PutInStorageService putInStorageService;
    /**
     * 获取商品代码
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/getCommodityCode",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<SkuVo2>> getCommodityCode() throws IOException{

        return putInStorageService.getCommodityCode();
    }
    /**
     * 设定任务
     * @param tuopanhao 托盘号
     * @param zhantai 站台
     * @param commodityCode 货品代码
     * @param num 数量
     * @return "0"设定成功，"1"设定失败
     * @throws IOException
     */
    @RequestMapping(value = "/addTask",method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj addTask(String tuopanhao, String zhantai, String commodityCode,String lotNo, int num) throws IOException{
        tuopanhao = URLDecoder.decode(tuopanhao,"utf-8");
        System.out.println("托盘号："+tuopanhao+";站台："+zhantai+";货品代码："+commodityCode+";数量："+num);
        return putInStorageService.addTask(tuopanhao,zhantai,commodityCode,lotNo,num);
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
    public PagerReturnObj<List<Map<String,Object>>> findPutInStorageOrder(int current,int defaultPageSize) throws IOException{
        int startIndex = (current-1)*defaultPageSize;
        return  putInStorageService.findPutInStorageOrder(startIndex,defaultPageSize);
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
        return putInStorageService.deleteTask(selectedRowKeysString);
    }

    /**
     * 获取抽检入库信息
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/getCheckStorageData",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String,Object>> getCheckStorageData() throws IOException{

        return putInStorageService.getCheckStorageData();
    }
    /*
     * @author：ed_chen
     * @date：2018/6/9 16:18
     * @description：设定抽检入库任务
     * @param num
     * @return：com.util.common.BaseReturnObj
     */
    @RequestMapping(value = "/addCheckInStorage",method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj addCheckInStorage(int num) throws IOException{
        return putInStorageService.addCheckInStorage(num);
    }

    /*
     * @author：ed_chen
     * @date：2018/6/9 16:18
     * @description：抽检回库取消任务
     * @param num
     * @return：com.util.common.BaseReturnObj
     */
    @RequestMapping(value = "/submitCancle",method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj submitCancle() throws IOException{
        return putInStorageService.submitCancle();
    }
}
