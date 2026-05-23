package com.y271727uy.moderndelight.item;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModItemGroups {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
            DeferredRegister.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, ModernDelightMain.MOD_ID);
    
    public static final RegistryObject<CreativeModeTab> ITEM_GROUP = CREATIVE_MODE_TABS.register("bakingdelight_itemgroup",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemgroup.moderndelight"))
                    .icon(() -> new ItemStack(ModBlocks.GLASS_BOWL.get()))
                    .displayItems((displayContext, output) -> {
                        // Whisks & Glass Bowl
                        addToCreativeTab(output, ModItems.ELECTRIC_WHISK.get());
                        addToCreativeTab(output, ModItems.IRON_WHISK.get());
                        addToCreativeTab(output, ModBlocks.GLASS_BOWL_ITEM.get());
                        // Amethyst Tools & Knifes & Stir-frying
                        addToCreativeTab(output, ModItems.AMETHYST_KNIFE.get());
                        addToCreativeTab(output, ModItems.COPPER_KNIFE.get());
                        addToCreativeTab(output, ModItems.SPATULA.get());
                        addToCreativeTab(output, ModBlocks.BAKING_TRAY.get().asItem());
                        addToCreativeTab(output, ModItems.STONE_MORTAR.get());
                        // Pizza Making & Oven
                        addToCreativeTab(output, ModItems.KNEADING_STICK.get());
                        addToCreativeTab(output, ModBlocks.WHEAT_DOUGH_ITEM.get());
                        addToCreativeTab(output, ModBlocks.PIZZA_WIP_ITEM.get());
                        addToCreativeTab(output, ModBlocks.RAW_PIZZA_ITEM.get());
                        addToCreativeTab(output, ModBlocks.PIZZA_ITEM.get());
                        addToCreativeTab(output, ModItems.CHEESE.get());
                        addToCreativeTab(output, ModBlocks.ADVANCE_FURNACE.get().asItem());
                        addToCreativeTab(output, ModBlocks.OVEN.get().asItem());
                        addToCreativeTab(output, ModItems.CROWBAR.get());
                        // Oil Extraction
                        addToCreativeTab(output, ModItems.FILTER.get());
                        addToCreativeTab(output, ModBlocks.WOODEN_BASIN.get().asItem());
                        addToCreativeTab(output, ModItems.VEGETABLE_OIL_BOTTLE.get());
                        addToCreativeTab(output, ModItems.VEGETABLE_OIL_BUCKET.get());
                        addToCreativeTab(output, ModItems.SWEETENED_WATER_BUCKET.get());
                        addToCreativeTab(output, ModItems.OIL_IMPURITY.get());
                        addToCreativeTab(output, ModBlocks.DEEP_FRYER.get().asItem());
                        addToCreativeTab(output, ModBlocks.DEEP_FRY_BASKET_ITEM.get());
                        addToCreativeTab(output, ModItems.HOLDER.get());
                        // Gas System
                        addToCreativeTab(output, ModBlocks.BIOGAS_DIGESTER_CONTROLLER.get().asItem());
                        addToCreativeTab(output, ModBlocks.BIOGAS_DIGESTER_IO.get().asItem());
                        addToCreativeTab(output, ModBlocks.GAS_CANISTER_ITEM.get());
                        addToCreativeTab(output, ModBlocks.GAS_COOKING_STOVE.get().asItem());
                        addToCreativeTab(output, ModItems.LIQUEFIED_BIOGAS_BUCKET.get());
                        // Power System
                        addToCreativeTab(output, ModBlocks.ELECTRICIANS_DESK.get().asItem());
                        addToCreativeTab(output, ModBlocks.PHOTOVOLTAIC_GENERATOR.get().asItem());
                        addToCreativeTab(output, ModBlocks.FAN_BLADE_ITEM.get());
                        addToCreativeTab(output, ModBlocks.WIND_TURBINE_CONTROLLER.get().asItem());
                        addToCreativeTab(output, ModBlocks.STERLING_ENGINE_ITEM.get());
                        addToCreativeTab(output, ModBlocks.FARADAY_GENERATOR.get().asItem());
                        addToCreativeTab(output, ModBlocks.AC_DC_CONVERTER.get().asItem());
                        addToCreativeTab(output, ModBlocks.TESLA_COIL.get().asItem());
                        addToCreativeTab(output, ModBlocks.SIMPLE_BATTERY_ITEM.get());
                        addToCreativeTab(output, ModBlocks.INTERMEDIATE_BATTERY_ITEM.get());
                        addToCreativeTab(output, ModBlocks.ADVANCE_BATTERY_ITEM.get());
                        addToCreativeTab(output, ModBlocks.DIMENSION_BATTERY_ITEM.get());
                        addToCreativeTab(output, ModBlocks.CHARGING_POST.get().asItem());
                        // Materials
                        addToCreativeTab(output, ModItems.SILICON_INGOT.get());
                        addToCreativeTab(output, ModBlocks.SILICON_BLOCK.get().asItem());
                        addToCreativeTab(output, ModItems.REDSTONE_COMPONENT.get());
                        addToCreativeTab(output, ModItems.SILICON_COMPONENT.get());
                        addToCreativeTab(output, ModItems.DIAMOND_COMPONENT.get());
                        // Freezer
                        addToCreativeTab(output, ModBlocks.FREEZER.get().asItem());
                        addToCreativeTab(output, ModItems.ICE_BRICK.get());
                        // Steamer
                        addToCreativeTab(output, ModBlocks.BAMBOO_GRATE.get().asItem());
                        addToCreativeTab(output, ModBlocks.BAMBOO_COVER.get().asItem());
                        addToCreativeTab(output, ModBlocks.ELECTRIC_STEAMER.get().asItem());
                        // Ice Cream
                        addToCreativeTab(output, ModBlocks.ICE_CREAM_MAKER_ITEM.get());
                        addToCreativeTab(output, ModItems.RAW_ICE_CREAM_CONE.get());
                        addToCreativeTab(output, ModItems.ICE_CREAM_CONE.get());
                        addToCreativeTab(output, ModItems.ICE_CREAM.get());
                        // Juice
                        addToCreativeTab(output, ModBlocks.JUICE_EXTRACTOR_ITEM.get());
                        addToCreativeTab(output, ModItems.TOMATO_JUICE.get());
                        addToCreativeTab(output, ModItems.SWEET_BERRIES_JUICE.get());
                        addToCreativeTab(output, ModBlocks.GLASS_CUP_ITEM.get());
                        addToCreativeTab(output, ModBlocks.CHERRY_MILK_TEA_ITEM.get());
                        addToCreativeTab(output, ModBlocks.ROSE_ICE_TEA_TEA_ITEM.get());
                        addToCreativeTab(output, ModItems.MANGO_MILK_TEA.get());
                        addToCreativeTab(output, ModItems.SEA_SALT_LEMON.get());
                        // Cuisine & Storage
                        addToCreativeTab(output, ModBlocks.CUISINE_TABLE.get().asItem());
                        addToCreativeTab(output, ModBlocks.DEEPSLATE_CABINET.get().asItem());
                        addToCreativeTab(output, ModBlocks.GRANITE_CABINET.get().asItem());
                        addToCreativeTab(output, ModBlocks.DIORITE_CABINET.get().asItem());
                        addToCreativeTab(output, ModBlocks.ANDESITE_CABINET.get().asItem());
                        addToCreativeTab(output, ModBlocks.OBSIDIAN_CABINET.get().asItem());
                        addToCreativeTab(output, ModBlocks.BLACKSTONE_CABINET.get().asItem());
                        addToCreativeTab(output, ModBlocks.BASALT_CABINET.get().asItem());
                        // Building
                        addToCreativeTab(output, ModBlocks.KITCHEN_UTENSIL_HOLDER.get().asItem());
                        addToCreativeTab(output, ModBlocks.WOODEN_PLATE.get().asItem());
                        // Instant Noodles
                        addToCreativeTab(output, ModItems.RAW_NOODLES.get());
                        addToCreativeTab(output, ModItems.FRIED_NOODLES.get());
                        addToCreativeTab(output, ModItems.MULTIFUNCTIONAL_WRAPPING_PAPER.get());
                        addToCreativeTab(output, ModItems.DIRTY_WRAPPING_PAPER.get());
                        addToCreativeTab(output, ModItems.PACKAGED_INSTANT_NOODLES.get());
                        addToCreativeTab(output, ModItems.PORTABLE_POT.get());
                        addToCreativeTab(output, ModItems.COOKED_PORTABLE_POT.get());
                        addToCreativeTab(output, ModItems.QUICKLIME.get());
                        // Misc
                        addToCreativeTab(output, ModItems.ANCIENT_SCRAP.get());
                        addToCreativeTab(output, ModItems.HONEY_CRYSTALLIZATION.get());
                        addToCreativeTab(output, ModItems.MATCHA.get());
                        // Jars
                        addToCreativeTab(output, ModItems.JAR.get());
                        addToCreativeTab(output, ModItems.CARAMEL.get());
                        addToCreativeTab(output, ModItems.CHOCOLATE_SAUCE.get());
                        addToCreativeTab(output, ModItems.SEA_SALT.get());
                        // Truffles
                        addToCreativeTab(output, ModItems.BLACK_TRUFFLE.get());
                        addToCreativeTab(output, ModItems.WHITE_TRUFFLE.get());
                        // Wild Crops
                        addToCreativeTab(output, ModBlocks.WILD_PEPPER_CROP.get().asItem());
                        addToCreativeTab(output, ModBlocks.WILD_GARLIC.get().asItem());
                        // Black Pepper
                        addToCreativeTab(output, ModItems.BLACK_PEPPER_CORN.get());
                        addToCreativeTab(output, ModItems.BLACK_PEPPER_DUST.get());
                        // Garlic
                        addToCreativeTab(output, ModItems.GARLIC.get());
                        addToCreativeTab(output, ModItems.GARLIC_PETAL.get());
                        addToCreativeTab(output, ModItems.GARLIC_PUREE.get());
                        // Starch
                        addToCreativeTab(output, ModItems.POTATO_STARCH.get());
                        addToCreativeTab(output, ModItems.WHEAT_FLOUR.get());
                        addToCreativeTab(output, ModItems.MIXED_DOUGH.get());
                        // Potato
                        addToCreativeTab(output, ModItems.MASHED_POTATO.get());
                        addToCreativeTab(output, ModItems.SAUCE_MASHED_POTATO.get());
                        addToCreativeTab(output, ModBlocks.MASHED_POTATO_BLOCK.get().asItem());
                        addToCreativeTab(output, ModItems.RAW_POTATO_CHIP.get());
                        addToCreativeTab(output, ModItems.POTATO_CHIP.get());
                        addToCreativeTab(output, ModItems.POTATO_CHIPS.get());
                        addToCreativeTab(output, ModItems.DEEP_FRIED_POTATO_CHIPS.get());
                        addToCreativeTab(output, ModItems.FRENCH_FRIES.get());
                        addToCreativeTab(output, ModItems.PACKAGING_BAG.get());
                        addToCreativeTab(output, ModItems.DIRTY_PACKAGING_BAG.get());
                        // Chicken
                        addToCreativeTab(output, ModItems.RAW_CHICKEN_FILLET.get());
                        addToCreativeTab(output, ModItems.CHICKEN_FILLET.get());
                        // Snacks
                        addToCreativeTab(output, ModItems.APPLE_PETAL.get());
                        addToCreativeTab(output, ModItems.FRIED_APPLE.get());

                        addToCreativeTab(output, ModItems.RAW_ONION_RING.get());
                        addToCreativeTab(output, ModItems.ONION_RING.get());

                        addToCreativeTab(output, ModItems.FRIED_MILK_WIP.get());
                        addToCreativeTab(output, ModItems.FRIED_MILK.get());

                        addToCreativeTab(output, ModItems.CHEESE_BALL.get());
                        addToCreativeTab(output, ModItems.FRIED_DOUGH_STICK.get());
                        addToCreativeTab(output, ModItems.STEAMED_BUN.get());
                        addToCreativeTab(output, ModItems.DEEP_FRIED_BUN.get());

                        addToCreativeTab(output, ModItems.CHERRY.get());
                        addToCreativeTab(output, ModItems.CHERRY_BOMB.get());
                        addToCreativeTab(output, ModBlocks.BOXED_CHERRIES.get().asItem());

                        addToCreativeTab(output, ModItems.BUTTER.get());
                        addToCreativeTab(output, ModItems.BUTTERFLY_CRISP.get());

                        addToCreativeTab(output, ModItems.EGG_TART.get());
                        addToCreativeTab(output, ModItems.TRUFFLE_EGG_TART.get());

                        addToCreativeTab(output, ModItems.FRIED_BROWN_MUSHROOM.get());
                        addToCreativeTab(output, ModItems.FRIED_RED_MUSHROOM.get());

                        addToCreativeTab(output, ModItems.SUNFLOWER_SEED.get());
                        addToCreativeTab(output, ModItems.SUNFLOWER_SEED_PEEL.get());
                        addToCreativeTab(output, ModItems.SUNFLOWER_SEED_PULP.get());
                        addToCreativeTab(output, ModItems.ROASTED_SUNFLOWER_SEED.get());

                        addToCreativeTab(output, ModItems.EGG_BOWL.get());
                        addToCreativeTab(output, ModItems.STEAMED_EGG.get());
                        addToCreativeTab(output, ModItems.CHERRY_EGG.get());
                        addToCreativeTab(output, ModItems.STEAMED_CHERRY_EGG.get());
                        addToCreativeTab(output, ModItems.FISH_EGG.get());
                        addToCreativeTab(output, ModItems.STEAMED_FISH_EGG.get());
                        addToCreativeTab(output, ModItems.STEAMED_STUFFED_BUN_WIP.get());
                        addToCreativeTab(output, ModItems.STEAMED_STUFFED_BUN.get());
                        addToCreativeTab(output, ModItems.VEGETABLE_STEAMED_STUFFED_BUN_WIP.get());
                        addToCreativeTab(output, ModItems.VEGETABLE_STEAMED_STUFFED_BUN.get());
                        addToCreativeTab(output, ModItems.SHAOMAI_WIP.get());
                        addToCreativeTab(output, ModItems.SHAOMAI.get());

                        addToCreativeTab(output, ModItems.STEAMED_PUMPKIN_WIP.get());
                        addToCreativeTab(output, ModBlocks.STEAMED_PUMPKIN_ITEM.get());
                        addToCreativeTab(output, ModItems.STEAMED_PUMPKIN_IN_BOWL.get());
                        // Puddings
                        addToCreativeTab(output, ModItems.PUDDING_WIP_1.get());
                        addToCreativeTab(output, ModItems.PUDDING_WIP_2.get());
                        addToCreativeTab(output, ModItems.APPLE_PUDDING.get());
                        addToCreativeTab(output, ModItems.MATCHA_PUDDING.get());
                        addToCreativeTab(output, ModItems.CARAMEL_PUDDING.get());
                        addToCreativeTab(output, ModItems.CHERRY_PUDDING.get());
                        // Ice Lolly
                        addToCreativeTab(output, ModItems.ICE_LOLLY.get());
                        addToCreativeTab(output, ModItems.CHERRY_ICE_LOLLY.get());
                        addToCreativeTab(output, ModItems.MATCHA_ICE_LOLLY.get());
                        addToCreativeTab(output, ModItems.WITHER_ICE_LOLLY.get());
                        addToCreativeTab(output, ModItems.CHOCOLATE_CRUNCH_ICE_LOLLY.get());
                        // Bread
                        addToCreativeTab(output, ModItems.BREAD_SLICE.get());
                        addToCreativeTab(output, ModItems.BUTTER_BREAD_SLICE.get());
                        addToCreativeTab(output, ModItems.RAW_DONUT.get());
                        addToCreativeTab(output, ModItems.DONUT.get());
                        addToCreativeTab(output, ModItems.CHOCOLATE_DONUT.get());
                        // Creams
                        addToCreativeTab(output, ModItems.CREAM_BUCKET.get());
                        addToCreativeTab(output, ModItems.CREAM.get());
                        addToCreativeTab(output, ModItems.APPLE_CREAM.get());
                        addToCreativeTab(output, ModItems.CHERRY_CREAM.get());
                        addToCreativeTab(output, ModItems.CHOCOLATE_CREAM.get());
                        addToCreativeTab(output, ModItems.GOLDEN_APPLE_CREAM.get());
                        addToCreativeTab(output, ModItems.MATCHA_CREAM.get());
                        addToCreativeTab(output, ModItems.PUMPKIN_CREAM.get());
                        // Mousses
                        addToCreativeTab(output, ModItems.MOUSSE_WIP.get());
                        addToCreativeTab(output, ModItems.CREAM_MOUSSE.get());
                        addToCreativeTab(output, ModItems.CHERRY_MOUSSE.get());
                        addToCreativeTab(output, ModItems.CHOCOLATE_MOUSSE.get());
                        addToCreativeTab(output, ModItems.PUMPKIN_MOUSSE.get());
                        addToCreativeTab(output, ModItems.GOLDEN_APPLE_MOUSSE.get());
                        addToCreativeTab(output, ModItems.MATCHA_MOUSSE.get());
                        // Dumplings
                        addToCreativeTab(output, ModItems.CRYSTAL_DUMPLING.get());
                        // Great Meal
                        addToCreativeTab(output, ModItems.BEEF_TOMATO_CUP.get());
                        addToCreativeTab(output, ModItems.CHEESE_BAKED_POTATO.get());
                        addToCreativeTab(output, ModItems.CHEESE_RICE_BALL.get());
                        addToCreativeTab(output, ModItems.FRIED_CHICKEN.get());
                        addToCreativeTab(output, ModItems.CREAM_OF_MUSHROOM_SOUP.get());
                        addToCreativeTab(output, ModItems.FRENCH_ONION_SOUP.get());
                        addToCreativeTab(output, ModItems.CHEESE_BURGER.get());
                        addToCreativeTab(output, ModItems.PORK_CHOP_BURGER.get());
                        addToCreativeTab(output, ModBlocks.FISH_AND_CHIPS_ITEM.get());
                        addToCreativeTab(output, ModItems.BLACK_PEPPER_STEAK.get());
                        // Pork
                        addToCreativeTab(output, ModItems.STREAKY_PORK.get());
                        addToCreativeTab(output, ModItems.ROAST_STREAKY_PORK.get());
                        addToCreativeTab(output, ModItems.PORK_RIBS.get());
                        addToCreativeTab(output, ModItems.PORK_HOOF.get());
                        // Fish
                        addToCreativeTab(output, ModItems.FRIED_COD.get());
                        addToCreativeTab(output, ModItems.FRIED_COD_NUGGET.get());
                        addToCreativeTab(output, ModItems.FRIED_SALMON.get());
                        // Shrimp
                        addToCreativeTab(output, ModItems.PRAWN.get());
                        addToCreativeTab(output, ModItems.BRAISED_SHRIMP_BALL.get());
                        addToCreativeTab(output, ModItems.SHRIMP_PASTE.get());
                        addToCreativeTab(output, ModItems.MIXED_SHRIMP_PASTE.get());
                        addToCreativeTab(output, ModItems.DEEP_FRIED_SHRIMP_CAKE.get());
                        addToCreativeTab(output, ModItems.SEAWEED_FRIED_SHRIMP_CAKE.get());
                        // Squid
                        addToCreativeTab(output, ModItems.SQUID.get());
                        addToCreativeTab(output, ModItems.ROASTED_SQUID.get());
                        addToCreativeTab(output, ModItems.SQUID_TENTACLE.get());
                        addToCreativeTab(output, ModItems.RAW_SQUID_TENTACLE_KEBABS.get());
                        addToCreativeTab(output, ModItems.SQUID_TENTACLE_KEBABS.get());
                        addToCreativeTab(output, ModItems.CUTTLEBONE.get());

                        addToCreativeTab(output, ModItems.GLOW_SQUID.get());
                        addToCreativeTab(output, ModItems.ROASTED_GLOW_SQUID.get());
                        addToCreativeTab(output, ModItems.GLOW_SQUID_TENTACLE.get());
                        addToCreativeTab(output, ModItems.RAW_GLOW_SQUID_TENTACLE_KEBABS.get());
                        addToCreativeTab(output, ModItems.GLOW_SQUID_TENTACLE_KEBABS.get());
                        addToCreativeTab(output, ModItems.GLOW_CUTTLEBONE.get());
                        // Sausages
                        addToCreativeTab(output, ModItems.SAUSAGE.get());
                        addToCreativeTab(output, ModItems.SECTIONED_SAUSAGE.get());
                        addToCreativeTab(output, ModItems.GRILLED_SAUSAGE.get());
                        addToCreativeTab(output, ModItems.STARCH_SAUSAGE.get());
                        addToCreativeTab(output, ModItems.GRILLED_STARCH_SAUSAGE.get());
                        addToCreativeTab(output, ModItems.LITTLE_OCTOPUS_SAUSAGE.get());
                        // Cakes
                        addToCreativeTab(output, ModItems.EMPTY_CAKE.get());
                        addToCreativeTab(output, ModItems.BLUE_ORCHID_FLOWER_CAKE.get());
                        addToCreativeTab(output, ModItems.CHERRY_CAKE.get());
                        addToCreativeTab(output, ModItems.LILAC_CAKE.get());
                        addToCreativeTab(output, ModItems.ORANGE_TULIP_CAKE.get());
                        addToCreativeTab(output, ModItems.OXEYE_DAISY_CAKE.get());
                        addToCreativeTab(output, ModItems.PINK_TULIP_CAKE.get());
                        addToCreativeTab(output, ModItems.ROSE_CAKE.get());
                        addToCreativeTab(output, ModItems.SUNFLOWER_CAKE.get());
                        addToCreativeTab(output, ModItems.WHITE_TULIP_CAKE.get());
                        addToCreativeTab(output, ModItems.WITHER_ROSE_CAKE.get());
                        // Unknown
                        addToCreativeTab(output, ModItems.TURNIP.get());
                    })
                    .build());
    
    private static void addToCreativeTab(CreativeModeTab.Output output, Item item) {
        if (item == Items.AIR) {
            return;
        }
        output.accept(new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
    
    public static void registerCreativeModeTabs(){
        ModernDelightMain.LOGGER.info("Registering Creative Mode Tabs for " + ModernDelightMain.MOD_ID);
        CREATIVE_MODE_TABS.register(ModernDelightMain.EVENT_BUS);
    }
}
