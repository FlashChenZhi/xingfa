package com.wms.domain;

import com.util.hibernate.HibernateUtil;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "RETRIEVAL_ORDER_LINE", schema = "XINGFA", catalog = "")
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

    @Id
    @Column(name = "RID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rolseq")
    @SequenceGenerator(name = "rolseq", sequenceName = "R_O_LSEQ", allocationSize = 1)
    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    @Basic
    @Column(name = "SHOUHUODANHAO", nullable = true, length = 50)
    public String getShouhuodanhao() {
        return shouhuodanhao;
    }

    public void setShouhuodanhao(String shouhuodanhao) {
        this.shouhuodanhao = shouhuodanhao;
    }

    @Basic
    @Column(name = "JINHUODANHAO", nullable = true, length = 50)
    public String getJinhuodanhao() {
        return jinhuodanhao;
    }

    public void setJinhuodanhao(String jinhuodanhao) {
        this.jinhuodanhao = jinhuodanhao;
    }

    @Basic
    @Column(name = "HUOZHUDAIMA", nullable = true, length = 50)
    public String getHuozhudaima() {
        return huozhudaima;
    }

    public void setHuozhudaima(String huozhudaima) {
        this.huozhudaima = huozhudaima;
    }

    @Basic
    @Column(name = "HUOZHUMINGCHENG", nullable = true, length = 50)
    public String getHuozhumingcheng() {
        return huozhumingcheng;
    }

    public void setHuozhumingcheng(String huozhumingcheng) {
        this.huozhumingcheng = huozhumingcheng;
    }

    @Basic
    @Column(name = "CANGKUDAIMA", nullable = true, length = 50)
    public String getCangkudaima() {
        return cangkudaima;
    }

    public void setCangkudaima(String cangkudaima) {
        this.cangkudaima = cangkudaima;
    }

    @Basic
    @Column(name = "SHOUHUOLEIXING", nullable = true, length = 50)
    public String getShouhuoleixing() {
        return shouhuoleixing;
    }

    public void setShouhuoleixing(String shouhuoleixing) {
        this.shouhuoleixing = shouhuoleixing;
    }

    @Basic
    @Column(name = "HANGHAO", nullable = true, length = 50)
    public String getHanghao() {
        return hanghao;
    }

    public void setHanghao(String hanghao) {
        this.hanghao = hanghao;
    }

    @Basic
    @Column(name = "SHANGPINDAIMA", nullable = true, length = 50)
    public String getShangpindaima() {
        return shangpindaima;
    }

    public void setShangpindaima(String shangpindaima) {
        this.shangpindaima = shangpindaima;
    }

    @Basic
    @Column(name = "SHANGPINMINGCHENG", nullable = true, length = 50)
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
    @Column(name = "DANWEI", nullable = true, length = 50)
    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei;
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
}
