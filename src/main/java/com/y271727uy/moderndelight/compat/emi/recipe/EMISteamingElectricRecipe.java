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
import net.minecraft.world.level.material.Fluids;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EMISteamingElectricRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/steaming_electric.png");
    public static final EmiStack WORKSTATION = EmiStack.of(ModBlocks.ELECTRIC_STEAMER.get());
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(new ResourceLocation(ModernDelightMain.MOD_ID, "steaming_electric"), WORKSTATION);

    private final ResourceLocation id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EMISteamingElectricRecipe(SteamingRecipe recipe) {
        this.id = new ResourceLocation(recipe.getId().getNamespace(),"electric_steamer/"+recipe.getId().getPath());
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
        return 140;
    }

    @Override
    public int getDisplayHeight() {
        return 53;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TEXTURE,5,5,130,43,4,4);
        widgets.addSlot(input.get(0), 55, 10);
        widgets.addSlot(EmiStack.of(Fluids.WATER), 14, 28);

        widgets.addSlot(output.get(0), 107, 10).recipeContext(this);
    }

}
