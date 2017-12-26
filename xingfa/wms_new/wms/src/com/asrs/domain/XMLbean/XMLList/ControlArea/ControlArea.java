package com.asrs.domain.XMLbean.XMLList.ControlArea;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.common.DateFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-4
 * Time: 下午1:28
 */
@Entity
@Table(name = "ControlArea")
public class ControlArea {
    @XStreamAlias("Sender")
    private Sender sender;

    //    @XStreamConverter(DateConverter.class)
    @XStreamAlias("CreationDateTime")
    private String creationDateTime;
    ;

    @XStreamAlias("RefId")
    private RefId RefId;

    @XStreamOmitField
    private int id;

    public ControlArea() {
        creationDateTime = DateFormat.format(new Date(), DateFormat.YYYYMMDDHHMMSS);
    }

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "CONTROLAREA_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToOne(targetEntity = RefId.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "RefIdID", updatable = true)
    public RefId getRefId() {
        return RefId;
    }

    public void setRefId(RefId refId) {
        RefId = refId;
    }

    @OneToOne(targetEntity = Sender.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "SenderID", updatable = true)
    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    @Column(name = "creationDateTime")
    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
}
