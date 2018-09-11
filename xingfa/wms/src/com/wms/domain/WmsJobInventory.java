package com.wms.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by van on 2017/12/15.
 */
@Entity
@Table(name = "XINGFA.WMS_JOB_INVENTORY")
@DynamicUpdate()
public class WmsJobInventory {

    private int id;
    private String palletNo;
    private boolean send;

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
    @Column(name = "PALLETNO")
    public String getPalletNo() {
        return palletNo;
    }

    public void setPalletNo(String palletNo) {
        this.palletNo = palletNo;
    }

    @Basic
    @Column(name = "SEND")
    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }
}
