package com.wms.domain;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by wangfan
 * Created on 2017/2/23.
 * 商品数据
 */
@Entity
@Table(name = "SKU")
public class Sku {

    public static final String __SKUCODE = "skuCode";
    private int id;
    private String skuCode;//商品代码
    private String skuName;//商品名称
    private String skuSpec;//商品规格
    private String custSkuName;//客户货名
    private String skuEom;//计量单位
    private String custName;//货主名称
    private BigDecimal packageQty;//装箱数量
    private BigDecimal palletLoadQTy;//托盘装载数量
    private int version;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_SKU_IDg", allocationSize = 1)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SKU_CODE")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Basic
    @Column(name = "SKU_NAME")
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @Basic
    @Column(name = "SKU_SPEC")
    public String getSkuSpec() {
        return skuSpec;
    }

    public void setSkuSpec(String skuSpec) {
        this.skuSpec = skuSpec;
    }


    @Basic
    @Column(name = "CUST_SKU_NAME")
    public String getCustSkuName() {
        return custSkuName;
    }

    public void setCustSkuName(String custSkuName) {
        this.custSkuName = custSkuName;
    }


    @Basic
    @Column(name = "SKU_EOM")
    public String getSkuEom() {
        return skuEom;
    }

    public void setSkuEom(String skuEom) {
        this.skuEom = skuEom;
    }

    @Basic
    @Column(name = "CUST_NAME")
    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    @Basic
    @Column(name = "PACKAGE_QTY")
    public BigDecimal getPackageQty() {
        return packageQty;
    }

    public void setPackageQty(BigDecimal packageQty) {
        this.packageQty = packageQty;
    }

    @Basic
    @Column(name = "PALLET_LOAD_QTY")
    public BigDecimal getPalletLoadQTy() {
        return palletLoadQTy;
    }

    public void setPalletLoadQTy(BigDecimal palletLoadQTy) {
        this.palletLoadQTy = palletLoadQTy;
    }

    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sku sku = (Sku) o;

        if (id != sku.id) return false;
        if (version != sku.version) return false;
        if (skuCode != null ? !skuCode.equals(sku.skuCode) : sku.skuCode != null) return false;
        if (skuName != null ? !skuName.equals(sku.skuName) : sku.skuName != null) return false;
        if (skuSpec != null ? !skuSpec.equals(sku.skuSpec) : sku.skuSpec != null) return false;
        if (custSkuName != null ? !custSkuName.equals(sku.custSkuName) : sku.custSkuName != null) return false;
        if (skuEom != null ? !skuEom.equals(sku.skuEom) : sku.skuEom != null) return false;
        if (custName != null ? !custName.equals(sku.custName) : sku.custName != null) return false;
        if (packageQty != null ? !packageQty.equals(sku.packageQty) : sku.packageQty != null) return false;
        return palletLoadQTy != null ? palletLoadQTy.equals(sku.palletLoadQTy) : sku.palletLoadQTy == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (skuCode != null ? skuCode.hashCode() : 0);
        result = 31 * result + (skuName != null ? skuName.hashCode() : 0);
        result = 31 * result + (skuSpec != null ? skuSpec.hashCode() : 0);
        result = 31 * result + (custSkuName != null ? custSkuName.hashCode() : 0);
        result = 31 * result + (skuEom != null ? skuEom.hashCode() : 0);
        result = 31 * result + (custName != null ? custName.hashCode() : 0);
        result = 31 * result + (packageQty != null ? packageQty.hashCode() : 0);
        result = 31 * result + (palletLoadQTy != null ? palletLoadQTy.hashCode() : 0);
        result = 31 * result + version;
        return result;
    }
}
