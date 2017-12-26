package com.domain.XMLbean.XMLList.DataArea;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-6
 * Time: 上午10:20
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "ToLocation")
public class ToLocation {
    @XStreamAlias("MHA")
    private String MHA;

    @XStreamAlias("Rack")
    private String rack;

    @XStreamAlias("X")
    private String x;

    @XStreamAlias("Y")
    private String y;

    @XStreamAlias("Z")
    private String z;

    @Column(name = "MHA")
    public String getMHA() {
        return MHA;
    }

    public void setMHA(String MHA) {
        this.MHA = MHA;
    }

    @Column(name = "RACK")
    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    @Column(name = "Z")
    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    @Column(name = "Y")
    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Column(name = "X")
    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "TOLOCATION_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
