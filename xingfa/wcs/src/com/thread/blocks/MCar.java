package com.thread.blocks;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 * 母车
 */
@Entity
@Table(name = "XINGFA.Block")
@DiscriminatorValue(value = "4")
@DynamicUpdate()
public class MCar extends Block {
    private String sCarBlockNo;
    private boolean liftSide;
    private int bay;
    private int level;
    private String dock;
    private String liftNo;
    private String position; //位置 ： 1左，2:右
    private String actualArea;
    private Boolean checkLocation;//是否校准位置
    private Integer groupNo;

    @Basic
    @Column(name = "liftNo")
    public String getLiftNo() {
        return liftNo;
    }

    public void setLiftNo(String liftNo) {
        this.liftNo = liftNo;
    }

    @Basic
    @Column(name = "LEV")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Basic
    @Column(name = "liftSide")
    public boolean isLiftSide() {
        return liftSide;
    }

    public void setLiftSide(boolean liftSide) {
        this.liftSide = liftSide;
    }

    @Transient
    public String getDock(String mCarNo, String liftNo) {
        Query query = HibernateUtil.getCurrentSession().createQuery("from Dock where mCarNo=:mcarNo and liftNo=:liftNO").setMaxResults(1);
        query.setParameter("mcarNo", mCarNo);
        query.setParameter("liftNO", liftNo);
        Dock dock = (Dock) query.uniqueResult();
        return  dock.getDockNo();
    }

    @Basic
    @Column(name = "BAY")
    public int getBay() {
        return bay;
    }

    public void setBay(int bay) {
        this.bay = bay;
    }

    @Basic
    @Column(name = "POSITION")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Basic
    @Column(name = "DOCK")
    public String getDock() {
        return dock;
    }

    public void setDock(String dock) {
        this.dock = dock;
    }

    @Basic
    @Column(name = "SCARBLOCKNO")
    public String getsCarBlockNo() {
        return sCarBlockNo;
    }

    public void setsCarBlockNo(String sCarBlockNo) {
        this.sCarBlockNo = sCarBlockNo;
    }

    @Basic
    @Column(name = "ACTUALAREA")
    public String getActualArea() {
        return actualArea;
    }

    public void setActualArea(String actuaneArea) {
        this.actualArea = actuaneArea;
    }

    @Basic
    @Column(name = "CHECK_LOCATION")
    public Boolean getCheckLocation() {
        return checkLocation;
    }

    public void setCheckLocation(Boolean checkLocation) {
        this.checkLocation = checkLocation;
    }

    @Basic
    @Column(name = "GROUP_NO")
    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

}
