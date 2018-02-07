package com.wms;

import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.RetrievalOrderLine;
import com.wms.domain.Sku;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class InputSkuThread  implements Runnable{
    public static void main(String[] args)
    {

        InputSkuThread thread = new InputSkuThread();
        thread.run();
    }
    public void run(){
        while (true){
            try
            {

                File file = new File("E:/test/zhu");
                File[] filelist = file.listFiles();
                for (File f:filelist){
                    if (f.isFile() && (f.getName().endsWith(".xls")|| f.getName().endsWith(".xlsx"))){
                        Transaction.begin();
                        Session session = HibernateUtil.getCurrentSession();
                        String oldPath="E:/test/zhu/"+f.getName();
                        Workbook data = Workbook.getWorkbook(new File(oldPath));

                        Sheet sheet = data.getSheet(0);

                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                        System.out.println("当前时间：" + sdf.format(d));
                        String newPath="E:/test/fu/"+f.getName().split("\\.")[0]+"_"+sdf.format(d)+".xls";
                        List<ErrorMessage> list=new ArrayList();
                        if(StringUtils.isBlank(sheet.getCell(1, 1).getContents())){
                            copyFile(oldPath,newPath);
                            deleteFile(oldPath);
                            for (int i = 1; i < sheet.getRows(); i++)
                            {
                                if(StringUtils.isNotBlank(sheet.getCell(7, i).getContents())){
                                    Sku sku = Sku.getByCode(sheet.getCell(7, i).getContents());
                                    if(sku != null)
                                    {
                                        list.add(new ErrorMessage(sku,"商品代码重复"));
                                        continue;
                                    }
                                    sku = new Sku();
                                    session.save(sku);
                                    sku.setHuozhudaima(sheet.getCell(2, i).getContents());
                                    sku.setHuozhumingcheng(sheet.getCell(3, i).getContents());
                                    sku.setCangkudaima(sheet.getCell(4, i).getContents());
                                    sku.setShouhuoleixing(sheet.getCell(5, i).getContents());
                                    sku.setSkuCode(sheet.getCell(7, i).getContents());
                                    sku.setSkuName(sheet.getCell(8, i).getContents());
                                    sku.setDanwei(sheet.getCell(10, i).getContents());
                                    if(StringUtils.isBlank(sheet.getCell(11, i).getContents())&&StringUtils.isBlank(sheet.getCell(12, i).getContents())){
                                        sku.setCunfangquyu(0);
                                    }else if(StringUtils.equals(sheet.getCell(11, i).getContents(),"少量、专区1")||StringUtils.equals(sheet.getCell(12, i).getContents(),"少量、专区1")){
                                        sku.setCunfangquyu(1);
                                    }else if(StringUtils.equals(sheet.getCell(11, i).getContents(),"危化品 专区2")||StringUtils.equals(sheet.getCell(12, i).getContents(),"危化品 专区2")){
                                        sku.setCunfangquyu(2);
                                    }
                                }
                            }
                        }else{
                            list.add(new ErrorMessage(new Sku(),"与Sku表不匹配"));
                        }
                        if(list.isEmpty()){
                            cuoWu(f.getName().split(".")[0],sdf.format(d),list);
                        }
                        Transaction.commit();
                    }
                }

            }
            catch (IOException e)
            {
                Transaction.rollback();

                System.out.println("IO错误");
            }
            catch (BiffException e)
            {
                Transaction.rollback();

                System.out.println("Biff错误");
            }
            catch (Exception e)
            {
                Transaction.rollback();

                System.out.println(e.getMessage());
            }finally {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }
    public static void cuoWu(String wenJianMing,String shiJian,List<ErrorMessage> list) throws Exception {

        WritableWorkbook book1 = Workbook.createWorkbook(new File("E:/test/cuowu/"+wenJianMing+"_" + shiJian+ ".xls"));
        // 生成名为“sheet1”的工作表，参数0表示这是第一页
        WritableSheet sheet1 = book1.createSheet("sheet3", 0);
        Label label = new Label(0, 0, "收货单号");
        sheet1.addCell(label);
        sheet1.addCell(new Label(1, 0, "交货单号"));
        sheet1.addCell(new Label(2, 0, "货主代码"));
        sheet1.addCell(new Label(3, 0, "货主名称"));
        sheet1.addCell(new Label(4, 0, "仓库代码"));
        sheet1.addCell(new Label(5, 0, "收货类型"));
        sheet1.addCell(new Label(6, 0, "行号"));
        sheet1.addCell(new Label(7, 0, "商品代码"));
        sheet1.addCell(new Label(8, 0, "商品名称"));
        sheet1.addCell(new Label(9, 0, "订单数量"));
        sheet1.addCell(new Label(10, 0, "单位"));
        sheet1.addCell(new Label(11, 0, "存放区域"));
        sheet1.addCell(new Label(12, 0, "错误信息"));
        for (int i = 1; i <=list.size(); i++) {
            sheet1.addCell(new Label(0, i, list.get(i).sku.getShouhuodanhao()));
            sheet1.addCell(new Label(1, i, list.get(i).sku.getJiaohuodanhao()));
            sheet1.addCell(new Label(2, i, list.get(i).sku.getHuozhudaima()));
            sheet1.addCell(new Label(3, i, list.get(i).sku.getHuozhumingcheng()));
            sheet1.addCell(new Label(4, i, list.get(i).sku.getCangkudaima()));
            sheet1.addCell(new Label(5, i, list.get(i).sku.getShouhuoleixing()));
            sheet1.addCell(new Label(6, i, list.get(i).sku.getHanghao()));
            sheet1.addCell(new Label(7, i, list.get(i).sku.getSkuCode()));
            sheet1.addCell(new Label(8, i,list.get(i).sku.getSkuName()));
            sheet1.addCell(new Label(9, i, list.get(i).sku.getDingdanshuliang() + ""));
            sheet1.addCell(new Label(10, i, list.get(i).sku.getDanwei()));
            sheet1.addCell(new Label(11, i, list.get(i).sku.getCunfangquyu()+""));
            sheet1.addCell(new Label(12, i, list.get(i).message));
        }
        book1.write();
        book1.close();
    }
    public static void deleteFile(String sPath) {
        //处理文件路径,将"/"替换成计算机识别的"\\"
        sPath =sPath.replace("/",File.separator);
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile()&& file.exists()) {
            file.delete();
        }
    }

    private static class ErrorMessage {
        public ErrorMessage(Sku sku,String message){
            this.sku = sku;
            this.message = message;
        }
        private Sku sku;

        private String message;

        public Sku getSku() {
            return sku;
        }

        public void setSku(Sku sku) {
            this.sku = sku;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
