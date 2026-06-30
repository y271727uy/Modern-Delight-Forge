package com.y271727uy.moderndelight.block.power;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.power.batteries.AbstractBatteryBlock;
import com.y271727uy.moderndelight.item.tools.ElectricWhiskItem;
import com.y271727uy.moderndelight.networking.packet.ItemStackSyncS2CPacket;
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
        if (world.isClientSide) {
            return;
        }
        if (world.getGameTime() % 20L == 0) {
            if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
                if (!drainBatterySlot(0)) {
                    drainBatterySlot(1);
                }
            }
            isWorking = chargeSlotItem(getItem(2)) ? 1 : 0;
            setChanged();
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

    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void setChanged() {
        if (level != null && !level.isClientSide) {
            ItemStackSyncS2CPacket.send(worldPosition, inventory, level);
        }
        super.setChanged();
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

    private boolean drainBatterySlot(int slot) {
        ItemStack stack = getItem(slot);
        if (stack.isEmpty()) {
            return false;
        }

        int transfer = getBatteryTransferAmount(stack);
        if (transfer <= 0) {
            return false;
        }

        IEnergyStorage itemEnergy = stack.getCapability(ForgeCapabilities.ENERGY).resolve().orElse(null);
        if (itemEnergy != null && itemEnergy.canExtract()) {
            int received = this.energyStorage.receiveEnergy(itemEnergy.extractEnergy(transfer, true), false);
            if (received > 0) {
                itemEnergy.extractEnergy(received, false);
                setChanged();
                return true;
            }
        }

        setItem(slot, AbstractBatteryBlock.transferEnergyBetween(stack, energyStorage, transfer, false));
        return true;
    }

    private static int getBatteryTransferAmount(ItemStack stack) {
        int batteryEnergy = AbstractBatteryBlock.getBatteryEnergy(stack);
        if (batteryEnergy >= 1000) {
            return 1000;
        }
        if (batteryEnergy >= 100) {
            return 100;
        }
        if (batteryEnergy >= 10) {
            return 10;
        }
        return 0;
    }

    private boolean chargeSlotItem(ItemStack stack) {
        if (chargeElectricWhisk(stack)) {
            return true;
        }

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

    private boolean chargeElectricWhisk(ItemStack stack) {
        if (!(stack.getItem() instanceof ElectricWhiskItem)) {
            return false;
        }
        int need = ElectricWhiskItem.MAX_ENERGY - ElectricWhiskItem.getStoredEnergy(stack);
        if (need <= 0) {
            return false;
        }

        int bulkCost = 300;
        int bulkReceived = Math.max(1, Math.round(bulkCost * getEfficiency()));
        if (need >= bulkReceived && this.energyStorage.getEnergyStored() >= bulkCost) {
            this.energyStorage.extractEnergy(bulkCost, false);
            ElectricWhiskItem.addStoredEnergy(stack, Math.min(need, bulkReceived));
            return true;
        }

        if (this.energyStorage.getEnergyStored() >= 10) {
            this.energyStorage.extractEnergy(10, false);
            ElectricWhiskItem.addStoredEnergy(stack, Math.min(need, 10));
            return true;
        }
        return false;
    }
}
