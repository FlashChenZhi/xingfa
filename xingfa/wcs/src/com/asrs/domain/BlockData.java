package com.asrs.domain;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Administrator on 2016/11/1.
 */
@Entity
@Table(name = "BLOCKDATA")
public class BlockData {


    private String _blockNo;

    @Id
    @Column(name = "BLOCKNO", nullable = false, length = 8)
    public String getBlockNo() {
        return _blockNo;
    }

    public void setBlockNo(String blockNo) {
        _blockNo = blockNo;
    }

    private Boolean _load;

    @Column(name = "LOAD")
    @Basic
    public Boolean getLoad() {
        return _load;
    }

    public void setLoad(Boolean load) {
        _load = load;
    }


    private Collection<BlockDataDetail> _blockDataDetails = new ArrayList<BlockDataDetail>();

    @OneToMany(mappedBy = "blockData")
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.DELETE})
    public Collection<BlockDataDetail> getBlockDataDetails() {
        return _blockDataDetails;
    }

    public void setBlockDataDetails(Collection<BlockDataDetail> blockDataDetails) {
        _blockDataDetails = blockDataDetails;
    }

    public void addBlockDataDetail(BlockDataDetail blockDataDetail) {
        _blockDataDetails.add(blockDataDetail);
        blockDataDetail.setBlockData(this);
    }


}
