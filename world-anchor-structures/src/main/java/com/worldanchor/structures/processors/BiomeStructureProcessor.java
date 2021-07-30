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
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BiomeStructureProcessor extends StructureProcessor {

    public static final Codec<BiomeStructureProcessor> CODEC = RecordCodecBuilder.create((proc) -> proc.group(
            Codec.BOOL.fieldOf("convertWood").forGetter(processor -> processor.convertWood)
    ).apply(proc, BiomeStructureProcessor::new));

    public static StructureProcessorType<BiomeStructureProcessor> TYPE = StructureProcessorType
            .register(Server.MODID + ":biome-structure-processor", CODEC);

    private boolean convertWood;

    public BiomeStructureProcessor(boolean convertWood) {
        this.convertWood = convertWood;
    }

    // This method is called for each block in the structure
    @Nullable
    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot,
            Structure.StructureBlockInfo structureBlockInfo, Structure.StructureBlockInfo structureBlockInfo2,
            StructurePlacementData data) {
        Random random = data.getRandom(structureBlockInfo2.pos);
        BlockState bS = structureBlockInfo2.state;
        BlockPos blockPos = structureBlockInfo2.pos;

        if (!bS.isAir() && !bS.isOf(Blocks.STRUCTURE_VOID)) {
            if (world.getBiome(blockPos).getCategory() == Biome.Category.TAIGA) {
                if (bS.isIn(BlockTags.WOODEN_FENCES)) bS = Blocks.SPRUCE_FENCE.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_STAIRS)) bS = Blocks.SPRUCE_STAIRS.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_DOORS)) bS = Blocks.SPRUCE_DOOR.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_BUTTONS)) bS = Blocks.SPRUCE_BUTTON.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_SLABS)) bS = Blocks.SPRUCE_SLAB.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_PRESSURE_PLATES)) bS = Blocks.SPRUCE_PRESSURE_PLATE.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_TRAPDOORS)) bS = Blocks.SPRUCE_TRAPDOOR.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.PLANKS)) bS = Blocks.SPRUCE_PLANKS.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.LEAVES)) bS = Blocks.SPRUCE_LEAVES.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.FENCE_GATES)) bS = Blocks.SPRUCE_FENCE_GATE.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.STANDING_SIGNS)) bS = Blocks.SPRUCE_SIGN.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WALL_SIGNS)) bS = Blocks.SPRUCE_WALL_SIGN.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.SAPLINGS)) bS = Blocks.SPRUCE_SAPLING.getStateWithProperties(bS);
                else if (bS.isOf(Blocks.SPRUCE_LOG) || bS.isOf(Blocks.ACACIA_LOG)
                        || bS.isOf(Blocks.BIRCH_LOG) || bS.isOf(Blocks.OAK_LOG)
                        || bS.isOf(Blocks.JUNGLE_LOG) || bS.isOf(Blocks.DARK_OAK_LOG))
                    bS = Blocks.SPRUCE_LOG.getStateWithProperties(bS);
                else if (bS.isOf(Blocks.STRIPPED_SPRUCE_LOG) || bS.isOf(Blocks.STRIPPED_ACACIA_LOG)
                        || bS.isOf(Blocks.STRIPPED_BIRCH_LOG) || bS.isOf(Blocks.STRIPPED_OAK_LOG)
                        || bS.isOf(Blocks.STRIPPED_JUNGLE_LOG) || bS.isOf(Blocks.STRIPPED_DARK_OAK_LOG))
                    bS = Blocks.STRIPPED_SPRUCE_LOG.getStateWithProperties(bS);
                else if (bS.isOf(Blocks.SPRUCE_WOOD) || bS.isOf(Blocks.ACACIA_WOOD)
                        || bS.isOf(Blocks.BIRCH_WOOD) || bS.isOf(Blocks.OAK_WOOD)
                        || bS.isOf(Blocks.JUNGLE_WOOD) || bS.isOf(Blocks.DARK_OAK_WOOD))
                    bS = Blocks.SPRUCE_WOOD.getStateWithProperties(bS);
                else if (bS.isOf(Blocks.STRIPPED_SPRUCE_WOOD) || bS.isOf(Blocks.STRIPPED_ACACIA_WOOD)
                        || bS.isOf(Blocks.STRIPPED_BIRCH_WOOD) || bS.isOf(Blocks.STRIPPED_OAK_WOOD)
                        || bS.isOf(Blocks.STRIPPED_JUNGLE_WOOD) || bS.isOf(Blocks.STRIPPED_DARK_OAK_WOOD))
                    bS = Blocks.STRIPPED_SPRUCE_WOOD.getStateWithProperties(bS);
            }
            else if (world.getBiome(blockPos).getCategory() == Biome.Category.JUNGLE) {
                if (bS.isIn(BlockTags.WOODEN_FENCES)) bS = Blocks.JUNGLE_FENCE.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_STAIRS)) bS = Blocks.JUNGLE_STAIRS.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_DOORS)) bS = Blocks.JUNGLE_DOOR.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_BUTTONS)) bS = Blocks.JUNGLE_BUTTON.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_SLABS)) bS = Blocks.JUNGLE_SLAB.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_PRESSURE_PLATES)) bS = Blocks.JUNGLE_PRESSURE_PLATE.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_TRAPDOORS)) bS = Blocks.JUNGLE_TRAPDOOR.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.PLANKS)) bS = Blocks.JUNGLE_PLANKS.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.LEAVES)) bS = Blocks.JUNGLE_LEAVES.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.FENCE_GATES)) bS = Blocks.JUNGLE_FENCE_GATE.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.STANDING_SIGNS)) bS = Blocks.JUNGLE_SIGN.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WALL_SIGNS)) bS = Blocks.JUNGLE_WALL_SIGN.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.SAPLINGS)) bS = Blocks.JUNGLE_SAPLING.getStateWithProperties(bS);
                else if (bS.isOf(Blocks.SPRUCE_LOG) || bS.isOf(Blocks.ACACIA_LOG)
                        || bS.isOf(Blocks.BIRCH_LOG) || bS.isOf(Blocks.OAK_LOG)
                        || bS.isOf(Blocks.JUNGLE_LOG) || bS.isOf(Blocks.DARK_OAK_LOG))
                    bS = Blocks.JUNGLE_LOG.getStateWithProperties(bS);
                else if (bS.isOf(Blocks.STRIPPED_SPRUCE_LOG) || bS.isOf(Blocks.STRIPPED_ACACIA_LOG)
                        || bS.isOf(Blocks.STRIPPED_BIRCH_LOG) || bS.isOf(Blocks.STRIPPED_OAK_LOG)
                        || bS.isOf(Blocks.STRIPPED_JUNGLE_LOG) || bS.isOf(Blocks.STRIPPED_DARK_OAK_LOG))
                    bS = Blocks.STRIPPED_JUNGLE_LOG.getStateWithProperties(bS);
                else if (bS.isOf(Blocks.SPRUCE_WOOD) || bS.isOf(Blocks.ACACIA_WOOD)
                        || bS.isOf(Blocks.BIRCH_WOOD) || bS.isOf(Blocks.OAK_WOOD)
                        || bS.isOf(Blocks.JUNGLE_WOOD) || bS.isOf(Blocks.DARK_OAK_WOOD))
                    bS = Blocks.JUNGLE_WOOD.getStateWithProperties(bS);
                else if (bS.isOf(Blocks.STRIPPED_SPRUCE_WOOD) || bS.isOf(Blocks.STRIPPED_ACACIA_WOOD)
                        || bS.isOf(Blocks.STRIPPED_BIRCH_WOOD) || bS.isOf(Blocks.STRIPPED_OAK_WOOD)
                        || bS.isOf(Blocks.STRIPPED_JUNGLE_WOOD) || bS.isOf(Blocks.STRIPPED_DARK_OAK_WOOD))
                    bS = Blocks.STRIPPED_JUNGLE_WOOD.getStateWithProperties(bS);
            }
            else if (world.getBiome(blockPos).getCategory() == Biome.Category.SAVANNA) {
                if (bS.isIn(BlockTags.WOODEN_FENCES)) bS = Blocks.ACACIA_FENCE.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_STAIRS)) bS = Blocks.ACACIA_STAIRS.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_DOORS)) bS = Blocks.ACACIA_DOOR.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_BUTTONS)) bS = Blocks.ACACIA_BUTTON.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_SLABS)) bS = Blocks.ACACIA_SLAB.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_PRESSURE_PLATES)) bS = Blocks.ACACIA_PRESSURE_PLATE.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WOODEN_TRAPDOORS)) bS = Blocks.ACACIA_TRAPDOOR.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.PLANKS)) bS = Blocks.ACACIA_PLANKS.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.LEAVES)) bS = Blocks.ACACIA_LEAVES.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.FENCE_GATES)) bS = Blocks.ACACIA_FENCE_GATE.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.STANDING_SIGNS)) bS = Blocks.ACACIA_SIGN.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.WALL_SIGNS)) bS = Blocks.ACACIA_WALL_SIGN.getStateWithProperties(bS);
                else if (bS.isIn(BlockTags.SAPLINGS)) bS = Blocks.ACACIA_SAPLING.getStateWithProperties(bS);
                else if (bS.isOf(Blocks.SPRUCE_LOG) || bS.isOf(Blocks.ACACIA_LOG)
                        || bS.isOf(Blocks.BIRCH_LOG) || bS.isOf(Blocks.OAK_LOG)
                        || bS.isOf(Blocks.JUNGLE_LOG) || bS.isOf(Blocks.DARK_OAK_LOG))
                    bS = Blocks.ACACIA_LOG.getStateWithProperties(bS);
                else if (bS.isOf(Blocks.STRIPPED_SPRUCE_LOG) || bS.isOf(Blocks.STRIPPED_ACACIA_LOG)
                        || bS.isOf(Blocks.STRIPPED_BIRCH_LOG) || bS.isOf(Blocks.STRIPPED_OAK_LOG)
                        || bS.isOf(Blocks.STRIPPED_JUNGLE_LOG) || bS.isOf(Blocks.STRIPPED_DARK_OAK_LOG))
                    bS = Blocks.STRIPPED_ACACIA_LOG.getStateWithProperties(bS);
                else if (bS.isOf(Blocks.SPRUCE_WOOD) || bS.isOf(Blocks.ACACIA_WOOD)
                        || bS.isOf(Blocks.BIRCH_WOOD) || bS.isOf(Blocks.OAK_WOOD)
                        || bS.isOf(Blocks.JUNGLE_WOOD) || bS.isOf(Blocks.DARK_OAK_WOOD))
                    bS = Blocks.ACACIA_WOOD.getStateWithProperties(bS);
                else if (bS.isOf(Blocks.STRIPPED_SPRUCE_WOOD) || bS.isOf(Blocks.STRIPPED_ACACIA_WOOD)
                        || bS.isOf(Blocks.STRIPPED_BIRCH_WOOD) || bS.isOf(Blocks.STRIPPED_OAK_WOOD)
                        || bS.isOf(Blocks.STRIPPED_JUNGLE_WOOD) || bS.isOf(Blocks.STRIPPED_DARK_OAK_WOOD))
                    bS = Blocks.STRIPPED_ACACIA_WOOD.getStateWithProperties(bS);
            }
        }
        else if (world.getBiome(blockPos).getCategory() == Biome.Category.NETHER) {
            if (bS.isIn(BlockTags.WOODEN_FENCES)) bS = Blocks.CRIMSON_FENCE.getStateWithProperties(bS);
            else if (bS.isIn(BlockTags.WOODEN_STAIRS)) bS = Blocks.CRIMSON_STAIRS.getStateWithProperties(bS);
            else if (bS.isIn(BlockTags.WOODEN_DOORS)) bS = Blocks.CRIMSON_DOOR.getStateWithProperties(bS);
            else if (bS.isIn(BlockTags.WOODEN_BUTTONS)) bS = Blocks.CRIMSON_BUTTON.getStateWithProperties(bS);
            else if (bS.isIn(BlockTags.WOODEN_SLABS)) bS = Blocks.CRIMSON_SLAB.getStateWithProperties(bS);
            else if (bS.isIn(BlockTags.WOODEN_PRESSURE_PLATES)) bS = Blocks.CRIMSON_PRESSURE_PLATE.getStateWithProperties(bS);
            else if (bS.isIn(BlockTags.WOODEN_TRAPDOORS)) bS = Blocks.CRIMSON_TRAPDOOR.getStateWithProperties(bS);
            else if (bS.isIn(BlockTags.PLANKS)) bS = Blocks.CRIMSON_PLANKS.getStateWithProperties(bS);
            else if (bS.isIn(BlockTags.LEAVES)) bS = Blocks.CRIMSON_HYPHAE.getStateWithProperties(bS);
            else if (bS.isIn(BlockTags.FENCE_GATES)) bS = Blocks.CRIMSON_FENCE_GATE.getStateWithProperties(bS);
            else if (bS.isIn(BlockTags.STANDING_SIGNS)) bS = Blocks.CRIMSON_SIGN.getStateWithProperties(bS);
            else if (bS.isIn(BlockTags.WALL_SIGNS)) bS = Blocks.CRIMSON_WALL_SIGN.getStateWithProperties(bS);
            else if (bS.isIn(BlockTags.SAPLINGS)) bS = Blocks.CRIMSON_FUNGUS.getStateWithProperties(bS);
            else if (bS.isOf(Blocks.SPRUCE_LOG) || bS.isOf(Blocks.ACACIA_LOG)
                    || bS.isOf(Blocks.BIRCH_LOG) || bS.isOf(Blocks.OAK_LOG)
                    || bS.isOf(Blocks.JUNGLE_LOG) || bS.isOf(Blocks.DARK_OAK_LOG))
                bS = Blocks.CRIMSON_STEM.getStateWithProperties(bS);
            else if (bS.isOf(Blocks.STRIPPED_SPRUCE_LOG) || bS.isOf(Blocks.STRIPPED_ACACIA_LOG)
                    || bS.isOf(Blocks.STRIPPED_BIRCH_LOG) || bS.isOf(Blocks.STRIPPED_OAK_LOG)
                    || bS.isOf(Blocks.STRIPPED_JUNGLE_LOG) || bS.isOf(Blocks.STRIPPED_DARK_OAK_LOG))
                bS = Blocks.STRIPPED_CRIMSON_STEM.getStateWithProperties(bS);
        }


        

        // Return new blockstate
        return new Structure.StructureBlockInfo(blockPos, bS, structureBlockInfo2.nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TYPE;
    }
}
