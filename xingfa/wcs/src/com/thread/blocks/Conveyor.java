package com.thread.blocks;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/12/16.
 * 输送机
 */
@Entity
@Table(name = "XINGFA.Block")
@DiscriminatorValue(value = "1")
public class Conveyor extends Block {
    private String dock;
    private String onCar;
    private boolean manty;
    private boolean mantWaiting;

    @Basic
    @Column(name = "DOCK")
    public String getDock() {
        return dock;
    }

    public void setDock(String dock) {
        this.dock = dock;
    }

    @Basic
    @Column(name = "ONCAR")
    public String getOnCar() {
        return onCar;
    }

    public void setOnCar(String onCar) {
        this.onCar = onCar;
    }

    @Basic
    @Column(name = "MANTY")
    public boolean isManty() {
        return manty;
    }

    public void setManty(boolean manty) {
        this.manty = manty;
    }

    @Basic
    @Column(name = "MANT_WAITING")
    public boolean isMantWaiting() {
        return mantWaiting;
    }

    public void setMantWaiting(boolean mantWaiting) {
        this.mantWaiting = mantWaiting;
    }

}
