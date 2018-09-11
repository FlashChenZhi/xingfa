package com.wms.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by van on 2018/1/24.
 */

@Entity
@Table(name = "XINGFA.RETRIEVAL_FINISH")
@DynamicUpdate()
public class RetrievalFinishLog {
    private int id;
    private String orderNo;
    private String container;

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
    @Column(name = "ORDER_NO")
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Basic
    @Column(name = "CONTAINER")
    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }
}
