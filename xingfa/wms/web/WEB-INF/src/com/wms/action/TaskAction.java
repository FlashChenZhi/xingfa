package com.wms.action;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.StationMode;
import com.wms.domain.*;
import com.wms.vo.BeginTask;
import com.wms.vo.PreserveTaskDTO;
import com.asrs.xml.util.XMLUtil;
import com.asrs.domain.XMLbean.Envelope;
import com.asrs.domain.XMLbean.XMLList.CancelTransportOrder;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.ControlArea.RefId;
import com.asrs.domain.XMLbean.XMLList.ControlArea.Sender;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.CancelTransportOrderDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.TransportModeChangeDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.WorkStartEndDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.StUnit;
import com.asrs.domain.XMLbean.XMLList.TransportModeChange;
import com.asrs.domain.XMLbean.XMLList.WorkStartEnd;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.util.common.HttpMessage;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/10/25.
 */
@Controller
@RequestMapping("/task")
public class TaskAction {
    /**
     * 作业开始/维护 再显示
     *
     * @return
     */
    @RequestMapping("/getRemainTask.do")
    @ResponseBody
    public HttpMessage getRemainTask() {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Session session = HibernateUtil.getCurrentSession();
            Transaction.begin();
            Wcs wcs = (Wcs) session.createQuery("from Wcs").uniqueResult();
            Long remainTaskNumber = (Long) session.createQuery("select count(id) from Job where status !=:done")
                    .setString("done", AsrsJobStatus.DONE).uniqueResult();
            BeginTask beginTask = new BeginTask();
            beginTask.setWcsNo(wcs.getWcsName());
            beginTask.setSystemState(wcs.getStatus());
            beginTask.setRemainTaskNumber(remainTaskNumber);

            Transaction.commit();

            httpMessage.setSuccess(true);
            httpMessage.setMsg(beginTask);

        } catch (Exception e) {
            Transaction.rollback();

            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了");
            e.printStackTrace();
        }
        return httpMessage;
    }

    /**
     * 作业开始/维护 作业开始
     *
     * @param beginTask
     * @return
     */
    @RequestMapping("/startTask.do")
    @ResponseBody
    public HttpMessage startTask(BeginTask beginTask) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Sender sender = new Sender();
            sender.setDivision(XMLConstant.COM_DIVISION);
            sender.setConfirmation(XMLConstant.COM_CONFIRMATION);

            RefId refId = new RefId();
            Transaction.begin();
            refId.setReferenceId(Mckey.getNext());
            Transaction.commit();
            ControlArea controlArea = new ControlArea();
            controlArea.setSender(sender);
            controlArea.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            WorkStartEndDA workStartEndDA = new WorkStartEndDA();
            workStartEndDA.setRequestId("");//TODO
            workStartEndDA.setType("start");//TODO

            WorkStartEnd workStartEnd = new WorkStartEnd();
            workStartEnd.setControlArea(controlArea);
            workStartEnd.setDataArea(workStartEndDA);
            workStartEnd.setDate(new Date());

            Envelope envelope = new Envelope();
            envelope.setWorkStartEnd(workStartEnd);

            XMLUtil.sendEnvelope(envelope);
            httpMessage.setSuccess(true);
            httpMessage.setMsg("作业已开始");
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("作业出现异常");
            e.printStackTrace();
        }
        System.out.println(beginTask.getSystemState());
        return httpMessage;
    }

    /**
     * 作业开始/维护 作业结束
     *
     * @return
     */
    @RequestMapping("/endTask.do")
    @ResponseBody
    public HttpMessage endTask() {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Sender sender = new Sender();
            sender.setDivision(XMLConstant.COM_DIVISION);
            sender.setConfirmation(XMLConstant.COM_CONFIRMATION);

            RefId refId = new RefId();
            Transaction.begin();
            refId.setReferenceId(Mckey.getNext());
            Transaction.commit();
            ControlArea controlArea = new ControlArea();
            controlArea.setSender(sender);
            controlArea.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            WorkStartEndDA workStartEndDA = new WorkStartEndDA();
            workStartEndDA.setRequestId("");//TODO
            workStartEndDA.setType("end");//TODO

            WorkStartEnd workStartEnd = new WorkStartEnd();
            workStartEnd.setControlArea(controlArea);
            workStartEnd.setDataArea(workStartEndDA);
            workStartEnd.setDate(new Date());

            Envelope envelope = new Envelope();
            envelope.setWorkStartEnd(workStartEnd);

            XMLUtil.sendEnvelope(envelope);
            httpMessage.setSuccess(true);
            httpMessage.setMsg("作业已结束");
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("作业出现异常");
            e.printStackTrace();
        }
        return httpMessage;
    }

    /**
     * 作业维护 查询
     *
     * @param currentPage
     * @param containerNo
     * @param station
     * @param jobType
     * @return
     */
    @RequestMapping("/preserve.do")
    @ResponseBody
    public HttpMessage preserveTask(int currentPage, String fromLocation, String toLocation, String containerNo, String station, String jobType) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Map<String, Object> map = new HashMap<>();
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Job.class);
            if (StringUtils.isNotBlank(fromLocation)) {
                Criteria locCri = criteria.createCriteria(Job.__FROMLOCATION);
                locCri.add(Restrictions.eq(Location.__LOCATIONNO, fromLocation));
            }
            if (StringUtils.isNotBlank(toLocation)) {
                Criteria locCri = criteria.createCriteria(Job.__TOLOCATION);
                locCri.add(Restrictions.eq(Location.__LOCATIONNO, toLocation));
            }
            if (StringUtils.isNotBlank(containerNo)) {
                Criteria conCri = criteria.createCriteria(Job.__CONTAINER);
                conCri.add(Restrictions.eq(Container.__BARCODE, containerNo));
            }
            if (StringUtils.isNotBlank(jobType)) {
                if (StringUtils.isNotBlank(station)) {
                    if (jobType.equals(AsrsJobType.PUTAWAY)) {
                        criteria.add(Restrictions.eq(Job.__FROMSTATION, station));
                    } else {
                        criteria.add(Restrictions.eq(Job.__TOSTATION, station));
                    }
                }
                criteria.add(Restrictions.eq(Job.__TYPE, jobType));
            } else {
                if (StringUtils.isNotBlank(station)) {
                    Disjunction dis = Restrictions.disjunction();
                    dis.add(Restrictions.eq(Job.__FROMSTATION, station));
                    dis.add(Restrictions.eq(Job.__TOSTATION, station));
                    criteria.add(dis);
                }
            }

            criteria.addOrder(Order.asc(Job.__ID));
            //获取总行数
            Long total = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
