package com.y271727uy.moderndelight.fluid;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.fluid.custom.CreamFluid;
import com.y271727uy.moderndelight.fluid.custom.LiquefiedBiogasFluid;
import com.y271727uy.moderndelight.fluid.custom.SweetenedWaterFluid;
import com.y271727uy.moderndelight.fluid.custom.VegetableOilFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModFluid {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ModernDelightMain.MOD_ID);

    public static final RegistryObject<FlowingFluid> STILL_CREAM = registerFluid("still_cream", CreamFluid.Still::new);
    public static final RegistryObject<FlowingFluid> FLOWING_CREAM = registerFluid("flowing_cream", CreamFluid.Flowing::new);
    public static final RegistryObject<FlowingFluid> FLOWING_VEGETABLE_OIL = registerFluid("flowing_vegetable_oil", VegetableOilFluid.Flowing::new);
    public static final RegistryObject<FlowingFluid> STILL_VEGETABLE_OIL = registerFluid("still_vegetable_oil", VegetableOilFluid.Still::new);
    public static final RegistryObject<FlowingFluid> FLOWING_LIQUEFIED_BIOGAS = registerFluid("flowing_liquefied_biogas", LiquefiedBiogasFluid.Flowing::new);
    public static final RegistryObject<FlowingFluid> STILL_LIQUEFIED_BIOGAS = registerFluid("still_liquefied_biogas", LiquefiedBiogasFluid.Still::new);
    public static final RegistryObject<FlowingFluid> FLOWING_SWEETENED_WATER = registerFluid("flowing_sweetened_water", SweetenedWaterFluid.Flowing::new);
    public static final RegistryObject<FlowingFluid> STILL_SWEETENED_WATER = registerFluid("still_sweetened_water", SweetenedWaterFluid.Still::new);

    private static RegistryObject<FlowingFluid> registerFluid(String name, Supplier<? extends FlowingFluid> fluidSupplier) {
        return FLUIDS.register(name, fluidSupplier);
    }

    public static void registerModFluid(IEventBus eventBus) {
        FLUIDS.register(eventBus);
         ModernDelightMain.LOGGER.info("Registering Mod Fluid for " + ModernDelightMain.MOD_ID);
    }
}
