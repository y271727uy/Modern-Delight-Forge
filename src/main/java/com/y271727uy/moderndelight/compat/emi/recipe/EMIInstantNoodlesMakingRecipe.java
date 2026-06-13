package com.y271727uy.moderndelight.compat.emi.recipe;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.item.food.instant_noodles.CookedPortablePotItem;
import com.y271727uy.moderndelight.util.TextUtil;
import com.y271727uy.moderndelight.util.enums.SpecialIngredient;
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
import net.minecraft.world.level.block.Blocks;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
public class EMIInstantNoodlesMakingRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID, "textures/gui/compats/instant_noodles.png");
    public static final EmiStack WORKSTATION = EmiStack.of(Blocks.CRAFTING_TABLE);
    public static final EmiRecipeCategory CATEGORY
            = new EmiRecipeCategory(new ResourceLocation(ModernDelightMain.MOD_ID, "instant_noodles_making"), WORKSTATION);

    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EMIInstantNoodlesMakingRecipe() {
        List<EmiIngredient> inputs = new ArrayList<>();
        inputs.add(EmiIngredient.of(Ingredient.of(ModItems.FRIED_NOODLES.get())));
        inputs.add(EmiIngredient.of(Ingredient.of(ModItems.PORTABLE_POT.get())));
        inputs.add(EmiIngredient.of(Ingredient.of(ModItems.PACKAGED_INSTANT_NOODLES.get())));
        inputs.add(EmiIngredient.of(Ingredient.of(ModItems.MULTIFUNCTIONAL_WRAPPING_PAPER.get())));
        for (SpecialIngredient specialIngredient : SpecialIngredient.values()){
            for(Ingredient ingredient : specialIngredient.getIngredients()){
                for(ItemStack stack : ingredient.getItems()){
                    inputs.add(EmiIngredient.of(Ingredient.of(stack.getItem())));
                }
            }
        }
        this.input = inputs;
        this.output = new ArrayList<>(List.of(EmiStack.of(ModItems.COOKED_PORTABLE_POT.get()),EmiStack.of(ModItems.PACKAGED_INSTANT_NOODLES.get())));
    }
    @Override
    public EmiRecipeCategory getCategory() {
        return CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return new ResourceLocation(ModernDelightMain.MOD_ID,"instant_noodles_making");
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
        return 116;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TEXTURE,5,5,143,109,4,4);

        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModItems.MULTIFUNCTIONAL_WRAPPING_PAPER.get())), 12, 7);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModItems.FRIED_NOODLES.get())), 30, 7);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModItems.PACKAGED_INSTANT_NOODLES.get())), 30, 50);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModItems.QUICKLIME.get())), 49, 50);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(Items.WATER_BUCKET)), 68, 50);
        widgets.addSlot(EmiIngredient.of(Ingredient.of(ModItems.PORTABLE_POT.get())), 49, 82);
        // Tooltip component not available in this environment
        List<ItemStack> exampleItems = new ArrayList<>();
        for (SpecialIngredient specialIngredient : SpecialIngredient.values()){
            exampleItems.add(CookedPortablePotItem.createCookedPot(specialIngredient.getIngredients()));
        }
        for (int i = 0; i < exampleItems.size(); i++){
            widgets.addSlot(EmiIngredient.of(Ingredient.of(exampleItems.get(i).getItem())), 129, 5 + (18 * i));
        }

        widgets.addSlot(output.get(0), 90, 82).recipeContext(this);
    }

}
