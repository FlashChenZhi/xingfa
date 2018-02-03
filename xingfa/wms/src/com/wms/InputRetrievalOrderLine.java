package com.wms;

import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.RetrievalOrderLine;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.hibernate.Session;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: 张铭
 * Date: 2011-11-4
 * Time: 19:39:39
 * To change this template use File | Settings | File Templates.
 */
public class InputRetrievalOrderLine
{
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
                RetrievalOrderLine rol = RetrievalOrderLine.getByBarcode(sheet.getCell(2, i).getContents(),sheet.getCell(7, i).getContents());

                if(rol != null)
                {
                    continue;
                }

                rol = new RetrievalOrderLine();
                session.save(rol);
                rol.setShouhuodanhao(sheet.getCell(1, i).getContents());
                rol.setJinhuodanhao(sheet.getCell(2, i).getContents());
                rol.setHuozhudaima(sheet.getCell(3, i).getContents());
                rol.setHuozhumingcheng(sheet.getCell(4, i).getContents());
                rol.setCangkudaima(sheet.getCell(5, i).getContents());
                rol.setShouhuoleixing(sheet.getCell(6, i).getContents());
                rol.setHanghao(sheet.getCell(7, i).getContents());
                rol.setShangpindaima(sheet.getCell(8, i).getContents());
                rol.setShangpinmingcheng(sheet.getCell(9, i).getContents());
                try {
                    rol.setDingdanshuliang(Integer.parseInt(sheet.getCell(10, i).getContents()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                rol.setDanwei(sheet.getCell(11, i).getContents());
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
