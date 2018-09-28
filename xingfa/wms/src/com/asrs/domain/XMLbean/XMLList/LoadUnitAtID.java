package com.asrs.domain.XMLbean.XMLList;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.TransportType;
import com.asrs.domain.XMLbean.Envelope;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.ControlArea.Receiver;
import com.asrs.domain.XMLbean.XMLList.ControlArea.RefId;
import com.asrs.domain.XMLbean.XMLList.ControlArea.Sender;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.LoadUnitAtIdDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.TransportOrderDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.asrs.domain.XMLbean.XMLList.DataArea.StUnit;
import com.asrs.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.asrs.domain.XMLbean.XMLProcess;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.asrs.xml.util.XMLUtil;
import com.opple.WebService;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.common.DateFormat;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.domain.AsrsJob;
import com.wms.domain.Location;
import com.wms.domain.blocks.Block;
import com.wms.domain.blocks.Srm;
import com.wms.domain.blocks.StationBlock;
import org.hibernate.*;
import org.hibernate.Query;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "XINGFA.LoadUnitAtID")
public class LoadUnitAtID extends XMLProcess {


    @XStreamAlias("height")
    @XStreamAsAttribute
    private String _height;

    @Column(name = "HEIGHT")
    public String getHeight() {
        return _height;
    }

    public void setHeight(String height) {
        this._height = height;
    }

    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private LoadUnitAtIdDA dataArea;

    @OneToOne(targetEntity = ControlArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ControlAreaID", updatable = true)
    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }

