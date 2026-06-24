package com.y271727uy.moderndelight.block.biogas;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.screen.custom.BiogasDigesterIOScreenHandler;
import com.y271727uy.moderndelight.util.ModConfig;
import com.y271727uy.moderndelight.util.block_util.ImplementedInventory;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Direction;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nullable;

import java.util.Objects;

public class BiogasDigesterIOBlockEntity extends BlockEntity implements ImplementedInventory, MenuProvider {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(12, ItemStack.EMPTY);
    private int progress = 0;
    private int maxProgress = 0;
    private int gasValue = 0;
    private int checked = 0;
    private int counter = 0;
    private boolean isCrafting = false;
    private int tempGasValue = 0;
    protected final ContainerData propertyDelegate;

    public BiogasDigesterIOBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BIOGAS_DIGESTER_IO_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BiogasDigesterIOBlockEntity.this.progress;
                    case 1 -> BiogasDigesterIOBlockEntity.this.maxProgress;
                    case 2 -> BiogasDigesterIOBlockEntity.this.gasValue & 0xFFFF;
                    case 3 -> BiogasDigesterIOBlockEntity.this.checked;
                    case 4 -> BiogasDigesterIOBlockEntity.this.gasValue >>> 16;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putInt("biogas_digester_io.progress", progress);
        nbt.putInt("biogas_digester_io.maxProgress", maxProgress);
        nbt.putBoolean("biogas_digester_io.isCrafting", isCrafting);
        nbt.putInt("biogas_digester_io.tempGasValue", tempGasValue);
        nbt.putInt("biogas_digester_io.counter", counter);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        ContainerHelper.loadAllItems(nbt, inventory);
        progress = nbt.getInt("biogas_digester_io.progress");
        maxProgress = nbt.getInt("biogas_digester_io.maxProgress");
        isCrafting = nbt.getBoolean("biogas_digester_io.isCrafting");
        tempGasValue = nbt.getInt("biogas_digester_io.tempGasValue");
        counter = nbt.getInt("biogas_digester_io.counter");
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }


    @Override
    public Component getDisplayName() {
        return ModBlocks.BIOGAS_DIGESTER_IO.get().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new BiogasDigesterIOScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public void tick(Level world, BlockPos pos, BiogasDigesterIOBlockEntity blockEntity) {
        if (world.isClientSide) {
            return;
        }
        if (world.getBlockEntity(pos.below()) instanceof BiogasDigesterControllerBlockEntity entity) {
            if (entity.isChecked()) {
                blockEntity.checked = 1;
                blockEntity.gasValue = entity.getGasValue();
                blockEntity.maxProgress = entity.getCurrentSize();
                if (!blockEntity.isCrafting) {
                    for (int i = 0; i < 9; i++) {
                        Item item = this.getItem(i).getItem();
                        if (item.isEdible()) {
                            FoodProperties component = item.getFoodProperties();
                            blockEntity.tempGasValue = (int) (Objects.requireNonNull(component).getNutrition() * 30 + component.getSaturationModifier() * 10);
                            this.removeItem(i, 1);
                            blockEntity.isCrafting = true;
                            break;
                        }
                    }
                } else {
                    inCreaseProgress();
                    if (blockEntity.progress == blockEntity.maxProgress) {
                        entity.addGas(tempGasValue);
                        resetProgress();
                        blockEntity.counter += world.random.nextInt(5);
                        if (blockEntity.counter >= 32) {
                            putItem(world);
                            blockEntity.counter = 0;
                        }
                        blockEntity.tempGasValue = 0;
                        blockEntity.isCrafting = false;
                    }
                }
                setChanged();
            }
        } else {
            blockEntity.checked = 0;
            blockEntity.gasValue = 0;
            blockEntity.maxProgress = 0;
            blockEntity.tempGasValue = 0;
            blockEntity.isCrafting = false;
            resetProgress();
            setChanged();
        }
    }

    @Override
    public void setChanged() {
        if (level != null) {
            level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
        }
        super.setChanged();
    }

    private void putItem(Level world) {
        boolean hasSpawn = false;
        for (int i = 9; i < 12; i++) {
            if (this.getItem(i).isEmpty()) {
                this.setItem(i, getDigestate().getDefaultInstance());
                hasSpawn = true;
                break;
            } else if (this.getItem(i).getItem().equals(getDigestate())) {
                int count = this.getItem(i).getCount();
                if (count < this.getItem(i).getMaxStackSize()) {
                    this.setItem(i, new ItemStack(getDigestate(), count + 1));
                    hasSpawn = true;
                    break;
                }
            }
        }
        if (!hasSpawn) {
            Containers.dropItemStack(world, worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), getDigestate().getDefaultInstance());
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private void inCreaseProgress() {
        progress++;
    }


    public static Item getDigestate() {
        try {
            String value = ModConfig.getDigestate();
            ResourceLocation id = ResourceLocation.tryParse(value);
            return id != null ? ForgeRegistries.ITEMS.getValue(id) : Items.BONE_MEAL;
        } catch (Throwable ignored) {
            return Items.BONE_MEAL;
        }
    }
}



