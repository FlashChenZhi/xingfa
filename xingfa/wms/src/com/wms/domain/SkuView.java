package com.wms.domain;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by van on 2017/3/17.
 */
@Entity
@Table(name = "SKU_IFAC")
public class SkuView {
    public static final String COL_CODE = "skuCode";
    private String id;
    private String skuCode;//商品代码
    private String skuName;//商品名称

    @Id
    @Column(name = "BASE_ITEM_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_SKUIFAC_ID", allocationSize = 1)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ITEM_CODE")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Basic
    @Column(name = "ITEM_NAME")
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
}
