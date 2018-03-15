package com.thread.threads.operator;

import com.asrs.domain.Location;
import com.asrs.message.Message03;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.Srm;
import com.thread.utils.MsgSender;
import org.apache.commons.lang.StringUtils;

/**
 * Created by van on 2017/11/2.
 */
public class MCarOperator {

    private MCar mCar;
    private String mckey;

    public MCarOperator(MCar mCar, String mcKey) {
        this.mCar = mCar;
        this.mckey = mcKey;
    }

    /**
     * 移栽取货
     *
     * @param block
     * @throws Exception
     */
    public void moveCarryGoods(String block) throws Exception {

        MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, mCar, "", block, "", "");

    }

    /**
     * 移栽卸货
     *
     * @param block
     * @throws Exception
     */
    public void moveUnloadGoods(String block) throws Exception {
        Block nextBlock = Block.getByBlockNo(block);
        MsgSender.send03(Message03._CycleOrder.moveUnloadGoods, mckey, mCar, "", block, "", "");
        MsgSender.send03(Message03._CycleOrder.moveCarryGoods, mckey, nextBlock, "", mCar.getBlockNo(), "", "");
    }

    /**
     * 移动到指定block边
     *
     * @param block
     * @throws Exception
     */
    public void move(String block) throws Exception {
        MsgSender.send03(Message03._CycleOrder.move, mckey, mCar, "", block, "", "");
    }

    /**
     * 移动
     *
     * @param toLocation 移动位置
     * @throws Exception
     */
    public void move(Location toLocation) throws Exception {
        MsgSender.send03(Message03._CycleOrder.move, mckey, mCar, toLocation.getLocationNo(), "", "", "");
    }

    /**
     * 母车尝试去接货
     *
     * @param block
     */
    public void tryLoadGoodsFromConveryor(Block block) throws Exception {

        if (!block.getBlockNo().equals(mCar.getDock()) || !mCar.getCheckLocation()) {
            move(block.getBlockNo());
        } else {
            moveCarryGoods(block.getBlockNo());
        }

    }

    /**
     * 母车尝试卸货
     *
     * @param block
     */
    public void tryUnloadGoodToConveryor(Block block) throws Exception {

        if (!block.getBlockNo().equals(mCar.getDock()) || !mCar.getCheckLocation()) {
            move(block.getBlockNo());
        } else {
            moveUnloadGoods(block.getBlockNo());

        }

    }


}
