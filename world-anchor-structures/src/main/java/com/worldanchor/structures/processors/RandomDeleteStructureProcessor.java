package com.worldanchor.structures.processors;

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

public class RandomDeleteStructureProcessor extends StructureProcessor {


    public static final Codec<RandomDeleteStructureProcessor> CODEC = RecordCodecBuilder.create((proc) -> proc.group(
            Codec.FLOAT.fieldOf("deleteChance").forGetter(processor -> processor.deleteChance),
            Codec.BOOL.fieldOf("makeWhitelistIntoBlacklist").forGetter(processor -> processor.makeWhitelistIntoBlacklist),
            Codec.list(BlockState.CODEC).fieldOf("blocksToDelete").forGetter(processor -> processor.blocksToDelete)
    ).apply(proc, RandomDeleteStructureProcessor::new));

    public static StructureProcessorType<RandomDeleteStructureProcessor> TYPE = StructureProcessorType
            .register(Server.MODID + ":random-delete-structure-processor", CODEC);

    private final float deleteChance;
    private final boolean makeWhitelistIntoBlacklist;
    private final List<BlockState> blocksToDelete;

    public RandomDeleteStructureProcessor(float deleteChance, boolean makeWhitelistIntoBlacklist,
            List<BlockState> blocksToDelete) {
        this.deleteChance = deleteChance;
        this.makeWhitelistIntoBlacklist = makeWhitelistIntoBlacklist;
        this.blocksToDelete = blocksToDelete;
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

        if (!blockState.isOf(Blocks.STRUCTURE_VOID) && !blockState.isAir() && random.nextFloat() <= deleteChance) {
            if (makeWhitelistIntoBlacklist && !blocksToDelete.contains(blockState.getBlock().getDefaultState()))
                return new Structure.StructureBlockInfo(blockPos, Blocks.AIR.getDefaultState(), structureBlockInfo2.nbt);
            else if (!makeWhitelistIntoBlacklist && blocksToDelete.contains(blockState.getBlock().getDefaultState()))
                return new Structure.StructureBlockInfo(blockPos, Blocks.AIR.getDefaultState(), structureBlockInfo2.nbt);
        }

        // Return new blockstate
        return new Structure.StructureBlockInfo(blockPos, blockState, structureBlockInfo2.nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TYPE;
    }
}
