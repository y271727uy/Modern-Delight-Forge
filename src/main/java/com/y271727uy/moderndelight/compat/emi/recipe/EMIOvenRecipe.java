package com.y271727uy.moderndelight.compat.emi.recipe;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.tag.TagKeys;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EMIOvenRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/transform.png");
    public static final EmiStack WORKSTATION = EmiStack.of(ModItems.CROWBAR.get());
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(new ResourceLocation(ModernDelightMain.MOD_ID, "oven_transforming"), WORKSTATION);

    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EMIOvenRecipe() {
        List<EmiIngredient> inputs = new ArrayList<>();
        inputs.add(EmiIngredient.of(Ingredient.of(ModBlocks.OVEN.get())));
        inputs.add(EmiIngredient.of(TagKeys.CROWBARS));
        this.input = inputs;
        this.output = List.of(EmiStack.of(ModBlocks.ADVANCE_FURNACE.get()),EmiStack.of(ModBlocks.BAKING_TRAY.get()));
    }
    @Override
    public EmiRecipeCategory getCategory() {
        return CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return new ResourceLocation(ModernDelightMain.MOD_ID,"oven_transforming");
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return input;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return output;
    }

    @Override
    public int getDisplayWidth() {
        return 150;
    }

    @Override
    public int getDisplayHeight() {
        return 52;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TEXTURE,5,5,142,44,4,4);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.OVEN.get())), 22, 18);
        widgets.addSlot(EmiIngredient.of(TagKeys.CROWBARS), 55, 6);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.ADVANCE_FURNACE.get())), 101, 18).recipeContext(this);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.BAKING_TRAY.get())), 119, 18).recipeContext(this);

    }

}
