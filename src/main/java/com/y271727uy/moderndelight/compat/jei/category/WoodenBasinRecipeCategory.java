package com.y271727uy.moderndelight.compat.jei.category;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.recipe.custom.SqueezeRecipe;
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

public class WoodenBasinRecipeCategory implements IRecipeCategory<SqueezeRecipe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/wooden_basin.png");
    public static final RecipeType<SqueezeRecipe> TYPE = RecipeType.create(ModernDelightMain.MOD_ID, "oil_extraction", SqueezeRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public WoodenBasinRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 75);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.WOODEN_BASIN.get()));
    }

    @Override public RecipeType<SqueezeRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return Component.translatable("emi.category.moderndelight.oil_extraction"); }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SqueezeRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 85, 5)
                .addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 85, 37)
                .addIngredients(Ingredient.of(TagKeys.FILTERS));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 130, 37)
                .addItemStack(recipe.getResultItem(null));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 40, 37)
                .addFluidStack(recipe.getOutputFluid().getFluid(), (int) recipe.getOutputFluid().getAmount());
    }

    @Override
    public void draw(SqueezeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 5, 5, 4, 4, 142, 67);
    }
}
