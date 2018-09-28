package com.master.service;

import com.asrs.business.consts.SkuType;
import com.util.common.LogMessage;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: ed_chen
 * @Date: Create in 14:12 2018/3/31
 * @Description:
 * @Modified By:
 */
@Service
public class FindDayNeatenService {
    /*
     * @author：ed_chen
     * @date：2018/3/31 14:37
     * @description：获取商品代码
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    public ReturnObj<List<Map<String, String>>> getSkuCode(){
        ReturnObj<List<Map<String, String>>> s = new ReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("select skuCode,skuName from Sku");
            List<Object[]> retList = query.list();
            List<Map<String,String>> mapList = new ArrayList<>();
            for (Object[] objects: retList) {
                Map<String, String> map = new HashMap();
                map.put("skuCode", objects[0].toString() );
                map.put("skuName",objects[1].toString() );
                mapList.add(map);
            }
            s.setSuccess(true);
            s.setRes(mapList);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            s.setSuccess(false);
            s.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return s;
    }
    /*
     * @author：ed_chen
     * @date：2018/3/31 14:39
     * @description：查找出入库详情
     * @param startIndex
     * @param defaultPageSize
     * @param productId
     * @param beginDate
     * @param endDate
     * @return：com.util.common.PagerReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     */
    public PagerReturnObj<List<Map<String,Object>>> findOutOrInWarehouse(int startIndex, int defaultPageSize,String productId,
                                                                         String dayDate, String monthDate,String type){
        PagerReturnObj<List<Map<String,Object>>> returnObj = new PagerReturnObj<List<Map<String,Object>>>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            String date ="";
            String table="";
            if("0".equals(type)){
                //日结
                date=dayDate;
                table="DayNeaten";
            }else if("1".equals(type)){
                //月结
                date=monthDate;
                table="MonthNeaten";
            }

            Query query =session.createQuery("select d.skuCode as skuCode,d.skuName as skuName,d.lotNum as lotNum," +
                    "d.benginningInventory as benginningInventory,d.inStorage as inStorage,d.outStorage as " +
                    "outStorage,d.carryover as carryover,d.date as date,s.skuType as skuType " +
                    "from "+table+" d,Sku s where d.skuCode=s.skuCode "
                    +(StringUtils.isNotBlank(date)?" and d.date=:date ":"")+""
                    +(StringUtils.isNotBlank(productId)?" and d.skuCode=:skuCode ":"")+" order by date desc,skuType asc").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            if(StringUtils.isNotBlank(date)){
                query.setParameter("date", date);
            }
            if(StringUtils.isNotBlank(productId)){
                query.setParameter("skuCode", productId);
            }
            query.setFirstResult(startIndex);
            query.setMaxResults(defaultPageSize);

            Query query1 =session.createQuery("select count(*) from "+table+" d where 1=1 "
                    +(StringUtils.isNotBlank(date)?" and d.date=:date ":"")+""
                    +(StringUtils.isNotBlank(productId)?" and d.skuCode=:skuCode ":""));
            if(StringUtils.isNotBlank(date)){
                query1.setParameter("date", date);
            }
            if(StringUtils.isNotBlank(productId)){
                query1.setParameter("skuCode", productId);
            }
            List<Map<String,Object>> jobList = query.list();
            long count = (long)query1.uniqueResult();
            for(Map<String,Object> map : jobList){
                if(map.get("skuType")!=null && StringUtils.isNotBlank(map.get("skuType").toString())){
                    map.put("skuType", SkuType.map.get(map.get("skuType").toString()));
                }else{
                    map.put("skuType", "无分类");
                }
            }
            returnObj.setSuccess(true);
            returnObj.setRes(jobList);
            returnObj.setCount(count);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return returnObj;
    }