//            获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult((currentPage - 1) * 10);
            criteria.setMaxResults(10);
            criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
            List<Job> jobs = criteria.list();


            List<PreserveTaskDTO> dtoList = new ArrayList<>();
            PreserveTaskDTO dto;
            for (Job job : jobs) {
                dto = new PreserveTaskDTO();
                dto.setBarcode(job.getContainer().getBarcode());
                dto.setJobStatus(AsrsJobStatus.map.get(job.getStatus()));
                dto.setJobType(AsrsJobType.map.get(job.getType()));
                dto.setMcKey(job.getMcKey());
                if (job.getFromStation() != null)
                    dto.setFromStation(job.getFromStation());
                if (job.getToStation() != null)
                    dto.setToStation(job.getToStation());
                if (job.getFromLocation() != null)
                    dto.setFromLocation(job.getFromLocation().getLocationNo());
                if (job.getToLocation() != null)
                    dto.setToLocation(job.getToLocation().getLocationNo());
                dtoList.add(dto);
            }
            map.put("total", total);
            map.put("data", dtoList);
            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg(map);
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了");
            e.printStackTrace();
        }
        return httpMessage;
    }

    /**
     * 作业维护 完成/取消
     *
     * @param preserveTaskDTO
     * @param operation
     * @return
     */
    @RequestMapping("/finishOrCancel.do")
    @ResponseBody
    public HttpMessage finishOrCancelTask(PreserveTaskDTO preserveTaskDTO, String operation) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Job job = Job.getByMcKey(preserveTaskDTO.getMcKey());
            Container container = job.getContainer();
            Location location;
            if ("完成".equals(operation)) {
                if (AsrsJobType.PUTAWAY.equals(job.getType())) {
                    location = job.getToLocation();
                    location.setReserved(false);
                    location.setEmpty(false);
                    container.setLocation(location);
                    session.saveOrUpdate(container);
                } else {
                    location = job.getFromLocation();
                    location.setEmpty(true);
                    session.delete(container);
                }
                session.saveOrUpdate(location);
            } else {
                if (AsrsJobType.PUTAWAY.equals(job.getType())) {
                    session.delete(container);
                } else {
                    location = job.getFromLocation();
                    location.setRetrievalRestricted(false);
                    session.saveOrUpdate(location);
                    container.setReserved(false);
                    session.saveOrUpdate(container);
                }

            }
            session.delete(job);

            Sender sender = new Sender();
            sender.setDivision(XMLConstant.COM_DIVISION);
            sender.setConfirmation(XMLConstant.COM_CONFIRMATION);

            RefId refId = new RefId();
            refId.setReferenceId(job.getMcKey());

            ControlArea controlArea = new ControlArea();
            controlArea.setSender(sender);
            controlArea.setRefId(refId);
            controlArea.setCreationDateTime(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));

            StUnit stUnit = new StUnit();
            stUnit.setStUnitID(preserveTaskDTO.getBarcode());

            CancelTransportOrderDA cancelTransportOrderDA = new CancelTransportOrderDA();
            cancelTransportOrderDA.setStUnit(stUnit);

            CancelTransportOrder cancelTransportOrder = new CancelTransportOrder();
            cancelTransportOrder.setControlArea(controlArea);
            cancelTransportOrder.setDataArea(cancelTransportOrderDA);

            Envelope envelope = new Envelope();
            envelope.setCancelTransportOrder(cancelTransportOrder);
            XMLUtil.sendEnvelope(envelope);

            Transaction.commit();
            httpMessage.setSuccess(true);
            httpMessage.setMsg(String.format("mcKey为%s的任务已成功%s。", preserveTaskDTO.getMcKey(), operation));
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg(String.format("mcKey为%s的任务已%s失败。", preserveTaskDTO.getMcKey(), operation));
            e.printStackTrace();
        }
        return httpMessage;
    }

    /**
     * 站台模式 设定
     *
     * @param station
     * @param model
     * @return
     */
    @RequestMapping("/stationModel.do")
    @ResponseBody
    public HttpMessage stationModel(String station, String model) {
        HttpMessage httpMessage = new HttpMessage();
        Transaction.begin();
        try {
            Session session = HibernateUtil.getCurrentSession();
            Station station1 = Station.getStation(station);
            if (model.equals(station1.getMode())) {
                httpMessage.setSuccess(true);
                httpMessage.setMsg(String.format("已经切换为%s模式", StationMode.MAP.get(model)));
            } else {
//                station1.setOldMode(station1.getMode());
//                station1.setMode(model);
//                session.saveOrUpdate(station);

                Sender sender = new Sender();
                sender.setDivision(XMLConstant.COM_DIVISION);

                ControlArea controlArea = new ControlArea();
                controlArea.setSender(sender);
                controlArea.setCreationDateTime(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));

                TransportModeChangeDA transportModeChangeDA = new TransportModeChangeDA();
                transportModeChangeDA.setMha(station);
                transportModeChangeDA.setTransportType(model);

                TransportModeChange transportModeChange = new TransportModeChange();
                transportModeChange.setControlArea(controlArea);
                transportModeChange.setDataArea(transportModeChangeDA);

                Envelope envelope = new Envelope();
                envelope.setTransportModeChange(transportModeChange);
                XMLUtil.sendEnvelope(envelope);

                Transaction.commit();
                httpMessage.setSuccess(true);
                httpMessage.setMsg(String.format("正在切换为%s模式", StationMode.MAP.get(model)));
            }
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg(String.format("切换为%s模式失败", StationMode.MAP.get(model)));
            e.printStackTrace();
        }
        return httpMessage;
    }

}
