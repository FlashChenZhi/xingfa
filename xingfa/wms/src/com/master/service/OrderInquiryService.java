package com.master.service;

import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderInquiryService {
    /**
     * 查询定单
     * @param orderNo 订单号（条件）
     * @param currentPage 当前页码
     * @param productId 商品代码（条件）
     * @param shipperId 货主代码（条件）
     * @param PageSize 每页条数
     * @return
     */
    public PagerReturnObj<List<Map<String,Object>>> findOrder(String orderNo, int currentPage, String productId, String shipperId, int PageSize){

        System.out.println(orderNo+";"+currentPage+";"+productId+";"+shipperId+";"+PageSize);

        PagerReturnObj<List<Map<String,Object>>> s = new PagerReturnObj<List<Map<String,Object>>>();
        List<Map<String,Object>> maplist = new ArrayList<Map<String,Object>>();
        for (int i = 0; i <10 ; i++) {
            Map<String,Object> map =  new HashMap<String,Object>();
            map.put("orderNo",i);
            map.put("productId","product"+i);
            map.put("productName","你好"+i);
            map.put("shipperId","shipper"+i);
            map.put("shipperName","哈哈"+i);
            map.put("productNum",10);
            map.put("allcatedNum",3);
            maplist.add(map);
        }
        s.setSuccess(true);
        s.setRes(maplist);
        s.setCount(10);
        return s;
    }

    /**
     * 获取货主代码
     * @return
     * @throws IOException
     */
    public ReturnObj<List<Map<String,Object>>> getshipperId() throws IOException{
        ReturnObj<List<Map<String,Object>>> s = new ReturnObj<List<Map<String,Object>>>();
        System.out.println("进入获取货主代码方法！");
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        for (int i = 0; i <8 ; i++) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id",i);
            map.put("name","货主代码"+i);
            mapList.add(map);
        }
        s.setSuccess(true);
        s.setRes(mapList);
        return s;
    }
    /**
     * 获取商品代码
     * @return
     * @throws IOException
     */
    public ReturnObj<List<Map<String,Object>>> getCommodityCode() throws IOException{
        ReturnObj<List<Map<String,Object>>> s = new ReturnObj<List<Map<String,Object>>>();
        System.out.println("进入获取商品代码方法！");

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
}
