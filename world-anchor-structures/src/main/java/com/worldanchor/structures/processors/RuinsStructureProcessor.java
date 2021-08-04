package com.worldanchor.structures.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.worldanchor.structures.Server;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class RuinsStructureProcessor extends StructureProcessor {

    public static final Codec<RuinsStructureProcessor> CODEC = RecordCodecBuilder.create((proc) -> proc.group(
            Codec.FLOAT.fieldOf("crackChance").forGetter(processor -> processor.crackChance),
            Codec.FLOAT.fieldOf("mossChance").forGetter(processor -> processor.mossChance),
            Codec.FLOAT.fieldOf("erodeChance").forGetter(processor -> processor.erodeChance),
            Codec.FLOAT.fieldOf("infestChance").forGetter(processor -> processor.infestChance)
    ).apply(proc, RuinsStructureProcessor::new));

    public static StructureProcessorType<RuinsStructureProcessor> TYPE = StructureProcessorType
            .register(Server.MODID + ":ruins-structure-processor", CODEC);

    private final float mossChance;
    private final float crackChance;
    private final float erodeChance;
    private final float infestChance;

    public RuinsStructureProcessor(float crackChance, float mossChance, float erodeChance, float infestChance) {
        this.crackChance = crackChance;
        this.mossChance = mossChance;
        this.erodeChance = erodeChance;
        this.infestChance = infestChance;
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

        if (crackChance > 0 && random.nextFloat() <= crackChance) {
            if (blockState.is(Blocks.STONE_BRICKS)) { blockState = Blocks.CRACKED_STONE_BRICKS.defaultBlockState(); }
            else if (blockState.is(Blocks.DEEPSLATE_BRICKS)) { blockState = Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState(); }
            else if (blockState.is(Blocks.DEEPSLATE_TILES)) { blockState = Blocks.CRACKED_DEEPSLATE_TILES.defaultBlockState(); }
            else if (blockState.is(Blocks.NETHER_BRICKS)) { blockState = Blocks.CRACKED_NETHER_BRICKS.defaultBlockState(); }
            else if (blockState.is(Blocks.OBSIDIAN)) { blockState = Blocks.CRYING_OBSIDIAN.defaultBlockState(); }
            else if (blockState.is(Blocks.POLISHED_BLACKSTONE_BRICKS)) { blockState = Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState(); }
        }
        else if (mossChance > 0 && random.nextFloat() <= mossChance) {
            if (blockState.is(Blocks.COBBLESTONE)) {blockState = Blocks.MOSSY_COBBLESTONE.defaultBlockState(); }
            else if (blockState.is(Blocks.COBBLESTONE_SLAB)) {blockState = Blocks.MOSSY_COBBLESTONE_SLAB.withPropertiesOf(blockState); }
            else if (blockState.is(Blocks.COBBLESTONE_STAIRS)) {blockState = Blocks.MOSSY_COBBLESTONE_STAIRS.withPropertiesOf(blockState); }
            else if (blockState.is(Blocks.COBBLESTONE_WALL)) {blockState = Blocks.MOSSY_COBBLESTONE_WALL.withPropertiesOf(blockState); }
            else if (blockState.is(Blocks.STONE_BRICKS)) {blockState = Blocks.MOSSY_STONE_BRICKS.defaultBlockState(); }
            else if (blockState.is(Blocks.STONE_BRICK_SLAB)) {blockState = Blocks.MOSSY_STONE_BRICK_SLAB.withPropertiesOf(blockState); }
            else if (blockState.is(Blocks.STONE_BRICK_STAIRS)) {blockState = Blocks.MOSSY_STONE_BRICK_STAIRS.withPropertiesOf(blockState); }
            else if (blockState.is(Blocks.STONE_BRICK_WALL)) {blockState = Blocks.MOSSY_STONE_BRICK_WALL.withPropertiesOf(blockState); }
        }

        if (erodeChance > 0 && random.nextFloat() <= erodeChance) {
            // Full block to stairs
            if (blockState.is(Blocks.STONE_BRICKS)) {blockState = Blocks.STONE_BRICK_STAIRS.defaultBlockState().setValue(
                    StairBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random)).setValue(StairBlock.HALF, Half
                    .values()[random.nextInt(Half.values().length)]); }
            else if (blockState.is(Blocks.MOSSY_STONE_BRICKS)) {blockState = Blocks.MOSSY_STONE_BRICK_STAIRS.defaultBlockState().setValue(
                    StairBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random)).setValue(StairBlock.HALF, Half
                    .values()[random.nextInt(Half.values().length)]); }
            else if (blockState.is(Blocks.NETHER_BRICKS)) {blockState = Blocks.NETHER_BRICK_STAIRS.defaultBlockState().setValue(
                    StairBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random)).setValue(StairBlock.HALF, Half
                    .values()[random.nextInt(Half.values().length)]); }
            else if (blockState.is(Blocks.DEEPSLATE_BRICKS)) {blockState = Blocks.DEEPSLATE_BRICK_STAIRS.defaultBlockState().setValue(
                    StairBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random)).setValue(StairBlock.HALF, Half
                    .values()[random.nextInt(Half.values().length)]); }
            else if (blockState.is(Blocks.DEEPSLATE_TILES)) {blockState = Blocks.DEEPSLATE_TILE_STAIRS.defaultBlockState().setValue(
                    StairBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random)).setValue(StairBlock.HALF, Half
                    .values()[random.nextInt(Half.values().length)]); }
        }

        if (infestChance > 0 && random.nextFloat() <= infestChance) {
            if (blockState.is(Blocks.STONE)) {blockState = Blocks.INFESTED_STONE.defaultBlockState(); }
            else if (blockState.is(Blocks.DEEPSLATE)) {blockState = Blocks.INFESTED_DEEPSLATE.defaultBlockState(); }
            else if (blockState.is(Blocks.COBBLESTONE)) {blockState = Blocks.INFESTED_COBBLESTONE.defaultBlockState(); }
            else if (blockState.is(Blocks.STONE_BRICKS)) {blockState = Blocks.INFESTED_STONE_BRICKS.defaultBlockState(); }
            else if (blockState.is(Blocks.MOSSY_STONE_BRICKS)) {blockState = Blocks.INFESTED_MOSSY_STONE_BRICKS.defaultBlockState(); }
            else if (blockState.is(Blocks.CRACKED_STONE_BRICKS)) {blockState = Blocks.INFESTED_CRACKED_STONE_BRICKS.defaultBlockState(); }
            else if (blockState.is(Blocks.CHISELED_STONE_BRICKS)) {blockState = Blocks.INFESTED_CHISELED_STONE_BRICKS.defaultBlockState(); }
        }

        // Return new blockstate
        return new StructureTemplate.StructureBlockInfo(blockPos, blockState, structureBlockInfo2.nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TYPE;
    }
}
