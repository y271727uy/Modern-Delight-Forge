package com.y271727uy.moderndelight.compat.jei.category;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.recipe.custom.SteamingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

public class SteamingRecipeCategory implements IRecipeCategory<SteamingRecipe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/steaming.png");
    public static final RecipeType<SteamingRecipe> TYPE = RecipeType.create(ModernDelightMain.MOD_ID, "steaming", SteamingRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public SteamingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 95);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BAMBOO_GRATE.get()));
    }

    @Override public RecipeType<SteamingRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return Component.translatable("emi.category.moderndelight.steaming"); }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SteamingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 53, 16)
                .addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 24, 7)
                .addIngredients(Ingredient.of(ModBlocks.BAMBOO_COVER.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 24, 25)
                .addIngredients(Ingredient.of(ModBlocks.BAMBOO_GRATE.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 24, 56)
                .addIngredients(Ingredient.of(Blocks.CAULDRON));
        builder.addSlot(RecipeIngredientRole.INPUT, 24, 74)
                .addIngredients(Ingredient.of(ModBlocks.GAS_COOKING_STOVE.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 42, 74)
                .addIngredients(Ingredient.of(ModBlocks.GAS_CANISTER.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 56)
                .addFluidStack(Fluids.WATER, 81000);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 107, 16)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(SteamingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 5, 5, 4, 4, 142, 87);
    }
}
