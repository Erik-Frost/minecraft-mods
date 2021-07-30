package com.worldanchor.structures.mixin;

import net.minecraft.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Structure.class)
public interface StructureMixin {
    @Accessor
    List<Structure.PalettedBlockInfoList> getBlockInfoLists();

}
