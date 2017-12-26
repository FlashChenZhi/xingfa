package com.wms.domain;

import javax.persistence.*;
import java.math.BigDecimal;

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
    private String lotNum;
    private BigDecimal qty;
    private BigDecimal caseQty;
    private String storeDate;
    private String storeTime;
    private String providerName;
    private int version;
    private String whCode;
    private String caseBarCode;

    private String skuCode;
    private Container container;

    public static final String COL_SKUCODE = "skuCode";
    public static final String COL_CONTAINER = "container";
    public static final String COL_LOTNUM = "lotNum";

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_INVENTORY_ID", allocationSize = 1)
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

    @Basic
    @Column(name = "SKUCODE")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @ManyToOne
    public
    @JoinColumn(name = "CONTAINERID", referencedColumnName = "ID")
    Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    @Basic
    @Column(name = "WHCODE")
    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    @Basic
    @Column(name = "CASEQTY")
    public BigDecimal getCaseQty() {
        return caseQty;
    }

    public void setCaseQty(BigDecimal caseQty) {
        this.caseQty = caseQty;
    }

    @Basic
    @Column(name = "CASE_BARCODE")
    public String getCaseBarCode() {
        return caseBarCode;
    }

    public void setCaseBarCode(String caseBarCode) {
        this.caseBarCode = caseBarCode;
    }
}
