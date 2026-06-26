package com.y271727uy.moderndelight.block.kitchenware;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.gas_cooking_stove.BurningGasCookingStoveBlockEntity;
import com.y271727uy.moderndelight.screen.custom.AdvanceFurnaceScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class AdvanceFurnaceBlockEntity extends BlockEntity implements MenuProvider, WorldlyContainer {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    private static final int INPUT_SLOT_1 = 0;
    private static final int INPUT_SLOT_2 = 1;
    private static final int INPUT_SLOT_3 = 2;
    private static final int INPUT_SLOT_4 = 3;
    private static final int OUTPUT_SLOT_1 = 4;
    private static final int OUTPUT_SLOT_2 = 5;
    private static final int OUTPUT_SLOT_3 = 6;
    private static final int OUTPUT_SLOT_4 = 7;
    private static final int FUEL_SLOT = 8;
    public static final String ADVANCE_FURNACE_NAME = "display_name.moderndelight.advance_furnace_name";
    protected final ContainerData propertyDelegate;
    private int progress_1 = 0;
    private int progress_2 = 0;
    private int progress_3 = 0;
    private int progress_4 = 0;
    private int maxProgress = 200;
    private int burnTime = 0;
    private int maxBurnTime = 1;
    private int experience = 0;
    private int cachedBurnTime = 0;
    private int cachedMaxBurnTime = 0;

    public AdvanceFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ADVANCE_FURNACE_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AdvanceFurnaceBlockEntity.this.progress_1;
                    case 1 -> AdvanceFurnaceBlockEntity.this.progress_2;
                    case 2 -> AdvanceFurnaceBlockEntity.this.progress_3;
                    case 3 -> AdvanceFurnaceBlockEntity.this.progress_4;
                    case 4 -> AdvanceFurnaceBlockEntity.this.maxProgress;
                    case 5 -> AdvanceFurnaceBlockEntity.this.burnTime;
                    case 6 -> AdvanceFurnaceBlockEntity.this.maxBurnTime;
                    case 7 -> AdvanceFurnaceBlockEntity.this.experience;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AdvanceFurnaceBlockEntity.this.progress_1 = value;
                    case 1 -> AdvanceFurnaceBlockEntity.this.progress_2 = value;
                    case 2 -> AdvanceFurnaceBlockEntity.this.progress_3 = value;
                    case 3 -> AdvanceFurnaceBlockEntity.this.progress_4 = value;
                    case 4 -> AdvanceFurnaceBlockEntity.this.maxProgress = value;
                    case 5 -> AdvanceFurnaceBlockEntity.this.burnTime = value;
                    case 6 -> AdvanceFurnaceBlockEntity.this.maxBurnTime = value;
                    case 7 -> AdvanceFurnaceBlockEntity.this.experience = value;
                }
            }

            @Override
            public int getCount() {
                return 8;
            }
        };
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        ContainerHelper.loadAllItems(nbt, inventory);
        progress_1 = nbt.getInt("advance_furnace.progress_1");
        progress_2 = nbt.getInt("advance_furnace.progress_2");
        progress_3 = nbt.getInt("advance_furnace.progress_3");
        progress_4 = nbt.getInt("advance_furnace.progress_4");
        burnTime = nbt.getInt("advance_furnace.burnTime");
        maxBurnTime = nbt.getInt("advance_furnace.maxBurnTime");
        experience = nbt.getInt("advance_furnace.experience");
        cachedBurnTime = nbt.getInt("advance_furnace.cachedBurnTime");
        cachedMaxBurnTime = nbt.getInt("advance_furnace.cachedMaxBurnTime");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putInt("advance_furnace.progress_1", progress_1);
        nbt.putInt("advance_furnace.progress_2", progress_2);
        nbt.putInt("advance_furnace.progress_3", progress_3);
        nbt.putInt("advance_furnace.progress_4", progress_4);
        nbt.putInt("advance_furnace.burnTime", burnTime);
        nbt.putInt("advance_furnace.maxBurnTime", maxBurnTime);
        nbt.putInt("advance_furnace.experience", experience);
        nbt.putInt("advance_furnace.cachedBurnTime", cachedBurnTime);
        nbt.putInt("advance_furnace.cachedMaxBurnTime", cachedMaxBurnTime);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(ADVANCE_FURNACE_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new AdvanceFurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    private int tickCounter = 20;
    private boolean alwaysBurning = false;

    public static void tick(Level world, BlockPos pos, BlockState state, AdvanceFurnaceBlockEntity entity) {
        if (world.isClientSide) {
            return;
        }
        entity.tickCounter--;
        if (entity.tickCounter <= 0) {
            entity.tickCounter = 20;
            entity.updateAlwaysBurning(world, pos);
        }

        if (entity.alwaysBurning) {
            if (entity.burnTime != 0 && entity.cachedBurnTime == 0) {
                entity.cachedBurnTime = entity.burnTime;
                entity.cachedMaxBurnTime = entity.maxBurnTime;
            }
            entity.maxBurnTime = 1;
            entity.burnTime = 1;
            if (!state.getValue(AdvanceFurnaceBlock.BURNING)) {
                world.setBlock(pos, state.setValue(AdvanceFurnaceBlock.BURNING, true), 3);
            }
            entity.setChanged();
            entity.checkStack(entity, state, INPUT_SLOT_1, OUTPUT_SLOT_1);
            entity.checkStack(entity, state, INPUT_SLOT_2, OUTPUT_SLOT_2);
            entity.checkStack(entity, state, INPUT_SLOT_3, OUTPUT_SLOT_3);
            entity.checkStack(entity, state, INPUT_SLOT_4, OUTPUT_SLOT_4);
        } else {
            if (entity.cachedBurnTime != 0) {
                entity.burnTime = entity.cachedBurnTime;
                entity.maxBurnTime = entity.cachedMaxBurnTime;
                entity.cachedBurnTime = 0;
                entity.cachedMaxBurnTime = 0;
            }
            if (entity.isFuelBurning()) {
                --entity.burnTime;
                if (!state.getValue(AdvanceFurnaceBlock.BURNING)) {
                    world.setBlock(pos, state.setValue(AdvanceFurnaceBlock.BURNING, true), 3);
                }
                entity.setChanged();
                entity.checkStack(entity, state, INPUT_SLOT_1, OUTPUT_SLOT_1);
                entity.checkStack(entity, state, INPUT_SLOT_2, OUTPUT_SLOT_2);
                entity.checkStack(entity, state, INPUT_SLOT_3, OUTPUT_SLOT_3);
                entity.checkStack(entity, state, INPUT_SLOT_4, OUTPUT_SLOT_4);
            } else {
                entity.decreaseCraftProgress();
                entity.maxBurnTime = 1;
                if (state.getValue(AdvanceFurnaceBlock.BURNING)) {
                    world.setBlock(pos, state.setValue(AdvanceFurnaceBlock.BURNING, false), 3);
                }
            }
            if (canUseAsFuel(entity.getItem(FUEL_SLOT)) && (entity.burnTime == 0) &&
                    (entity.hasRecipe(entity, INPUT_SLOT_1, OUTPUT_SLOT_1) || entity.hasRecipe(entity, INPUT_SLOT_2, OUTPUT_SLOT_2)
                            || entity.hasRecipe(entity, INPUT_SLOT_3, OUTPUT_SLOT_3) || entity.hasRecipe(entity, INPUT_SLOT_4, OUTPUT_SLOT_4))
            ) {
                ItemStack fuel = entity.getItem(FUEL_SLOT);
                entity.burnTime = entity.getFuelTime(fuel);
                entity.maxBurnTime = entity.burnTime;
                if (entity.getItem(FUEL_SLOT).is(Items.LAVA_BUCKET)) {
                    entity.setItem(FUEL_SLOT, new ItemStack(Items.BUCKET));
                } else {
                    entity.removeItem(FUEL_SLOT, 1);
                }
                entity.setChanged();
            }
        }
    }

    private void updateAlwaysBurning(Level world, BlockPos pos) {
        BlockEntity downBe = world.getBlockEntity(pos.below());
        this.alwaysBurning = downBe != null && downBe.getClass().getSimpleName().equals("BurningGasCookingStoveBlockEntity");
    }

    private void checkStack(AdvanceFurnaceBlockEntity entity, BlockState state, int inputSlot, int outputSlot) {
        if (isOutputSlotEmptyOrReceivable(outputSlot)) {
            if (hasRecipe(entity, inputSlot, outputSlot)) {
                this.increaseCraftProgress(inputSlot + 1);
                setChanged();
                if (hasCraftingFinished(inputSlot + 1)) {
                    this.craftItem(entity, inputSlot, outputSlot);
                    this.removeItem(inputSlot, 1);
                    this.resetProgress(inputSlot + 1);
                }
            } else {
                resetProgress(inputSlot + 1);
                setChanged();
            }
        }
    }

    public static boolean canUseAsFuel(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, null) > 0;
    }

    private int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty()) return 0;
        return ForgeHooks.getBurnTime(fuel, null);
    }

    private boolean isFuelBurning() {
        return this.burnTime > 0;
    }

    private void resetProgress(int slot) {
        switch (slot) {
            case 1 -> this.progress_1 = 0;
            case 2 -> this.progress_2 = 0;
            case 3 -> this.progress_3 = 0;
            case 4 -> this.progress_4 = 0;
        }
    }

    private void craftItem(AdvanceFurnaceBlockEntity entity, int inputSlot, int outputSlot) {
        ItemStack input = entity.getItem(inputSlot);
        Optional<SmeltingRecipe> match = Objects.requireNonNull(entity.level).getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new net.minecraft.world.SimpleContainer(input), entity.level);
        
        if (match.isPresent()) {
            SmeltingRecipe recipe = match.get();
            experience += (int) (recipe.getExperience() * 10);
            ItemStack result = recipe.assemble(new net.minecraft.world.SimpleContainer(input), entity.level.registryAccess());
            ItemStack currentOutput = getItem(outputSlot);
            if (currentOutput.isEmpty()) {
                setItem(outputSlot, result.copy());
            } else {
                currentOutput.grow(result.getCount());
            }
        }
    }

    public int getExperience() {
        return experience / 10;
    }

    public void setExperience(int experience) {
        this.experience = Math.max(0, experience * 10);
        setChanged();
    }

    public int takeExperience() {
        int result = getExperience();
        this.experience = 0;
        setChanged();
        return result;
    }

    private boolean hasCraftingFinished(int slot) {
        return switch (slot) {
            case 1 -> progress_1 >= maxProgress;
            case 2 -> progress_2 >= maxProgress;
            case 3 -> progress_3 >= maxProgress;
            case 4 -> progress_4 >= maxProgress;
            default -> false;
        };
    }

    private void increaseCraftProgress(int slot) {
        switch (slot) {
            case 1 -> progress_1++;
            case 2 -> progress_2++;
            case 3 -> progress_3++;
            case 4 -> progress_4++;
        }
    }

    private void decreaseCraftProgress() {
        if (progress_1 > 0) progress_1--;
        if (progress_2 > 0) progress_2--;
        if (progress_3 > 0) progress_3--;
        if (progress_4 > 0) progress_4--;
    }

    private boolean hasRecipe(AdvanceFurnaceBlockEntity entity, int inputSlot, int outputSlot) {
        ItemStack input = entity.getItem(inputSlot);
        if (input.isEmpty()) return false;
        Optional<SmeltingRecipe> match = Objects.requireNonNull(entity.level).getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new net.minecraft.world.SimpleContainer(input), entity.level);
        
        if (match.isPresent()) {
            ItemStack output = match.get().assemble(new net.minecraft.world.SimpleContainer(input), entity.level.registryAccess());
            return canInsertAmountIntoOutputSlot(output, outputSlot) && canInsertItemIntoOutputSlot(output.getItem(), outputSlot);
        }
        return false;
    }

    private boolean canInsertItemIntoOutputSlot(Item item, int slot) {
        return this.getItem(slot).is(item) || this.getItem(slot).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result, int slot) {
        return this.getItem(slot).getCount() + result.getCount() <= getItem(slot).getMaxStackSize();
    }

    private boolean isOutputSlotEmptyOrReceivable(int slot) {
        return this.getItem(slot).isEmpty() || this.getItem(slot).getCount() < getItem(slot).getMaxStackSize();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) return new int[]{INPUT_SLOT_1, INPUT_SLOT_2, INPUT_SLOT_3, INPUT_SLOT_4};
        if (side == Direction.DOWN) return new int[]{OUTPUT_SLOT_1, OUTPUT_SLOT_2, OUTPUT_SLOT_3, OUTPUT_SLOT_4, FUEL_SLOT};
        return new int[]{FUEL_SLOT};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        if (dir == Direction.UP) return slot >= 0 && slot <= 3;
        return slot == FUEL_SLOT && canUseAsFuel(stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        if (dir == Direction.DOWN) {
            if (slot == FUEL_SLOT) return stack.is(Items.BUCKET);
            return slot >= 4 && slot <= 7;
        }
        return false;
    }

    public void useFurnace(Level world, BlockState state) {
        world.playSound(null, getBlockPos(), SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 2.3f, world.random.nextFloat() + 0.5f);
        world.setBlock(getBlockPos(), ModBlocks.OVEN.get().defaultBlockState()
                .setValue(OvenBlock.FACING, state.getValue(AdvanceFurnaceBlock.FACING))
                .setValue(OvenBlock.OVEN_BURNING, false), 3);
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
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
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && side != null && cap == ForgeCapabilities.ITEM_HANDLER) {
            return handlers[side.ordinal()].cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
    }
}


