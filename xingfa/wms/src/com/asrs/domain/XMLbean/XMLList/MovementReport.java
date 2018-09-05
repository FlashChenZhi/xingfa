package com.asrs.domain.XMLbean.XMLList;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.opple.WebService;
import com.wms.domain.*;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.asrs.domain.XMLbean.XMLProcess;
import com.asrs.business.consts.ReasonCode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.hibernate.HibernateUtil;
import com.wms.domain.Location;
import com.wms.domain.blocks.Block;
import com.wms.domain.blocks.StationBlock;
import org.apache.http.client.utils.DateUtils;
import org.hibernate.*;
import org.hibernate.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "XINGFA.MovementReport")
public class MovementReport extends XMLProcess {

    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private MovementReportDA dataArea;

    @OneToOne(targetEntity = ControlArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ControlAreaID", updatable = true)
    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }

    @OneToOne(targetEntity = MovementReportDA.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "MovementReportDAID", updatable = true)
    public MovementReportDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(MovementReportDA dataArea) {
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
        String mcKey = controlArea.getRefId().getReferenceId();
        Job j = Job.getByMcKey(mcKey);
        if (j == null) {
            return;
        }
        if (dataArea.getReasonCode().equals(ReasonCode.PUTAWAYFINISHED) || dataArea.getReasonCode().equals(ReasonCode.CHECKINFINISHED)) {
            Location l = j.getToLocation();
            l.setReserved(false);
            l.setEmpty(false);
            session.update(l);

            Container container = null;

            InventoryView view = InventoryView.getByPalletNo(j.getContainer());


            container = Container.getByBarcode(j.getContainer());

            if (container == null) {
                container = new Container();
                container.setBarcode(j.getContainer());
                container.setLocation(l);
                container.setCreateDate(new Date());
                container.setCreateUser("sys");
                container.setReserved(false);
                HibernateUtil.getCurrentSession().save(container);
            }else{
                if(dataArea.getReasonCode().equals(ReasonCode.CHECKINFINISHED)){
                    //若是抽检入库将container的reserved的设为false
                    container.setReserved(false);
                }
            }
            InventoryLog inventoryLog = new InventoryLog();
            inventoryLog.setQty(BigDecimal.ZERO);
            inventoryLog.setType(InventoryLog.TYPE_IN);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            if (view != null) {
                if (dataArea.getReasonCode().equals(ReasonCode.PUTAWAYFINISHED)){
                    Inventory inventory = new Inventory();
                    inventory.setWhCode(view.getWhCode());
                    inventory.setSkuName(view.getSkuName());
                    inventory.setLotNum(view.getLotNum());
                    inventory.setQty(view.getQty());
                    inventory.setSkuCode(view.getSkuCode());
                    inventory.setContainer(container);
                    inventory.setStoreDate(sdf.format(new Date()));
                    inventory.setStoreTime(sdf2.format(new Date()));

                    j.getJobDetails().iterator().next().setInventory(inventory);

                    HibernateUtil.getCurrentSession().save(inventory);
                    inventoryLog.setQty(inventoryLog.getQty().add(inventory.getQty()));
                    inventoryLog.setSkuCode(inventory.getSkuCode());
                    inventoryLog.setWhCode(inventory.getWhCode());
                    inventoryLog.setToLocation(container.getLocation().getLocationNo());
                    inventoryLog.setLotNum(inventory.getLotNum());
                    inventoryLog.setSkuName(inventory.getSkuName());
                }else if(dataArea.getReasonCode().equals(ReasonCode.CHECKINFINISHED)){
                    //若为抽检入库修改其库存数量
                    Inventory inventory = j.getJobDetails().iterator().next().getInventory();
                    inventory.setQty(j.getJobDetails().iterator().next().getQty());
                    session.saveOrUpdate(inventory);
                }

                session.delete(view);
            }
            inventoryLog.setContainer(container.getBarcode());
            inventoryLog.setCreateDate(new Date());
            session.save(inventoryLog);

            if(dataArea.getReasonCode().equals(ReasonCode.CHECKINFINISHED)) {
                TransportOrderLog transportOrderLog = TransportOrderLog.getTransportOrderByType();

                if(transportOrderLog!=null && transportOrderLog.getContainer().getBarcode().equals(j.getContainer())){
                    //所有完成后，若任务为抽检入库
                    List<TransportOrderLog> transportOrderLogList = TransportOrderLog.getTransportOrderByType2();
                    //生成回库任务
                    if(transportOrderLogList.size()!=0){
                        for(TransportOrderLog transportOrderLog1 :transportOrderLogList){
                            yiku(session,transportOrderLog1.getToLocation().getLocationNo(),transportOrderLog1.getFromLocation());
                        }
                    }
                    session.delete(transportOrderLog);
                }else{
                    System.out.println("抽检货物与抽检入库货物不一致");
                    return;
                }

            }
            // TODO: 放在后台
//                WebService.finishPutaway(j.getContainer());

        } else if (dataArea.getReasonCode().equals(ReasonCode.RETRIEVALFINISHED) || dataArea.getReasonCode().equals(ReasonCode.CHECKOUTFINISHED) ) {

            Location location = j.getFromLocation();
            location.setEmpty(true);
            if(dataArea.getReasonCode().equals(ReasonCode.RETRIEVALFINISHED)){
                //当出库完成时，删除库存，并将对应的出库限制去掉，抽检出库完成不动这一块，等抽检入库时再将container的reserved设为false
                location.setRetrievalRestricted(false);
                for (Container container : location.getContainers()) {
                    session.delete(container);
                }
            }

            session.update(location);
            j.setStatus(AsrsJobStatus.DONE);
//            OutMessage.info(j.getToStation(), j.getOrderNo(), j.getContainer());

            String message1 = j.getContainer();
            String message2 = "";
            String message3 = "";
            String message4 = "";
            if(j.getJobDetails().iterator().hasNext()){
                JobDetail jd = j.getJobDetails().iterator().next();
                Inventory inventory = jd.getInventory();
                if(inventory != null){
                    message2 = inventory.getSkuName();
                    message3 = inventory.getLotNum();
                }
                message4 = String.valueOf(jd.getQty());
            }


            LedMessage.show(j.getToStation(),message1,message2,message3,message4,j.getMcKey());
            //// TODO: 放在后台
//                WebService.finishOrder(j);
        } else if (dataArea.getReasonCode().equals(ReasonCode.SLOCATIONTOLOCATION)) {
            Location location = j.getFromLocation();
            Location toLocation = j.getToLocation();
            location.setEmpty(true);
            location.setRetrievalRestricted(false);

            toLocation.setEmpty(false);
            toLocation.setRetrievalRestricted(false);

            session.update(location);
            session.update(toLocation);

        }else if (dataArea.getReasonCode().equals(ReasonCode.LTLFINISHED)||dataArea.getReasonCode().equals(ReasonCode.BACK_PUTAWAYFINISHED)) {
            //移库成功
            Location fromLocation = j.getFromLocation();
            Location toLocation = j.getToLocation();

            fromLocation.setEmpty(true);
            toLocation.setEmpty(false);
            toLocation.setReserved(false);

            TransportOrderLog transportOrderLog=null;
            if(dataArea.getReasonCode().equals(ReasonCode.BACK_PUTAWAYFINISHED)){
                transportOrderLog= TransportOrderLog.getTransportOrderLogByFTL(toLocation.getId(),fromLocation.getId());
            }else{
                transportOrderLog= TransportOrderLog.getTransportOrderLogByFTL(fromLocation.getId(),toLocation.getId());
            }

            if(j.getJobDetails().iterator().hasNext()){
                JobDetail jd = j.getJobDetails().iterator().next();
                Inventory inventory = jd.getInventory();
                if(inventory != null){
                    //库存货位所在位置更改
                    inventory.getContainer().setLocation(toLocation);
                    //将container的预定设为false
                    if(dataArea.getReasonCode().equals(ReasonCode.BACK_PUTAWAYFINISHED) || transportOrderLog==null){
                        inventory.getContainer().setReserved(false);
                    }

                    session.update(inventory);
                }
            }


            if(transportOrderLog==null){
                fromLocation.setRetrievalRestricted(false);
                toLocation.setRetrievalRestricted(false);
            }else{
                if(dataArea.getReasonCode().equals(ReasonCode.BACK_PUTAWAYFINISHED)&&transportOrderLog.getType().equals("2")){
                    //判断这个盘库回库是否是最后一个
                    int count = TransportOrderLog.getTransportOrderCountByType2();
                    if(count==1){
                        List<String> locationNos = new ArrayList<>();
                        for(int i =1 ;i<5;i++){
                            locationNos.add("01200800"+i);
                            locationNos.add("01201800"+i);
                            locationNos.add("01202800"+i);
                            locationNos.add("01203900"+i);
                        }

                        Location location = transportOrderLog.getFromLocation();
                        //将此列货位的retrievalrestrited 设为false
                        Query query3=session.createQuery("update Location l set l.retrievalRestricted=false " +
                                "where l.level=:level and l.bank!=26 and l.locationNo not in (:locationNos) and  l.bay=:bay and l.position=:position and l.actualArea=:actualArea");
                        query3.setParameter("level", location.getLevel());
                        query3.setParameter("position", location.getPosition());
                        query3.setParameter("bay", location.getBay());
                        query3.setParameter("actualArea", location.getActualArea());
                        query3.setParameterList("locationNos", locationNos);
                        query3.executeUpdate();
                    }
                    session.delete(transportOrderLog);
                }
            }

            session.update(fromLocation);
            session.update(toLocation);

        }else if(dataArea.getReasonCode().equals(ReasonCode.MSFINISHED)){
            //理货成功
            Location fromLocation = j.getFromLocation();
            String locationNo = fromLocation.getLocationNo();
            //将库存移到前面
            Query query2 = session.createQuery("from Inventory i where i.container.location.bay = :bay " +
                    "and i.container.location.level = :level and i.container.location.position = :position " +
                    "and i.container.location.actualArea = :actualArea order by i.container.location.bank asc ");
            query2.setParameter("bay", fromLocation.getBay());
            query2.setParameter("level", fromLocation.getLevel());
            query2.setParameter("position", fromLocation.getPosition());
            query2.setParameter("actualArea", fromLocation.getActualArea());
            List<Inventory> inventoryList = query2.list();
            for(int i =0;i<inventoryList.size();i++){
                Inventory inventory=inventoryList.get(i);
                Container container = inventory.getContainer();
                Location fromLocation2 =container.getLocation();
                int j2 = i+3;
                String toLocationNo = locationNo.substring(0,2 )+""+j2+locationNo.substring(3,9 );
                Location location = Location.getByLocationNo(toLocationNo);
                container.setLocation(location);
                container.setReserved(false);
                session.update(container);
                fromLocation2.setEmpty(true);
                location.setEmpty(false);
                session.update(fromLocation2);
                session.update(location);
            }
        }

        j.asrsDone();

    }

