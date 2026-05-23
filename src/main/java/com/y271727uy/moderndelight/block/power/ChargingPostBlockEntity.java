package com.y271727uy.moderndelight.block.power;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.power.batteries.AbstractBatteryBlock;
import com.y271727uy.moderndelight.screen.custom.ChargingPostScreenHandler;
import com.y271727uy.moderndelight.util.ModConfig;
import com.y271727uy.moderndelight.util.energy.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChargingPostBlockEntity extends BlockEntity implements net.minecraft.world.Container, MenuProvider {
    public ChargingPostBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHARGING_POST_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ChargingPostBlockEntity.this.isWorking;
                    case 1 -> ChargingPostBlockEntity.this.ticker;
                    case 2 -> ChargingPostBlockEntity.this.energyStorage.getEnergyStored();
                    case 3 -> ChargingPostBlockEntity.this.energyStorage.getMaxEnergyStored();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    public final ModEnergyStorage energyStorage = new ModEnergyStorage(10000, 1000, 1000, this::setChanged);
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energyStorage);
    private final ContainerData propertyDelegate;
    private int isWorking = 0;
    private int ticker = 0;
    private boolean dir = false;

    public float getEfficiency() {
        try {
            float efficiency = (float) ModConfig.getChargingPostEfficiency();
            if (efficiency > 0 && efficiency < 1) {
                return efficiency;
            }
            return 0.9f;
        } catch (Throwable ignored) {
            return 0.9f;
        }
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(inventory, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(inventory, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return net.minecraft.world.Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        inventory.clear();
        setChanged();
    }

    public void tick(Level world) {
        if (!world.isClientSide) {
            return;
        }
        if (world.getGameTime() % 20L == 0) {
            int battery1 = AbstractBatteryBlock.getBatteryEnergy(getItem(0));
            int battery2 = AbstractBatteryBlock.getBatteryEnergy(getItem(1));
            if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
                if (battery1 >= 1000) {
                    setItem(0, AbstractBatteryBlock.transferEnergyBetween(getItem(0), energyStorage, 1000, false));
                } else if (battery1 >= 100) {
                    setItem(0, AbstractBatteryBlock.transferEnergyBetween(getItem(0), energyStorage, 100, false));
                } else if (battery1 >= 10) {
                    setItem(0, AbstractBatteryBlock.transferEnergyBetween(getItem(0), energyStorage, 10, false));
                } else if (battery2 >= 1000) {
                    setItem(1, AbstractBatteryBlock.transferEnergyBetween(getItem(1), energyStorage, 1000, false));
                } else if (battery2 >= 100) {
                    setItem(1, AbstractBatteryBlock.transferEnergyBetween(getItem(1), energyStorage, 100, false));
                } else if (battery2 >= 10) {
                    setItem(1, AbstractBatteryBlock.transferEnergyBetween(getItem(1), energyStorage, 10, false));
                }
            }
            isWorking = chargeSlotItem(getItem(2)) ? 1 : 0;
        }
        if (isWorking != 0) {
            if (!dir) {
                ticker++;
            } else {
                ticker--;
            }
            if (ticker == 0) {
                dir = false;
            }
            if (ticker == 10) {
                dir = true;
            }
        } else {
            ticker = 0;
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putInt("charging_post.energy", this.energyStorage.getEnergyStored());
        nbt.putLong("charging_post.power", this.energyStorage.getEnergyStored() / 10L);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.inventory.clear();
        ContainerHelper.loadAllItems(nbt, inventory);
        if (nbt.contains("charging_post.energy")) {
            this.energyStorage.setEnergyStored(nbt.getInt("charging_post.energy"));
        } else if (nbt.contains("charging_post.power")) {
            this.energyStorage.setEnergyStored((int) Math.min(Integer.MAX_VALUE, nbt.getLong("charging_post.power") * 10L));
        } else {
            this.energyStorage.setEnergyStored(0);
        }
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.literal(ModBlocks.CHARGING_POST.get().getName().getString());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ChargingPostScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public ItemStack getRendererStack() {
        return this.getItem(2);
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

    private boolean chargeSlotItem(ItemStack stack) {
        IEnergyStorage itemEnergy = stack.getCapability(ForgeCapabilities.ENERGY).resolve().orElse(null);
        if (itemEnergy == null || !itemEnergy.canReceive()) {
            return false;
        }
        int needed = itemEnergy.getMaxEnergyStored() - itemEnergy.getEnergyStored();
        if (needed <= 0) {
            return false;
        }

        int bulkCost = 300;
        int bulkReceived = Math.max(1, Math.round(bulkCost * getEfficiency()));
        if (needed >= bulkReceived && this.energyStorage.getEnergyStored() >= bulkCost) {
            int received = itemEnergy.receiveEnergy(Math.min(needed, bulkReceived), false);
            if (received > 0) {
                this.energyStorage.extractEnergy(bulkCost, false);
                return true;
            }
        }

        if (this.energyStorage.getEnergyStored() >= 10) {
            int received = itemEnergy.receiveEnergy(Math.min(needed, 10), false);
            if (received > 0) {
                this.energyStorage.extractEnergy(10, false);
                return true;
            }
        }
        return false;
    }
}
