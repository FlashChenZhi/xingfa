package com.opple;


import com.util.common.Const;
import com.util.common.ContentUtil;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.webservice.vo.RetrievalSuccessVo;
import com.wms.domain.*;
import net.sf.json.JSONObject;
import org.hibernate.Query;

import java.util.*;

/**
 * Created by van on 2017/12/14.
 */
public class WebService {

    public static void main(String[] args) {
        Transaction.begin();
        Job job = Job.getById(1);
        finishOrder(job);

        Transaction.commit();
//        finishPutaway("OPWJ00156");
    }

    public static InventoryView getPutawayInfo(String palletNo) {
        Query query = HibernateUtil.getCurrentSession().createQuery("from InventoryView where palletCode=:palletNo");
        query.setParameter("palletNo", palletNo);
        query.setMaxResults(1);
        InventoryView view = (InventoryView) query.uniqueResult();
        return view;

    }

    public static void save(String palletNo) {
        Container container = null;

        Query query = HibernateUtil.getCurrentSession().createQuery("from InventoryView where palletCode=:palletNo");
        query.setParameter("palletNo", palletNo);
        List<InventoryView> views = query.list();


        container = Container.getByBarcode(palletNo);

        if (container == null) {
            container = new Container();
            container.setBarcode(palletNo);
            container.setLocation(Location.getByLocationNo("000"));
            container.setCreateDate(new Date());
            container.setCreateUser("sys");
            container.setReserved(true);
            HibernateUtil.getCurrentSession().save(container);
        }

        for (InventoryView view : views) {

            if (view != null) {
                Inventory inventory = new Inventory();
                inventory.setWhCode(view.getWhCode());
                inventory.setLotNum(view.getLotNum());
                inventory.setCaseBarCode(view.getCaseBarCode());
                inventory.setQty(view.getQty());
                inventory.setCaseQty(view.getCaseQty());
                inventory.setSkuCode(view.getSkuCode());
                inventory.setContainer(container);
                HibernateUtil.getCurrentSession().save(inventory);
            }
        }
    }

    /**
     * 出库完成
     */
    public static void finishOrder(Job job) {
        try {

            Container container = job.getContainer();
            Set<Inventory> inventorySet = new HashSet<>(container.getInventories());

            RetrievalOrder retrievalOrder = RetrievalOrder.getByOrderNo(job.getOrderNo());
            RetrievalSuccessVo successVo = new RetrievalSuccessVo();
            successVo.setWhCode(retrievalOrder.getWhCode());
            successVo.setOrderType(retrievalOrder.getJobType());
            successVo.setOrderCode(retrievalOrder.getOrderNo());
            successVo.setPalletCode(job.getContainer().getBarcode());
            successVo.setItemCode(job.getContainer().getInventories().iterator().next().getSkuCode());
            String param = JSONObject.fromObject(successVo).toString();

            HibernateUtil.getCurrentSession().delete(job);

            Iterator<Inventory> iterator = inventorySet.iterator();
            while (iterator.hasNext()) {
                Inventory inventory = iterator.next();
                HibernateUtil.getCurrentSession().delete(inventory);
            }

            HibernateUtil.getCurrentSession().delete(container);

            String result = ContentUtil.getResultJsonType(Const.OPPLE_OUT_WMS_URL, param);
            JSONObject resultJson = JSONObject.fromObject(result);
            if (!(Boolean) resultJson.get("success")) {
                throw new Exception("上传出库完成失败");
            }
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * 上架完成
     */
    public static void finishPutaway(String palletNo) {
        try {
            Transaction.begin();
            //上架完成，保存库存
            save(palletNo);

            //上报上位ERP
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("palletCode", palletNo);
            String param = jsonObject.toString();
            String result = ContentUtil.getResultJsonType(Const.OPPLE_IN_WMS_URL, param);
            JSONObject resultJson = JSONObject.fromObject(result);
            if (!(Boolean) resultJson.get("success")) {
                throw new Exception("上传入库完成失败");
            }
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }
    }

}
