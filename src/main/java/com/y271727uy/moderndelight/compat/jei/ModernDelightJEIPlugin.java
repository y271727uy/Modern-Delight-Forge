package com.y271727uy.moderndelight.compat.jei;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.compat.jei.category.*;
import com.y271727uy.moderndelight.recipe.custom.*;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import com.y271727uy.moderndelight.screen.custom.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.constants.RecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

@JeiPlugin
public class ModernDelightJEIPlugin implements IModPlugin {

    private static final ResourceLocation UID = new ResourceLocation(ModernDelightMain.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MixWithWaterRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new WhiskingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AssemblyRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BakingTrayRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BiogasFermentationRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CuisineRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new DeepFryingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FreezingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BakingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new PizzaRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SteamingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SteamingElectricRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AdvanceFurnaceRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new OvenRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new WoodenBasinRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new IceCreamRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new JuiceExtractingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new InstantNoodlesMakingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new GrindingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

        registration.addRecipes(MixWithWaterRecipeCategory.TYPE, manager.getAllRecipesFor(MixWithWaterRecipe.Type.INSTANCE));
        registration.addRecipes(WhiskingRecipeCategory.TYPE, manager.getAllRecipesFor(WhiskingRecipe.Type.INSTANCE));
        registration.addRecipes(AssemblyRecipeCategory.TYPE, manager.getAllRecipesFor(AssemblyRecipe.Type.INSTANCE));
        registration.addRecipes(BakingTrayRecipeCategory.TYPE, manager.getAllRecipesFor(RecipeType.CAMPFIRE_COOKING));
        registration.addRecipes(BiogasFermentationRecipeCategory.TYPE, BiogasFermentationRecipeCategory.getRecipes());
        registration.addRecipes(CuisineRecipeCategory.TYPE, manager.getAllRecipesFor(CuisineRecipe.Type.INSTANCE));
        registration.addRecipes(DeepFryingRecipeCategory.TYPE, manager.getAllRecipesFor(DeepFryingRecipe.Type.INSTANCE));
        registration.addRecipes(FreezingRecipeCategory.TYPE, manager.getAllRecipesFor(FreezingRecipe.Type.INSTANCE));
        registration.addRecipes(BakingRecipeCategory.TYPE, manager.getAllRecipesFor(BakingRecipe.Type.INSTANCE));
        registration.addRecipes(PizzaRecipeCategory.TYPE, PizzaRecipeCategory.getRecipes());
        registration.addRecipes(SteamingRecipeCategory.TYPE, manager.getAllRecipesFor(SteamingRecipe.Type.INSTANCE));
        registration.addRecipes(SteamingElectricRecipeCategory.TYPE, manager.getAllRecipesFor(SteamingRecipe.Type.INSTANCE));
        registration.addRecipes(AdvanceFurnaceRecipeCategory.TYPE, AdvanceFurnaceRecipeCategory.getRecipes());
        registration.addRecipes(OvenRecipeCategory.TYPE, OvenRecipeCategory.getRecipes());
        registration.addRecipes(WoodenBasinRecipeCategory.TYPE, manager.getAllRecipesFor(SqueezeRecipe.Type.INSTANCE));
        registration.addRecipes(IceCreamRecipeCategory.TYPE, IceCreamRecipeCategory.getRecipes());
        registration.addRecipes(JuiceExtractingRecipeCategory.TYPE, manager.getAllRecipesFor(JuiceExtractingRecipe.Type.INSTANCE));
        registration.addRecipes(InstantNoodlesMakingRecipeCategory.TYPE, InstantNoodlesMakingRecipeCategory.getRecipes());
        registration.addRecipes(GrindingRecipeCategory.TYPE, manager.getAllRecipesFor(GrindingRecipe.Type.INSTANCE));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ElectriciansDeskScreen.class, 112, 24, 20, 20, AssemblyRecipeCategory.TYPE);
        registration.addRecipeClickArea(CuisineTableScreen.class, 75, 31, 24, 17, CuisineRecipeCategory.TYPE);
        registration.addRecipeClickArea(FreezerScreen.class, 154, 34, 12, 16, FreezingRecipeCategory.TYPE);
        registration.addRecipeClickArea(OvenScreen.class, 95, 21, 24, 17, BakingRecipeCategory.TYPE);
        registration.addRecipeClickArea(AdvanceFurnaceScreen.class, 43, 37, 126, 18, RecipeTypes.SMELTING);
        registration.addRecipeClickArea(ElectricSteamerScreen.class, 36, 8, 70, 70, SteamingElectricRecipeCategory.TYPE);
        registration.addRecipeClickArea(BambooSteamerScreen.class, 7, 25, 142, 34, SteamingRecipeCategory.TYPE);
        registration.addRecipeClickArea(IceCreamMakerScreen.class, 63, 22, 19, 42, IceCreamRecipeCategory.TYPE);
        registration.addRecipeClickArea(WoodenBasinScreen.class, 46, 20, 20, 49, WoodenBasinRecipeCategory.TYPE);
        registration.addRecipeClickArea(DeepFryerScreen.class, 54, 40, 72, 8, DeepFryingRecipeCategory.TYPE);
        registration.addRecipeClickArea(BiogasDigesterIOScreen.class, 64, 36, 18, 18, BiogasFermentationRecipeCategory.TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        // Assembly recipes have six material slots plus paper and ink.
        registration.addRecipeTransferHandler(ElectriciansDeskScreenHandler.class, ModScreenHandlers.ELECTRICIANS_DESK_SCREEN_HANDLER.get(), AssemblyRecipeCategory.TYPE, 0, 8, 9, 36);
        registration.addRecipeTransferHandler(CuisineTableScreenHandler.class, ModScreenHandlers.CUISINE_TABLE_SCREEN_HANDLER.get(), CuisineRecipeCategory.TYPE, 0, 2, 3, 36);
        registration.addRecipeTransferHandler(FreezerScreenHandler.class, ModScreenHandlers.FREEZER_SCREEN_HANDLER.get(), FreezingRecipeCategory.TYPE, 0, 3, 5, 36);
        registration.addRecipeTransferHandler(OvenScreenHandler.class, ModScreenHandlers.OVEN_SCREEN_HANDLER.get(), BakingRecipeCategory.TYPE, 0, 4, 6, 36);
        registration.addRecipeTransferHandler(ElectricSteamerScreenHandler.class, ModScreenHandlers.ELECTRIC_STEAMER_SCREEN_HANDLER.get(), SteamingElectricRecipeCategory.TYPE, 0, 12, 13, 36);
        registration.addRecipeTransferHandler(BambooSteamerScreenHandler.class, ModScreenHandlers.BAMBOO_STEAMER_SCREEN_HANDLER.get(), SteamingRecipeCategory.TYPE, 0, 16, 16, 36);
    }
}
