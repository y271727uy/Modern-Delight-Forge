package com.y271727uy.moderndelight.compat.emi.recipe;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.fluid.ModFluid;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.recipe.custom.DeepFryingRecipe;
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

public class EMIDeepFryingRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/deep_fryer.png");
    public static final EmiStack WORKSTATION = EmiStack.of(ModBlocks.DEEP_FRYER.get());
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(new ResourceLocation(ModernDelightMain.MOD_ID, "deep_frying"), WORKSTATION);

    private final ResourceLocation id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EMIDeepFryingRecipe(DeepFryingRecipe recipe) {
        this.id = recipe.getId();
        List<EmiIngredient> inputs = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()){
            inputs.add(EmiIngredient.of(ingredient));
        }
        inputs.add(EmiIngredient.of(Ingredient.of(ModBlocks.GAS_CANISTER.get())));
        inputs.add(EmiIngredient.of(Ingredient.of(ModItems.HOLDER.get())));
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
        widgets.addTexture(TEXTURE,20,5,94,64,19,4);
        widgets.addSlot(input.get(0), 20, 10);
        widgets.addSlot(EmiStack.of(ModFluid.STILL_VEGETABLE_OIL.get()),57,10);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.DEEP_FRYER.get())),38,34);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.GAS_CANISTER.get())),20,34);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModItems.HOLDER.get())),78,52);


        widgets.addSlot(output.get(0), 115, 34).recipeContext(this);
    }

}
