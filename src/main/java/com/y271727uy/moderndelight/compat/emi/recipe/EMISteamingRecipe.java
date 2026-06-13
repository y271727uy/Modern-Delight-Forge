package com.y271727uy.moderndelight.compat.emi.recipe;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.recipe.custom.SteamingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EMISteamingRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/steaming.png");
    public static final EmiStack WORKSTATION = EmiStack.of(ModBlocks.BAMBOO_GRATE.get());
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(new ResourceLocation(ModernDelightMain.MOD_ID, "steaming"), WORKSTATION);

    private final ResourceLocation id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EMISteamingRecipe(SteamingRecipe recipe) {
        this.id = recipe.getId();
        List<EmiIngredient> inputs = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()){
            inputs.add(EmiIngredient.of(ingredient));
        }
        inputs.add(EmiIngredient.of(Ingredient.of(ModBlocks.BAMBOO_COVER.get())));
        inputs.add(EmiIngredient.of(Ingredient.of(Blocks.CAULDRON)));
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
        return 95;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TEXTURE,5,5,142,87,4,4);
        widgets.addSlot(input.get(0), 52, 15);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.BAMBOO_COVER.get())), 23, 6);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.BAMBOO_GRATE.get())), 23, 24);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(Blocks.CAULDRON)), 23, 55);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.GAS_COOKING_STOVE.get())), 23, 73);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.GAS_CANISTER.get())), 41, 73);
        widgets.addSlot(EmiStack.of(Fluids.WATER), 61, 55);

        widgets.addSlot(output.get(0), 106, 15).recipeContext(this);
    }

}
