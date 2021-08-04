package com.worldanchor.structures.features;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.worldanchor.structures.Utility;
import com.worldanchor.structures.processors.BiomeStructureProcessor;
import com.worldanchor.structures.processors.RandomDeleteStructureProcessor;
import com.worldanchor.structures.processors.ReplaceBlocksStructureProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static com.worldanchor.structures.Server.MODID;
import static com.worldanchor.structures.Utility.registerStructure;

public class GiantBeehiveFeature extends Utility.ModStructureFeature {
    public static ResourceLocation ID = new ResourceLocation(MODID + ":giant-beehive");

    public static final StructureProcessorList PROCESSOR_LIST = BuiltinRegistries.register(
            BuiltinRegistries.PROCESSOR_LIST, ID + "-processor-list", new StructureProcessorList(
                    Arrays.asList(
                            new RandomDeleteStructureProcessor(0.05f, false, Arrays.asList(
                                    Blocks.OAK_PLANKS.defaultBlockState()
                            )),
                            new ReplaceBlocksStructureProcessor(Arrays.asList(Blocks.YELLOW_CONCRETE.defaultBlockState()),
                                    Arrays.asList(new Pair<>("7", Blocks.HONEY_BLOCK.defaultBlockState()),
                                            new Pair<>("20", Blocks.HONEYCOMB_BLOCK.defaultBlockState()),
                                            new Pair<>("2", Blocks.RAW_GOLD_BLOCK.defaultBlockState()),
                                            new Pair<>("1", Blocks.GLOWSTONE.defaultBlockState())
                                    )
                            ),
                            new BiomeStructureProcessor(true)
                    )
            )
    );
    public static StructureTemplatePool STRUCTURE_POOLS = Pools.register(new StructureTemplatePool(
            ID, new ResourceLocation("empty"),
            ImmutableList.of(
                    // Use ofProcessedSingle to add processors or just ofSingle to add elements without processors
                    Pair.of(StructurePoolElement.single(ID.toString(), PROCESSOR_LIST), 1)
            ),
            StructureTemplatePool.Projection.RIGID
    ));
    public static final StructureFeature<JigsawConfiguration> DEFAULT =
            new GiantBeehiveFeature(JigsawConfiguration.CODEC);
    static {
        Pools.register(new StructureTemplatePool(
                new ResourceLocation(MODID + ":entity/bee"), new ResourceLocation("empty"),
                ImmutableList.of(
                        // Use ofProcessedSingle to add processors or just ofSingle to add elements without processors
                        Pair.of(StructurePoolElement.single(MODID + ":entity/bee"), 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));
        registerStructure(ID, DEFAULT, GenerationStep.Decoration.STRONGHOLDS,
                64,60,634523774, false);
        Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ID,
                DEFAULT.configured(new JigsawConfiguration(() -> STRUCTURE_POOLS, STRUCTURE_POOLS.size())));
    }


    public GiantBeehiveFeature(Codec<JigsawConfiguration> codec) {
        super(codec, ID);
    }

    @Override
    public @Nullable Utility.PlacementData shouldStartAt(ChunkGenerator generator, ChunkPos pos,
            WorldgenRandom random, LevelHeightAccessor world, Rotation rotation) {
        BlockPos structurePos = TestStructureMask(generator, world,
                generator.getBaseHeight(pos.getMinBlockX(), pos.getMinBlockZ(), Heightmap.Types.WORLD_SURFACE_WG, world) - 30,
                generator.getBaseHeight(pos.getMinBlockX(), pos.getMinBlockZ(), Heightmap.Types.WORLD_SURFACE_WG, world) - 10, 1, rotation, pos);
        if (structurePos == null) return null;
        else return new Utility.PlacementData(structurePos, rotation);
    }

}


