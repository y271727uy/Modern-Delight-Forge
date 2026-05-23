package com.y271727uy.moderndelight.util.enums;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.tag.TagKeys;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.List;

public enum SpecialIngredient{
    BRAISED_BEEF_NOODLE_SOUP("braised_beef_noodle_soup",
            Ingredient.of(TagKeys.RAW_BEEF),
            Ingredient.of(Items.CARROT),
            Ingredient.of(TagKeys.CABBAGE)),
    STEW_CHICKEN_NOODLE_WITH_MUSHROOM("stewed_chicken_noodle_with_mushroom",
            Ingredient.of(Items.BROWN_MUSHROOM),
            Ingredient.of(Items.CHICKEN),
            Ingredient.of(TagKeys.CABBAGE)),
    TONKOTSU_RAMEN("tonkotsu_ramen",
            Ingredient.of(TagKeys.RAW_PORK),
            Ingredient.of(Items.DRIED_KELP),
            Ingredient.of(Items.EGG));
    final String id;
    final List<Ingredient> ingredients;
    SpecialIngredient(String id, Ingredient... ingredients) {
        this.id = id;
        this.ingredients = Arrays.asList(ingredients);
    }

    public String getId() {
        return id;
    }
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    public String toTranslationKey(){
        return "items." + ModernDelightMain.MOD_ID + ".packaged_instant_noodles." + id;
    }
    public boolean match(SimpleContainer match){
        for (Ingredient ingredient : ingredients) {
            boolean foundMatchForIngredient = false;
            ItemStack[] ingredientItems = ingredient.getItems();
            for (int i = 0; i < match.getContainerSize(); i++) {
                ItemStack stack = match.getItem(i);
                if (stack.isEmpty()) {
                    continue;
                }
                Item item = stack.getItem();
                for (ItemStack ingredientItem : ingredientItems) {
                    if (item == ingredientItem.getItem()) {
                        foundMatchForIngredient = true;
                        break;
                    }
                }
                if (foundMatchForIngredient) {
                    break;
                }
            }
            if (!foundMatchForIngredient) {
                return false;
            }
        }
        return true;
    }
}
