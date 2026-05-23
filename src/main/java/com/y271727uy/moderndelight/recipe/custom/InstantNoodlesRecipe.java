package com.y271727uy.moderndelight.recipe.custom;

import com.y271727uy.moderndelight.item.ModItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class InstantNoodlesRecipe extends CustomRecipe {
    public InstantNoodlesRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level world) {
        boolean hasPackage = false;
        boolean hasFriedNoodles = false;
        
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(ModItems.MULTIFUNCTIONAL_WRAPPING_PAPER.get())) {
                    hasPackage = true;
                } else if (stack.is(ModItems.FRIED_NOODLES.get())) {
                    hasFriedNoodles = true;
                }
            }
        }
        
        return hasPackage && hasFriedNoodles;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        ItemStack result = new ItemStack(ModItems.PACKAGED_INSTANT_NOODLES.get());
        CompoundTag nbt = result.getOrCreateTagElement("instant_noodles_ingredients");
        int counter = 1;
        boolean hasPackage = false;
        boolean hasFriedNoodles = false;
        
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(ModItems.MULTIFUNCTIONAL_WRAPPING_PAPER.get()) && !hasPackage) {
                    hasPackage = true;
                } else if (stack.is(ModItems.FRIED_NOODLES.get()) && !hasFriedNoodles) {
                    hasFriedNoodles = true;
                } else {
                    String itemName = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();
                    nbt.putString("ingredients_" + counter, itemName);
                    counter++;
                }
            }
        }
        
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<InstantNoodlesRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "crafting_special_packaged_instant_noodles";

        @Override
        public InstantNoodlesRecipe fromJson(ResourceLocation id, com.google.gson.JsonObject json) {
            return new InstantNoodlesRecipe(id, CraftingBookCategory.MISC);
        }

        @Override
        public InstantNoodlesRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new InstantNoodlesRecipe(id, CraftingBookCategory.MISC);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, InstantNoodlesRecipe recipe) {
        }
    }
}
