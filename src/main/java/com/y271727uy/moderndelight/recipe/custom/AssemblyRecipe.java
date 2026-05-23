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

public class AssemblyRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient[] recipeItems;
    private final int mini_game_type;
    private final int goal;
    
    public AssemblyRecipe(ResourceLocation id, Ingredient[] ingredients, ItemStack output, int mini_game_type, int goal){
        this.id = id;
        this.output = output;
        this.recipeItems = ingredients;
        this.mini_game_type = mini_game_type;
        this.goal = goal;
    }
    
    @Override
    public boolean matches(Container inventory, net.minecraft.world.level.Level level) {
        return recipeItems.length >= 6 &&
                recipeItems[0].test(inventory.getItem(0)) &&
                recipeItems[1].test(inventory.getItem(1)) &&
                recipeItems[2].test(inventory.getItem(2)) &&
                recipeItems[3].test(inventory.getItem(3)) &&
                recipeItems[4].test(inventory.getItem(4)) &&
                recipeItems[5].test(inventory.getItem(5));
    }

    @Override
    public ItemStack assemble(Container inventory, RegistryAccess registryManager) {
        return output.copy();
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public int getMiniGameType() {
        return mini_game_type;
    }
    
    public int getGoal() {
        return goal;
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
        return AssemblyRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return AssemblyRecipe.Type.INSTANCE;
    }
    
    public static class Type implements RecipeType<AssemblyRecipe>{
        private Type() {}
        public static final AssemblyRecipe.Type INSTANCE = new AssemblyRecipe.Type();
        public static final String ID  = "assembly";
    }
    
    public static class Serializer implements RecipeSerializer<AssemblyRecipe> {
        public static final AssemblyRecipe.Serializer INSTANCE = new AssemblyRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation("moderndelight", "assembly");

        @Override
        public AssemblyRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonObject outputJson = GsonHelper.getAsJsonObject(json, "output");
            ItemStack output = ShapedRecipe.itemStackFromJson(outputJson);
            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            int type = GsonHelper.getAsInt(json, "mini_game_type", 0);
            int goal = GsonHelper.getAsInt(json, "goal", 0);
            
            Ingredient[] inputs = new Ingredient[6];
            for(int i = 0; i < inputs.length; i++){
                inputs[i] = Ingredient.fromJson(ingredients.get(i));
            }

            return new AssemblyRecipe(id, inputs, output, type, goal);
        }

        @Override
        public AssemblyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int ingredientCount = buf.readInt();
            Ingredient[] inputs = new Ingredient[ingredientCount];
            for (int i = 0; i < ingredientCount; i++) {
                inputs[i] = Ingredient.fromNetwork(buf);
            }
            ItemStack output = buf.readItem();
            int type = buf.readInt();
            int goal = buf.readInt();
            return new AssemblyRecipe(id, inputs, output, type, goal);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, AssemblyRecipe recipe) {
            buf.writeInt(recipe.recipeItems.length);
            for(Ingredient ingredient : recipe.recipeItems){
                ingredient.toNetwork(buf);
            }
            buf.writeItem(recipe.output);
            buf.writeInt(recipe.mini_game_type);
            buf.writeInt(recipe.goal);
        }
    }
}
