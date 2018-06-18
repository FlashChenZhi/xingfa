package com;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 18:23 2018/3/10
 * @Description:
 * @Modified By:
 */
public class Test {
    public static void main(String[] args) {
        Transaction.begin();


        List<TransportOrderLog> transportOrderLogList = TransportOrderLog.getTransportOrderByType2();
        //生成回库任务
        if(transportOrderLogList.size()!=0){
            for(TransportOrderLog transportOrderLog:transportOrderLogList){
                System.out.println(transportOrderLog.getFromLocation().getLocationNo());

            }
        }
        Transaction.commit();
    }




}
