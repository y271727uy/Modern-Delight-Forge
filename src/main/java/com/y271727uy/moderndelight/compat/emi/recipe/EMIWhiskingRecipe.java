package com.y271727uy.moderndelight.compat.emi.recipe;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.recipe.custom.WhiskingRecipe;
import com.y271727uy.moderndelight.tag.TagKeys;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EMIWhiskingRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/glass_bowl.png");
    public static final EmiStack WORKSTATION = EmiStack.of(ModBlocks.GLASS_BOWL.get());
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(new ResourceLocation(ModernDelightMain.MOD_ID, "whisking"), WORKSTATION);
    private final ResourceLocation id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EMIWhiskingRecipe(WhiskingRecipe recipe) {
        this.id = recipe.getId();
        List<EmiIngredient> lists = new ArrayList<>(List.of(EmiIngredient.of(recipe.getIngredients().get(0))));
        lists.add(EmiIngredient.of(TagKeys.WHISKS));
        this.input = lists;
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
        widgets.addTexture(TEXTURE, 27, 5 ,69,47,26,4);

        widgets.addSlot(EmiIngredient.of(TagKeys.WHISKS,1),27,5);
        widgets.addSlot(input.get(0), 46, 23);

        widgets.addSlot(output.get(0), 101, 20).recipeContext(this);
    }
    
}
