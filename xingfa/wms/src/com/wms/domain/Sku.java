package com.wms.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Version;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wangfan
 * Created on 2017/2/23.
 * 商品数据
 */
@Entity
@Table(name = "XINGFA.SKU")
@DynamicUpdate()
public class Sku {

    public static final String __SKUCODE = "skuCode";
    public static final String COL_CODE = "skuCode";
    private int id;
    private String skuCode;//商品代码
    private String skuName;//商品名称
    private int shelfLift;//存储周期
    private int warning;//提前预警时间
    private int version;
    private String shouhuodanhao;
    private String jiaohuodanhao;
    private String huozhudaima;
    private String huozhumingcheng;
    private String cangkudaima;
    private String shouhuoleixing;
    private String hanghao;
    private Integer dingdanshuliang;
    private String danwei;
    private Integer cunfangquyu;
    private Date chuangjianshijian;
    private String skuType;

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
    @Column(name = "SKU_CODE")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Basic
    @Column(name = "SHELF_LIFE")
    public int getShelfLift() {
        return shelfLift;
    }

    public void setShelfLift(int shelfLift) {
        this.shelfLift = shelfLift;
    }

    @Basic
    @Column(name = "WARNING")
    public int getWarning() {
        return warning;
    }

    public void setWarning(int warning) {
        this.warning = warning;
    }

    @Basic
    @Column(name = "SKU_NAME")
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Basic
    @Column(name = "shouhuodanhao")
    public String getShouhuodanhao() {
        return shouhuodanhao;
    }

    public void setShouhuodanhao(String shouhuodanhao) {
        this.shouhuodanhao = shouhuodanhao;
    }

    @Basic
    @Column(name = "jiaohuodanhao")
    public String getJiaohuodanhao() {
        return jiaohuodanhao;
    }

    public void setJiaohuodanhao(String jiaohuodanhao) {
        this.jiaohuodanhao = jiaohuodanhao;
    }

    @Basic
    @Column(name = "huozhudaima")
    public String getHuozhudaima() {
        return huozhudaima;
    }

    public void setHuozhudaima(String huozhudaima) {
        this.huozhudaima = huozhudaima;
    }

    @Basic
    @Column(name = "huozhumingcheng")
    public String getHuozhumingcheng() {
        return huozhumingcheng;
    }

    public void setHuozhumingcheng(String huozhumingcheng) {
        this.huozhumingcheng = huozhumingcheng;
    }

    @Basic
    @Column(name = "cangkudaima")
    public String getCangkudaima() {
        return cangkudaima;
    }

    public void setCangkudaima(String cangkudaima) {
        this.cangkudaima = cangkudaima;
    }

    @Basic
    @Column(name = "shouhuoleixing")
    public String getShouhuoleixing() {
        return shouhuoleixing;
    }

    public void setShouhuoleixing(String shouhuoleixing) {
        this.shouhuoleixing = shouhuoleixing;
    }

    @Basic
    @Column(name = "hanghao")
    public String getHanghao() {
        return hanghao;
    }

    public void setHanghao(String hanghao) {
        this.hanghao = hanghao;
    }

    @Basic
    @Column(name = "dingdanshuliang")
    public Integer getDingdanshuliang() {
        return dingdanshuliang;
    }

    public void setDingdanshuliang(Integer dingdanshuliang) {
        this.dingdanshuliang = dingdanshuliang;
    }

    @Basic
    @Column(name = "danwei")
    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei;
    }

    @Basic
    @Column(name = "cunfangquyu")
    public Integer getCunfangquyu() {
        return cunfangquyu;
    }

    public void setCunfangquyu(Integer cunfangquyu) {
        this.cunfangquyu = cunfangquyu;
    }

    @Basic
    @Column(name = "chuangjianshijian")
    public Date getChuangjianshijian() {
        return chuangjianshijian;
    }

    public void setChuangjianshijian(Date chuangjianshijian) {
        this.chuangjianshijian = chuangjianshijian;
    }

    @Basic
    @Column(name = "SKUTYPE")
    public String getSkuType() {
        return skuType;
    }

    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }

    public static Sku getByCode(String skuCode) {

        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from Sku where skuCode =:skuCode");
        query.setParameter("skuCode", skuCode);
        query.setMaxResults(1);
        return (Sku) query.uniqueResult();
    }

}
