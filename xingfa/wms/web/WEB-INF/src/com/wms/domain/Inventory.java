package com.wms.domain;

import javax.persistence.*;

/**
 * Author: Zhouyue
 * Date: 2010-11-12
 * Time: 12:19:29
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "INVENTORY")
public class Inventory {

    public static final String __CONTAINER = "container";
    public static final String __SKUCODE = "sku";
    public static final String __ID = "id";
    private int id;
    private String orderNo;
    private String batchNo;
    private String lotNum;
    private int qty;
    private String storeDate;
    private String storeTime;
    private String providerName;
    private int version;

    private Sku sku;
    private Container container;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_INVENTORY_ID", allocationSize = 1)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Basic
    @Column(name = "STORE_DATE")
    public String getStoreDate() {
        return storeDate;
    }

    public void setStoreDate(String storeDate) {
        this.storeDate = storeDate;
    }

    @Basic
    @Column(name = "STORE_TIME")
    public String getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(String storeTime) {
        this.storeTime = storeTime;
    }

    @Basic
    @Column(name = "PROVIDER_NAME")
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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


    @ManyToOne
    public
    @JoinColumn(name = "CONTAINERID", referencedColumnName = "ID")
    Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        container = container;
    }

}
