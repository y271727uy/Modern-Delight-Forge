package com.y271727uy.moderndelight.compat.emi.recipe;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.kitchenware.FreezerBlockEntity;
import com.y271727uy.moderndelight.recipe.custom.FreezingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EMIFreezingRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/freezer.png");
    public static final EmiStack WORKSTATION = EmiStack.of(ModBlocks.FREEZER.get());
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(new ResourceLocation(ModernDelightMain.MOD_ID, "freezing"), WORKSTATION);

    private final ResourceLocation id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;
    private final List<EmiIngredient> coolItems = new ArrayList<>();

    public EMIFreezingRecipe(FreezingRecipe recipe) {
        this.id = recipe.getId();
        List<EmiIngredient> inputs = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()){
            inputs.add(EmiIngredient.of(ingredient));
        }
        for (Item item : FreezerBlockEntity.createCoolTimeMap().keySet()){
            inputs.add(EmiIngredient.of(Ingredient.of(item)));
            coolItems.add(EmiIngredient.of(Ingredient.of(item)));
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
        return 70;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TEXTURE,4,26,144,42,3,25);
        widgets.addSlot(input.get(0), 47, 9);
        widgets.addSlot(input.get(1), 65, 9);
        widgets.addSlot(input.get(2), 83, 9);
        widgets.addSlot(EmiIngredient.of(coolItems),29,45);

        widgets.addSlot(output.get(0), 65, 45).recipeContext(this);
    }

}
