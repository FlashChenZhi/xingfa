package com.order.service;

import com.order.vo.*;
import com.util.common.DateFormat;
import com.util.common.DateTimeFormatter;
import com.util.common.LogMessage;
import com.util.common.PagerReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.util.pages.GridPages;
import com.wms.domain.Job;
import com.wms.domain.JobLog;
import com.wms.domain.Location;
import com.wms.domain.SystemLog;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by van on 2018/1/15.
 */
@Service
public class AsrsService {

    public PagerReturnObj<List<AsrsVo>> searchAsrsjob(SearchAsrsVo searchAsrsVo, GridPages pages) {
        PagerReturnObj<List<AsrsVo>> returnObj = new PagerReturnObj<List<AsrsVo>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Job.class);

            if (StringUtils.isNotBlank(searchAsrsVo.getType())) {
                criteria.add(Restrictions.eq(Job.__TYPE, searchAsrsVo.getType()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getBarcode())) {
                criteria.add(Restrictions.eq(Job.__CONTAINER, searchAsrsVo.getBarcode()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getFromLocation())) {
                Criteria locCri = criteria.createCriteria(Job.__FROMLOCATION);
                locCri.add(Restrictions.eq(Location.__LOCATIONNO, searchAsrsVo.getFromLocation()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getToLocation())) {
                Criteria locCri = criteria.createCriteria(Job.__TOLOCATION);
                locCri.add(Restrictions.eq(Location.__LOCATIONNO, searchAsrsVo.getToLocation()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getFromStation())) {
                criteria.add(Restrictions.eq(Job.__FROMSTATION, searchAsrsVo.getFromStation()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getToStation())) {
                criteria.add(Restrictions.eq(Job.__TOSTATION, searchAsrsVo.getToStation()));
            }


            criteria.addOrder(Order.desc(Job.__ID));

            //获取总行数
            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult(pages.getFirstRow());
            criteria.setMaxResults(pages.getPageSize());

            List<Job> list = criteria.list();

            List<AsrsVo> vos = new ArrayList<AsrsVo>(pages.getPageSize());
            AsrsVo vo;
            for (Job job : list) {
                vo = new AsrsVo();
                vo.setId(job.getId());
                vo.setType(job.getType());
                vo.setStatus(job.getStatus());
                vo.setMcKey(job.getMcKey());
                vo.setCreateDate(DateFormat.format(job.getCreateDate(), DateFormat.YYYYMMDDHHMMSS));
                vo.setFromStation(job.getFromStation());
                vo.setToStation(job.getToStation());
                if (job.getFromLocation() != null)
                    vo.setFromLocation(job.getFromLocation().getLocationNo());
                if (job.getToLocation() != null)
                    vo.setToLocation(job.getToLocation().getLocationNo());
                vo.setBarCode(job.getContainer());
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

    public PagerReturnObj<List<AsrsLogVo>> searchAsrsjobLog(SearchAsrsLogVo searchAsrsVo, GridPages pages) {
        PagerReturnObj<List<AsrsLogVo>> returnObj = new PagerReturnObj<List<AsrsLogVo>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(JobLog.class);

            if (StringUtils.isNotBlank(searchAsrsVo.getType())) {
                criteria.add(Restrictions.eq(JobLog.__TYPE, searchAsrsVo.getType()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getBarcode())) {
                criteria.add(Restrictions.eq(JobLog.__CONTAINER, searchAsrsVo.getBarcode()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getFromLocation())) {
                criteria.add(Restrictions.eq(JobLog.__FROMLOCATIONNO, searchAsrsVo.getBarcode()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getToLocation())) {
                criteria.add(Restrictions.eq(JobLog.__TOLOCATIONNO, searchAsrsVo.getBarcode()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getFromStation())) {
                criteria.add(Restrictions.eq(JobLog.__FROMSTATION, searchAsrsVo.getFromStation()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getToStation())) {
                criteria.add(Restrictions.eq(JobLog.__TOSTATION, searchAsrsVo.getToStation()));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getBeginDate())) {
                DateTimeFormatter formatter = new DateTimeFormatter("yyyy-MM-dd");
                criteria.add(Restrictions.ge(JobLog.__CREATEDATE, formatter.unformat(searchAsrsVo.getBeginDate())));
            }

            if (StringUtils.isNotBlank(searchAsrsVo.getEndDate())) {
                DateTimeFormatter formatter = new DateTimeFormatter("yyyy-MM-dd");
                criteria.add(Restrictions.le(JobLog.__CREATEDATE, formatter.unformat(searchAsrsVo.getEndDate())));
            }

            criteria.addOrder(Order.desc(JobLog.COL_ID));

            //获取总行数
            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult(pages.getFirstRow());
            criteria.setMaxResults(pages.getPageSize());

            List<JobLog> list = criteria.list();

            List<AsrsLogVo> vos = new ArrayList<AsrsLogVo>(pages.getPageSize());
            AsrsLogVo vo;
            for (JobLog job : list) {
                vo = new AsrsLogVo();
                vo.setId(job.getId());
                vo.setType(job.getType());
                vo.setStatus(job.getStatus());
                vo.setMcKey(job.getMckey());
                vo.setCreateDate(DateFormat.format(job.getCreateDate(), DateFormat.YYYYMMDDHHMMSS));
                vo.setFromStation(job.getFromStation());
                vo.setToStation(job.getToStation());
                vo.setFromLocation(job.getFromLocation());
                vo.setToLocation(job.getToLocation());
                vo.setBarCode(job.getContainer());
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


    public PagerReturnObj<List<SystemLogVo>> searchSystemLog(SearchAsrsLogVo searchVo, GridPages pages) {
        PagerReturnObj<List<SystemLogVo>> returnObj = new PagerReturnObj<List<SystemLogVo>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(SystemLog.class);


            criteria.addOrder(Order.desc(JobLog.COL_ID));

            //获取总行数
            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult(pages.getFirstRow());
            criteria.setMaxResults(pages.getPageSize());

            List<SystemLog> list = criteria.list();

            List<SystemLogVo> vos = new ArrayList<SystemLogVo>(pages.getPageSize());
            SystemLogVo vo;
            for (SystemLog job : list) {
                vo = new SystemLogVo();
                vo.setId(job.getId());
                vo.setMessage(job.getMessage());
                vo.setType(job.getType());
                vo.setCreateDate(DateFormat.format(job.getCreateDate(), DateFormat.YYYYMMDDHHMMSS));
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
