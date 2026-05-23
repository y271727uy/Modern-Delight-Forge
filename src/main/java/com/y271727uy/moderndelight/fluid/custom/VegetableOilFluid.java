package com.y271727uy.moderndelight.fluid.custom;

import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.fluid.ModAbstractFluid;
import com.y271727uy.moderndelight.fluid.ModFluid;
import com.y271727uy.moderndelight.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public abstract class VegetableOilFluid extends ModAbstractFluid {
    public Fluid getFlowing() {
        return ModFluid.FLOWING_VEGETABLE_OIL.get();
    }

    public Fluid getStill() {
        return ModFluid.STILL_VEGETABLE_OIL.get();
    }

    public Item getBucketItem() {
        return ModItems.VEGETABLE_OIL_BUCKET.get();
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return 5;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return ModBlocks.VEGETABLE_OIL_FLUID_BLOCK.get().defaultBlockState().setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.LEVEL, getLegacyLevel(state));
    }

    @Override
    public Fluid getSource() {
        return ModFluid.STILL_VEGETABLE_OIL.get();
    }

    @Override
    protected boolean isRandomlyTicking() {
        return false;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    public static class Flowing extends VegetableOilFluid {
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return fluidState.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return false;
        }

        @Override
        public Item getBucket() {
            return ModItems.VEGETABLE_OIL_BUCKET.get();
        }
    }

    public static class Still extends VegetableOilFluid {
        @Override
        public int getAmount(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return true;
        }

        @Override
        public Item getBucket() {
            return ModItems.VEGETABLE_OIL_BUCKET.get();
        }
    }
}
