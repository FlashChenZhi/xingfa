package com.wms.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "XINGFA.RETRIEVAL_ORDER_LINE", schema = "XINGFA", catalog = "")
@DynamicUpdate()
public class RetrievalOrderLine {

    private int rid;
    private String shouhuodanhao;
    private String jinhuodanhao;
    private String huozhudaima;
    private String huozhumingcheng;
    private String cangkudaima;
    private String shouhuoleixing;
    private String hanghao;
    private String shangpindaima;
    private String shangpinmingcheng;
    private Integer dingdanshuliang;
    private Integer wanchengdingdanshuliang;
    private String danwei;
    private String lotNo;
    private Date chuangjianshijian;
    private String fromStation;
    @Id
    @Column(name = "RID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    @Basic
    @Column(name = "SHOUHUODANHAO", nullable = true)
    public String getShouhuodanhao() {
        return shouhuodanhao;
    }

    public void setShouhuodanhao(String shouhuodanhao) {
        this.shouhuodanhao = shouhuodanhao;
    }

    @Basic
    @Column(name = "JINHUODANHAO", nullable = true)
    public String getJinhuodanhao() {
        return jinhuodanhao;
    }

    public void setJinhuodanhao(String jinhuodanhao) {
        this.jinhuodanhao = jinhuodanhao;
    }

    @Basic
    @Column(name = "HUOZHUDAIMA", nullable = true)
    public String getHuozhudaima() {
        return huozhudaima;
    }

    public void setHuozhudaima(String huozhudaima) {
        this.huozhudaima = huozhudaima;
    }

    @Basic
    @Column(name = "HUOZHUMINGCHENG", nullable = true)
    public String getHuozhumingcheng() {
        return huozhumingcheng;
    }

    public void setHuozhumingcheng(String huozhumingcheng) {
        this.huozhumingcheng = huozhumingcheng;
    }

    @Basic
    @Column(name = "CANGKUDAIMA", nullable = true)
    public String getCangkudaima() {
        return cangkudaima;
    }

    public void setCangkudaima(String cangkudaima) {
        this.cangkudaima = cangkudaima;
    }

    @Basic
    @Column(name = "SHOUHUOLEIXING", nullable = true)
    public String getShouhuoleixing() {
        return shouhuoleixing;
    }

    public void setShouhuoleixing(String shouhuoleixing) {
        this.shouhuoleixing = shouhuoleixing;
    }

    @Basic
    @Column(name = "HANGHAO", nullable = true)
    public String getHanghao() {
        return hanghao;
    }

    public void setHanghao(String hanghao) {
        this.hanghao = hanghao;
    }

    @Basic
    @Column(name = "SHANGPINDAIMA", nullable = true)
    public String getShangpindaima() {
        return shangpindaima;
    }

    public void setShangpindaima(String shangpindaima) {
        this.shangpindaima = shangpindaima;
    }

    @Basic
    @Column(name = "SHANGPINMINGCHENG", nullable = true)
    public String getShangpinmingcheng() {
        return shangpinmingcheng;
    }

    public void setShangpinmingcheng(String shangpinmingcheng) {
        this.shangpinmingcheng = shangpinmingcheng;
    }

    @Basic
    @Column(name = "DINGDANSHULIANG", nullable = true, precision = 0)
    public Integer getDingdanshuliang() {
        return dingdanshuliang;
    }

    public void setDingdanshuliang(Integer dingdanshuliang) {
        this.dingdanshuliang = dingdanshuliang;
    }

    @Basic
    @Column(name = "WANCHENGDINGDANSHULIANG", nullable = true, precision = 0)
    public Integer getWanchengdingdanshuliang() {
        return wanchengdingdanshuliang;
    }

    public void setWanchengdingdanshuliang(Integer wanchengdingdanshuliang) {
        this.wanchengdingdanshuliang = wanchengdingdanshuliang;
    }

    @Basic
    @Column(name = "DANWEI", nullable = true)
    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei;
    }

    @Basic
    @Column(name = "LOTNO", nullable = true)
    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    @Basic
    @Column(name = "FROMSTATION", nullable = true)
    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RetrievalOrderLine that = (RetrievalOrderLine) o;
        return rid == that.rid &&
                Objects.equals(shouhuodanhao, that.shouhuodanhao) &&
                Objects.equals(jinhuodanhao, that.jinhuodanhao) &&
                Objects.equals(huozhudaima, that.huozhudaima) &&
                Objects.equals(huozhumingcheng, that.huozhumingcheng) &&
                Objects.equals(cangkudaima, that.cangkudaima) &&
                Objects.equals(shouhuoleixing, that.shouhuoleixing) &&
                Objects.equals(hanghao, that.hanghao) &&
                Objects.equals(shangpindaima, that.shangpindaima) &&
                Objects.equals(shangpinmingcheng, that.shangpinmingcheng) &&
                Objects.equals(dingdanshuliang, that.dingdanshuliang) &&
                Objects.equals(wanchengdingdanshuliang, that.wanchengdingdanshuliang) &&
                Objects.equals(danwei, that.danwei);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rid, shouhuodanhao, jinhuodanhao, huozhudaima, huozhumingcheng, cangkudaima, shouhuoleixing, hanghao, shangpindaima, shangpinmingcheng, dingdanshuliang, wanchengdingdanshuliang, danwei);
    }


    public static RetrievalOrderLine getByRetrievalOrderLine(String jinhuodanhao,String hanghao) {
        org.hibernate.Query q = HibernateUtil.getCurrentSession().createQuery("from RetrievalOrderLine r where r.jinhuodanhao = :jinhuodanhao and r.hanghao=:hanghao")
                .setString("jinhuodanhao", jinhuodanhao).setString("hanghao",hanghao);

        return (RetrievalOrderLine) q.uniqueResult();
    }

    public static RetrievalOrderLine getRetrievalOrderLineByjinhuodanhao(String jinhuodanhao) {
        org.hibernate.Query q = HibernateUtil.getCurrentSession().createQuery("from RetrievalOrderLine r where r.jinhuodanhao = :jinhuodanhao ")
                .setString("jinhuodanhao", jinhuodanhao);

        return (RetrievalOrderLine) q.uniqueResult();
    }
    public Date getChuangjianshijian() {
        return chuangjianshijian;
    }

    public void setChuangjianshijian(Date chuangjianshijian) {
        this.chuangjianshijian = chuangjianshijian;
    }
}
