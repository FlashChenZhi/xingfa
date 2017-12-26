package com.test.blocks;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Administrator on 2016/12/16.
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "7")
public class Crane extends Block {
    private boolean carExist;
    private String sCarBlockNo;
    private int bay;
    private int level;
    private int aisleLeft;
    private int aisleRight;
    private String modLeft;
    private String modRight;
    private String reservedMcKey;

    @Basic
    @Column(name = "`LEVEL`")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Basic
    @Column(name = "aisleLeft")
    public int getAisleLeft() {
        return aisleLeft;
    }

    public void setAisleLeft(int aisleLeft) {
        this.aisleLeft = aisleLeft;
    }

    @Basic
    @Column(name = "aisleRight")
    public int getAisleRight() {
        return aisleRight;
    }

    public void setAisleRight(int aisleRight) {
        this.aisleRight = aisleRight;
    }

    @Basic
    @Column(name = "modLeft")
    public String getModLeft() {
        return modLeft;
    }

    public void setModLeft(String modLeft) {
        this.modLeft = modLeft;
    }

    @Basic
    @Column(name = "modRight")
    public String getModRight() {
        return modRight;
    }

    public void setModRight(String modRight) {
        this.modRight = modRight;
    }

    @Basic
    @Column(name = "carExist")
    public boolean isCarExist() {
        return carExist;
    }

    public void setCarExist(boolean carExist) {
        this.carExist = carExist;
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
    @Column(name = "BAY")
    public int getBay() {
        return bay;
    }

    public void setBay(int bay) {
        this.bay = bay;
    }

    @Basic
    @Column(name = "SCARBLOCKNO")
    public String getsCarBlockNo() {
        return sCarBlockNo;
    }

    public void setsCarBlockNo(String sCarBlockNo) {
        this.sCarBlockNo = sCarBlockNo;
    }

    public static List<Crane> getToCrane(int aisle) {
        Session session = HibernateUtil.getCurrentSession();
        Query query = session.createQuery("from Crane  c where (c.aisleLeft=:aisle and c.modLeft in('01','03') ) or (c.aisleRight=:aisle and c.modRight in('01','03')) ").setInteger("aisle", aisle);
        List<Crane> cranes = query.list();
        return cranes;
    }

    public static List<Crane> getFromCrane(int aisle) {
        Session session = HibernateUtil.getCurrentSession();
        Query query = session.createQuery("from Crane  c where (c.aisleLeft=:aisle and c.modLeft in('02','03') ) or (c.aisleRight=:aisle and c.modRight in('02','03'))").setInteger("aisle", aisle);
        List<Crane> cranes = query.list();
        return cranes;
    }
}
