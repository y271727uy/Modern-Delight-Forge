package com.y271727uy.moderndelight.compat.jei.category;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.fluid.ModFluid;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.recipe.custom.DeepFryingRecipe;
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

public class DeepFryingRecipeCategory implements IRecipeCategory<DeepFryingRecipe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/deep_fryer.png");
    public static final RecipeType<DeepFryingRecipe> TYPE = RecipeType.create(ModernDelightMain.MOD_ID, "deep_frying", DeepFryingRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public DeepFryingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 73);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.DEEP_FRYER.get()));
    }

    @Override
    public RecipeType<DeepFryingRecipe> getRecipeType() { return TYPE; }
    @Override
    public Component getTitle() { return Component.translatable("emi.category.moderndelight.deep_frying"); }
    @Override
    public IDrawable getBackground() { return background; }
    @Override
    public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DeepFryingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 21, 11)
                .addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 58, 11)
                .addFluidStack(ModFluid.STILL_VEGETABLE_OIL.get(), 1000);
        builder.addSlot(RecipeIngredientRole.INPUT, 39, 35)
                .addIngredients(Ingredient.of(ModBlocks.DEEP_FRYER.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 21, 35)
                .addIngredients(Ingredient.of(ModBlocks.GAS_CANISTER.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 79, 53)
                .addIngredients(Ingredient.of(ModItems.HOLDER.get()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 35)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(DeepFryingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 20, 5, 19, 4, 94, 64);
    }
}
