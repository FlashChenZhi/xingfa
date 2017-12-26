package com.wms.vo;

/**
 * Created by Administrator on 2016/10/19.
 */
public class OutArea {
    private String goodsNo;
    private Integer outNum;
    private Long allNum;

    public OutArea(){

    }
    public OutArea(String goodsNo, Integer outNum, Long allNum) {
        this.goodsNo = goodsNo;
        this.outNum = outNum;
        this.allNum = allNum;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public Integer getOutNum() {
        return outNum;
    }

    public void setOutNum(Integer outNum) {
        this.outNum = outNum;
    }

    public Long getAllNum() {
        return allNum;
    }

    public void setAllNum(Long allNum) {
        this.allNum = allNum;
    }
}
