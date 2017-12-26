package com.test;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.ReasonCode;
import com.asrs.business.msgProc.Msg35Proc;
import com.asrs.communication.MessageProxy;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message05;
import com.asrs.message.Message35;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.RefId;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.domain.XMLbean.XMLList.DataArea.StUnit;
import com.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.domain.XMLbean.XMLList.MovementReport;
import com.domain.consts.xmlbean.XMLConstant;
import com.test.blocks.*;
import com.util.common.Const;
import com.util.common.LogType;
import com.util.common.LogWriter;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/6.
 */
public class Msg35 {
    public static void proc(Message35 message35) {
        System.out.println("**********");
        System.out.println(message35.toString());
        System.out.println("**********");
        try {
            Transaction.begin();
            AsrsJob aj = AsrsJob.getAsrsJobByMcKey(message35.McKey);
            Message05 m5 = new Message05();
            m5.setPlcName(message35.getPlcName());
            m5.McKey = message35.McKey;
            m5.Response = "0";
            m5.setPlcName(message35.getPlcName());
            Session session = HibernateUtil.getCurrentSession();
            Block block = Block.getByBlockNo(message35.MachineNo);
            if (aj != null) {
                if (AsrsJobType.PUTAWAY.equals(aj.getType())) {
                    Location location = Location.getByLocationNo(aj.getToLocation());
                    if (block instanceof MCar) {
                        MCar mCar = (MCar) block;
                        if (Message35._CycleOrder.loadCar.equals(message35.CycleOrder)) {
                            mCar.setCarExist(true);
                            SCar sCar = (SCar) session.createCriteria(SCar.class).add(Restrictions.eq("reservedMcKey", mCar.getMcKey())).uniqueResult();
                            mCar.setsCarBlockNo(sCar.getBlockNo());
                            sCar.setOnMCar(true);
                            if (Integer.parseInt(message35.Bay) == location.getBay()) {
                                mCar.setMcKey(null);
                                mCar.setLoaded(false);
                                sCar.setReservedMcKey(null);
                                sCar.setMcKey(message35.McKey);
                                sCar.setLoaded(true);
                            }
                        } else if (Message35._CycleOrder.move.equals(message35.CycleOrder)) {
                            //移动到电梯口、移动到子车bay、移动到目标bay
                            if (message35.Bay.equals("00")) {
                                //移动到电梯口
                                mCar.setLiftSide(true);
                                mCar.setBay(0);
                            } else {
                                mCar.setLiftSide(false);
                                mCar.setBay(Integer.parseInt(message35.Bay));
                                if (Integer.parseInt(message35.Bay) == location.getBay() && mCar.isCarExist()) {
                                    mCar.setMcKey(null);
                                    mCar.setLoaded(false);
                                    SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                    sCar.setBay(location.getBay());
                                    sCar.setReservedMcKey(null);
                                    sCar.setMcKey(message35.McKey);
                                    sCar.setLoaded(true);
                                }
                            }
                        } else if (Message35._CycleOrder.moveCarryGoods.equals(message35.CycleOrder)) {
//                            mCar.setReservedMcKey(null);
//                            mCar.setMcKey(message35.McKey);
//                            mCar.setLoaded(true);
                        } else if (Message35._CycleOrder.unloadCar.equals(message35.CycleOrder)) {
                            SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                            sCar.setOnMCar(false);
                            mCar.setCarExist(false);
                            mCar.setsCarBlockNo(null);

                        }
                    } else if (block instanceof SCar) {
                        SCar sCar = (SCar) block;
                        if (Message35._CycleOrder.unloadGoods.equals(message35.CycleOrder)) {
                            sCar.setMcKey(null);
                            sCar.setLoaded(false);
                            //入库完成
                            //创建ControlArea控制域对象
                            ControlArea ca = new ControlArea();
                            Sender sd = new Sender();
                            sd.setDivision(XMLConstant.COM_DIVISION);
                            ca.setSender(sd);
                            RefId ri = new RefId();
                            ri.setReferenceId(message35.McKey);
                            ca.setRefId(ri);
                            ca.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            //创建MovementReportDA数据域对象
                            MovementReportDA mrd = new MovementReportDA();
                            mrd.setReasonCode(ReasonCode.PUTAWAYFINISHED);

                            StUnit stUnit = new StUnit();
                            stUnit.setStUnitID(aj.getBarcode());
                            mrd.setStUnit(stUnit);
                            ToLocation toLocation = new ToLocation();
                            toLocation.setRack(location.getLocationNo());
                            toLocation.setMHA(aj.getFromStation());
                            toLocation.setX(String.valueOf(location.getBank()));
                            toLocation.setY(String.valueOf(location.getBay()));
                            toLocation.setZ(String.valueOf(location.getLevel()));
                            mrd.setToLocation(toLocation);
                            //创建MovementReport响应核心对象
                            MovementReport mr = new MovementReport();
                            mr.setControlArea(ca);
                            mr.setDataArea(mrd);
                            //将MovementReport发送给wms
                            Envelope el = new Envelope();
                            el.setMovementReport(mr);
                            XMLUtil.sendEnvelope(el);
                            session.delete(aj);
                        } else if (Message35._CycleOrder.onCar.equals(message35.CycleOrder)) {
                        }
                    } else if (block instanceof Crane) {
                        Crane crane = (Crane) block;
                        if (Message35._CycleOrder.loadCar.equals(message35.CycleOrder)) {
                            crane.setCarExist(true);
                            SCar sCar = (SCar) session.createCriteria(SCar.class).add(Restrictions.eq("reservedMcKey", crane.getMcKey())).uniqueResult();
                            crane.setsCarBlockNo(sCar.getBlockNo());
                            sCar.setOnMCar(true);
                            if (Integer.parseInt(message35.Bay) == location.getBay()) {
                                crane.setMcKey(null);
                                crane.setLoaded(false);
                                sCar.setReservedMcKey(null);
                                sCar.setMcKey(message35.McKey);
                                sCar.setLoaded(true);
                            }
                        } else if (Message35._CycleOrder.move.equals(message35.CycleOrder)) {
                            //移动到站台、移动到目标level子车bay、移动到目标bay
                            if (message35.Bay.equals("00") && message35.Level.equals("00")) {
                                //移动到站台
                                crane.setLevel(0);
                                crane.setBay(0);
                            } else {
                                crane.setLevel(Integer.parseInt(message35.Level));
                                if (crane.isCarExist()) {
                                    crane.setMcKey(null);
                                    crane.setLoaded(false);
                                    SCar sCar = (SCar) Block.getByBlockNo(crane.getsCarBlockNo());
                                    sCar.setBay(location.getBay());
                                    crane.setBay(location.getBay());
                                    sCar.setReservedMcKey(null);
                                    sCar.setMcKey(message35.McKey);
                                    sCar.setLoaded(true);
                                }
                            }
                        } else if (Message35._CycleOrder.moveCarryGoods.equals(message35.CycleOrder)) {

                        } else if (Message35._CycleOrder.unloadCar.equals(message35.CycleOrder)) {
                            SCar sCar = (SCar) Block.getByBlockNo(crane.getsCarBlockNo());
                            sCar.setOnMCar(false);
                            crane.setCarExist(false);
                            crane.setsCarBlockNo(null);

                        }
                    } else {
                        Block nextBlock = block.getNextBlock(aj.getType(), aj.getToStation());
                        if (nextBlock == null) {
                            LogWriter.write(Msg35Proc.class, String.format("blockNo为%s的nextBlock为空", block.getBlockNo()), LogType.Error);
                            return;
                        }
                        if (block instanceof Conveyor) {
                            Conveyor conveyor = (Conveyor) block;
                            conveyor.setLoaded(false);
                            conveyor.setMcKey(null);
                            nextBlock.setMcKey(message35.McKey);
                            nextBlock.setLoaded(true);
                        } else if (block instanceof StationBlock) {
                            StationBlock station = (StationBlock) block;
                            if (Message35._CycleOrder.moveUnloadGoods.equals(message35.CycleOrder)) {
                                station.setLoaded(false);
                                station.setMcKey(null);
                                nextBlock.setMcKey(message35.McKey);
                                nextBlock.setLoaded(true);
                                if (nextBlock instanceof Lift) {
                                    Lift lift = (Lift) nextBlock;
                                    lift.setReservedMcKey(null);
                                } else if (nextBlock instanceof Crane) {
                                    Crane crane = (Crane) nextBlock;
                                    crane.setReservedMcKey(null);
                                }
                            }
                        } else if (block instanceof Lift) {
                            Lift lift = (Lift) block;
                            if (Message35._CycleOrder.move.equals(message35.CycleOrder)) {
                                Block block1 = Block.getByBlockNo(message35.Station);
                                if (block1 instanceof Dock) {
                                    Dock dock = (Dock) block1;
                                    lift.setLevel(dock.getLevel());
                                } else if (block1 instanceof MCar) {
                                    MCar mCar = (MCar) block1;
                                    lift.setLevel(mCar.getLevel());
                                } else {
                                    lift.setLevel(0);
                                }
                            } else if (Message35._CycleOrder.moveUnloadGoods.equals(message35.CycleOrder)) {
                                lift.setLoaded(false);
                                lift.setMcKey(null);
                                nextBlock.setLoaded(true);
                                nextBlock.setMcKey(message35.McKey);
                            }
                        } else if (block instanceof Dock) {
                            Dock dock = (Dock) block;
                            if (Message35._CycleOrder.moveUnloadGoods.equals(message35.CycleOrder)) {
                                dock.setLoaded(false);
                                dock.setMcKey(null);
                                nextBlock.setLoaded(true);
                                nextBlock.setMcKey(message35.McKey);
                                if (nextBlock instanceof MCar) {
                                    MCar mCar = (MCar) nextBlock;
                                    mCar.setReservedMcKey(null);
                                }
                            }
                        }
                    }
                } else if (AsrsJobType.RETRIEVAL.equals(aj.getType())) {
                    //出库
                    if (block instanceof SCar) {
                        SCar sCar = (SCar) block;
                        Block block1 = Block.getByBlockNo(aj.getFromStation());
                        if (Message35._CycleOrder.pickUpGoods.equals(message35.CycleOrder)) {
                            sCar.setReservedMcKey(null);
                            if (block1 instanceof MCar) {
                                MCar mCar = (MCar) block1;
                                mCar.setReservedMcKey(null);
                                mCar.setsCarBlockNo(sCar.getBlockNo());
                                mCar.setMcKey(message35.McKey);
                                mCar.setLoaded(true);
                                mCar.setCarExist(true);
                            } else if (block1 instanceof Crane) {
                                Crane crane = (Crane) block1;
                                crane.setReservedMcKey(null);
                                crane.setsCarBlockNo(sCar.getBlockNo());
                                crane.setMcKey(message35.McKey);
                                crane.setLoaded(true);
                                crane.setCarExist(true);
                            }

                        } else if (Message35._CycleOrder.onCar.equals(message35.CycleOrder)) {
                        } else if (Message35._CycleOrder.offCar.equals(message35.CycleOrder)) {
                        }
                    } else if (block instanceof StationBlock) {

                    } else {
                        Block nextBlock = block.getNextBlock(aj.getType(), aj.getToStation());
                        if (nextBlock == null) {
                            LogWriter.write(Msg35Proc.class, String.format("blockNo为%s的nextBlock为空", block.getBlockNo()), LogType.Error);
                            return;
                        }
                        if (block instanceof MCar) {
                            MCar mCar = (MCar) block;
                            if (Message35._CycleOrder.moveUnloadGoods.equals(message35.CycleOrder)) {
                                mCar.setMcKey(null);
                                mCar.setLoaded(false);
                                nextBlock.setLoaded(true);
                                nextBlock.setMcKey(message35.McKey);
                            } else if (Message35._CycleOrder.unloadCar.equals(message35.CycleOrder)) {
                                SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                sCar.setOnMCar(false);
                                mCar.setCarExist(false);
                                mCar.setsCarBlockNo(null);
                            } else if (Message35._CycleOrder.move.equals(message35.CycleOrder)) {
                                if (message35.Bay.equals("00")) {
                                    mCar.setLiftSide(true);
                                    mCar.setBay(0);
                                } else {
                                    mCar.setLiftSide(false);
                                    mCar.setBay(Integer.parseInt(message35.Bay));
                                    Location location = Location.getByLocationNo(aj.getFromLocation());
                                    if (mCar.isCarExist()) {
                                        SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                                        sCar.setBay(location.getBay());
                                    }
                                }
                            } else if (Message35._CycleOrder.loadCar.equals(message35.CycleOrder)) {
                                SCar sCar = (SCar) session.createCriteria(SCar.class).add(Restrictions.eq("reservedMcKey", mCar.getReservedMcKey())).uniqueResult();
                                mCar.setCarExist(true);
                                sCar.setOnMCar(true);
                                mCar.setsCarBlockNo(sCar.getBlockNo());

                            }
                        } else if (block instanceof Crane) {
                            Crane crane = (Crane) block;
                            if (Message35._CycleOrder.moveUnloadGoods.equals(message35.CycleOrder)) {
                                crane.setMcKey(null);
                                crane.setLoaded(false);
                                nextBlock.setLoaded(true);
                                nextBlock.setMcKey(message35.McKey);
                            } else if (Message35._CycleOrder.unloadCar.equals(message35.CycleOrder)) {
                                SCar sCar = (SCar) Block.getByBlockNo(crane.getsCarBlockNo());
                                sCar.setOnMCar(false);
                                crane.setCarExist(false);
                                crane.setsCarBlockNo(null);
                            } else if (Message35._CycleOrder.move.equals(message35.CycleOrder)) {
                                if (message35.Bay.equals("00") && message35.Level.equals("00")) {
                                    crane.setLevel(0);
                                    crane.setBay(0);
                                } else {
                                    crane.setLevel(Integer.parseInt(message35.Level));
                                    Location location = Location.getByLocationNo(aj.getFromLocation());
                                    if (crane.isCarExist()) {
                                        SCar sCar = (SCar) Block.getByBlockNo(crane.getsCarBlockNo());
                                        sCar.setBay(location.getBay());
                                        crane.setBay(location.getBay());
                                    }
                                }
                            } else if (Message35._CycleOrder.loadCar.equals(message35.CycleOrder)) {
                                SCar sCar = (SCar) session.createCriteria(SCar.class).add(Restrictions.eq("reservedMcKey", crane.getReservedMcKey())).uniqueResult();
                                crane.setCarExist(true);
                                sCar.setOnMCar(true);
                                crane.setsCarBlockNo(sCar.getBlockNo());
                            }
                        } else if (block instanceof Conveyor) {
                            Conveyor conveyor = (Conveyor) block;
                            conveyor.setLoaded(false);
                            conveyor.setMcKey(null);
                            nextBlock.setMcKey(message35.McKey);
                            nextBlock.setLoaded(true);

                        } else if (block instanceof Lift) {
                            Lift lift = (Lift) block;
                            if (Message35._CycleOrder.move.equals(message35.CycleOrder)) {
                                Block block1 = Block.getByBlockNo(message35.Station);
                                if (block1 instanceof Dock) {
                                    Dock dock = (Dock) block1;
                                    lift.setLevel(dock.getLevel());
                                } else if (block1 instanceof MCar) {
                                    MCar mCar = (MCar) block1;
                                    lift.setLevel(mCar.getLevel());
                                } else {
                                    lift.setLevel(0);
                                }
                            } else if (Message35._CycleOrder.moveUnloadGoods.equals(message35.CycleOrder)) {
                                lift.setLoaded(false);
                                lift.setMcKey(null);
                                nextBlock.setLoaded(true);
                                nextBlock.setMcKey(message35.McKey);
                            }
                        } else if (block instanceof Dock) {
                            Dock dock = (Dock) block;
                            if (Message35._CycleOrder.moveUnloadGoods.equals(message35.CycleOrder)) {
                                dock.setLoaded(false);
                                dock.setMcKey(null);
                                nextBlock.setLoaded(true);
                                nextBlock.setMcKey(message35.McKey);
                                if (nextBlock instanceof Lift) {
                                    Lift lift = (Lift) nextBlock;
                                    lift.setReservedMcKey(null);
                                }
                            }

                        }
                    }
                }
            }
            block.setWaitingResponse(false);
            Transaction.commit();
            MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
            _wcsproxy.addSndMsg(m5);
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();

        }
    }
}
