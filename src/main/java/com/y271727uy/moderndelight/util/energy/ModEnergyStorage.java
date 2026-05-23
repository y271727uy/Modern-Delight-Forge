package com.y271727uy.moderndelight.util.energy;

import net.minecraft.util.Mth;
import net.minecraftforge.energy.EnergyStorage;

public class ModEnergyStorage extends EnergyStorage {
    private final Runnable onChange;

    public ModEnergyStorage(int capacity, int maxTransfer, Runnable onChange) {
        this(capacity, maxTransfer, maxTransfer, onChange);
    }

    public ModEnergyStorage(int capacity, int maxReceive, int maxExtract, Runnable onChange) {
        super(capacity, maxReceive, maxExtract);
        this.onChange = onChange;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int received = super.receiveEnergy(maxReceive, simulate);
        if (received > 0 && !simulate) {
            onEnergyChanged();
        }
        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extracted = super.extractEnergy(maxExtract, simulate);
        if (extracted > 0 && !simulate) {
            onEnergyChanged();
        }
        return extracted;
    }

    public void setEnergyStored(int energy) {
        this.energy = Mth.clamp(energy, 0, this.capacity);
        onEnergyChanged();
    }

    protected void onEnergyChanged() {
        if (this.onChange != null) {
            this.onChange.run();
        }
    }
}
