package com.web.service;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.communication.MessageProxy;
import com.asrs.domain.Station;
import com.asrs.message.Message40;
import com.thread.blocks.StationBlock;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.rmi.Naming;
import java.util.Date;

@Service
public class PlatformSwitchService {
    /**
     * 进入页面查询站台模式，默认下拉框选择该模式
     * @param zhantai 站台
     * @return 模式编号
     */
    public String findPlatformSwitch(String zhantai){
        System.out.println(zhantai);
        return "03";
    }
    /**
     * 站台模式切换更新
     * @param pattern 模式
     * @param zhantai 站台ID
     * @return "0"设定成功，"1"设定失败
     */
    public String updatePlatformSwitch(String pattern,String zhantai){
        try {
            Transaction.begin();
            Station station = Station.getStation(zhantai);
            StationBlock block = (StationBlock) HibernateUtil.getCurrentSession().createQuery("from StationBlock sb where sb.stationNo = :stationNo").uniqueResult();

            if (!pattern.equals(station.getMode())) {
                station.setOldMode(station.getMode());
                station.setMode(pattern);
                station.setModeChangeTime(new Date());
            }
            Message40 message40 = new Message40();
            message40.setPlcName(block.getPlcName());
            message40.Mode = pattern.equals(AsrsJobType.PUTAWAY) ? "01" : "02";
            message40.Station = zhantai;
            MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
            _wcsproxy.addSndMsg(message40);
            Transaction.commit();

        } catch (Exception ex) {
            Transaction.rollback();
            ex.printStackTrace();
            return "1";
        }
        return "0";
    }
}
