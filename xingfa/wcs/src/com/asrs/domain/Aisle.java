package com.asrs.domain;

import javax.persistence.*;

/**
 * Author: Zhouyue
 * Date: 2010-7-15
 * Time: 11:43:08
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "AISLE")
public class Aisle {
    public static final String __AISLENO = "aisleNo";

    public static final String __RESTRICTEDFLAG = "restrictedFlag";

    private int _aisleNo;

    private boolean _restrictedFlag;

    @Column(name = "AISLENO")
    @Id
    public int getAisleNo() {
        return _aisleNo;
    }

    public void setAisleNo(int aisleNo) {
        _aisleNo = aisleNo;
    }

    @Column(name = "RESTRICTEDFLAG")
    @Basic
    public boolean isRestrictedFlag() {
        return _restrictedFlag;
    }

    public void setRestrictedFlag(boolean restrictedFlag) {
        _restrictedFlag = restrictedFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Aisle aisle = (Aisle) o;

        if (_aisleNo != aisle._aisleNo) return false;
        if (_restrictedFlag != aisle._restrictedFlag) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _aisleNo;
        result = 31 * result + (_restrictedFlag ? 1 : 0);
        return result;
    }

}
