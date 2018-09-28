package com.asrs.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: ed_chen
 * @Date: Create in 15:03 2018/9/12
 * @Description:
 * @Modified By:
 */
@Entity
@Table(name = "INSTORAGESTRATEGY")
@DynamicUpdate()
public class InStorageStrategy {
    private int id;
    private String skuCode;//商品代码
    private String lotNum;//商品名称
    private int bay;
    private int level;
    private String bayLevel;
    private Date date;

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
    @Column(name = "SKUCODE")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Basic
    @Column(name = "LOTNUM")
    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    @Basic
    @Column(name = "BAY")
    public int getBay() {
        return bay;
    }

    public void setBay(int bay) {
        this.bay = bay;
    }

    @Basic
    @Column(name = "LEV")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Basic
    @Column(name = "BAYLEV")
    public String getBayLevel() {
        return bayLevel;
    }

    public void setBayLevel(String bayLevel) {
        this.bayLevel = bayLevel;
    }

    @Basic
    @Column(name = "DATE")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
