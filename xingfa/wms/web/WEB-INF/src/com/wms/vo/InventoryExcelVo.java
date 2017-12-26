package com.wms.vo;

/**
 * Created by Administrator on 2016/11/18.
 */
public class InventoryExcelVo {
    private String locationNo;
    private String barcode;
    private String skuCode;
    private int qty;

    public InventoryExcelVo(String locationNo, String barcode, String skuCode, int qty) {
        this.locationNo = locationNo;
        this.barcode = barcode;
        this.skuCode = skuCode;
        this.qty = qty;
    }

    public InventoryExcelVo() {
    }

    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
