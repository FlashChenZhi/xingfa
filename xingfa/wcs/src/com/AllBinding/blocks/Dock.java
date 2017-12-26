package com.AllBinding.blocks;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/12/13.
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "4")
public class Dock extends Block {
    private int level;

    @Basic
    @Column(name = "`LEVEL`")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
