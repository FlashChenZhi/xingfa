package com.wms.action;

import com.wms.service.InventoryService;
import com.wms.vo.OutArea;
import com.util.common.HttpMessage;
import com.util.excel.ExcelExportUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;


/**
 * Created by Administrator on 2016/10/17.
 */

@Controller
@RequestMapping("/inventory")
public class InventoryAction {
    InventoryService inventoryService = new InventoryService();

    /**
     * 入库 设定
     *
     * @param barcode
     * @param skuCode
     * @param qty
     * @param station
     * @return
     */
    @RequestMapping("inputAreaSet.do")
    @ResponseBody
    public HttpMessage inputArea(String barcode, String skuCode, int qty, String station,HttpSession httpSession) {
        HttpMessage httpMessage = inventoryService.saveInputArea(barcode, skuCode, qty, station,httpSession);
        return httpMessage;

    }

    /**
     * 出库 查询
     *
     * @param goodsNo
     * @param current
     * @return
     */
    @RequestMapping("/getOutAreaData.do")
    @ResponseBody
    public HttpMessage getOutAreaData(String goodsNo, int current) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Session session = HibernateUtil.getCurrentSession();
            Transaction.begin();
            Query query;
            Map map = new HashMap();
            if (StringUtils.isNotBlank(goodsNo)) {
                Long total = (Long) session.createQuery("select count(distinct skuCode) from Inventory where skuCode=:goodsNo").setParameter("goodsNo", goodsNo.trim()).uniqueResult();
                map.put("total", total);
                query = session.createQuery("select new com.wms.vo.OutArea(skuCode,1,count(id)) from Inventory where skuCode=:goodsNo group by skuCode order by skuCode").setParameter("goodsNo", goodsNo.trim());
            } else {
                Long total = (Long) session.createQuery("select count(distinct skuCode) from Inventory").uniqueResult();
                map.put("total", total);
                query = session.createQuery("select new com.wms.vo.OutArea(skuCode,1,count(id))  from Inventory  group by skuCode order by skuCode");
            }
            query.setFirstResult((current - 1) * 10);
            query.setMaxResults(10);
            List<OutArea> list = query.list();
            map.put("data", list);
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg(map);
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了");
            e.printStackTrace();
        }
        return httpMessage;

    }

    /**
     * 出库 设定
     *
     * @param location
     * @param station
     * @return
     */
    @RequestMapping("/outAreaSet.do")
    @ResponseBody
    public HttpMessage outAreaSet(String location, String station) {
        HttpMessage httpMessage = null;
        JSONArray jsonArray = JSONArray.fromObject(location);
        for (int i = 0; i < jsonArray.size(); i++) {
            OutArea outArea = (OutArea) JSONObject.toBean(jsonArray.getJSONObject(i), OutArea.class);
            String skuCode = outArea.getGoodsNo();
            int outNum = outArea.getOutNum();
            for (int j = 1; j <= outNum; j++) {
                httpMessage = inventoryService.outAreaSet(skuCode, station, j);
                if (!httpMessage.isSuccess()) {
                    break;
                }
            }
        }

        return httpMessage;
    }

    /**
     * 库存查询 查询
     *
     * @param currentPage
     * @param locationNo
     * @param barcode
     * @param skuCode
     * @return
     */
    @RequestMapping("inventoryQuery.do")
    @ResponseBody
    public HttpMessage getInventorys(int currentPage, String locationNo, String barcode, String skuCode) {
        HttpMessage httpMessage = inventoryService.getInventorys(currentPage, locationNo, barcode, skuCode);
        return httpMessage;

    }


    /**
     * 库存查询 导出excel
     *
     * @param locationNo
     * @param barcode
     * @param skuCode
     * @param httpServletResponse
     * @throws IOException
     */
    @RequestMapping("exportExcel.do")
    @ResponseBody
    public void exportExcel(String locationNo, String barcode, String skuCode, HttpServletResponse httpServletResponse) throws IOException {
        HttpMessage returnObj = inventoryService.generateExcelExportFile(locationNo, barcode, skuCode);
        if (returnObj.isSuccess())
            ExcelExportUtils.exportExcelToClient((HSSFWorkbook) (returnObj.getMsg()), "库存一览.xls", httpServletResponse);

    }


}
