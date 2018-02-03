package com.wms.service;

import com.order.vo.OrderVo;
import com.order.vo.SearchOrderVo;
import com.util.common.LogMessage;
import com.util.common.PagerReturnObj;
import com.util.common.StringUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.util.pages.GridPages;
import com.wms.domain.Location;
import com.wms.domain.RetrievalOrder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.Test;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class StockOutService {

  public static void main(String[] args) {
      while (true){
          try {
              Transaction.begin();

              Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Location.class);
              criteria.add(Restrictions.eq(Location., );


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
      }
  }
}

