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

import java.util.Random;

public class OresStructureProcessor extends StructureProcessor {

    public static final Codec<OresStructureProcessor> CODEC = RecordCodecBuilder.create((proc) -> proc.group(
            Codec.BOOL.fieldOf("blendStoneToDeepslate").forGetter(processor -> processor.blendStoneToDeepslate),
            Codec.FLOAT.fieldOf("oreChance").forGetter(processor -> processor.oreChance),
            Codec.INT.fieldOf("coalWeight").forGetter(processor -> processor.coalWeight),
            Codec.INT.fieldOf("ironWeight").forGetter(processor -> processor.ironWeight),
            Codec.INT.fieldOf("goldWeight").forGetter(processor -> processor.goldWeight),
            Codec.INT.fieldOf("copperWeight").forGetter(processor -> processor.copperWeight),
            Codec.INT.fieldOf("redstoneWeight").forGetter(processor -> processor.redstoneWeight),
            Codec.INT.fieldOf("lapisWeight").forGetter(processor -> processor.lapisWeight),
            Codec.INT.fieldOf("emeraldWeight").forGetter(processor -> processor.emeraldWeight),
            Codec.INT.fieldOf("diamondWeight").forGetter(processor -> processor.diamondWeight),
            Codec.INT.fieldOf("quartzWeight").forGetter(processor -> processor.quartzWeight)
    ).apply(proc, OresStructureProcessor::new));

    public static StructureProcessorType<OresStructureProcessor> TYPE = StructureProcessorType
            .register(Server.MODID + ":ores-structure-processor", CODEC);


    private final boolean blendStoneToDeepslate;
    private final float oreChance;
    private final int coalWeight;
    private final int goldWeight;
    private final int ironWeight;
    private final int copperWeight;
    private final int redstoneWeight;
    private final int lapisWeight;
    private final int emeraldWeight;
    private final int diamondWeight;
    private final int quartzWeight;
    private final int stoneTotal;
    private final int netherrackTotal;


    public OresStructureProcessor(boolean blendStoneToDeepslate,float oreChance, int coalWeight, int ironWeight, int goldWeight, int copperWeight,
            int redstoneWeight, int lapisWeight, int emeraldWeight, int diamondWeight, int quartzWeight) {
        this.blendStoneToDeepslate = blendStoneToDeepslate;
        this.oreChance = oreChance;
        this.coalWeight = coalWeight;
        this.ironWeight = ironWeight;
        this.goldWeight = goldWeight;
        this.copperWeight = copperWeight;
        this.redstoneWeight = redstoneWeight;
        this.lapisWeight = lapisWeight;
        this.emeraldWeight = emeraldWeight;
        this.diamondWeight = diamondWeight;
        this.quartzWeight = quartzWeight;
        stoneTotal = coalWeight + ironWeight + goldWeight + copperWeight + redstoneWeight + lapisWeight + emeraldWeight + diamondWeight;
        netherrackTotal = goldWeight + quartzWeight;
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

        if (blendStoneToDeepslate &&  blockState.is(Blocks.STONE) && structureBlockInfo2.pos.getY() < -random.nextInt(8))
            blockState = Blocks.DEEPSLATE.defaultBlockState();

        if (oreChance > 0 && random.nextFloat() <= oreChance) {
            if (blockState.is(Blocks.STONE)) {
                int num = random.nextInt(stoneTotal);
                if ((num = num - coalWeight) < 0) blockState = Blocks.COAL_ORE.defaultBlockState();
                else if ((num = num - ironWeight) < 0) blockState = Blocks.IRON_ORE.defaultBlockState();
                else if ((num = num - goldWeight) < 0) blockState = Blocks.GOLD_ORE.defaultBlockState();
                else if ((num = num - copperWeight) < 0) blockState = Blocks.COPPER_ORE.defaultBlockState();
                else if ((num = num - redstoneWeight) < 0) blockState = Blocks.REDSTONE_ORE.defaultBlockState();
                else if ((num = num - lapisWeight) < 0) blockState = Blocks.LAPIS_ORE.defaultBlockState();
                else if ((num = num - emeraldWeight) < 0) blockState = Blocks.EMERALD_ORE.defaultBlockState();
                else if ((num - diamondWeight) < 0) blockState = Blocks.DIAMOND_ORE.defaultBlockState();
            }
            else if (blockState.is(Blocks.DEEPSLATE)) {
                int num = random.nextInt(stoneTotal);
                if ((num = num - coalWeight) < 0) blockState = Blocks.DEEPSLATE_COAL_ORE.defaultBlockState();
                else if ((num = num - ironWeight) < 0) blockState = Blocks.DEEPSLATE_IRON_ORE.defaultBlockState();
                else if ((num = num - goldWeight) < 0) blockState = Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState();
                else if ((num = num - copperWeight) < 0) blockState = Blocks.DEEPSLATE_COPPER_ORE.defaultBlockState();
                else if ((num = num - redstoneWeight) < 0) blockState = Blocks.DEEPSLATE_REDSTONE_ORE.defaultBlockState();
                else if ((num = num - lapisWeight) < 0) blockState = Blocks.DEEPSLATE_LAPIS_ORE.defaultBlockState();
                else if ((num = num - emeraldWeight) < 0) blockState = Blocks.DEEPSLATE_EMERALD_ORE.defaultBlockState();
                else if ((num - diamondWeight) < 0) blockState = Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState();
            }
            else if (blockState.is(Blocks.NETHERRACK)) {
                int num = random.nextInt(netherrackTotal);
                if ((num = num - goldWeight) < 0) blockState = Blocks.NETHER_GOLD_ORE.defaultBlockState();
                else if ((num - quartzWeight) < 0) blockState = Blocks.NETHER_QUARTZ_ORE.defaultBlockState();
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
