package com.wms.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Sku {
    private int id;
    private String skuCode;
    private Long shelfLife;
    private Integer version;
    private Long warning;
    private String skuName;
    private Integer shouhuodanhao;
    private Integer jiaohuodanhao;
    private Integer huozhudaima;
    private String huozhumingcheng;
    private Integer cangkudaima;
    private String shouhuoleixing;
    private Integer hanghao;
    private Integer dingdanshuliang;
    private String danwei;
    private Integer cunfangquyu;

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SKU_CODE", nullable = true, length = 255)
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Basic
    @Column(name = "SHELF_LIFE", nullable = true, precision = 0)
    public Long getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Long shelfLife) {
        this.shelfLife = shelfLife;
    }

    @Basic
    @Column(name = "VERSION", nullable = true, precision = 0)
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Basic
    @Column(name = "WARNING", nullable = true, precision = 0)
    public Long getWarning() {
        return warning;
    }

    public void setWarning(Long warning) {
        this.warning = warning;
    }

    @Basic
    @Column(name = "SKU_NAME", nullable = true, length = 256)
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @Basic
    @Column(name = "SHOUHUODANHAO", nullable = true, precision = 0)
    public Integer getShouhuodanhao() {
        return shouhuodanhao;
    }

    public void setShouhuodanhao(Integer shouhuodanhao) {
        this.shouhuodanhao = shouhuodanhao;
    }

    @Basic
    @Column(name = "JIAOHUODANHAO", nullable = true, precision = 0)
    public Integer getJiaohuodanhao() {
        return jiaohuodanhao;
    }

    public void setJiaohuodanhao(Integer jiaohuodanhao) {
        this.jiaohuodanhao = jiaohuodanhao;
    }

    @Basic
    @Column(name = "HUOZHUDAIMA", nullable = true, precision = 0)
    public Integer getHuozhudaima() {
        return huozhudaima;
    }

    public void setHuozhudaima(Integer huozhudaima) {
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
    @Column(name = "CANGKUDAIMA", nullable = true, precision = 0)
    public Integer getCangkudaima() {
        return cangkudaima;
    }

    public void setCangkudaima(Integer cangkudaima) {
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
    @Column(name = "HANGHAO", nullable = true, precision = 0)
    public Integer getHanghao() {
        return hanghao;
    }

    public void setHanghao(Integer hanghao) {
        this.hanghao = hanghao;
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
    @Column(name = "DANWEI", nullable = true, length = 50)
    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei;
    }

    @Basic
    @Column(name = "CUNFANGQUYU", nullable = true, precision = 0)
    public Integer getCunfangquyu() {
        return cunfangquyu;
    }

    public void setCunfangquyu(Integer cunfangquyu) {
        this.cunfangquyu = cunfangquyu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sku sku = (Sku) o;
        return id == sku.id &&
                Objects.equals(skuCode, sku.skuCode) &&
                Objects.equals(shelfLife, sku.shelfLife) &&
                Objects.equals(version, sku.version) &&
                Objects.equals(warning, sku.warning) &&
                Objects.equals(skuName, sku.skuName) &&
                Objects.equals(shouhuodanhao, sku.shouhuodanhao) &&
                Objects.equals(jiaohuodanhao, sku.jiaohuodanhao) &&
                Objects.equals(huozhudaima, sku.huozhudaima) &&
                Objects.equals(huozhumingcheng, sku.huozhumingcheng) &&
                Objects.equals(cangkudaima, sku.cangkudaima) &&
                Objects.equals(shouhuoleixing, sku.shouhuoleixing) &&
                Objects.equals(hanghao, sku.hanghao) &&
                Objects.equals(dingdanshuliang, sku.dingdanshuliang) &&
                Objects.equals(danwei, sku.danwei) &&
                Objects.equals(cunfangquyu, sku.cunfangquyu);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, skuCode, shelfLife, version, warning, skuName, shouhuodanhao, jiaohuodanhao, huozhudaima, huozhumingcheng, cangkudaima, shouhuoleixing, hanghao, dingdanshuliang, danwei, cunfangquyu);
    }
}
