package com.wms.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by wangfan
 * Created on 2017/2/23.
 * 入库数据
 */
@Entity
@Table(name = "ReceivingPlan")
public class ReceivingPlan {
    private int id;

    private String providerName;
    private String orderNo;
    private String batchNo;
    private String lotNum;
    private BigDecimal qty;
    private BigDecimal recvedQty = BigDecimal.ZERO;
    private String status;
    private int version;
    private Sku sku;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_RECVPLAN_ID", allocationSize = 1)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "PROVIDER_NAME")
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Basic
    @Column(name = "BATCH_NO")
    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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
    @Column(name = "QTY")
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    @Basic
    @Column(name = "RECVEDQTY")
    public BigDecimal getRecvedQty() {
        return recvedQty;
    }

    public void setRecvedQty(BigDecimal recvedQty) {
        this.recvedQty = recvedQty;
    }

    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @ManyToOne
    @JoinColumn(name = "SKU_ID", referencedColumnName = "ID")
    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
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
    @Column(name = "STATUS")
    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReceivingPlan)) return false;

        ReceivingPlan that = (ReceivingPlan) o;

        if (id != that.id) return false;
        if (qty != that.qty) return false;
        if (recvedQty != that.recvedQty) return false;
        if (version != that.version) return false;
        if (!providerName.equals(that.providerName)) return false;
        if (!orderNo.equals(that.orderNo)) return false;
        if (!batchNo.equals(that.batchNo)) return false;
        if (!lotNum.equals(that.lotNum)) return false;
        if (!status.equals(that.status)) return false;
        return sku.equals(that.sku);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + providerName.hashCode();
        result = 31 * result + orderNo.hashCode();
        result = 31 * result + batchNo.hashCode();
        result = 31 * result + lotNum.hashCode();
        result = 31 * result + sku.hashCode();
        return result;
    }

    public static ReceivingPlan getByOrderNo(String orderNo, String skuCode) {
        Query query = HibernateUtil.getCurrentSession().createQuery("from ReceivingPlan  where orderNo=:orderNo and sku.skuCode=:skuCode");
        query.setParameter("orderNo", orderNo);
        query.setParameter("skuCode", skuCode);
        query.setMaxResults(1);
        return (ReceivingPlan) query.uniqueResult();
    }

    public static ReceivingPlan getByLotNum(String lotNum, String skuCode) {
        Query query = HibernateUtil.getCurrentSession().createQuery("from ReceivingPlan  where batchNo=:lotNum and status<>'3' and sku.skuCode=:skuCode");
        query.setParameter("lotNum", lotNum);
        query.setParameter("skuCode", skuCode);
        query.setMaxResults(1);
        return (ReceivingPlan) query.uniqueResult();
    }
}
