package com.y271727uy.moderndelight.compat.emi.recipe;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.recipe.custom.SqueezeRecipe;
import com.y271727uy.moderndelight.tag.TagKeys;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nullable;

import java.util.List;

public class EMIWoodenBasinRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/wooden_basin.png");
    public static final EmiStack WORKSTATION = EmiStack.of(ModBlocks.WOODEN_BASIN.get());
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(new ResourceLocation(ModernDelightMain.MOD_ID, "oil_extraction"), WORKSTATION);

    private final List<EmiIngredient> input;
    private final List<EmiStack> output;
    private final ResourceLocation id;
    public EMIWoodenBasinRecipe(SqueezeRecipe recipe) {
        this.input = List.of(EmiIngredient.of(recipe.getIngredients().get(0)));
        this.output = List.of(
                EmiStack.of(recipe.getResultItem(null)),
                EmiStack.of(recipe.getOutputFluid().getFluid(), (int) recipe.getOutputFluid().getAmount()));
        this.id = recipe.getId();
    }
    @Override
    public EmiRecipeCategory getCategory() {
        return CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
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
        return 75;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TEXTURE,5,5,142,67,4,4);
        widgets.addSlot(input.get(0), 84, 4);
        widgets.addSlot(EmiIngredient.of(TagKeys.FILTERS), 84, 36);
        widgets.addSlot(output.get(0), 129, 36).recipeContext(this);
        widgets.addSlot(output.get(1), 39, 36).recipeContext(this);
    }

}
