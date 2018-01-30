package com.wms.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.*;

import javax.persistence.*;

/**
 * Created by van on 2018/1/15.
 */
@Entity
@Table(name = "IN_STRATEGY")
public class InStrategy {

    private int id;
    private String skuCode;
    private String skuName;
    private String position;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_IN_STRATEGY_ID", allocationSize = 1)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SKU_CODE")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Basic
    @Column(name = "SKU_NAME")
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @Basic
    @Column(name = "POSITION")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Transient
    public static InStrategy getInStrategy(String skuCode) {
        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from InStrategy where skuCode=:skuCode");
        query.setParameter("skuCode", skuCode);
        query.setMaxResults(1);
        return (InStrategy) query.uniqueResult();
    }

}