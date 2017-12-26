package com.wms.domain;

import javax.persistence.*;

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
    private int qty;
    private String status;
    private int version;
    private Sku sku;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_RECEIVING_PLAN_ID", allocationSize = 1)
    @Column(name = "ID")
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
    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @ManyToOne(fetch = FetchType.LAZY)
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
        if (o == null || getClass() != o.getClass()) return false;

        ReceivingPlan that = (ReceivingPlan) o;

        if (id != that.id) return false;
        if (qty != that.qty) return false;
        if (version != that.version) return false;
        if (providerName != null ? !providerName.equals(that.providerName) : that.providerName != null) return false;
        if (orderNo != null ? !orderNo.equals(that.orderNo) : that.orderNo != null) return false;
        if (batchNo != null ? !batchNo.equals(that.batchNo) : that.batchNo != null) return false;
        if (lotNum != null ? !lotNum.equals(that.lotNum) : that.lotNum != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        return sku != null ? sku.equals(that.sku) : that.sku == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (providerName != null ? providerName.hashCode() : 0);
        result = 31 * result + (orderNo != null ? orderNo.hashCode() : 0);
        result = 31 * result + (batchNo != null ? batchNo.hashCode() : 0);
        result = 31 * result + (lotNum != null ? lotNum.hashCode() : 0);
        result = 31 * result + qty;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + version;
        result = 31 * result + (sku != null ? sku.hashCode() : 0);
        return result;
    }
}
