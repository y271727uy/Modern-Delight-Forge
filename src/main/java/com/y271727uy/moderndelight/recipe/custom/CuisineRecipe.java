package com.y271727uy.moderndelight.recipe.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.y271727uy.moderndelight.block.ModBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public class CuisineRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient[] recipeItems;
    
    public CuisineRecipe(ResourceLocation id, Ingredient[] ingredients, ItemStack itemStack){
        this.id = id;
        this.output = itemStack;
        this.recipeItems = ingredients;
    }

    @Override
    public boolean matches(Container inventory, net.minecraft.world.level.Level level) {
        return recipeItems.length >= 2 &&
                recipeItems[0].test(inventory.getItem(0)) &&
                recipeItems[1].test(inventory.getItem(1));
    }

    @Override
    public ItemStack assemble(Container inventory, RegistryAccess registryManager) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryManager) {
        return output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        for (Ingredient ingredient : recipeItems) {
            list.add(ingredient);
        }
        return list;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    
    public static class Type implements RecipeType<CuisineRecipe>{
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID  = "cuisine";
    }
    
    public static class Serializer implements RecipeSerializer<CuisineRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation("moderndelight", "cuisine");

        @Override
        public CuisineRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonObject outputJson = GsonHelper.getAsJsonObject(json, "output");
            ItemStack output = ShapedRecipe.itemStackFromJson(outputJson);
            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");

            Ingredient[] inputs = new Ingredient[2];
            for(int i = 0; i < inputs.length; i++){
                inputs[i] = Ingredient.fromJson(ingredients.get(i));
            }

            return new CuisineRecipe(id, inputs, output);
        }

        @Override
        public CuisineRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int ingredientCount = buf.readInt();
            Ingredient[] inputs = new Ingredient[ingredientCount];
            for (int i = 0; i < ingredientCount; i++) {
                inputs[i] = Ingredient.fromNetwork(buf);
            }
            ItemStack output = buf.readItem();
            return new CuisineRecipe(id, inputs, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CuisineRecipe recipe) {
            buf.writeInt(recipe.recipeItems.length);
            for(Ingredient ingredient : recipe.recipeItems){
                ingredient.toNetwork(buf);
            }
            buf.writeItem(recipe.output);
        }
    }
}
