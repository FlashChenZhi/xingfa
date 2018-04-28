package com.wms;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by Administrator on 2018/4/28.
 */
public class StockinStLedShower {
    public static void main(String[] args) {
        while (true){
            try{
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();
                Query query = session.createQuery("from Station s where s.stationNo in (:st1,:st2)")
                        .setString("st1","1101")
                        .setString("st2","1301");

                List<Station> stations = query.list();

                for(Station station : stations){
                    if(!station.getMode().equals(AsrsJobType.PUTAWAY)){
                        continue;
                    }
                    query = session.createQuery("from Job j where j.fromStation = :stationNo and j.status = :waiting order by j.createDate")
                            .setString("stationNo",station.getStationNo())
                            .setString("waiting", AsrsJobStatus.WAITING)
                            .setMaxResults(1);

                    Job j = (Job) query.uniqueResult();
                    if(j != null){
                        String message1 = j.getContainer();
                        String message2 = "";
                        String message3 = "";
                        String message4 = "";
                        InventoryView inventoryView = InventoryView.getByPalletNo(j.getContainer());
                        if(inventoryView != null){
                            message2 = inventoryView.getSkuName();
                            message3 = inventoryView.getLotNum();
                            message4 = String.valueOf(inventoryView.getQty());
                        }

                        LedMessage.show(station.getStationNo(),message1,message2,message3,message4,j.getMcKey());

                    }else{
                        LedMessage ledMessage = LedMessage.getByLedNo(station.getStationNo());
                        if(StringUtils.isNotBlank(ledMessage.getMckey())) {
                            LedMessage.clear(station.getStationNo());
                        }
                    }
                }


                Transaction.commit();
            }catch (Exception e){
                Transaction.rollback();
                e.printStackTrace();
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
