package com.worldanchor.structures.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.worldanchor.structures.Server;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
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
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos pivot,
            StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo2,
            StructurePlaceSettings data) {
        Random random = data.getRandom(structureBlockInfo2.pos);
        BlockState bS = structureBlockInfo2.state;
        BlockPos blockPos = structureBlockInfo2.pos;

        if (!bS.isAir() && !bS.is(Blocks.STRUCTURE_VOID)) {
            if (world.getBiome(blockPos).getBiomeCategory() == Biome.BiomeCategory.TAIGA) {
                if (bS.is(BlockTags.WOODEN_FENCES)) bS = Blocks.SPRUCE_FENCE.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_STAIRS)) bS = Blocks.SPRUCE_STAIRS.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_DOORS)) bS = Blocks.SPRUCE_DOOR.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_BUTTONS)) bS = Blocks.SPRUCE_BUTTON.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_SLABS)) bS = Blocks.SPRUCE_SLAB.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_PRESSURE_PLATES)) bS = Blocks.SPRUCE_PRESSURE_PLATE.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_TRAPDOORS)) bS = Blocks.SPRUCE_TRAPDOOR.withPropertiesOf(bS);
                else if (bS.is(BlockTags.PLANKS)) bS = Blocks.SPRUCE_PLANKS.withPropertiesOf(bS);
                else if (bS.is(BlockTags.LEAVES)) bS = Blocks.SPRUCE_LEAVES.withPropertiesOf(bS);
                else if (bS.is(BlockTags.FENCE_GATES)) bS = Blocks.SPRUCE_FENCE_GATE.withPropertiesOf(bS);
                else if (bS.is(BlockTags.STANDING_SIGNS)) bS = Blocks.SPRUCE_SIGN.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WALL_SIGNS)) bS = Blocks.SPRUCE_WALL_SIGN.withPropertiesOf(bS);
                else if (bS.is(BlockTags.SAPLINGS)) bS = Blocks.SPRUCE_SAPLING.withPropertiesOf(bS);
                else if (bS.is(Blocks.SPRUCE_LOG) || bS.is(Blocks.ACACIA_LOG)
                        || bS.is(Blocks.BIRCH_LOG) || bS.is(Blocks.OAK_LOG)
                        || bS.is(Blocks.JUNGLE_LOG) || bS.is(Blocks.DARK_OAK_LOG))
                    bS = Blocks.SPRUCE_LOG.withPropertiesOf(bS);
                else if (bS.is(Blocks.STRIPPED_SPRUCE_LOG) || bS.is(Blocks.STRIPPED_ACACIA_LOG)
                        || bS.is(Blocks.STRIPPED_BIRCH_LOG) || bS.is(Blocks.STRIPPED_OAK_LOG)
                        || bS.is(Blocks.STRIPPED_JUNGLE_LOG) || bS.is(Blocks.STRIPPED_DARK_OAK_LOG))
                    bS = Blocks.STRIPPED_SPRUCE_LOG.withPropertiesOf(bS);
                else if (bS.is(Blocks.SPRUCE_WOOD) || bS.is(Blocks.ACACIA_WOOD)
                        || bS.is(Blocks.BIRCH_WOOD) || bS.is(Blocks.OAK_WOOD)
                        || bS.is(Blocks.JUNGLE_WOOD) || bS.is(Blocks.DARK_OAK_WOOD))
                    bS = Blocks.SPRUCE_WOOD.withPropertiesOf(bS);
                else if (bS.is(Blocks.STRIPPED_SPRUCE_WOOD) || bS.is(Blocks.STRIPPED_ACACIA_WOOD)
                        || bS.is(Blocks.STRIPPED_BIRCH_WOOD) || bS.is(Blocks.STRIPPED_OAK_WOOD)
                        || bS.is(Blocks.STRIPPED_JUNGLE_WOOD) || bS.is(Blocks.STRIPPED_DARK_OAK_WOOD))
                    bS = Blocks.STRIPPED_SPRUCE_WOOD.withPropertiesOf(bS);
            }
            else if (world.getBiome(blockPos).getBiomeCategory() == Biome.BiomeCategory.JUNGLE) {
                if (bS.is(BlockTags.WOODEN_FENCES)) bS = Blocks.JUNGLE_FENCE.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_STAIRS)) bS = Blocks.JUNGLE_STAIRS.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_DOORS)) bS = Blocks.JUNGLE_DOOR.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_BUTTONS)) bS = Blocks.JUNGLE_BUTTON.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_SLABS)) bS = Blocks.JUNGLE_SLAB.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_PRESSURE_PLATES)) bS = Blocks.JUNGLE_PRESSURE_PLATE.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_TRAPDOORS)) bS = Blocks.JUNGLE_TRAPDOOR.withPropertiesOf(bS);
                else if (bS.is(BlockTags.PLANKS)) bS = Blocks.JUNGLE_PLANKS.withPropertiesOf(bS);
                else if (bS.is(BlockTags.LEAVES)) bS = Blocks.JUNGLE_LEAVES.withPropertiesOf(bS);
                else if (bS.is(BlockTags.FENCE_GATES)) bS = Blocks.JUNGLE_FENCE_GATE.withPropertiesOf(bS);
                else if (bS.is(BlockTags.STANDING_SIGNS)) bS = Blocks.JUNGLE_SIGN.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WALL_SIGNS)) bS = Blocks.JUNGLE_WALL_SIGN.withPropertiesOf(bS);
                else if (bS.is(BlockTags.SAPLINGS)) bS = Blocks.JUNGLE_SAPLING.withPropertiesOf(bS);
                else if (bS.is(Blocks.SPRUCE_LOG) || bS.is(Blocks.ACACIA_LOG)
                        || bS.is(Blocks.BIRCH_LOG) || bS.is(Blocks.OAK_LOG)
                        || bS.is(Blocks.JUNGLE_LOG) || bS.is(Blocks.DARK_OAK_LOG))
                    bS = Blocks.JUNGLE_LOG.withPropertiesOf(bS);
                else if (bS.is(Blocks.STRIPPED_SPRUCE_LOG) || bS.is(Blocks.STRIPPED_ACACIA_LOG)
                        || bS.is(Blocks.STRIPPED_BIRCH_LOG) || bS.is(Blocks.STRIPPED_OAK_LOG)
                        || bS.is(Blocks.STRIPPED_JUNGLE_LOG) || bS.is(Blocks.STRIPPED_DARK_OAK_LOG))
                    bS = Blocks.STRIPPED_JUNGLE_LOG.withPropertiesOf(bS);
                else if (bS.is(Blocks.SPRUCE_WOOD) || bS.is(Blocks.ACACIA_WOOD)
                        || bS.is(Blocks.BIRCH_WOOD) || bS.is(Blocks.OAK_WOOD)
                        || bS.is(Blocks.JUNGLE_WOOD) || bS.is(Blocks.DARK_OAK_WOOD))
                    bS = Blocks.JUNGLE_WOOD.withPropertiesOf(bS);
                else if (bS.is(Blocks.STRIPPED_SPRUCE_WOOD) || bS.is(Blocks.STRIPPED_ACACIA_WOOD)
                        || bS.is(Blocks.STRIPPED_BIRCH_WOOD) || bS.is(Blocks.STRIPPED_OAK_WOOD)
                        || bS.is(Blocks.STRIPPED_JUNGLE_WOOD) || bS.is(Blocks.STRIPPED_DARK_OAK_WOOD))
                    bS = Blocks.STRIPPED_JUNGLE_WOOD.withPropertiesOf(bS);
            }
            else if (world.getBiome(blockPos).getBiomeCategory() == Biome.BiomeCategory.SAVANNA) {
                if (bS.is(BlockTags.WOODEN_FENCES)) bS = Blocks.ACACIA_FENCE.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_STAIRS)) bS = Blocks.ACACIA_STAIRS.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_DOORS)) bS = Blocks.ACACIA_DOOR.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_BUTTONS)) bS = Blocks.ACACIA_BUTTON.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_SLABS)) bS = Blocks.ACACIA_SLAB.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_PRESSURE_PLATES)) bS = Blocks.ACACIA_PRESSURE_PLATE.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WOODEN_TRAPDOORS)) bS = Blocks.ACACIA_TRAPDOOR.withPropertiesOf(bS);
                else if (bS.is(BlockTags.PLANKS)) bS = Blocks.ACACIA_PLANKS.withPropertiesOf(bS);
                else if (bS.is(BlockTags.LEAVES)) bS = Blocks.ACACIA_LEAVES.withPropertiesOf(bS);
                else if (bS.is(BlockTags.FENCE_GATES)) bS = Blocks.ACACIA_FENCE_GATE.withPropertiesOf(bS);
                else if (bS.is(BlockTags.STANDING_SIGNS)) bS = Blocks.ACACIA_SIGN.withPropertiesOf(bS);
                else if (bS.is(BlockTags.WALL_SIGNS)) bS = Blocks.ACACIA_WALL_SIGN.withPropertiesOf(bS);
                else if (bS.is(BlockTags.SAPLINGS)) bS = Blocks.ACACIA_SAPLING.withPropertiesOf(bS);
                else if (bS.is(Blocks.SPRUCE_LOG) || bS.is(Blocks.ACACIA_LOG)
                        || bS.is(Blocks.BIRCH_LOG) || bS.is(Blocks.OAK_LOG)
                        || bS.is(Blocks.JUNGLE_LOG) || bS.is(Blocks.DARK_OAK_LOG))
                    bS = Blocks.ACACIA_LOG.withPropertiesOf(bS);
                else if (bS.is(Blocks.STRIPPED_SPRUCE_LOG) || bS.is(Blocks.STRIPPED_ACACIA_LOG)
                        || bS.is(Blocks.STRIPPED_BIRCH_LOG) || bS.is(Blocks.STRIPPED_OAK_LOG)
                        || bS.is(Blocks.STRIPPED_JUNGLE_LOG) || bS.is(Blocks.STRIPPED_DARK_OAK_LOG))
                    bS = Blocks.STRIPPED_ACACIA_LOG.withPropertiesOf(bS);
                else if (bS.is(Blocks.SPRUCE_WOOD) || bS.is(Blocks.ACACIA_WOOD)
                        || bS.is(Blocks.BIRCH_WOOD) || bS.is(Blocks.OAK_WOOD)
                        || bS.is(Blocks.JUNGLE_WOOD) || bS.is(Blocks.DARK_OAK_WOOD))
                    bS = Blocks.ACACIA_WOOD.withPropertiesOf(bS);
                else if (bS.is(Blocks.STRIPPED_SPRUCE_WOOD) || bS.is(Blocks.STRIPPED_ACACIA_WOOD)
                        || bS.is(Blocks.STRIPPED_BIRCH_WOOD) || bS.is(Blocks.STRIPPED_OAK_WOOD)
                        || bS.is(Blocks.STRIPPED_JUNGLE_WOOD) || bS.is(Blocks.STRIPPED_DARK_OAK_WOOD))
                    bS = Blocks.STRIPPED_ACACIA_WOOD.withPropertiesOf(bS);
            }
        }
        else if (world.getBiome(blockPos).getBiomeCategory() == Biome.BiomeCategory.NETHER) {
            if (bS.is(BlockTags.WOODEN_FENCES)) bS = Blocks.CRIMSON_FENCE.withPropertiesOf(bS);
            else if (bS.is(BlockTags.WOODEN_STAIRS)) bS = Blocks.CRIMSON_STAIRS.withPropertiesOf(bS);
            else if (bS.is(BlockTags.WOODEN_DOORS)) bS = Blocks.CRIMSON_DOOR.withPropertiesOf(bS);
            else if (bS.is(BlockTags.WOODEN_BUTTONS)) bS = Blocks.CRIMSON_BUTTON.withPropertiesOf(bS);
            else if (bS.is(BlockTags.WOODEN_SLABS)) bS = Blocks.CRIMSON_SLAB.withPropertiesOf(bS);
            else if (bS.is(BlockTags.WOODEN_PRESSURE_PLATES)) bS = Blocks.CRIMSON_PRESSURE_PLATE.withPropertiesOf(bS);
            else if (bS.is(BlockTags.WOODEN_TRAPDOORS)) bS = Blocks.CRIMSON_TRAPDOOR.withPropertiesOf(bS);
            else if (bS.is(BlockTags.PLANKS)) bS = Blocks.CRIMSON_PLANKS.withPropertiesOf(bS);
            else if (bS.is(BlockTags.LEAVES)) bS = Blocks.CRIMSON_HYPHAE.withPropertiesOf(bS);
            else if (bS.is(BlockTags.FENCE_GATES)) bS = Blocks.CRIMSON_FENCE_GATE.withPropertiesOf(bS);
            else if (bS.is(BlockTags.STANDING_SIGNS)) bS = Blocks.CRIMSON_SIGN.withPropertiesOf(bS);
            else if (bS.is(BlockTags.WALL_SIGNS)) bS = Blocks.CRIMSON_WALL_SIGN.withPropertiesOf(bS);
            else if (bS.is(BlockTags.SAPLINGS)) bS = Blocks.CRIMSON_FUNGUS.withPropertiesOf(bS);
            else if (bS.is(Blocks.SPRUCE_LOG) || bS.is(Blocks.ACACIA_LOG)
                    || bS.is(Blocks.BIRCH_LOG) || bS.is(Blocks.OAK_LOG)
                    || bS.is(Blocks.JUNGLE_LOG) || bS.is(Blocks.DARK_OAK_LOG))
                bS = Blocks.CRIMSON_STEM.withPropertiesOf(bS);
            else if (bS.is(Blocks.STRIPPED_SPRUCE_LOG) || bS.is(Blocks.STRIPPED_ACACIA_LOG)
                    || bS.is(Blocks.STRIPPED_BIRCH_LOG) || bS.is(Blocks.STRIPPED_OAK_LOG)
                    || bS.is(Blocks.STRIPPED_JUNGLE_LOG) || bS.is(Blocks.STRIPPED_DARK_OAK_LOG))
                bS = Blocks.STRIPPED_CRIMSON_STEM.withPropertiesOf(bS);
        }




        // Return new blockstate
        return new StructureTemplate.StructureBlockInfo(blockPos, bS, structureBlockInfo2.nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TYPE;
    }
}
