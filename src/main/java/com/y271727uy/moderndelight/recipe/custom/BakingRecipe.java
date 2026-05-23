package com.y271727uy.moderndelight.recipe.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.y271727uy.moderndelight.block.ModBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;

public class BakingRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    public BakingRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack itemStack){
        this.id = id;
        this.output = itemStack;
        this.recipeItems = ingredients;
    }

    @Override
    public boolean matches(Container inventory, Level world) {
        if (world.isClientSide){
            return false;
        }
        if (inventory.getContainerSize() < 4) return false;
        return recipeItems.get(0).test(inventory.getItem(0)) &&
                recipeItems.get(1).test(inventory.getItem(1)) &&
                recipeItems.get(2).test(inventory.getItem(2)) &&
                recipeItems.get(3).test(inventory.getItem(3));
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
        return recipeItems;
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
    public static class Type implements RecipeType<BakingRecipe>{
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID  = "oven_baking";
    }
    public static class Serializer implements RecipeSerializer<BakingRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "oven_baking";

        @Override
        public BakingRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json,"output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(json,"ingredients");

            NonNullList<Ingredient> inputs = NonNullList.withSize(4, Ingredient.EMPTY);

            for(int i=0;i<inputs.size();i++){
                inputs.set(i,Ingredient.fromJson(ingredients.get(i)));
            }

            return new BakingRecipe(id, inputs, output);
        }

        @Override
        public @Nullable BakingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(),Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new BakingRecipe(id, inputs, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BakingRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for(Ingredient ingredient : recipe.getIngredients()){
                ingredient.toNetwork(buf);
            }
            buf.writeItem(recipe.output);
        }
    }
}
