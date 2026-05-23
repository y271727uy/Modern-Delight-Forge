package com.y271727uy.moderndelight.util.block_util;

import com.y271727uy.moderndelight.networking.packet.FluidSyncS2CPacket;
import com.y271727uy.moderndelight.util.FluidStack;
import com.y271727uy.moderndelight.util.fluid.FluidVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface FluidStorageAble {
    Object getFluidStorage();
    default void sendFluidPacket(Level level, BlockPos pos){
        try {
            Object storage = getFluidStorage();
            Object fluid = storage.getClass().getMethod("getFluid").invoke(storage);
            Object amount = storage.getClass().getMethod("getFluidAmount").invoke(storage);
            if (fluid instanceof FluidVariant variant && amount instanceof Number number && !variant.isBlank()) {
                FluidSyncS2CPacket.send(pos, new FluidStack(variant, number.longValue()), level);
            }
        } catch (ReflectiveOperationException ignored) {
        }
    }

    default void setFluid(FluidStack fluidStack){
        try {
            Object storage = getFluidStorage();
            storage.getClass().getMethod("setFluid", FluidVariant.class).invoke(storage, FluidVariant.of(fluidStack.stack.getFluid()));
            storage.getClass().getMethod("setAmount", long.class).invoke(storage, fluidStack.getAmount());
        } catch (ReflectiveOperationException ignored) {
        }
    }
}
