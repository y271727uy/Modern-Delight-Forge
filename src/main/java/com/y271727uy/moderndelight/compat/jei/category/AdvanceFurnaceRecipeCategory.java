package com.y271727uy.moderndelight.compat.jei.category;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
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

import java.util.List;

public class AdvanceFurnaceRecipeCategory implements IRecipeCategory<AdvanceFurnaceRecipeCategory.AdvanceFurnaceRecipe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/transform.png");
    public static final RecipeType<AdvanceFurnaceRecipe> TYPE = RecipeType.create(ModernDelightMain.MOD_ID, "advance_furnace_transforming", AdvanceFurnaceRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public static List<AdvanceFurnaceRecipe> getRecipes() {
        return List.of(new AdvanceFurnaceRecipe());
    }

    public record AdvanceFurnaceRecipe() {}

    public AdvanceFurnaceRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 52);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BAKING_TRAY.get()));
    }

    @Override public RecipeType<AdvanceFurnaceRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return Component.translatable("emi.category.moderndelight.advance_furnace_transforming"); }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AdvanceFurnaceRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 23, 19)
                .addIngredients(Ingredient.of(ModBlocks.ADVANCE_FURNACE.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 56, 7)
                .addIngredients(Ingredient.of(ModBlocks.BAKING_TRAY.get()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 102, 19)
                .addItemStack(new ItemStack(ModBlocks.OVEN.get()));
    }

    @Override
    public void draw(AdvanceFurnaceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 5, 5, 4, 4, 142, 44);
    }
}
