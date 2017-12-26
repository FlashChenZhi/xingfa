package com.asrs.domain.XMLbean.XMLList;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.LoadUnitAtIdDA;
import com.asrs.domain.XMLbean.XMLProcess;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "LoadUnitAtID")
public class LoadUnitAtID extends XMLProcess {
    private static List<String> skus = new ArrayList<>();

    static {
        skus.add("test1");
        skus.add("test2");
        skus.add("test3");
        skus.add("test4");
    }

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
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "LOADUNITATID_SEQ", allocationSize = 1)
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
        //To change body of implemented methods use File | Settings | File Templates.
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            //通过站台和最晚生成作业的时间得到一条job
            String station = dataArea.getXMLLocation().getMHA();
            Job job = (Job) session.createQuery("from Job j where j.fromStation=:station and j.status = :waiting order by j.createDate desc ")
                    .setString("station", station)
                    .setString("waiting", AsrsJobStatus.WAITING).setMaxResults(1).uniqueResult();
//            Job job = (Job) session.createCriteria(Job.class).createCriteria(Job.__CONTAINER)
//                    .add(Restrictions.eq(Container.__BARCODE, dataArea.getScanData().getItemData().getValue())).uniqueResult();
            if (job != null) {
                job.setStatus(AsrsJobStatus.ARRIVAL);
            } else {
                Config config = Config.getByKey(Config.KEY_RUM_MODEL);
                if (config != null && config.getValue().equals(Config.MODEL_TEST)) {
                    //暂时将托盘放入虚拟货位
                    Location vl = Location.getByLocationNo(Const.RECV_TEMP_LOCATION);
                    Container c = new Container();
                    c.setBarcode(StringUtils.leftPad(String.valueOf(HibernateUtil.nextSeq("SEQ_CONTAINER_BARCODE")), 10, "0"));
                    c.setLocation(vl);
                    c.setReserved(false);
                    c.setCreateDate(new Date());
                    c.setCreateUser("test");
                    session.save(c);

                    Random random = new Random();
                    String skuCode = skus.get(random.nextInt(2));
                    //生成一个库存
                    Inventory i = new Inventory();
//                    i.setSkuCode(skuCode);
//                    i.setQty(100);
//                    i.setAvailableQty(100);
//                    i.setContainer(c);
//                    i.setCreateDate(new Date());
                    session.save(i);

                    //生成一个入库作业
                    Job j = new Job();
                    j.setMcKey(Mckey.getNext());
                    //设置来自哪个站台
                    j.setFromStation(station);
                    //设置托盘号
                    j.setContainer(c);
                    j.setStatus(AsrsJobStatus.ARRIVAL);
                    j.setType(AsrsJobType.PUTAWAY);
                    //托盘暂时先放入虚拟货位
                    //从httpSession中获取操作员名字
                    j.setCreateUser("test");
                    //设置作业创建时间
                    j.setCreateDate(new Date());
                    JobDetail jobDetail = new JobDetail();
                    jobDetail.setInventory(i);
                    jobDetail.setQty(i.getQty());
                    j.addJobDetail(jobDetail);
                    session.save(j);
                }
            }

            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }

    }
}
