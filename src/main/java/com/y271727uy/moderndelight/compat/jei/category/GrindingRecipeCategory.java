package com.y271727uy.moderndelight.compat.jei.category;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.recipe.custom.GrindingRecipe;
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

public class GrindingRecipeCategory implements IRecipeCategory<GrindingRecipe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/grinding.png");
    public static final RecipeType<GrindingRecipe> TYPE = RecipeType.create(ModernDelightMain.MOD_ID, "grinding", GrindingRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public GrindingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 53);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.STONE_MORTAR.get()));
    }

    @Override public RecipeType<GrindingRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return Component.translatable("emi.category.moderndelight.grinding"); }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GrindingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 18)
                .addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.CATALYST, 58, 29)
                .addIngredients(Ingredient.of(ModItems.STONE_MORTAR.get()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 101, 18)
                .addItemStack(recipe.getOutputs().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 119, 18)
                .addItemStack(recipe.getOutputs().get(1));
    }

    @Override
    public void draw(GrindingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 5, 5, 4, 4, 142, 45);
    }
}
