package com.y271727uy.moderndelight.block.food.pizza;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class PizzaBlockEntity extends AbstractPizzaBlockEntity {
    public PizzaBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PIZZA_BLOCK_ENTITY.get(), pos, state);
    }

    public int getHunger() {
        int result = 0;
        for (int i = 0; i < getContainerSize(); i++) {
            ItemStack stack = getItem(i);
            if (!stack.isEmpty() && stack.isEdible()) {
                result += Objects.requireNonNull(stack.getItem().getFoodProperties()).getNutrition();
            }
        }
        return result / 3;
    }
}
