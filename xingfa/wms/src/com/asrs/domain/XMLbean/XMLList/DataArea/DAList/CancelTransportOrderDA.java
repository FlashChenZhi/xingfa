package com.asrs.domain.XMLbean.XMLList.DataArea.DAList;

import com.asrs.domain.XMLbean.XMLList.DataArea.StUnit;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-6
 * Time: 上午9:32
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "XINGFA.CancelTransportOrderDA")
public class CancelTransportOrderDA {
    @XStreamAlias("RequestId")
    private String requestId;

    @XStreamAlias("StUnit")
    private StUnit stUnit;

    @Column(name = "requestId")
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @OneToOne(targetEntity = StUnit.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "StUnitID", updatable = true)
    public StUnit getStUnit() {
        return stUnit;
    }

    public void setStUnit(StUnit stUnit) {
        this.stUnit = stUnit;
    }

    @XStreamOmitField
    private int id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
