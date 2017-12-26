package com.wms.domain;

import javax.persistence.*;

/**
 * Created by van on 2017/12/15.
 */
@Entity
@Table(name = "OUTSEA_BATCH")
public class OutSeaBatch {

    private int id;
    private RetrievalOrderDetail retrievalOrderDetail;
    private String batchNo;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_OUTSEA_BATCH_ID", allocationSize = 1)
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