    /*
     * @author：ed_chen
     * @date：2018/5/4 19:18
     * @description：导出报表
     * @param beginDate
     * @param endDate
     * @return：com.util.common.PagerReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     */
    public void exportReport(String date,String type,HttpServletResponse response,HttpServletRequest request){

        OutputStream ouputStream = null;
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();

            String fileName="";
            String filename1 = "";
            String table="";
            if("0".equals(type)){
                //日结
                fileName="库存日结表";
                filename1 = "库存日结表";
                table="DayNeaten";
            }else if("1".equals(type)){
                //月结
                fileName="库存月结表";
                filename1 = "库存月结表";
                table="MonthNeaten";
            }

            String userAgent = request.getHeader("User-Agent");
            //针对IE或者以IE为内核的浏览器：
            if (userAgent.contains("MSIE")||userAgent.contains("Trident")) {
                try {
                    filename1 = URLEncoder.encode(filename1, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                //非IE浏览器的处理：
                try {
                    filename1 = new String(filename1.getBytes("UTF-8"),"ISO-8859-1");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            response.setContentType("application/msexcel;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ filename1+".xls");
            response.setHeader("Content-Type", "application/force-download");

            ouputStream = response.getOutputStream();

            List<List<Map<String,Object>>> dataList= getExclContent(date,table);

            //jxl导出报表方法
            exportExcel(dataList, ouputStream,date,fileName);

            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            Transaction.rollback();
        }finally {
            if(ouputStream!=null){
                try {
                    ouputStream.close();
                    ouputStream.flush();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }

        }
       // return returnObj;
    }

    /**
     * 将数据写入到excel中
     *            创建Excel的输出流
     * @param datalines
     *            要插入的数据源
     *            excel表名称
     */
    public void exportExcel( List<List<Map<String,Object>>> datalist, OutputStream os,
                             String date,String fileName ) {

        WritableWorkbook workbook = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //给单元格设置一个统一的格式
            WritableCellFormat wcformat = getExcelCellFormat();
            //创建一个excel
            workbook = Workbook.createWorkbook(os);
            //workbook = Workbook.createWorkbook(new File(path+fileName+hz));
            Map<String,String> mapTitle = new HashMap<>();
            mapTitle.put("title0", "产成品");
            mapTitle.put("title1", "原材料");
            mapTitle.put("title2", "包装袋");

            WritableSheet sheet=null;
            //设置字体为Arial，30号，加粗
            WritableFont font = new  WritableFont(WritableFont.ARIAL, 11 ,WritableFont.NO_BOLD);
            WritableCellFormat format = new  WritableCellFormat(font);
            WritableFont font1 = new  WritableFont(WritableFont.ARIAL, 11 ,WritableFont.BOLD);
            WritableCellFormat format1 = new  WritableCellFormat(font1);
            WritableCellFormat format2 = new  WritableCellFormat(font);
            //设置上下居中对齐
            format1.setVerticalAlignment(VerticalAlignment.CENTRE);
            //设置左右居中对齐
            format1.setAlignment(Alignment.CENTRE);
            format2.setVerticalAlignment(VerticalAlignment.CENTRE);
            for(int i=0;i<datalist.size();i++){
                //创建一个sheet
                sheet = workbook.createSheet(mapTitle.get("title"+i), i);
                //设置单元格宽度
                for(int j = 0;j<7;j++){
                    if(j == 0){
                        sheet.setColumnView(j, 10);
                    }else if(j == 2){
                        sheet.setColumnView(j, 33);
                    }else if(j == 5){
                        sheet.setColumnView(j, 22);
                    }else{
                        sheet.setColumnView(j, 20);
                    }
                }
                sheet.setRowView(0, 600);
                sheet.setRowView(1, 600);
                // 合并单元格    (开始列, 开始行, 结束列, 结束行)
                sheet.mergeCells(0, 1, 2, 1);
                // 合并单元格  (开始列, 开始行, 结束列, 结束行)
                sheet.mergeCells(0, 0, 7, 0);
                //添加第二行标题
                sheet.addCell(new Label(0, 1, "时间："+date,format2));

                //添加第一行标题
                sheet.addCell(new Label(0, 0, mapTitle.get("title"+i),format1));

                sheet.addCell(new Label(0, 2, "行号"));
                sheet.addCell(new Label(1, 2, "商品代码"));
                sheet.addCell(new Label(2, 2, "商品名称"));
                sheet.addCell(new Label(3, 2, "商品类型"));
                sheet.addCell(new Label(4, 2, "批次"));
                sheet.addCell(new Label(5, 2, "期初"));
                sheet.addCell(new Label(6, 2, "入库"));
                sheet.addCell(new Label(7, 2, "出库"));
                sheet.addCell(new Label(8, 2, "结余"));
                String skuCode="";

                List<Map<String,Object>> datalist2=datalist.get(i);
                for(int j =0;j<datalist2.size();j++){

                    Map<String,Object> dataMap = datalist2.get(j);
                    int k =j+1;
                    sheet.addCell(new Label(0, j+3, k+"",format));
                    if(!skuCode.equals((String)dataMap.get("skuCode"))){
                        sheet.addCell(new Label(1, j+3, (String)dataMap.get("skuCode"),format));
                        sheet.addCell(new Label(2, j+3, (String)dataMap.get("skuName"),format));
                        skuCode=(String)dataMap.get("skuCode");
                    }
                    sheet.addCell(new Label(3, j+3, (String)dataMap.get("skuType"),format));
                    sheet.addCell(new Label(4, j+3, (String)dataMap.get("lotNum"),format));
                    sheet.addCell(new Label(5, j+3, ""+dataMap.get("benginningInventory"),format));
                    sheet.addCell(new Label(6, j+3, ""+dataMap.get("inStorage"),format));
                    sheet.addCell(new Label(7, j+3, ""+dataMap.get("outStorage"),format));
                    sheet.addCell(new Label(8, j+3, ""+dataMap.get("carryover"),format));
                }
            }


            workbook.write();
        } catch (RowsExceededException e) {
            //LogUtil.error(e.getMessage(),e);
            e.printStackTrace();
        } catch (WriteException e) {
            //LogUtil.error(e.getMessage(),e);
            e.printStackTrace();
        } catch (IOException e) {
            //LogUtil.error(e.getMessage(),e);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            //LogUtil.error(e.getMessage(),e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            //LogUtil.error(e.getMessage(),e);
            e.printStackTrace();
        } catch (Exception e) {
            //LogUtil.error(e.getMessage(),e);
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                    System.out.println("11");
                } catch (WriteException e) {
                    //LogUtil.error(e.getMessage(),e);
                    e.printStackTrace();
                } catch (IOException e) {
                    //LogUtil.error(e.getMessage(),e);
                    e.printStackTrace();
                }
            }

        }
    }

    public static WritableCellFormat getExcelCellFormat() throws Exception {
        WritableCellFormat wcformat = new WritableCellFormat();
        wcformat.setBorder(Border.ALL, BorderLineStyle.THIN);
        return wcformat;
    }

    public List<List<Map<String,Object>>> getExclContent(String date, String table){
        Session session = HibernateUtil.getCurrentSession();
        //查询sheet1，库存汇总表,当前时间
        Query query =session.createQuery("select d.skuCode as skuCode,d.skuName as skuName,d.lotNum as lotNum," +
                "d.benginningInventory as benginningInventory,d.inStorage as inStorage,d.outStorage as " +
                "outStorage,d.carryover as carryover,d.date as date,s.skuType as skuType from " +
                table+" d,Sku s where d.skuCode =s.skuCode and s.skuType=:skuType "
                +(StringUtils.isNotBlank(date)?" and d.date=:date ":"")+" order by skuCode asc").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        if(StringUtils.isNotBlank(date)){
            query.setParameter("date", date);
        }
        query.setParameter("skuType", SkuType.FINISHED_PRODUCT);
        List<Map<String,Object>> list1 = query.list();


        query.setParameter("skuType", SkuType.RAW_MATERIAL);
        List<Map<String,Object>> list2 = query.list();

        query.setParameter("skuType", SkuType.PACKING_BAG);
        List<Map<String,Object>> list3 = query.list();

        List<List<Map<String,Object>>> lists = new ArrayList<>();

        lists.add(list1);
        lists.add(list2);
        lists.add(list3);
        for(List<Map<String,Object>> listt :lists){
            for(Map<String,Object> map : listt){
                if(map.get("skuType")!=null && StringUtils.isNotBlank(map.get("skuType").toString())){
                    map.put("skuType", SkuType.map.get(map.get("skuType").toString()));
                }else{
                    map.put("skuType", "无分类");
                }
            }
        }
        return lists;
    }


}