    public void yiku(Session session,String locationNo,Location toLocation){
        Query query2 = session.createQuery("from Inventory i where i.container.location.locationNo = :locationNo");
        query2.setString("locationNo",locationNo);
        List<Inventory> inventoryList = query2.list();
        Inventory inventory =inventoryList.get(0);
        int qty = inventory.getQty().intValue();//货品数量

        String outPosition = inventory.getContainer().getLocation().getOutPosition();

        JobDetail jobDetail = new JobDetail();
        Job job = new Job();
        //session准备存入job，commit时才会执行sql
        session.save(job);
        session.save(jobDetail);
        //数据准备

        String mckey = Mckey.getNext();
        String toStation = "ML01" ;//到达站台
        String fromStation =  "ML01" ;//出发地点
        String type = AsrsJobType.BACK_PUTAWAY; //抽检回库
        //存入jobDetail
        jobDetail.setInventory(inventory);
        jobDetail.setQty(inventory.getQty());
        //存入job
        job.setContainer(inventory.getContainer().getBarcode());
        job.setFromStation(fromStation);
        job.setMcKey(mckey);
        job.setOrderNo("4200026559");
        job.setSendReport(false);
        job.setStatus("1");
        job.setToStation(toStation);
        job.setType(type);
        job.addJobDetail(jobDetail);
        job.setCreateDate(new Date());
        job.setFromLocation(inventory.getContainer().getLocation());
        //要比出库多一个toLocation
        job.setToLocation(toLocation);

        //修改此托盘
        Container container = inventory.getContainer();
        container.setReserved(true);
        session.saveOrUpdate(container);
    }
}
