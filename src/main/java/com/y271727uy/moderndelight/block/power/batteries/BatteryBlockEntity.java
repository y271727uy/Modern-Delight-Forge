package com.y271727uy.moderndelight.block.power.batteries;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.util.energy.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BatteryBlockEntity extends BlockEntity {

    public BatteryBlockEntity(BlockPos pos, BlockState state, long maxPower) {
        super(ModBlockEntities.BATTERY_BLOCK_ENTITY.get(), pos, state);
        this.energyStorage = new ModEnergyStorage(toEnergy(maxPower), 1000, 1000, this::setChanged);
        this.energyHandler = LazyOptional.of(() -> energyStorage);
    }
    
    public BatteryBlockEntity(BlockPos pos, BlockState state) {
        this(pos, state, getMaxPowerFromState(state));
    }
    
    public final ModEnergyStorage energyStorage;
    private final LazyOptional<IEnergyStorage> energyHandler;

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putLong("battery.maxPower", getMaxPowerFromState(getBlockState()));
        nbt.putLong("battery.power", this.energyStorage.getEnergyStored() / 10L);
        nbt.putInt("battery.energy", this.energyStorage.getEnergyStored());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("battery.energy")) {
            this.energyStorage.setEnergyStored(nbt.getInt("battery.energy"));
        } else if (nbt.contains("battery.power")) {
            this.energyStorage.setEnergyStored((int) Math.min(Integer.MAX_VALUE, nbt.getLong("battery.power") * 10L));
        } else {
            this.energyStorage.setEnergyStored(0);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    public void tick(Level world, BlockPos pos) {
        if (!world.isClientSide){
            return;
        }
        AbstractBatteryBlock self = (AbstractBatteryBlock) world.getBlockState(pos).getBlock();
        int maxEnergy = toEnergy(self.getMaxPower());
        if (energyStorage.getEnergyStored() > maxEnergy){
            energyStorage.setEnergyStored(maxEnergy);
        }
    }

    @Override
    public <T> @Nonnull LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && cap == ForgeCapabilities.ENERGY) {
            return this.energyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.energyHandler.invalidate();
    }

    private static long getMaxPowerFromState(BlockState state) {
        if (state.getBlock() instanceof AbstractBatteryBlock batteryBlock) {
            return batteryBlock.getMaxPower();
        }
        return 5000L;
    }

    private static int toEnergy(long power) {
        return (int) Math.min(Integer.MAX_VALUE, Math.max(0L, power * 10L));
    }
}
