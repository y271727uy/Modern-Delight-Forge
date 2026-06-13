package com.y271727uy.moderndelight.compat.jei.category;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.tag.TagKeys;
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
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;

public class BakingTrayRecipeCategory implements IRecipeCategory<CampfireCookingRecipe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/baking_tray.png");
    public static final RecipeType<CampfireCookingRecipe> TYPE = RecipeType.create(ModernDelightMain.MOD_ID, "baking_tray", CampfireCookingRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public BakingTrayRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 73);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BAKING_TRAY.get()));
    }

    @Override
    public RecipeType<CampfireCookingRecipe> getRecipeType() { return TYPE; }
    @Override
    public Component getTitle() { return Component.translatable("emi.category.moderndelight.stir_frying"); }
    @Override
    public IDrawable getBackground() { return background; }
    @Override
    public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CampfireCookingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 38, 6)
                .addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 38, 24)
                .addIngredients(Ingredient.of(ModBlocks.BAKING_TRAY.get()));
        builder.addSlot(RecipeIngredientRole.CATALYST, 67, 44)
                .addIngredients(Ingredient.of(TagKeys.SPATULAS));
        builder.addSlot(RecipeIngredientRole.INPUT, 38, 53)
                .addIngredients(Ingredient.of(ModBlocks.GAS_COOKING_STOVE.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 53)
                .addIngredients(Ingredient.of(ModBlocks.GAS_CANISTER.get()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 98, 6)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(CampfireCookingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 37, 5, 36, 4, 58, 47);
    }
}
