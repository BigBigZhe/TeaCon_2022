package com.liu.mod.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class CombustibleRedBlock extends Block {
    public CombustibleRedBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE));
    }
}
