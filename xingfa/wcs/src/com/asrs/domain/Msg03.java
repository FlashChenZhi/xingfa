package com.asrs.domain;

import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MSG03")
public class Msg03 {
    private int id;
    private String plcName = "";
    private String mcKey = "";
    private String MachineNo = "";
    private String CycleOrder = "";
    private String JobType = "";
    private String Height = "";
    private String Width = "";
    private String Bank = "";
    private String Bay = "";
    private String Level = "";
    private String Station = "";
    private String Dock = "";
    private Date lastSendDate;
    private Date createDate;
    private boolean received;
    private String sendStatus;
    private String msgType;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ_MSG03_ID", allocationSize = 1)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "PLCNAME")
    @Basic
    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    @Basic
    @Column(name = "MCKEY")
    public String getMcKey() {
        return mcKey;
    }

    public void setMcKey(String mcKey) {
        this.mcKey = mcKey;
    }

    @Column(name = "MACHINENO")
    @Basic
    public String getMachineNo() {
        return MachineNo;
    }

    public void setMachineNo(String machineNo) {
        MachineNo = machineNo;
    }

    @Column(name = "CYCLEORDER")
    @Basic
    public String getCycleOrder() {
        return CycleOrder;
    }

    public void setCycleOrder(String cycleOrder) {
        CycleOrder = cycleOrder;
    }

    @Column(name = "JOBTYPE")
    @Basic
    public String getJobType() {
        return JobType;
    }

    public void setJobType(String jobType) {
        JobType = jobType;
    }

    @Column(name = "HEIGHT")
    @Basic
    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    @Column(name = "WIDTH")
    @Basic
    public String getWidth() {
        return Width;
    }

    public void setWidth(String width) {
        Width = width;
    }

    @Column(name = "BANK")
    @Basic
    public String getBank() {
        return Bank;
    }

    public void setBank(String bank) {
        Bank = bank;
    }

    @Column(name = "BAY")
    @Basic
    public String getBay() {
        return Bay;
    }

    public void setBay(String bay) {
        Bay = bay;
    }

    @Column(name = "`LEVEL`")
    @Basic
    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    @Column(name = "STATION")
    @Basic
    public String getStation() {
        return Station;
    }

    public void setStation(String station) {
        Station = station;
    }

    @Column(name = "DOCK")
    @Basic
    public String getDock() {
        return Dock;
    }

    public void setDock(String dock) {
        Dock = dock;
    }


    @Column(name = "LASTSENDDATE")
    @Basic
    public Date getLastSendDate() {
        return lastSendDate;
    }

    public void setLastSendDate(Date lastSendDate) {
        this.lastSendDate = lastSendDate;
    }

    @Column(name = "CREATEDATE")
    @Basic
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "RECEIVED")
    @Basic
    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    @Basic
    @Column(name = "SENDSTATUS")
    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    @Basic
    @Column(name = "MSGTYPE")
    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Msg03 msg03 = (Msg03) o;

        if (id != msg03.id) return false;
        if (received != msg03.received) return false;
        if (Bank != null ? !Bank.equals(msg03.Bank) : msg03.Bank != null) return false;
        if (Bay != null ? !Bay.equals(msg03.Bay) : msg03.Bay != null) return false;
        if (CycleOrder != null ? !CycleOrder.equals(msg03.CycleOrder) : msg03.CycleOrder != null) return false;
        if (Dock != null ? !Dock.equals(msg03.Dock) : msg03.Dock != null) return false;
        if (Height != null ? !Height.equals(msg03.Height) : msg03.Height != null) return false;
        if (JobType != null ? !JobType.equals(msg03.JobType) : msg03.JobType != null) return false;
        if (Level != null ? !Level.equals(msg03.Level) : msg03.Level != null) return false;
        if (MachineNo != null ? !MachineNo.equals(msg03.MachineNo) : msg03.MachineNo != null) return false;
        if (Station != null ? !Station.equals(msg03.Station) : msg03.Station != null) return false;
        if (Width != null ? !Width.equals(msg03.Width) : msg03.Width != null) return false;
        if (lastSendDate != null ? !lastSendDate.equals(msg03.lastSendDate) : msg03.lastSendDate != null) return false;
        if (plcName != null ? !plcName.equals(msg03.plcName) : msg03.plcName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (plcName != null ? plcName.hashCode() : 0);
        result = 31 * result + (MachineNo != null ? MachineNo.hashCode() : 0);
        result = 31 * result + (CycleOrder != null ? CycleOrder.hashCode() : 0);
        result = 31 * result + (JobType != null ? JobType.hashCode() : 0);
        result = 31 * result + (Height != null ? Height.hashCode() : 0);
        result = 31 * result + (Width != null ? Width.hashCode() : 0);
        result = 31 * result + (Bank != null ? Bank.hashCode() : 0);
        result = 31 * result + (Bay != null ? Bay.hashCode() : 0);
        result = 31 * result + (Level != null ? Level.hashCode() : 0);
        result = 31 * result + (Station != null ? Station.hashCode() : 0);
        result = 31 * result + (Dock != null ? Dock.hashCode() : 0);
        result = 31 * result + (lastSendDate != null ? lastSendDate.hashCode() : 0);
        result = 31 * result + (received ? 1 : 0);
        return result;
    }

    public static void clearMsg03(String mcKey) {

        Query q = HibernateUtil.getCurrentSession().createQuery("from Msg03 where mcKey=:mckey and received=false ").setParameter("mckey",mcKey);

        List<Msg03> msg03s = q.list();
        for (Msg03 msg : msg03s) {
            HibernateUtil.getCurrentSession().createQuery("update Msg03 set received=true where id=:msgId")
                    .setParameter("msgId", msg.getId()).executeUpdate();

        }
    }

    public static void main(String[] args) {
        Transaction.begin();

        Msg03 msg = new Msg03();

        msg.setMcKey("1232");
        msg.setMsgType("35");

        HibernateUtil.getCurrentSession().save(msg);

        Transaction.commit();
    }
}
