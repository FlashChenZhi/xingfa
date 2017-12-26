package com.asrs.domain;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/12/5.
 */
@Entity
@Table(name = "MACHINE")
public class Machine {
    private String machineNo;
    private String Run;
    private String Stop;
    private String Abnormal;
    private String EmergencyStop;
    private String Offline;
    private String BatteryOk;
    private String BatteryLow;
    private String BatteryElectricity;
    private String ErrorCode;

    public Machine() {
    }

    @Id
    @Column(name = "MACHINENO")
    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    @Basic
    @Column(name = "RUN")
    public String getRun() {
        return Run;
    }

    public void setRun(String run) {
        Run = run;
    }

    @Basic
    @Column(name = "STOP")
    public String getStop() {
        return Stop;
    }

    public void setStop(String stop) {
        Stop = stop;
    }

    @Basic
    @Column(name = "ABNORMAL")
    public String getAbnormal() {
        return Abnormal;
    }

    public void setAbnormal(String abnormal) {
        Abnormal = abnormal;
    }

    @Basic
    @Column(name = "EMERGENCYSTOP")
    public String getEmergencyStop() {
        return EmergencyStop;
    }

    public void setEmergencyStop(String emergencyStop) {
        EmergencyStop = emergencyStop;
    }

    @Basic
    @Column(name = "OFFLINE")
    public String getOffline() {
        return Offline;
    }

    public void setOffline(String offline) {
        Offline = offline;
    }

    @Basic
    @Column(name = "BATTERYOK")
    public String getBatteryOk() {
        return BatteryOk;
    }

    public void setBatteryOk(String batteryOk) {
        BatteryOk = batteryOk;
    }

    @Basic
    @Column(name = "BATTERYLOW")
    public String getBatteryLow() {
        return BatteryLow;
    }

    public void setBatteryLow(String batteryLow) {
        BatteryLow = batteryLow;
    }

    @Basic
    @Column(name = "BATTERYELECTRICITY")
    public String getBatteryElectricity() {
        return BatteryElectricity;
    }

    public void setBatteryElectricity(String batteryElectricity) {
        BatteryElectricity = batteryElectricity;
    }

    @Basic
    @Column(name = "ERRORCODE")
    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }
}
