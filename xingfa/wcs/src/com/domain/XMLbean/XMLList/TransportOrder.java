package com.domain.XMLbean.XMLList;

import com.AllBinding.blocks.Block;
import com.AllBinding.blocks.Crane;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.StatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.DataArea.DAList.TransportOrderDA;
import com.domain.XMLbean.XMLProcess;
import com.domain.consts.xmlbean.XMLConstant;
import com.AllBinding.blocks.StationBlock;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.common.DateTimeFormatter;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:17
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "TransportOrder")
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
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "MOVEMENTREPORT_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public void execute() {
        AsrsJob asrsJob = new AsrsJob();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            if (AsrsJobType.PUTAWAY.equals(dataArea.getTransportType())) {
                String fromStation = dataArea.getToLocation().getMHA();
                StationBlock stationBlock = StationBlock.getByStationNo(fromStation);
                asrsJob.setFromStation(stationBlock.getBlockNo());
                asrsJob.setToLocation(dataArea.getToLocation().getRack());
                asrsJob.setType(AsrsJobType.PUTAWAY);
                asrsJob.setStatusDetail(StatusDetail.START);
                stationBlock.setMcKey(controlArea.getRefId().getReferenceId());
                stationBlock.setWaitingResponse(false);
                String toStation = (String) session.createQuery("select r.toStation from Route r where r.fromStation=:stationNo")
                        .setString("stationNo", stationBlock.getBlockNo()).setMaxResults(1).uniqueResult();
                Block toBlock = Block.getByBlockNo(toStation);
                if (toBlock instanceof Crane) {
                    asrsJob.setToStation(toStation);
                } else {
                    Location location = Location.getByLocationNo(asrsJob.getToLocation());
                    asrsJob.setToStation(Crane.getCrane().getBlockNo());
                }

                //不绑定开始
//                String fromStation = dataArea.getToLocation().getMHA();
//                StationBlock stationBlock = StationBlock.getByStationNo(fromStation);
//                asrsJob.setFromStation(stationBlock.getBlockNo());
//                asrsJob.setToLocation(dataArea.getToLocation().getRack());
//                //入库类型
//                asrsJob.setType(AsrsJobType.PUTAWAY);
//                stationBlock.setMcKey(controlArea.getRefId().getReferenceId());
//                stationBlock.setWaitingResponse(false);
//                stationBlock.setLoaded(true);
//                session.saveOrUpdate(stationBlock);
//                Location location = Location.getByLocationNo(asrsJob.getToLocation());
//                List<MCar> mCars = MCar.getToMCar(location.getAisle(), location.getLevel());
//                List<Crane> cranes = Crane.getToCrane(location.getAisle());
//                List<String> blockNos = new ArrayList<String>();
//                for(MCar mCar : mCars){
//                    blockNos.add(mCar.getBlockNo());
//                }
//                for(Crane crane : cranes) {
//                    blockNos.add(crane.getBlockNo());
//                }
//                List<Route> routes = session.createQuery("from Route r where r.fromStation=:fromStation and r.toStation in (:blockNos) and r.type=:type")
//                        .setParameterList("blockNos", blockNos)
//                        .setString("fromStation", asrsJob.getFromStation())
//                        .setString("type", AsrsJobType.PUTAWAY).list();
//                asrsJob.setToStation(routes.get(0).getToStation());
                //不绑定结束
            } else if (AsrsJobType.RETRIEVAL.equals(dataArea.getTransportType())) {
                asrsJob.setFromLocation(dataArea.getFromLocation().getRack());
                String toStation = dataArea.getFromLocation().getMHA();
                StationBlock stationBlock = StationBlock.getByStationNo(toStation);
                asrsJob.setToStation(stationBlock.getBlockNo());
                asrsJob.setType(AsrsJobType.RETRIEVAL);
                asrsJob.setStatusDetail(StatusDetail.WAITING);
                String fromStation = (String) session.createQuery("select r.fromStation from Route r where r.toStation=:stationNo")
                        .setString("stationNo", stationBlock.getBlockNo()).setMaxResults(1).uniqueResult();
                Block fromBlock = Block.getByBlockNo(fromStation);
                if (fromBlock instanceof Crane) {
                    asrsJob.setFromStation(fromStation);
                } else {
                    Location location = Location.getByLocationNo(asrsJob.getToLocation());
                    asrsJob.setFromStation(Crane.getCrane().getBlockNo());
                }
                //不绑定开始
//                asrsJob.setFromLocation(dataArea.getFromLocation().getRack());
//                String toStation = dataArea.getFromLocation().getMHA();
//                StationBlock stationBlock = StationBlock.getByStationNo(toStation);
//                asrsJob.setToStation(stationBlock.getBlockNo());
//                asrsJob.setType(AsrsJobType.RETRIEVAL);
//                Location location = Location.getByLocationNo(asrsJob.getFromLocation());
//                List<MCar> mCars = MCar.getFromMCar(location.getAisle(), location.getLevel());
//                List<Crane> cranes = Crane.getFromCrane(location.getAisle());
//                List<String> blockNos = new ArrayList<String>();
//                for(MCar mCar : mCars){
//                    blockNos.add(mCar.getBlockNo());
//                }
//                for(Crane crane : cranes) {
//                    blockNos.add(crane.getBlockNo());
//                }
//                List<Route> routes = session.createQuery("from Route r where r.toStation=:toStation and r.fromStation in (:blockNos)")
//                        .setParameterList("blockNos", blockNos).setString("toStation", asrsJob.getToStation()).list();
//                asrsJob.setFromStation(routes.get(0).getFromStation());
                //不绑定结束
            }
            asrsJob.setMcKey(controlArea.getRefId().getReferenceId());
            asrsJob.setBarcode(dataArea.getStUnit().getStUnitID());
            asrsJob.setStatus(AsrsJobStatus.RUNNING);
            asrsJob.setGenerateTime(new Date());
            session.save(asrsJob);
            System.out.println(asrsJob.getMcKey()+"创建时间：" + new DateTimeFormatter().format(new Date()));
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }
    }
}

