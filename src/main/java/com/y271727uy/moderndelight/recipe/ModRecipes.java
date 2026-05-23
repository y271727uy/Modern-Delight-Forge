package com.y271727uy.moderndelight.recipe;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.recipe.custom.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = 
            DeferredRegister.create(ForgeRegistries.Keys.RECIPE_SERIALIZERS, ModernDelightMain.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = 
            DeferredRegister.create(ForgeRegistries.Keys.RECIPE_TYPES, ModernDelightMain.MOD_ID);

    // Baking Recipe
    public static final RegistryObject<RecipeSerializer<?>> BAKING_SERIALIZER = RECIPE_SERIALIZERS.register("oven_baking", 
            () -> BakingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeType<?>> BAKING_TYPE = RECIPE_TYPES.register("oven_baking",
            () -> BakingRecipe.Type.INSTANCE);

    // Whisking Recipe
    public static final RegistryObject<RecipeSerializer<?>> WHISKING_SERIALIZER = RECIPE_SERIALIZERS.register("whisking",
            () -> WhiskingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeType<?>> WHISKING_TYPE = RECIPE_TYPES.register("whisking",
            () -> WhiskingRecipe.Type.INSTANCE);

    // Freezing Recipe
    public static final RegistryObject<RecipeSerializer<?>> FREEZING_SERIALIZER = RECIPE_SERIALIZERS.register("freezing",
            () -> FreezingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeType<?>> FREEZING_TYPE = RECIPE_TYPES.register("freezing",
            () -> FreezingRecipe.Type.INSTANCE);

    // MixWithWater Recipe
    public static final RegistryObject<RecipeSerializer<?>> MIX_WITH_WATER_SERIALIZER = RECIPE_SERIALIZERS.register("mix_with_water",
            () -> MixWithWaterRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeType<?>> MIX_WITH_WATER_TYPE = RECIPE_TYPES.register("mix_with_water",
            () -> MixWithWaterRecipe.Type.INSTANCE);

    // DeepFrying Recipe
    public static final RegistryObject<RecipeSerializer<?>> DEEP_FRYING_SERIALIZER = RECIPE_SERIALIZERS.register("deep_frying",
            () -> DeepFryingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeType<?>> DEEP_FRYING_TYPE = RECIPE_TYPES.register("deep_frying",
            () -> DeepFryingRecipe.Type.INSTANCE);

    // Cuisine Recipe
    public static final RegistryObject<RecipeSerializer<?>> CUISINE_SERIALIZER = RECIPE_SERIALIZERS.register("cuisine",
            () -> CuisineRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeType<?>> CUISINE_TYPE = RECIPE_TYPES.register("cuisine",
            () -> CuisineRecipe.Type.INSTANCE);

    // Assembly Recipe
    public static final RegistryObject<RecipeSerializer<?>> ASSEMBLY_SERIALIZER = RECIPE_SERIALIZERS.register("assembly",
            () -> AssemblyRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeType<?>> ASSEMBLY_TYPE = RECIPE_TYPES.register("assembly",
            () -> AssemblyRecipe.Type.INSTANCE);

    // Steaming Recipe
    public static final RegistryObject<RecipeSerializer<?>> STEAMING_SERIALIZER = RECIPE_SERIALIZERS.register("steaming",
            () -> SteamingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeType<?>> STEAMING_TYPE = RECIPE_TYPES.register("steaming",
            () -> SteamingRecipe.Type.INSTANCE);

    // JuiceExtracting Recipe
    public static final RegistryObject<RecipeSerializer<?>> JUICE_EXTRACTING_SERIALIZER = RECIPE_SERIALIZERS.register("juice_extracting",
            () -> JuiceExtractingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeType<?>> JUICE_EXTRACTING_TYPE = RECIPE_TYPES.register("juice_extracting",
            () -> JuiceExtractingRecipe.Type.INSTANCE);

    // InstantNoodles Recipe
    public static final RegistryObject<RecipeSerializer<?>> INSTANT_NOODLES_SERIALIZER = RECIPE_SERIALIZERS.register("crafting_special_packaged_instant_noodles",
            () -> InstantNoodlesRecipe.Serializer.INSTANCE);

    // Grinding Recipe
    public static final RegistryObject<RecipeSerializer<?>> GRINDING_SERIALIZER = RECIPE_SERIALIZERS.register("grinding",
            () -> GrindingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeType<?>> GRINDING_TYPE = RECIPE_TYPES.register("grinding",
            () -> GrindingRecipe.Type.INSTANCE);

    // Squeeze Recipe
    public static final RegistryObject<RecipeSerializer<?>> SQUEEZE_SERIALIZER = RECIPE_SERIALIZERS.register("squeeze",
            () -> SqueezeRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeType<?>> SQUEEZE_TYPE = RECIPE_TYPES.register("squeeze",
            () -> SqueezeRecipe.Type.INSTANCE);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}
