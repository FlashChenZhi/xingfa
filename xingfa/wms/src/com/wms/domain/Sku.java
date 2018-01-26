package com.wms.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.*;

import javax.persistence.*;
import javax.persistence.Version;
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
    public static final String COL_CODE = "skuCode";
    private int id;
    private String skuCode;//商品代码
    private String skuName;//商品名称
    private int shelfLift;//存储周期
    private int warning;//提前预警时间
    private int version;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_SKU_ID", allocationSize = 1)
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
    @Column(name = "SHELF_LIFE")
    public int getShelfLift() {
        return shelfLift;
    }

    public void setShelfLift(int shelfLift) {
        this.shelfLift = shelfLift;
    }

    @Basic
    @Column(name = "WARNING")
    public int getWarning() {
        return warning;
    }

    public void setWarning(int warning) {
        this.warning = warning;
    }

    @Basic
    @Column(name = "SKU_NAME")
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


    public static Sku getByCode(String skuCode) {

        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from Sku where skuCode =:skuCode");
        query.setParameter("skuCode", skuCode);
        query.setMaxResults(1);
        return (Sku) query.uniqueResult();
    }
}
