package com.y271727uy.moderndelight.compat.jei.category;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.item.ModItems;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class IceCreamRecipeCategory implements IRecipeCategory<IceCreamRecipeCategory.IceCreamRecipe> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/ice_cream.png");
    public static final RecipeType<IceCreamRecipe> TYPE = RecipeType.create(ModernDelightMain.MOD_ID, "ice_cream_making", IceCreamRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public static List<IceCreamRecipe> getRecipes() {
        return List.of(new IceCreamRecipe());
    }

    public record IceCreamRecipe() {}

    public IceCreamRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 90);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.ICE_CREAM_MAKER.get()));
    }

    @Override public RecipeType<IceCreamRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return Component.translatable("emi.category.moderndelight.ice_cream_making"); }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IceCreamRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 45, 10)
                .addIngredients(Ingredient.of(TagKeys.CREAMS));
        builder.addSlot(RecipeIngredientRole.INPUT, 45, 28)
                .addItemStack(new ItemStack(Items.SUGAR));
        builder.addSlot(RecipeIngredientRole.INPUT, 45, 46)
                .addItemStack(new ItemStack(Items.EGG));
        builder.addSlot(RecipeIngredientRole.INPUT, 34, 70)
                .addItemStack(new ItemStack(ModItems.ICE_CREAM_CONE.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 67, 70)
                .addItemStack(new ItemStack(ModBlocks.ICE_CREAM_MAKER.get()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 101, 70)
                .addItemStack(new ItemStack(ModItems.ICE_CREAM.get()));
    }

    @Override
    public void draw(IceCreamRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(TEXTURE, 5, 5, 4, 4, 142, 83);
    }
}
