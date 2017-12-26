package com.test.utils;

import com.test.blocks.*;
import com.test.threads.*;

/**
 * Created by Administrator on 2016/10/26.
 */
public class ThreadFactory {
    public static BlockThread getThread(Block block) {
        if (block instanceof SCar) {
            return new SCarThread(block.getBlockNo());
        } else if (block instanceof Lift) {
            return new LiftThread(block.getBlockNo());
        } else if (block instanceof StationBlock) {
            return new StationThread(block.getBlockNo());
        } else if (block instanceof MCar) {
            return new MCarThread(block.getBlockNo());
        } else if (block instanceof Dock) {
            return new DockThread(block.getBlockNo());
        } else if (block instanceof Conveyor) {
            return new ConveyorThread(block.getBlockNo());
        } else if (block instanceof Crane) {
            return new CraneThread(block.getBlockNo());
        }
        return null;
    }
}

