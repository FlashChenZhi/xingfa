package com.asrs.domain;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Zhouyue
 * Date: 2010-6-29
 * Time: 11:43:18
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "ASRSMACHINE")
public class AsrsMachine {
    private int _id;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_ASRSMACHINE_ID", allocationSize = 1)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    private String _macType;

    @Column(name = "MACTYPE")
    @Basic
    public String getMacType() {
        return _macType;
    }

    public void setMacType(String macType) {
        _macType = macType;
    }

    private String _macNo;

    @Column(name = "MACNO")
    @Basic
    public String getMacNo() {
        return _macNo;
    }

    public void setMacNo(String macNo) {
        _macNo = macNo;
    }

    private String _status;

    @Column(name = "STATUS")
    @Basic
    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        _status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AsrsMachine that = (AsrsMachine) o;

        if (_id != that._id) return false;
        if (_macNo != null ? !_macNo.equals(that._macNo) : that._macNo != null) return false;
        if (_macType != null ? !_macType.equals(that._macType) : that._macType != null) return false;
        if (_status != null ? !_status.equals(that._status) : that._status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_macType != null ? _macType.hashCode() : 0);
        result = 31 * result + (_macNo != null ? _macNo.hashCode() : 0);
        result = 31 * result + (_status != null ? _status.hashCode() : 0);
        return result;
    }

    private List<Route> _routes = new ArrayList<Route>();

    @ManyToMany
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinTable(name = "ROUTEMACHINE",
            joinColumns = {@JoinColumn(name = "MACID")},
            inverseJoinColumns = {@JoinColumn(name = "ROUTEID")})
    public List<Route> getRoutes() {
        return _routes;
    }

    public void setRoutes(List<Route> routes) {
        _routes = routes;
    }

}
