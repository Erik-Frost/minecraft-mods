package com.worldanchor.structures.processors;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.worldanchor.structures.Server;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class ReplaceBlocksStructureProcessor extends StructureProcessor {

    public static final Codec<ReplaceBlocksStructureProcessor> CODEC = RecordCodecBuilder.create((proc) -> proc.group(
            Codec.list(BlockState.CODEC).fieldOf("blocksToReplace").forGetter(processor -> processor.blocksToReplace),
            Codec.compoundList(Codec.STRING, BlockState.CODEC).fieldOf("replacingBlocks").forGetter(processor -> processor.replacingBlockWeights)
    ).apply(proc, ReplaceBlocksStructureProcessor::new));

    public static StructureProcessorType<ReplaceBlocksStructureProcessor> TYPE = StructureProcessorType
            .register(Server.MODID + ":replace-blocks-structure-processor", CODEC);

    private final List<BlockState> blocksToReplace;
    private final List<Pair<String, BlockState>> replacingBlockWeights;
    private int totalWeight;

    public ReplaceBlocksStructureProcessor(List<BlockState> blocksToReplace, List<Pair<String, BlockState>> replacingBlockWeights) {
        this.blocksToReplace = blocksToReplace;
        this.replacingBlockWeights = replacingBlockWeights;
        totalWeight = 0;
        for (Pair<String, BlockState> replacingBlockWeight : replacingBlockWeights) {
            totalWeight += Integer.parseInt(replacingBlockWeight.getFirst());
        }
    }

    // This method is called for each block in the structure
    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos pivot,
            StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo2,
            StructurePlaceSettings data) {
        Random random = data.getRandom(structureBlockInfo2.pos);
        BlockState blockState = structureBlockInfo2.state;
        BlockPos blockPos = structureBlockInfo2.pos;
        if (blocksToReplace.contains(blockState.getBlock().defaultBlockState())) {
            int num = random.nextInt(totalWeight);
            for (Pair<String, BlockState> replacingBlockWeight : replacingBlockWeights) {
                if ((num = num - Integer.parseInt(replacingBlockWeight.getFirst())) < 0)  {
                    blockState = replacingBlockWeight.getSecond().getBlock().withPropertiesOf(blockState);
                    break;
                }
            }
        }

        // Return new blockstate
        return new StructureTemplate.StructureBlockInfo(blockPos, blockState, structureBlockInfo2.nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TYPE;
    }
}
