package com.asrs.business.msgProc;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.ReasonCode;
import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.*;
import com.asrs.message.*;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.Receiver;
import com.domain.XMLbean.XMLList.ControlArea.RefId;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.domain.XMLbean.XMLList.MovementReport;
import com.domain.consts.xmlbean.XMLConstant;
import com.thread.blocks.*;
import com.util.common.Const;
import com.util.common.LogType;
import com.util.common.LogWriter;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-3-19
 * Time: 20:44:42
 * Copyright Dsl.Worgsoft.
 */
public class Msg35Proc implements MsgProcess {
    public void Do(MessageBuilder msg) throws MsgException {
        Message35 message35 = new Message35(msg.DataString);
        message35.setPlcName(msg.PlcName);
        Do(message35);
    }

    @Override
    public void setProxy(XmlProxy wmsProxy, MessageProxy wcsProxy) {
        this._wmsProxy = wmsProxy;
        this._wcsProxy = wcsProxy;
    }

    XmlProxy _wmsProxy;
    MessageProxy _wcsProxy;

    public static void main(String[] args) throws Exception {
//        Message35 msg35 = new Message35("0001MC0504030015080100000000" + "000");
        Transaction.begin();
        Msg35Proc proc = new Msg35Proc();
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey("1899");
        Location location = Location.getByLocationNo(asrsJob.getToLocation());
        proc.putawayFinish(asrsJob, location);
        Transaction.commit();
//        proc.Do(msg35);
    }

