package com.AllBinding.threads;


import com.AllBinding.blocks.*;
import com.AllBinding.utils.BlockStatus;
import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.AsrsJob;
import com.asrs.message.Message26;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.util.common.JedisUtil;
import com.util.hibernate.HibernateUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public abstract class BlockThread<T> extends Thread {
    protected String _blockNo;

    public BlockThread(String blockNo) {
        _blockNo = blockNo;
    }

    protected T getBlock() {
        return (T) Block.getByBlockNo(_blockNo);
    }

    @Override
    public abstract void run();

    public void Message26Proc(Block block) {
        String result = JedisUtil.get(block.getBlockNo());
        JedisUtil.del(block.getBlockNo());

        if (block instanceof SCar) {
            if (StringUtils.isEmpty(block.getMcKey()) && StringUtils.isEmpty(block.getReservedMcKey())) {
                Query query = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type = '08' and status = '3'");
                List<AsrsJob> asrsJobs = query.list();
                for (AsrsJob asrsJob : asrsJobs) {
                    HibernateUtil.getCurrentSession().delete(asrsJob);
                }
            }
        }

        if (result != null) {
            JSONObject jsonObject = JSONObject.fromObject(result);
            Message26 msg26 = new Message26();
            Message26.Block messageBlock = msg26.new Block();
            messageBlock.setAbnormal(jsonObject.getString("abnormal"));
            messageBlock.setBank(jsonObject.getString("bank"));
            messageBlock.setBatteryElectricity(jsonObject.getString("batteryElectricity"));
            messageBlock.setBatteryLow(jsonObject.getString("batteryLow"));
            messageBlock.setBatteryOk(jsonObject.getString("batteryOk"));
            messageBlock.setBay(jsonObject.getString("bay"));
            messageBlock.setEmergencyStop(jsonObject.getString("emergencyStop"));
            messageBlock.setErrorCode(jsonObject.getString("errorCode"));
            messageBlock.setHostMachine(jsonObject.getString("hostMachine"));
            messageBlock.setLevel(jsonObject.getString("level"));
            messageBlock.setOffline(jsonObject.getString("offline"));
            messageBlock.setRun(jsonObject.getString("run"));
            messageBlock.setStop(jsonObject.getString("stop"));

            if (block instanceof StationBlock) {

            } else if (block instanceof Crane) {
                Crane crane = (Crane) block;
                if (block.getReservedMcKey() == null && block.getMcKey() == null) {
                    crane.setLevel(Integer.parseInt(messageBlock.getLevel()));
                    crane.setBay(Integer.parseInt(messageBlock.getBay()));
                }

            } else if (block instanceof Dock) {


            } else if (block instanceof SCar) {
                System.out.println(Integer.parseInt(jsonObject.getString("batteryElectricity")));
                SCar sCar = (SCar) block;
                if (sCar.getStatus().equals(BlockStatus.STATUS_RUN)) {

                    if (Integer.parseInt(jsonObject.getString("batteryElectricity")) < 45) {
                        Query query = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type=:jType");
                        query.setParameter("jType", AsrsJobType.RECHARGE);
                        List<AsrsJob> jobs = query.list();
                        if (jobs.isEmpty()) {
                            AsrsJob job = new AsrsJob();
                            job.setType(AsrsJobType.RECHARGE);
                            job.setFromStation("ML01");
                            job.setToStation("ML01");
                            job.setToLocation("120101");
                            job.setFromLocation("120101");
                            job.setStatus("1");
                            job.setStatusDetail("0");
                            job.setMcKey(Mckey.getNext());
                            HibernateUtil.getCurrentSession().save(job);
                        }
                    }

                } else if (sCar.getStatus().equals(BlockStatus.STATUS_CHARING)) {
                    Integer.parseInt(jsonObject.getString("batteryElectricity"));

                    if (Integer.parseInt(jsonObject.getString("batteryElectricity")) > 98) {
                        Query query = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type=:jType");
                        query.setParameter("jType", AsrsJobType.RECHARGE_OVER);
                        List<AsrsJob> jobs = query.list();
                        if (jobs.isEmpty()) {
                            AsrsJob job = new AsrsJob();
                            job.setType(AsrsJobType.RECHARGE_OVER);
                            job.setFromStation("ML01");
                            job.setToStation("ML01");
                            job.setFromLocation("120101");
                            job.setStatus("1");
                            job.setStatusDetail("0");
                            job.setMcKey(Mckey.getNext());
                            HibernateUtil.getCurrentSession().save(job);

                        }

                    }

                }


            } else {

            }

        }
    }
}
