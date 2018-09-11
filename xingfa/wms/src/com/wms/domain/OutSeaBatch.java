package com.wms.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created by van on 2017/12/15.
 */
@Entity
@Table(name = "XINGFA.OUTSEA_BATCH")
@DynamicUpdate()
public class OutSeaBatch {

    private int id;
    private RetrievalOrderDetail retrievalOrderDetail;
    private String batchNo;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDERDETAIL_ID", referencedColumnName = "id")
    public RetrievalOrderDetail getRetrievalOrderDetail() {
        return retrievalOrderDetail;
    }

    public void setRetrievalOrderDetail(RetrievalOrderDetail retrievalOrderDetail) {
        this.retrievalOrderDetail = retrievalOrderDetail;
    }

    @Basic
    @Column(name = "BATCH_NO")
    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
