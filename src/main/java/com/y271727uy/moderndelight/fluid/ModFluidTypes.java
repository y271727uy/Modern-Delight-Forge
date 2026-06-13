package com.y271727uy.moderndelight.fluid;

import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class ModFluidTypes {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, ModernDelightMain.MOD_ID);

    public static final RegistryObject<FluidType> CREAM_FLUID_TYPE = FLUID_TYPES.register("still_cream",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.moderndelight.still_cream")
                    .canDrown(false)
                    .canExtinguish(false)
                    .canConvertToSource(false)
                    .viscosity(3000)
                    .density(1500)) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        private static final ResourceLocation STILL = new ResourceLocation("minecraft:block/water_still");
                        private static final ResourceLocation FLOW = new ResourceLocation("minecraft:block/water_flow");

                        @Override
                        public ResourceLocation getStillTexture() { return STILL; }

                        @Override
                        public ResourceLocation getFlowingTexture() { return FLOW; }

                        @Override
                        public int getTintColor() { return 0xFFD4D4D4; }
                    });
                }
            });

    public static final RegistryObject<FluidType> VEGETABLE_OIL_FLUID_TYPE = FLUID_TYPES.register("still_vegetable_oil",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.moderndelight.still_vegetable_oil")
                    .canDrown(false)
                    .canExtinguish(false)
                    .canConvertToSource(false)
                    .viscosity(2000)
                    .density(1000)) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        private static final ResourceLocation STILL = new ResourceLocation("minecraft:block/water_still");
                        private static final ResourceLocation FLOW = new ResourceLocation("minecraft:block/water_flow");

                        @Override
                        public ResourceLocation getStillTexture() { return STILL; }

                        @Override
                        public ResourceLocation getFlowingTexture() { return FLOW; }

                        @Override
                        public int getTintColor() { return 0xFFECD67E; }
                    });
                }
            });

    public static final RegistryObject<FluidType> LIQUEFIED_BIOGAS_FLUID_TYPE = FLUID_TYPES.register("still_liquefied_biogas",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.moderndelight.still_liquefied_biogas")
                    .canDrown(false)
                    .canExtinguish(false)
                    .canConvertToSource(false)
                    .viscosity(1000)
                    .density(800)) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        private static final ResourceLocation STILL = new ResourceLocation("minecraft:block/water_still");
                        private static final ResourceLocation FLOW = new ResourceLocation("minecraft:block/water_flow");

                        @Override
                        public ResourceLocation getStillTexture() { return STILL; }

                        @Override
                        public ResourceLocation getFlowingTexture() { return FLOW; }

                        @Override
                        public int getTintColor() { return 0xFF5C8A4A; }
                    });
                }
            });

    public static final RegistryObject<FluidType> SWEETENED_WATER_FLUID_TYPE = FLUID_TYPES.register("still_sweetened_water",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.moderndelight.still_sweetened_water")
                    .canDrown(false)
                    .canExtinguish(false)
                    .canConvertToSource(false)
                    .viscosity(1200)
                    .density(1050)) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        private static final ResourceLocation STILL = new ResourceLocation("minecraft:block/water_still");
                        private static final ResourceLocation FLOW = new ResourceLocation("minecraft:block/water_flow");

                        @Override
                        public ResourceLocation getStillTexture() { return STILL; }

                        @Override
                        public ResourceLocation getFlowingTexture() { return FLOW; }

                        @Override
                        public int getTintColor() { return 0xFFA8D8EA; }
                    });
                }
            });
}
