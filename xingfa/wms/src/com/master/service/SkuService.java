package com.master.service;

import com.master.vo.SkuInStrategyVo;
import com.master.vo.SkuShelfLifeVo;
import com.master.vo.SkuVo;
import com.order.vo.OrderVo;
import com.util.common.*;
import com.util.excel.*;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.util.pages.GridPages;
import com.wms.domain.InStrategy;
import com.wms.domain.RetrievalOrder;
import com.wms.domain.Sku;
import com.wms.domain.SkuView;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by van on 2018/1/16.
 */
@Service
public class SkuService {

    public PagerReturnObj<List<SkuVo>> getSkus(String skuCode, GridPages pages) {

        PagerReturnObj<List<SkuVo>> returnObj = new PagerReturnObj<List<SkuVo>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(SkuView.class);
            if (StringUtils.isNotEmpty(skuCode)) {
                criteria.add(Restrictions.eq(SkuView.COL_CODE, skuCode));
            }
            criteria.addOrder(Order.desc(SkuView.COL_CODE));

            //获取总行数
            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult(pages.getFirstRow());
            criteria.setMaxResults(pages.getPageSize());

            List<SkuView> list = criteria.list();

            List<SkuVo> vos = new ArrayList<SkuVo>(pages.getPageSize());
            SkuVo vo;
            for (SkuView skuView : list) {
                vo = new SkuVo();
                vo.setSkuCode(skuView.getSkuCode());
                vo.setSkuName(skuView.getSkuName());
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


    public PagerReturnObj<List<SkuShelfLifeVo>> getShelfLife(String skuCode, GridPages pages) {

        PagerReturnObj<List<SkuShelfLifeVo>> returnObj = new PagerReturnObj<List<SkuShelfLifeVo>>();
        try {
            Transaction.begin();

            StringBuilder sb = new StringBuilder("select i.ITEM_CODE,i.item_name,s.SHELF_LIFE,s.WARNING from SKU_IFAC i left join sku s on i.ITEM_CODE = s.SKU_CODE ");
            StringBuilder coutSb = new StringBuilder("select count(1) from SKU_IFAC i left join sku s on i.ITEM_CODE = s.SKU_CODE");
            if (org.apache.commons.lang.StringUtils.isNotBlank(skuCode)) {
                sb.append(" where i.ITEM_CODE ='" + skuCode + "' ");
                coutSb.append(" where i.ITEM_CODE ='" + skuCode + "' ");
            }

            Query query = HibernateUtil.getCurrentSession().createSQLQuery(sb.toString());
            Query countQuery = HibernateUtil.getCurrentSession().createSQLQuery(coutSb.toString());

            query.setFirstResult(pages.getFirstRow());
            query.setMaxResults(pages.getPageSize());
            BigDecimal total = (BigDecimal) countQuery.uniqueResult();
            List<Object[]> skus = query.list();

            List<SkuShelfLifeVo> vos = new ArrayList<SkuShelfLifeVo>(pages.getPageSize());
            SkuShelfLifeVo vo;
            for (Object[] sku : skus) {
                vo = new SkuShelfLifeVo();
                vo.setSkuCode((String) sku[0]);
                vo.setSkuName((String) sku[1]);
                if (sku[2] != null)
                    vo.setShelfLife(((BigDecimal) sku[2]).intValue());
                if (sku[3] != null)
                    vo.setShelfLife(((BigDecimal) sku[3]).intValue());

                vos.add(vo);
            }

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(vos);
            returnObj.setCount(total == null ? 0 : total.longValue());
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

    public PagerReturnObj<List<SkuInStrategyVo>> getInStrategy(String skuCode, GridPages pages) {
        PagerReturnObj<List<SkuInStrategyVo>> returnObj = new PagerReturnObj<List<SkuInStrategyVo>>();
        try {
            Transaction.begin();

            StringBuilder sb = new StringBuilder("select i.ITEM_CODE,i.item_name,s.position from SKU_IFAC i left join in_strategy s on i.ITEM_CODE = s.SKU_CODE ");
            StringBuilder coutSb = new StringBuilder("select count(1) from SKU_IFAC i left join in_strategy s on i.ITEM_CODE = s.SKU_CODE");
            if (org.apache.commons.lang.StringUtils.isNotBlank(skuCode)) {
                sb.append(" where i.ITEM_CODE ='" + skuCode + "' ");
                coutSb.append(" where i.ITEM_CODE ='" + skuCode + "' ");
            }

            Query query = HibernateUtil.getCurrentSession().createSQLQuery(sb.toString());
            Query countQuery = HibernateUtil.getCurrentSession().createSQLQuery(coutSb.toString());

            query.setFirstResult(pages.getFirstRow());
            query.setMaxResults(pages.getPageSize());
            BigDecimal total = (BigDecimal) countQuery.uniqueResult();
            List<Object[]> skus = query.list();

            List<SkuInStrategyVo> vos = new ArrayList<SkuInStrategyVo>(pages.getPageSize());
            SkuInStrategyVo vo;
            for (Object[] sku : skus) {
                vo = new SkuInStrategyVo();
                vo.setSkuCode((String) sku[0]);
                vo.setSkuName((String) sku[1]);
                if (sku[2] != null)
                    vo.setPosition((String) sku[2]);
                vos.add(vo);
            }

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(vos);
            returnObj.setCount(total == null ? 0 : total.longValue());
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

    public ReturnObj<HSSFWorkbook> generateExcelTemplateExportFile() {
        ReturnObj<HSSFWorkbook> returnObj = new ReturnObj<HSSFWorkbook>();
        try {
            Transaction.begin();

            List<SkuInStrategyVo> excelVos = new ArrayList<SkuInStrategyVo>();

            HSSFWorkbook hssfWorkbook = ExcelExportUtils.generateExcelFile(generateExcelExportParams(), excelVos);

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(hssfWorkbook);
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (WmsServiceException ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(ex.getMessage());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());

        }

        return returnObj;
    }

    public List<ExcelExportParam> generateExcelExportParams() {
        List<ExcelExportParam> list = new ArrayList<ExcelExportParam>();
        list.add(new ExcelExportParam("skuCode", "skuCode"));
        list.add(new ExcelExportParam("skuName", "skuName"));
        list.add(new ExcelExportParam("position", "position"));

        return list;
    }


    public BaseReturnObj doExcelImport(HttpSession httpSession, MultipartFile file) {

        BaseReturnObj returnObj = new BaseReturnObj();

        List<SkuInStrategyVo> excelDataList;
        try {
            httpSession.removeAttribute("done");
            httpSession.removeAttribute("total");

            excelDataList = ExcelImportUtils.doExcelImport(SkuInStrategyVo.class, generateExcelImportParams(), file, 0);
        } catch (ExcelException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(ex.getMessage());

            return returnObj;
        } catch (Exception ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());

            return returnObj;
        }


        int totalQty = excelDataList.size();

        httpSession.setAttribute("total", totalQty);

        int updateQty = 0;
        int rowNum = 1;
        String errorMsg = "";
        for (SkuInStrategyVo excelData : excelDataList) {
            try {
                Transaction.begin();

                Query query = HibernateUtil.getCurrentSession().createQuery("from InStrategy where skuCode=:skuCode");
                query.setParameter("skuCode", excelData.getSkuCode());
                InStrategy vo = (InStrategy) query.uniqueResult();

                if (vo == null) {
                    vo = new InStrategy();
                    vo.setSkuCode(excelData.getSkuCode());
                    vo.setSkuName(excelData.getSkuName());
                    vo.setPosition(excelData.getPosition());
                    HibernateUtil.getCurrentSession().save(vo);
                } else {
                    vo.setSkuCode(excelData.getSkuCode());
                    vo.setSkuName(excelData.getSkuName());
                    vo.setPosition(excelData.getPosition());
                }

                updateQty++;

                httpSession.setAttribute("done", rowNum++);

                Transaction.commit();
            } catch (JDBCConnectionException ex) {
                errorMsg = LogMessage.DB_DISCONNECTED.getName();
                httpSession.setAttribute("done", totalQty);
                break;
            } catch (WmsServiceException ex) {
                Transaction.rollback();
                errorMsg = ex.getMessage();
                httpSession.setAttribute("done", totalQty);
                break;
            } catch (Exception ex) {
                Transaction.rollback();
                errorMsg = LogMessage.UNEXPECTED_ERROR.getName();
                httpSession.setAttribute("done", totalQty);
                break;
            }
        }

        if (totalQty == updateQty) {
            returnObj.setSuccess(true);
            returnObj.setMsg(String.format("全部导入成功, 更新 %s 条", updateQty));
        }
        if (totalQty > updateQty) {
            returnObj.setSuccess(true);
            if (StringUtils.isEmpty(errorMsg)) {
                returnObj.setMsg(String.format("部分导入成功, 总数 %s 条, 更新 %s 条, 失败 %s 条", totalQty, updateQty, totalQty - updateQty));
            } else {
                returnObj.setMsg(String.format("部分导入成功, 更新 %s 条, 第 %s 处理失败, 原因:%s", updateQty, rowNum, errorMsg));
            }
        }
        if (updateQty == 0) {
            returnObj.setSuccess(false);

            if (StringUtils.isEmpty(errorMsg)) {
                returnObj.setMsg("全部导入失败");
            } else {
                returnObj.setMsg(String.format("全部导入失败, 第 %s 处理失败, 原因:%s", rowNum, errorMsg));
            }
        }

        return returnObj;

    }

    public List<ExcelImportParam> generateExcelImportParams() {
        List<ExcelImportParam> list = new ArrayList<ExcelImportParam>();
        list.add(new ExcelImportParam(0, "logicZoneCode", false, "location.import.logicZoneCode.empty", "", "", ExcelImportParam.VALIDATE_TYPE_DEFAULT));
        list.add(new ExcelImportParam(1, "locationNo", false, "location.import.locationNo.empty", "", "", ExcelImportParam.VALIDATE_TYPE_DEFAULT));
        list.add(new ExcelImportParam(2, "putawaySeq", false, "location.import.putawaySeq.empty", "", "", ExcelImportParam.VALIDATE_TYPE_DEFAULT));

        return list;
    }

}
