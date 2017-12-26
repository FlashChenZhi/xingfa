package com.asrs.message;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class Message42 extends Message implements Serializable {
    private String id = "42";
    private String plcName = "";

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    public String Action = "";
    public String Station = "";
    public String Mode = "";

    public Message42() {
    }

    public Message42(String str) throws MsgException {
        if (str.length() == 8) {
            Action = str.substring(0, 2);
            Station = str.substring(2, 6);
            Mode = str.substring(6, 8);
        } else {
            throw new MsgException("MsgException.Invalid_length   " + str);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.rightPad(Action, 2, '0'));
        sb.append(StringUtils.rightPad(Station, 4, '0'));
        sb.append(StringUtils.rightPad(Mode, 2, '0'));
        return sb.toString();
    }
}
