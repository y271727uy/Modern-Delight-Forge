package com.y271727uy.moderndelight.compat.emi.recipe;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.recipe.custom.BakingRecipe;
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

public class EMIBakingRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/oven.png");
    public static final EmiStack WORKSTATION = EmiStack.of(ModBlocks.OVEN.get());
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(new ResourceLocation(ModernDelightMain.MOD_ID, "baking"), WORKSTATION);

    private final ResourceLocation id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EMIBakingRecipe(BakingRecipe recipe) {
        this.id = recipe.getId();
        List<EmiIngredient> inputs = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()){
            inputs.add(EmiIngredient.of(ingredient));
        }
        this.input = inputs;
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
        widgets.addTexture(TEXTURE,5,5,142,47,4,4);
        widgets.addSlot(input.get(0), 9, 19);
        widgets.addSlot(input.get(1), 27, 19);
        widgets.addSlot(input.get(2), 45, 19);
        widgets.addSlot(input.get(3), 63, 19);

        widgets.addSlot(output.get(0), 118, 19).recipeContext(this);
    }

}
