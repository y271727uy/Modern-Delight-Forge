package com.y271727uy.moderndelight.block.biogas;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.screen.custom.BiogasDigesterControllerScreenHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import javax.annotation.Nullable;

public class BiogasDigesterControllerBlockEntity extends BlockEntity implements MenuProvider {
    protected final ContainerData propertyDelegate;
    private int yCounter = 1;
    private int maxXCounter = 0;
    private int maxZCounter = 0;
    private int minXCounter = 0;
    private int minZCounter = 0;
    private int checked = 0;
    private int size = 0;
    private int gasValue = 0;
    private int maxGasValue = 0;

    public BiogasDigesterControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BIOGAS_DIGESTER_CONTROLLER_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BiogasDigesterControllerBlockEntity.this.checked;
                    case 1 -> BiogasDigesterControllerBlockEntity.this.size;
                    case 2 -> BiogasDigesterControllerBlockEntity.this.gasValue & 0xFFFF;
                    case 3 -> BiogasDigesterControllerBlockEntity.this.gasValue >>> 16;
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

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("biogas_digester_controller.gasValue", gasValue);
        nbt.putInt("biogas_digester_controller.maxGasValue", maxGasValue);
    }

    public boolean isChecked() {
        return checked != 0;
    }

    public int getGasValue() {
        return gasValue;
    }

    public void addGas(int value) {
        gasValue += value;
    }

    public void reduceGas(int value) {
        if (gasValue > value) {
            gasValue -= value;
        } else {
            gasValue = 0;
        }
    }

    public int getCurrentSize() {
        return size;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        gasValue = nbt.getInt("biogas_digester_controller.gasValue");
        maxGasValue = nbt.getInt("biogas_digester_controller.maxGasValue");
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public Component getDisplayName() {
        return ModBlocks.BIOGAS_DIGESTER_CONTROLLER.get().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new BiogasDigesterControllerScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    private int time = 60;

    public void tick(Level world, BlockPos pos) {
        if (world.isClientSide) {
            return;
        }
        time--;
        if (time == 10) {
            if (check(world)) {
                checked = 1;
            } else {
                checked = 0;
                size = 0;
            }
            restAll();
        }
        if (checked == 1) {
            maxGasValue = size * 1000;
            setChanged();
        } else {
            maxGasValue = 0;
            setChanged();
        }
        if (gasValue > maxGasValue && time == 1) {
            if (gasValue >= 1000) {
                createExplode(world);
            } else {
                if (gasValue > 500) {
                    world.setBlock(worldPosition.below(), ModBlocks.LIQUEFIED_BIOGAS_FLUID_BLOCK.get().defaultBlockState(), 3);
                }
                gasValue = 0;
            }
        }
        if (time <= 0) {
            time = 60;
        }
    }

    public void createExplode(Level world) {
        if (gasValue < 6000) {
            world.setBlock(worldPosition.below(), ModBlocks.LIQUEFIED_BIOGAS_FLUID_BLOCK.get().defaultBlockState(), 3);
            world.setBlock(worldPosition.below(2), ModBlocks.LIQUEFIED_BIOGAS_FLUID_BLOCK.get().defaultBlockState(), 3);
            world.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 3.5f, false, Level.ExplosionInteraction.BLOCK);
        } else {
            world.setBlock(worldPosition.west(), ModBlocks.LIQUEFIED_BIOGAS_FLUID_BLOCK.get().defaultBlockState(), 3);
            world.setBlock(worldPosition.east(), ModBlocks.LIQUEFIED_BIOGAS_FLUID_BLOCK.get().defaultBlockState(), 3);
            world.setBlock(worldPosition.north(), ModBlocks.LIQUEFIED_BIOGAS_FLUID_BLOCK.get().defaultBlockState(), 3);
            world.setBlock(worldPosition.south(), ModBlocks.LIQUEFIED_BIOGAS_FLUID_BLOCK.get().defaultBlockState(), 3);
            world.setBlock(worldPosition.above(), ModBlocks.LIQUEFIED_BIOGAS_FLUID_BLOCK.get().defaultBlockState(), 3);
            world.setBlock(worldPosition.below(), ModBlocks.LIQUEFIED_BIOGAS_FLUID_BLOCK.get().defaultBlockState(), 3);
            world.setBlock(worldPosition.below(2), ModBlocks.LIQUEFIED_BIOGAS_FLUID_BLOCK.get().defaultBlockState(), 3);
            world.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), 6.0f, false, Level.ExplosionInteraction.BLOCK);
        }
        setChanged();
    }

    private boolean check(Level world) {
        BlockPos maxPos;
        BlockPos minPos;
        int length;
        int width;
        if (world.getBlockState(worldPosition.below()).isAir()) {
            int maxValue = 5;
            do {
                yCounter++;
                if (!world.getBlockState(worldPosition.below(yCounter)).isAir()) {
                    yCounter--;
                    break;
                }
            } while (yCounter <= maxValue);
            if (!world.getBlockState(worldPosition.below(yCounter + 1)).canOcclude()) {
                return false;
            } else {
                do {
                    maxXCounter++;
                    if (!world.getBlockState(worldPosition.east(maxXCounter).below(yCounter)).isAir()) {
                        maxXCounter--;
                        break;
                    }
                } while (maxXCounter <= maxValue);
                if (!world.getBlockState(worldPosition.east(maxXCounter + 1).below(yCounter)).canOcclude()) {
                    return false;
                } else {
                    do {
                        maxZCounter++;
                        if (!world.getBlockState(worldPosition.south(maxZCounter).below(yCounter)).isAir()) {
                            maxZCounter--;
                            break;
                        }
                    } while (maxZCounter <= maxValue);
                    if (!world.getBlockState(worldPosition.south(maxZCounter + 1).below(yCounter)).canOcclude()) {
                        return false;
                    } else {
                        maxPos = new BlockPos(worldPosition.getX() + maxXCounter,
                                worldPosition.getY() - yCounter, worldPosition.getZ() + maxZCounter);
                        do {
                            minXCounter++;
                            if (!world.getBlockState(worldPosition.west(minXCounter).below()).isAir()) {
                                minXCounter--;
                                break;
                            }
                        } while (minXCounter <= maxValue);
                        if (!world.getBlockState(worldPosition.west(minXCounter + 1).below()).canOcclude()) {
                            return false;
                        } else {
                            do {
                                minZCounter++;
                                if (!world.getBlockState(worldPosition.north(minZCounter).below()).isAir()) {
                                    minZCounter--;
                                    break;
                                }
                            } while (minZCounter <= maxValue);
                            if (!world.getBlockState(worldPosition.north(minZCounter + 1).below()).canOcclude()) {
                                return false;
                            } else {
                                minPos = new BlockPos(worldPosition.getX() - minXCounter,
                                        worldPosition.getY() - 1, worldPosition.getZ() - minZCounter);
                                length = maxZCounter + minZCounter + 1;
                                width = maxXCounter + minXCounter + 1;
                                BlockPos up = minPos.above();
                                for (int s = 0; s < length; s++) {
                                    for (int e = 0; e < width; e++) {
                                        if (world.getBlockState(new BlockPos(up.getX() + e, up.getY(), up.getZ() + s)).getBlock() == ModBlocks.BIOGAS_DIGESTER_CONTROLLER.get()) {
                                            if (!(up.getX() + e == worldPosition.getX() && up.getZ() + s == worldPosition.getZ())) {
                                                return false;
                                            }
                                        }
                                        if (!world.getBlockState(new BlockPos(up.getX() + e, up.getY(), up.getZ() + s)).canOcclude()) {
                                            return false;
                                        }
                                    }
                                }
                                BlockPos north = minPos.north();
                                for (int e = 0; e < length; e++) {
                                    for (int d = 0; d < yCounter; d++) {
                                        if (!world.getBlockState(new BlockPos(north.getX() + e, north.getY() - d, north.getZ())).canOcclude()) {
                                            return false;
                                        }
                                    }
                                }
                                BlockPos west = minPos.west();
                                for (int d = 0; d < yCounter; d++) {
                                    for (int s = 0; s < length; s++) {
                                        if (!world.getBlockState(new BlockPos(west.getX(), west.getY() - d, west.getZ() + s)).canOcclude()) {
                                            return false;
                                        }
                                    }
                                }
                                BlockPos down = maxPos.below();
                                for (int n = 0; n < length; n++) {
                                    for (int w = 0; w < width; w++) {
                                        if (!world.getBlockState(new BlockPos(down.getX() - w, down.getY(), down.getZ() - n)).canOcclude()) {
                                            return false;
                                        }
                                    }
                                }
                                BlockPos east = maxPos.east();
                                for (int n = 0; n < length; n++) {
                                    for (int u = 0; u < yCounter; u++) {
                                        if (!world.getBlockState(new BlockPos(east.getX(), east.getY() + u, east.getZ() - n)).canOcclude()) {
                                            return false;
                                        }
                                    }
                                }
                                BlockPos south = maxPos.south();
                                for (int u = 0; u < yCounter; u++) {
                                    for (int w = 0; w < width; w++) {
                                        if (!world.getBlockState(new BlockPos(south.getX() - w, south.getY() + u, south.getZ())).canOcclude()) {
                                            return false;
                                        }
                                    }
                                }
                                for (int u = 0; u < yCounter; u++) {
                                    for (int e = 0; e < width; e++) {
                                        for (int n = 0; n < length; n++) {
                                            if (!world.getBlockState(new BlockPos(maxPos.getX() - e, maxPos.getY() + u, maxPos.getZ() - n)).isAir()) {
                                                return false;
                                            }
                                        }
                                    }
                                }
                                size = length * width * yCounter;
                                return true;
                            }
                        }
                    }
                }
            }
        } else {
            return false;
        }
    }

    private void restAll() {
        yCounter = 1;
        maxXCounter = 0;
        maxZCounter = 0;
        minXCounter = 0;
        minZCounter = 0;
    }
}


