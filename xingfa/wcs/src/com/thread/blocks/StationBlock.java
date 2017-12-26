package com.thread.blocks;

import com.util.hibernate.HibernateUtil;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/10/28.
 * 站台
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "3")
public class StationBlock extends Block {
    protected String stationNo;
    private String dock;
    private String liftNo;
    private String buffMckey;
    private String load;

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

    @Basic
    @Column(name = "BUFF_MCKEY")
    public String getBuffMckey() {
        return buffMckey;
    }

    public void setBuffMckey(String buffMckey) {
        this.buffMckey = buffMckey;
    }

    @Basic
    @Column(name = "LOAD_FLAG")
    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public static StationBlock getByStationNo(String stationNo) {
        return (StationBlock) HibernateUtil.getCurrentSession().createQuery("from StationBlock sb where sb.stationNo = :stationNo")
                .setString("stationNo", stationNo).uniqueResult();
    }
}
