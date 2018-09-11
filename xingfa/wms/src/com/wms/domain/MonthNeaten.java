package com.wms.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 10:33 2018/9/4
 * @Description: 月结
 * @Modified By:
 */
@Entity
@Table(name = "MONTHNEATEN")
@DynamicUpdate()
public class MonthNeaten {
    private int id;
    private String skuCode;
    private String skuName;
    private String lotNum;
    private String date;
    private int benginningInventory;//期初
    private int inStorage;//入库
    private int outStorage;//出库
    private int carryover;//结余

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SKUCODE")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Basic
    @Column(name = "SKUNAME")
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @Basic
    @Column(name = "LOTNUM")
    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    @Basic
    @Column(name = "DATE")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Basic
    @Column(name = "BEGINNINGINVENTORY")
    public int getBenginningInventory() {
        return benginningInventory;
    }

    public void setBenginningInventory(int benginningInventory) {
        this.benginningInventory = benginningInventory;
    }

    @Basic
    @Column(name = "INSTORAGE")
    public int getInStorage() {
        return inStorage;
    }

    public void setInStorage(int inStorage) {
        this.inStorage = inStorage;
    }

    @Basic
    @Column(name = "OUTSTORAGE")
    public int getOutStorage() {
        return outStorage;
    }

    public void setOutStorage(int outStorage) {
        this.outStorage = outStorage;
    }

    @Basic
    @Column(name = "CARRYOVER")
    public int getCarryover() {
        return carryover;
    }

    public void setCarryover(int carryover) {
        this.carryover = carryover;
    }

    /*private Collection<MonthNeatenDetail> monthNeatenDetail = new ArrayList<MonthNeatenDetail>();

    @OneToMany(mappedBy = "monthNeaten")
    public Collection<MonthNeatenDetail> getMonthNeatenDetail() {
        return monthNeatenDetail;
    }

    public void setMonthNeatenDetail(Collection<MonthNeatenDetail> monthNeatenDetail) {
        this.monthNeatenDetail = monthNeatenDetail;
    }*/

    //根据日期查询结存数据
    public static long getMonthNeatenByDate(String date){
        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("select count (*) as count from MonthNeaten where date=:date ");
        query.setString("date", date);
        long count = (long) query.uniqueResult();
        return count;
    }

    //根据日期倒序查询结存数据
    public static List<MonthNeaten> getMonthNeatenByDateDesc(){
        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from MonthNeaten d where d.date=(select max(date) from MonthNeaten ) ");

        return query.list();
    }
    //根据日期,类型，批次查询结存数据
    public static MonthNeaten getMonthNeatenByConditions(String date,String skuCode,String lotNum){
        Query query = HibernateUtil.getCurrentSession().createQuery("from MonthNeaten d where " +
                "d.date=:date and d.skuCode=:skuCode and d.lotNum=:lotNum ");
        query.setParameter("date", date);
        query.setParameter("skuCode", skuCode);
        query.setParameter("lotNum", lotNum);
        query.setMaxResults(1);
        return (MonthNeaten)query.uniqueResult();
    }

}
