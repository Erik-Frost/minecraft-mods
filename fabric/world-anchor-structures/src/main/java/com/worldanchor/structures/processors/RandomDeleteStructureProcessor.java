package com.worldanchor.structures.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.worldanchor.structures.Server;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
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
    public  StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos pivot,
            StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo2,
            StructurePlaceSettings data) {
        Random random = data.getRandom(structureBlockInfo2.pos);
        BlockState blockState = structureBlockInfo2.state;
        BlockPos blockPos = structureBlockInfo2.pos;

        if (!blockState.is(Blocks.STRUCTURE_VOID) && !blockState.isAir() && random.nextFloat() <= deleteChance) {
            if (makeWhitelistIntoBlacklist && !blocksToDelete.contains(blockState.getBlock().defaultBlockState()))
                return new StructureTemplate.StructureBlockInfo(blockPos, Blocks.AIR.defaultBlockState(), structureBlockInfo2.nbt);
            else if (!makeWhitelistIntoBlacklist && blocksToDelete.contains(blockState.getBlock().defaultBlockState()))
                return new StructureTemplate.StructureBlockInfo(blockPos, Blocks.AIR.defaultBlockState(), structureBlockInfo2.nbt);
        }

        // Return new blockstate
        return new StructureTemplate.StructureBlockInfo(blockPos, blockState, structureBlockInfo2.nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TYPE;
    }
}
