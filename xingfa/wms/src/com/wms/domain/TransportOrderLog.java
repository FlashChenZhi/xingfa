package com.wms.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by van on 2017/6/15.
 */
@Entity
@Table(name = "TRANSPORTORDER")
@DynamicUpdate()
public class TransportOrderLog {

    private int id;
    private Location fromLocation;
    private Location toLocation;
    private String type;
    private Container container;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "FROMLOCATIONID", referencedColumnName = "ID")
    public Location getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(Location fromLocation) {
        this.fromLocation = fromLocation;
    }

    @ManyToOne
    @JoinColumn(name = "TOLOCATIONID", referencedColumnName = "ID")
    public Location getToLocation() {
        return toLocation;
    }

    public void setToLocation(Location toLocation) {
        this.toLocation = toLocation;
    }

    @Basic
    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name = "CONTAINERID", referencedColumnName = "ID")
    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    private Date _createDate;

    @Column(name = "CREATEDATE")
    @Basic
    public Date getCreateDate() {
        return _createDate;
    }

    public void setCreateDate(Date createDate) {
        _createDate = createDate;
    }

    public static TransportOrderLog getTransportOrderLogByFTL(int fromLocationId,int toLocationId){
        Query query = HibernateUtil.getCurrentSession().createQuery("from TransportOrderLog t where " +
                "t.fromLocation.id=:fromLocationId and t.toLocation.id=:toLocationId");
        query.setParameter("fromLocationId", fromLocationId);
        query.setParameter("toLocationId", toLocationId);
        query.setMaxResults(1);
        return (TransportOrderLog) query.uniqueResult();
    }

    public static int getTransportOrderCountByType2(){
        Query query = HibernateUtil.getCurrentSession().createQuery("select count(*) as count from TransportOrderLog where type =2");
        int count = ((Long)query.uniqueResult()).intValue();
        return count;
    }

    public static int getTransportOrderCountByType(){
        Query query = HibernateUtil.getCurrentSession().createQuery("select count(*) as count from TransportOrderLog where type =1");
        int count = ((Long)query.uniqueResult()).intValue();
        return count;
    }

    public static TransportOrderLog getTransportOrderByType(){
        Query query = HibernateUtil.getCurrentSession().createQuery("from TransportOrderLog t where " +
                "t.type=1");
        query.setMaxResults(1);
        return (TransportOrderLog) query.uniqueResult();
    }
    public static List<TransportOrderLog> getTransportOrderByType2(){
        Query query = HibernateUtil.getCurrentSession().createQuery("from TransportOrderLog t where " +
                "t.type=2 order by id desc ");
        return (List<TransportOrderLog>) query.list();
    }
}
