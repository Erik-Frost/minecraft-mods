package com.worldanchor.structures.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.worldanchor.structures.Server.MODID;

public class ChestLootStructureProcessor extends StructureProcessor {

    public static final Codec<ChestLootStructureProcessor> CODEC = RecordCodecBuilder.create((proc) -> proc.group(
            Codec.STRING.fieldOf("structurePath").forGetter(processor -> processor.structurePath)
    ).apply(proc, ChestLootStructureProcessor::new));

    public static StructureProcessorType<ChestLootStructureProcessor> TYPE = StructureProcessorType
            .register(MODID + ":chest-loot-structure-processor", CODEC);

    private final String structurePath;

    public ChestLootStructureProcessor(String structurePath) {
        this.structurePath = structurePath;
    }

    // This method is called for each block in the structure
    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos pivot,
            StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo2,
            StructurePlaceSettings data) {
        BlockState bS = structureBlockInfo2.state;
        if (bS.is(Blocks.CHEST) || bS.is(Blocks.TRAPPED_CHEST)) {
            if (!structureBlockInfo.nbt.contains("LootTable")
                    && Objects.equals(structureBlockInfo2.nbt.get("Items"), new ListTag()))
                structureBlockInfo2.nbt.putString("LootTable", MODID + ":chests/" + structurePath);
        }
        return new StructureTemplate.StructureBlockInfo(structureBlockInfo2.pos, bS, structureBlockInfo2.nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TYPE;
    }
}
