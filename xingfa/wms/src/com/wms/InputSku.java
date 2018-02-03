package com.wms;

import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Sku;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.hibernate.Session;

import java.io.File;
import java.io.IOException;

public class InputSku {
    public static void main(String[] args)
    {
        try
        {
            Transaction.begin();

            Session session = HibernateUtil.getCurrentSession();

            Workbook data = Workbook.getWorkbook(new File("D:/projects/xingfa_wcs/xingfa/doc/入库数据示例.xls"));

            Sheet sheet = data.getSheet(0);


            for (int i = 1; i < sheet.getRows(); i++)
            {
                Sku sku = Sku.getByBarcode(sheet.getCell(8, i).getContents());

                if(sku != null)
                {
                    continue;
                }

                sku = new Sku();
                session.save(sku);
                sku.setShouhuodanhao(sheet.getCell(1, i).getContents());
                sku.setJiaohuodanhao(sheet.getCell(2, i).getContents());
                sku.setHuozhudaima(sheet.getCell(3, i).getContents());
                sku.setHuozhumingcheng(sheet.getCell(4, i).getContents());
                sku.setCangkudaima(sheet.getCell(5, i).getContents());
                sku.setShouhuoleixing(sheet.getCell(6, i).getContents());
                sku.setHanghao(sheet.getCell(7, i).getContents());
                sku.setSkuCode(sheet.getCell(8, i).getContents());
                sku.setSkuName(sheet.getCell(9, i).getContents());
                try {
                    sku.setDingdanshuliang(Integer.parseInt(sheet.getCell(10, i).getContents()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sku.setDanwei(sheet.getCell(11, i).getContents());
                if(sheet.getCell(12, i).getContents().equals("")&&sheet.getCell(13, i).getContents().equals("")){
                    sku.setCunfangquyu(0);
                }else if(sheet.getCell(12, i).getContents().equals("少量、专区1")||sheet.getCell(13, i).getContents().equals("少量、专区1")){
                    sku.setCunfangquyu(1);
                }else if(sheet.getCell(12, i).getContents().equals("危化品 专区2")||sheet.getCell(13, i).getContents().equals("危化品 专区2")){
                    sku.setCunfangquyu(2);
                }
            }

            Transaction.commit();
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
        }
    }
}
