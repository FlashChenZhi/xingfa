package com.wms.domain;

import com.asrs.business.consts.AsrsJobType;
import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by van on 2018/1/14.
 */
@Entity
@Table(name = "XINGFA.JOBLOG")
@DynamicUpdate()
public class JobLog {
    public static final String __TYPE = "type";
    public static final String __CONTAINER = "container";
    public static final String __FROMLOCATIONNO = "fromLocation";
    public static final String __TOLOCATIONNO = "toLocation";
    public static final String __FROMSTATION = "fromStation";
    public static final String __TOSTATION = "toStation";
    public static final String __CREATEDATE = "createDate";
    private int id;
    private String mckey;
    private String orderNo;
    private String fromLocation;
    private String toLocation;
    private String container;
    private String type;
    private String fromStation;
    private String toStation;
    private Date createDate;
    private String createUser;
    private String status;
    private String skuCode;
    private String skuName;
    private BigDecimal _qty;
    private String lotNum;
    private boolean isRead;

    public static final String COL_ID = "id";

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "LOT_NUM")
    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    @Basic
    @Column(name = "SKUCODE")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Basic
    @Column(name = "SKUNAME")
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @Column(name = "QTY")
    @Basic
    public BigDecimal getQty() {
        return _qty;
    }

    public void setQty(BigDecimal qty) {
        _qty = qty;
    }

    @Basic
    @Column(name = "MCKEY")
    public String getMckey() {
        return mckey;
    }

    public void setMckey(String mckey) {
        this.mckey = mckey;
    }

    @Basic
    @Column(name = "ORDER_NO")
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Basic
    @Column(name = "FROMLOCATION")
    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    @Basic
    @Column(name = "TOLOCATION")
    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    @Basic
    @Column(name = "CONTAINER")
    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    @Basic
    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "FROMSTATION")
    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    @Basic
    @Column(name = "CREATEDATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "CREATEUSER")
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Basic
    @Column(name = "TOSTATION")
    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    @Basic
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "ISREAD")
    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    //查询出入库的jobLog
    public static List<JobLog> getJobLogByType(){
        List<String> list = new ArrayList<>();
        list.add(AsrsJobType.PUTAWAY);
        list.add(AsrsJobType.RETRIEVAL);
        //Query query = HibernateUtil.getCurrentSession().createQuery("from JobLog  ");
        Query query = HibernateUtil.getCurrentSession().createQuery("from JobLog  where " +
                " isRead is false and type in (:types) ");
        query.setParameterList("types", list);
        return query.list();
    }
}
