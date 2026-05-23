package com.y271727uy.moderndelight.block.food.pizza;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class RawPizzaBlockEntity extends AbstractPizzaBlockEntity {
    public RawPizzaBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RAW_PIZZA_BLOCK_ENTITY.get(), pos, state);
    }
}
