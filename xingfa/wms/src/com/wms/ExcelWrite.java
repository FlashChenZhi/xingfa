package com.wms;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import com.wms.domain.Sku;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelWrite {
    private static HSSFWorkbook workbook = null;

    /**
     * 判断文件是否存在.
     * @param fileDir  文件路径
     * @return
     */
    public static boolean fileExist(String fileDir){
        boolean flag = false;
        File file = new File(fileDir);
        flag = file.exists();
        return flag;
    }
    /**
     * 判断文件的sheet是否存在.
     * @param fileDir   文件路径
     * @param sheetName  表格索引名
     * @return
     */
    public static boolean sheetExist(String fileDir,String sheetName) throws Exception{
        boolean flag = false;
        File file = new File(fileDir);
        if(file.exists()){    //文件存在
            //创建workbook
            try {
                workbook = new HSSFWorkbook(new FileInputStream(file));
                //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
                HSSFSheet sheet = workbook.getSheet(sheetName);
                if(sheet!=null)
                    flag = true;
            } catch (Exception e) {
                throw e;
            }

        }else{    //文件不存在
            flag = false;
        }
        return flag;
    }
    /**
     * 创建新excel.
     * @param fileDir  excel的路径
     * @param sheetName 要创建的表格索引
     * @param titleRow excel的第一行即表格头
     */
    public static void createExcel(String fileDir,String sheetName,String titleRow[]) throws Exception{
        //创建workbook
        workbook = new HSSFWorkbook();
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        HSSFSheet sheet1 = workbook.createSheet(sheetName);
        //新建文件
        FileOutputStream out = null;
        try {
            //添加表头
            HSSFRow row = workbook.getSheet(sheetName).createRow(0);    //创建第一行
            for(short i = 0;i < titleRow.length;i++){
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(titleRow[i]);
            }
            out = new FileOutputStream(fileDir);
            workbook.write(out);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 删除文件.
     * @param fileDir  文件路径
     */
    public static boolean deleteExcel(String fileDir) {
        boolean flag = false;
        File file = new File(fileDir);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                file.delete();
                flag = true;
            }
        }
        return flag;
    }
    public static void writeToExcel(String fileDir,String sheetName,List<Map> mapList) throws Exception{
        //创建workbook
        File file = new File(fileDir);
        try {
            workbook = new HSSFWorkbook(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //流
        FileOutputStream out = null;
        HSSFSheet sheet = workbook.getSheet(sheetName);
        // 获取表格的总行数
        // int rowCount = sheet.getLastRowNum() + 1; // 需要加一
        // 获取表头的列数
        int columnCount = sheet.getRow(0).getLastCellNum()+1;
        try {
            // 获得表头行对象
            HSSFRow titleRow = sheet.getRow(0);
            if(titleRow!=null){
                for(int rowId=0;rowId<mapList.size();rowId++){
                    Map map = mapList.get(rowId);
                    HSSFRow newRow=sheet.createRow(rowId+1);
                    for (short columnIndex = 0; columnIndex < columnCount; columnIndex++) {  //遍历表头
                        String mapKey = titleRow.getCell(columnIndex).toString().trim().toString().trim();
                        HSSFCell cell = newRow.createCell(columnIndex);
                        cell.setCellValue(map.get(mapKey)==null ? null : map.get(mapKey).toString());
                    }
                }
            }

            out = new FileOutputStream(fileDir);
            workbook.write(out);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public static void main(String[] args) {
//            System.out.println( ExcelWrite.fileExist("E:/test/cuowu/test.xls"));
//            //创建文件
//            String title[] = {"id","name","password"};
//            try {
//                ExcelWrite.createExcel("E:/test/cuowu/test.xls","sheet1",title);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            List<Map> list=new ArrayList();
//            Map<String,String> map=new HashMap();
//            map.put("id", "111");
//            map.put("name", "张三");
//            map.put("password", "111！@#");
//
//            Map<String,String> map2=new HashMap();
//            map2.put("id", "222");
//            map2.put("name", "李四");
//            map2.put("password", "222！@#");
//            list.add(map);
//            list.add(map2);
//            try {
//                ExcelWrite.writeToExcel("E:/test/cuowu/test.xls","sheet1",list);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            String sql="select aaa,bbb,ccc from dddd";
//            String sqlForSplit = sql.substring(sql.toLowerCase().indexOf("select")+6,sql.toLowerCase().indexOf("from")).trim();
//            String sqlRemoveFrom=sql.substring(sql.toLowerCase().indexOf("from")+5).trim();
//            System.out.println(sqlRemoveFrom);
//            String tableName=sqlRemoveFrom.indexOf(" ")==-1 ?  sqlRemoveFrom : sqlRemoveFrom.substring(0,sqlRemoveFrom.indexOf(" "));
//            System.out.println(tableName);
//    }
//public static void main(String[] args) throws WriteException, IOException {
//    // 打开文件
//                WritableWorkbook book = Workbook.createWorkbook(new File("E:/test/cuowu/test.xls"));
//               // 生成名为“sheet1”的工作表，参数0表示这是第一页
//               WritableSheet sheet = book.createSheet("sheet1", 0);
//
//               /* 第一行 */
//                // 在Label对象的构造子中指名单元格位置是第一列第一行(0,0),单元格内容为string
//             Label label = new Label(0, 0, "张富昌");
//                // 将定义好的单元格添加到工作表中
//                sheet.addCell(label);
//                 // 生成一个保存数字的单元格,单元格位置是第二列，第一行，单元格的内容为1234.5
//                 Number number = new Number(1, 0, 1234.5);
//                 sheet.addCell(number);
//                 // 生成一个保存日期的单元格，单元格位置是第三列，第一行，单元格的内容为当前日期
//                 DateTime dtime = new DateTime(2, 0, new Date());
//                 sheet.addCell(dtime);
//    // 将定义好的单元格添加到工作表中
//    sheet.addCell(new Label(3, 0, "张富昌1"));
//
//                 /* 第二行 */
//                 // 在Label对象的构造子中指名单元格位置是第一列第一行(0,0),单元格内容为string
//                 label = new Label(0, 1, "zfc");
//                 // 将定义好的单元格添加到工作表中
//                 sheet.addCell(label);
//                 // 生成一个保存数字的单元格,单元格位置是第二列，第一行，单元格的内容为1234.5
//                 number = new Number(1, 1, 1234);
//                 sheet.addCell(number);
//                 // 生成一个保存日期的单元格，单元格位置是第三列，第一行，单元格的内容为当前日期
//                 dtime = new DateTime(2, 1, new Date());
//                 sheet.addCell(dtime);
//
//                 // 写入数据并关闭文件
//                 book.write();
//                 book.close();
//             }
public static void main(String[] args) {

    List<Map<String,Sku>> list=new ArrayList();
    Sku sku=new Sku();
    sku.setDanwei("fdas");
    for(int i=0;i<20;i++){

        Map map=new HashMap();
        map.put("Sku",sku);
        map.put("cuoWuHao",1);
        list.add(map);
    }

    System.out.println();
        System.out.println(list.get(1).get("Sku").getDanwei());



}

}

