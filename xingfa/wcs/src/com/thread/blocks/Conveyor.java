package com.thread.blocks;

import com.asrs.message.Message03;
import com.thread.utils.MsgSender;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/12/16.
 * 输送机
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "1")
public class Conveyor extends Block {
    private String dock;
    private String onCar;

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

}