    @OneToOne(targetEntity = LoadUnitAtIdDA.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "LoadUnitAtIdDAID", updatable = true)
    public LoadUnitAtIdDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(LoadUnitAtIdDA dataArea) {
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
    public void execute() {
        Session session = HibernateUtil.getCurrentSession();

        //读码器15位条码，首位字符不要，后面用_补齐，全部去掉。
//        String palletNo = dataArea.getScanDate().substring(1).replaceAll("_", "");
//
//        org.hibernate.Query jobQuery = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where barcode=:code");
//        jobQuery.setParameter("code", palletNo);
//        List<AsrsJob> jobs = jobQuery.list();
//        String stationNo = dataArea.getXMLLocation().getMHA();
//        StationBlock stationBlock = StationBlock.getByStationNo(stationNo);
//
//        Query invQ = HibernateUtil.getCurrentSession().createQuery("from Container c where barcode=:bcode");
//        invQ.setParameter("bcode", palletNo);
//        List<Container> containers = invQ.list();
//        if (!jobs.isEmpty() || !containers.isEmpty()) {
//            SystemLog.error("托盘" + palletNo + "已存在");
//            InMessage.error(stationNo, "托盘" + palletNo + "已存在");
//        } else {
//
//            InventoryView view = WebService.getPutawayInfo(palletNo);
//            if (view != null) {
//                Location newLocation = null;
//
//                String[] positon = stationBlock.getInPostion().split("-");
//
//                InStrategy inStrategy = InStrategy.getInStrategy(view.getSkuCode());
//
//                if (("整托").equals(view.getStatus())) {
//
//                    if (inStrategy != null) {
//                        Srm srm = Srm.getSrmByPosition(inStrategy.getPosition());
//                        if (srm.getStatus().equals(Block.STATUS_RUN))
//                            newLocation = Location.getEmptyLocation(view.getSkuCode(), view.getLotNum(), inStrategy.getPosition(), view.getWhCode());
//                    }
//
//                    if (newLocation == null) {
//                        for (int i = 0; i < positon.length; i++) {
//                            String po = positon[i];
//                            Srm srm = Srm.getSrmByPosition(po);
//                            if (!srm.getStatus().equals(Block.STATUS_RUN))
//                                continue;
//
//                            newLocation = Location.getEmptyLocation(view.getSkuCode(), view.getLotNum(), po, view.getWhCode());
//                            if (newLocation != null) {
//                                newLocation.setReserved(true);
//                                break;
//                            }
//
//                        }
//                    }
//                } else if ("非整托".equals(view.getStatus())) {
//                    //
//                    if (newLocation == null) {
//                        for (int i = 0; i < positon.length; i++) {
//                            String po = positon[i];
//                            Srm srm = Srm.getSrmByPosition(po);
//                            if (!srm.getStatus().equals(Block.STATUS_RUN))
//                                continue;
//
//                            newLocation = Location.getPartPalletLocation();
//                            if (newLocation != null) {
//                                newLocation.setReserved(true);
//                                break;
//                            }
//                        }
//                    }
//
//                } else if ("空托盘".equals(view.getStatus())) {
//
//
//
//                }
//
//
//                if (newLocation == null) {
//                    SystemLog.error("托盘" + palletNo + "找不到合适的货位");
//                    InMessage.error(stationNo, "托盘" + palletNo + "，找不到合适的货位");
//                }else{
//
//                    String mckey = Mckey.getNext();
//                    //开始
//                    //创建ControlArea控制域对象
//                    ControlArea ca = new ControlArea();
//                    Sender sd = new Sender();
//                    sd.setDivision(XMLConstant.COM_DIVISION);
//                    ca.setSender(sd);
//
//                    Receiver rv = new Receiver();
//                    rv.setDivision("WCS1");
//                    ca.setReceiver(rv);
//
//                    RefId ri = new RefId();
//                    ri.setReferenceId(mckey);
//                    ca.setRefId(ri);
//                    ca.setCreationDateTime(new DateFormat().format(new Date(), DateFormat.YYYYMMDDHHMMSS));
//
//                    //创建TransportOrderDA数据域对象
//                    TransportOrderDA toa = new TransportOrderDA();
//
//                    toa.setTransportType(TransportType.PUTAWAY);
//                    ToLocation toLocation = new ToLocation();
//                    Srm srm = Srm.getSrmByPosition(newLocation.getPosition());
//                    toLocation.setMHA(srm.getBlockNo());
//                    List<String> locations = new ArrayList<>();
//                    locations.add(newLocation.getBank() + "");
//                    locations.add(newLocation.getBay() + "");
//                    locations.add(newLocation.getLevel() + "");
//                    toLocation.setRack(locations);
//                    toa.setToLocation(toLocation);
//                    FromLocation fromLocation = new FromLocation();
//                    fromLocation.setMHA(dataArea.getXMLLocation().getMHA());
//                    toa.setFromLocation(fromLocation);
//                    StUnit su = new StUnit();
//                    su.setStUnitID(view.getPalletCode());
//                    toa.setStUnit(su);
//                    toa.setToLocation(toLocation);
//
//                    //创建TransportOrder核心对象
//                    TransportOrder to = new TransportOrder();
//                    to.setControlArea(ca);
//                    to.setDataArea(toa);
//                    //结束
//                    Envelope el = new Envelope();
//                    el.setTransportOrder(to);
//
//                    XMLMessage xmlMessage = new XMLMessage();
//                    xmlMessage.setRecv("WCS");
//                    xmlMessage.setStatus("1");
//                    xmlMessage.setMessageInfo(XMLUtil.getSendXML(el));
//                    HibernateUtil.getCurrentSession().save(xmlMessage);
//
//                    Job job = new Job();
//                    job.setStatus("1");
//                    job.setContainer(palletNo);
//                    job.setToLocation(newLocation);
//                    job.setCreateDate(new Date());
//                    job.setCreateUser("sys");
//                    job.setFromStation(fromLocation.getMHA());
//                    job.setToStation(toLocation.getMHA());
//                    job.setMcKey(mckey);
//                    job.setType(AsrsJobType.PUTAWAY);
//                    job.setSendReport(false);
//
//                    HibernateUtil.getCurrentSession().save(job);
//
//                    InMessage.info(stationNo, view);
//                }
//
//            } else {
//                SystemLog.error("托盘数据" + dataArea.getScanDate() + "不存在");
//                InMessage.error(stationNo, "托盘数据" + dataArea.getScanDate() + "不存在");
//            }
//        }

        String stationNo = dataArea.getXMLLocation().getMHA();
        org.hibernate.Query jobQuery = HibernateUtil.getCurrentSession().createQuery("from Job j where j.fromStation = :station and j.status = :waiting order by j.createDate")
                .setString("station",stationNo)
                .setString("waiting", AsrsJobStatus.WAITING)
                .setMaxResults(1);

        Job j = (Job) jobQuery.uniqueResult();
        if(j == null) {
            SystemLog.error("入库站台" + stationNo + "不存在任务");
            InMessage.error(stationNo, "入库站台" + stationNo + "不存在任务");
        }else{
//            Container container = Container.getByBarcode(j.getContainer());
//            Inventory inventory = container.getInventories().iterator().next();
            InventoryView view = InventoryView.getByPalletNo(j.getContainer());
            Station station = Station.getStation(stationNo);

            Location newLocation=null;

            if(AsrsJobType.PUTAWAY.equals(j.getType())){
                WholeInStorageStrategy wholeInStorageStrategy =WholeInStorageStrategy.getWholeInStorageStrategyById(1);
                if(wholeInStorageStrategy.isStatus()){
                    //若是入库分配货位
                    newLocation = Location.getEmptyLocation(view.getSkuCode(),view.getLotNum(),station.getPosition(),dataArea.getLoadType(),j.getBay(),j.getLevel());
                }else{
                    //若全局入库策略是手动的，则只能入有入库策略的货物
                    if(!(j.getBay()==0 && j.getLevel()==0 )){
                        newLocation = Location.getEmptyLocation(view.getSkuCode(),view.getLotNum(),station.getPosition(),dataArea.getLoadType(),j.getBay(),j.getLevel());
                    }else{
                        j.setError("全局入库策略为手动，不可入没有入库策略的货物");
                    }
                }

            }else if(AsrsJobType.CHECKINSTORAGE.equals(j.getType())){
                //若是抽检入库，则回原位
                newLocation=j.getToLocation();
            }

            String palletNo = j.getContainer();
            if (newLocation == null) {
                SystemLog.error("托盘" + palletNo + "找不到合适的货位");
                InMessage.error(stationNo, "托盘" + palletNo + "，找不到合适的货位");
                if(j.getError()==null){
                    j.setError("托盘" + palletNo + "，找不到合适的货位");
                }
            }else{

                newLocation.setReserved(true);

//                String mckey = Mckey.getNext();
                //开始
                //创建ControlArea控制域对象
                ControlArea ca = new ControlArea();
                Sender sd = new Sender();
                sd.setDivision(XMLConstant.COM_DIVISION);
                ca.setSender(sd);

                Receiver rv = new Receiver();
                rv.setDivision("WCS1");
                ca.setReceiver(rv);

                RefId ri = new RefId();
                ri.setReferenceId(j.getMcKey());
                ca.setRefId(ri);
                ca.setCreationDateTime(new DateFormat().format(new Date(), DateFormat.YYYYMMDDHHMMSS));

                //创建TransportOrderDA数据域对象
                TransportOrderDA toa = new TransportOrderDA();

                if(AsrsJobType.PUTAWAY.equals(j.getType())) {
                    //入库类型
                    toa.setTransportType(TransportType.PUTAWAY);
                }else if(AsrsJobType.CHECKINSTORAGE.equals(j.getType())){
                    //抽检入库类型
                    toa.setTransportType(TransportType.CHECKINSTORAGE);
                }

                ToLocation toLocation = new ToLocation();

                if(AsrsJobType.PUTAWAY.equals(j.getType())) {
                    //入库类型
                    Srm srm = Srm.getSrmByPosition(newLocation.getPosition());
                    toLocation.setMHA(srm.getBlockNo());
                }else if(AsrsJobType.CHECKINSTORAGE.equals(j.getType())) {
                    //抽检入库类型
                    toLocation.setMHA(j.getToStation());
                }

                List<String> locations = new ArrayList<>();
                locations.add(newLocation.getBank() + "");
                locations.add(newLocation.getBay() + "");
                locations.add(newLocation.getLevel() + "");
                toLocation.setRack(locations);
                toa.setToLocation(toLocation);
                FromLocation fromLocation = new FromLocation();
                fromLocation.setMHA(dataArea.getXMLLocation().getMHA());
                toa.setFromLocation(fromLocation);
                StUnit su = new StUnit();
                su.setStUnitID(palletNo);
                toa.setStUnit(su);
                toa.setToLocation(toLocation);

                //创建TransportOrder核心对象
                TransportOrder to = new TransportOrder();
                to.setControlArea(ca);
                to.setDataArea(toa);
                //结束
                Envelope el = new Envelope();
                el.setTransportOrder(to);

                XMLMessage xmlMessage = new XMLMessage();
                xmlMessage.setRecv("WCS");
                xmlMessage.setStatus("1");
                xmlMessage.setMessageInfo(XMLUtil.getSendXML(el));
                HibernateUtil.getCurrentSession().save(xmlMessage);

//                Job job = new Job();
//                job.setStatus("1");
//                job.setContainer(palletNo);
//                job.setToLocation(newLocation);
//                job.setCreateDate(new Date());
//                job.setCreateUser("sys");
//                job.setFromStation(fromLocation.getMHA());
//                job.setToStation(toLocation.getMHA());
//                job.setMcKey(mckey);
//                job.setType(AsrsJobType.PUTAWAY);
//                job.setSendReport(false);
//
//                HibernateUtil.getCurrentSession().save(job);
                if(AsrsJobType.PUTAWAY.equals(j.getType())) {
                    j.setToLocation(newLocation);
                }
                j.setStatus(AsrsJobStatus.RUNNING);

                InMessage.info(stationNo, palletNo,view.getSkuCode());
            }
        }

    }
}
