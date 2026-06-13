package com.y271727uy.moderndelight.compat.emi;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.compat.emi.recipe.*;
import com.y271727uy.moderndelight.recipe.custom.*;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;

@EmiEntrypoint
public class ModernDelightEMIPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(EMIMixWithWaterRecipe.CATEGORY);
        registry.addCategory(EMIWhiskingRecipe.CATEGORY);
        registry.addCategory(EMIAssemblyRecipe.CATEGORY);
        registry.addCategory(EMIBakingTrayRecipe.CATEGORY);
        registry.addCategory(EMIBiogasFermentationRecipe.CATEGORY);
        registry.addCategory(EMICuisineRecipe.CATEGORY);
        registry.addCategory(EMIDeepFryingRecipe.CATEGORY);
        registry.addCategory(EMIFreezingRecipe.CATEGORY);
        registry.addCategory(EMIBakingRecipe.CATEGORY);
        registry.addCategory(EMIPizzaRecipe.CATEGORY);
        registry.addCategory(EMISteamingRecipe.CATEGORY);
        registry.addCategory(EMISteamingElectricRecipe.CATEGORY);
        registry.addCategory(EMIAdvanceFurnaceRecipe.CATEGORY);
        registry.addCategory(EMIOvenRecipe.CATEGORY);
        registry.addCategory(EMIWoodenBasinRecipe.CATEGORY);
        registry.addCategory(EMIIceCreamRecipe.CATEGORY);
        registry.addCategory(EMIJuiceExtractingRecipe.CATEGORY);
        registry.addCategory(EMIInstantNoodlesMakingRecipe.CATEGORY);
        registry.addCategory(EMIGrindingRecipe.CATEGORY);

        registry.addWorkstation(EMIMixWithWaterRecipe.CATEGORY, EMIMixWithWaterRecipe.WORKSTATION);
        registry.addWorkstation(EMIWhiskingRecipe.CATEGORY, EMIWhiskingRecipe.WORKSTATION);
        registry.addWorkstation(EMIAssemblyRecipe.CATEGORY, EMIAssemblyRecipe.WORKSTATION);

        registry.addWorkstation(EMIBakingTrayRecipe.CATEGORY, EMIBakingTrayRecipe.WORKSTATION);
        registry.addWorkstation(EMIBakingTrayRecipe.CATEGORY, EmiStack.of(ModBlocks.GAS_CANISTER.get()));
        registry.addWorkstation(EMIBakingTrayRecipe.CATEGORY, EmiStack.of(ModBlocks.GAS_COOKING_STOVE.get()));

        registry.addWorkstation(EMIBiogasFermentationRecipe.CATEGORY, EMIBiogasFermentationRecipe.WORKSTATION);
        registry.addWorkstation(EMIBiogasFermentationRecipe.CATEGORY, EmiStack.of(ModBlocks.BIOGAS_DIGESTER_IO.get()));

        registry.addWorkstation(EMICuisineRecipe.CATEGORY, EMICuisineRecipe.WORKSTATION);

        registry.addWorkstation(EMIDeepFryingRecipe.CATEGORY, EMIDeepFryingRecipe.WORKSTATION);
        registry.addWorkstation(EMIDeepFryingRecipe.CATEGORY, EmiStack.of(ModBlocks.GAS_CANISTER.get()));

        registry.addWorkstation(EMIFreezingRecipe.CATEGORY, EMIFreezingRecipe.WORKSTATION);

        registry.addWorkstation(EMIBakingRecipe.CATEGORY, EMIBakingRecipe.WORKSTATION);
        registry.addWorkstation(EMIBakingRecipe.CATEGORY, EmiStack.of(ModBlocks.GAS_CANISTER.get()));
        registry.addWorkstation(EMIBakingRecipe.CATEGORY, EmiStack.of(ModBlocks.GAS_COOKING_STOVE.get()));

        registry.addWorkstation(EMISteamingElectricRecipe.CATEGORY, EmiStack.of(ModBlocks.ELECTRIC_STEAMER.get()));
        registry.addWorkstation(EMISteamingRecipe.CATEGORY, EmiStack.of(ModBlocks.BAMBOO_COVER.get()));
        registry.addWorkstation(EMISteamingRecipe.CATEGORY, EMISteamingRecipe.WORKSTATION);
        registry.addWorkstation(EMISteamingRecipe.CATEGORY, EmiStack.of(Blocks.CAULDRON));
        registry.addWorkstation(EMISteamingRecipe.CATEGORY, EmiStack.of(ModBlocks.GAS_CANISTER.get()));
        registry.addWorkstation(EMISteamingRecipe.CATEGORY, EmiStack.of(ModBlocks.GAS_COOKING_STOVE.get()));

        registry.addWorkstation(EMIWoodenBasinRecipe.CATEGORY, EMIWoodenBasinRecipe.WORKSTATION);
        registry.addWorkstation(EMIIceCreamRecipe.CATEGORY, EMIIceCreamRecipe.WORKSTATION);
        registry.addWorkstation(EMIJuiceExtractingRecipe.CATEGORY, EMIJuiceExtractingRecipe.WORKSTATION);
        registry.addWorkstation(EMIInstantNoodlesMakingRecipe.CATEGORY, EMIInstantNoodlesMakingRecipe.WORKSTATION);

        RecipeManager manager = registry.getRecipeManager();
        for (MixWithWaterRecipe recipe : manager.getAllRecipesFor(MixWithWaterRecipe.Type.INSTANCE)) {
            registry.addRecipe(new EMIMixWithWaterRecipe(recipe));
        }
        for (WhiskingRecipe recipe : manager.getAllRecipesFor(WhiskingRecipe.Type.INSTANCE)) {
            registry.addRecipe(new EMIWhiskingRecipe(recipe));
        }
        for (AssemblyRecipe recipe : manager.getAllRecipesFor(AssemblyRecipe.Type.INSTANCE)) {
            registry.addRecipe(new EMIAssemblyRecipe(recipe));
        }
        for (CampfireCookingRecipe recipe : manager.getAllRecipesFor(RecipeType.CAMPFIRE_COOKING)) {
            registry.addRecipe(new EMIBakingTrayRecipe(recipe));
        }
        registry.addRecipe(new EMIBiogasFermentationRecipe());
        for (CuisineRecipe recipe : manager.getAllRecipesFor(CuisineRecipe.Type.INSTANCE)) {
            registry.addRecipe(new EMICuisineRecipe(recipe));
        }
        for (DeepFryingRecipe recipe : manager.getAllRecipesFor(DeepFryingRecipe.Type.INSTANCE)) {
            registry.addRecipe(new EMIDeepFryingRecipe(recipe));
        }
        for (FreezingRecipe recipe : manager.getAllRecipesFor(FreezingRecipe.Type.INSTANCE)) {
            registry.addRecipe(new EMIFreezingRecipe(recipe));
        }
        for (BakingRecipe recipe : manager.getAllRecipesFor(BakingRecipe.Type.INSTANCE)) {
            registry.addRecipe(new EMIBakingRecipe(recipe));
        }
        registry.addRecipe(new EMIPizzaRecipe());
        for (SteamingRecipe recipe : manager.getAllRecipesFor(SteamingRecipe.Type.INSTANCE)) {
            registry.addRecipe(new EMISteamingRecipe(recipe));
            registry.addRecipe(new EMISteamingElectricRecipe(recipe));
        }
        registry.addRecipe(new EMIAdvanceFurnaceRecipe());
        registry.addRecipe(new EMIOvenRecipe());
        for (SqueezeRecipe recipe : manager.getAllRecipesFor(SqueezeRecipe.Type.INSTANCE)) {
            registry.addRecipe(new EMIWoodenBasinRecipe(recipe));
        }
        registry.addRecipe(new EMIIceCreamRecipe());
        for (JuiceExtractingRecipe recipe : manager.getAllRecipesFor(JuiceExtractingRecipe.Type.INSTANCE)) {
            registry.addRecipe(new EMIJuiceExtractingRecipe(recipe));
        }
        registry.addRecipe(new EMIInstantNoodlesMakingRecipe());
        for (GrindingRecipe recipe : manager.getAllRecipesFor(GrindingRecipe.Type.INSTANCE)) {
            registry.addRecipe(new EMIGrindingRecipe(recipe));
        }
    }
}
