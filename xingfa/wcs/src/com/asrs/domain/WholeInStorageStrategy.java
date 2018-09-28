package com.asrs.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @Author: ed_chen
 * @Date: Create in 15:13 2018/9/12
 * @Description:
 * @Modified By:
 */
@Entity
@Table(name = "WHOLEINSTORAGESTRATEGY")
@DynamicUpdate()
public class WholeInStorageStrategy {
    private int id;
    private boolean status;

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
    @Column(name = "STATUS")
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}