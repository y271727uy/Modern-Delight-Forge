package com.y271727uy.moderndelight.block.biogas;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.fluid.ModFluid;
import com.y271727uy.moderndelight.screen.custom.GasCanisterScreenHandler;
import com.y271727uy.moderndelight.sound.ModSounds;
import com.y271727uy.moderndelight.tag.TagKeys;
import com.y271727uy.moderndelight.util.FluidStack;
import com.y271727uy.moderndelight.util.ModConfig;
import com.y271727uy.moderndelight.util.fluid.FluidVariant;
import com.y271727uy.moderndelight.util.fluid.SingleVariantStorage;
import com.y271727uy.moderndelight.util.fluid.Transaction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import javax.annotation.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GasCanisterBlockEntity extends BlockEntity implements MenuProvider {
    private int gasValue = 0;
    private int cycleInt = 0;
    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidStack.convertMbToDroplets(getMaxCapacity());
        }

        @Override
        protected void onFinalCommit() {
            setChanged();
        }
    };
    public static final String GAS_CANISTER_NAME = "display_name.moderndelight.gas_canister_name";
    protected final ContainerData propertyDelegate;

    public GasCanisterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GAS_CANISTER_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> GasCanisterBlockEntity.this.gasValue;
                    case 1 -> GasCanisterBlockEntity.this.cycleInt;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> GasCanisterBlockEntity.this.gasValue = value;
                    case 1 -> GasCanisterBlockEntity.this.cycleInt = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    private int tick = 20;

    public void tick(Level world, BlockPos pos, BlockState state, GasCanisterBlockEntity blockEntity) {
        if (!world.isClientSide) {
            return;
        }
        gasValue = (int) FluidStack.convertDropletsToMb(fluidStorage.amount);
        blockEntity.tick--;
        switch (tick) {
            case 20, 3 -> cycleInt = 0;
            case 17, 7 -> cycleInt = 1;
            case 15, 10 -> cycleInt = 2;
            case 12 -> cycleInt = 3;
            case 0 -> tick = 20;
        }
        if (allowExplode() && fluidIsGas()) {
            if (isDangerBlock(world.getBlockState(worldPosition.below()).getBlock()) ||
                    isDangerBlock(world.getBlockState(worldPosition.above()).getBlock()) ||
                    isDangerBlock(world.getBlockState(worldPosition.north()).getBlock()) ||
                    isDangerBlock(world.getBlockState(worldPosition.south()).getBlock()) ||
                    isDangerBlock(world.getBlockState(worldPosition.west()).getBlock()) ||
                    isDangerBlock(world.getBlockState(worldPosition.east()).getBlock())) {
                randomExplode(world);
            } else if (world.dimensionType().ultraWarm() && !allowNether()) {
                randomExplode(world);
            } else if (fluidStorage.amount >= FluidStack.convertMbToDroplets(getMaxCapacity())) {
                world.setBlock(worldPosition, Blocks.AIR.defaultBlockState(), 3);
                world.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), getGasValue() / 1000f, false, Level.ExplosionInteraction.BLOCK);
            }
        }

        Direction direction = state.getValue(GasCanisterBlock.FACING);
        BlockPos facingBlock = worldPosition;
        BlockPos underBlock = worldPosition;
        switch (direction) {
            case EAST -> {
                facingBlock = worldPosition.east(1);
                underBlock = worldPosition.east().below();
            }
            case SOUTH -> {
                facingBlock = worldPosition.south(1);
                underBlock = worldPosition.south().below();
            }
            case WEST -> {
                facingBlock = worldPosition.west(1);
                underBlock = worldPosition.west().below();
            }
            case NORTH -> {
                facingBlock = worldPosition.north(1);
                underBlock = worldPosition.north().below();
            }
        }

        if ((fluidIsGas() || fluidStorage.variant.isBlank()) &&
                world.getBlockState(facingBlock).getBlock().equals(ModBlocks.BIOGAS_DIGESTER_IO.get()) &&
                world.getBlockEntity(underBlock) instanceof BiogasDigesterControllerBlockEntity entity) {
            if (gasValue < getMaxCapacity()) {
                if (entity.getGasValue() >= 5) {
                    playSound(ModSounds.BLOCK_GAS_CANISTER_FILLING.get(), 0.5f, 0.8f);
                    entity.reduceGas(5);
                    try (Transaction transaction = Transaction.openOuter()) {
                        fluidStorage.insert(FluidVariant.of(ModFluid.STILL_LIQUEFIED_BIOGAS.get()),
                                FluidStack.convertMbToDroplets(5), transaction);
                        transaction.commit();
                    }
                    setChanged();
                } else if (entity.getGasValue() > 0) {
                    playSound(ModSounds.BLOCK_GAS_CANISTER_FILLING.get(), 0.5f, 0.8f);
                    entity.reduceGas(1);
                    try (Transaction transaction = Transaction.openOuter()) {
                        fluidStorage.insert(FluidVariant.of(ModFluid.STILL_LIQUEFIED_BIOGAS.get()),
                                FluidStack.convertMbToDroplets(1), transaction);
                        transaction.commit();
                    }
                    setChanged();
                }
            }
        }
    }

    public boolean fluidIsGas() {
        return fluidIsGas(fluidStorage.variant.getFluid());
    }

    public static boolean fluidIsGas(Fluid fluid) {
        return fluid.is(TagKeys.GAS);
    }

    public static boolean isDangerBlock(Block block) {
        return block.builtInRegistryHolder().is(TagKeys.DANGER_BLOCKS) && 
               block != Blocks.CAULDRON && 
               block != ModBlocks.BURNING_GAS_COOKING_STOVE.get();
    }

    private float explodeTime = 0;

    public void randomExplode(Level world) {
        if (gasValue > 999 && fluidIsGas()) {
            explodeTime += world.random.nextFloat();
            if (explodeTime > 60) {
                world.setBlock(worldPosition, Blocks.AIR.defaultBlockState(), 3);
                world.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), (float) (gasValue * 5 / getMaxCapacity()), false, Level.ExplosionInteraction.BLOCK);
            }
        }
    }

    public void instantExplode(Level world) {
        if (gasValue > 999 && fluidIsGas()) {
            world.setBlock(worldPosition, Blocks.AIR.defaultBlockState(), 3);
            world.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), (float) (gasValue * 5 / getMaxCapacity()), false, Level.ExplosionInteraction.BLOCK);
        }
    }

    public void use(Player player, Level world) {
        if (gasValue > 999 && fluidIsGas()) {
            ItemStack offHand = player.getOffhandItem();
            ItemStack mainHand = player.getMainHandItem();
            
            if (offHand.is(Items.FLINT_AND_STEEL)) {
                offHand.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
                playSound(SoundEvents.FLINTANDSTEEL_USE, 1.0f, 1.0f);
                instantExplode(world);
            } else if (mainHand.is(Items.FLINT_AND_STEEL)) {
                mainHand.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
                playSound(SoundEvents.FLINTANDSTEEL_USE, 1.0f, 1.0f);
                instantExplode(world);
            } else if (offHand.is(Items.FIRE_CHARGE)) {
                offHand.shrink(1);
                playSound(SoundEvents.FIRECHARGE_USE, 1.0f, 1.0f);
                instantExplode(world);
            } else if (mainHand.is(Items.FIRE_CHARGE)) {
                mainHand.shrink(1);
                playSound(SoundEvents.FIRECHARGE_USE, 1.0f, 1.0f);
                instantExplode(world);
            }
        }
    }

    public void playSound(SoundEvent sound, float volume, float pitch) {
        if (level != null) {
            level.playSound(null,
                    worldPosition.getX() + .5f, worldPosition.getY() + .5f, worldPosition.getZ() + .5f,
                    sound, SoundSource.BLOCKS, volume, pitch);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putLong("gas_canister.fluid_amount", fluidStorage.amount);
        nbt.put("gas_canister.fluid_variant", fluidStorage.variant.toNbt());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        fluidStorage.variant = FluidVariant.fromNbt(nbt.getCompound("gas_canister.fluid_variant"));
        fluidStorage.amount = nbt.getLong("gas_canister.fluid_amount");
        setChanged();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(GAS_CANISTER_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new GasCanisterScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public int getGasValue() {
        return gasValue;
    }

    public boolean reduceGas() {
        if (gasValue != 0 && fluidIsGas()) {
            fluidStorage.amount -= 27;
            setChanged();
            return true;
        } else return false;
    }

    @Override
    public void setChanged() {
        if (level != null) {
            level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
        }
        super.setChanged();
    }

    public static int getMaxCapacity() {
        try {
            int value = ModConfig.getGasCanisterVolume();
            if (value > 0) {
                return value;
            } else return 6000;
        } catch (Throwable e) {
            return 6000;
        }
    }

    public static boolean allowExplode() {
        try {
            return ModConfig.isAllowGasCanisterExplode();
        } catch (Throwable e) {
            return true;
        }
    }

    public static boolean allowNether() {
        try {
            return ModConfig.isAllowGasCanisterInNether();
        } catch (Throwable e) {
            return false;
        }
    }


}



