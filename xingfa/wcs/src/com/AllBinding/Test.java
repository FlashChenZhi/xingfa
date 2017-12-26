package com.AllBinding;

import com.AllBinding.blocks.Crane;
import com.AllBinding.blocks.Dock;
import com.AllBinding.blocks.SCar;
import com.AllBinding.blocks.StationBlock;
import com.asrs.business.consts.StatusDetail;
import com.asrs.domain.AsrsJob;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Session;

import java.util.Date;

/**
 * Created by Administrator on 2017/1/12.
 */
public class Test {
    public static void main(String[] args) {
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            StationBlock stationBlock=new StationBlock();
            stationBlock.setStationNo("1101");
            stationBlock.setBlockNo("A001");
            stationBlock.setPlcName("BL01");
            StationBlock stationBlock1=new StationBlock();
            stationBlock1.setStationNo("1201");
            stationBlock1.setBlockNo("A002");
            stationBlock1.setPlcName("BL01");
            Dock dock=new Dock();
            dock.setBlockNo("AA02");
            dock.setPlcName("BL01");
            dock.setLevel(0);
            Crane crane=new Crane();
            crane.setBlockNo("MC01");
            crane.setPlcName("MC01");
            crane.setLevel(-1);
            crane.setLevel(-1);
            crane.setsCarNo("SC01");
            SCar sCar=new SCar();
            sCar.setBlockNo("SC01");
            sCar.setPlcName("SC01");
            sCar.setOnCarNo("MC01");
            sCar.setBindingCrane(true);
            session.save(stationBlock);
            session.save(stationBlock1);
            session.save(sCar);
            session.save(crane);
            session.save(dock);
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }
    }
}
