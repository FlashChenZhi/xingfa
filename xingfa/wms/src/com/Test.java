package com;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 18:23 2018/3/10
 * @Description:
 * @Modified By:
 */
public class Test {
    public static void main(String[] args) {
        try{
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("select skuCode as skuCode ,skuName as skuName ," +
                    "lotNum as lotNum ,sum(qty) as qty from Inventory group by skuCode," +
                    "skuName,lotNum");
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map<String,Object>> list = query.list();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date =sdf.format(new Date(new Date().getTime()-3600*1000*24));
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
            String date2 =sdf2.format(new Date(new Date().getTime()-3600*1000*24*30));
            for(Map<String,Object> map:list){
                DayNeaten dayNeaten = new DayNeaten();
                dayNeaten.setSkuCode(map.get("skuCode").toString());
                dayNeaten.setSkuName(map.get("skuName").toString());
                dayNeaten.setLotNum(map.get("lotNum").toString());
                dayNeaten.setBenginningInventory(0);
                dayNeaten.setInStorage(0);
                dayNeaten.setOutStorage(0);
                dayNeaten.setCarryover(((BigDecimal)map.get("qty")).intValue());
                dayNeaten.setDate(date);
                session.save(dayNeaten);

                MonthNeaten monthNeaten = new MonthNeaten();
                monthNeaten.setSkuCode(map.get("skuCode").toString());
                monthNeaten.setSkuName(map.get("skuName").toString());
                monthNeaten.setLotNum(map.get("lotNum").toString());
                monthNeaten.setBenginningInventory(0);
                monthNeaten.setInStorage(0);
                monthNeaten.setOutStorage(0);
                monthNeaten.setCarryover(((BigDecimal)map.get("qty")).intValue());
                monthNeaten.setDate(date2);
                session.save(monthNeaten);
            }
            Transaction.commit();
        }catch (Exception e){
            Transaction.rollback();
            e.printStackTrace();
        }

    }




}
