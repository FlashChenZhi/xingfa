package com.wms.domain.blocks;

import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/10/26.
 */
@Entity
@Table(name = "XINGFA.Block")
@DiscriminatorColumn(name = "type")
public abstract class Block {
    protected String blockNo;
    protected boolean waitingResponse;
    private String reservedMcKey;
    protected String plcName;
    protected String mcKey;
    protected String status;
    protected String error;
    private String wareHouse;

    public static final String STATUS_RUN = "1";
    public static final String STATUS_CHARGE = "3";
    public static final String STATUS_CHARGE_OVER = "4";

    public Block(String blockNo, String plcName) {
        this.blockNo = blockNo;
        waitingResponse = false;
        this.plcName = plcName;
    }

    protected Block() {
    }

    @Id
    @Column(name = "blockNo")
    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    @Basic
    @Column(name = "MCKEY")
    public String getMcKey() {
        return mcKey;
    }

    public void setMcKey(String mcKey) {
        this.mcKey = mcKey;
    }

    @Basic
    @Column(name = "waitingResponse")
    public boolean isWaitingResponse() {
        return waitingResponse;
    }

    public void setWaitingResponse(boolean waitingResponse) {
        this.waitingResponse = waitingResponse;
    }

    @Basic
    @Column(name = "PLCNAME")
    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    @Basic
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Basic
    @Column(name = "reservedMcKey")
    public String getReservedMcKey() {
        return reservedMcKey;
    }

    public void setReservedMcKey(String reservedMcKey) {
        this.reservedMcKey = reservedMcKey;
    }

    @Basic
    @Column(name = "ERROR")
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Basic
    @Column(name = "WARE_HOUSE")
    public String getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(String wareHouse) {
        this.wareHouse = wareHouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = blockNo != null ? blockNo.hashCode() : 0;
        result = 31 * result + (waitingResponse ? 1 : 0);
//        result = 31 * result + (nextBlockNo != null ? nextBlockNo.hashCode() : 0);
        return result;
    }


    public static Block getByBlockNo(String blockNo) {
        if (StringUtils.isNotBlank(blockNo)) {
            Session session = HibernateUtil.getCurrentSession();
            return (Block) session.get(Block.class, blockNo);
        } else {
            return null;
        }

    }
}
