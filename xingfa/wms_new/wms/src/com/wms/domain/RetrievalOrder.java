package com.wms.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.persistence.Version;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wangfan
 * Created on 2017/2/23.
 * 出库数据
 */
@Entity
@Table(name = "RETRIEVAL_ORDER")
public class RetrievalOrder {

    public static final String COL_ORDER_NO = "orderNo";
    public static final String COL_STATUS = "status";
    public static final String COL_WHCODE = "whCode";
    public static final String COL_ID = "id";
    private int id;

    private String whCode;
    private String orderNo;
    private String jobType;
    private BigDecimal boxQty;
    private String coustomName;
    private String carrierName;
    private String toLocation;
    private String area;
    private String desc;
    private String status;

    private Set<RetrievalOrderDetail> retrievalOrderDetailSet = new HashSet<RetrievalOrderDetail>();

    public static final String STATUS_WAIT = "0";
    public static final String STATUS_START = "1";
    public static final String STATUS_FINISH = "2";
    public static final String STATUS_CANCEL = "9";

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_RETRIEVALORDER_ID", allocationSize = 1)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "WH_CODE")
    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
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
    @Column(name = "JOB_TYPE")
    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    @Basic
    @Column(name = "BOX_QTY")
    public BigDecimal getBoxQty() {
        return boxQty;
    }

    public void setBoxQty(BigDecimal boxQty) {
        this.boxQty = boxQty;
    }

    @Basic
    @Column(name = "COUSTOM_NAME")
    public String getCoustomName() {
        return coustomName;
    }

    public void setCoustomName(String coustomName) {
        this.coustomName = coustomName;
    }

    @Basic
    @Column(name = "CARRIER_NAME")
    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    @Basic
    @Column(name = "TO_LOCATION")
    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    @Basic
    @Column(name = "AREA")
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Basic
    @Column(name = "\"DESC\"")
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Basic
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @OneToMany(mappedBy = "retrievalOrder")
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.DELETE})
    public Set<RetrievalOrderDetail> getRetrievalOrderDetailSet() {
        return retrievalOrderDetailSet;
    }

    public void setRetrievalOrderDetailSet(Set<RetrievalOrderDetail> retrievalOrderDetailSet) {
        this.retrievalOrderDetailSet = retrievalOrderDetailSet;
    }

    @Transient
    public static RetrievalOrder getRetrievalOrder(String orderNo, String skuCode) {

        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from RetrievalOrder where orderNo=:orderNo and sku.skuCode=:skuCode");
        query.setParameter("orderNo", orderNo);
        query.setParameter("skuCode", skuCode);
        query.setMaxResults(1);
        return (RetrievalOrder) query.uniqueResult();
    }

    @Transient
    public static RetrievalOrder getById(Integer orderId) {
        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from RetrievalOrder where id=:oid");
        query.setParameter("oid", orderId);
        return (RetrievalOrder) query.uniqueResult();
    }

    @Transient
    public static RetrievalOrder getByOrderNo(String orderNo) {
        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from RetrievalOrder where orderNo=:orderNo");
        query.setParameter("orderNo", orderNo);
        query.setMaxResults(1);
        return (RetrievalOrder) query.uniqueResult();

    }
}
