package com.wms.domain;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/10/8.
 */
@Entity(name = "WMSUSER")
public class User {

    public User() {
    }

    private Long id;

    @Id
    @Column(name = "ID", nullable = false, length = 8)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_USER_ID", allocationSize = 1)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String userName;

    @Column(name = "USERNAME")
    @Basic
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String pwd;

    @Column(name = "PASSWORD")
    @Basic
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    private String salt;

    @Column(name = "SALT")
    @Basic
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
