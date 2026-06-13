package com.y271727uy.moderndelight.compat.jei.category;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.recipe.custom.JuiceExtractingRecipe;
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

public class JuiceExtractingRecipeCategory implements IRecipeCategory<JuiceExtractingRecipe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/juice_extracting.png");
    public static final RecipeType<JuiceExtractingRecipe> TYPE = RecipeType.create(ModernDelightMain.MOD_ID, "juice_extracting", JuiceExtractingRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public JuiceExtractingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 53);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.JUICE_EXTRACTOR.get()));
    }

    @Override public RecipeType<JuiceExtractingRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return Component.translatable("emi.category.moderndelight.juice_extracting"); }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JuiceExtractingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 13, 10)
                .addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 31, 10)
                .addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 13, 28)
                .addIngredients(recipe.getIngredients().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 31, 28)
                .addIngredients(recipe.getIngredients().get(3));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 19)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(JuiceExtractingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 50, 11, 49, 10, 63, 26);
    }
}
