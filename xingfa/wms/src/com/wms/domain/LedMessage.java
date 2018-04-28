package com.wms.domain;

import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;

/**
 * Created by Administrator on 2018/4/28.
 */
@Entity
@Table(name = "XINGFA.LEDMESSAGE")
public class LedMessage {
    private String ledNo;

    @Column(name = "LEDNO")
    @Id
    public String getLedNo() {
        return ledNo;
    }

    public void setLedNo(String ledNo) {
        this.ledNo = ledNo;
    }

    private String _ipAddress;

    @Column(name = "IPADDRESS")
    @Basic
    public String getIpAddress()
    {
        return _ipAddress;
    }

    public void setIpAddress(String ipAddress)
    {
        _ipAddress = ipAddress;
    }

    private String message1;

    @Column(name = "MESSAGE1")
    @Basic
    public String getMessage1() {
        return message1;
    }

    public void setMessage1(String message1) {
        this.message1 = message1;
    }

    private String message2;

    @Column(name = "MESSAGE2")
    @Basic
    public String getMessage2() {
        return message2;
    }

    public void setMessage2(String message2) {
        this.message2 = message2;
    }

    private String message3;

    @Column(name = "MESSAGE3")
    @Basic
    public String getMessage3() {
        return message3;
    }

    public void setMessage3(String message3) {
        this.message3 = message3;
    }

    private String message4;

    @Column(name = "MESSAGE4")
    @Basic
    public String getMessage4() {
        return message4;
    }

    public void setMessage4(String message4) {
        this.message4 = message4;
    }

    private boolean processed;

    @Column(name = "PROCESSED")
    @Basic
    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    private String mckey;

    @Column(name = "MCKEY")
    @Basic
    public String getMckey() {
        return mckey;
    }

    public void setMckey(String mckey) {
        this.mckey = mckey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LedMessage that = (LedMessage) o;

        if (processed != that.processed) return false;
        if (ledNo != null ? !ledNo.equals(that.ledNo) : that.ledNo != null) return false;
        if (_ipAddress != null ? !_ipAddress.equals(that._ipAddress) : that._ipAddress != null) return false;
        if (message1 != null ? !message1.equals(that.message1) : that.message1 != null) return false;
        if (message2 != null ? !message2.equals(that.message2) : that.message2 != null) return false;
        if (message3 != null ? !message3.equals(that.message3) : that.message3 != null) return false;
        if (message4 != null ? !message4.equals(that.message4) : that.message4 != null) return false;
        return mckey != null ? mckey.equals(that.mckey) : that.mckey == null;
    }

    @Override
    public int hashCode() {
        int result = ledNo != null ? ledNo.hashCode() : 0;
        result = 31 * result + (_ipAddress != null ? _ipAddress.hashCode() : 0);
        result = 31 * result + (message1 != null ? message1.hashCode() : 0);
        result = 31 * result + (message2 != null ? message2.hashCode() : 0);
        result = 31 * result + (message3 != null ? message3.hashCode() : 0);
        result = 31 * result + (message4 != null ? message4.hashCode() : 0);
        result = 31 * result + (processed ? 1 : 0);
        result = 31 * result + (mckey != null ? mckey.hashCode() : 0);
        return result;
    }

    public static LedMessage getByLedNo(String ledNo){
        return (LedMessage) HibernateUtil.getCurrentSession().get(LedMessage.class,ledNo);
    }

    public static void show(String ledNo,String message1,String message2,String message3,String message4,String mckey){
        LedMessage ledMessage = LedMessage.getByLedNo(ledNo);
        if(ledMessage != null && (StringUtils.isBlank(mckey) || !mckey.equals(ledMessage.getMckey()))){
            ledMessage.setMessage1(message1);
            ledMessage.setMessage2(message2);
            ledMessage.setMessage3(message3);
            ledMessage.setMessage4(message4);
            ledMessage.setMckey(mckey);
            ledMessage.setProcessed(true);
        }
    }

    public static void clear(String ledNo){
        show(ledNo,ledNo,"","","","");
    }
}
