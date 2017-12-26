package com.wms.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.TransportType;
import com.asrs.domain.XMLbean.Envelope;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.ControlArea.RefId;
import com.asrs.domain.XMLbean.XMLList.ControlArea.Sender;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.TransportOrderDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.asrs.domain.XMLbean.XMLList.DataArea.StUnit;
import com.asrs.domain.XMLbean.XMLList.TransportOrder;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.asrs.xml.util.XMLUtil;
import com.util.common.Const;
import com.util.common.HttpMessage;
import com.util.excel.ExcelExportParam;
import com.util.excel.ExcelExportUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.vo.InventoryExcelVo;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by Administrator on 2016/10/19.
 */
public class InventoryService {
    //入库
    public HttpMessage saveInputArea(String barcode, String skuCode, int qty, String station, HttpSession httpSession) {
        HttpMessage httpMessage = new HttpMessage();
        String userName = (String) httpSession.getAttribute("userName");
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            //暂时将托盘放入虚拟货位
            Location vl = Location.getByLocationNo(Const.RECV_TEMP_LOCATION);
            if (vl == null) {
                Transaction.rollback();
                httpMessage.setSuccess(false);
                httpMessage.setMsg("没有找到虚拟货位，请联系软件部");
                return httpMessage;
            }

            Container c = Container.getByBarcode(barcode);
            if (c != null) {
                Transaction.rollback();
                httpMessage.setSuccess(false);
                httpMessage.setMsg("托盘号重复，请换一块托盘");
                return httpMessage;
            }
            //获取一个托盘对象
            c = new Container();
            c.setBarcode(barcode);
            c.setLocation(vl);
            c.setReserved(false);
            c.setCreateDate(new Date());
            c.setCreateUser(userName);
            session.save(c);

            //生成一个库存
            Inventory i = new Inventory();
//            i.setSkuCode(skuCode);
//            i.setQty(qty);
//            i.setAvailableQty(qty);
//            i.setContainer(c);
//            i.setCreateDate(new Date());
            session.save(i);

            //生成一个入库作业
            Job j = new Job();
            j.setMcKey(Mckey.getNext());
            //设置来自哪个站台
            j.setFromStation(station);
            //设置托盘号
            j.setContainer(c);
            j.setStatus(AsrsJobStatus.WAITING);
            j.setType(AsrsJobType.PUTAWAY);
            //托盘暂时先放入虚拟货位
//            j.setToLocation(vl);
            //从httpSession中获取操作员名字
            j.setCreateUser(userName);
            //设置作业创建时间
            j.setCreateDate(new Date());
            JobDetail jobDetail = new JobDetail();
            jobDetail.setInventory(i);
            jobDetail.setQty(i.getQty());
            j.addJobDetail(jobDetail);
            session.save(j);
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg("入库成功");
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("其他错误，请联系软件部");
            e.printStackTrace();

        }
        return httpMessage;

    }

    public HttpMessage getInventorys(int currentPage, String locationNo, String barcode, String skuCode) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Map<String, Object> map = new HashMap<>();
            StringBuffer hql = new StringBuffer("select new com.wms.vo.InventoryExcelVo(l.locationNo,c.barcode,i.skuCode,i.qty) from Inventory i,Container c,Location l where i.container.id=c.id and c.location.id=l.id");
            if (StringUtils.isNotBlank(locationNo)) {
                hql.append(" and l.locationNo=:locationNo");
                map.put("locationNo", locationNo);
            }
            if (StringUtils.isNotBlank(barcode)) {
                hql.append(" and c.barcode=:barcode");
                map.put("barcode", barcode);
            }
            if (StringUtils.isNotBlank(skuCode)) {
                hql.append(" and i.skuCode=:skuCode");
                map.put("skuCode", skuCode);
            }
            Query query = session.createQuery(hql.toString());
            Query countQuery = session.createQuery("select count(*) " + hql.substring(hql.indexOf("from Inventory"), hql.length()));
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
                countQuery.setParameter(entry.getKey(), entry.getValue());
            }
            List<InventoryExcelVo> list = query.setFirstResult((currentPage - 1) * 10).setMaxResults(10).list();
            long total = (long) countQuery.uniqueResult();
            map.clear();
            map.put("total", total);
            map.put("data", list);
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg(map);
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了。");
            e.printStackTrace();
        }
        return httpMessage;
    }

    //出库
    public HttpMessage outAreaSet(String skuCode, String station, int j) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            String sql = " from Location l where l.abnormal = false and l.retrievalRestricted = false and exists (select 1 from Container c where c.reserved = false and c.location.id = l.id and exists (select 1 from Inventory i where i.container.id = c.id and i.skuCode =:skuCode)) order by seq";

            Query query = session.createQuery(sql).setString("skuCode", skuCode);
            List<Location> list = query.list();
            if (list.size() > 0) {
                Location location = list.get(0);
                location.setRetrievalRestricted(true);
                //获取容器Container
                Container container = location.getContainers().iterator().next();
                container.setReserved(true);
                session.save(container);

                //创建一条job
                Job job = new Job();
                job.setType(AsrsJobType.RETRIEVAL);
                String mcKey = Mckey.getNext();
                job.setMcKey(mcKey);
                job.setContainer(container);
                job.setFromLocation(location);
                job.setToStation(station);
                job.setStatus(AsrsJobStatus.WAITING);
                job.setCreateDate(new Date());

                Collection<Inventory> inventories = container.getInventories();
                for (Inventory inventory : inventories) {

//                    inventory.setAvailableQty(0);
                    session.save(inventory);

                    JobDetail jobDetail = new JobDetail();
                    jobDetail.setJob(job);
                    jobDetail.setQty(inventory.getQty());
                    jobDetail.setInventory(inventory);
                    session.save(jobDetail);

                    job.addJobDetail(jobDetail);
                }
                session.save(job);


                //创建FromLocation对象
                FromLocation fromLocation = new FromLocation();
                fromLocation.setRack(location.getLocationNo());
                fromLocation.setX(String.valueOf(location.getBank()));
                fromLocation.setY(String.valueOf(location.getBay()));
                fromLocation.setZ(String.valueOf(location.getLevel()));
                fromLocation.setMHA(station);
                StUnit stUnit = new StUnit();

                stUnit.setStUnitID(container.getBarcode());
                //创建TransportOrderDA对象
                TransportOrderDA transportOrderDA = new TransportOrderDA();
                transportOrderDA.setTransportType(TransportType.RETRIEVAL);
                transportOrderDA.setFromLocation(fromLocation);
                transportOrderDA.setStUnit(stUnit);

                //创建sender对象
                Sender sender = new Sender();
                sender.setDivision(XMLConstant.COM_DIVISION);


                RefId refId = new RefId();
                refId.setReferenceId(mcKey);

                //创建ControlArea对象
                ControlArea controlArea = new ControlArea();
                controlArea.setRefId(refId);
                controlArea.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                controlArea.setSender(sender);

                //wms发送给wcs的运输命令
                TransportOrder transportOrder = new TransportOrder();
                transportOrder.setDataArea(transportOrderDA);
                transportOrder.setControlArea(controlArea);

                Envelope envelope = new Envelope();
                envelope.setTransportOrder(transportOrder);

                Transaction.commit();
                XMLUtil.sendEnvelope(envelope);

                httpMessage.setSuccess(true);
                httpMessage.setMsg(String.format("编号为%s的货物第%d次出库任务生成成功", skuCode, j));
            } else {

                Transaction.rollback();
                httpMessage.setSuccess(false);
                httpMessage.setMsg("库存不足");
                return httpMessage;
            }


        } catch (Exception e) {
            Transaction.rollback();

            httpMessage.setSuccess(false);
            httpMessage.setMsg(String.format("编号为%s的货物第%d次出库任务生成失败", skuCode, j));

            e.printStackTrace();

        }

        return httpMessage;
    }

    public HttpMessage generateExcelExportFile(String locationNo, String barcode, String skuCode) {
        HttpMessage message = new HttpMessage();
        List<InventoryExcelVo> list = new ArrayList();
        try {

            Transaction.begin();
            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Inventory.class);
            Criteria conCri = null;
            if (StringUtils.isNotBlank(barcode)) {
                conCri = criteria.createCriteria(Inventory.__CONTAINER);
                conCri.add(Restrictions.eq(Container.__BARCODE, barcode));
            }
            if (StringUtils.isNotBlank(locationNo)) {
                if (conCri == null)
                    conCri = criteria.createCriteria(Inventory.__CONTAINER);
                Criteria locCri = conCri.createCriteria(Container.__LOCATION);
                locCri.add(Restrictions.eq(Location.__LOCATIONNO, locationNo));
            }
            if (StringUtils.isNotBlank(skuCode)) {
                Criteria skuCri = criteria.createCriteria(Inventory.__SKUCODE);
                skuCri.add(Restrictions.eq(Sku.__SKUCODE,skuCode));
            }
            criteria.addOrder(Order.asc(Inventory.__ID));
            List<Inventory> inventories = criteria.list();
            InventoryExcelVo ivo;
            for (Inventory inv : inventories) {
                ivo = new InventoryExcelVo();
                ivo.setBarcode(inv.getContainer().getBarcode());
                ivo.setSkuCode(inv.getSku().getSkuCode());
                ivo.setLocationNo(inv.getContainer().getLocation().getLocationNo());
                ivo.setQty(inv.getQty());
                list.add(ivo);
            }
            Workbook hssfWorkbook = ExcelExportUtils.generateExcelFile(generateExcelExportParams(), list);
            message.setMsg(hssfWorkbook);
            message.setSuccess(true);

            Transaction.commit();

        } catch (Exception e) {
            Transaction.rollback();

            message.setSuccess(false);
            message.setMsg("导出异常");

            e.printStackTrace();

        }
        return message;

    }

    private List<ExcelExportParam> generateExcelExportParams() {
        List<ExcelExportParam> list = new ArrayList<ExcelExportParam>();
        list.add(new ExcelExportParam("inventory.locationNo", "locationNo"));
        list.add(new ExcelExportParam("inventory.barcode", "barcode"));
        list.add(new ExcelExportParam("inventory.skuCode", "skuCode"));
        list.add(new ExcelExportParam("inventory.qty", "qty"));
        return list;
    }

}
