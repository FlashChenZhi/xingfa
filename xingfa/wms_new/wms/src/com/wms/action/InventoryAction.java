package com.wms.action;

import com.wms.service.InventoryService;
import com.wms.vo.InOutDataVo;
import com.wms.vo.InputAreaQueryVo;
import com.util.common.HttpMessage;
import com.util.excel.ExcelExportUtils;
import com.wms.vo.RetrievalOrderVo;
import com.wms.vo.SearchRetrievalVo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/10/17.
 */

@Controller
@RequestMapping("/inventory")
public class InventoryAction {
    InventoryService inventoryService = new InventoryService();

    /**
     * 入库 查询
     *
     * @param batchNo
     * @return
     */
    @RequestMapping("inputAreaQuery.do")
    @ResponseBody
    public HttpMessage inputAreaQuery(String batchNo) {
        HttpMessage httpMessage = inventoryService.inputAreaQuery(batchNo);
        return httpMessage;
    }


    /**
     * 入库 设定
     *
     * @return
     */
    @RequestMapping("inputAreaSet.do")
    @ResponseBody
    public HttpMessage inputAreaSet(InputAreaQueryVo inputAreaQueryVo, HttpSession httpSession) {
        HttpMessage httpMessage = inventoryService.inputAreaSet(inputAreaQueryVo, httpSession);
        return httpMessage;

    }

    @RequestMapping("createReceivingPlan.do")
    @ResponseBody
    public HttpMessage createReceivingPlan(InputAreaQueryVo inputAreaQueryVo, HttpSession httpSession) {
        HttpMessage httpMessage = inventoryService.createReceivingPlan(inputAreaQueryVo, httpSession);
        return httpMessage;

    }


    @RequestMapping("checkSku.do")
    @ResponseBody
    public HttpMessage checkSku(String skuCode) {

        HttpMessage httpMessage = inventoryService.checkSku(skuCode);
        return httpMessage;

    }


    /**
     * 入库单删除
     *
     * @return
     */
    @RequestMapping("closeRecvPlan.do")
    @ResponseBody
    public HttpMessage closeRecvPlan(String batchNo) {

        HttpMessage httpMessage = inventoryService.closeRecvPlan(batchNo);
        return httpMessage;

    }

    /**
     * 出库 查询
     *
     * @param searchRetrievalVo
     * @return
     */
    @RequestMapping("/getOutAreaData.do")
    @ResponseBody
    public HttpMessage getOutAreaData(SearchRetrievalVo searchRetrievalVo, int currentPage) {
        HttpMessage httpMessage = inventoryService.searchRetrieval(searchRetrievalVo, currentPage);
        return httpMessage;
    }

    /**
     * 入库查询
     *
     * @param searchRetrievalVo
     * @return
     */
    @RequestMapping("/recePlanSearch.do")
    @ResponseBody
    public HttpMessage recePlanSearch(SearchRetrievalVo searchRetrievalVo) {
        HttpMessage httpMessage = inventoryService.recePlanSearch(searchRetrievalVo);
        return httpMessage;
    }


    /**
     * 指定库存出库 设定
     *
     * @param outAreaData
     * @return
     */
    @RequestMapping("/inventoryOut.do")
    @ResponseBody
    public HttpMessage inventoryOut(String outAreaData) {
        HttpMessage httpMessage = null;
        JSONArray jsonArray = JSONArray.fromObject(outAreaData);
        List<Integer> invIds = new ArrayList<Integer>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            invIds.add(Integer.parseInt(object.get("id").toString()));
        }
        httpMessage = inventoryService.outStore(invIds);


        return httpMessage;
    }


    /**
     * 出库 设定
     *
     * @param outAreaData
     * @return
     */
    @RequestMapping("/outAreaSet.do")
    @ResponseBody
    public HttpMessage outAreaSet(String outAreaData) {

        JSONArray jsonArray = JSONArray.fromObject(outAreaData);
        List<RetrievalOrderVo> vos = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            RetrievalOrderVo inOutDataVo = (RetrievalOrderVo) JSONObject.toBean(jsonArray.getJSONObject(i), RetrievalOrderVo.class);
            vos.add(inOutDataVo);
        }
        return inventoryService.rerieval(vos);

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
