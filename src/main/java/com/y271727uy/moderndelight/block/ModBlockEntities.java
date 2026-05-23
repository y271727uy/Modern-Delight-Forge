package com.y271727uy.moderndelight.block;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.biogas.BiogasDigesterControllerBlockEntity;
import com.y271727uy.moderndelight.block.biogas.BiogasDigesterIOBlockEntity;
import com.y271727uy.moderndelight.block.biogas.GasCanisterBlockEntity;
import com.y271727uy.moderndelight.block.food.fish_and_chips.FishAndChipsBlockEntity;
import com.y271727uy.moderndelight.block.food.pizza.PizzaBlockEntity;
import com.y271727uy.moderndelight.block.food.pizza.PizzaWIPBlockEntity;
import com.y271727uy.moderndelight.block.food.pizza.RawPizzaBlockEntity;
import com.y271727uy.moderndelight.block.kitchenware.*;
import com.y271727uy.moderndelight.block.kitchenware.decor.CabinetBlockEntity;
import com.y271727uy.moderndelight.block.kitchenware.decor.KitchenUtensilHolderBlockEntity;
import com.y271727uy.moderndelight.block.kitchenware.decor.WoodenPlateBlockEntity;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.BakingTrayBlockEntity;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying.DeepFryBasketBlockEntity;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying.DeepFryerBlockEntity;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying.WoodenBasinBlockEntity;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.gas_cooking_stove.BurningGasCookingStoveBlockEntity;
import com.y271727uy.moderndelight.block.kitchenware.ice_cream_maker.IceCreamMakerBlockEntity;
import com.y271727uy.moderndelight.block.kitchenware.juice_extractor.JuiceExtractorBlockEntity;
import com.y271727uy.moderndelight.block.kitchenware.steaming.BambooGrateBlockEntity;
import com.y271727uy.moderndelight.block.kitchenware.steaming.ElectricSteamerBlockEntity;
import com.y271727uy.moderndelight.block.power.ChargingPostBlockEntity;
import com.y271727uy.moderndelight.block.power.ElectriciansDeskBlockEntity;
import com.y271727uy.moderndelight.block.power.TeslaCoilBlockEntity;
import com.y271727uy.moderndelight.block.power.alternator.ACDCConverterBlockEntity;
import com.y271727uy.moderndelight.block.power.alternator.PhotovoltaicGeneratorBlockEntity;
import com.y271727uy.moderndelight.block.power.alternator.thermal_power.FaradayGeneratorBlockEntity;
import com.y271727uy.moderndelight.block.power.alternator.thermal_power.SterlingEngineBlockEntity;
import com.y271727uy.moderndelight.block.power.alternator.wind_power.FanBladeBlockEntity;
import com.y271727uy.moderndelight.block.power.alternator.wind_power.WindTurbineControllerBlockEntity;
import com.y271727uy.moderndelight.block.power.batteries.BatteryBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ModernDelightMain.MOD_ID);

    public static final RegistryObject<BlockEntityType<OvenBlockEntity>> OVEN_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("oven_be", () ->
                    BlockEntityType.Builder.of(OvenBlockEntity::new, ModBlocks.OVEN.get()).build(null));

    public static final RegistryObject<BlockEntityType<GlassBowlBlockEntity>> GLASS_BOWL_ENTITY =
            BLOCK_ENTITIES.register("glass_bowl_be", () ->
                    BlockEntityType.Builder.of(GlassBowlBlockEntity::new, ModBlocks.GLASS_BOWL.get()).build(null));

    public static final RegistryObject<BlockEntityType<FreezerBlockEntity>> FREEZER_ENTITY =
            BLOCK_ENTITIES.register("freezer_be", () ->
                    BlockEntityType.Builder.of(FreezerBlockEntity::new, ModBlocks.FREEZER.get()).build(null));

    public static final RegistryObject<BlockEntityType<PizzaWIPBlockEntity>> PIZZA_WIP_ENTITY =
            BLOCK_ENTITIES.register("pizza_wip_be", () ->
                    BlockEntityType.Builder.of(PizzaWIPBlockEntity::new, ModBlocks.PIZZA_WIP.get()).build(null));

    public static final RegistryObject<BlockEntityType<BakingTrayBlockEntity>> BAKING_TRAY_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("baking_tray_be", () ->
                    BlockEntityType.Builder.of(BakingTrayBlockEntity::new, ModBlocks.BAKING_TRAY.get()).build(null));

    public static final RegistryObject<BlockEntityType<AdvanceFurnaceBlockEntity>> ADVANCE_FURNACE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("advance_furnace_block_entity", () ->
                    BlockEntityType.Builder.of(AdvanceFurnaceBlockEntity::new, ModBlocks.ADVANCE_FURNACE.get()).build(null));

    public static final RegistryObject<BlockEntityType<WoodenBasinBlockEntity>> WOODEN_BASIN_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("wooden_basin_block_be", () ->
                    BlockEntityType.Builder.of(WoodenBasinBlockEntity::new, ModBlocks.WOODEN_BASIN.get()).build(null));

    public static final RegistryObject<BlockEntityType<GasCanisterBlockEntity>> GAS_CANISTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("gas_canister_block_be", () ->
                    BlockEntityType.Builder.of(GasCanisterBlockEntity::new, ModBlocks.GAS_CANISTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BiogasDigesterControllerBlockEntity>> BIOGAS_DIGESTER_CONTROLLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("biogas_digester_controller_block_be", () ->
                    BlockEntityType.Builder.of(BiogasDigesterControllerBlockEntity::new, ModBlocks.BIOGAS_DIGESTER_CONTROLLER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BiogasDigesterIOBlockEntity>> BIOGAS_DIGESTER_IO_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("biogas_digester_io_block_be", () ->
                    BlockEntityType.Builder.of(BiogasDigesterIOBlockEntity::new, ModBlocks.BIOGAS_DIGESTER_IO.get()).build(null));

    public static final RegistryObject<BlockEntityType<BurningGasCookingStoveBlockEntity>> BURNING_GAS_COOKING_STOVE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("gas_cooking_stove_block_be", () ->
                    BlockEntityType.Builder.of(BurningGasCookingStoveBlockEntity::new, ModBlocks.BURNING_GAS_COOKING_STOVE.get()).build(null));

    public static final RegistryObject<BlockEntityType<DeepFryerBlockEntity>> DEEP_FRYER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("deep_fryer_block_be", () ->
                    BlockEntityType.Builder.of(DeepFryerBlockEntity::new, ModBlocks.DEEP_FRYER.get()).build(null));

    public static final RegistryObject<BlockEntityType<KitchenUtensilHolderBlockEntity>> KITCHEN_UTENSIL_HOLDER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("kitchen_utensil_holder_be", () ->
                    BlockEntityType.Builder.of(KitchenUtensilHolderBlockEntity::new, ModBlocks.KITCHEN_UTENSIL_HOLDER.get()).build(null));

    public static final RegistryObject<BlockEntityType<CuisineTableBlockEntity>> CUISINE_TABLE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("cuisine_table_be", () ->
                    BlockEntityType.Builder.of(CuisineTableBlockEntity::new, ModBlocks.CUISINE_TABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<RawPizzaBlockEntity>> RAW_PIZZA_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("raw_pizza_be", () ->
                    BlockEntityType.Builder.of(RawPizzaBlockEntity::new, ModBlocks.RAW_PIZZA.get()).build(null));

    public static final RegistryObject<BlockEntityType<PizzaBlockEntity>> PIZZA_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("pizza_be", () ->
                    BlockEntityType.Builder.of(PizzaBlockEntity::new, ModBlocks.PIZZA.get()).build(null));

    public static final RegistryObject<BlockEntityType<CabinetBlockEntity>> CABINET_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("cabinet_be", () ->
                    BlockEntityType.Builder.of(CabinetBlockEntity::new, ModBlocks.ANDESITE_CABINET.get(),
                            ModBlocks.BLACKSTONE_CABINET.get(), ModBlocks.BASALT_CABINET.get(), ModBlocks.DEEPSLATE_CABINET.get(),
                            ModBlocks.DIORITE_CABINET.get(), ModBlocks.GRANITE_CABINET.get(), ModBlocks.OBSIDIAN_CABINET.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<PhotovoltaicGeneratorBlockEntity>> PHOTOVOLTAIC_GENERATOR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("photovoltaic_generator_be", () ->
                    BlockEntityType.Builder.of(PhotovoltaicGeneratorBlockEntity::new, ModBlocks.PHOTOVOLTAIC_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<ACDCConverterBlockEntity>> AC_DC_CONVERTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("ac_dc_converter_be", () ->
                    BlockEntityType.Builder.of(ACDCConverterBlockEntity::new, ModBlocks.AC_DC_CONVERTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<FanBladeBlockEntity>> FAN_BLADE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("fan_blade_be", () ->
                    BlockEntityType.Builder.of(FanBladeBlockEntity::new, ModBlocks.FAN_BLADE.get()).build(null));

    public static final RegistryObject<BlockEntityType<WindTurbineControllerBlockEntity>> WIND_TURBINE_CONTROLLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("wind_turbine_controller_be", () ->
                    BlockEntityType.Builder.of(WindTurbineControllerBlockEntity::new, ModBlocks.WIND_TURBINE_CONTROLLER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BatteryBlockEntity>> BATTERY_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("battery_be", () ->
                    BlockEntityType.Builder.of(BatteryBlockEntity::new,
                            ModBlocks.SIMPLE_BATTERY.get(),
                            ModBlocks.INTERMEDIATE_BATTERY.get(),
                            ModBlocks.ADVANCE_BATTERY.get(),
                            ModBlocks.DIMENSION_BATTERY.get()).build(null));

    public static final RegistryObject<BlockEntityType<SterlingEngineBlockEntity>> STERLING_ENGINE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("sterling_engine_be", () ->
                    BlockEntityType.Builder.of(SterlingEngineBlockEntity::new, ModBlocks.STERLING_ENGINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<FaradayGeneratorBlockEntity>> FARADAY_GENERATOR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("faraday_generator_be", () ->
                    BlockEntityType.Builder.of(FaradayGeneratorBlockEntity::new, ModBlocks.FARADAY_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<TeslaCoilBlockEntity>> TESLA_COIL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("tesla_coil_be", () ->
                    BlockEntityType.Builder.of(TeslaCoilBlockEntity::new, ModBlocks.TESLA_COIL.get()).build(null));

    public static final RegistryObject<BlockEntityType<ElectriciansDeskBlockEntity>> ELECTRICIANS_DESK_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("electricians_desk_be", () ->
                    BlockEntityType.Builder.of(ElectriciansDeskBlockEntity::new, ModBlocks.ELECTRICIANS_DESK.get()).build(null));

    public static final RegistryObject<BlockEntityType<BambooGrateBlockEntity>> BAMBOO_GRATE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("bamboo_grate_be", () ->
                    BlockEntityType.Builder.of(BambooGrateBlockEntity::new, ModBlocks.BAMBOO_GRATE.get()).build(null));

    public static final RegistryObject<BlockEntityType<ElectricSteamerBlockEntity>> ELECTRIC_STEAMER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("electric_steamer_be", () ->
                    BlockEntityType.Builder.of(ElectricSteamerBlockEntity::new, ModBlocks.ELECTRIC_STEAMER.get()).build(null));

    public static final RegistryObject<BlockEntityType<IceCreamMakerBlockEntity>> ICE_CREAM_MAKER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("ice_cream_maker_be", () ->
                    BlockEntityType.Builder.of(IceCreamMakerBlockEntity::new, ModBlocks.ICE_CREAM_MAKER.get()).build(null));

    public static final RegistryObject<BlockEntityType<FishAndChipsBlockEntity>> FISH_AND_CHIPS_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("fish_and_chips_be", () ->
                    BlockEntityType.Builder.of(FishAndChipsBlockEntity::new, ModBlocks.FISH_AND_CHIPS.get()).build(null));

    public static final RegistryObject<BlockEntityType<ChargingPostBlockEntity>> CHARGING_POST_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("charging_post_be", () ->
                    BlockEntityType.Builder.of(ChargingPostBlockEntity::new, ModBlocks.CHARGING_POST.get()).build(null));

    public static final RegistryObject<BlockEntityType<JuiceExtractorBlockEntity>> JUICE_EXTRACTOR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("juice_extractor_be", () ->
                    BlockEntityType.Builder.of(JuiceExtractorBlockEntity::new, ModBlocks.JUICE_EXTRACTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<DeepFryBasketBlockEntity>> DEEP_FRY_BASKET_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("deep_fry_basket_be", () ->
                    BlockEntityType.Builder.of(DeepFryBasketBlockEntity::new, ModBlocks.DEEP_FRY_BASKET.get()).build(null));

    public static final RegistryObject<BlockEntityType<WoodenPlateBlockEntity>> WOODEN_PLATE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("wooden_plate_be", () ->
                    BlockEntityType.Builder.of(WoodenPlateBlockEntity::new, ModBlocks.WOODEN_PLATE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}

