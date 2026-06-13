package com.y271727uy.moderndelight.compat.emi.recipe;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.recipe.custom.MixWithWaterRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nullable;

import java.util.List;

public class EMIMixWithWaterRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/water_glass_bowl.png");
    public static final EmiStack WORKSTATION = EmiStack.of(ModBlocks.GLASS_BOWL.get());
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(new ResourceLocation(ModernDelightMain.MOD_ID, "mix_with_water"), WORKSTATION);
    private final ResourceLocation id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EMIMixWithWaterRecipe(MixWithWaterRecipe recipe) {
        this.id = recipe.getId();
        this.input = List.of(EmiIngredient.of(recipe.getIngredients().get(0)));
        this.output = List.of(EmiStack.of(recipe.getResultItem(null)));
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
        return 55;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TEXTURE, 44, 20 ,52,22,43,19);
        widgets.addSlot(input.get(0), 48, 13);

        widgets.addSlot(output.get(0), 101, 20).recipeContext(this);
    }

}
