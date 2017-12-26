package com.test.blocks;

import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/10/12.
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "2")
public class Lift extends Block {
    private int level;
    private String reservedMcKey;

    @Basic
    @Column(name = "`LEVEL`")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Basic
    @Column(name = "reservedMcKey")
    public String getReservedMcKey() {
        return reservedMcKey;
    }

    public void setReservedMcKey(String reservedMcKey) {
        this.reservedMcKey = reservedMcKey;
    }

    @Transient
    public boolean isOkToGo(String mcKey, int level) {
        if (!this.loaded && !this.waitingResponse && mcKey.equals(this.reservedMcKey) && this.level == level) {
            return true;
        } else {
            return false;
        }
    }
}
