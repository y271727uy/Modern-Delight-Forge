package com.y271727uy.moderndelight.compat.emi.recipe;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.tag.TagKeys;
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

public class EMIPizzaRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/pizza.png");
    public static final EmiStack WORKSTATION = EmiStack.of(ModBlocks.WHEAT_DOUGH.get());
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(new ResourceLocation(ModernDelightMain.MOD_ID, "pizza_making"), WORKSTATION);

    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EMIPizzaRecipe() {
        List<EmiIngredient> inputs = new ArrayList<>();
        inputs.add(EmiIngredient.of(Ingredient.of(ModBlocks.WHEAT_DOUGH_ITEM.get())));
        inputs.add(EmiIngredient.of(TagKeys.KNEADING_STICKS));
        inputs.add(EmiIngredient.of(TagKeys.PIZZA_INGREDIENTS));
        inputs.add(EmiIngredient.of(Ingredient.of(ModItems.CHEESE.get())));
        inputs.add(EmiIngredient.of(Ingredient.of(ModBlocks.PIZZA_WIP.get())));
        this.input = inputs;
        this.output = List.of(EmiStack.of(ModBlocks.RAW_PIZZA.get()),EmiStack.of(ModBlocks.PIZZA_WIP.get()));
    }
    @Override
    public EmiRecipeCategory getCategory() {
        return CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return new ResourceLocation(ModernDelightMain.MOD_ID,"pizza_making");
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
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.WHEAT_DOUGH.get())), 5, 10);
        widgets.addSlot(EmiIngredient.of(TagKeys.KNEADING_STICKS), 24, 29);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModItems.CHEESE.get())), 44, 29);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.PIZZA_WIP.get())), 67, 10);
        widgets.addSlot(EmiIngredient.of(TagKeys.PIZZA_INGREDIENTS), 86, 29);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModItems.CHEESE.get())), 106, 29);


        widgets.addSlot(output.get(0), 129, 10).recipeContext(this);
    }

}
