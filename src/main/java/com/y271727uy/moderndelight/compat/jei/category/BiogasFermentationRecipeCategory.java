package com.y271727uy.moderndelight.compat.jei.category;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.biogas.BiogasDigesterIOBlockEntity;
import com.y271727uy.moderndelight.fluid.ModFluid;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class BiogasFermentationRecipeCategory implements IRecipeCategory<BiogasFermentationRecipeCategory.BiogasRecipe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/biogas_fermentation.png");
    public static final RecipeType<BiogasRecipe> TYPE = RecipeType.create(ModernDelightMain.MOD_ID, "biogas_fermentation", BiogasRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public static List<BiogasRecipe> getRecipes() {
        return List.of(new BiogasRecipe());
    }

    public record BiogasRecipe() {}

    public BiogasFermentationRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 122);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BIOGAS_DIGESTER_CONTROLLER.get()));
    }

    @Override public RecipeType<BiogasRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return Component.translatable("emi.category.moderndelight.biogas_fermentation"); }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BiogasRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.CATALYST, 86, 35)
                .addIngredients(Ingredient.of(ModBlocks.GAS_CANISTER.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 68, 35)
                .addIngredients(Ingredient.of(ModBlocks.BIOGAS_DIGESTER_IO.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 68, 53)
                .addIngredients(Ingredient.of(ModBlocks.BIOGAS_DIGESTER_CONTROLLER.get()));
        ItemStack foodHint = new ItemStack(Items.APPLE);
        foodHint.setHoverName(Component.translatable("moderndelight.rei_plugin.biogas_fermentation.food"));
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 14)
                .addItemStack(foodHint);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 32, 35)
                .addItemStack(new ItemStack(BiogasDigesterIOBlockEntity.getDigestate()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 122, 35)
                .addFluidStack(ModFluid.STILL_LIQUEFIED_BIOGAS.get(), 1000);
    }

    @Override
    public void draw(BiogasRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 5, 5, 4, 4, 142, 114);
    }
}
