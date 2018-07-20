package com.wms.domain;


import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Author: Zhouyue
 * Date: 2010-11-12
 * Time: 12:19:31
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "XINGFA.LOCATION")
public class Location {

    public static final String __LOCATIONNO = "locationNo";

    public static final String __RESERVED = "reserved";

    public static final String __ABNORMAL = "abnormal";

    public static final String __CYCLECOUNTING = "cyclecounting";

    public static final String __AISLE = "aisle";

    public static final String __NEIGHBORLOCATION = "neighborLocation";

    public static final String __RETRIEVALRESTRICTED = "retrievalRestricted";

    public static final String __ID = "id";

    public static final String __EMPTY = "empty";
    public static final String __ASRSFLAG = "asrsFlag";

    public static final String __SEQ = "seq";

    public static final String __SEQ2 = "seq2";

    public static final String  __BAY = "bay";

    public static final String  __BANK = "bank";

    public static final String  __LEVEL = "level";

    public static final String  __OUTPOSITION = "outPosition";

    public static final String  __ACTUALAREA = "actualArea";
    private static int bigCurrAisle = 0;

    private static int smallCurrAisle = 0;

    private int _id;

    @Id
    @Column(name = "ID", nullable = false, length = 8)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private String positionType;

    @Basic
    @Column(name = "POSITIONTYPE")
    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
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
        if (_reserved != location._reserved) return false;
        if (_putawayRestricted != location._putawayRestricted) return false;
        if (_retrievalRestricted != location._retrievalRestricted) return false;
        if (_empty != location._empty) return false;
        if (_seq != location._seq) return false;
        if (_capacity != location._capacity) return false;
        if (_system != location._system) return false;
        if (_asrsFlag != location._asrsFlag) return false;
        if (_cyclecounting != location._cyclecounting) return false;
        if (_abnormal != location._abnormal) return false;
        if (_version != location._version) return false;
        if (_locationNo != null ? !_locationNo.equals(location._locationNo) : location._locationNo != null)
            return false;
        if (_size != null ? !_size.equals(location._size) : location._size != null) return false;
        if (_accessTime != null ? !_accessTime.equals(location._accessTime) : location._accessTime != null)
            return false;
        if (_type != null ? !_type.equals(location._type) : location._type != null) return false;
        if (_createDate != null ? !_createDate.equals(location._createDate) : location._createDate != null)
            return false;
        if (_createUser != null ? !_createUser.equals(location._createUser) : location._createUser != null)
            return false;
        if (_lastUpdateDate != null ? !_lastUpdateDate.equals(location._lastUpdateDate) : location._lastUpdateDate != null)
            return false;
        if (_lastUpdateUser != null ? !_lastUpdateUser.equals(location._lastUpdateUser) : location._lastUpdateUser != null)
            return false;
        if (_width != null ? !_width.equals(location._width) : location._width != null) return false;
        if (_height != null ? !_height.equals(location._height) : location._height != null) return false;
        if (_orientation != null ? !_orientation.equals(location._orientation) : location._orientation != null)
            return false;
        if (_weightFlag != null ? !_weightFlag.equals(location._weightFlag) : location._weightFlag != null)
            return false;
        return _containers != null ? _containers.equals(location._containers) : location._containers == null;

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
        result = 31 * result + (_reserved ? 1 : 0);
        result = 31 * result + (_putawayRestricted ? 1 : 0);
        result = 31 * result + (_retrievalRestricted ? 1 : 0);
        result = 31 * result + (_empty ? 1 : 0);
        result = 31 * result + (_accessTime != null ? _accessTime.hashCode() : 0);
        result = 31 * result + _seq;
        result = 31 * result + (_type != null ? _type.hashCode() : 0);
        result = 31 * result + _capacity;
        result = 31 * result + (_createDate != null ? _createDate.hashCode() : 0);
        result = 31 * result + (_createUser != null ? _createUser.hashCode() : 0);
        result = 31 * result + (_lastUpdateDate != null ? _lastUpdateDate.hashCode() : 0);
        result = 31 * result + (_lastUpdateUser != null ? _lastUpdateUser.hashCode() : 0);
        result = 31 * result + (_system ? 1 : 0);
        result = 31 * result + (_asrsFlag ? 1 : 0);
        result = 31 * result + (_cyclecounting ? 1 : 0);
        result = 31 * result + (_abnormal ? 1 : 0);
        result = 31 * result + (_width != null ? _width.hashCode() : 0);
        result = 31 * result + (_height != null ? _height.hashCode() : 0);
        result = 31 * result + (_orientation != null ? _orientation.hashCode() : 0);
        result = 31 * result + (_weightFlag != null ? _weightFlag.hashCode() : 0);
        result = 31 * result + _version;
        result = 31 * result + (_containers != null ? _containers.hashCode() : 0);
        return result;
    }

    private Collection<Container> _containers = new ArrayList<Container>();

    @OneToMany(mappedBy = "location")
    public Collection<Container> getContainers() {
        return _containers;
    }

    public void setContainers(Collection<Container> containers) {
        _containers = containers;
    }


    private Collection<TransportOrderLog> _transportOrderLogFL = new ArrayList<TransportOrderLog>();

    @OneToMany(mappedBy = "fromLocation")
    public Collection<TransportOrderLog> getTransportOrderLogFL() {
        return _transportOrderLogFL;
    }

    public void setTransportOrderLogFL(Collection<TransportOrderLog> transportOrderLogFL) {
        this._transportOrderLogFL = transportOrderLogFL;
    }

    private Collection<TransportOrderLog> _transportOrderLogTL = new ArrayList<TransportOrderLog>();

    @OneToMany(mappedBy = "toLocation")
    public Collection<TransportOrderLog> getTransportOrderLogTL() {
        return _transportOrderLogTL;
    }

    public void setTransportOrderLogTL(Collection<TransportOrderLog> transportOrderLogTL) {
        this._transportOrderLogTL = transportOrderLogTL;
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
     * 欧普要求整托，商品批次一直
     *
     * @param skuCode
     * @param position
     * @return
     */
    public static Location getEmptyLocation(String skuCode,String lotNo,String position,String loadType) {
        //loadType = 01 高 02 低
        Location location=null;

        if(StringUtils.isNotBlank(loadType)){
            location = getLocation(skuCode,lotNo,position,loadType);
            if(location == null && "02".equals(loadType)){
                location = getLocation(skuCode,lotNo,position,"01");
            }
        }
        return location;
    }

    public static Location getLocation(String skuCode,String lotNo,String position,String loadType){
        Session session = HibernateUtil.getCurrentSession();
        //存在同批次的库存同一边的可用
        Query q = session.createQuery("from Location l where exists( select 1 from Inventory i where l.bay=i.container.location.bay and l.actualArea=i.container.location.actualArea " +
                " and l.level =i.container.location.level  and i.skuCode=:skuCode " + (StringUtils.isBlank(lotNo) ? " and (i.lotNum is null or i.lotNum = '') " : " and i.lotNum = :lotNum ") +
                " and  l.position=i.container.location.position and i.container.location.seq<l.seq ) and not exists (select 1 from Container c where l.bay=c.location.bay and l.actualArea=c.location.actualArea " +
                " and l.level =c.location.level and  l.position=c.location.position and c.location.seq<l.seq and c.location.seq2>l.seq2 and c.reserved = true ) " +
                " and l.empty=true  and l.position=:po and l.reserved=false and l.asrsFlag = true and l.putawayRestricted = false and l.positionType = :positionType order by l.seq")
                .setString("skuCode", skuCode).setParameter("po", position).setParameter("positionType",loadType);
        if(StringUtils.isNotBlank(lotNo)){
            q.setString("lotNum",lotNo);
        }
        if (!q.list().isEmpty()) {
            return (Location) q.list().get(0);
        } else {
            //查找正在执行的入库任务
            q = session.createQuery("from Location l where exists( select j from Job j,InventoryView  v where j.container =v.palletCode" +
                    " and l.actualArea= j.toLocation.actualArea " +
                    " and l.level = j.toLocation.level and l.bay = j.toLocation.bay and v.skuCode=:skuCode " + (StringUtils.isBlank(lotNo) ? " and (v.lotNum is null or v.lotNum = '') " : " and v.lotNum = :lotNum ") +
                    " and l.position=j.toLocation.position and j.toLocation.seq <l.seq )  " +
                    "and l.empty=true and l.position=:po and l.reserved=false and l.asrsFlag = true and l.putawayRestricted = false and l.positionType = :positionType order by l.seq asc")
                    .setParameter("po", position).setParameter("skuCode", skuCode).setParameter("positionType",loadType);
            if(StringUtils.isNotBlank(lotNo)){
                q.setString("lotNum",lotNo);
            }
            if (!q.list().isEmpty()) {
                return (Location) q.list().get(0);
            } else {
                //查找一个空的先进先出的巷道
                q = session.createQuery("from Location l where not exists (select 1 from Location ol where ol.bay = l.bay and (ol.reserved=true or ol.empty=false ) " +
                        "and l.level =ol.level and l.actualArea=ol.actualArea and l.position=ol.position )" +
                        " and l.empty=true  and l.position=:po and l.reserved=false and l.asrsFlag = true and l.putawayRestricted = false and not exists (select 1 from Location l2 where l.bay=l2.bay and l.actualArea=l2.actualArea " +
                        " and l.level =l2.level and  l.position=l2.position and l2.seq <> l2.seq2 ) and l.positionType = :positionType  order by l.bay asc,level asc,actualArea asc,seq asc ")
                        .setParameter("po", position).setParameter("positionType",loadType);
                if (!q.list().isEmpty()) {
                    return (Location) q.list().get(0);
                }else{
                    //查找一个空的巷道
                    q = session.createQuery("from Location l where not exists (select 1 from Location ol where ol.bay = l.bay and (ol.reserved=true or ol.empty=false ) " +
                            "and l.level =ol.level and l.actualArea=ol.actualArea and l.position=ol.position )" +
                            " and l.empty=true  and l.position=:po and l.reserved=false and l.asrsFlag = true and l.putawayRestricted = false and l.positionType = :positionType order by l.bay asc,level asc,actualArea asc,seq asc ")
                            .setParameter("po", position).setParameter("positionType",loadType);
                    if (!q.list().isEmpty()) {
                        return (Location) q.list().get(0);
                    }
                }
            }
            return null;
        }
    }
//    /**
//     * @return
//     */
//    public static Location getPartPalletLocation() {
//
//        //超找非整托货位
//        Session session = HibernateUtil.getCurrentSession();
//
//        Query q = session.createQuery("from Location l where exists( select 1 rom Inventory i where l.bay=i.container.location.bay and l.actualArea=i.container.location.actualArea and l.level =i.container.location.level and i.container.status='非整托')");
//
//        if (!q.list().isEmpty()) {
//
//            return (Location) q.list().get(0);
//
//        }else {
//
//            //查找正在执行的入库任务
//            q = session.createQuery("from Location l where exists( select j from Job j,InventoryView  v where j.container =v.palletCode" +
//                    " and l.actualArea= j.toLocation.actualArea " +
//                    " and l.level = j.toLocation.level and l.bay = j.toLocation.bay and  l.position=j.toLocation.position and v.status='非整托' )  " +
//                    "and l.empty=true and l.position=:po and l.reserved=false and l.asrsFlag = true and l.putawayRestricted = false order by l.seq asc");
//            if (!q.list().isEmpty()) {
//                return (Location) q.list().get(0);
//            } else {
//                //查找一个空的巷道
//                q = session.createQuery("from Location l where not exists (select 1 from Location ol where ol.bay = l.bay and (ol.reserved=true or ol.empty=false ) " +
//                        "and l.level =ol.level and l.actualArea=ol.actualArea and l.position=ol.position )" +
//                        " and l.empty=true  and l.position=:po and l.reserved=false and l.asrsFlag = true and l.putawayRestricted = false order by l.bay asc,level asc,actualArea asc,seq asc ");
//                if (!q.list().isEmpty()) {
//                    return (Location) q.list().get(0);
//                }
//            }
//            return null;
//        }
//
//    }

    /**
     * 查找空托盘存储货位
     *
     * @return
     */
    public static Location getEmpteyPalletLocation() {

        return null;
    }

}
