package com.test.blocks;

import com.util.hibernate.HibernateUtil;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/10/28.
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "3")
public class StationBlock extends Block {
    protected String stationNo;
    private String dock;
    private String liftNo;

    @Basic
    @Column(name = "stationNo")
    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    @Basic
    @Column(name = "liftNo")
    public String getLiftNo() {
        return liftNo;
    }

    public void setLiftNo(String liftNo) {
        this.liftNo = liftNo;
    }

    @Basic
    @Column(name = "DOCK")
    public String getDock() {
        return dock;
    }

    public void setDock(String dock) {
        this.dock = dock;
    }

    public static StationBlock getByStationNo(String stationNo) {
        return (StationBlock) HibernateUtil.getCurrentSession().createQuery("from StationBlock sb where sb.stationNo = :stationNo")
                .setString("stationNo",stationNo).uniqueResult();
    }
}
