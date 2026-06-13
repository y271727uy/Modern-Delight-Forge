package com.y271727uy.moderndelight.compat.jei.category;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.kitchenware.FreezerBlockEntity;
import com.y271727uy.moderndelight.recipe.custom.FreezingRecipe;
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
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class FreezingRecipeCategory implements IRecipeCategory<FreezingRecipe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/freezer.png");
    public static final RecipeType<FreezingRecipe> TYPE = RecipeType.create(ModernDelightMain.MOD_ID, "freezing", FreezingRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public FreezingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 70);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FREEZER.get()));
    }

    @Override
    public RecipeType<FreezingRecipe> getRecipeType() { return TYPE; }
    @Override
    public Component getTitle() { return Component.translatable("emi.category.moderndelight.freezing"); }
    @Override
    public IDrawable getBackground() { return background; }
    @Override
    public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FreezingRecipe recipe, IFocusGroup focuses) {
        List<Ingredient> ingredients = recipe.getIngredients();
        int[][] inputPos = {{48, 10}, {66, 10}, {84, 10}};
        for (int i = 0; i < ingredients.size() && i < inputPos.length; i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, inputPos[i][0], inputPos[i][1])
                    .addIngredients(ingredients.get(i));
        }
        List<ItemStack> coolStacks = FreezerBlockEntity.createCoolTimeMap().keySet().stream()
                .map(ItemStack::new).toList();
        builder.addSlot(RecipeIngredientRole.CATALYST, 30, 46)
                .addItemStacks(coolStacks);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 66, 46)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(FreezingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 4, 26, 3, 25, 144, 42);
    }
}
