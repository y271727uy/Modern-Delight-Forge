package com.y271727uy.moderndelight.screen;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.screen.custom.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModScreenHandlers {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ModernDelightMain.MOD_ID);
    
    public static class PlaceholderMenu extends AbstractContainerMenu {
        protected PlaceholderMenu(int syncId) {
            super(null, syncId);
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }

        @Override
        public net.minecraft.world.item.ItemStack quickMoveStack(Player player, int index) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }
    }

    public static final RegistryObject<MenuType<OvenScreenHandler>> OVEN_SCREEN_HANDLER =
            MENU_TYPES.register("oven_screen", () -> IForgeMenuType.create(OvenScreenHandler::new));
    public static final RegistryObject<MenuType<FreezerScreenHandler>> FREEZER_SCREEN_HANDLER =
            MENU_TYPES.register("freezing_screen", () -> IForgeMenuType.create(FreezerScreenHandler::new));
    public static final RegistryObject<MenuType<AdvanceFurnaceScreenHandler>> ADVANCE_FURNACE_SCREEN_HANDLER =
            MENU_TYPES.register("advance_furnace_screen", () -> IForgeMenuType.create(AdvanceFurnaceScreenHandler::new));
    public static final RegistryObject<MenuType<WoodenBasinScreenHandler>> WOODEN_BASIN_SCREEN_HANDLER =
            MENU_TYPES.register("wooden_basin_screen", () -> IForgeMenuType.create(WoodenBasinScreenHandler::new));
    public static final RegistryObject<MenuType<GasCanisterScreenHandler>> GAS_CANISTER_SCREEN_HANDLER =
            MENU_TYPES.register("gas_canister_screen", () -> IForgeMenuType.create(GasCanisterScreenHandler::new));
    public static final RegistryObject<MenuType<BiogasDigesterControllerScreenHandler>> BIOGAS_DIGESTER_CONTROLLER_SCREEN_HANDLER =
            MENU_TYPES.register("biogas_digester_controller_screen", () -> IForgeMenuType.create(BiogasDigesterControllerScreenHandler::new));
    public static final RegistryObject<MenuType<BiogasDigesterIOScreenHandler>> BIOGAS_DIGESTER_IO_SCREEN_HANDLER =
            MENU_TYPES.register("biogas_digester_io_screen", () -> IForgeMenuType.create(BiogasDigesterIOScreenHandler::new));
    public static final RegistryObject<MenuType<DeepFryerScreenHandler>> DEEP_FRYER_SCREEN_HANDLER =
            MENU_TYPES.register("deep_fryer_screen", () -> IForgeMenuType.create(DeepFryerScreenHandler::new));
    public static final RegistryObject<MenuType<CuisineTableScreenHandler>> CUISINE_TABLE_SCREEN_HANDLER =
            MENU_TYPES.register("cuisine_table_screen", () -> IForgeMenuType.create(CuisineTableScreenHandler::new));
    public static final RegistryObject<MenuType<CabinetScreenHandler>> CABINET_SCREEN_HANDLER =
            MENU_TYPES.register("cabinet_screen", () -> IForgeMenuType.create(CabinetScreenHandler::new));
    public static final RegistryObject<MenuType<PhotovoltaicGeneratorScreenHandler>> PHOTOVOLTAIC_GENERATOR_SCREEN_HANDLER =
            MENU_TYPES.register("photovoltaic_generator_screen", () -> IForgeMenuType.create(PhotovoltaicGeneratorScreenHandler::new));
    public static final RegistryObject<MenuType<ACDCConverterScreenHandler>> ACDC_CONVERTER_SCREEN_HANDLER =
            MENU_TYPES.register("gas_pump_screen", () -> IForgeMenuType.create(ACDCConverterScreenHandler::new));
    public static final RegistryObject<MenuType<WindTurbineControllerScreenHandler>> WIND_TURBINE_CONTROLLER_SCREEN_HANDLER =
            MENU_TYPES.register("wind_turbine_controller_screen", () -> IForgeMenuType.create(WindTurbineControllerScreenHandler::new));
    public static final RegistryObject<MenuType<FaradayGeneratorScreenHandler>> FARADAY_GENERATOR_SCREEN_HANDLER =
            MENU_TYPES.register("faraday_generator_screen", () -> IForgeMenuType.create(FaradayGeneratorScreenHandler::new));
    public static final RegistryObject<MenuType<TeslaCoilScreenHandler>> TESLA_COIL_SCREEN_HANDLER =
            MENU_TYPES.register("tesla_coil_screen", () -> IForgeMenuType.create(TeslaCoilScreenHandler::new));
    public static final RegistryObject<MenuType<ElectriciansDeskScreenHandler>> ELECTRICIANS_DESK_SCREEN_HANDLER =
            MENU_TYPES.register("electric_desk_screen", () -> IForgeMenuType.create(ElectriciansDeskScreenHandler::new));
    public static final RegistryObject<MenuType<BambooSteamerScreenHandler>> BAMBOO_STEAMER_SCREEN_HANDLER =
            MENU_TYPES.register("bamboo_steamer_screen", () -> IForgeMenuType.create(BambooSteamerScreenHandler::new));
    public static final RegistryObject<MenuType<ElectricSteamerScreenHandler>> ELECTRIC_STEAMER_SCREEN_HANDLER =
            MENU_TYPES.register("electric_steamer_screen", () -> IForgeMenuType.create(ElectricSteamerScreenHandler::new));
    public static final RegistryObject<MenuType<IceCreamMakerScreenHandler>> ICE_CREAM_MAKER_SCREEN_HANDLER =
            MENU_TYPES.register("ice_cream_maker_screen", () -> IForgeMenuType.create(IceCreamMakerScreenHandler::new));
    public static final RegistryObject<MenuType<ChargingPostScreenHandler>> CHARGING_POST_SCREEN_HANDLER =
            MENU_TYPES.register("charging_post_screen", () -> IForgeMenuType.create(ChargingPostScreenHandler::new));

    public static void registerScreenHandlers(){
        ModernDelightMain.LOGGER.info("Registering Screen Handlers for " + ModernDelightMain.MOD_ID);
        MENU_TYPES.register(ModernDelightMain.EVENT_BUS);
    }
}
