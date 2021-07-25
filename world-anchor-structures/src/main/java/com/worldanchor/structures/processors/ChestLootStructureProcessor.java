package com.worldanchor.structures.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtList;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
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
    public Structure.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot,
            Structure.StructureBlockInfo structureBlockInfo, Structure.StructureBlockInfo structureBlockInfo2,
            StructurePlacementData data) {
        BlockState bS = structureBlockInfo2.state;
        if (bS.isOf(Blocks.CHEST) || bS.isOf(Blocks.TRAPPED_CHEST)) {
            if (!structureBlockInfo.nbt.contains("LootTable")
                    && Objects.equals(structureBlockInfo2.nbt.get("Items"), new NbtList()))
                structureBlockInfo2.nbt.putString("LootTable", MODID + ":chests/" + structurePath);
        }
        return new Structure.StructureBlockInfo(structureBlockInfo2.pos, bS, structureBlockInfo2.nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TYPE;
    }
}
