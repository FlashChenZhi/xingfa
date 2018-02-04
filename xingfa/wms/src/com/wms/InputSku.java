package com.wms;

import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Sku;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.lang.StringUtils;
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

            Workbook data = Workbook.getWorkbook(new File("E:/test/zhu/test1.xls"));

            Sheet sheet = data.getSheet(0);
            for (int i = 1; i < sheet.getRows(); i++)
            {
                if(StringUtils.isNotBlank(sheet.getCell(7, i).getContents())){
                    Sku sku = Sku.getByCode(sheet.getCell(7, i).getContents());

                    if(sku != null)
                    {
                        continue;
                    }

                    sku = new Sku();
                    session.save(sku);
                    sku.setShouhuodanhao(sheet.getCell(0, i).getContents());
                    sku.setJiaohuodanhao(sheet.getCell(1, i).getContents());
                    sku.setHuozhudaima(sheet.getCell(2, i).getContents());
                    sku.setHuozhumingcheng(sheet.getCell(3, i).getContents());
                    sku.setCangkudaima(sheet.getCell(4, i).getContents());
                    sku.setShouhuoleixing(sheet.getCell(5, i).getContents());
                    sku.setHanghao(sheet.getCell(6, i).getContents());
                    sku.setSkuCode(sheet.getCell(7, i).getContents());
                    sku.setSkuName(sheet.getCell(8, i).getContents());
                    try {
                        if(StringUtils.isNotBlank(sheet.getCell(9, i).getContents())){
                            sku.setDingdanshuliang(Integer.parseInt(sheet.getCell(9, i).getContents()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
