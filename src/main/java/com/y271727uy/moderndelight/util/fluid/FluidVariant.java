package com.y271727uy.moderndelight.util.fluid;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public final class FluidVariant {
    private final Fluid fluid;

    private FluidVariant(Fluid fluid) {
        this.fluid = fluid == null ? Fluids.EMPTY : fluid;
    }

    public static FluidVariant blank() {
        return new FluidVariant(Fluids.EMPTY);
    }

    public static FluidVariant of(Fluid fluid) {
        return new FluidVariant(fluid);
    }

    public static FluidVariant fromNbt(CompoundTag nbt) {
        if (nbt == null || !nbt.contains("fluid")) {
            return blank();
        }
        ResourceLocation id = ResourceLocation.tryParse(nbt.getString("fluid"));
        if (id == null) {
            return blank();
        }
        Fluid fluid = BuiltInRegistries.FLUID.get(id);
        return fluid == Fluids.EMPTY ? blank() : of(fluid);
    }

    public CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        ResourceLocation id = BuiltInRegistries.FLUID.getKey(fluid);
        if (id != null) {
            nbt.putString("fluid", id.toString());
        }
        return nbt;
    }

    public boolean isBlank() {
        return fluid == Fluids.EMPTY;
    }

    public boolean isOf(Fluid other) {
        return !isBlank() && fluid == other;
    }

    public Fluid getFluid() {
        return fluid;
    }
}

