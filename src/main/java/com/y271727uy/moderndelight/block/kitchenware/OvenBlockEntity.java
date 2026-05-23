package com.y271727uy.moderndelight.block.kitchenware;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.gas_cooking_stove.BurningGasCookingStoveBlockEntity;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.recipe.custom.BakingRecipe;
import com.y271727uy.moderndelight.screen.custom.OvenScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

import static com.y271727uy.moderndelight.block.kitchenware.OvenBlock.FACING;
import static com.y271727uy.moderndelight.block.kitchenware.OvenBlock.OVEN_BURNING;

public class OvenBlockEntity extends BlockEntity implements MenuProvider, WorldlyContainer {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(6, ItemStack.EMPTY);
    private static final int INPUT_SLOT_1 = 0;
    private static final int INPUT_SLOT_2 = 1;
    private static final int INPUT_SLOT_3 = 2;
    private static final int INPUT_SLOT_4 = 3;
    private static final int FUEL_SLOT = 4;
    private static final int OUTPUT_SLOT = 5;
    public static final String OVEN_NAME = "display_name.moderndelight.oven_name";
    protected final ContainerData propertyDelegate;
    private int progress = 0;
    private int maxProgress = 200;
    private int burnTime = 0;
    private int maxBurnTime = 1;
    private int experiences = 0;
    private int cachedBurnTime = 0;
    private int cachedMaxBurnTime = 0;
    public OvenBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.OVEN_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> OvenBlockEntity.this.progress;
                    case 1 -> OvenBlockEntity.this.maxProgress;
                    case 2 -> OvenBlockEntity.this.burnTime;
                    case 3 -> OvenBlockEntity.this.maxBurnTime;
                    case 4 -> OvenBlockEntity.this.experiences;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0 -> OvenBlockEntity.this.progress = value;
                    case 1 -> OvenBlockEntity.this.maxProgress = value;
                    case 2 -> OvenBlockEntity.this.burnTime = value;
                    case 3 -> OvenBlockEntity.this.maxBurnTime = value;
                    case 4 -> OvenBlockEntity.this.experiences = value;
                }
            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        net.minecraft.world.ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putInt("oven.progress",progress);
        nbt.putInt("oven.fuelTime", burnTime);
        nbt.putInt("oven.maxFuelTime", maxBurnTime);
        nbt.putInt("oven.experiences", experiences);
        nbt.putInt("oven.cachedBurnTime", cachedBurnTime);
        nbt.putInt("oven.cachedMaxBurnTime", cachedMaxBurnTime);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        net.minecraft.world.ContainerHelper.loadAllItems(nbt, inventory);
        progress = nbt.getInt("oven.progress");
        burnTime = nbt.getInt("oven.fuelTime");
        maxBurnTime = nbt.getInt("oven.maxFuelTime");
        experiences = nbt.getInt("oven.experiences");
        cachedBurnTime = nbt.getInt("oven.cachedBurnTime");
        cachedMaxBurnTime = nbt.getInt("oven.cachedMaxBurnTime");
    }

    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(OVEN_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new OvenScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public void playSound(SoundEvent sound, float volume, float pitch) {
        Objects.requireNonNull(level).playSound(null, worldPosition.getX() + .5f, worldPosition.getY() + .5f, worldPosition.getZ() + .5f, sound, SoundSource.BLOCKS, volume, pitch);
    }

    public int getExperience(){
        return experiences;
    }

    public void tick(Level world, BlockPos pos, BlockState state, OvenBlockEntity entity) {
        if (!world.isClientSide){
            return;
        }
        if (world.getBlockEntity(pos.below()) instanceof BurningGasCookingStoveBlockEntity){
            if (burnTime != 0 && cachedBurnTime == 0){
                cachedBurnTime = burnTime;
                cachedMaxBurnTime = maxBurnTime;
            }
            burnTime = 1;
            maxBurnTime = 1;
            world.setBlock(pos, state.setValue(OVEN_BURNING,true), 3);
            setChanged(world, pos, state);
            checkAndCraft(world, pos, state, entity);
        } else {
            if (cachedBurnTime != 0){
                burnTime = cachedBurnTime;
                maxBurnTime = cachedMaxBurnTime;
                cachedBurnTime = 0;
                cachedMaxBurnTime = 0;
            }
            if (isFuelBurning()){
                --burnTime;
                world.setBlock(pos, state.setValue(OVEN_BURNING,true), 3);
                checkAndCraft(world, pos, state, entity);
            } else {
                if (hasRecipe(entity)){
                    progress--;
                } else {
                    resetProgress();
                }
                maxBurnTime = 1;
                world.setBlock(pos, state.setValue(OVEN_BURNING,false), 3);
                setChanged(world, pos, state);
            }
            if (canUseAsFuel(getItem(FUEL_SLOT))&&(burnTime == 0)&&hasRecipe(entity)){
                ItemStack fuel = this.getItem(FUEL_SLOT);
                burnTime = this.getFuelTime(fuel);
                maxBurnTime = burnTime;
                if (this.getItem(FUEL_SLOT).getItem() == Items.LAVA_BUCKET){
                    this.setItem(FUEL_SLOT, Items.BUCKET.getDefaultInstance());
                } else {
                    this.removeItem(FUEL_SLOT,1);
                }
            }
        }
    }

    private void checkAndCraft(Level world, BlockPos pos, BlockState state, OvenBlockEntity entity) {
        if (isOutputSlotEmptyOrReceivable()){
            if (this.hasRecipe(entity)){
                this.increaseCraftProgress();
                setChanged(world, pos, state);
                if (hasCraftingFinished()){
                    this.craftItem(entity);
                    for (int i = 0; i < 4; i++){
                        if (!getItem(i).getCraftingRemainingItem().isEmpty()){
                            Containers.dropItemStack(world,pos.getX(),pos.getY(),pos.getZ(),this.getItem(i).getCraftingRemainingItem().copy());
                        }
                    }
                    this.removeItem(INPUT_SLOT_1,1);
                    this.removeItem(INPUT_SLOT_2,1);
                    this.removeItem(INPUT_SLOT_3,1);
                    this.removeItem(INPUT_SLOT_4,1);
                    this.resetProgress();
                }
            }
            else {
                resetProgress();
            }
        }
    }
    public static boolean canUseAsFuel(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, null) > 0;
    }
    private int getFuelTime(ItemStack fuel){
        if (fuel.isEmpty()) {
            return 0;
        }
        return ForgeHooks.getBurnTime(fuel, null);
    }
    private boolean isFuelBurning() {
        return this.burnTime > 0;
    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem(OvenBlockEntity entity) {
        if (this.getItem(INPUT_SLOT_1).getItem().equals(ModItems.BLACK_PEPPER_DUST.get()) &&
                this.getItem(INPUT_SLOT_2).getItem().equals(Items.SUGAR) &&
                this.getItem(INPUT_SLOT_3).getItem().equals(ModItems.BLACK_PEPPER_DUST.get()) &&
                this.getItem(INPUT_SLOT_4).is(ModBlocks.RAW_PIZZA_ITEM.get())){
            ItemStack rawPizzaStack = this.getItem(INPUT_SLOT_4);
            CompoundTag rawPizzaNBT = BlockItem.getBlockEntityData(rawPizzaStack);
            NonNullList<ItemStack> defaultedList = NonNullList.withSize(5, ItemStack.EMPTY);
            if (rawPizzaNBT != null) {
                if (rawPizzaNBT.contains("Items", 9)) {
                    ContainerHelper.loadAllItems(rawPizzaNBT, defaultedList);
                }
            }
            ItemStack pizzaStack = new ItemStack(ModBlocks.PIZZA_ITEM.get());
            CompoundTag nbt = getNbtCompound(defaultedList);
            BlockItem.setBlockEntityData(pizzaStack, ModBlockEntities.PIZZA_BLOCK_ENTITY.get(), nbt);
            this.setItem(OUTPUT_SLOT,pizzaStack);
        } else {
            SimpleContainer inventory = new SimpleContainer(entity.getContainerSize());
            for(int i = 0; i< entity.getContainerSize();i++){
                inventory.setItem(i,entity.getItem(i));
            }
            Optional<BakingRecipe> match = Objects.requireNonNull(entity.getLevel()).getRecipeManager()
                    .getRecipeFor(BakingRecipe.Type.INSTANCE, (Container) inventory, entity.getLevel());
            this.setItem(OUTPUT_SLOT, new ItemStack(match.get().getResultItem(entity.getLevel().registryAccess()).getItem(),
                    getItem(OUTPUT_SLOT).getCount() + match.get().getResultItem(entity.getLevel().registryAccess()).getCount()));
        }
        experiences += 4;
    }
    private static CompoundTag getNbtCompound(NonNullList<ItemStack> defaultedList) {
        CompoundTag nbt = new CompoundTag();
        ContainerHelper.saveAllItems(nbt, defaultedList);
        return nbt;
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftProgress() {
        progress++;
    }

    private boolean hasRecipe(OvenBlockEntity entity) {
        if (this.getItem(INPUT_SLOT_1).getItem().equals(ModItems.BLACK_PEPPER_DUST.get()) &&
        this.getItem(INPUT_SLOT_2).getItem().equals(Items.SUGAR) &&
        this.getItem(INPUT_SLOT_3).getItem().equals(ModItems.BLACK_PEPPER_DUST.get()) &&
        this.getItem(INPUT_SLOT_4).is(ModBlocks.RAW_PIZZA_ITEM.get()) &&
        this.getItem(OUTPUT_SLOT).isEmpty()){
            return true;
        }
        SimpleContainer inventory = new SimpleContainer(entity.getContainerSize());
        for(int i = 0; i < entity.getContainerSize();i++){
            inventory.setItem(i,entity.getItem(i));
        }
        Optional<BakingRecipe> match = Objects.requireNonNull(entity.getLevel()).getRecipeManager()
                .getRecipeFor(BakingRecipe.Type.INSTANCE, (Container) inventory, entity.getLevel());

        if (entity.level != null) {
            return match.isPresent() &&
                    canInsertAmountIntoOutputSlot(match.get().getResultItem(entity.level.registryAccess())) &&
                    canInsertItemIntoOutputSlot(match.get().getResultItem(entity.level.registryAccess()).getItem());
        }
        return false;
    }

    private boolean canInsertItemIntoOutputSlot(net.minecraft.world.item.Item item) {
        return this.getItem(OUTPUT_SLOT).is(item) || this.getItem(OUTPUT_SLOT).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        return this.getItem(OUTPUT_SLOT).getCount() + result.getCount() <= getItem(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.getItem(OUTPUT_SLOT).isEmpty() || this.getItem(OUTPUT_SLOT).getCount() < this.getItem(OUTPUT_SLOT).getMaxStackSize();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        int[] result = new int[getContainerSize()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        if (dir == Direction.UP && slot == 4){
            return true;
        } else if (dir == Direction.EAST && slot == 0) {
            return true;
        } else if (dir == Direction.SOUTH && slot == 1) {
            return true;
        } else if (dir == Direction.WEST && slot == 2) {
            return true;
        } else return dir == Direction.NORTH && slot == 3;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        if (dir == Direction.DOWN && slot == 4){
            return stack.is(Items.BUCKET);
        } else return dir == Direction.DOWN && slot == 5;
    }

    public void use(BlockState state, Level world) {
        playSound(SoundEvents.STONE_BREAK,2.3f,world.random.nextFloat()/2 +0.5f);
        Containers.dropItemStack(world, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), ModBlocks.BAKING_TRAY.get().asItem().getDefaultInstance());
        world.setBlock(worldPosition, ModBlocks.ADVANCE_FURNACE.get().defaultBlockState().setValue(AdvanceFurnaceBlock.FACING,state.getValue(FACING)).setValue(AdvanceFurnaceBlock.BURNING,false), 3);
    }

    // WorldlyContainer implementation (Standard Forge calls)
    @Override public int getContainerSize() { return inventory.size(); }
    @Override public boolean isEmpty() { return inventory.stream().allMatch(ItemStack::isEmpty); }
    @Override public ItemStack getItem(int i) { return inventory.get(i); }
    @Override public ItemStack removeItem(int i, int i1) { ItemStack stack = net.minecraft.world.ContainerHelper.removeItem(inventory, i, i1); if (!stack.isEmpty()) setChanged(); return stack; }
    @Override public ItemStack removeItemNoUpdate(int i) { return net.minecraft.world.ContainerHelper.takeItem(inventory, i); }
    @Override public void setItem(int i, ItemStack itemStack) { inventory.set(i, itemStack); if (itemStack.getCount() > getMaxStackSize()) itemStack.setCount(getMaxStackSize()); setChanged(); }
    @Override public boolean stillValid(Player player) { return true; }
    @Override public void clearContent() { inventory.clear(); setChanged(); }

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER) {
            return handlers[facing.ordinal()].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
    }
}


