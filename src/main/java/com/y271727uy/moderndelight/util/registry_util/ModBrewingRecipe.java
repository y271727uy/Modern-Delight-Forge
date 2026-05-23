package com.y271727uy.moderndelight.util.registry_util;

import com.y271727uy.moderndelight.effects.ModEffectsAndPotions;
import com.y271727uy.moderndelight.item.ModItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class ModBrewingRecipe {
    public static void registerModBrewingRecipe(){
        // Register custom brewing recipes using IBrewingRecipe
        BrewingRecipeRegistry.addRecipe(new IBrewingRecipe() {
            @Override
            public boolean isInput(ItemStack input) {
                return PotionUtils.getPotion(input) == Potions.STRONG_SLOWNESS;
            }

            @Override
            public boolean isIngredient(ItemStack ingredient) {
                return ingredient.is(ModItems.BUTTER.get());
            }

            @Override
            public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
                if (isInput(input) && isIngredient(ingredient)) {
                    return PotionUtils.setPotion(new ItemStack(Items.POTION), ModEffectsAndPotions.STICKY_POTION.get());
                }
                return ItemStack.EMPTY;
            }
        });
        
        // Add more recipes similarly...
        // Note: Forge brewing API is different from Fabric, you may need to implement each recipe individually
    }
}
