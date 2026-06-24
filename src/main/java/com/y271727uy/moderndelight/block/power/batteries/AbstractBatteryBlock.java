package com.y271727uy.moderndelight.block.power.batteries;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlockEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class AbstractBatteryBlock extends BaseEntityBlock {
    public static final String BATTERY_POWER_TAG = "battery.power";
    public static final String BATTERY_MAX_POWER_TAG = "battery.maxPower";
    public static final String BATTERY_ENERGY_TAG = "battery.energy";

    public AbstractBatteryBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    public abstract long getMaxPower();

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && !player.isCreative() && level.getBlockEntity(pos) instanceof BatteryBlockEntity blockEntity) {
            ItemStack stack = new ItemStack(getBlock().asItem());
            writeBlockEntityData(stack, blockEntity, this);
            ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        ItemStack stack = new ItemStack(getBlock().asItem());
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BatteryBlockEntity batteryBlockEntity) {
            writeBlockEntityData(stack, batteryBlockEntity, this);
        } else {
            setBatteryEnergy(stack, 0);
        }
        return stack;
    }

    private static void writeBlockEntityData(ItemStack stack, BatteryBlockEntity blockEntity, AbstractBatteryBlock batteryBlock) {
        CompoundTag tag = blockEntity.saveWithoutMetadata();
        int energy = Mth.clamp(tag.getInt(BATTERY_ENERGY_TAG), 0, getMaxEnergy(batteryBlock));
        tag.putInt(BATTERY_ENERGY_TAG, energy);
        tag.putLong(BATTERY_POWER_TAG, energy / 10L);
        tag.putLong(BATTERY_MAX_POWER_TAG, batteryBlock.getMaxPower());
        BlockItem.setBlockEntityData(stack, ModBlockEntities.BATTERY_BLOCK_ENTITY.get(), tag);
    }

    public static int getBatteryEnergy(ItemStack batteryItem) {
        if (batteryItem.getItem() instanceof BlockItem blockItem){
            if (blockItem.getBlock() instanceof AbstractBatteryBlock batteryBlock){
                CompoundTag nbt = BlockItem.getBlockEntityData(batteryItem);
                if (nbt != null) {
                    int maxEnergy = getMaxEnergy(batteryBlock);
                    if (nbt.contains(BATTERY_ENERGY_TAG)) {
                        return Mth.clamp(nbt.getInt(BATTERY_ENERGY_TAG), 0, maxEnergy);
                    }
                    if (nbt.contains(BATTERY_POWER_TAG)) {
                        return Mth.clamp(toEnergy(nbt.getLong(BATTERY_POWER_TAG)), 0, maxEnergy);
                    }
                }
            }
        }
        return 0;
    }

    public static int getBatteryMaxEnergy(ItemStack batteryItem) {
        if (batteryItem.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractBatteryBlock batteryBlock) {
            return getMaxEnergy(batteryBlock);
        }
        return 0;
    }

    public static void setBatteryEnergy(ItemStack batteryItem, int energy) {
        if (batteryItem.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractBatteryBlock batteryBlock) {
            CompoundTag nbtCompound = getOrCreateBatteryTag(batteryItem, blockItem, batteryBlock);
            int clampedEnergy = Mth.clamp(energy, 0, getMaxEnergy(batteryBlock));
            nbtCompound.putInt(BATTERY_ENERGY_TAG, clampedEnergy);
            nbtCompound.putLong(BATTERY_POWER_TAG, clampedEnergy / 10L);
            nbtCompound.putLong(BATTERY_MAX_POWER_TAG, batteryBlock.getMaxPower());
            BlockItem.setBlockEntityData(batteryItem, ModBlockEntities.BATTERY_BLOCK_ENTITY.get(), nbtCompound);
        }
    }

    public static void changeBatteryEnergy(ItemStack batteryItem, long value, boolean isAdd){
        if (batteryItem.getItem() instanceof BlockItem blockItem){
            if (blockItem.getBlock() instanceof AbstractBatteryBlock batteryBlock){
                long batteryEnergy = getBatteryEnergy(batteryItem);
                long maxBatteryEnergy = getMaxEnergy(batteryBlock);
                if (isAdd){
                    if (batteryEnergy + value < maxBatteryEnergy){
                        batteryEnergy += value;
                    } else if (batteryEnergy < maxBatteryEnergy){
                        batteryEnergy ++;
                    }
                } else {
                    if (batteryEnergy - value > 0){
                        batteryEnergy -= value;
                    } else if (batteryEnergy > 0){
                        batteryEnergy --;
                    }
                }
                setBatteryEnergy(batteryItem, (int) batteryEnergy);
            }
        }
    }


    public static void addEnergy(long value, IEnergyStorage energyStorage){
        long remaining = Math.max(0L, value);
        while (remaining > 0L) {
            int inserted = energyStorage.receiveEnergy((int) Math.min(Integer.MAX_VALUE, remaining), false);
            if (inserted <= 0) {
                return;
            }
            remaining -= inserted;
        }
    }

    public static void reduceEnergy(long value, IEnergyStorage energyStorage){
        long remaining = Math.max(0L, value);
        while (remaining > 0L) {
            int extracted = energyStorage.extractEnergy((int) Math.min(Integer.MAX_VALUE, remaining), false);
            if (extracted <= 0) {
                return;
            }
            remaining -= extracted;
        }
    }

    public static ItemStack transferEnergyBetween(ItemStack oldBatteryItemStack, IEnergyStorage energyStorage, long maxTransfer, boolean chargeBattery) {
        if (maxTransfer < 0){
            ModernDelightMain.LOGGER.error("Exception battery energy value: \"{}\" is not a positive number!", maxTransfer);
        }
        ItemStack newStack = oldBatteryItemStack.copy();
        if (oldBatteryItemStack.getItem() instanceof BlockItem blockItem){
            if (blockItem.getBlock() instanceof AbstractBatteryBlock batteryBlock){
                int batteryEnergy = getBatteryEnergy(oldBatteryItemStack);
                int maxBatteryEnergy = getMaxEnergy(batteryBlock);
                if (chargeBattery){
                    long transferred = Math.min(Math.min(maxTransfer, maxBatteryEnergy - batteryEnergy), energyStorage.getEnergyStored());
                    if (transferred > 0L) {
                        reduceEnergy(transferred, energyStorage);
                        setBatteryEnergy(newStack, (int) (batteryEnergy + transferred));
                    }
                } else {
                    long transferred = Math.min(Math.min(maxTransfer, batteryEnergy), energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored());
                    if (transferred > 0L) {
                        addEnergy(transferred, energyStorage);
                        setBatteryEnergy(newStack, (int) (batteryEnergy - transferred));
                    }
                }
            }
        }
        return newStack;
    }

    private static CompoundTag getOrCreateBatteryTag(ItemStack batteryItem, BlockItem blockItem, AbstractBatteryBlock batteryBlock) {
        CompoundTag nbtCompound = BlockItem.getBlockEntityData(batteryItem);
        if (nbtCompound == null) {
            nbtCompound = initNbtCompound(blockItem);
            BlockItem.setBlockEntityData(batteryItem, ModBlockEntities.BATTERY_BLOCK_ENTITY.get(), nbtCompound);
        } else if (!nbtCompound.contains(BATTERY_MAX_POWER_TAG)) {
            nbtCompound.putLong(BATTERY_MAX_POWER_TAG, batteryBlock.getMaxPower());
        }
        int maxEnergy = getMaxEnergy(batteryBlock);
        if (nbtCompound.contains(BATTERY_ENERGY_TAG)) {
            int normalizedEnergy = Mth.clamp(nbtCompound.getInt(BATTERY_ENERGY_TAG), 0, maxEnergy);
            nbtCompound.putInt(BATTERY_ENERGY_TAG, normalizedEnergy);
            nbtCompound.putLong(BATTERY_POWER_TAG, normalizedEnergy / 10L);
        } else if (nbtCompound.contains(BATTERY_POWER_TAG)) {
            int normalizedEnergy = Mth.clamp(toEnergy(nbtCompound.getLong(BATTERY_POWER_TAG)), 0, maxEnergy);
            nbtCompound.putInt(BATTERY_ENERGY_TAG, normalizedEnergy);
            nbtCompound.putLong(BATTERY_POWER_TAG, normalizedEnergy / 10L);
        }
        return nbtCompound;
    }

    private static int getMaxEnergy(AbstractBatteryBlock batteryBlock) {
        return toEnergy(batteryBlock.getMaxPower());
    }

    private static int toEnergy(long power) {
        return (int) Math.min(Integer.MAX_VALUE, Math.max(0L, power * 10L));
    }

    private static CompoundTag initNbtCompound(BlockItem blockItem) {
        CompoundTag newNBT = new CompoundTag();
        newNBT.putLong(BATTERY_POWER_TAG, 0);
        AbstractBatteryBlock block = (AbstractBatteryBlock) blockItem.getBlock();
        newNBT.putLong(BATTERY_MAX_POWER_TAG, block.getMaxPower());
        newNBT.putInt(BATTERY_ENERGY_TAG, 0);
        return newNBT;
    }

    protected abstract Block getBlock();
}
