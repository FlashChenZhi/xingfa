package com.web.service;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.communication.MessageProxy;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Station;
import com.asrs.message.Message40;
import com.thread.blocks.StationBlock;
import com.util.common.Const;
import com.util.common.HttpMessage;
import com.util.common.ReturnObj;

import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.rmi.Naming;
import java.util.Date;
import java.util.List;

@Service
public class PlatformSwitchService {
    /**
     * 进入页面查询站台模式，默认下拉框选择该模式
     * @param zhantai 站台
     * @return 模式编号
     */
    public ReturnObj<String> findPlatformSwitch(String zhantai){
        System.out.println(zhantai);
        ReturnObj<String> returnObj = new ReturnObj<String>();
        try {
            Transaction.begin();
            Station station = (Station) HibernateUtil.getCurrentSession().createQuery("from Station s where s.stationNo = :stationNo")
                    .setString("stationNo",zhantai).uniqueResult();
            returnObj.setSuccess(true);
            returnObj.setRes(station.getMode());
            Transaction.commit();
        }catch (Exception ex) {
            Transaction.rollback();
            ex.printStackTrace();
            returnObj.setSuccess(false);
        }

        return returnObj;
    }
    /**
     * 站台模式切换更新
     * @param pattern 模式
     * @param zhantai 站台ID
     * @return "0"设定成功，"1"设定失败
     */
    public ReturnObj<String> updatePlatformSwitch(String pattern,String zhantai){
        ReturnObj<String> returnObj = new ReturnObj<String>();
        try {
            Transaction.begin();
            List<AsrsJob> asrsJobList=null;
            //查询是否有asrsjob存在
            if(StringUtils.isNotBlank(pattern)){
                Query query = null;
                if("01".equals(pattern)){
                    //入库
                    query = HibernateUtil.getCurrentSession().createQuery("from AsrsJob  where toStation=:s and status!=:status");
                }else{
                    //出库
                    query = HibernateUtil.getCurrentSession().createQuery("from AsrsJob  where fromStation=:s and status!=:status");
                }
                query.setParameter("s",pattern);
                query.setParameter("status", AsrsJobStatus.DONE);
                asrsJobList = query.list();
            }
            if(asrsJobList==null||asrsJobList.size()==0){
                Station station = Station.getStation(zhantai);
                StationBlock block = (StationBlock) HibernateUtil.getCurrentSession().createQuery("from StationBlock sb where sb.stationNo = :stationNo")
                        .setString("stationNo",zhantai).uniqueResult();

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
                returnObj.setSuccess(true);
            }else{
                returnObj.setSuccess(false);
                returnObj.setMsg("请等待任务做完再切换模式！");
            }

        } catch (Exception ex) {
            Transaction.rollback();
            ex.printStackTrace();
            returnObj.setSuccess(false);
        }
        return returnObj;
    }
}
