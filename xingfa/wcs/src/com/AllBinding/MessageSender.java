package com.AllBinding;

import com.AllBinding.blocks.*;
import com.AllBinding.utils.BlockStatus;
import com.AllBinding.utils.Message04Type;
import com.AllBinding.utils.Message06Status;
import com.AllBinding.utils.MsgSender;
import com.asrs.communication.MessageProxy;
import com.asrs.domain.Message;
import com.asrs.message.Message03;
import com.asrs.message.Message04;
import com.asrs.message.Message06;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.rmi.Naming;

/**
 * 发送设备状态更改请求
 * Created by Administrator on 2017/1/17.
 */
public class MessageSender {
    public static void main(String[] args) {

        while (true) {
            try {

                Transaction.begin();

                Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Message.class);
                criteria.add(Restrictions.eq(Message.__STATUS, Message.STATUS_WAIT));
                criteria.addOrder(Order.asc(Message.__CREATEDATE));
                criteria.setMaxResults(1);
                Message message = (Message) criteria.uniqueResult();

                if (message != null) {
                    if (message.getType().equals(Message.TYPE_REMOVE_ABNORMAL)) {
                        //异常解除
                        removeAbnormal(message);
                    } else if (message.getType().equals(Message.TYPE_OFFLINE)) {
                        //设备切离
                        offLine(message);
                    } else if (message.getType().equals(Message.TYPE_ONLINE)) {
                        //设备复位
                        onLine(message);
                    } else if (message.getType().equals(Message.TYPE_DELDATE)) {
                        //任务删除
                        deleteOrFinish(message, Message04Type.TYPE_DELETE);
                    } else if (message.getType().equals(Message.TYPE_FINISH)) {
                        //强制完成
                        deleteOrFinish(message, Message04Type.TYPE_FINISH);
                    } else if (message.getType().equals(Message.TYPE_ONCAR)) {
                        //子车上车
                        scarOnMcar(message);
                    } else if (message.getType().equals(Message.TYPE_LOADCAR)) {
                        //母车去接车
                        mCarmove(message);
                    }

                    message.setStatus(Message.STATUS_SEND);
                }


                Transaction.commit();

                Thread.sleep(500);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 设备异常解除
     *
     * @param message
     */
    private static void removeAbnormal(Message message) {
        try {
            MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
            Message06 message06 = new Message06();
            Block block = Block.getByBlockNo(message.getMachineNo());
            message06.setPlcName(block.getPlcName());
            message06.Status = "3";
            _wcsproxy.addSndMsg(message06);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设备恢复
     *
     * @param message
     * @throws Exception
     */
    private static void onLine(Message message) throws Exception {
        try {
            MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
            Message06 message06 = new Message06();
            Block block = Block.getByBlockNo(message.getMachineNo());
            message06.setPlcName(block.getPlcName());
            message06.Status = Message06Status.STATUS_ON_LINE;
            _wcsproxy.addSndMsg(message06);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设备切离
     *
     * @param message
     * @throws Exception
     */
    private static void offLine(Message message) throws Exception {
        try {
            MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
            Message06 message06 = new Message06();
            Block block = Block.getByBlockNo(message.getMachineNo());
            message06.setPlcName(block.getPlcName());
            message06.Status = Message06Status.STATUS_OFF_LINE;
            _wcsproxy.addSndMsg(message06);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除数据
     *
     * @param message
     * @throws Exception
     */
    public static void deleteOrFinish(Message message, String type) throws Exception {
        try {
            MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
            Block block = Block.getByBlockNo(message.getMachineNo());

            //非运行模式清除数据
            if (!block.getStatus().equals(BlockStatus.STATUS_RUN)) {

                String mcKey = null;
                if (block instanceof SCar)
                    mcKey = block.getMcKey() != null ? block.getMcKey() : ((SCar) block).getReservedMcKey();
                else
                    mcKey = block.getMcKey();
                if (mcKey != null) {

                    Message04 message04 = new Message04();
                    message04.setPlcName(block.getPlcName());
                    message04.MachineNo = block.getPlcName();
                    message04.mcKey = block.getMcKey();
                    message04.type = type;
                    _wcsproxy.addSndMsg(message04);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 子车上母车
     *
     * @param message
     * @throws Exception
     */
    public static void scarOnMcar(Message message) throws Exception {
        try {
            Block block = Block.getByBlockNo(message.getMachineNo());
            Block nextBlock = Block.getByBlockNo(message.getNextBlock());
            MsgSender.send03(Message03._CycleOrder.onCar, "9999", block, "", nextBlock.getBlockNo(), "", "");
            MsgSender.send03(Message03._CycleOrder.loadCar, "9999", nextBlock, "", block.getBlockNo(), "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 母车移动
     *
     * @param message
     */
    public static void mCarmove(Message message) throws Exception {
        try {
            Block block = Block.getByBlockNo(message.getMachineNo());

            //费运行模式下方可运行手动操作
            if (!block.getStatus().equals(BlockStatus.STATUS_RUN)) {
                if (block instanceof Crane) {
                    Crane crane = (Crane) block;
                    Block nextBlock = Block.getByBlockNo(message.getNextBlock());
                    if (nextBlock instanceof SCar) {
                        SCar sCar = (SCar) nextBlock;
                        if (sCar.isBindingCrane())
                            MsgSender.send03(Message03._CycleOrder.move, "9999", crane, "", "", sCar.getBay() + "", sCar.getLevel() + "");
                    }
                }
            }

        } catch (Exception e) {

        }
    }
}
