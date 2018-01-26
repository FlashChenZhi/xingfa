package com.inventory.service;

import com.inventory.vo.InventoryLogVo;
import com.inventory.vo.InventoryVo;
import com.inventory.vo.SearchInvLogVo;
import com.inventory.vo.SearchInventoryVo;
import com.util.common.*;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.util.pages.GridPages;
import com.wms.domain.Container;
import com.wms.domain.Inventory;
import com.wms.domain.InventoryLog;
import com.wms.domain.Location;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by van on 2018/1/14.
 */
@Service
public class InventoryService {

    public PagerReturnObj<List<InventoryVo>> list(SearchInventoryVo searchVo, GridPages pages) {
        PagerReturnObj<List<InventoryVo>> returnObj = new PagerReturnObj<List<InventoryVo>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Inventory.class);
            Criteria conCri = criteria.createCriteria(Inventory.COL_CONTAINER);

            if (StringUtils.isNotEmpty(searchVo.getWhCode())) {
                criteria.add(Restrictions.eq(Inventory.COL_WHCODE, searchVo.getWhCode()));
            }
            if (StringUtils.isNotEmpty(searchVo.getSkuCode())) {
                criteria.add(Restrictions.eq(Inventory.COL_SKUCODE, searchVo.getSkuCode()));
            }
            if (StringUtils.isNotEmpty(searchVo.getBarcodes())) {
                criteria.add(Restrictions.eq(Inventory.COL_CASEBARCODE, searchVo.getBarcodes()));
            }
            if (StringUtils.isNotEmpty(searchVo.getContainerBarcode())) {
                conCri.add(Restrictions.eq(Container.__BARCODE, searchVo.getContainerBarcode()));
            }
            if (StringUtils.isNotEmpty(searchVo.getLocationNo())) {
                Criteria loCri = conCri.createCriteria(Container.__LOCATION);
                loCri.add(Restrictions.eq(Location.__LOCATIONNO, searchVo.getLocationNo()));
            }

            criteria.addOrder(Order.desc(Inventory.__ID));

            //获取总行数
            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据s
            criteria.setProjection(null);
            criteria.setFirstResult(pages.getFirstRow());
            criteria.setMaxResults(pages.getPageSize());

            List<Inventory> list = criteria.list();

            List<InventoryVo> vos = new ArrayList<InventoryVo>(pages.getPageSize());
            InventoryVo vo;
            for (Inventory inventory : list) {
                vo = new InventoryVo();
                vo.setId(inventory.getId());
                vo.setQty(inventory.getQty());
                vo.setPalletNo(inventory.getContainer().getBarcode());
                vo.setWhCode(inventory.getWhCode());
                vo.setItemCode(inventory.getSkuCode());
                vo.setCaseBarCode(inventory.getCaseBarCode());
                vo.setLotNum(inventory.getLotNum());
                vo.setLocationNo(inventory.getContainer().getLocation().getLocationNo());
                vo.setItemName(inventory.getSkuName());
                vos.add(vo);
            }

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(vos);
            returnObj.setCount(count);
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());

        }

        return returnObj;

    }

    public PagerReturnObj<List<InventoryLogVo>> searchLog(SearchInvLogVo searchVo, GridPages pages) {
        PagerReturnObj<List<InventoryLogVo>> returnObj = new PagerReturnObj<List<InventoryLogVo>>();
        try {
            Transaction.begin();
            DateTimeFormatter formatter = new DateTimeFormatter("yyyy-mm-dd");

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(InventoryLog.class);
            if (StringUtils.isNotEmpty(searchVo.getContainerBarcode())) {
                criteria.add(Restrictions.eq(InventoryLog.COL_CONTAINER, searchVo.getContainerBarcode()));
            }
            if (StringUtils.isNotEmpty(searchVo.getFromLocation())) {
                criteria.add(Restrictions.eq(InventoryLog.COL_FROMLOCATION, searchVo.getFromLocation()));
            }
            if (StringUtils.isNotEmpty(searchVo.getToLocation())) {
                criteria.add(Restrictions.eq(InventoryLog.COL_TOLOCATION, searchVo.getToLocation()));
            }
            if (StringUtils.isNotEmpty(searchVo.getSkuCode())) {
                criteria.add(Restrictions.eq(InventoryLog.COL_SKUCODE, searchVo.getSkuCode()));
            }
            if (StringUtils.isNotEmpty(searchVo.getType())) {
                criteria.add(Restrictions.eq(InventoryLog.COL_TYPE, searchVo.getType()));
            }
            if (StringUtils.isNotEmpty(searchVo.getBeginCreateDate())) {
                criteria.add(Restrictions.ge(InventoryLog.COL_CREATEDATE, formatter.unformat(searchVo.getBeginCreateDate())));
            }
            if (StringUtils.isNotEmpty(searchVo.getEndCreateDate())) {
                criteria.add(Restrictions.le(InventoryLog.COL_CREATEDATE, formatter.unformat(searchVo.getEndCreateDate())));
            }

            criteria.addOrder(Order.desc(InventoryLog.COL_ID));

            //获取总行数
            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据s
            criteria.setProjection(null);
            criteria.setFirstResult(pages.getFirstRow());
            criteria.setMaxResults(pages.getPageSize());

            List<InventoryLog> list = criteria.list();

            List<InventoryLogVo> vos = new ArrayList<InventoryLogVo>(pages.getPageSize());
            InventoryLogVo vo;
            for (InventoryLog log : list) {
                vo = new InventoryLogVo();
                vo.setContainer(log.getContainer());
                vo.setFromLocation(log.getFromLocation());
                vo.setToLocation(log.getToLocation());
                vo.setQty(log.getQty());
                vo.setWhCode(log.getWhCode());
                vo.setSkuCode(log.getSkuCode());
                vo.setLotNum(log.getLotNum());
                vo.setOrderNo(log.getOrderNo());
                vo.setId(log.getId());
                vo.setSkuName(log.getSkuName());

                vos.add(vo);
            }

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(vos);
            returnObj.setCount(count);
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());

        }

        return returnObj;

    }
}
