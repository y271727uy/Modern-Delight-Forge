package com.y271727uy.moderndelight.compat.jei;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.compat.jei.category.*;
import com.y271727uy.moderndelight.recipe.custom.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
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
}
