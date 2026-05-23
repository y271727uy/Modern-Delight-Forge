package com.y271727uy.moderndelight.recipe.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.y271727uy.moderndelight.block.ModBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class SteamingRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final int maxProgress;
    
    public SteamingRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack itemStack, int maxProgress){
        this.id = id;
        this.output = itemStack;
        this.recipeItems = ingredients;
        this.maxProgress = maxProgress;
    }

    @Override
    public boolean matches(Container inventory, Level world) {
        if (world.isClientSide){
            return false;
        }
        return recipeItems.get(0).test(inventory.getItem(0));
    }
    
    public int getMaxProgress(){
        return this.maxProgress;
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
        return output.copy();
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
    
    public static class Type implements RecipeType<SteamingRecipe>{
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "steaming";
    }
    
    public static class Serializer implements RecipeSerializer<SteamingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation("moderndelight", "steaming");

        @Override
        public SteamingRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonArray ingredients = json.getAsJsonArray("ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }
            
            JsonObject outputJson = json.getAsJsonObject("output");
            ItemStack output = ShapedRecipe.itemStackFromJson(outputJson);
            int maxProgress = json.has("maxProgress") ? json.get("maxProgress").getAsInt() : 10;
            
            return new SteamingRecipe(id, inputs, output, maxProgress);
        }

        @Override
        public SteamingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            inputs.replaceAll(ignored -> Ingredient.fromNetwork(buf));
            ItemStack output = buf.readItem();
            int maxProgress = buf.readInt();
            return new SteamingRecipe(id, inputs, output, maxProgress);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, SteamingRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buf);
            }
            buf.writeItem(recipe.output);
            buf.writeInt(recipe.getMaxProgress());
        }
    }
}
