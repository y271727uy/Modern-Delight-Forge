package com.y271727uy.moderndelight.block.power.alternator;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.power.alternator.thermal_power.FaradayGeneratorBlock;
import com.y271727uy.moderndelight.block.power.alternator.thermal_power.FaradayGeneratorBlockEntity;
import com.y271727uy.moderndelight.block.power.alternator.wind_power.WindTurbineControllerBlock;
import com.y271727uy.moderndelight.block.power.alternator.wind_power.WindTurbineControllerBlockEntity;
import com.y271727uy.moderndelight.block.power.batteries.AbstractBatteryBlock;
import com.y271727uy.moderndelight.screen.custom.ACDCConverterScreenHandler;
import com.y271727uy.moderndelight.util.ModConfig;
import com.y271727uy.moderndelight.util.block_util.power_util.ACConsumer;
import com.y271727uy.moderndelight.util.block_util.power_util.ACGenerateAble;
import com.y271727uy.moderndelight.util.energy.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import java.util.ArrayList;
import java.util.List;

public class ACDCConverterBlockEntity extends BlockEntity implements MenuProvider, net.minecraft.world.Container, ACGenerateAble, ACConsumer {
    public ACDCConverterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AC_DC_CONVERTER_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ACDCConverterBlockEntity.this.energyStorage.getEnergyStored();
                    case 1 -> ACDCConverterBlockEntity.this.energyStorage.getMaxEnergyStored();
                    case 2 -> ACDCConverterBlockEntity.this.isACMode;
                    case 3 -> ACDCConverterBlockEntity.this.workSpeed;
                    case 4 -> ACDCConverterBlockEntity.this.getMaxWorkSpeed();
                    case 5 -> ACDCConverterBlockEntity.this.efficiency;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 2 -> ACDCConverterBlockEntity.this.isACMode = value;
                    case 3 -> ACDCConverterBlockEntity.this.workSpeed = value;
                    case 5 -> ACDCConverterBlockEntity.this.efficiency = value;
                }
            }

            @Override
            public int getCount() {
                return 6;
            }
        };
    }

    private int efficiency = 0;
    private final List<ItemStack> inventory = new ArrayList<>(List.of(ItemStack.EMPTY));
    public final ModEnergyStorage energyStorage = new ModEnergyStorage(30000, 10000, 10000, this::setChanged);
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energyStorage);
    private int isACMode = 0;
    private int workSpeed = 0;
    private final ContainerData propertyDelegate;

    public boolean getIsACMode() {
        return isACMode != 0;
    }

    public void setACMode(boolean value) {
        this.isACMode = value ? 1 : 0;
    }

    public int getWorkSpeed() {
        return workSpeed;
    }

    public int getMaxWorkSpeed() {
        try {
            int maxSpeed = ModConfig.getAcDcConverterMaxWorkSpeed();
            if (maxSpeed >= 1) {
                return maxSpeed;
            }
            return 20;
        } catch (Throwable ignored) {
            return 20;
        }
    }

    public void addWorkSpeed(int value) {
        if (workSpeed + value >= getMaxWorkSpeed()) {
            workSpeed = getMaxWorkSpeed();
        } else {
            workSpeed += value;
        }
    }

    public void reduceWorkSpeed(int value) {
        if (workSpeed - value <= 0) {
            workSpeed = 0;
        } else {
            workSpeed -= value;
        }
    }

    public void tick(Level world, ACDCConverterBlockEntity blockEntity, BlockState state) {
        if (!world.isClientSide) {
            return;
        }
        if (world.getGameTime() % 20L == 0L) {
            ItemStack itemStack = blockEntity.getItem(0);
            if (blockEntity.getIsACMode()) {
                blockEntity.setItem(0, AbstractBatteryBlock.transferEnergyBetween(itemStack, blockEntity.energyStorage,
                        100L * blockEntity.workSpeed, false));
                if (blockEntity.energyStorage.getEnergyStored() <= 0) {
                    blockEntity.efficiency = 0;
                } else if (blockEntity.energyStorage.getEnergyStored() - blockEntity.workSpeed * 100L > 0) {
                    blockEntity.energyStorage.extractEnergy(blockEntity.workSpeed * 100, false);
                    blockEntity.efficiency = blockEntity.workSpeed * 10;
                } else {
                    int extracted = blockEntity.energyStorage.extractEnergy(10, false);
                    blockEntity.efficiency = extracted > 0 ? 1 : 0;
                }
            } else {
                blockEntity.efficiency = 0;
                blockEntity.setItem(0, AbstractBatteryBlock.transferEnergyBetween(itemStack, blockEntity.energyStorage,
                        100L * blockEntity.workSpeed, true));
                Direction thisDir = state.getValue(ACDCConverterBlock.FACING);
                ACGenerateAble inputBlock = null;
                switch (thisDir) {
                    case EAST, WEST -> {
                        if (world.getBlockEntity(blockEntity.getBlockPos().north()) instanceof ACGenerateAble entity && entity.getEfficiency() != 0) {
                            if (blockEntity.checkACGeneratorType(world.getBlockEntity(blockEntity.getBlockPos().north()), world, Direction.NORTH)) {
                                inputBlock = entity;
                            }
                        } else if (world.getBlockEntity(blockEntity.getBlockPos().south()) instanceof ACGenerateAble entity && entity.getEfficiency() != 0) {
                            if (blockEntity.checkACGeneratorType(world.getBlockEntity(blockEntity.getBlockPos().south()), world, Direction.SOUTH)) {
                                inputBlock = entity;
                            }
                        }
                    }
                    case SOUTH, NORTH -> {
                        if (world.getBlockEntity(blockEntity.getBlockPos().west()) instanceof ACGenerateAble entity && entity.getEfficiency() != 0) {
                            if (blockEntity.checkACGeneratorType(world.getBlockEntity(blockEntity.getBlockPos().west()), world, Direction.WEST)) {
                                inputBlock = entity;
                            }
                        } else if (world.getBlockEntity(blockEntity.getBlockPos().east()) instanceof ACGenerateAble entity && entity.getEfficiency() != 0) {
                            if (blockEntity.checkACGeneratorType(world.getBlockEntity(blockEntity.getBlockPos().east()), world, Direction.EAST)) {
                                inputBlock = entity;
                            }
                        }
                    }
                }
                if (inputBlock != null && blockEntity.workSpeed != 0) {
                    long added = (long) ((double) inputBlock.getEfficiency() * (1.0 - (double) blockEntity.workSpeed / ((double) getMaxWorkSpeed() * 3.0))) * 10L;
                    blockEntity.energyStorage.receiveEnergy((int) Math.min(Integer.MAX_VALUE, Math.max(0L, added)), false);
                }
            }
        }
    }

    private boolean checkACGeneratorType(BlockEntity blockEntity, Level world, Direction dirType) {
        if (blockEntity instanceof FaradayGeneratorBlockEntity) {
            return world.getBlockState(blockEntity.getBlockPos()).getValue(FaradayGeneratorBlock.FACING) == dirType.getOpposite();
        } else if (blockEntity instanceof WindTurbineControllerBlockEntity) {
            return world.getBlockState(blockEntity.getBlockPos()).getValue(WindTurbineControllerBlock.FACING) == dirType;
        } else if (blockEntity instanceof ACDCConverterBlockEntity) {
            Direction temp = null;
            switch (dirType) {
                case EAST, WEST -> temp = Direction.NORTH;
                case NORTH, SOUTH -> temp = Direction.EAST;
            }
            if (temp == null) {
                return false;
            }
            return world.getBlockState(blockEntity.getBlockPos()).getValue(WindTurbineControllerBlock.FACING) == temp ||
                    world.getBlockState(blockEntity.getBlockPos()).getValue(WindTurbineControllerBlock.FACING) == temp.getOpposite();
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        for (int i = 0; i < inventory.size(); i++) {
            nbt.put("Item" + i, inventory.get(i).save(new CompoundTag()));
        }
        nbt.putInt("acdcc.energy", this.energyStorage.getEnergyStored());
        nbt.putLong("acdcc.power", this.energyStorage.getEnergyStored() / 10L);
        nbt.putInt("acdcc.isOpen", this.isACMode);
        nbt.putInt("acdcc.workSpeed", this.workSpeed);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        for (int i = 0; i < inventory.size(); i++) {
            String key = "Item" + i;
            if (nbt.contains(key)) {
                inventory.set(i, ItemStack.of(nbt.getCompound(key)));
            }
        }
        if (nbt.contains("acdcc.energy")) {
            this.energyStorage.setEnergyStored(nbt.getInt("acdcc.energy"));
        } else if (nbt.contains("acdcc.power")) {
            this.energyStorage.setEnergyStored((int) Math.min(Integer.MAX_VALUE, nbt.getLong("acdcc.power") * 10L));
        } else {
            this.energyStorage.setEnergyStored(0);
        }
        this.workSpeed = nbt.getInt("acdcc.workSpeed");
        this.isACMode = nbt.getInt("acdcc.isOpen");
        setChanged();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public Component getDisplayName() {
        return ModBlocks.AC_DC_CONVERTER.get().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ACDCConverterScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot >= 0 && slot < inventory.size()) {
            return inventory.get(slot);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot >= 0 && slot < inventory.size()) {
            ItemStack stack = inventory.get(slot);
            if (!stack.isEmpty()) {
                ItemStack result = stack.split(amount);
                if (stack.isEmpty()) {
                    inventory.set(slot, ItemStack.EMPTY);
                }
                setChanged();
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot >= 0 && slot < inventory.size()) {
            ItemStack stack = inventory.get(slot);
            inventory.set(slot, ItemStack.EMPTY);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < inventory.size()) {
            inventory.set(slot, stack);
            setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        inventory.clear();
        setChanged();
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

    @Override
    public long getEfficiency() {
        return this.efficiency;
    }

    @Override
    public long getConsumedValue() {
        return this.workSpeed * 10L;
    }

    @Override
    public boolean isWorking() {
        return !this.getIsACMode() && this.workSpeed != 0;
    }

    @Override
    public void energize() {
        long generated = (long) ((double) getWorkSpeed() * 10 * (1.0 - (double) getWorkSpeed() / ((double) getMaxWorkSpeed() * 3.0))) * 3L;
        this.energyStorage.receiveEnergy((int) Math.min(Integer.MAX_VALUE, Math.max(0L, generated)), false);
    }
}
