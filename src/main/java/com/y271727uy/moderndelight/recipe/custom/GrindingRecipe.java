package com.y271727uy.moderndelight.recipe.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.y271727uy.moderndelight.item.ModItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.NonNullList;

import java.util.ArrayList;
import java.util.List;

public class GrindingRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final ItemStack chancedOutput;
    private final float chance;
    private final NonNullList<Ingredient> recipeItems;
    public GrindingRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack output, ItemStack chancedOutput, float chance){
        this.id = id;
        this.output = output;
        this.recipeItems = ingredients;
        this.chancedOutput = chancedOutput;
        this.chance = chance;
    }

    public float getChance() {
        return chance;
    }

    public ItemStack getChancedOutput() {
        return chancedOutput.copy();
    }

    @Override
    public boolean matches(Container inventory, Level level) {
        return recipeItems.get(0).test(inventory.getItem(0));
    }

    @Override
    public ItemStack assemble(Container inventory, RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ItemStack getToastSymbol() {
        return ModItems.STONE_MORTAR.get().getDefaultInstance();
    }
    public ItemStack getOutput(RegistryAccess registryAccess) {
        return output.copy();
    }

    public List<ItemStack> getOutputs(){
        List<ItemStack> list = new ArrayList<>(2);
        list.add(output.copy());
        list.add(chancedOutput.copy());
        return list;
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
    public static class Type implements RecipeType<GrindingRecipe>{
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID  = "grinding";
    }
    public static class Serializer implements RecipeSerializer<GrindingRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "grinding";

        @Override
        public GrindingRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = outputFromJson(GsonHelper.getAsJsonObject(json,"output"));
            ItemStack chancedOutput = outputFromJson(GsonHelper.getAsJsonObject(json,"extra"));
            float chance = GsonHelper.getAsFloat(json,"chance");
            JsonArray ingredients = GsonHelper.getAsJsonArray(json,"ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for(int i=0;i<inputs.size();i++){
                inputs.set(i,Ingredient.fromJson(ingredients.get(i)));
            }

            return new GrindingRecipe(id, inputs, output,chancedOutput,chance);
        }
        public static ItemStack outputFromJson(JsonObject json) {
            Item item = getItem(json);
            if (json.has("data")) {
                throw new JsonParseException("Disallowed data tag found");
            } else {
                int i = GsonHelper.getAsInt(json, "count", 1);
                if (i < 1) {
                    throw new JsonSyntaxException("Invalid output count: " + i);
                } else {
                    if (item == Items.AIR){
                        return ItemStack.EMPTY;
                    } else return new ItemStack(item, i);
                }
            }
        }

        public static Item getItem(JsonObject json) {
            String string = GsonHelper.getAsString(json, "item");
            return BuiltInRegistries.ITEM.getOptional(ResourceLocation.tryParse(string)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + string + "'"));
        }

        @Override
        public GrindingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            ItemStack chancedOutput = buf.readItem();
            float chance = buf.readFloat();
            return new GrindingRecipe(id, inputs, output, chancedOutput, chance);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GrindingRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for(Ingredient ingredient : recipe.getIngredients()){
                ingredient.toNetwork(buf);
            }
            buf.writeItem(recipe.output);
            buf.writeItem(recipe.chancedOutput);
            buf.writeFloat(recipe.chance);
        }
    }
}
