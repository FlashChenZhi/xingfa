package com.wms.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 15:03 2018/9/12
 * @Description:
 * @Modified By:
 */
@Entity
@Table(name = "INSTORAGESTRATEGY")
@DynamicUpdate()
public class InStorageStrategy {
    private int id;
    private String skuCode;//商品代码
    private String lotNum;//商品名称
    private int bay;
    private int level;
    private String bayLevel;
    private Date date;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "LOTNUM")
    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
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
    @Column(name = "LEV")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Basic
    @Column(name = "BAYLEV")
    public String getBayLevel() {
        return bayLevel;
    }

    public void setBayLevel(String bayLevel) {
        this.bayLevel = bayLevel;
    }

    @Basic
    @Column(name = "DATE")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /*
     * @author：ed_chen
     * @date：2018/9/12 16:28
     * @description： 根据具体条件查询入库策略
     * @param skuCode
     * @param lotNum
     * @param bay
     * @param level
     * @return：com.wms.domain.InStroageStrategy
     */
    public static InStorageStrategy findInStroageStrategyById (int id){
        Session session = HibernateUtil.getCurrentSession();
        Query query =session.createQuery("from InStorageStrategy where id=:id");
        query.setParameter("id", id);
        query.setMaxResults(1);

        return (InStorageStrategy)query.uniqueResult();
    }

    /*
     * @author：ed_chen
     * @date：2018/9/12 16:28
     * @description： 根据具体条件查询入库策略
     * @param skuCode
     * @param lotNum
     * @param bay
     * @param level
     * @return：com.wms.domain.InStroageStrategy
     */
    public static InStorageStrategy findInStroageStrategy (String skuCode,String lotNum,int bay,int level){
        Session session = HibernateUtil.getCurrentSession();
        Query query =session.createQuery("from InStorageStrategy where skuCode=:skuCode " +
                "and lotNum=:lotNum and bay=:bay and level=:level");
        query.setParameter("skuCode", skuCode);
        query.setParameter("lotNum", lotNum);
        query.setParameter("bay", bay);
        query.setParameter("level", level);
        query.setMaxResults(1);

        return (InStorageStrategy)query.uniqueResult();
    }

    /*
     * @author：ed_chen
     * @date：2018/9/14 11:44
     * @description：根据商品代码和批次查询入库策略
     * @param skuCode
     * @param lotNum
     * @return：com.wms.domain.InStorageStrategy
     */
    public static List<InStorageStrategy> findInStroageStrategyBaySL (String skuCode,String lotNum){
        Session session = HibernateUtil.getCurrentSession();
        Query query =session.createQuery("from InStorageStrategy where skuCode=:skuCode " +
                "and lotNum=:lotNum ");
        query.setParameter("skuCode", skuCode);
        query.setParameter("lotNum", lotNum);

        return query.list();
    }

    /*
     * @author：ed_chen
     * @date：2018/9/14 11:45
     * @description：根据列层查询入库策略
     * @param bay
     * @param level
     * @return：java.util.List<com.wms.domain.InStorageStrategy>
     */
    public static List<InStorageStrategy> findInStroageStrategyByBayLev ( int bay, int level){
        Session session = HibernateUtil.getCurrentSession();
        Query query =session.createQuery("from InStorageStrategy where  " +
                " bay=:bay and level=:level");
        query.setParameter("bay", bay);
        query.setParameter("level", level);

        return query.list();
    }



}
