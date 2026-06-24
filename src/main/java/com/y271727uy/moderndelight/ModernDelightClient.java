package com.y271727uy.moderndelight;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.food.fish_and_chips.FishAndChipsBlockEntityRender;
import com.y271727uy.moderndelight.block.kitchenware.GlassBowlBlockEntityRenderer;
import com.y271727uy.moderndelight.block.kitchenware.decor.KitchenUtensilHolderBlockEntityRender;
import com.y271727uy.moderndelight.block.kitchenware.decor.WoodenPlateBlockEntityRenderer;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.BakingTrayBlockEntityRenderer;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying.DeepFryBasketBlockEntityRenderer;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying.DeepFryerBlockEntityRenderer;
import com.y271727uy.moderndelight.block.kitchenware.FreezerBlockEntityRenderer;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying.WoodenBasinBlockEntityRenderer;
import com.y271727uy.moderndelight.block.kitchenware.ice_cream_maker.IceCreamMakerBlockEntityRender;
import com.y271727uy.moderndelight.block.kitchenware.juice_extractor.JuiceExtractorBlockEntityRender;
import com.y271727uy.moderndelight.block.kitchenware.steaming.BambooGrateBlockEntityRenderer;
import com.y271727uy.moderndelight.block.kitchenware.steaming.ElectricSteamerBlockEntityRenderer;
import com.y271727uy.moderndelight.block.power.ChargingPostBlockEntityRenderer;
import com.y271727uy.moderndelight.block.power.alternator.thermal_power.SterlingEngineBlockEntityRender;
import com.y271727uy.moderndelight.block.power.alternator.wind_power.FanBladeBlockEntityRender;
import com.y271727uy.moderndelight.entity.ModEntities;
import com.y271727uy.moderndelight.fluid.ModFluid;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.networking.NetworkHandler;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import com.y271727uy.moderndelight.screen.custom.*;
import com.y271727uy.moderndelight.util.enums.SpecialIngredient;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ModernDelightMain.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unused")
public class ModernDelightClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            NetworkHandler.registerS2CPacket();
            registerModelPredicateProviders();
            registerRenderLayers();
            registerMenuScreens();
        });
    }

    @SuppressWarnings("unchecked")
    private static void registerMenuScreens() {
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.OVEN_SCREEN_HANDLER.get(), OvenScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.FREEZER_SCREEN_HANDLER.get(), FreezerScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.ADVANCE_FURNACE_SCREEN_HANDLER.get(), AdvanceFurnaceScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.WOODEN_BASIN_SCREEN_HANDLER.get(), WoodenBasinScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.GAS_CANISTER_SCREEN_HANDLER.get(), GasCanisterScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.BIOGAS_DIGESTER_CONTROLLER_SCREEN_HANDLER.get(), BiogasDigesterControllerScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.BIOGAS_DIGESTER_IO_SCREEN_HANDLER.get(), BiogasDigesterIOScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.DEEP_FRYER_SCREEN_HANDLER.get(), DeepFryerScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.CUISINE_TABLE_SCREEN_HANDLER.get(), CuisineTableScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.CABINET_SCREEN_HANDLER.get(), CabinetScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.PHOTOVOLTAIC_GENERATOR_SCREEN_HANDLER.get(), PhotovoltaicGeneratorScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.ACDC_CONVERTER_SCREEN_HANDLER.get(), ACDCConverterScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.WIND_TURBINE_CONTROLLER_SCREEN_HANDLER.get(), WindTurbineControllerScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.FARADAY_GENERATOR_SCREEN_HANDLER.get(), FaradayGeneratorScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.TESLA_COIL_SCREEN_HANDLER.get(), TeslaCoilScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.ELECTRICIANS_DESK_SCREEN_HANDLER.get(), ElectriciansDeskScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.BAMBOO_STEAMER_SCREEN_HANDLER.get(), BambooSteamerScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.ELECTRIC_STEAMER_SCREEN_HANDLER.get(), ElectricSteamerScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.ICE_CREAM_MAKER_SCREEN_HANDLER.get(), IceCreamMakerScreen::new);
        net.minecraft.client.gui.screens.MenuScreens.register(ModScreenHandlers.CHARGING_POST_SCREEN_HANDLER.get(), ChargingPostScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BUTTER.get(), context -> new net.minecraft.client.renderer.entity.ThrownItemRenderer<>(context));
        event.registerEntityRenderer(ModEntities.CHERRY_BOMB.get(), context -> new net.minecraft.client.renderer.entity.ThrownItemRenderer<>(context));
        event.registerBlockEntityRenderer(ModBlockEntities.GLASS_BOWL_ENTITY.get(), GlassBowlBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FREEZER_ENTITY.get(), FreezerBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BAKING_TRAY_BLOCK_ENTITY.get(), BakingTrayBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WOODEN_BASIN_BLOCK_ENTITY.get(), WoodenBasinBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DEEP_FRYER_BLOCK_ENTITY.get(), DeepFryerBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FAN_BLADE_BLOCK_ENTITY.get(), FanBladeBlockEntityRender::new);
        event.registerBlockEntityRenderer(ModBlockEntities.KITCHEN_UTENSIL_HOLDER_BLOCK_ENTITY.get(), KitchenUtensilHolderBlockEntityRender::new);
        event.registerBlockEntityRenderer(ModBlockEntities.STERLING_ENGINE_BLOCK_ENTITY.get(), SterlingEngineBlockEntityRender::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BAMBOO_GRATE_BLOCK_ENTITY.get(), BambooGrateBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ICE_CREAM_MAKER_BLOCK_ENTITY.get(), IceCreamMakerBlockEntityRender::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FISH_AND_CHIPS_BLOCK_ENTITY.get(), FishAndChipsBlockEntityRender::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CHARGING_POST_BLOCK_ENTITY.get(), ChargingPostBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ELECTRIC_STEAMER_BLOCK_ENTITY.get(), ElectricSteamerBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.JUICE_EXTRACTOR_BLOCK_ENTITY.get(), JuiceExtractorBlockEntityRender::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DEEP_FRY_BASKET_BLOCK_ENTITY.get(), DeepFryBasketBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WOODEN_PLATE_BLOCK_ENTITY.get(), WoodenPlateBlockEntityRenderer::new);
    }

    private static void registerRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GLASS_BOWL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DEEP_FRYER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WOODEN_PLATE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLACK_PEPPER_CROP.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_PEPPER_CROP.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GARLIC_CROP.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_GARLIC.get(), RenderType.cutout());
    }

    public static void registerModelPredicateProviders() {
        ItemProperties.register(ModItems.PORTABLE_POT.get(), new ResourceLocation(ModernDelightMain.MOD_ID, "pot_state"), (itemStack, clientWorld, livingEntity, seed) -> {
            if (hasNoodle(itemStack) || hasWater(itemStack) || hasQuicklime(itemStack)){
                return 0.1F;
            }
            return 0.0F;
        });
        ItemProperties.register(ModItems.COOKED_PORTABLE_POT.get(), new ResourceLocation(ModernDelightMain.MOD_ID, "noodle_type"), (itemStack, clientWorld, livingEntity, seed) -> {
            SpecialIngredient specialIngredient = getNoodleType(itemStack);
            if (specialIngredient != null){
                return switch (specialIngredient){
                    case STEW_CHICKEN_NOODLE_WITH_MUSHROOM -> 0.1F;
                    case BRAISED_BEEF_NOODLE_SOUP -> 0.2F;
                    case TONKOTSU_RAMEN -> 0.3F;
                };
            }
            if (isUnhealthy(itemStack)){
                return 0.9F;
            }
            return 0.0F;
        });
    }

    private static boolean hasNoodle(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("noodles_data");
    }

    private static boolean hasWater(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("has_water") && stack.getTag().getBoolean("has_water");
    }

    private static boolean hasQuicklime(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("has_quicklime") && stack.getTag().getBoolean("has_quicklime");
    }

    private static SpecialIngredient getNoodleType(ItemStack stack) {
        java.util.List<Item> ingredients = getWorldNoodleItems(stack);
        if (ingredients == null) {
            return null;
        }
        if (containsAnyTagged(ingredients, com.y271727uy.moderndelight.tag.TagKeys.RAW_BEEF) && containsAnyItem(ingredients, Items.CARROT) && containsAnyTagged(ingredients, com.y271727uy.moderndelight.tag.TagKeys.CABBAGE)) {
            return SpecialIngredient.BRAISED_BEEF_NOODLE_SOUP;
        }
        if (containsAnyItem(ingredients, Items.BROWN_MUSHROOM) && containsAnyItem(ingredients, Items.CHICKEN) && containsAnyTagged(ingredients, com.y271727uy.moderndelight.tag.TagKeys.CABBAGE)) {
            return SpecialIngredient.STEW_CHICKEN_NOODLE_WITH_MUSHROOM;
        }
        if (containsAnyTagged(ingredients, com.y271727uy.moderndelight.tag.TagKeys.RAW_PORK) && containsAnyItem(ingredients, Items.DRIED_KELP) && containsAnyItem(ingredients, Items.EGG)) {
            return SpecialIngredient.TONKOTSU_RAMEN;
        }
        return null;
    }

    private static boolean isUnhealthy(ItemStack stack) {
        if (getNoodleType(stack) != null) {
            return false;
        }
        java.util.List<Item> ingredients = getWorldNoodleItems(stack);
        if (ingredients != null) {
            for (Item item : ingredients) {
                if (!item.isEdible()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static java.util.List<Item> getWorldNoodleItems(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains("noodles_data")) {
            return null;
        }
        net.minecraft.nbt.CompoundTag noodleTag = stack.getTag().getCompound("noodles_data");
        ItemStack noodles = ItemStack.of(noodleTag);
        if (noodles.getTag() == null || !noodles.getTag().contains("instant_noodles_ingredients")) {
            return null;
        }
        net.minecraft.nbt.CompoundTag ingredientTag = noodles.getTag().getCompound("instant_noodles_ingredients");
        java.util.List<Item> items = new java.util.ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            if (ingredientTag.contains("ingredients_" + i)) {
                String registerKey = ingredientTag.getString("ingredients_" + i);
                if (!registerKey.isEmpty()) {
                    ResourceLocation id = ResourceLocation.tryParse(registerKey);
                    if (id != null) {
                        Item item = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(id);
                        if (item != Items.AIR) {
                            items.add(item);
                        }
                    }
                }
            }
        }
        return items.isEmpty() ? null : items;
    }

    private static boolean containsAnyItem(java.util.List<Item> items, Item required) {
        for (Item item : items) {
            if (item == required) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsAnyTagged(java.util.List<Item> items, TagKey<Item> tag) {
        for (Item item : items) {
            if (new ItemStack(item).is(tag)) {
                return true;
            }
        }
        return false;
    }
}
