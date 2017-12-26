package com.wms.domain.blocks;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/12/12.
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "6")
public class SCar extends Block {
    private int bay;
    private int level;
    private String onCarNo;
    private boolean bindingCrane;
    private Integer power;

    @Basic
    @Column(name = "bindingCrane")
    public boolean isBindingCrane() {
        return bindingCrane;
    }

    public void setBindingCrane(boolean bindingCrane) {
        this.bindingCrane = bindingCrane;
    }

    @Basic
    @Column(name = "`LEVEL`")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
    @Column(name = "ONMCARNO")
    public String getOnCarNo() {
        return onCarNo;
    }

    public void setOnCarNo(String onCarNo) {
        this.onCarNo = onCarNo;
    }

    @Basic
    @Column(name = "POWER")
    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }
}
