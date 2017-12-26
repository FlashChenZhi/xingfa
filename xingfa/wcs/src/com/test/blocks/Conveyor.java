package com.test.blocks;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/10/12.
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "1")
public class Conveyor extends Block {
    private String liftNo;

    @Basic
    @Column(name = "liftNo")
    public String getLiftNo() {
        return liftNo;
    }

    public void setLiftNo(String liftNo) {
        this.liftNo = liftNo;
    }

}
