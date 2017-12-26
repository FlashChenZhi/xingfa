package com.AllBinding.blocks;

import com.asrs.domain.RouteDetail;
import com.util.hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

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
    protected boolean waitingResponse = false;
    protected String plcName;
    protected String mcKey;
    protected String status;
    protected String reservedMcKey;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        if (waitingResponse != block.waitingResponse) return false;
        if (blockNo != null ? !blockNo.equals(block.blockNo) : block.blockNo != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = blockNo != null ? blockNo.hashCode() : 0;
        result = 31 * result + (waitingResponse ? 1 : 0);
        return result;
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
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Transient
    public Block getNextBlock(String fromStation, String toStation) {
        Session session = HibernateUtil.getCurrentSession();
        RouteDetail routeDetail = (RouteDetail) session.createQuery("from RouteDetail rd where rd.currentBlockNo =:currentBlockNo and" +
                " rd.route.toStation =:toStation and rd.route.fromStation = :fromStation").setString("currentBlockNo", getBlockNo())
                .setString("toStation", toStation).setString("fromStation", fromStation).uniqueResult();
        return getByBlockNo(routeDetail.getNextBlockNo());
    }

    public static Block getByBlockNo(String blockNo) {
        Session session = HibernateUtil.getCurrentSession();
        Block block = (Block) session.createCriteria(Block.class).add(Restrictions.eq("blockNo", blockNo)).uniqueResult();
        return block;
    }

    @Transient
    public Block getPreBlock(String fromStation, String toStation) {
        Session session = HibernateUtil.getCurrentSession();
        RouteDetail routeDetail = (RouteDetail) session.createQuery("from RouteDetail rd where rd.nextBlockNo =:currentBlockNo and" +
                " rd.route.toStation =:toStation and rd.route.fromStation = :fromStation").setString("currentBlockNo", getBlockNo())
                .setString("toStation", toStation).setString("fromStation", fromStation).uniqueResult();
        return getByBlockNo(routeDetail.getCurrentBlockNo());

    }

    public static List<Block> getByPlcName(String plcName) {
        Session session = HibernateUtil.getCurrentSession();
        return session.createCriteria(Block.class).add(Restrictions.eq("plcName", plcName)).list();
    }
}
