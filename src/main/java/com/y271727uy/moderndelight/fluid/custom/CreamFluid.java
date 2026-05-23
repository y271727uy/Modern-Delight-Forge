package com.y271727uy.moderndelight.fluid.custom;

import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.fluid.ModAbstractFluid;
import com.y271727uy.moderndelight.fluid.ModFluid;
import com.y271727uy.moderndelight.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public abstract class CreamFluid extends ModAbstractFluid {
    public Fluid getFlowing() {
        return ModFluid.FLOWING_CREAM.get();
    }

    public Fluid getStill() {
        return ModFluid.STILL_CREAM.get();
    }

    public Item getBucketItem() {
        return ModItems.CREAM_BUCKET.get();
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return 100;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return ModBlocks.CREAM_FLUID_BLOCK.get().defaultBlockState().setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.LEVEL, getLegacyLevel(state));
    }

    @Override
    public Fluid getSource() {
        return ModFluid.STILL_CREAM.get();
    }

    public static class Flowing extends CreamFluid {
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
            return ModItems.CREAM_BUCKET.get();
        }
    }

    public static class Still extends CreamFluid {
        @Override
        public int getAmount(FluidState fluidState) {
            return 5;
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return true;
        }

        @Override
        public Item getBucket() {
            return ModItems.CREAM_BUCKET.get();
        }
    }
}
