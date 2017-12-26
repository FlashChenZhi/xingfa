package com.test.blocks;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/12/12.
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "5")
public class SCar extends Block{
    private int level;
    private int bay;
    private boolean onMCar;
    private String reservedMcKey;
    private boolean charging;
    @Basic
    @Column(name = "reservedMcKey")
    public String getReservedMcKey() {
        return reservedMcKey;
    }

    public void setReservedMcKey(String reservedMcKey) {
        this.reservedMcKey = reservedMcKey;
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
    @Column(name = "ONMCAR")
    public boolean isOnMCar() {
        return onMCar;
    }

    public void setOnMCar(boolean onMCar) {
        this.onMCar = onMCar;
    }

    @Basic
    @Column(name = "CHARGING")
    public boolean isCharging() {
        return charging;
    }

    public void setCharging(boolean charging) {
        this.charging = charging;
    }
}
