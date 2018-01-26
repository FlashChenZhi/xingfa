package com.master.action;

import com.master.service.SkuService;
import com.master.vo.SkuInStrategyVo;
import com.master.vo.SkuShelfLifeVo;
import com.master.vo.SkuVo;
import com.util.common.BaseReturnObj;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.util.excel.ExcelExportUtils;
import com.util.pages.GridPages;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by van on 2018/1/16.
 */
@Controller
@RequestMapping(value = "/master/sku")
public class SkuAction {

    @Resource
    private SkuService skuService;

    @RequestMapping(value = "/getSkus", method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<SkuVo>> getSkus(String skuCode, GridPages pages) {
        return skuService.getSkus(skuCode, pages);
    }

    @RequestMapping(value = "/getShelfLife", method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<SkuShelfLifeVo>> getShelfLife(String skuCode, GridPages pages) {
        return skuService.getShelfLife(skuCode, pages);
    }

    @RequestMapping(value = "/getInStrategy", method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<SkuInStrategyVo>> getInStrategy(String skuCode, GridPages pages) {
        return skuService.getInStrategy(skuCode, pages);
    }

    @RequestMapping(value = "/exportExcelTemplate", method = RequestMethod.GET)
    public void exportExcel(HttpServletResponse response) throws IOException {
        ReturnObj<HSSFWorkbook> returnObj = skuService.generateExcelTemplateExportFile();
        if (returnObj.isSuccess())
            ExcelExportUtils.exportExcelToClient(returnObj.getRes(), "货位模板.xls", response);
    }

    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj importExcel(@RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        BaseReturnObj returnObj = skuService.doExcelImport(session, file);
        return returnObj;
    }



}
