package com.test.blocks;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "4")
public class MCar extends Block {
    private boolean carExist;
    private String sCarBlockNo;
    private boolean liftSide;
    private int bay;
    private int level;
    private int aisleLeft;
    private int aisleRight;
    private String modLeft;
    private String modRight;
    private String liftNo;
    private String dock;
    private String reservedMcKey;

    @Basic
    @Column(name = "liftNo")
    public String getLiftNo() {
        return liftNo;
    }

    public void setLiftNo(String liftNo) {
        this.liftNo = liftNo;
    }

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
    @Column(name = "liftSide")
    public boolean isLiftSide() {
        return liftSide;
    }

    public void setLiftSide(boolean liftSide) {
        this.liftSide = liftSide;
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

    public static List<MCar> getToMCar(int aisle, int level) {
        Session session = HibernateUtil.getCurrentSession();
        Query query = session.createQuery("from MCar  sc where ((sc.aisleLeft=:aisle and sc.modLeft in('01','03') ) or (sc.aisleRight=:aisle and sc.modRight in('01','03'))) and sc.level=:level").setInteger("aisle", aisle).setInteger("level", level);
        List<MCar> mCars = query.list();
        return mCars;
    }

    public static List<MCar> getFromMCar(int aisle, int level) {
        Session session = HibernateUtil.getCurrentSession();
        Query query = session.createQuery("from MCar  sc where ((sc.aisleLeft=:aisle and sc.modLeft in('02','03') ) or (sc.aisleRight=:aisle and sc.modRight in('02','03'))) and sc.level=:level").setInteger("aisle", aisle).setInteger("level", level);
        List<MCar> mCars = query.list();
        return mCars;
    }
}
