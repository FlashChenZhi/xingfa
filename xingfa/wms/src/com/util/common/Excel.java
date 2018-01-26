package com.util.common;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

public class Excel {
    public static void ExportExcel( String[] titles, List list, OutputStream outputStream) {

        try {
            // 创建一个workbook 对应一个excel应用文件
            XSSFWorkbook workBook = new XSSFWorkbook();
            // 在workbook中添加一个sheet,对应Excel文件中的sheet
            //Sheet名称，可以自定义中文名称
            XSSFSheet sheet = workBook.createSheet("Sheet1");
            // 构建表头
            XSSFRow headRow = sheet.createRow(0);
            // 创建单元格样式
            XSSFCellStyle cellStyle = workBook.createCellStyle();
            // 设置单元格的背景颜色为淡蓝色
            cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
            cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
            // 设置单元格居中对齐
            cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            // 设置单元格垂直居中对齐
            cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
            // 创建单元格内容显示不下时自动换行
            cellStyle.setWrapText(true);
            // 设置单元格字体样式
            XSSFFont font = workBook.createFont();
            // 设置字体加粗
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
            font.setFontName("宋体");
            font.setFontHeight((short) 200);
            cellStyle.setFont(font);
            // 设置单元格边框为细线条
            cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
            XSSFCell cell;
            // 输出标题
            for (int i = 0; i < titles.length; i++) {
                cell = headRow.createCell(i);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(titles[i]);
            }
            // 构建表体数据
            for (int i = 0; i < list.size(); i++) {
                XSSFRow bodyRow = sheet.createRow(i + 1);
                Object object = list.get(i);
                Field[] fields = object.getClass().getDeclaredFields();
                for (int j = 0; j < fields.length; j++) {
                    cell = bodyRow.createCell(j);
                    cell.setCellStyle(cellStyle);
                    Field field = fields[j];
                    field.setAccessible(true);
                    Object value = field.get(object);
                    if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof String) {
                        cell.setCellValue((String) value);
                    }
                }
            }
            workBook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}