    public void Do(Message35 message35) {
        try {
            Transaction.begin();

            Message05 m5 = new Message05();
            m5.setPlcName(message35.getPlcName());
            m5.McKey = message35.McKey;
            m5.Response = "0";
            m5.setPlcName(message35.getPlcName());

            Query wcsQ = HibernateUtil.getCurrentSession().createQuery("from WcsMessage where dock=:dock and msgType=:msgType and mcKey=:mckey");
            wcsQ.setParameter("dock", message35.Dock);
            wcsQ.setParameter("msgType", WcsMessage.MSGTYPE_35);
            wcsQ.setParameter("mckey", message35.McKey);
            List<WcsMessage> msg35List = wcsQ.list();
            if (msg35List.isEmpty()) {
                WcsMessage.save35(message35);
                AsrsJob aj = AsrsJob.getAsrsJobByMcKey(message35.McKey);
                Session session = HibernateUtil.getCurrentSession();
                Block block = Block.getByBlockNo(message35.MachineNo);
                if (aj != null) {
                    if (AsrsJobType.PUTAWAY.equals(aj.getType())) {
                        Location location = Location.getByLocationNo(aj.getToLocation());
                        if (block instanceof Srm) {
                            Srm srm = (Srm) block;
                            if (message35.isMove()) {
                                //升降机移动
                                if (message35.Station.equals("0000")) {
                                    srm.setDock(null);
                                    srm.setBay(Integer.parseInt(message35.Bay));
                                    srm.setLevel(Integer.parseInt(message35.Level));
                                    Location toLoc = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), srm.getBay(), srm.getLevel(), srm.getPosition());
                                    srm.setActualArea(toLoc.getActualArea());
                                    if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                                        SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                                        sCar.setBay(srm.getBay());
                                        sCar.setLevel(srm.getLevel());
                                        sCar.setActualArea(srm.getActualArea());
                                    }
                                } else {
                                    srm.setDock(message35.Station);
                                    if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                                        SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                                        sCar.setBay(0);
                                    }
                                }
                                srm.setCheckLocation(true);
                            } else if (message35.isMoveCarryGoods()) {
                                srm.generateMckey(message35.McKey);
                                aj.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
                            } else if (message35.isMoveUnloadGoods()) {
                                srm.clearMckeyAndReservMckey();
                            } else if (message35.isLoadCar()) {

                                SCar sCar = (SCar) Block.getByBlockNo(message35.Station);
                                srm.setsCarBlockNo(sCar.getBlockNo());

                                if (StringUtils.isNotBlank(sCar.getMcKey())) {
                                    srm.generateMckey(sCar.getMcKey());
                                }

                            } else if (message35.isUnLoadCar()) {
                                SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                                srm.setsCarBlockNo(null);
                                // mCar.setReservedMcKey(null);
                                srm.clearMckeyAndReservMckey();
                            }

                        } else if (block instanceof SCar) {
                            SCar sCar = (SCar) block;
                            if (message35.isUnloadGoods()) {
                                sCar.clearMckeyAndReservMckey();
                                sCar.setOnMCar(null);
                                //入库完成，发送消息给wms
                                putawayFinish(aj, location);

                                sCar.setBank(location.getBank());
                                if (aj.getStatus().equals(AsrsJobStatus.DONE)) {
                                    aj.delete();
                                } else {
                                    aj.setStatus(AsrsJobStatus.DONE);
                                }

                                sCar.clearMckeyAndReservMckey();

                            } else if (message35.isOnCar()) {

                                sCar.setOnMCar(message35.Station);
                                sCar.setBank(0);

                                Block block1 = Block.getByBlockNo(message35.Station);
                                if (block1 instanceof Srm) {
                                    Srm srm = (Srm) block1;
                                    sCar.setOnMCar(srm.getBlockNo());
                                    if (StringUtils.isNotEmpty(srm.getMcKey())) {
                                        sCar.generateReserveMckey(message35.McKey);
                                    }
                                }
                            }

                        } else if (block instanceof MCar) {
                            MCar mCar = (MCar) block;
                            if (message35.isMove()) {
                                if (message35.Station.equals("0000")) {
                                    mCar.setDock(null);
                                } else {
                                    mCar.setDock(message35.Station);
                                }
                                mCar.setCheckLocation(true);
                            } else if (message35.isMoveCarryGoods()) {
                                mCar.generateMckey(message35.McKey);
                            } else if (message35.isMoveUnloadGoods()) {
                                mCar.clearMckeyAndReservMckey();
                            }
                        } else if (block instanceof StationBlock) {

                            StationBlock station = (StationBlock) block;
                            if (message35.isMoveUnloadGoods()) {
                                station.setMcKey(null);
                                InMessage.error(station.getStationNo(), "");
                            }

                        } else if (block instanceof Conveyor) {
                            Conveyor conveyor = (Conveyor) block;
                            if (message35.isMoveUnloadGoods()) {
                                conveyor.clearMckeyAndReservMckey();
                            } else if (message35.isMoveCarryGoods()) {
                                conveyor.generateMckey(message35.McKey);
                            }
                        }

                    } else if (AsrsJobType.RETRIEVAL.equals(aj.getType())) {
                        // TODO: 2017/4/27  出库
                        if (block instanceof SCar) {
                            SCar sCar = (SCar) block;
                            if (message35.isPickingUpGoods()) {

                                sCar.clearMckeyAndReservMckey();
                                sCar.setOnMCar(message35.Station);
                                aj.setStatus(AsrsJobStatus.PICKING);

                                Srm srm = Srm.getSrmByPosition(sCar.getPosition());

                                Query query = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type=:ajType and status=:st and statusDetail=:detail and fromStation=:frs order by id asc ");
                                query.setParameter("ajType", AsrsJobType.RETRIEVAL);
                                query.setParameter("detail", AsrsJobStatusDetail.WAITING);
                                query.setParameter("st", AsrsJobStatus.RUNNING);
                                query.setParameter("frs", srm.getBlockNo());

                                query.setMaxResults(1);
                                AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
                                if (asrsJob != null) {

                                    Location location = Location.getByLocationNo(asrsJob.getFromLocation());
                                    if(location.getBay() == sCar.getBay() && location.getLevel() == sCar.getLevel()) {

                                        sCar.setReservedMcKey(asrsJob.getMcKey());
                                        asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
                                        asrsJob.setStatus(AsrsJobStatus.ACCEPT);
                                    }
                                }

                            } else if (message35.isOnCar()) {

                                sCar.setOnMCar(message35.Station);
                                sCar.setBank(0);

                            } else if (message35.isOffCar()) {

                                sCar.setBank(Integer.parseInt(message35.Bank));
                                sCar.setOnMCar(null);
                                sCar.generateMckey(message35.McKey);

                            }
                        } else if (block instanceof StationBlock) {
                            if (message35.isMoveCarryGoods()) {
                                block.setMcKey(message35.McKey);
                                retrievalFinish(aj);
                            }
                        } else if (block instanceof Srm) {
                            Srm srm = (Srm) block;
                            if (message35.isMoveUnloadGoods()) {
                                srm.setMcKey(null);
                            } else if (message35.isUnLoadCar()) {

                                srm.setsCarBlockNo(null);

                            } else if (message35.isMove()) {

                                srm.setCheckLocation(true);
                                srm.setLevel(Integer.parseInt(message35.Level));

                                Location location = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), Integer.parseInt(message35.Bay), Integer.parseInt(message35.Level), srm.getPosition());

                                if (location != null) {
                                    srm.setActualArea(location.getActualArea());
                                }

                                if (message35.Bay.equals("00")) {
                                    srm.setDock(message35.Station);
                                    srm.setBay(0);
                                    if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                                        SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                                        sCar.setBay(srm.getBay());
                                        sCar.setActualArea(srm.getActualArea());
                                        sCar.setLevel(srm.getLevel());
                                    }

                                } else {
                                    srm.setDock(null);
                                    srm.setBay(Integer.parseInt(message35.Bay));
                                    if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                                        SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                                        sCar.setBay(srm.getBay());
                                        sCar.setLevel(srm.getLevel());
                                        sCar.setActualArea(srm.getActualArea());
                                    }
                                }

                            } else if (message35.isLoadCar()) {
                                SCar sCar = (SCar) Block.getByBlockNo(message35.Station);
                                srm.setsCarBlockNo(sCar.getBlockNo());


                                if (StringUtils.isEmpty(sCar.getMcKey()) && StringUtils.isEmpty(sCar.getReservedMcKey())) {
                                    if (StringUtils.isEmpty(sCar.getOnMCar())) {
                                        //子车不在母车上，子车有排列层，查找货位是否是aj的源货位，如果一样，取货上车，如果不是，简单接车
                                        Location sLocation = Location.getByBankBayLevel(sCar.getBank(), sCar.getBay(), sCar.getLevel(), sCar.getPosition());
                                        if (sLocation.getLocationNo().equals(aj.getFromLocation())) {
                                            srm.generateMckey(message35.McKey);
                                        } else {

                                        }

                                    } else {
                                        //如果订单状态不是1，表明子车已经领取过任务了，子车领取过任务后，子车没有任务号后，表示已经做完任务
                                        if (!aj.getStatus().equals(AsrsJobStatus.RUNNING)) {
                                            srm.generateMckey(message35.McKey);
                                        }
                                    }
                                }

                                if (StringUtils.isNotEmpty(sCar.getMcKey())) {
                                    srm.generateMckey(message35.McKey);
                                } else if (StringUtils.isNotEmpty(sCar.getReservedMcKey())) {

                                    if (StringUtils.isEmpty(sCar.getOnMCar())) {
                                        //子车不在母车上，子车有排列层，查找货位是否是aj的源货位，如果一样，取货上车，如果不是，简单接车
                                        Location sLocation = Location.getByBankBayLevel(sCar.getBank(), sCar.getBay(), sCar.getLevel(), sCar.getPosition());
                                        if (sLocation.getLocationNo().equals(aj.getFromLocation())) {
                                            srm.generateMckey(message35.McKey);
                                        } else {

                                        }

                                    } else {
                                        //子车在堆垛机，子车有reservedmckey，子车的任务和堆垛机的任务不一样
                                        if (!message35.McKey.equals(sCar.getReservedMcKey())) {
                                            //堆垛机上任务和子车任务不同
                                            srm.generateMckey(message35.McKey);
                                        }
                                    }
                                }
                            }
                        } else if (block instanceof MCar) {
                            MCar mCar = (MCar) block;
                            if (message35.isMove()) {
                                if (message35.Station.equals("0000")) {
                                    mCar.setDock(null);
                                } else {
                                    mCar.setDock(message35.Station);
                                }
                                mCar.setCheckLocation(true);
                            } else if (message35.isMoveCarryGoods()) {
                                mCar.generateMckey(message35.McKey);
                            } else if (message35.isMoveUnloadGoods()) {
                                mCar.clearMckeyAndReservMckey();
                            }
                        } else if (block instanceof Conveyor) {
                            Conveyor conveyor = (Conveyor) block;
                            if (message35.isMoveCarryGoods()) {
                                conveyor.generateMckey(message35.McKey);
                            } else if (message35.isMoveUnloadGoods()) {
                                conveyor.clearMckeyAndReservMckey();
                            }
                        }
                    } else if (AsrsJobType.RECHARGED.equals(aj.getType())) {

                        if (block instanceof Srm) {

                            Srm mCar = (Srm) block;

                            if (message35.isMove()) {
                                mCar.setBay(Integer.parseInt(message35.Bay));
                                mCar.setDock(message35.Station);
                                mCar.setCheckLocation(true);
                                mCar.setLevel(Integer.parseInt(message35.Level));

                                Location loc = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), Integer.parseInt(message35.Bay), Integer.parseInt(message35.Level), mCar.getPosition());

                                if (loc != null) {
                                    mCar.setActualArea(loc.getActualArea());
                                }

                                if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                                    SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                    sCar.setBay(mCar.getBay());
                                    sCar.setLevel(mCar.getLevel());
                                }

                                mCar.setDock(null);

                            } else if (message35.isLoadCar()) {
                                mCar.setsCarBlockNo(message35.Station);
                                mCar.generateMckey(message35.McKey);
                            } else if (message35.isUnLoadCar()) {
                                mCar.setsCarBlockNo(null);
                                Location location = Location.getByLocationNo(aj.getToLocation());
                                //充电任务中，如果堆垛机的列是充电的这一列，那么堆垛机正常完成任务，
                                if (location.getBay() == Integer.parseInt(message35.Bay)) {
                                    mCar.clearMckeyAndReservMckey();
                                    if (mCar.getPosition().equals(location.getPosition())) {
                                        aj.setStatus(AsrsJobStatus.DONE);
                                    }
                                }
                                if (mCar.getBlockNo().equals(aj.getFromStation())) {
                                    mCar.clearMckeyAndReservMckey();
                                }
                            }

                        } else if (block instanceof SCar) {

                            SCar sCar = (SCar) block;

                            if (message35.isMove()) {
                                Srm srm = (Srm) Srm.getByBlockNo(aj.getToStation());
                                sCar.setPosition(srm.getPosition());

                            } else if (message35.isOffCar()) {

                                if (StringUtils.isNotBlank(sCar.getReservedMcKey())) {
                                    sCar.setStatus(SCar.STATUS_CHARGE);
                                }
                                sCar.setOnMCar(null);
                                sCar.setReservedMcKey(null);

                                sCar.setBank(Integer.parseInt(message35.Bank));

                            } else if (message35.isOnCar()) {

                                sCar.setOnMCar(message35.Station);

                            } else if (message35.isCharge()) {

                                sCar.setMcKey(null);
                                aj.setStatus(AsrsJobStatus.DONE);
                                sCar.setStatus(SCar.STATUS_CHARGE);

                            }

                        }
                    } else if (AsrsJobType.RECHARGEDOVER.equals(aj.getType())) {

                        if (block instanceof Srm) {

                            Srm mCar = (Srm) block;
                            if (message35.isMove()) {
                                mCar.setCheckLocation(true);
                                mCar.setBay(Integer.parseInt(message35.Bay));
                                mCar.setLevel(Integer.parseInt(message35.Level));

                                if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                                    SCar sCar = (SCar) SCar.getByBlockNo(mCar.getsCarBlockNo());
                                    sCar.setLevel(mCar.getLevel());
                                    sCar.setBay(mCar.getBay());
                                }

                            } else if (message35.isLoadCar()) {
                                mCar.setsCarBlockNo(message35.Station);
                                if (mCar.getBlockNo().equals(aj.getToStation())) {
                                    mCar.clearMckeyAndReservMckey();
                                    aj.setStatus(AsrsJobStatus.DONE);

                                    Query query = HibernateUtil.getCurrentSession().createQuery("from SCar where wareHouse=:wh and status=:st ");
                                    query.setParameter("wh", mCar.getWareHouse());
                                    query.setParameter("st", SCar.STATUS_CHARGE);
                                    List<SCar> sCars = query.list();
                                    for (SCar sCar : sCars) {
                                        sCar.setStatus(SCar.STATUS_RUN);
                                    }

                                } else {
                                    mCar.generateMckey(message35.McKey);
                                }
                            } else if (message35.isUnLoadCar()) {
                                mCar.setsCarBlockNo(null);
                                mCar.clearMckeyAndReservMckey();
                            }

                        } else if (block instanceof SCar) {

                            SCar sCar = (SCar) block;

                            if (message35.isMove()) {
                                Srm srm = (Srm) Srm.getByBlockNo(aj.getToStation());
                                sCar.setPosition(srm.getPosition());

                            } else if (message35.isOffCar()) {
                                sCar.setOnMCar(null);
                                sCar.setBank(Integer.parseInt(message35.Bank));

                            } else if (message35.isOnCar()) {
                                sCar.setOnMCar(message35.Station);
                                sCar.setBank(0);
                                if (message35.Station.equals(aj.getToStation())) {
                                    aj.setStatus(AsrsJobStatus.DONE);
                                    sCar.clearMckeyAndReservMckey();
                                }
                            }
                        }
                    } else if (AsrsJobType.LOCATIONTOLOCATION.equals(aj.getType())) {
                        if (block instanceof Srm) {
                            Srm srm = (Srm) block;
                            if (message35.isMove()) {
                                srm.setLevel(Integer.parseInt(message35.Level));
                                srm.setBay(Integer.parseInt(message35.Bay));
                                srm.setDock(message35.Station);
                                srm.setCheckLocation(true);
                                Location location = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), Integer.parseInt(message35.Bay), Integer.parseInt(message35.Level), srm.getPosition());
                                srm.setActualArea(location.getActualArea());
                                if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                                    SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                                    sCar.setLevel(srm.getLevel());
                                    sCar.setBay(srm.getBay());
                                    sCar.setActualArea(location.getActualArea());
                                }
                            } else if (message35.isUnLoadCar()) {
                                srm.setsCarBlockNo(null);
                                if (StringUtils.isNotBlank(srm.getMcKey())) {
                                    srm.clearMckeyAndReservMckey();
                                }
                            } else if (message35.isLoadCar()) {
                                srm.setsCarBlockNo(message35.Station);
                                srm.generateMckey(message35.McKey);
                            }
                        } else if (block instanceof SCar) {
                            SCar sCar = (SCar) block;
                            if (message35.isUnloadGoods()) {
                                sCar.setOnMCar(null);
                                sCar.setBank(Integer.parseInt(message35.Bank));
                                sCar.clearMckeyAndReservMckey();
                                if (aj.getStatus().equals(AsrsJobStatus.DONE)) {
                                    aj.delete();
                                } else {
                                    aj.setStatus(AsrsJobStatus.DONE);
                                }

                            } else if (message35.isPickingUpGoods()) {
                                sCar.generateMckey(message35.McKey);
                                sCar.setOnMCar(message35.Station);
                                sCar.setBank(0);
                            } else if (message35.isOffCar()) {
                                sCar.setBank(Integer.parseInt(message35.Bank));
                                sCar.setOnMCar(null);
                                sCar.generateMckey(message35.McKey);

                            }
                        }
                    } else if (AsrsJobType.ST2ST.equals(aj.getType())) {

                        if (block instanceof StationBlock) {
                            if (message35.isMoveCarryGoods()) {
                                block.generateMckey(message35.McKey);
                                aj.setStatus(AsrsJobStatus.DONE);
                            } else if (message35.isMoveUnloadGoods()) {
                                block.clearMckeyAndReservMckey();
                            }

                        } else if (block instanceof Conveyor) {
                            if (message35.isMoveCarryGoods()) {
                                block.generateMckey(message35.McKey);
                            } else if (message35.isMoveUnloadGoods()) {
                                block.clearMckeyAndReservMckey();
                            }

                        } else if (block instanceof Srm) {
                            if (message35.isMoveCarryGoods()) {
                                block.generateMckey(message35.McKey);
                            } else if (message35.isMoveUnloadGoods()) {
                                block.clearMckeyAndReservMckey();
                            } else if (message35.isMove()) {
                                Srm srm = (Srm) block;
                                srm.setLevel(Integer.parseInt(message35.Level));
                                srm.setBay(Integer.parseInt(message35.Bay));
                                srm.setDock(message35.Station);
                                srm.setCheckLocation(true);
                                if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                                    SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                                    sCar.setLevel(srm.getLevel());
                                    sCar.setBay(srm.getBay());
                                }
                            }
                        }

                    }
                } else if (message35.McKey.equals("9999")) {
                    if (StringUtils.isEmpty(block.getMcKey()) && StringUtils.isEmpty(block.getReservedMcKey())) {
                        //meck找不到job
                        if (block instanceof SCar) {

                            SCar sCar = (SCar) block;
                            if (message35.isOnCar()) {
                                Srm mCar = (Srm) Block.getByBlockNo(message35.Station);
                                sCar.setPosition(mCar.getPosition());
                                sCar.setOnMCar(message35.Station);
                                sCar.setBank(0);
                            } else if (message35.isChargeFinish()) {
                                //欧普照明，充电完成，不做处理，解除子车状态，后续按照正常空车上车处理
                            }

                        } else if (block instanceof Srm) {
                            Srm srm = (Srm) block;
                            if (message35.isMove()) {
                                if ("0000".equals(message35.Station) || StringUtils.isBlank(message35.Station)) {
                                    srm.setDock(null);
                                    Location loc = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), Integer.parseInt(message35.Bay), Integer.parseInt(message35.Level), srm.getPosition());
                                    srm.setBay(loc.getBay());
                                    srm.setLevel(loc.getLevel());
                                    srm.setActualArea(loc.getActualArea());
                                } else {
                                    srm.setLevel(1);
                                    srm.setDock(message35.Station);
                                    srm.setBay(0);
                                    if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                                        SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                                        sCar.setLevel(1);
                                        sCar.setBay(0);
                                    }
                                }
                                srm.setCheckLocation(true);
                            } else if (message35.isLoadCar()) {
                                srm.setsCarBlockNo(message35.Station);
                            }
                        }

                        Query delQ = HibernateUtil.getCurrentSession().createQuery("delete from WcsMessage where mcKey= '9999' and machineNo=:blockNo ");
                        delQ.setParameter("blockNo", block.getBlockNo());
                        delQ.executeUpdate();

                    }
                }
                block.setWaitingResponse(false);
                if (block instanceof Conveyor) {
                    Conveyor conveyor = (Conveyor) block;
                    conveyor.setMantWaiting(false);
                }

                Thread.sleep(100);

            }
            WcsMessage.clear(message35.McKey, message35.CycleOrder, message35.MachineNo);

            Transaction.commit();

            MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
            _wcsproxy.addSndMsg(m5);

        } catch (
                Exception e)

        {
            Transaction.rollback();
            e.printStackTrace();

        }

    }

    private void retrievalFinish(AsrsJob asrsJob) throws Exception {
        Sender sd = new Sender();
        sd.setDivision(XMLConstant.COM_DIVISION);

        Receiver receiver = new Receiver();
        receiver.setDivision(XMLConstant.WMS_DIVISION);
        RefId ri = new RefId();
        ri.setReferenceId(asrsJob.getWmsMckey());

        ControlArea ca = new ControlArea();
        ca.setSender(sd);
        ca.setReceiver(receiver);
        ca.setRefId(ri);
        ca.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        Location location = Location.getByLocationNo(asrsJob.getFromLocation());
        FromLocation fromLocation = new FromLocation();
        fromLocation.setMHA(asrsJob.getFromStation());
        List<String> rack = new ArrayList<>(3);
        rack.add(String.valueOf(location.getBank()));
        rack.add(String.valueOf(location.getBay()));
        rack.add(String.valueOf(location.getLevel()));
        fromLocation.setRack(rack);

        ToLocation toLocation = new ToLocation();
        List<String> list = new ArrayList<>(3);
        list.add("");
        list.add("");
        list.add("");
        toLocation.setMHA("");
        toLocation.setRack(list);


        //创建MovementReportDA数据域对象
        MovementReportDA mrd = new MovementReportDA();
        mrd.setReasonCode(ReasonCode.RETRIEVALFINISHED);
        mrd.setStUnitId(asrsJob.getBarcode());
        mrd.setFromLocation(fromLocation);
        mrd.setToLocation(toLocation);
        //创建MovementReport响应核心对象
        MovementReport mr = new MovementReport();
        mr.setControlArea(ca);
        mr.setDataArea(mrd);
        //将MovementReport发送给wms
        Envelope el = new Envelope();
        el.setMovementReport(mr);

        XMLMessage xmlMessage = new XMLMessage();
        xmlMessage.setRecv("WMS");
        xmlMessage.setStatus("1");
        xmlMessage.setMessageInfo(XMLUtil.getSendXML(el));
        HibernateUtil.getCurrentSession().save(xmlMessage);


    }


    private void putawayFinish(AsrsJob aj, Location location) throws Exception {

        //创建ControlArea控制域对象
        Sender sd = new Sender();
        sd.setDivision(XMLConstant.COM_DIVISION);

        Receiver receiver = new Receiver();
        receiver.setDivision(XMLConstant.WMS_DIVISION);

        RefId ri = new RefId();
        ri.setReferenceId(aj.getWmsMckey());

        ControlArea ca = new ControlArea();
        ca.setSender(sd);
        ca.setReceiver(receiver);
        ca.setRefId(ri);
        ca.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        FromLocation fromLocation = new FromLocation();
        List<String> list = new ArrayList<>(3);
        fromLocation.setMHA("");
        list.add("");
        list.add("");
        list.add("");
        fromLocation.setRack(list);

        ToLocation toLocation = new ToLocation();
        // TODO: 2017/5/1 修改货位
        toLocation.setMHA(aj.getFromStation());
        List<String> rack = new ArrayList<>(3);
        rack.add(String.valueOf(location.getBank()));
        rack.add(String.valueOf(location.getBay()));
        rack.add(String.valueOf(location.getLevel()));
        toLocation.setRack(rack);

        //创建MovementReportDA数据域对象
        MovementReportDA mrd = new MovementReportDA();
        mrd.setFromLocation(fromLocation);
        mrd.setStUnitId(aj.getBarcode());
        mrd.setReasonCode(ReasonCode.PUTAWAYFINISHED);
        mrd.setToLocation(toLocation);
        //创建MovementReport响应核心对象
        MovementReport mr = new MovementReport();
        mr.setControlArea(ca);
        mr.setDataArea(mrd);
        //将MovementReport发送给wms
        Envelope el = new Envelope();
        el.setMovementReport(mr);

        XMLMessage xmlMessage = new XMLMessage();
        xmlMessage.setStatus("1");
        xmlMessage.setRecv("WMS");
        xmlMessage.setMessageInfo(XMLUtil.getSendXML(el));
        HibernateUtil.getCurrentSession().save(xmlMessage);

    }
}
