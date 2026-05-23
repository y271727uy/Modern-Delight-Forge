package com.y271727uy.moderndelight.util;

import com.y271727uy.moderndelight.util.fluid.FluidVariant;
import net.minecraft.world.level.material.Fluid;

/**
 * Originally by Flandre923
 **/
public class FluidStack {
    public net.minecraftforge.fluids.FluidStack stack;
    public long amount_droplets;

    public FluidStack(net.minecraftforge.fluids.FluidStack stack, long amount_droplets){
        this.stack = stack;
        this.amount_droplets = amount_droplets;
    }

    public FluidStack(FluidVariant variant, long amount_droplets) {
        this(new net.minecraftforge.fluids.FluidStack(variant.getFluid(), (int) convertDropletsToMb(amount_droplets)), amount_droplets);
    }

    public Fluid getFluid() {
        return stack.getFluid();
    }

    public FluidVariant getVariant() {
        return FluidVariant.of(stack.getFluid());
    }

    public void setFluidVariant(net.minecraftforge.fluids.FluidStack stack) {
        this.stack = stack;
    }

    public long getAmount() {
        return amount_droplets;
    }

    public void setAmount(long amount_droplets) {
        this.amount_droplets = amount_droplets;
    }

    public static long convertDropletsToMb(long droplets){
        return droplets/81;
    }

    public static long convertMbToDroplets(long mb){
        return mb*81;
    }
}
