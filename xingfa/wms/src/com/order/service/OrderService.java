package com.order.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.*;
import com.inventory.vo.RetrievalOrderIds;
import com.order.vo.OrderDetailVo;
import com.order.vo.OrderVo;
import com.order.vo.SearchOrderVo;
import com.util.common.*;
import com.util.common.StringUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.util.pages.GridPages;
import com.webservice.vo.RetrievalCloseVo;
import com.wms.domain.*;
import com.wms.domain.AsrsJob;
import com.wms.domain.Location;
import com.wms.domain.blocks.Srm;
import net.sf.json.JSONObject;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by van on 2018/1/13.
 */
@Service
public class OrderService {

    public PagerReturnObj<List<OrderVo>> list(SearchOrderVo searchOrderVo, GridPages pages) {
        PagerReturnObj<List<OrderVo>> returnObj = new PagerReturnObj<List<OrderVo>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(RetrievalOrder.class);

            if (StringUtils.isNotEmpty(searchOrderVo.getOrderNo())) {
                criteria.add(Restrictions.eq(RetrievalOrder.COL_ORDER_NO, searchOrderVo.getOrderNo()));
            }
            if (StringUtils.isNotEmpty(searchOrderVo.getStatus())) {
                criteria.add(Restrictions.eq(RetrievalOrder.COL_STATUS, searchOrderVo.getStatus()));
            }
            if (StringUtils.isNotEmpty(searchOrderVo.getWhCode())) {
                criteria.add(Restrictions.eq(RetrievalOrder.COL_WHCODE, searchOrderVo.getWhCode()));
            }

            criteria.addOrder(Order.desc(RetrievalOrder.COL_ID));

            //获取总行数
            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult(pages.getFirstRow());
            criteria.setMaxResults(pages.getPageSize());

            List<RetrievalOrder> list = criteria.list();

            List<OrderVo> vos = new ArrayList<OrderVo>(pages.getPageSize());
            OrderVo vo;
            for (RetrievalOrder order : list) {
                vo = new OrderVo();
                vo.setId(order.getId());
                vo.setOrderNo(order.getOrderNo());
                vo.setStatus(order.getStatus());
                vo.setArea(order.getArea());
                vo.setBoxQty(order.getBoxQty());
                vo.setCarrierName(order.getCarrierName());
                vo.setCoustomName(order.getCoustomName());
                vo.setWhCode(order.getWhCode());
                vo.setJobType(order.getJobType());
                vo.setToLocation(order.getToLocation());
                vo.setDesc(order.getDesc());
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

    public ReturnObj<List<OrderDetailVo>> searchDetail(Integer orderId, String keyword) {

        ReturnObj<List<OrderDetailVo>> returnObj = new ReturnObj<>();
        try {

            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(RetrievalOrderDetail.class);
            Criteria orderCri = criteria.createCriteria(RetrievalOrderDetail.COL_RETRIEVALORDER);
            orderCri.add(Restrictions.eq(RetrievalOrder.COL_ID, orderId));
            if (StringUtils.isNotEmpty(keyword)) {
                criteria.add(Restrictions.eq(RetrievalOrderDetail.COL_ITEMCODE, keyword));
            }
            List<RetrievalOrderDetail> orderDetails = criteria.list();
            List<OrderDetailVo> vos = new ArrayList<>();
            OrderDetailVo vo;
            for (RetrievalOrderDetail detail : orderDetails) {
                vo = new OrderDetailVo();

                vo.setId(detail.getId());
                vo.setItemCode(detail.getItemCode());
                vo.setPalletNo(detail.getPalletNo());
                vo.setBatch(detail.getBatch());
                vo.setQty(detail.getQty());

                vos.add(vo);
            }
            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(vos);

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

    public BaseReturnObj retrieval(List<RetrievalOrderIds> orderIds) {
        BaseReturnObj returnObj = new BaseReturnObj();
        String failNos = "";

        try {

            Transaction.begin();
            Query query = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type!=:jt");
            query.setParameter("jt", AsrsJobType.PUTAWAY);
            List<AsrsJob> asrsJobs = query.list();

            Transaction.commit();

            if (!asrsJobs.isEmpty()) {
                returnObj.setSuccess(false);
                returnObj.setMsg("系统正在进行其他出库任务");
                return returnObj;
            }

        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
            returnObj.setSuccess(false);
            returnObj.setMsg("系统异常");
            return returnObj;
        }

        for (RetrievalOrderIds orderId : orderIds) {
            try {
                Transaction.begin();

                RetrievalOrder retrievalOrder = RetrievalOrder.getById(orderId.getId());
                if (retrievalOrder == null) {
                    Transaction.rollback();
                    failNos = failNos + retrievalOrder.getOrderNo();
                    continue;
                }

                if (!retrievalOrder.getStatus().equals(RetrievalOrder.STATUS_WAIT)) {
                    Transaction.rollback();
                    failNos = failNos + retrievalOrder.getOrderNo();
                    continue;
                }

                Set<RetrievalOrderDetail> details = retrievalOrder.getRetrievalOrderDetailSet();
                Iterator<RetrievalOrderDetail> itor = details.iterator();
                List<String> containers = new ArrayList<>();
                boolean flag = true;
                while (itor.hasNext()) {
                    RetrievalOrderDetail detail = itor.next();
                    if (org.apache.commons.lang.StringUtils.isNotEmpty(detail.getPalletNo())) {
                        //指定托盘出库
                        Container container = Container.getByBarcode(detail.getPalletNo());
                        if (container != null) {
                            Location location = container.getLocation();
                            Query query = HibernateUtil.getCurrentSession().createQuery(" from Container where location.position=:po and location.level=:lv " +
                                    "and location.bay=:b and location.seq<:s and location.actualArea=:ar");
                            query.setParameter("po", location.getPosition());
                            query.setParameter("lv", location.getLevel());
                            query.setParameter("b", location.getBay());
                            query.setParameter("s", location.getSeq());
                            query.setParameter("ar", location.getActualArea());
                            List<Container> cis = query.list();
                            if (cis.isEmpty()) {
                                containers.add(container.getBarcode());
                            }

                        } else {
                            flag = false;
                            break;
                        }
                    } else {
                        //按照出库顺序找库存
                        Query query = HibernateUtil.getCurrentSession().createQuery("select i.container.barcode,i.lotNum, i.container.location.seq,i.container.location.level,i.container.location.bay,i.container.location.position from Inventory i,Srm s where " +
                                " i.skuCode =:skuCode and i.whCode =:whCode and i.orderNo is null and i.container.location.position=s.position and s.status='1' group by i.container.barcode,i.lotNum, i.container.location.seq,i.container.location.level,i.container.location.bay,i.container.location.position  order by i.lotNum asc,i.container.location.position asc,i.container.location.bay asc, i.container.location.level asc,i.container.location.seq desc ");
                        query.setParameter("skuCode", detail.getItemCode());
                        query.setParameter("whCode", retrievalOrder.getWhCode());
                        List<Object[]> barCodes = query.list();
                        BigDecimal remindQty = detail.getQty();
                        for (Object[] barCode : barCodes) {

                            if (remindQty.compareTo(BigDecimal.ZERO) <= 0) {
                                break;
                            }

                            Container container = Container.getByBarcode(barCode[0].toString());
                            List<Inventory> inventories = (List<Inventory>) container.getInventories();
                            for (Inventory inventory : inventories) {
                                remindQty = remindQty.subtract(inventory.getQty());
                                inventory.setOrderNo(retrievalOrder.getOrderNo());

                            }
                            if (containers.indexOf(container.getBarcode()) == -1)
                                containers.add(container.getBarcode());

                        }

                        if (remindQty.compareTo(BigDecimal.ZERO) == 1) {
                            Transaction.rollback();
                            failNos = failNos + retrievalOrder.getOrderNo();
                            flag = false;
                            break;
                        }
                    }
                }

                if (!flag) {
                    break;
                }

                for (String barCode : containers) {
                    Container container = Container.getByBarcode(barCode);
                    Location location = container.getLocation();
                    Srm srm = Srm.getSrmByPosition(location.getPosition());
                    Configuration configuration = Configuration.getConfig(srm.getBlockNo());
                    Job job = new Job();
                    job.setStatus("1");
                    job.setFromLocation(location);
                    job.setFromStation(srm.getBlockNo());
                    job.setMcKey(Mckey.getNext());
                    job.setToStation(configuration.getValue());
                    job.setOrderNo(retrievalOrder.getOrderNo());
                    job.setContainer(barCode);
                    job.setType(AsrsJobType.RETRIEVAL);
                    HibernateUtil.getCurrentSession().save(job);
                }

                retrievalOrder.setStatus("1");

                Transaction.commit();

            } catch (Exception e) {
                Transaction.rollback();
                failNos = failNos + orderId.getId() + ";";
                LogWriter.error(LoggerType.WMS, e);

            }
        }
        returnObj.setSuccess(true);
        if (failNos.equals("")) {
            returnObj.setMsg("全部出库成功 ");
        } else {
            returnObj.setMsg(failNos + "出库失败");
        }

        return returnObj;
    }

    public BaseReturnObj close(String orderNo) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            RetrievalOrder retrievalOrder = RetrievalOrder.getByOrderNo(orderNo);
            if (retrievalOrder == null) {
                throw new WmsServiceException("订单不存在");
            }

            RetrievalCloseVo closeVo = new RetrievalCloseVo();
            closeVo.setWhCode(retrievalOrder.getWhCode());
            closeVo.setOrderType(retrievalOrder.getJobType());
            closeVo.setOrderCode(retrievalOrder.getOrderNo());
            String param = JSONObject.fromObject(closeVo).toString();
            String result = ContentUtil.getResultJsonType(Const.OPPLE_OUT_CLOSE_WMS_URL, param);
            JSONObject jsonObject = JSONObject.fromObject(result);
            if (!(Boolean) jsonObject.get("success")) {
                throw new WmsServiceException("订单取消失败");
            } else {
                retrievalOrder.setStatus(RetrievalOrder.STATUS_CANCEL);
            }

            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("关闭成功");

        } catch (WmsServiceException e) {
            Transaction.rollback();
            e.printStackTrace();
            returnObj.setSuccess(false);
            returnObj.setMsg(e.getMessage());
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
            returnObj.setSuccess(false);
            returnObj.setMsg("系统错误");
        }

        return returnObj;

    }
}
