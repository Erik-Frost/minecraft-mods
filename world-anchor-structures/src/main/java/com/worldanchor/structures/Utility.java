package com.worldanchor.structures;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public class Utility {

    public interface BlockStateInterface {
        boolean compareBlockState(BlockState blockstate);
    }

    public static boolean verticalBlockSampleCompare(VerticalBlockSample sample, BlockStateInterface condition,
            int from, int to) {
        if (from < to) {
            for (int y = from; y < to; y++) {
                if (!condition.compareBlockState(sample.getState(new BlockPos(0, y, 0)))) return false;
            }
        }
        else {
            for (int y = from; y > to; y--) {
                if (!condition.compareBlockState(sample.getState(new BlockPos(0, y, 0)))) return false;
            }
        }
        return true;
    }

}
