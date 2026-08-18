package com.y271727uy.moderndelight.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

import java.util.Arrays;
import java.util.List;

final class StandardMenuRecipeHandler<T extends AbstractContainerMenu> implements StandardRecipeHandler<T> {
    private final EmiRecipeCategory category;
    private final int[] craftingSlots;
    private final int outputSlot;

    StandardMenuRecipeHandler(EmiRecipeCategory category, int outputSlot, int... craftingSlots) {
        this.category = category;
        this.craftingSlots = craftingSlots;
        this.outputSlot = outputSlot;
    }

    @Override
    public List<Slot> getInputSources(T menu) {
        return menu.slots.subList(menu.slots.size() - 36, menu.slots.size());
    }

    @Override
    public List<Slot> getCraftingSlots(T menu) {
        return Arrays.stream(craftingSlots).mapToObj(menu.slots::get).toList();
    }

    @Override
    public Slot getOutputSlot(T menu) {
        return menu.slots.get(outputSlot);
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory().equals(category);
    }
}
