package com.domain.XMLbean.XMLList.DataArea;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:52
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "XINGFA.XMLLocation")
public class XMLLocation {
    @XStreamAlias("MHA")
    private String MHA = StringUtils.EMPTY;

    @Column(name = "MHA")
    public String getMHA() {
        return MHA;
    }

    public void setMHA(String MHA) {
        this.MHA = MHA;
    }

    @XStreamImplicit(itemFieldName = "Rack")
    private List<String> rack;

    public List<String> getRack() {
        return rack;
    }

    public void setRack(List<String> rack) {
        this.rack = rack;
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
