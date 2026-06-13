package com.y271727uy.moderndelight.compat.jei.category;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.recipe.custom.MixWithWaterRecipe;
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

public class MixWithWaterRecipeCategory implements IRecipeCategory<MixWithWaterRecipe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/water_glass_bowl.png");
    public static final RecipeType<MixWithWaterRecipe> TYPE = RecipeType.create(ModernDelightMain.MOD_ID, "mix_with_water", MixWithWaterRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public MixWithWaterRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 55);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.GLASS_BOWL.get()));
    }

    @Override
    public RecipeType<MixWithWaterRecipe> getRecipeType() { return TYPE; }
    @Override
    public Component getTitle() { return Component.translatable("emi.category.moderndelight.mix_with_water"); }
    @Override
    public IDrawable getBackground() { return background; }
    @Override
    public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MixWithWaterRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 49, 14)
                .addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 102, 21)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(MixWithWaterRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 44, 20, 43, 19, 52, 22);
    }
}
