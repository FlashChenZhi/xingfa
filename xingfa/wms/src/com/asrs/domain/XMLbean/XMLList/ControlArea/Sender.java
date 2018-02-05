package com.asrs.domain.XMLbean.XMLList.ControlArea;

import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-4
 * Time: 下午1:29
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "XINGFA.Sender")
public class Sender {
    @XStreamAlias("Division")
    private String division = XMLConstant.COM_DIVISION;

    @XStreamAlias("Confirmation")
    private String confirmation;

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

    @Column(name = "division")
    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    @Column(name = "confirmation")
    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }
}
