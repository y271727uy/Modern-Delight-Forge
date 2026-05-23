package com.y271727uy.moderndelight.block;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.biogas.*;
import com.y271727uy.moderndelight.block.crops.BlackPepperCropBlock;
import com.y271727uy.moderndelight.block.crops.GarlicCropBlock;
import com.y271727uy.moderndelight.block.food.*;
import com.y271727uy.moderndelight.block.food.fish_and_chips.FishAndChipsBlock;
import com.y271727uy.moderndelight.block.food.pizza.*;
import com.y271727uy.moderndelight.block.kitchenware.*;
import com.y271727uy.moderndelight.block.kitchenware.decor.*;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.BakingTrayBlock;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying.*;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.gas_cooking_stove.*;
import com.y271727uy.moderndelight.block.kitchenware.ice_cream_maker.*;
import com.y271727uy.moderndelight.block.kitchenware.juice_extractor.*;
import com.y271727uy.moderndelight.block.kitchenware.steaming.*;
import com.y271727uy.moderndelight.block.power.*;
import com.y271727uy.moderndelight.block.power.alternator.*;
import com.y271727uy.moderndelight.block.power.alternator.thermal_power.*;
import com.y271727uy.moderndelight.block.power.alternator.wind_power.*;
import com.y271727uy.moderndelight.block.power.batteries.*;
import com.y271727uy.moderndelight.fluid.ModFluid;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ModernDelightMain.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ModernDelightMain.MOD_ID);

    public static final RegistryObject<Block> SILICON_BLOCK = registerBlock("silicon_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> MASHED_POTATO_BLOCK = registerBlock("mashed_potato_block",
            () -> new MashedPotatoBlock(BlockBehaviour.Properties.copy(Blocks.DIRT).sound(SoundType.SAND)
                    .mapColor(MapColor.COLOR_YELLOW).speedFactor(0.6f).jumpFactor(0.5f)));
    public static final RegistryObject<Block> GLASS_BOWL = registerBlockWithoutItem("glass_bowl",
            () -> new GlassBowlBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).strength(0)
                    .mapColor(MapColor.SNOW).noOcclusion()));
    public static final RegistryObject<Block> BLACK_PEPPER_CROP = registerBlockWithoutItem("black_pepper_crop",
            () -> new BlackPepperCropBlock(BlockBehaviour.Properties.copy(Blocks.POTATOES)));
    public static final RegistryObject<Block> OVEN = registerBlock("oven",
            () -> new OvenBlock(BlockBehaviour.Properties.copy(Blocks.BRICKS)
                    .lightLevel(state -> state.getValue(OvenBlock.OVEN_BURNING) ? 15 : 0)));
    public static final RegistryObject<Block> FREEZER = registerBlock("freezer",
            () -> new FreezerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final RegistryObject<Block> WHEAT_DOUGH = registerBlockWithoutItem("wheat_dough",
            () -> new WheatDoughBlock(BlockBehaviour.Properties.copy(Blocks.REPEATER).sound(SoundType.HONEY_BLOCK)
                    .jumpFactor(0.5f).mapColor(MapColor.SNOW).noOcclusion()));
    public static final RegistryObject<Block> PIZZA_WIP = registerBlockWithoutItem("pizza_wip", PizzaWIPBlock::new);
    public static final RegistryObject<Block> RAW_PIZZA = registerBlockWithoutItem("raw_pizza", RawPizzaBlock::new);
    public static final RegistryObject<Block> PIZZA = registerBlockWithoutItem("pizza", PizzaBlock::new);
    public static final RegistryObject<Block> BAKING_TRAY = registerBlock("baking_tray", BakingTrayBlock::new);
    public static final RegistryObject<Block> DEEP_FRYER = registerBlock("deep_fryer", DeepFryerBlock::new);
    public static final RegistryObject<Block> ADVANCE_FURNACE = registerBlock("advance_furnace", AdvanceFurnaceBlock::new);
    public static final RegistryObject<Block> CREAM_FLUID_BLOCK = registerBlockWithoutItem("cream_fluid",
            () -> new LiquidBlock(ModFluid.STILL_CREAM::get, BlockBehaviour.Properties.copy(Blocks.WATER)));
    public static final RegistryObject<Block> WOODEN_BASIN = registerBlock("wooden_basin",
            WoodenBasinBlock::new);
    public static final RegistryObject<Block> VEGETABLE_OIL_FLUID_BLOCK = registerBlockWithoutItem("vegetable_oil_fluid",
            () -> new LiquidBlock(ModFluid.STILL_VEGETABLE_OIL::get, BlockBehaviour.Properties.copy(Blocks.WATER)));
    public static final RegistryObject<Block> GAS_CANISTER = registerBlockWithoutItem("gas_canister",
            GasCanisterBlock::new);
    public static final RegistryObject<Block> BIOGAS_DIGESTER_CONTROLLER = registerBlock("biogas_digester_controller",
            BiogasDigesterControllerBlock::new);
    public static final RegistryObject<Block> BIOGAS_DIGESTER_IO = registerBlock("biogas_digester_io",
            BiogasDigesterIOBlock::new);
    public static final RegistryObject<Block> BURNING_GAS_COOKING_STOVE = registerBlock("burning_gas_cooking_stove",
            BurningGasCookingStoveBlock::new);
    public static final RegistryObject<Block> GAS_COOKING_STOVE = registerBlock("gas_cooking_stove",
            GasCookingStoveBlock::new);
    public static final RegistryObject<Block> DEEP_FRY_BASKET = registerBlockWithoutItem("deep_fry_basket",
            DeepFryBasketBlock::new);
    public static final RegistryObject<Block> KITCHEN_UTENSIL_HOLDER = registerBlock("kitchen_utensil_holder",
            () -> new KitchenUtensilHolderBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(0)
                    .sound(SoundType.STONE).mapColor(MapColor.SNOW).strength(5.0f).noOcclusion()));
    public static final RegistryObject<Block> CUISINE_TABLE = registerBlock("cuisine_table", CuisineTableBlock::new);
    public static final RegistryObject<Block> ANDESITE_CABINET = registerBlock("andesite_cabinet", CabinetBlock::new);
    public static final RegistryObject<Block> DIORITE_CABINET = registerBlock("diorite_cabinet", CabinetBlock::new);
    public static final RegistryObject<Block> GRANITE_CABINET = registerBlock("granite_cabinet", CabinetBlock::new);
    public static final RegistryObject<Block> DEEPSLATE_CABINET = registerBlock("deepslate_cabinet", () -> new CabinetBlock(
            BlockBehaviour.Properties.copy(Blocks.DEEPSLATE).noOcclusion()
    ));
    public static final RegistryObject<Block> BLACKSTONE_CABINET = registerBlock("blackstone_cabinet", () -> new CabinetBlock(
            BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).noOcclusion()
    ));
    public static final RegistryObject<Block> BASALT_CABINET = registerBlock("basalt_cabinet", () -> new CabinetBlock(
            BlockBehaviour.Properties.copy(Blocks.BASALT).noOcclusion()
    ));
    public static final RegistryObject<Block> OBSIDIAN_CABINET = registerBlock("obsidian_cabinet", () -> new CabinetBlock(
            BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noOcclusion()
    ));
    public static final RegistryObject<Block> PHOTOVOLTAIC_GENERATOR = registerBlock("photovoltaic_generator", PhotovoltaicGeneratorBlock::new);
    public static final RegistryObject<Block> AC_DC_CONVERTER = registerBlock("ac_dc_converter", ACDCConverterBlock::new);
    public static final RegistryObject<Block> FAN_BLADE = registerBlockWithoutItem("fan_blade", FanBladeBlock::new);
    public static final RegistryObject<Block> WIND_TURBINE_CONTROLLER = registerBlock("wind_turbine_controller", WindTurbineControllerBlock::new);
    public static final RegistryObject<Block> SIMPLE_BATTERY = registerBlockWithoutItem("simple_battery", () -> new SimpleBatteryBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS)));
    public static final RegistryObject<Block> INTERMEDIATE_BATTERY = registerBlockWithoutItem("intermediate_battery", () -> new IntermediateBatteryBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS)));
    public static final RegistryObject<Block> ADVANCE_BATTERY = registerBlockWithoutItem("advance_battery", () -> new AdvanceBatteryBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS)));
    public static final RegistryObject<Block> DIMENSION_BATTERY = registerBlockWithoutItem("dimension_battery", () -> new DimensionBatteryBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).lightLevel(state -> 8)));
    public static final RegistryObject<Block> STERLING_ENGINE = registerBlockWithoutItem("sterling_engine", SterlingEngineBlock::new);
    public static final RegistryObject<Block> FARADAY_GENERATOR = registerBlock("faraday_generator", FaradayGeneratorBlock::new);
    public static final RegistryObject<Block> TESLA_COIL = registerBlock("tesla_coil", TeslaCoilBlock::new);
    public static final RegistryObject<Block> ELECTRICIANS_DESK = registerBlock("electricians_desk", ElectriciansDeskBlock::new);
    public static final RegistryObject<Block> BOXED_CHERRIES = registerBlock("boxed_cherries", BoxedCherriesBlock::new);
    public static final RegistryObject<Block> BAMBOO_GRATE = registerBlock("bamboo_grate", BambooGrateBlock::new);
    public static final RegistryObject<Block> BAMBOO_COVER = registerBlock("bamboo_cover", BambooCoverBlock::new);
    public static final RegistryObject<Block> ELECTRIC_STEAMER = registerBlock("electric_steamer", ElectricSteamerBlock::new);
    public static final RegistryObject<Block> ICE_CREAM_MAKER = registerBlockWithoutItem("ice_cream_maker", IceCreamMakerBlock::new);
    public static final RegistryObject<Block> CARAMEL_PUDDING = registerBlockWithoutItem("caramel_pudding", CaramelPuddingBlock::new);
    public static final RegistryObject<Block> FISH_AND_CHIPS = registerBlockWithoutItem("fish_and_chips", FishAndChipsBlock::new);
    public static final RegistryObject<Block> WILD_PEPPER_CROP = registerBlock("wild_pepper_crop", () -> new PlantBlock(
            BlockBehaviour.Properties.copy(Blocks.DANDELION).noOcclusion().noCollission()
    ));
    public static final RegistryObject<Block> GARLIC_CROP = registerBlockWithoutItem("garlic_crop",
            () -> new GarlicCropBlock(BlockBehaviour.Properties.copy(Blocks.POTATOES)));
    public static final RegistryObject<Block> WILD_GARLIC = registerBlock("wild_garlic", () -> new PlantBlock(
            BlockBehaviour.Properties.copy(Blocks.DANDELION).noOcclusion().noCollission()
    ));
    public static final RegistryObject<Block> CHARGING_POST = registerBlock("charging_post", ChargingPostBlock::new);
    public static final RegistryObject<Block> JUICE_EXTRACTOR = registerBlockWithoutItem("juice_extractor",
            () -> new JuiceExtractorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final RegistryObject<Block> GLASS_CUP = registerBlockWithoutItem("glass_cup", GlassCupBlock::new);
    public static final RegistryObject<Block> CHERRY_MILK_TEA = registerBlockWithoutItem("cherry_milk_tea",
            () -> new GlassCupOfTeaBlock(5,0.3f,
                    new MobEffectInstance(MobEffects.DAMAGE_BOOST,10 * 20,0),
                    new MobEffectInstance(MobEffects.DIG_SPEED,60 * 20,0)));
    public static final RegistryObject<Block> ROSE_ICE_TEA = registerBlockWithoutItem("rose_ice_tea",
            () -> new GlassCupOfTeaBlock(5,0.3f,
                    new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,20 * 20,0),
                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED,60 * 20,0)));
    public static final RegistryObject<Block> LIQUEFIED_BIOGAS_FLUID_BLOCK = registerBlockWithoutItem("liquefied_biogas_fluid",
            () -> new LiquidBlock(ModFluid.STILL_LIQUEFIED_BIOGAS::get, BlockBehaviour.Properties.copy(Blocks.WATER)));
    public static final RegistryObject<Block> WOODEN_PLATE = registerBlock("wooden_plate",
            WoodenPlateBlock::new);
    public static final RegistryObject<Block> STEAMED_PUMPKIN = registerBlockWithoutItem("steamed_pumpkin",
            SteamedPumpkinBlock::new);
    public static final RegistryObject<Block> SWEENTENED_WATER_FLUID_BLOCK = registerBlockWithoutItem("sweetened_water_fluid",
            () -> new LiquidBlock(ModFluid.STILL_SWEETENED_WATER::get, BlockBehaviour.Properties.copy(Blocks.WATER)));

    // Block Items
    public static final RegistryObject<Item> FISH_AND_CHIPS_ITEM = ITEMS.register("fish_and_chips",
            () -> new BlockItem(FISH_AND_CHIPS.get(), new Item.Properties().stacksTo(16).food(
                    new FoodProperties.Builder().nutrition(20).saturationMod(0.6f).build()
            )){
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
                    if (user instanceof Player player){
                        if (stack.getCount() == 1){
                            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(WOODEN_PLATE.get()));
                        } else {
                            player.getInventory().add(new ItemStack(WOODEN_PLATE.get()));
                            stack.shrink(1);
                        }
                    }
                    return super.finishUsingItem(stack, world, user);
                }
            });
    public static final RegistryObject<Item> ICE_CREAM_MAKER_ITEM = ITEMS.register("ice_cream_maker",
            () -> new IceCreamMakerBlockItem(ICE_CREAM_MAKER.get()));
    public static final RegistryObject<Item> STERLING_ENGINE_ITEM = ITEMS.register("sterling_engine",
            () -> new SterlingEngineBlockItem(STERLING_ENGINE.get()));
    public static final RegistryObject<Item> FAN_BLADE_ITEM = ITEMS.register("fan_blade",
            () -> new BlockItem(FAN_BLADE.get(), new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> SIMPLE_BATTERY_ITEM = ITEMS.register("simple_battery",
            () -> new BatteryBlockItem((AbstractBatteryBlock) SIMPLE_BATTERY.get()));
    public static final RegistryObject<Item> INTERMEDIATE_BATTERY_ITEM = ITEMS.register("intermediate_battery",
            () -> new BatteryBlockItem((AbstractBatteryBlock) INTERMEDIATE_BATTERY.get()));
    public static final RegistryObject<Item> ADVANCE_BATTERY_ITEM = ITEMS.register("advance_battery",
            () -> new BatteryBlockItem((AbstractBatteryBlock) ADVANCE_BATTERY.get()));
    public static final RegistryObject<Item> DIMENSION_BATTERY_ITEM = ITEMS.register("dimension_battery",
            () -> new BatteryBlockItem((AbstractBatteryBlock) DIMENSION_BATTERY.get()));
    public static final RegistryObject<Item> DEEP_FRY_BASKET_ITEM = ITEMS.register("deep_fry_basket",
            () -> new BlockItem(DEEP_FRY_BASKET.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GAS_CANISTER_ITEM = ITEMS.register("gas_canister",
            () -> new GasCanisterBlockItem(GAS_CANISTER.get()));
    public static final RegistryObject<Item> PIZZA_ITEM = ITEMS.register("pizza",
            () -> new BlockItem(PIZZA.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> RAW_PIZZA_ITEM = ITEMS.register("raw_pizza",
            () -> new BlockItem(RAW_PIZZA.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(8).saturationMod(0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 200, 0), 0.4f).build()).stacksTo(1)));
    public static final RegistryObject<Item> PIZZA_WIP_ITEM = ITEMS.register("pizza_wip",
            () -> new BlockItem(PIZZA_WIP.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationMod(0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 200, 0), 0.3f).build()).stacksTo(1)));
    public static final RegistryObject<Item> WHEAT_DOUGH_ITEM = ITEMS.register("wheat_dough",
            () -> new BlockItem(WHEAT_DOUGH.get(), new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 200, 0), 0.5f).build())));
    public static final RegistryObject<Item> JUICE_EXTRACTOR_ITEM = ITEMS.register("juice_extractor",
            () -> new JuiceExtractorBlockItem(JUICE_EXTRACTOR.get()));
    public static final RegistryObject<Item> CHERRY_MILK_TEA_ITEM = ITEMS.register("cherry_milk_tea",
            () -> new GlassCupOfTeaBlockItem(CHERRY_MILK_TEA.get()));
    public static final RegistryObject<Item> ROSE_ICE_TEA_TEA_ITEM = ITEMS.register("rose_ice_tea",
            () -> new GlassCupOfTeaBlockItem(ROSE_ICE_TEA.get()));
    public static final RegistryObject<Item> STEAMED_PUMPKIN_ITEM = ITEMS.register("steamed_pumpkin",
            () -> new BlockItem(STEAMED_PUMPKIN.get(), new Item.Properties().stacksTo(16).craftRemainder(WOODEN_PLATE.get().asItem())));
    public static final RegistryObject<Item> GLASS_BOWL_ITEM = ITEMS.register("glass_bowl",
            () -> new BlockItem(GLASS_BOWL.get(), new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GLASS_CUP_ITEM = ITEMS.register("glass_cup",
            () -> new BlockItem(GLASS_CUP.get(), new Item.Properties().stacksTo(1)));

    private static <T extends Block> RegistryObject<T> registerBlockWithoutItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}

