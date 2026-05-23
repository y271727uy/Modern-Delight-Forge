package com.y271727uy.moderndelight.util.energy;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class StackEnergyStorage implements IEnergyStorage {
    protected final ItemStack stack;
    private final int capacity;
    private final int maxReceive;
    private final int maxExtract;

    protected StackEnergyStorage(ItemStack stack, int capacity, int maxReceive, int maxExtract) {
        this.stack = stack;
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    protected abstract int readEnergy();

    protected abstract void writeEnergy(int energy);

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive()) {
            return 0;
        }
        int stored = getEnergyStored();
        int received = Math.min(this.capacity - stored, Math.min(this.maxReceive, maxReceive));
        if (!simulate && received > 0) {
            writeEnergy(stored + received);
        }
        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract()) {
            return 0;
        }
        int stored = getEnergyStored();
        int extracted = Math.min(stored, Math.min(this.maxExtract, maxExtract));
        if (!simulate && extracted > 0) {
            writeEnergy(stored - extracted);
        }
        return extracted;
    }

    @Override
    public int getEnergyStored() {
        return Mth.clamp(readEnergy(), 0, this.capacity);
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }
}
