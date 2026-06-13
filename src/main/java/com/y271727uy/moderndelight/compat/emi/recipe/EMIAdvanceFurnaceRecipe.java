package com.y271727uy.moderndelight.compat.emi.recipe;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
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

public class EMIAdvanceFurnaceRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/transform.png");
    public static final EmiStack WORKSTATION = EmiStack.of(ModBlocks.BAKING_TRAY.get());
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(new ResourceLocation(ModernDelightMain.MOD_ID, "advance_furnace_transforming"), WORKSTATION);

    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EMIAdvanceFurnaceRecipe() {
        List<EmiIngredient> inputs = new ArrayList<>();
        inputs.add(EmiIngredient.of(Ingredient.of(ModBlocks.ADVANCE_FURNACE.get())));
        inputs.add(EmiIngredient.of(Ingredient.of(ModBlocks.BAKING_TRAY.get())));
        this.input = inputs;
        this.output = List.of(EmiStack.of(ModBlocks.OVEN.get()));
    }
    @Override
    public EmiRecipeCategory getCategory() {
        return CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return new ResourceLocation(ModernDelightMain.MOD_ID,"advance_furnace_transforming");
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
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.ADVANCE_FURNACE.get())), 22, 18);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.BAKING_TRAY.get())), 55, 6);

        widgets.addSlot(output.get(0), 101, 18).recipeContext(this);

    }

}
