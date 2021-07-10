package com.worldanchor.structures.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.worldanchor.structures.Server;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class RuinsStructureProcessor extends StructureProcessor {

    public static final Codec<RuinsStructureProcessor> CODEC = RecordCodecBuilder.create((proc) -> proc.group(
            Codec.FLOAT.fieldOf("deleteChance").forGetter(processor -> processor.deleteChance),
            Codec.FLOAT.fieldOf("crackChance").forGetter(processor -> processor.crackChance),
            Codec.FLOAT.fieldOf("mossChance").forGetter(processor -> processor.mossChance),
            Codec.FLOAT.fieldOf("erodeChance").forGetter(processor -> processor.erodeChance),
            Codec.FLOAT.fieldOf("infestChance").forGetter(processor -> processor.infestChance),
            Codec.list(BlockState.CODEC).fieldOf("excludeDeleteBlocks").forGetter(processor -> processor.excludeDeleteBlocks)
    ).apply(proc, RuinsStructureProcessor::new));

    private final float deleteChance;
    private final float mossChance;
    private final float crackChance;
    private final float erodeChance;
    private final float infestChance;
    private final List<BlockState> excludeDeleteBlocks;

    public RuinsStructureProcessor(float deleteChance, float crackChance, float mossChance, float erodeChance,
            float infestChance, List<BlockState> excludeDeleteBlocks) {
        this.deleteChance = deleteChance;
        this.crackChance = crackChance;
        this.mossChance = mossChance;
        this.erodeChance = erodeChance;
        this.infestChance = infestChance;
        this.excludeDeleteBlocks = excludeDeleteBlocks;
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

        if (deleteChance > 0 && random.nextFloat() <= deleteChance
                && !excludeDeleteBlocks.contains(blockState.getBlock().getDefaultState())) {
            return new Structure.StructureBlockInfo(blockPos, Blocks.AIR.getDefaultState(), structureBlockInfo2.nbt);
        }

        if (crackChance > 0 && random.nextFloat() <= crackChance) {
            if (blockState.isOf(Blocks.STONE_BRICKS)) { blockState = Blocks.CRACKED_STONE_BRICKS.getDefaultState(); }
            else if (blockState.isOf(Blocks.DEEPSLATE_BRICKS)) { blockState = Blocks.CRACKED_DEEPSLATE_BRICKS.getDefaultState(); }
            else if (blockState.isOf(Blocks.DEEPSLATE_TILES)) { blockState = Blocks.CRACKED_DEEPSLATE_TILES.getDefaultState(); }
            else if (blockState.isOf(Blocks.NETHER_BRICKS)) { blockState = Blocks.CRACKED_NETHER_BRICKS.getDefaultState(); }
            else if (blockState.isOf(Blocks.POLISHED_BLACKSTONE_BRICKS)) { blockState = Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.getDefaultState(); }
        }
        else if (mossChance > 0 && random.nextFloat() <= mossChance) {
            if (blockState.isOf(Blocks.COBBLESTONE)) {blockState = Blocks.MOSSY_COBBLESTONE.getDefaultState(); }
            else if (blockState.isOf(Blocks.COBBLESTONE_SLAB)) {blockState = Blocks.MOSSY_COBBLESTONE_SLAB.getStateWithProperties(blockState); }
            else if (blockState.isOf(Blocks.COBBLESTONE_STAIRS)) {blockState = Blocks.MOSSY_COBBLESTONE_STAIRS.getStateWithProperties(blockState); }
            else if (blockState.isOf(Blocks.COBBLESTONE_WALL)) {blockState = Blocks.MOSSY_COBBLESTONE_WALL.getStateWithProperties(blockState); }
            else if (blockState.isOf(Blocks.STONE_BRICKS)) {blockState = Blocks.MOSSY_STONE_BRICKS.getDefaultState(); }
            else if (blockState.isOf(Blocks.STONE_BRICK_SLAB)) {blockState = Blocks.MOSSY_STONE_BRICK_SLAB.getStateWithProperties(blockState); }
            else if (blockState.isOf(Blocks.STONE_BRICK_STAIRS)) {blockState = Blocks.MOSSY_STONE_BRICK_STAIRS.getStateWithProperties(blockState); }
            else if (blockState.isOf(Blocks.STONE_BRICK_WALL)) {blockState = Blocks.MOSSY_STONE_BRICK_WALL.getStateWithProperties(blockState); }
        }


        if (erodeChance > 0 && random.nextFloat() <= erodeChance) {
            // Full block to stiars
            if (blockState.isOf(Blocks.STONE_BRICKS)) {blockState = Blocks.STONE_BRICK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.Type.HORIZONTAL.random(random)).with(StairsBlock.HALF, BlockHalf
                    .values()[random.nextInt(BlockHalf.values().length)]); }
            else if (blockState.isOf(Blocks.MOSSY_STONE_BRICKS)) {blockState = Blocks.MOSSY_STONE_BRICK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.Type.HORIZONTAL.random(random)).with(StairsBlock.HALF, BlockHalf
                    .values()[random.nextInt(BlockHalf.values().length)]); }
            else if (blockState.isOf(Blocks.NETHER_BRICKS)) {blockState = Blocks.NETHER_BRICK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.Type.HORIZONTAL.random(random)).with(StairsBlock.HALF, BlockHalf
                    .values()[random.nextInt(BlockHalf.values().length)]); }
        }


        if (infestChance > 0 && random.nextFloat() <= infestChance) {
            if (blockState.isOf(Blocks.STONE)) {blockState = Blocks.INFESTED_STONE.getDefaultState(); }
            else if (blockState.isOf(Blocks.DEEPSLATE)) {blockState = Blocks.INFESTED_DEEPSLATE.getDefaultState(); }
            else if (blockState.isOf(Blocks.COBBLESTONE)) {blockState = Blocks.INFESTED_COBBLESTONE.getDefaultState(); }
            else if (blockState.isOf(Blocks.STONE_BRICKS)) {blockState = Blocks.INFESTED_STONE_BRICKS.getDefaultState(); }
            else if (blockState.isOf(Blocks.MOSSY_STONE_BRICKS)) {blockState = Blocks.INFESTED_MOSSY_STONE_BRICKS.getDefaultState(); }
            else if (blockState.isOf(Blocks.CRACKED_STONE_BRICKS)) {blockState = Blocks.INFESTED_CRACKED_STONE_BRICKS.getDefaultState(); }
            else if (blockState.isOf(Blocks.CHISELED_STONE_BRICKS)) {blockState = Blocks.INFESTED_CHISELED_STONE_BRICKS.getDefaultState(); }
        }




        // Return new blockstate
        return new Structure.StructureBlockInfo(blockPos, blockState, structureBlockInfo2.nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return Server.RUINS_STRUCTURE_PROCESSOR_TYPE;
    }
}
