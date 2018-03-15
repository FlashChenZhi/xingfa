package com.thread;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.ReasonCode;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.domain.Station;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.Receiver;
import com.domain.XMLbean.XMLList.ControlArea.RefId;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.domain.XMLbean.XMLList.DataArea.StUnit;
import com.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.domain.XMLbean.XMLList.MovementReport;
import com.domain.consts.xmlbean.XMLConstant;
import com.thread.blocks.StationBlock;
import com.util.common.Const;
import com.util.common.JedisUtil;
import com.util.common.StringUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by van on 2017/6/15.
 */
public class SendReportThread {
    public static void main(String[] args) {

        while (true) {
            try {

                Transaction.begin();

                Query stQ = HibernateUtil.getCurrentSession().createQuery("from Station where type=:stype");
                stQ.setParameter("stype", AsrsJobType.RETRIEVAL);
                List<Station> stations = stQ.list();
                for (Station station : stations) {

                    Query bq = HibernateUtil.getCurrentSession().createQuery("from  StationBlock where stationNo=:stNo");
                    bq.setParameter("stNo", station.getStationNo());

                    List<StationBlock> blocks = bq.list();

                    for (StationBlock stationBlock : blocks) {

                        if (StringUtils.isNotEmpty(stationBlock.getMcKey())) {
                            //查看出库作业
                            AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(stationBlock.getMcKey());

                            if (asrsJob != null && !asrsJob.isSendReport()) {

                                Sender sd = new Sender();
                                sd.setDivision(XMLConstant.COM_DIVISION);

                                Receiver receiver = new Receiver();
                                receiver.setDivision(XMLConstant.WMS_DIVISION);
                                RefId ri = new RefId();
                                ri.setReferenceId(asrsJob.getWmsMckey());

                                ControlArea ca = new ControlArea();
                                ca.setSender(sd);
                                ca.setReceiver(receiver);
                                ca.setRefId(ri);
                                ca.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                                Location location = Location.getByLocationNo(asrsJob.getFromLocation());
                                FromLocation fromLocation = new FromLocation();
                                fromLocation.setMHA(asrsJob.getFromStation());
                                List<String> rack = new ArrayList<>(3);
                                rack.add(String.valueOf(location.getBank()));
                                rack.add(String.valueOf(location.getBay()));
                                rack.add(String.valueOf(location.getLevel()));
                                fromLocation.setRack(rack);

                                ToLocation toLocation = new ToLocation();
                                List<String> list = new ArrayList<>(3);
                                list.add("");
                                list.add("");
                                list.add("");
                                toLocation.setMHA("");
                                toLocation.setRack(list);


                                //创建MovementReportDA数据域对象
                                MovementReportDA mrd = new MovementReportDA();
                                mrd.setReasonCode(ReasonCode.RETRIEVALFINISHED);
                                mrd.setStUnitId(asrsJob.getBarcode());
                                mrd.setFromLocation(fromLocation);
                                mrd.setToLocation(toLocation);
                                //创建MovementReport响应核心对象
                                MovementReport mr = new MovementReport();
                                mr.setControlArea(ca);
                                mr.setDataArea(mrd);
                                //将MovementReport发送给wms
                                Envelope el = new Envelope();
                                el.setMovementReport(mr);
                                XMLUtil.sendEnvelope(el);

                                //订单已发送
                                asrsJob.setSendReport(true);
                            }
                        }
                    }
                }

                Transaction.commit();

            } catch (Exception e) {
                e.printStackTrace();
                Transaction.rollback();
            } finally {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
