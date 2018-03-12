package com;

import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import org.hibernate.Query;
import org.hibernate.Session;

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
        try{
            Transaction.begin();

            Session session = HibernateUtil.getCurrentSession();

            Job j = Job.getById(378);

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
                container.setReserved(false);
                HibernateUtil.getCurrentSession().save(container);
            }
            InventoryLog inventoryLog = new InventoryLog();
            inventoryLog.setQty(BigDecimal.ZERO);
            inventoryLog.setType(InventoryLog.TYPE_IN);
            for (InventoryView view : views) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                if (view != null) {
                    Inventory inventory = new Inventory();
                    inventory.setWhCode(view.getWhCode());
                    inventory.setSkuName(view.getSkuName());
                    inventory.setLotNum(view.getLotNum());
                    inventory.setQty(view.getQty());
                    inventory.setSkuCode(view.getSkuCode());
                    inventory.setContainer(container);
                    inventory.setStoreDate(sdf.format(new Date()));
                    inventory.setStoreTime(sdf2.format(new Date()));
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

        }catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }
    }
}
