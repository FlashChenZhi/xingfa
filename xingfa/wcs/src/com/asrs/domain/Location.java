package com.asrs.domain;


import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.*;
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

    public static final String MISS = "0";
    public static final String LEFT = "1";
    public static final String RIGHT = "2";

    private Double width;
    private Double height;
    private int _id;

    @Id
    @Column(name = "ID", nullable = false, length = 8)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_LOCATION_ID", allocationSize = 1)
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
    @Column(name = "`LEV`")
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

    private boolean _reserved;

    @Basic
    @Column(name = "RESERVED")
    //保留的
    public boolean getReserved() {
        return _reserved;
    }

    public void setReserved(boolean reserved) {
        _reserved = reserved;
    }


    private boolean _putawayRestricted;

    @Basic
    @Column(name = "PUTAWAYRESTRICTED")
    public boolean isPutawayRestricted() {
        return _putawayRestricted;
    }

    public void setPutawayRestricted(boolean putawayRestricted) {
        _putawayRestricted = putawayRestricted;
    }

    private boolean _retrievalRestricted;

    @Basic
    @Column(name = "RETRIEVALRESTRICTED")
    //检索受限
    public boolean isRetrievalRestricted() {
        return _retrievalRestricted;
    }

    public void setRetrievalRestricted(boolean retrievalRestricted) {
        _retrievalRestricted = retrievalRestricted;
    }

    private boolean _empty;

    @Basic
    @Column(name = "EMPTY")
    public boolean isEmpty() {
        return _empty;
    }

    public void setEmpty(boolean empty) {
        _empty = empty;
    }

    private String position;

    @Basic
    @Column(name = "POSITION")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    private Date _accessTime;

    @Basic
    @Column(name = "ACCESSTIME")
    public Date getAccessTime() {
        return _accessTime;
    }

    public void setAccessTime(Date accessTime) {
        _accessTime = accessTime;
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

    private int _seq2;

    @Basic
    @Column(name = "SEQ2")
    public int getSeq2() {
        return _seq2;
    }

    public void setSeq2(int seq2) {
        _seq2 = seq2;
    }

    private String _type;

    @Basic
    @Column(name = "TYPE")
    public String getType() {
        return _type;
    }

    public void setType(String type) {
        _type = type;
    }


    private int _capacity;

    @Basic
    @Column(name = "CAPACITY")
    //容量
    public int getCapacity() {
        return _capacity;
    }

    public void setCapacity(int capacity) {
        _capacity = capacity;
    }


    private Date _createDate;

    @Column(name = "CREATEDATE")
    @Basic
    public Date getCreateDate() {
        return _createDate;
    }

    public void setCreateDate(Date createDate) {
        _createDate = createDate;
    }

    private String _createUser;

    @Column(name = "CREATEUSER")
    @Basic
    public String getCreateUser() {
        return _createUser;
    }

    public void setCreateUser(String createUser) {
        _createUser = createUser;
    }

    private Date _lastUpdateDate;

    @Column(name = "LASTUPDATEDATE")
    @Basic
    public Date getLastUpdateDate() {
        return _lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        _lastUpdateDate = lastUpdateDate;
    }

    private String _lastUpdateUser;

    @Column(name = "LASTUPDATEUSER")
    @Basic
    public String getLastUpdateUser() {
        return _lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        _lastUpdateUser = lastUpdateUser;
    }

    private boolean _system;

    @Basic
    @Column(name = "SYSTEM")
    public boolean isSystem() {
        return _system;
    }

    public void setSystem(boolean system) {
        _system = system;
    }

    private boolean _asrsFlag;

    @Basic
    @Column(name = "ASRSFLAG")
    public boolean isAsrsFlag() {
        return _asrsFlag;
    }

    public void setAsrsFlag(boolean asrsFlag) {
        _asrsFlag = asrsFlag;
    }

    private boolean _cyclecounting;

    @Basic
    @Column(name = "CYCLECOUNTING")
    public boolean getCyclecounting() {
        return _cyclecounting;
    }

    public void setCyclecounting(boolean cyclecounting) {
        _cyclecounting = cyclecounting;
    }

    private boolean _abnormal;

    @Basic
    @Column(name = "ABNORMAL")
    //反常的，不规则的；变态的
    public boolean isAbnormal() {
        return _abnormal;
    }

    public void setAbnormal(boolean abnormal) {
        _abnormal = abnormal;
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

    private String _weightFlag;

    @Basic
    @Column(name = "WEIGHTFLAG")
    public String getWeightFlag() {
        return _weightFlag;
    }

    public void setWeightFlag(String weightFlag) {
        _weightFlag = weightFlag;
    }

    private int _version = 0;

    private String _skuType;

    @Basic
    @Column(name = "SKUTYPE")
    public String getSkuType() {
        return _skuType;
    }

    public void setSkuType(String skuType) {
        this._skuType = skuType;
    }

    private String actualArea;

    @Basic
    @Column(name = "AREA")
    public String getActualArea() {
        return actualArea;
    }

    public void setActualArea(String actureArea) {
        this.actualArea = actureArea;
    }


    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return _version;
    }

    public void setVersion(int version) {
        _version = version;
    }


    private String outPosition;

    @Basic
    @Column(name = "OUTPOSITION")
    public String getOutPosition() {
        return outPosition;
    }

    public void setOutPosition(String outPosition) {
        this.outPosition = outPosition;
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

    /**
     * @param i  　　bank
     * @param i1 bay
     * @param i2 level
     * @return
     */
    public static Location getByBankBayLevel(int i, int i1, int i2,String position) {

        Query q = HibernateUtil.getCurrentSession().createQuery("from Location  l where l.bank=:b and l.bay =:ba and l.level=:lv and l.position=:po")
                .setParameter("b", i).setParameter("ba", i1).setParameter("lv", i2).setParameter("po",position);

        return (Location) q.uniqueResult();
    }
}
