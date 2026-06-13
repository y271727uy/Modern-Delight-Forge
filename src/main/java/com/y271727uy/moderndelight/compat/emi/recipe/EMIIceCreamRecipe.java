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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EMIIceCreamRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/ice_cream.png");
    public static final EmiStack WORKSTATION = EmiStack.of(ModBlocks.ICE_CREAM_MAKER.get());
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(new ResourceLocation(ModernDelightMain.MOD_ID, "ice_cream_making"), WORKSTATION);

    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EMIIceCreamRecipe() {
        List<EmiIngredient> inputs = new ArrayList<>();
        inputs.add(EmiIngredient.of(TagKeys.CREAMS));
        inputs.add(EmiIngredient.of(Ingredient.of(Items.SUGAR)));
        inputs.add(EmiIngredient.of(Ingredient.of(Items.EGG)));
        inputs.add(EmiIngredient.of(Ingredient.of(ModItems.ICE_CREAM_CONE.get())));
        this.input = inputs;
        this.output = List.of(EmiStack.of(ModItems.ICE_CREAM.get()));
    }
    @Override
    public EmiRecipeCategory getCategory() {
        return CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return new ResourceLocation(ModernDelightMain.MOD_ID,"ice_cream_making");
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
        return 90;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TEXTURE,5,5,142,83,4,4);
        widgets.addSlot(EmiIngredient.of(TagKeys.CREAMS), 44, 9);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(Items.SUGAR)), 44, 27);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(Items.EGG)), 44, 45);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModItems.ICE_CREAM_CONE.get())), 33, 69);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModBlocks.ICE_CREAM_MAKER.get())), 66, 69);


        widgets.addSlot(output.get(0), 100, 69).recipeContext(this);
    }

}
