package com.asrs.domain;


import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Author: Zhouyue
 * Date: 2010-11-12
 * Time: 12:19:31
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "LOCATION")
public class Location {
    public static final String __LOCATIONNO = "locationNo";

    public static final String __AISLE = "aisle";


    public static final String __ID = "id";


    private static int bigCurrAisle = 0;

    private static int smallCurrAisle = 0;

    private int _id;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_LOCATION_ID", allocationSize = 1)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    private String _locationNo;

    @Basic
    @Column(name = "LOCATIONNO")
    public String getLocationNo() {
        return _locationNo;
    }

    public void setLocationNo(String locationNo) {
        _locationNo = locationNo;
    }

    private int _aisle;

    @Basic
    @Column(name = "AISLE")
    //. 通道，走道；侧廊
    public int getAisle() {
        return _aisle;
    }

    public void setAisle(int aisle) {
        _aisle = aisle;
    }

    private int _bank;

    @Basic
    @Column(name = "BANK")
    public int getBank() {
        return _bank;
    }

    public void setBank(int bank) {
        _bank = bank;
    }

    private int _bay;

    @Basic
    @Column(name = "BAY")
    public int getBay() {
        return _bay;
    }

    public void setBay(int bay) {
        _bay = bay;
    }

    private int _level;

    @Basic
    @Column(name = "`LEVEL`")
    public int getLevel() {
        return _level;
    }

    public void setLevel(int level) {
        _level = level;
    }

    private String _size;

    @Basic
    @Column(name = "`SIZE`")
    public String getSize() {
        return _size;
    }

    public void setSize(String size) {
        _size = size;
    }

    private String _width;

    @Basic
    @Column(name = "WIDTH")
    public String getWidth() {
        return _width;
    }

    public void setWidth(String width) {
        this._width = width;
    }

    private String _height;

    @Basic
    @Column(name = "HEIGHT")
    public String getHeight() {
        return _height;
    }

    public void setHeight(String height) {
        this._height = height;
    }

    private String _orientation;

    @Basic
    @Column(name = "ORIENTATION")
    public String getOrientation() {
        return _orientation;
    }

    public void setOrientation(String orientation) {
        this._orientation = orientation;
    }

    private int _seq;

    @Basic
    @Column(name = "SEQ")
    public int getSeq() {
        return _seq;
    }

    public void setSeq(int seq) {
        _seq = seq;
    }


    private int _version = 0;

    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return _version;
    }

    public void setVersion(int version) {
        _version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (_id != location._id) return false;
        if (_aisle != location._aisle) return false;
        if (_bank != location._bank) return false;
        if (_bay != location._bay) return false;
        if (_level != location._level) return false;
        if (_version != location._version) return false;
        if (_locationNo != null ? !_locationNo.equals(location._locationNo) : location._locationNo != null)
            return false;
        return !(_size != null ? !_size.equals(location._size) : location._size != null);

    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_locationNo != null ? _locationNo.hashCode() : 0);
        result = 31 * result + _aisle;
        result = 31 * result + _bank;
        result = 31 * result + _bay;
        result = 31 * result + _level;
        result = 31 * result + (_size != null ? _size.hashCode() : 0);
        result = 31 * result + _version;
        return result;
    }

    public static Location getByLocationNo(String locationNo) {
        Session session = HibernateUtil.getCurrentSession();

        Query q = session.createQuery(" from Location l where l.locationNo = :locationNo")
                .setString("locationNo", locationNo);
        return (Location) q.uniqueResult();
    }

    public static Location getById(int id) {
        Session session = HibernateUtil.getCurrentSession();

        return (Location) session.get(Location.class, id);
    }

}
