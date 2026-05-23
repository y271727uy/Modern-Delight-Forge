package com.y271727uy.moderndelight.recipe.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class JuiceExtractingRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    final int progress;
    final ItemStack output;
    final NonNullList<Ingredient> input;
    final Item container;
    public JuiceExtractingRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack itemStack, int progress, Item container){
        this.id = id;
        this.output = itemStack;
        this.input = ingredients;
        this.progress = progress;
        this.container = container;
    }
    @Override
    public boolean matches(Container inventory, Level world) {
        StackedContents recipeMatcher = new StackedContents();
        int i = 0;
        for(int j = 0; j < inventory.getContainerSize(); ++j) {
            ItemStack itemStack = inventory.getItem(j);
            if (!itemStack.isEmpty()) {
                ++i;
                recipeMatcher.accountStack(itemStack, 1);
            }
        }
        return i == this.input.size() && recipeMatcher.canCraft(this, null);
    }
    @Override
    public ItemStack assemble(Container inventory, RegistryAccess registryManager) {
        return output.copy();
    }

    @Override
    public ItemStack getToastSymbol() {
        return ModBlocks.JUICE_EXTRACTOR.get().asItem().getDefaultInstance();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }
    public int getProgress(){
        return progress;
    }

    public Item getContainer() {
        return container;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryManager) {
        return output;
    }
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return input;
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
    public static class Type implements RecipeType<JuiceExtractingRecipe>{
        private Type() {}
        public static final JuiceExtractingRecipe.Type INSTANCE = new JuiceExtractingRecipe.Type();
        public static final String ID  = "juice_extracting";
    }
    public static class Serializer implements RecipeSerializer<JuiceExtractingRecipe> {

        public static final JuiceExtractingRecipe.Serializer INSTANCE = new JuiceExtractingRecipe.Serializer();
        public static final String ID = "juice_extracting";

        @Override
        public JuiceExtractingRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json,"output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(json,"ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(4,Ingredient.EMPTY);
            int progress = GsonHelper.getAsInt(json,"progress",200);
            for(int i=0;i<inputs.size();i++){
                inputs.set(i,Ingredient.fromJson(ingredients.get(i)));
            }
            String tempContainer = GsonHelper.getAsString(json,"container");
            Item container = getItemFromString(tempContainer);
            return new JuiceExtractingRecipe(id, inputs, output, progress, container);
        }

        public static Item getItemFromString(String string) {
            Item item = Items.GLASS_BOTTLE;
            try {
                item = BuiltInRegistries.ITEM.get(new ResourceLocation(string));
            } catch (Exception ignored){
                ModernDelightMain.LOGGER.error("Unknown item '{}'", string);
            }
            return item;
        }

        @Override
        public JuiceExtractingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(),Ingredient.EMPTY);
            inputs.replaceAll(ignored -> Ingredient.fromNetwork(buf));
            ItemStack output = buf.readItem();
            int progress = (int)buf.readLong();
            Item container = getItemFromString(buf.readUtf());
            return new JuiceExtractingRecipe(id, inputs, output, progress, container);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, JuiceExtractingRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for(Ingredient ingredient : recipe.getIngredients()){
                ingredient.toNetwork(buf);
            }
            buf.writeItem(recipe.output);
            buf.writeLong(recipe.progress);
            buf.writeUtf(BuiltInRegistries.ITEM.getKey(recipe.container).toString());
        }
    }
}
