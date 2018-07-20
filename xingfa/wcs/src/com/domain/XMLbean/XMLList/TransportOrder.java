package com.domain.XMLbean.XMLList;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.*;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.Receiver;
import com.domain.XMLbean.XMLList.ControlArea.RefId;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.AcceptTransportOrderDA;
import com.domain.XMLbean.XMLList.DataArea.DAList.TransportOrderDA;
import com.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.domain.XMLbean.XMLProcess;
import com.domain.consts.xmlbean.XMLConstant;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thread.blocks.*;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:17
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "XINGFA.TransportOrderLog")
public class TransportOrder extends XMLProcess {

    @XStreamAlias("version")
    @XStreamAsAttribute
    private String version = XMLConstant.COM_VERSION;

    @Column(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private TransportOrderDA dataArea;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ControlAreaID", updatable = true)
    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TransportOrderDAID", updatable = true)
    public TransportOrderDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(TransportOrderDA dataArea) {
        this.dataArea = dataArea;
    }

    @XStreamOmitField
    private int id;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public void execute() throws Exception {
        AsrsJob asrsJob = new AsrsJob();
        Session session = HibernateUtil.getCurrentSession();
        List<AsrsJob> asrsJobs = session.createCriteria(AsrsJob.class)
                .add(Restrictions.eq(AsrsJob._WMSMCKEY, controlArea.getRefId().getReferenceId())).list();
        if (asrsJobs.isEmpty()) {
            System.out.println("接受任务");

            Query jq = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type =:tp1 or type =:tp2");
            jq.setParameter("tp1", AsrsJobType.RECHARGED);
            jq.setParameter("tp2", AsrsJobType.RECHARGEDOVER);
            List<AsrsJob> jobs = jq.list();
            if (!jobs.isEmpty()) {
                throw new Exception("存在充电任务");
            }

            if (AsrsJobType.PUTAWAY.equals(dataArea.getTransportType()) || AsrsJobType.CHECKINSTORAGE.equals(dataArea.getTransportType()) ) {

                String fromStation = dataArea.getFromLocation().getMHA();

                Station station = Station.getStation(fromStation);
                if (!station.getMode().equals(AsrsJobType.PUTAWAY)) {
                    throw new Exception("站台不是入库站台");
                }

                StationBlock stationBlock = StationBlock.getByStationNo(fromStation);
                asrsJob.setFromStation(stationBlock.getBlockNo());
                List<String> locationS = dataArea.getToLocation().getRack();

                if (StringUtils.isNotEmpty(stationBlock.getMcKey())) {
                    throw new Exception("入库站台有任务");
                }

                String blockNo = dataArea.getToLocation().getMHA();
                Srm srm = (Srm) Block.getByBlockNo(blockNo);

                Location location = Location.getByBankBayLevel(Integer.parseInt(locationS.get(0)), Integer.parseInt(locationS.get(1)), Integer.parseInt(locationS.get(2)));

                if (location == null) {
                    throw new Exception("货位不存在");
                }

                if (stationBlock.getLoad() == null || stationBlock.getLoad().equals("0")) {
                    throw new Exception("入库站台无载荷");
                }

                location.setReserved(true);
                //入库类型,或抽检入库类型
                asrsJob.setType(dataArea.getTransportType());

                asrsJob.setToLocation(location.getLocationNo());
                stationBlock.setMcKey(controlArea.getRefId().getReferenceId());
                stationBlock.setWaitingResponse(false);
                session.saveOrUpdate(stationBlock);


                asrsJob.setToStation(srm.getBlockNo());
                stationBlock.setLoad("0");
                asrsJob.setWareHouse(srm.getWareHouse());

            } else if (AsrsJobType.RETRIEVAL.equals(dataArea.getTransportType()) || AsrsJobType.CHECKOUTSTORAGE.equals(dataArea.getTransportType())) {
                //抽检出库和出库其实是一样的
                List<String> locationS = dataArea.getFromLocation().getRack();

                String blockNo = dataArea.getFromLocation().getMHA();
                Srm srm = (Srm) Block.getByBlockNo(blockNo);

                Location location = Location.getByBankBayLevel(Integer.parseInt(locationS.get(0)), Integer.parseInt(locationS.get(1)), Integer.parseInt(locationS.get(2)));

                asrsJob.setFromLocation(location.getLocationNo());
                String toStation = dataArea.getToLocation().getMHA();

                Station station = Station.getStation(toStation);
                if (!station.getMode().equals(AsrsJobType.RETRIEVAL)) {
                    throw new Exception("站台不是出库站台");
                }

                StationBlock stationBlock = StationBlock.getByStationNo(toStation);
                asrsJob.setToStation(stationBlock.getBlockNo());
                //修改为传来的type
                asrsJob.setType(dataArea.getTransportType());

                asrsJob.setFromStation(srm.getBlockNo());
                asrsJob.setWareHouse(srm.getWareHouse());

            }else if (AsrsJobType.LOCATIONTOLOCATION.equals(dataArea.getTransportType())||AsrsJobType.BACK_PUTAWAY.equals(dataArea.getTransportType())) {
                //移库
                List<String> locationS = dataArea.getFromLocation().getRack();
                List<String> locationT = dataArea.getToLocation().getRack();


                Location location = Location.getByBankBayLevel(Integer.parseInt(locationS.get(0)), Integer.parseInt(locationS.get(1)), Integer.parseInt(locationS.get(2)));
                Location tolocation = Location.getByBankBayLevel(Integer.parseInt(locationT.get(0)), Integer.parseInt(locationT.get(1)), Integer.parseInt(locationT.get(2)));

                asrsJob.setFromLocation(location.getLocationNo());
                asrsJob.setToLocation(tolocation.getLocationNo());

                String blockNo = dataArea.getFromLocation().getMHA();
                Srm fsrm = (Srm) Block.getByBlockNo(blockNo);

                String toStation = dataArea.getToLocation().getMHA();
                Srm tsrm = (Srm) Block.getByBlockNo(toStation);
                /*Station station = Station.getStation(toStation);
                if (!station.getMode().equals(AsrsJobType.RETRIEVAL)) {
                    throw new Exception("站台不是出库站台");
                }*/
                //StationBlock stationBlock = StationBlock.getByStationNo(toStation);
                asrsJob.setToStation(tsrm.getBlockNo());
                //修改为传来的type
                asrsJob.setType(dataArea.getTransportType());

                asrsJob.setFromStation(fsrm.getBlockNo());
                asrsJob.setWareHouse(fsrm.getWareHouse());

            }
            asrsJob.setPriority(1);
            asrsJob.setIndicating(false);
            asrsJob.setWmsMckey(controlArea.getRefId().getReferenceId());
            asrsJob.setMcKey(controlArea.getRefId().getReferenceId());
            asrsJob.setBarcode(dataArea.getStUnit().getStUnitID());
            asrsJob.setStatus(AsrsJobStatus.RUNNING);
            asrsJob.setStatusDetail(AsrsJobStatusDetail.WAITING);
            asrsJob.setGenerateTime(new Date());
            asrsJob.setSendReport(false);
            session.save(asrsJob);
        }
        sendReport("00");

    }

    public void sendReport(String result) throws Exception {
        AcceptTransportOrder acceptTransportOrder = new AcceptTransportOrder();


        ControlArea controlArea = new ControlArea();
        RefId refId = new RefId();
        refId.setReferenceId(this.controlArea.getRefId().getReferenceId());
        controlArea.setRefId(refId);
        controlArea.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));


        Sender sender = new Sender();
        if(this.controlArea.getReceiver()!=null) {
            sender.setDivision(this.controlArea.getReceiver().getDivision());
        }
        controlArea.setSender(sender);

        Receiver receiver = new Receiver();
        receiver.setDivision(this.controlArea.getSender().getDivision());
        controlArea.setReceiver(receiver);

        AcceptTransportOrderDA da = new AcceptTransportOrderDA();
        da.setInformation(result);
        da.setStUnitID(this.dataArea.getStUnitId());
        da.setRouteChange(this.dataArea.getRouteChange());


        acceptTransportOrder.setDataArea(da);
        acceptTransportOrder.setControlArea(controlArea);

        //将MovementReport发送给wms
        Envelope el = new Envelope();
        el.setAcceptTransportOrder(acceptTransportOrder);

        XMLMessage xmlMessage = new XMLMessage();
        xmlMessage.setStatus("1");
        xmlMessage.setRecv("WMS");
        xmlMessage.setMessageInfo(XMLUtil.getSendXML(el));
        HibernateUtil.getCurrentSession().save(xmlMessage);


    }
}

