package com.wms;

import com.asrs.business.consts.AsrsJobType;
import com.util.common.LogWriter;
import com.util.common.LoggerType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.DayNeaten;
import com.wms.domain.JobLog;
import com.wms.domain.MonthNeaten;
import org.hibernate.Session;

import java.beans.SimpleBeanInfo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 11:01 2018/9/4
 * @Description:
 * @Modified By:
 */
public class DayNeatenThread implements Runnable {
    @Override
    public void run() {
        while (true){
            try{
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date =sdf.format(new Date());
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
                String date2 =sdf2.format(new Date());
                long count =DayNeaten.getDayNeatenByDate(date);
                long count2 = MonthNeaten.getMonthNeatenByDate(date2);
                if(count==0 || count2==0){
                    if(count==0){
                        List<DayNeaten> dayNeatenList=DayNeaten.getDayNeatenByDateDesc();
                        for(DayNeaten dayNeaten:dayNeatenList){
                            DayNeaten dayNeaten1 = new DayNeaten();
                            dayNeaten1.setSkuCode(dayNeaten.getSkuCode());
                            dayNeaten1.setSkuName(dayNeaten.getSkuName());
                            dayNeaten1.setLotNum(dayNeaten.getLotNum());
                            dayNeaten1.setBenginningInventory(dayNeaten.getCarryover());
                            dayNeaten1.setInStorage(0);
                            dayNeaten1.setOutStorage(0);
                            dayNeaten1.setCarryover(dayNeaten.getCarryover());
                            dayNeaten1.setDate(date);
                            session.save(dayNeaten1);
                        }
                    }

                    if(count2==0){
                        List<MonthNeaten> monthNeatenList=MonthNeaten.getMonthNeatenByDateDesc();
                        for(MonthNeaten monthNeaten:monthNeatenList){
                            MonthNeaten monthNeaten1 = new MonthNeaten();
                            monthNeaten1.setSkuCode(monthNeaten.getSkuCode());
                            monthNeaten1.setSkuName(monthNeaten.getSkuName());
                            monthNeaten1.setLotNum(monthNeaten.getLotNum());
                            monthNeaten1.setBenginningInventory(monthNeaten.getCarryover());
                            monthNeaten1.setInStorage(0);
                            monthNeaten1.setOutStorage(0);
                            monthNeaten1.setCarryover(monthNeaten.getCarryover());
                            monthNeaten1.setDate(date2);
                            session.save(monthNeaten1);
                        }
                    }

                }else{
                    List<JobLog> jobLogList = JobLog.getJobLogByType();
                    for(JobLog jobLog:jobLogList){
                        //日结
                        DayNeaten dayNeaten = DayNeaten.getDayNeatenByConditions(date,jobLog.getSkuCode(),jobLog.getLotNum() );
                        int qty= jobLog.getQty().intValue();
                        if(dayNeaten!=null){
                            if(jobLog.getType().equals(AsrsJobType.PUTAWAY)){
                                dayNeaten.setInStorage(dayNeaten.getInStorage()+qty);
                                dayNeaten.setCarryover(dayNeaten.getCarryover()+qty);
                            }else if(jobLog.getType().equals(AsrsJobType.RETRIEVAL)){
                                dayNeaten.setOutStorage(dayNeaten.getOutStorage()+qty);
                                dayNeaten.setCarryover(dayNeaten.getCarryover()-qty);
                            }
                            session.saveOrUpdate(dayNeaten);
                        }else{
                            if(jobLog.getType().equals(AsrsJobType.PUTAWAY)){
                                DayNeaten dayNeaten1 = new DayNeaten();
                                dayNeaten1.setSkuCode(jobLog.getSkuCode());
                                dayNeaten1.setSkuName(jobLog.getSkuName());
                                dayNeaten1.setLotNum(jobLog.getLotNum());
                                dayNeaten1.setBenginningInventory(0);
                                dayNeaten1.setInStorage(qty);
                                dayNeaten1.setOutStorage(0);
                                dayNeaten1.setCarryover(qty);
                                dayNeaten1.setDate(date);
                                session.save(dayNeaten1);
                            }else if(jobLog.getType().equals(AsrsJobType.RETRIEVAL)){
                                System.out.println("not exists this Inventory: skuCode:"+jobLog.getSkuCode()+",lotNum:"+jobLog.getLotNum());
                                LogWriter.error(LoggerType.XMLMessageInfo,"not exists this Inventory: skuCode:"+jobLog.getSkuCode()+",lotNum:"+jobLog.getLotNum() );
                                throw new Exception("not exists this Inventory: skuCode:"+jobLog.getSkuCode()+",lotNum:"+jobLog.getLotNum());
                            }
                        }
                        //月结
                        MonthNeaten monthNeaten = MonthNeaten.getMonthNeatenByConditions(date2,jobLog.getSkuCode(),jobLog.getLotNum() );
                        if(monthNeaten!=null){
                            if(jobLog.getType().equals(AsrsJobType.PUTAWAY)){
                                monthNeaten.setInStorage(monthNeaten.getInStorage()+qty);
                                monthNeaten.setCarryover(monthNeaten.getCarryover()+qty);
                            }else if(jobLog.getType().equals(AsrsJobType.RETRIEVAL)){
                                monthNeaten.setOutStorage(monthNeaten.getOutStorage()+qty);
                                monthNeaten.setCarryover(monthNeaten.getCarryover()-qty);
                            }
                            session.saveOrUpdate(monthNeaten);
                        }else{
                            if(jobLog.getType().equals(AsrsJobType.PUTAWAY)){
                                MonthNeaten monthNeaten1 = new MonthNeaten();
                                monthNeaten1.setSkuCode(jobLog.getSkuCode());
                                monthNeaten1.setSkuName(jobLog.getSkuName());
                                monthNeaten1.setLotNum(jobLog.getLotNum());
                                monthNeaten1.setBenginningInventory(0);
                                monthNeaten1.setInStorage(qty);
                                monthNeaten1.setOutStorage(0);
                                monthNeaten1.setCarryover(qty);
                                monthNeaten1.setDate(date2);
                                session.save(monthNeaten1);
                            }else if(jobLog.getType().equals(AsrsJobType.RETRIEVAL)){
                                System.out.println("not exists this Inventory: skuCode:"+jobLog.getSkuCode()+",lotNum:"+jobLog.getLotNum());
                                LogWriter.error(LoggerType.XMLMessageInfo,"not exists this Inventory: skuCode:"+jobLog.getSkuCode()+",lotNum:"+jobLog.getLotNum() );
                                throw new Exception("not exists this Inventory: skuCode:"+jobLog.getSkuCode()+",lotNum:"+jobLog.getLotNum());
                            }
                        }
                        jobLog.setRead(true);
                    }
                }
                Transaction.commit();
            }catch (Exception e){
                Transaction.rollback();
                e.printStackTrace();
            }
        }
    }
}
