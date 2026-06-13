package com.y271727uy.moderndelight.compat.jei.category;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.item.food.instant_noodles.CookedPortablePotItem;
import com.y271727uy.moderndelight.util.enums.SpecialIngredient;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class InstantNoodlesMakingRecipeCategory implements IRecipeCategory<InstantNoodlesMakingRecipeCategory.InstantNoodlesRecipe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/instant_noodles.png");
    public static final RecipeType<InstantNoodlesRecipe> TYPE = RecipeType.create(ModernDelightMain.MOD_ID, "instant_noodles_making", InstantNoodlesRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public static List<InstantNoodlesRecipe> getRecipes() {
        return List.of(new InstantNoodlesRecipe());
    }

    public record InstantNoodlesRecipe() {}

    public InstantNoodlesMakingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 116);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.CRAFTING_TABLE));
    }

    @Override public RecipeType<InstantNoodlesRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return Component.translatable("emi.category.moderndelight.instant_noodles_making"); }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, InstantNoodlesRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 13, 8)
                .addItemStack(new ItemStack(ModItems.MULTIFUNCTIONAL_WRAPPING_PAPER.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 31, 8)
                .addItemStack(new ItemStack(ModItems.FRIED_NOODLES.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 31, 51)
                .addItemStack(new ItemStack(ModItems.PACKAGED_INSTANT_NOODLES.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 51)
                .addItemStack(new ItemStack(ModItems.QUICKLIME.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 69, 51)
                .addItemStack(new ItemStack(Items.WATER_BUCKET));
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 83)
                .addItemStack(new ItemStack(ModItems.PORTABLE_POT.get()));

        int i = 0;
        for (SpecialIngredient specialIngredient : SpecialIngredient.values()) {
            List<ItemStack> exampleItems = new ArrayList<>();
            exampleItems.add(CookedPortablePotItem.createCookedPot(specialIngredient.getIngredients()));
            for (ItemStack stack : exampleItems) {
                builder.addSlot(RecipeIngredientRole.INPUT, 130, 6 + (18 * i))
                        .addItemStack(stack);
                i++;
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 91, 83)
                .addItemStack(new ItemStack(ModItems.COOKED_PORTABLE_POT.get()));
    }

    @Override
    public void draw(InstantNoodlesRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 5, 5, 4, 4, 143, 109);
    }
}
