package com.y271727uy.moderndelight.recipe.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.util.FluidStack;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

public class SqueezeRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final ItemStack outputItem;
    private final NonNullList<Ingredient> ingredients;
    private final FluidStack outputFluid;
    private final boolean isDanger;
    private final boolean doCreateFire;

    public SqueezeRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack outputItem, FluidStack outputFluid, boolean isDanger, boolean doCreateFire){
        this.id = id;
        this.outputItem = outputItem;
        this.ingredients = ingredients;
        this.outputFluid = outputFluid;
        this.isDanger = isDanger;
        this.doCreateFire = doCreateFire;
    }

    @Override
    public boolean matches(Container inventory, Level level) {
        if (level.isClientSide) {
            return false;
        }
        return !ingredients.isEmpty() && ingredients.get(0).test(inventory.getItem(0));
    }

    public boolean isDanger() {
        return isDanger;
    }

    public boolean doCreateFire() {
        return doCreateFire;
    }

    @Override
    public ItemStack assemble(Container inventory, RegistryAccess registryAccess) {
        return outputItem.copy();
    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return outputItem.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public ItemStack getToastSymbol() {
        return ModBlocks.WOODEN_BASIN.get().asItem().getDefaultInstance();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SqueezeRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return SqueezeRecipe.Type.INSTANCE;
    }
    public static class Type implements RecipeType<SqueezeRecipe>{
        private Type() {}
        public static final SqueezeRecipe.Type INSTANCE = new SqueezeRecipe.Type();
        public static final String ID  = "squeeze";
    }
    public static class Serializer implements RecipeSerializer<SqueezeRecipe> {

        public static final SqueezeRecipe.Serializer INSTANCE = new SqueezeRecipe.Serializer();
        public static final String ID = "squeeze";

        public static FluidStack getFluidFromJson(JsonObject json){
            String string = GsonHelper.getAsString(json, "fluid");
            var fluid = BuiltInRegistries.FLUID.getOptional(ResourceLocation.tryParse(string)).orElseThrow(() -> new JsonSyntaxException("Unknown fluid '" + string + "'"));
            int i = GsonHelper.getAsInt(json, "amount", 9000);
            if (i < 1) {
                throw new JsonSyntaxException("Invalid fluid amount count: " + i);
            } else {
                return new FluidStack(new net.minecraftforge.fluids.FluidStack(fluid, i), i);
            }
        }
        @Override
        public SqueezeRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json,"output"));
            JsonArray ingredients = GsonHelper.getAsJsonArray(json,"ingredients");
            FluidStack fluid = getFluidFromJson(GsonHelper.getAsJsonObject(json,"fluid_output"));

            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for(int i=0;i<inputs.size();i++){
                inputs.set(i,Ingredient.fromJson(ingredients.get(i)));
            }
            boolean isDanger = GsonHelper.getAsBoolean(json,"is_danger",false);
            boolean doCreateFire = GsonHelper.getAsBoolean(json,"do_create_fire",false);
            return new SqueezeRecipe(id, inputs, output,fluid,isDanger,doCreateFire);
        }

        @Override
        public SqueezeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            inputs.replaceAll(ignored -> Ingredient.fromNetwork(buf));

            ItemStack output = buf.readItem();
            String string = buf.readUtf();
            var fluid = BuiltInRegistries.FLUID.getOptional(ResourceLocation.tryParse(string)).orElseThrow(() -> new JsonSyntaxException("Unknown fluid '" + string + "'"));
            int amount = buf.readInt();
            boolean isDanger = buf.readBoolean();
            boolean doCreateFire = buf.readBoolean();
            return new SqueezeRecipe(id, inputs, output, new FluidStack(new net.minecraftforge.fluids.FluidStack(fluid, amount), amount), isDanger, doCreateFire);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, SqueezeRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for(Ingredient ingredient : recipe.getIngredients()){
                ingredient.toNetwork(buf);
            }
            buf.writeItem(recipe.outputItem);
            var fluid = recipe.getOutputFluid().stack.getFluid();
            String string = BuiltInRegistries.FLUID.getKey(fluid).toString();
            buf.writeUtf(string);
            int amount = (int)recipe.getOutputFluid().getAmount();
            buf.writeInt(amount);
            buf.writeBoolean(recipe.isDanger);
            buf.writeBoolean(recipe.doCreateFire);
        }
    }
}
