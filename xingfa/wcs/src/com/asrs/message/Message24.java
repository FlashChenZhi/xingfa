package com.asrs.message;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class Message24 extends Message implements Serializable {
    private String id = "24";
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

    public String mcKey = "";
    public String MachineNo = "";
    public String type = "";

    public Message24() {
    }

    public Message24(String str) throws MsgException {
        if (str.length() == 9) {
            mcKey = str.substring(0, 4);
            MachineNo = str.substring(4, 8);
            type = str.substring(8, 9);
        } else {
            throw new MsgException("MsgException.Invalid_length   " + str);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.rightPad(mcKey, 4, '0'));
        sb.append(StringUtils.rightPad(MachineNo, 4, '0'));
        sb.append(StringUtils.rightPad(type, 1, '0'));
        return sb.toString();
    }
}
