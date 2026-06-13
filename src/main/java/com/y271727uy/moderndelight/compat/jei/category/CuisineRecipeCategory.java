package com.y271727uy.moderndelight.compat.jei.category;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.recipe.custom.CuisineRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CuisineRecipeCategory implements IRecipeCategory<CuisineRecipe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/cuisine.png");
    public static final RecipeType<CuisineRecipe> TYPE = RecipeType.create(ModernDelightMain.MOD_ID, "cuisine", CuisineRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public CuisineRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 51);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CUISINE_TABLE.get()));
    }

    @Override
    public RecipeType<CuisineRecipe> getRecipeType() { return TYPE; }
    @Override
    public Component getTitle() { return Component.translatable("emi.category.moderndelight.cuisine"); }
    @Override
    public IDrawable getBackground() { return background; }
    @Override
    public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CuisineRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 29)
                .addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 8)
                .addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 29)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(CuisineRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, 150, 51);
    }
}
