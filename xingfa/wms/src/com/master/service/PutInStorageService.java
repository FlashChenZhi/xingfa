package com.master.service;

import com.util.common.PagerReturnObj;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PutInStorageService {
    /**
     * 获取商品代码
     * @return
     * @throws IOException
     */
    public PagerReturnObj<List<Map<String,Object>>> getCommodityCode() throws IOException{
        System.out.println("进入获取商品代码方法！");
        PagerReturnObj<List<Map<String,Object>>> s = new PagerReturnObj<List<Map<String,Object>>>();
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        for (int i = 0; i <10 ; i++) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id",i);
            map.put("name","商品代码"+i);
            mapList.add(map);
        }
        s.setSuccess(true);
        s.setRes(mapList);
        return s;
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
    public String addTask(String tuopanhao,String zhantai,String commodityCode,int num) throws IOException{

        return "0";
    }
}
