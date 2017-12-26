package com.asrs.domain;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/11/1.
 */
@Entity
@Table(name = "BLOCKDATADETAIL")
public class BlockDataDetail {

    private int _id;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_BLOCKDATADETAIL_ID", allocationSize = 1)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return _id;
    }
    public void setId(int id) {
        _id = id;
    }


    private BlockData _blockData;

    @ManyToOne
    public
    @JoinColumn(name = "BLOCKNO", referencedColumnName = "BLOCKNO")
    BlockData getBlockData() {
        return _blockData;
    }

    public void setBlockData(BlockData blockData) {
        _blockData = blockData;
    }

    private int _serialNo;
    @Column(name = "SERIALNO")
    @Basic
    public int getSerialNo() {
        return _serialNo;
    }
    public void setSerialNo(int serialNo) {
        _serialNo = serialNo;
    }

    private String _mcKey;
    @Column(name = "MCKEY")
    @Basic
    public String getMcKey() {
        return _mcKey;
    }
    public void setMcKey(String mcKey) {
        _mcKey = mcKey;
    }

    private String _barCode;
    @Column(name = "BARCODE")
    @Basic
    public String getBarCode() {
        return _barCode;
    }
    public void setBarCode(String barCode) {
        _barCode = barCode;
    }
}
