package com.y271727uy.moderndelight.compat.emi.recipe;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.biogas.BiogasDigesterIOBlockEntity;
import com.y271727uy.moderndelight.fluid.ModFluid;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EMIBiogasFermentationRecipe implements EmiRecipe {
    private static final String FOOD_TRANSLATION_KEY = "moderndelight.rei_plugin.biogas_fermentation.food";
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/biogas_fermentation.png");
    public static final EmiStack WORKSTATION = EmiStack.of(ModBlocks.BIOGAS_DIGESTER_CONTROLLER.get());
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(new ResourceLocation(ModernDelightMain.MOD_ID, "biogas_fermentation"), WORKSTATION);

    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EMIBiogasFermentationRecipe() {
        List<EmiIngredient> inputs = new ArrayList<>();
        inputs.add(EmiIngredient.of(Ingredient.of(ModBlocks.BIOGAS_DIGESTER_IO.get())));
        inputs.add(EmiIngredient.of(Ingredient.of(ModBlocks.GAS_CANISTER.get())));
        inputs.add(EmiIngredient.of(Ingredient.of(ModBlocks.BIOGAS_DIGESTER_CONTROLLER.get())));
        this.input = inputs;
        List<EmiStack> lists = new ArrayList<>(List.of(EmiStack.of(BiogasDigesterIOBlockEntity.getDigestate())));
        lists.add(EmiStack.of(ModFluid.STILL_LIQUEFIED_BIOGAS.get()));
        this.output = lists;
    }
    @Override
    public EmiRecipeCategory getCategory() {
        return CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return new ResourceLocation(ModernDelightMain.MOD_ID,"biogas_fermentation");
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
        return 122;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TEXTURE,5,5,142,114,4,4);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.GAS_CANISTER.get())), 85, 34).recipeContext(this);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.BIOGAS_DIGESTER_IO.get())), 67, 34);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.BIOGAS_DIGESTER_CONTROLLER.get())), 67, 52);
        ItemStack stack = new ItemStack(Items.APPLE);
        stack.setHoverName(Component.translatable(FOOD_TRANSLATION_KEY));
        widgets.addSlot(EmiIngredient.of(Ingredient.of(stack)), 49, 13);

        widgets.addSlot(output.get(0), 31, 34).recipeContext(this);
        widgets.addSlot(output.get(1), 121, 34).recipeContext(this);
    }

}
