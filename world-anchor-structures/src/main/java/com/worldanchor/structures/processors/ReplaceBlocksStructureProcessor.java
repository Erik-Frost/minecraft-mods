package com.worldanchor.structures.processors;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.worldanchor.structures.Server;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class ReplaceBlocksStructureProcessor extends StructureProcessor {

    public static final Codec<ReplaceBlocksStructureProcessor> CODEC = RecordCodecBuilder.create((proc) -> proc.group(
            Codec.list(BlockState.CODEC).fieldOf("blocksToReplace").forGetter(processor -> processor.blocksToReplace),
            Codec.list(Codec.pair(Codec.INT, BlockState.CODEC)).fieldOf("replacingBlocks").forGetter(processor -> processor.replacingBlockWeights)
    ).apply(proc, ReplaceBlocksStructureProcessor::new));

    public static StructureProcessorType<ReplaceBlocksStructureProcessor> TYPE = StructureProcessorType
            .register(Server.MODID + ":replace-blocks-structure-processor", CODEC);

    private final List<BlockState> blocksToReplace;
    private final List<Pair<Integer, BlockState>> replacingBlockWeights;
    private int totalWeight;

    public ReplaceBlocksStructureProcessor(List<BlockState> blocksToReplace, List<Pair<Integer, BlockState>> replacingBlockWeights) {
        this.blocksToReplace = blocksToReplace;
        this.replacingBlockWeights = replacingBlockWeights;
        totalWeight = 0;
        for (Pair<Integer, BlockState> replacingBlockWeight : replacingBlockWeights) {
            totalWeight += replacingBlockWeight.getFirst();
        }
    }

    // This method is called for each block in the structure
    @Nullable
    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot,
            Structure.StructureBlockInfo structureBlockInfo, Structure.StructureBlockInfo structureBlockInfo2,
            StructurePlacementData data) {
        Random random = data.getRandom(structureBlockInfo2.pos);
        BlockState blockState = structureBlockInfo2.state;
        BlockPos blockPos = structureBlockInfo2.pos;

        if (blocksToReplace.contains(blockState.getBlock().getDefaultState())) {
            int num = random.nextInt(totalWeight);
            for (Pair<Integer, BlockState> replacingBlockWeight : replacingBlockWeights) {
                if ((num = num - replacingBlockWeight.getFirst()) < 0)  {
                    blockState = replacingBlockWeight.getSecond().getBlock().getStateWithProperties(blockState);
                    break;
                }
            }
        }

        // Return new blockstate
        return new Structure.StructureBlockInfo(blockPos, blockState, structureBlockInfo2.nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TYPE;
    }
}
