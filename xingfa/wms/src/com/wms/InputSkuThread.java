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
    private String path = "D:/source/sku";
    private String localPath = "D:/back/sku/";
    private String errorPath = "D:/error/sku/";
    public void run(){
        while (true){
            try
            {

                File file = new File(path);
                File[] filelist = file.listFiles();
                if(filelist != null){
                    for (File f:filelist){
                        if (f.isFile() && (f.getName().endsWith(".xls")|| f.getName().endsWith(".xlsx"))){
                            Transaction.begin();
                            Session session = HibernateUtil.getCurrentSession();
                            String oldPath=path+"/" +f.getName();
                            Workbook data = Workbook.getWorkbook(new File(oldPath));

                            Sheet sheet = data.getSheet(0);

                            Date d = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                            System.out.println("当前时间：" + sdf.format(d));
                            String newPath=localPath+f.getName().split("\\.")[0]+"_"+sdf.format(d)+".xls";
                            List<ErrorMessage> list=new ArrayList();

                            for (int i = 1; i < sheet.getRows(); i++)
                            {
                                if(StringUtils.isNotBlank(sheet.getCell(0, i).getContents())){
                                    Sku sku = Sku.getByCode(sheet.getCell(0, i).getContents());
                                    if(sku != null)
                                    {
                                        list.add(new ErrorMessage(sku,"商品代码重复"));
                                        continue;
                                    }
                                    sku = new Sku();
                                    session.save(sku);
                                    sku.setSkuCode(sheet.getCell(0, i).getContents());
                                    sku.setSkuName(sheet.getCell(1, i).getContents());
                                    sku.setDanwei(sheet.getCell(2, i).getContents());

                                }
                            }
                            if(!list.isEmpty()){
                                cuoWu(oldPath,newPath,f.getName().split("\\.")[0]+"",sdf.format(d)+"",list);
                            }
                            Transaction.commit();
                            copyFile(oldPath,newPath);
                            deleteFile(oldPath);
                        }
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

                e.printStackTrace();
            }finally {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void copyFile(String oldPath, String newPath) {
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
    public void cuoWu(String oldPath,String newPath,String wenJianMing,String shiJian,List<ErrorMessage> list1) throws Exception {

        WritableWorkbook book1 = Workbook.createWorkbook(new File(errorPath +wenJianMing+"_" + shiJian+ ".xls"));
        System.out.println(wenJianMing+":"+shiJian);
        // 生成名为“sheet1”的工作表，参数0表示这是第一页
        WritableSheet sheet1 = book1.createSheet("sheet9", 0);
        Label label = new Label(0, 0, "商品代码");
        sheet1.addCell(label);
        sheet1.addCell(new Label(1, 0, "商品名称"));
        sheet1.addCell(new Label(2, 0, "单位"));
        sheet1.addCell(new Label(3, 0, "错误信息"));
        for (int i = 0; i <list1.size(); i++) {
            sheet1.addCell(new Label(0, i+1, list1.get(i).sku.getSkuCode()));
            sheet1.addCell(new Label(1, i+1, list1.get(i).sku.getSkuName()));
            sheet1.addCell(new Label(2, i+1, list1.get(i).sku.getDanwei()));
            sheet1.addCell(new Label(3, i+1, list1.get(i).message));
        }
        book1.write();
        book1.close();
    }
    public void deleteFile(String sPath) {
        //处理文件路径,将"/"替换成计算机识别的"\\"
        sPath =sPath.replace("/",File.separator);
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile()&& file.exists()) {
            file.delete();
        }
    }

    private class ErrorMessage {
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
