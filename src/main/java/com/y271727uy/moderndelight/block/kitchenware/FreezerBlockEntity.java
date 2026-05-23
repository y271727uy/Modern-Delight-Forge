package com.y271727uy.moderndelight.block.kitchenware;

import com.google.common.collect.Maps;
import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.recipe.custom.FreezingRecipe;
import com.y271727uy.moderndelight.screen.custom.FreezerScreenHandler;
import com.y271727uy.moderndelight.sound.ModSounds;
import com.y271727uy.moderndelight.tag.TagKeys;
import com.y271727uy.moderndelight.util.block_util.power_util.ACConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
import javax.annotation.Nullable;
import javax.annotation.Nonnull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class FreezerBlockEntity extends BlockEntity implements MenuProvider, WorldlyContainer, ACConsumer {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(20, ItemStack.EMPTY);
    private static final int INPUT_SLOT_1 = 0;
    private static final int INPUT_SLOT_2 = 1;
    private static final int INPUT_SLOT_3 = 2;
    private static final int ICE_SLOT = 3;
    private static final int OUTPUT_SLOT = 4;
    public static final String FREEZER_NAME = "display_name.moderndelight.freezer_name";
    protected final ContainerData propertyDelegate;
    private int progress = 0;
    private int maxProgress = 400;
    private int coolTime = 0;
    private int maxCoolTime = 1;
    private int experiences = 0;

    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());

    public FreezerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FREEZER_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FreezerBlockEntity.this.progress;
                    case 1 -> FreezerBlockEntity.this.maxProgress;
                    case 2 -> FreezerBlockEntity.this.coolTime;
                    case 3 -> FreezerBlockEntity.this.maxCoolTime;
                    case 4 -> FreezerBlockEntity.this.experiences;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0 : FreezerBlockEntity.this.progress = value; break;
                    case 1 : FreezerBlockEntity.this.maxProgress = value; break;
                    case 2 : FreezerBlockEntity.this.coolTime = value; break;
                    case 3 : FreezerBlockEntity.this.maxCoolTime = value; break;
                    case 4 : FreezerBlockEntity.this.experiences = value; break;
                }
            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.inventory.clear();
        net.minecraft.world.ContainerHelper.loadAllItems(nbt, this.inventory);
        progress = nbt.getInt("freezer.progress");
        coolTime = nbt.getInt("freezer.coolTime");
        maxCoolTime = nbt.getInt("freezer.maxCoolTime");
        experiences = nbt.getInt("freezer.experiences");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        net.minecraft.world.ContainerHelper.saveAllItems(nbt, this.inventory);
        nbt.putInt("freezer.progress", progress);
        nbt.putInt("freezer.coolTime", coolTime);
        nbt.putInt("freezer.maxCoolTime", maxCoolTime);
        nbt.putInt("freezer.experiences", experiences);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(FREEZER_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new FreezerScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, FreezerBlockEntity entity){
        if (world.isClientSide){
            return;
        }
        if (entity.isCool()){
            --entity.coolTime;
            if (world.getGameTime() % 100L == 0L){
                entity.playSound(ModSounds.BLOCK_FREEZER_RUNNING.get(), 0.3f, 0.8f);
            }
            if (entity.isOutputSlotEmptyOrReceivable()){
                if (entity.hasRecipe()){
                    entity.increaseCraftProgress();
                    entity.setChanged();
                    if (entity.hasCraftingFinished()){
                        entity.craftItem();
                        for (int i = 0; i < 3; i++){
                            ItemStack remainder = entity.getItem(i).getCraftingRemainingItem();
                            if (!remainder.isEmpty()){
                                Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), remainder.copy());
                            }
                        }
                        entity.removeItem(INPUT_SLOT_1, 1);
                        entity.removeItem(INPUT_SLOT_2, 1);
                        entity.removeItem(INPUT_SLOT_3, 1);
                        entity.resetProgress();
                    }
                } else {
                    if (entity.progress != 0){
                        entity.resetProgress();
                    }
                }
            }
        } else {
            if (entity.hasRecipe()){
                entity.progress--;
            } else {
                entity.resetProgress();
            }
            entity.maxCoolTime = 60;
            entity.setChanged();
        }

        if (entity.canUseAsIce(entity.getItem(ICE_SLOT)) && entity.coolTime == 0){
            ItemStack ice = entity.getItem(ICE_SLOT);
            entity.coolTime = entity.getCoolTime(ice);
            entity.maxCoolTime = entity.coolTime;
            if (entity.getItem(ICE_SLOT).is(Items.POWDER_SNOW_BUCKET)){
                entity.setItem(ICE_SLOT, new ItemStack(Items.BUCKET));
            } else {
                entity.removeItem(ICE_SLOT, 1);
            }
        }
    }

    public void playSound(net.minecraft.sounds.SoundEvent sound, float volume, float pitch) {
        Objects.requireNonNull(level).playSound(null, worldPosition, sound, SoundSource.BLOCKS, volume, pitch);
    }

    public static Map<Item, Integer> createCoolTimeMap() {
        LinkedHashMap<Item, Integer> map = Maps.newLinkedHashMap();
        addIce(map, Items.ICE, 20);
        addIce(map, Items.PACKED_ICE, 190);
        addIce(map, Items.BLUE_ICE, 1830);
        addIce(map, Items.SNOW_BLOCK, 15);
        addIce(map, Items.SNOW, 10);
        addIce(map, Items.POWDER_SNOW_BUCKET, 400);
        addIce(map, TagKeys.COLD_ITEMS, 3);
        return map;
    }

    private static void addIce(Map<Item, Integer> coolTimes, TagKey<Item> tag, int coolTime) {
        for (net.minecraft.core.Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
            coolTimes.put(holder.value(), coolTime);
        }
    }

    private static void addIce(Map<Item, Integer> coolTimes, net.minecraft.world.level.ItemLike item, int coolTime) {
        coolTimes.put(item.asItem(), coolTime);
    }

    public boolean canUseAsIce(ItemStack stack){
        return createCoolTimeMap().containsKey(stack.getItem());
    }

    private int getCoolTime(ItemStack ice){
        if (ice.isEmpty()){
            return 0;
        }
        return createCoolTimeMap().getOrDefault(ice.getItem(), 0);
    }

    private boolean isCool(){
        return this.coolTime > 0;
    }

    private void resetProgress(){
        this.progress = 0;
    }

    private void craftItem() {
        Optional<FreezingRecipe> match = Objects.requireNonNull(level).getRecipeManager()
                .getRecipeFor(FreezingRecipe.Type.INSTANCE, this, level);
        experiences += 3;
        ItemStack output = match.get().getResultItem(level.registryAccess());
        ItemStack currentOutput = getItem(OUTPUT_SLOT);
        if (currentOutput.isEmpty()) {
            setItem(OUTPUT_SLOT, output.copy());
        } else {
            currentOutput.grow(output.getCount());
        }
    }

    private boolean hasCraftingFinished(){
        return progress >= maxProgress;
    }

    private void increaseCraftProgress(){
        progress++;
    }

    private boolean hasRecipe() {
        Optional<FreezingRecipe> match = Objects.requireNonNull(level).getRecipeManager()
                .getRecipeFor(FreezingRecipe.Type.INSTANCE, this, level);

        if (match.isPresent()) {
            ItemStack output = match.get().getResultItem(level.registryAccess());
            return canInsertAmountIntoOutputSlot(output) && canInsertItemIntoOutputSlot(output.getItem());
        }
        return false;
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return getItem(OUTPUT_SLOT).is(item) || getItem(OUTPUT_SLOT).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        return getItem(OUTPUT_SLOT).isEmpty() || getItem(OUTPUT_SLOT).getCount() + result.getCount() <= getItem(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return getItem(OUTPUT_SLOT).isEmpty() || getItem(OUTPUT_SLOT).getCount() < getItem(OUTPUT_SLOT).getMaxStackSize();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        int[] result = new int[inventory.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        if (dir == Direction.UP && slot >= 5){
            return true;
        } else if (dir == Direction.NORTH && slot == 3) {
            return true;
        } else if (dir == Direction.WEST && slot == 0) {
            return true;
        } else if (dir == Direction.SOUTH && slot == 1) {
            return true;
        } else return dir == Direction.EAST && slot == 2;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN && slot >= 4;
    }

    public int getExperience(){
        return experiences;
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
        return net.minecraft.world.ContainerHelper.removeItem(inventory, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return net.minecraft.world.ContainerHelper.takeItem(inventory, slot);
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
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        inventory.clear();
        setChanged();
    }

    @Override
    public <T> @Nonnull LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return handlers[0].cast();
            }
            return handlers[side.get3DDataValue()].cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
    }

    @Override
    public long getConsumedValue() {
        return 15;
    }

    @Override
    public boolean isWorking() {
        return !this.isCool();
    }

    @Override
    public void energize() {
        if (!this.isCool()){
            this.coolTime = 60;
        }
    }
}


