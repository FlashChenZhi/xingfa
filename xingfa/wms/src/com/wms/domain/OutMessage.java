package com.wms.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by van on 2018/1/19.
 */
@Entity
@Table(name = "XINGFA.OUTMESSAGE")
@DynamicUpdate()
public class OutMessage {
    private String stationNo;
    private String sku;
    private String lotNum;
    private String qty;
    private String coustomer;
    private String carrier;
    private String toLocation;
    private String area;
    private String outDesc;
    private String caseQty;
    private String orderType;

    @Id
    @Column(name = "STATION_NO")
    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    @Basic
    @Column(name = "SKU")
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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
    @Column(name = "QTY")
    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    @Basic
    @Column(name = "COUSTOMER")
    public String getCoustomer() {
        return coustomer;
    }

    public void setCoustomer(String coustomer) {
        this.coustomer = coustomer;
    }

    @Basic
    @Column(name = "CARRIER")
    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    @Basic
    @Column(name = "TOLOCATION")
    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    @Basic
    @Column(name = "AREA")
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Basic
    @Column(name = "OUTDESC")
    public String getOutDesc() {
        return outDesc;
    }

    public void setOutDesc(String outDesc) {
        this.outDesc = outDesc;
    }

    @Basic
    @Column(name = "CASEQTY")
    public String getCaseQty() {
        return caseQty;
    }

    public void setCaseQty(String caseQty) {
        this.caseQty = caseQty;
    }

    @Basic
    @Column(name = "ORDERTYPE")
    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }


    public static void info(String blockNo, String orderNo, String palletNo) {
        OutMessage outMessage = (OutMessage) HibernateUtil.getCurrentSession().get(OutMessage.class, blockNo);

        if (outMessage != null) {

            RetrievalOrder order = RetrievalOrder.getByOrderNo(orderNo);
            outMessage.setOutDesc(order.getDesc());
            if (order.getBoxQty() != null)
                outMessage.setCaseQty(order.getBoxQty().toString());

            outMessage.setToLocation(order.getToLocation());
            outMessage.setArea(order.getArea());
            outMessage.setCarrier(order.getCarrierName());
            outMessage.setCoustomer(order.getCoustomName());
            Container container = Container.getByBarcode(palletNo);
            List<Inventory> inventories = new ArrayList<>(container.getInventories());
            BigDecimal qty = BigDecimal.ZERO;
            for (Inventory inventory : inventories) {
                outMessage.setSku(inventory.getSkuCode());
                qty = qty.add(inventory.getQty());
            }
            outMessage.setCaseQty(inventories.size() + "");
            outMessage.setQty(qty.toString());
            if (order.getJobType().equals("0")) {
                outMessage.setOrderType("订单出库");
            } else if (order.getJobType().equals("1")) {
                outMessage.setOrderType("移库出库");
            }
        }

    }

}
