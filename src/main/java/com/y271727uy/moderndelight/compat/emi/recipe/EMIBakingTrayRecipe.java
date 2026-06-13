package com.y271727uy.moderndelight.compat.emi.recipe;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.tag.TagKeys;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EMIBakingTrayRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/baking_tray.png");
    public static final EmiStack WORKSTATION = EmiStack.of(ModBlocks.BAKING_TRAY.get());
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(new ResourceLocation(ModernDelightMain.MOD_ID, "stir_frying"), WORKSTATION);

    private final ResourceLocation id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EMIBakingTrayRecipe(CampfireCookingRecipe recipe) {
        this.id = new ResourceLocation(ModernDelightMain.MOD_ID,"stir_frying/"+recipe.getResultItem(null).getItem().toString());
        List<EmiIngredient> inputs = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()){
            inputs.add(EmiIngredient.of(ingredient));
        }
        inputs.add(EmiIngredient.of(TagKeys.SPATULAS));
        inputs.add(EmiIngredient.of(Ingredient.of(ModBlocks.GAS_COOKING_STOVE.get())));
        inputs.add(EmiIngredient.of(Ingredient.of(ModBlocks.GAS_CANISTER.get())));
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
        return 73;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TEXTURE,37,5,58,47,36,4);
        widgets.addSlot(input.get(0), 37, 5);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.BAKING_TRAY.get())), 37, 23);
        widgets.addSlot(EmiIngredient.of(TagKeys.SPATULAS), 66, 43);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.GAS_COOKING_STOVE.get())), 37, 52);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.GAS_CANISTER.get())), 19, 52);

        widgets.addSlot(output.get(0), 97, 5).recipeContext(this);
    }

}
