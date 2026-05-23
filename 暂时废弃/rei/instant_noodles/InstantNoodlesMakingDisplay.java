package com.y271727uy.moderndelight.compat.rei.instant_noodles;

import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.util.enums.SpecialIngredient;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InstantNoodlesMakingDisplay implements Display {
    @Override
    public List<EntryIngredient> getInputEntries() {
        List<EntryIngredient> stacks = new ArrayList<>();
        stacks.add(EntryIngredients.of(ModItems.MULTIFUNCTIONAL_WRAPPING_PAPER));
        stacks.add(EntryIngredients.of(ModItems.FRIED_NOODLES));
        stacks.add(EntryIngredients.of(ModItems.QUICKLIME));
        stacks.add(EntryIngredients.of(Items.WATER_BUCKET));
        stacks.add(EntryIngredients.of(ModItems.PORTABLE_POT));
        for (SpecialIngredient specialIngredient : SpecialIngredient.values()) {
            for (Ingredient ingredient : specialIngredient.getIngredients()) {
                stacks.add(EntryIngredients.ofItemStacks(Arrays.asList(ingredient.getItems())));
            }
        }
        return stacks;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(
                EntryIngredients.of(ModItems.PACKAGED_INSTANT_NOODLES),
                EntryIngredients.of(ModItems.COOKED_PORTABLE_POT)
        );
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return InstantNoodlesMakingCategory.INSTANT_NOODLES_MAKING;
    }

}
