package com.test.blocks;

import com.asrs.domain.RouteDetail;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.*;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
@Entity
@Table(name = "Block")
@DiscriminatorColumn(name = "type")
public abstract class Block {
    protected String blockNo;
    protected boolean loaded;
    protected boolean waitingResponse;
    protected String plcName;
    protected String mcKey;

    public Block(String blockNo, boolean loaded,String plcName) {
        this.blockNo = blockNo;
        this.loaded = loaded;
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
    @Column(name = "loaded")
    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    @Basic
    @Column(name ="MCKEY")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        if (loaded != block.loaded) return false;
        if (waitingResponse != block.waitingResponse) return false;
        if (blockNo != null ? !blockNo.equals(block.blockNo) : block.blockNo != null) return false;
//        if (nextBlockNo != null ? !nextBlockNo.equals(block.nextBlockNo) : block.nextBlockNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = blockNo != null ? blockNo.hashCode() : 0;
        result = 31 * result + (loaded ? 1 : 0);
        result = 31 * result + (waitingResponse ? 1 : 0);
//        result = 31 * result + (nextBlockNo != null ? nextBlockNo.hashCode() : 0);
        return result;
    }

    @Transient
    public Block getNextBlock(String jobType,String destStation) {
        org.hibernate.Query q = HibernateUtil.getCurrentSession().createQuery("from RouteDetail rd where rd.currentBlockNo = :currentBlockNo and rd.route.toStation = :toStation and rd.route.type = :type")
                .setString("currentBlockNo",getBlockNo())
                .setString("toStation",destStation)
                .setString("type", jobType);

        List<RouteDetail> rds = q.list();

        if(rds.isEmpty()){
            return null;
        }

        return getByBlockNo(rds.get(0).getNextBlockNo());
    }

    public static Block getByBlockNo(String blockNo) {
        if(StringUtils.isNotBlank(blockNo)) {
            Session session = HibernateUtil.getCurrentSession();
            return (Block) session.get(Block.class, blockNo);
        }else{
            return null;
        }
    }
}
