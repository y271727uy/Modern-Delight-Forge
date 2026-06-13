package com.y271727uy.moderndelight.compat.jei.category;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.recipe.custom.WhiskingRecipe;
import com.y271727uy.moderndelight.tag.TagKeys;
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

public class WhiskingRecipeCategory implements IRecipeCategory<WhiskingRecipe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/glass_bowl.png");
    public static final RecipeType<WhiskingRecipe> TYPE = RecipeType.create(ModernDelightMain.MOD_ID, "whisking", WhiskingRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public WhiskingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 55);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.GLASS_BOWL.get()));
    }

    @Override
    public RecipeType<WhiskingRecipe> getRecipeType() { return TYPE; }
    @Override
    public Component getTitle() { return Component.translatable("emi.category.moderndelight.whisking"); }
    @Override
    public IDrawable getBackground() { return background; }
    @Override
    public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WhiskingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 28, 6)
                .addIngredients(Ingredient.of(TagKeys.WHISKS));
        builder.addSlot(RecipeIngredientRole.INPUT, 47, 24)
                .addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 102, 21)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(WhiskingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 27, 5, 26, 4, 69, 47);
    }
}
