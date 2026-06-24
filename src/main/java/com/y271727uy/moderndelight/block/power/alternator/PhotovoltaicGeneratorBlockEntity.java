package com.y271727uy.moderndelight.block.power.alternator;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.power.batteries.AbstractBatteryBlock;
import com.y271727uy.moderndelight.screen.custom.PhotovoltaicGeneratorScreenHandler;
import com.y271727uy.moderndelight.util.ModConfig;
import com.y271727uy.moderndelight.util.energy.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PhotovoltaicGeneratorBlockEntity extends BlockEntity implements MenuProvider, net.minecraft.world.Container {
    public PhotovoltaicGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PHOTOVOLTAIC_GENERATOR_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0 -> PhotovoltaicGeneratorBlockEntity.this.isWorking;
                    case 1 -> PhotovoltaicGeneratorBlockEntity.this.energyStorage.getEnergyStored();
                    case 2 -> PhotovoltaicGeneratorBlockEntity.this.energyStorage.getMaxEnergyStored();
                    case 3 -> PhotovoltaicGeneratorBlockEntity.this.slowMode;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0 -> PhotovoltaicGeneratorBlockEntity.this.isWorking = value;
                    case 3 -> PhotovoltaicGeneratorBlockEntity.this.slowMode = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }
    
    public final ModEnergyStorage energyStorage = new ModEnergyStorage(30000, 1000, 1000, this::setChanged);
    private final LazyOptional<IEnergyStorage> energyHandler = LazyOptional.of(() -> energyStorage);
    
    private final List<ItemStack> inventory = new ArrayList<>(List.of(ItemStack.EMPTY));
    private final ContainerData propertyDelegate;
    private int isWorking = 0;
    private int slowMode = 0;
    
    public static final String PHOTOVOLTAIC_GENERATOR_NAME = "display_name.moderndelight.photovoltaic_generator_name";
    
    public void tick(Level world, PhotovoltaicGeneratorBlockEntity blockEntity) {
        if (world.isClientSide){
            return;
        }
        if (world.getGameTime() % 20L == 0L){
            ItemStack itemStack = blockEntity.getItem(0);
            blockEntity.setItem(0,
                    AbstractBatteryBlock.transferEnergyBetween(itemStack, energyStorage, 800, true));
            int light = world.getBrightness(LightLayer.BLOCK, blockEntity.getBlockPos()) / 3 - 1;
            if (light > 0){
                blockEntity.energyStorage.receiveEnergy((int) (light * 10L), false);
                blockEntity.slowMode = 1;
            } else {
                blockEntity.slowMode = 0;
            }
            if (isInOpenAir(world)){
                if (isEarlyMorningOrTwilight(world)){
                    blockEntity.addPowerAndCheck(1, world);
                    if (world.isThundering() || world.isRaining()) blockEntity.isWorking = 0;
                    else blockEntity.isWorking = 1;
                } else if (isMorningOrAfternoon(world)) {
                    blockEntity.addPowerAndCheck(2, world);
                    if (world.isThundering()) blockEntity.isWorking = 0;
                    else blockEntity.isWorking = 1;
                } else if (isNoon(world)) {
                    blockEntity.addPowerAndCheck(3, world);
                    blockEntity.isWorking = 1;
                } else {
                    blockEntity.isWorking = 0;
                }
            } else {
                blockEntity.isWorking = 0;
            }
            setChanged();
        }
    }

    private void addPowerAndCheck(int multiplier, Level world){
        int y = getBlockPos().getY() / 2;
        double var = multiplier * getMultiplier();
        if (var <= 0){
            return;
        }
        if (world.isThundering()){
            if (var - 3 >= 0){
                var -= 3;
            } else {
                return;
            }
        } else if (world.isRaining()) {
            var--;
        }
        if (y / 10 > 0){
            this.energyStorage.receiveEnergy((int) Math.min(Integer.MAX_VALUE, Math.max(0L, (long) (var * y))), false);
        } else {
            this.energyStorage.receiveEnergy((int) Math.min(Integer.MAX_VALUE, Math.max(0L, (long) (var * 10))), false);
        }
    }
    
    private boolean isInOpenAir(Level world){
        return world.getBrightness(LightLayer.SKY, getBlockPos()) >= 13 && !world.dimensionType().fixedTime().isPresent() &&
                !world.getBlockState(getBlockPos().above()).isFaceSturdy(world, getBlockPos().above(), Direction.DOWN);
    }
    
    public static boolean isEarlyMorningOrTwilight(Level world){
        long timeOfDay = world.dayTime() % 24000L;
        return (timeOfDay >= 0 && timeOfDay < 167) || (timeOfDay >= 11617 && timeOfDay < 13702) || (timeOfDay >= 23000);
    }
    
    public static boolean isMorningOrAfternoon(Level world){
        long timeOfDay = world.dayTime() % 24000L;
        return (timeOfDay >= 167 && timeOfDay < 4283) || (timeOfDay >= 7700 && timeOfDay < 11617);
    }
    
    public static boolean isNoon(Level world){
        long timeOfDay = world.dayTime() % 24000L;
        return timeOfDay >= 4283 && timeOfDay < 7700;
    }
    
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        // Save inventory manually
        for (int i = 0; i < inventory.size(); i++) {
            nbt.put("Item" + i, inventory.get(i).save(new CompoundTag()));
        }
        nbt.putInt("photovoltaic_generator.energy", this.energyStorage.getEnergyStored());
        nbt.putLong("photovoltaic_generator.power", this.energyStorage.getEnergyStored() / 10L);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        // Load inventory manually
        for (int i = 0; i < inventory.size(); i++) {
            String key = "Item" + i;
            if (nbt.contains(key)) {
                inventory.set(i, ItemStack.of(nbt.getCompound(key)));
            }
        }
        if (nbt.contains("photovoltaic_generator.energy")) {
            this.energyStorage.setEnergyStored(nbt.getInt("photovoltaic_generator.energy"));
        } else if (nbt.contains("photovoltaic_generator.power")) {
            this.energyStorage.setEnergyStored((int) Math.min(Integer.MAX_VALUE, nbt.getLong("photovoltaic_generator.power") * 10L));
        } else {
            this.energyStorage.setEnergyStored(0);
        }
        setChanged();
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(PHOTOVOLTAIC_GENERATOR_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new PhotovoltaicGeneratorScreenHandler(syncId, playerInventory, this, propertyDelegate);
    }

    // Container interface methods
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
    
    public static float getMultiplier(){
        try {
            float value = (float) ModConfig.getPhotovoltaicGeneratorMultiplier();
            if (value > 0){
                return value;
            } else return 2.0f;
        } catch (Throwable e){
            return 2.0f;
        }
    }
}
