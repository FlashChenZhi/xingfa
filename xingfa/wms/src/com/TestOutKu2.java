package com;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.ReasonCode;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.domain.blocks.SCar;
import com.wms.domain.blocks.Srm;
import org.apache.http.client.utils.DateUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestOutKu2 {
    public static void main(String[] args) {
            try {
                Transaction.begin();


                Session session = HibernateUtil.getCurrentSession();
                String mcKey = "0076";

                AsrsJob aj = AsrsJob.getAsrsJobByMcKey(mcKey);
                SCar sCar = (SCar) SCar.getByBlockNo("SC01");
                Srm srm = (Srm) Srm.getByBlockNo("ML01");
                sCar.setMcKey(null);
                sCar.setReservedMcKey(null);
                sCar.setOnMCar(null);

                sCar.setBank(0);
                sCar.setOnMCar("ML01");

                srm.setsCarBlockNo(sCar.getBlockNo());

                aj.delete();


                sCar.setWaitingResponse(false);

                Job j = Job.getByMcKey(mcKey);
                Location l = j.getToLocation();
                l.setReserved(false);
                l.setEmpty(false);
                session.update(l);

                Container container = null;

                Query query = HibernateUtil.getCurrentSession().createQuery("from InventoryView where palletCode=:palletNo");
                query.setParameter("palletNo", j.getContainer());
                List<InventoryView> views = query.list();


                container = Container.getByBarcode(j.getContainer());

                if (container == null) {
                    container = new Container();
                    container.setBarcode(j.getContainer());
                    container.setLocation(l);
                    container.setCreateDate(new Date());
                    container.setCreateUser("sys");
                    container.setReserved(true);
                    HibernateUtil.getCurrentSession().save(container);
                }
                InventoryLog inventoryLog = new InventoryLog();
                inventoryLog.setQty(BigDecimal.ZERO);
                inventoryLog.setType(InventoryLog.TYPE_IN);
                for (InventoryView view : views) {

                    if (view != null) {
                        Inventory inventory = new Inventory();
                        inventory.setWhCode(view.getWhCode());
                        inventory.setSkuName(view.getSkuName());
                        inventory.setLotNum(view.getLotNum());
                        inventory.setQty(view.getQty());
                        inventory.setSkuCode(view.getSkuCode());
                        inventory.setContainer(container);
                        inventory.setStoreDate(DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                        inventory.setStoreTime(DateUtils.formatDate(new Date(), "HH:mm:ss"));
                        HibernateUtil.getCurrentSession().save(inventory);
                        inventoryLog.setQty(inventoryLog.getQty().add(inventory.getQty()));
                        inventoryLog.setSkuCode(inventory.getSkuCode());
                        inventoryLog.setWhCode(inventory.getWhCode());
                        inventoryLog.setToLocation(container.getLocation().getLocationNo());
                        inventoryLog.setLotNum(inventory.getLotNum());
                        inventoryLog.setSkuName(inventory.getSkuName());

                        session.delete(view);
                    }

                }
                inventoryLog.setContainer(container.getBarcode());
                inventoryLog.setCreateDate(new Date());
                session.save(inventoryLog);
                j.asrsDone();

                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                e.printStackTrace();
            }


    }

    @Test
    public void test(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        System.out.println(DateUtils.formatDate(new Date(), "HH:mm:ss"));
        System.out.println(sdf2.format(new Date()));
    }
}
