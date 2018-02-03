package com.opple;


import com.util.common.Const;
import com.util.common.ContentUtil;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.webservice.vo.RetrievalSuccessVo;
import com.wms.domain.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by van on 2017/12/14.
 */
public class WebService {

    public static void main(String[] args) {

        try {
            Transaction.begin();

//            save("OPWJ00041");

            Transaction.commit();

        } catch (Exception e) {
            Transaction.rollback();
        }
        //        finishPutaway("OPWJ00156");
    }

//    public static void save(String palletNo) {
//        Container container = null;
//
//        Query query = HibernateUtil.getCurrentSession().createQuery("from InventoryView where palletCode=:palletNo");
//        query.setParameter("palletNo", palletNo);
//        List<InventoryView> views = query.list();
//
//
//        container = Container.getByBarcode(palletNo);
//
//        if (container == null) {
//            container = new Container();
//            container.setBarcode(palletNo);
//            container.setLocation(Location.getByLocationNo("000"));
//            container.setCreateDate(new Date());
//            container.setCreateUser("sys");
//            container.setReserved(true);
//            HibernateUtil.getCurrentSession().save(container);
//        }
//
//        for (InventoryView view : views) {
//
//            if (view != null) {
//                Inventory inventory = new Inventory();
//                inventory.setWhCode(view.getWhCode());
//                inventory.setLotNum(view.getLotNum());
//                inventory.setCaseBarCode(view.getCaseBarCode());
//                inventory.setQty(view.getQty());
//                inventory.setCaseQty(view.getCaseQty());
//                inventory.setSkuCode(view.getSkuCode());
//                inventory.setContainer(container);
//                HibernateUtil.getCurrentSession().save(inventory);
//            }
//        }
//    }

    /**
     * 出库完成
     */
    public static void finishOrder(Job job) throws Exception {

        Container container = Container.getByBarcode(job.getContainer());
        Set<Inventory> inventorySet = new HashSet<>(container.getInventories());

        RetrievalOrder retrievalOrder = RetrievalOrder.getByOrderNo(job.getOrderNo());
        RetrievalSuccessVo successVo = new RetrievalSuccessVo();
        successVo.setWhCode(retrievalOrder.getWhCode());
        successVo.setOrderType(retrievalOrder.getJobType());
        successVo.setOrderCode(retrievalOrder.getOrderNo());
        successVo.setPalletCode(container.getBarcode());
        successVo.setItemCode(container.getInventories().iterator().next().getSkuCode());
        String param = JSONObject.fromObject(successVo).toString();

        HibernateUtil.getCurrentSession().delete(job);

        InventoryLog inventoryLog = new InventoryLog();
        inventoryLog.setQty(BigDecimal.ZERO);
        inventoryLog.setType(InventoryLog.TYPE_OUT);
        Iterator<Inventory> iterator = inventorySet.iterator();
        while (iterator.hasNext()) {
            Inventory inventory = iterator.next();
            inventoryLog.setQty(inventoryLog.getQty().add(inventory.getQty()));
            inventoryLog.setLotNum(inventory.getLotNum());
            inventoryLog.setSkuCode(inventory.getSkuCode());
            inventoryLog.setSkuName(inventory.getSkuName());
            inventoryLog.setWhCode(inventory.getWhCode());
            inventoryLog.setFromLocation(container.getLocation().getLocationNo());
            HibernateUtil.getCurrentSession().delete(inventory);
        }
        inventoryLog.setCreateDate(new Date());
        HibernateUtil.getCurrentSession().save(inventoryLog);

        HibernateUtil.getCurrentSession().delete(container);

        String result = ContentUtil.getResultJsonType(Const.OPPLE_OUT_WMS_URL, param);
        JSONObject resultJson = JSONObject.fromObject(result);
        if (!(Boolean) resultJson.get("success")) {
//            throw new Exception("上传出库完成失败");
            RetrievalFinishLog log = new RetrievalFinishLog();
            log.setOrderNo(job.getOrderNo());
            log.setContainer(container.getBarcode());
            HibernateUtil.getCurrentSession().save(log);
        }

    }

    /**
     * 上架完成
     */
    public static void finishPutaway(String palletNo) throws Exception {
        //上报上位ERP
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("palletCode", palletNo);
        String param = jsonObject.toString();
        String result = ContentUtil.getResultJsonType(Const.OPPLE_IN_WMS_URL, param);
        JSONObject resultJson = JSONObject.fromObject(result);
        if (!(Boolean) resultJson.get("success")) {
            throw new Exception("上传入库完成失败");
        }
    }

}
