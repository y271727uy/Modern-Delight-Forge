package com.y271727uy.moderndelight.compat.jei.category;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.item.ModItems;
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
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class PizzaRecipeCategory implements IRecipeCategory<PizzaRecipeCategory.PizzaRecipe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/pizza.png");
    public static final RecipeType<PizzaRecipe> TYPE = RecipeType.create(ModernDelightMain.MOD_ID, "pizza", PizzaRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public static List<PizzaRecipe> getRecipes() {
        return List.of(new PizzaRecipe());
    }

    public record PizzaRecipe() {}

    public PizzaRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 55);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.OVEN.get()));
    }

    @Override public RecipeType<PizzaRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return Component.translatable("emi.category.moderndelight.pizza_making"); }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PizzaRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 11)
                .addIngredients(Ingredient.of(ModBlocks.WHEAT_DOUGH.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 25, 30)
                .addIngredients(Ingredient.of(TagKeys.KNEADING_STICKS));
        builder.addSlot(RecipeIngredientRole.INPUT, 45, 30)
                .addIngredients(Ingredient.of(ModItems.CHEESE.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 68, 11)
                .addIngredients(Ingredient.of(ModBlocks.PIZZA_WIP.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 87, 30)
                .addIngredients(Ingredient.of(TagKeys.PIZZA_INGREDIENTS));
        builder.addSlot(RecipeIngredientRole.INPUT, 107, 30)
                .addIngredients(Ingredient.of(ModItems.CHEESE.get()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 130, 11)
                .addItemStack(new ItemStack(ModBlocks.RAW_PIZZA.get()));
    }

    @Override
    public void draw(PizzaRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 5, 5, 4, 4, 142, 47);
    }
}
