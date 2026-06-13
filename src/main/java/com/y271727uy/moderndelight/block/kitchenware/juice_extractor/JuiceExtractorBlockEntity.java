package com.y271727uy.moderndelight.block.kitchenware.juice_extractor;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.recipe.custom.JuiceExtractingRecipe;
import com.y271727uy.moderndelight.sound.ModSounds;
import com.y271727uy.moderndelight.util.block_util.power_util.ACConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraft.world.Containers;
import net.minecraft.core.registries.BuiltInRegistries;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.Optional;

public class JuiceExtractorBlockEntity extends BlockEntity implements GeoBlockEntity, WorldlyContainer, ACConsumer {
    public JuiceExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.JUICE_EXTRACTOR_BLOCK_ENTITY.get(), pos, state);
    }
    public static final String NO_POWER = "moderndelight.juice_extractor_message.no_power";
    public static final String WRONG_RECIPE = "moderndelight.juice_extractor_message.wrong_recipe";
    public static final String IS_FULL_MSG = "moderndelight.juice_extractor_message.is_full";

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WORK = RawAnimation.begin().thenLoop("work");
    private static final RawAnimation FULL = RawAnimation.begin().thenPlay("full");

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private int cachedPower = 0;
    private int progress = 0;
    private boolean scream = false;
    private boolean hasRecipe = false;
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(5, ItemStack.EMPTY);
    public ItemStack tempOutput = ItemStack.EMPTY;
    public Item tempContainer = Items.AIR;
    public static final int SLOT_1 = 0;
    public static final int SLOT_2 = 1;
    public static final int SLOT_3 = 2;
    public static final int SLOT_4 = 3;
    public static final int OUTPUT = 4;

    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());

    @Override
    public long getConsumedValue() {
        return 10;
    }

    @Override
    public boolean isWorking() {
        return true;
    }

    @Override
    public void energize() {
        cachedPower = 60;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> {
            if (state.getAnimatable().getBlockState().getValue(JuiceExtractorBlock.IS_WORKING)) {
                return state.setAndContinue(WORK);
            } else if (state.getAnimatable().getBlockState().getValue(JuiceExtractorBlock.IS_FULL)){
                return state.setAndContinue(FULL);
            } else {
                return state.setAndContinue(IDLE);
            }
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag nbt) {
        super.saveAdditional(nbt);
        net.minecraft.world.ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putInt("juice_extractor.progress", progress);
        nbt.put("juice_extractor.tempOutput", tempOutput.save(new CompoundTag()));
        nbt.putString("juice_extractor.tempContainer", BuiltInRegistries.ITEM.getKey(tempContainer).toString());
        nbt.putBoolean("juice_extractor.hasRecipe", hasRecipe);
    }

    @Override
    public void load(@Nonnull CompoundTag nbt) {
        super.load(nbt);
        this.inventory.clear();
        net.minecraft.world.ContainerHelper.loadAllItems(nbt, inventory);
        progress = nbt.getInt("juice_extractor.progress");
        if (nbt.contains("juice_extractor.tempOutput")) {
            tempOutput = ItemStack.of(nbt.getCompound("juice_extractor.tempOutput"));
        }
        if (nbt.contains("juice_extractor.tempContainer")) {
            tempContainer = BuiltInRegistries.ITEM.get(new net.minecraft.resources.ResourceLocation(nbt.getString("juice_extractor.tempContainer")));
        }
        hasRecipe = nbt.getBoolean("juice_extractor.hasRecipe");
    }

    public void use(Level world, BlockPos bPos, Player player) {
        if (world.isClientSide || world.getBlockState(bPos).getValue(JuiceExtractorBlock.IS_WORKING)){
            return;
        }
        InteractionHand hand = player.getUsedItemHand();
        if (player.isShiftKeyDown()) {
            for (int slot = SLOT_4; slot >= SLOT_1; slot--) {
                if (!getItem(slot).isEmpty()) {
                    ItemStack tmp = getItem(slot).copy();
                    tmp.setCount(1);
                    Containers.dropItemStack(world, bPos.getX(), bPos.getY(), bPos.getZ(), tmp);
                    world.playSound(null, bPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0f, 0.6f);
                    removeItem(slot, 1);
                    setChanged();
                    return;
                }
            }
        } else {
            if (world.getBlockState(bPos).getValue(JuiceExtractorBlock.IS_FULL)){
                if (player.getItemInHand(hand).is(tempContainer)){
                    ItemStack out = getItem(OUTPUT).copyWithCount(1);
                    if (player.getItemInHand(hand).getCount() == 1){
                        player.setItemInHand(hand, out);
                    } else {
                        player.getItemInHand(hand).shrink(1);
                        if (!player.getInventory().add(out)) {
                            player.drop(out, false);
                        }
                    }
                    world.playSound(null, bPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                    removeItem(OUTPUT, 1);
                    if(getItem(OUTPUT).isEmpty()){
                        setState(world, bPos, JuiceExtractorBlock.IS_FULL, false);
                        tempOutput = ItemStack.EMPTY;
                        tempContainer = Items.AIR;
                    }
                    setChanged();
                } else {
                    MutableComponent text = Component.translatable(IS_FULL_MSG);
                    text.append(" ").append(tempContainer.getDescription());
                    player.displayClientMessage(text, true);
                }
                return;
            }
            
            if (player.getItemInHand(hand).isEmpty()) {
                boolean allFilled = true;
                for(int i = 0; i < 4; i++){
                    if (getItem(i).isEmpty()){
                        allFilled = false;
                        break;
                    }
                }
                if (allFilled) {
                    tryStart(player, world);
                }
                return;
            }

            for (int slot = SLOT_1; slot <= SLOT_4; slot++) {
                if (getItem(slot).isEmpty()) {
                    setItem(slot, player.getItemInHand(hand).split(1));
                    world.playSound(null, bPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0f, 1.0f);
                    setChanged();
                    return;
                }
            }
            tryStart(player, world);
        }
    }

    private void tryStart(Player player, Level world) {
        if (cachedPower > 0){
            Optional<JuiceExtractingRecipe> match = world.getRecipeManager()
                    .getRecipeFor(JuiceExtractingRecipe.Type.INSTANCE, this, world);
            if (match.isPresent()){
                this.tempOutput = match.get().getResultItem(world.registryAccess()).copy();
                this.tempContainer = match.get().getContainer();
                this.progress = match.get().getProgress();
                clearInternal(world);
                if (hasHardThings()){
                    scream = true;
                }
                hasRecipe = true;
                setWorking(world);
            } else if (hasHardThings()){
                scream = true;
                this.tempOutput = ItemStack.EMPTY;
                this.tempContainer = Items.AIR;
                this.progress = 200;
                clearInternal(world);
                hasRecipe = false;
                setWorking(world);
            } else {
                player.displayClientMessage(Component.translatable(WRONG_RECIPE), true);
            }
            setChanged();
        } else {
            player.displayClientMessage(Component.translatable(NO_POWER), true);
        }
    }

    private void setWorking(Level world) {
        if (world.getBlockState(this.worldPosition).getBlock() instanceof JuiceExtractorBlock) {
            world.setBlock(this.worldPosition, world.getBlockState(this.worldPosition).setValue(JuiceExtractorBlock.IS_WORKING, true), 3);
        }
    }

    private void clearInternal(Level world) {
        for(int i = 0; i < 4; i++){
            ItemStack remainder = getItem(i).getCraftingRemainingItem();
            if (!remainder.isEmpty()){
                Containers.dropItemStack(world, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), remainder.copy());
            }
            setItem(i, ItemStack.EMPTY);
        }
        setChanged();
    }

    private boolean hasHardThings() {
        for(int i = 0; i < 4; i++){
            ItemStack stack = getItem(i);
            if (stack.getItem() instanceof BlockItem blockItem){
                if (blockItem.getBlock().builtInRegistryHolder().is(BlockTags.MINEABLE_WITH_PICKAXE)){
                    return true;
                }
            }
        }
        return false;
    }

    public static void tick(Level world, BlockPos bPos, BlockState state, JuiceExtractorBlockEntity entity) {
        if (world.isClientSide){
            return;
        }
        if (entity.cachedPower != 0){
            entity.cachedPower--;
        }
        if (state.getValue(JuiceExtractorBlock.IS_WORKING)){
            if (entity.progress > 0){
                if (world.getGameTime() % 5 == 0){
                    if (entity.scream){
                        world.playSound(null, bPos, ModSounds.BLOCK_JUICE_EXTRACTOR_SCREAM.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                    } else {
                        world.playSound(null, bPos, ModSounds.BLOCK_JUICE_EXTRACTOR_WORKING.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                }
                entity.progress--;
            } else {
                world.playSound(null, bPos, ModSounds.BLOCK_JUICE_EXTRACTOR_STOP.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                if (entity.hasRecipe){
                    entity.setItem(OUTPUT, entity.tempOutput.copy());
                    setState(world, bPos, JuiceExtractorBlock.IS_FULL, true);
                } else {
                    world.explode(null, bPos.getX(), bPos.getY(), bPos.getZ(), 1.5f, false, Level.ExplosionInteraction.BLOCK);
                }
                entity.hasRecipe = false;
                entity.scream = false;
                setState(world, bPos, JuiceExtractorBlock.IS_WORKING, false);
                entity.setChanged();
            }
        }
    }

    private static void setState(Level world, BlockPos bPos, BooleanProperty booleanProperty, boolean value) {
        BlockState state = world.getBlockState(bPos);
        if (state.getBlock() instanceof JuiceExtractorBlock) {
            world.setBlock(bPos, state.setValue(booleanProperty, value), 3);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1, 2, 3};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, @Nonnull ItemStack stack, @Nullable Direction dir) {
        return slot < 4;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, @Nonnull ItemStack stack, @Nonnull Direction dir) {
        return slot < 4;
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : inventory) if(!stack.isEmpty()) return false;
        return true;
    }

    @Override
    public @Nonnull ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    @Nonnull
    public ItemStack removeItem(int slot, int amount) {
        return net.minecraft.world.ContainerHelper.removeItem(inventory, slot, amount);
    }

    @Override
    @Nonnull
    public ItemStack removeItemNoUpdate(int slot) {
        return net.minecraft.world.ContainerHelper.takeItem(inventory, slot);
    }

    @Override
    public void setItem(int slot, @Nonnull ItemStack stack) {
        inventory.set(slot, stack);
        setChanged();
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return net.minecraft.world.Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        inventory.clear();
        setChanged();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!remove && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) return handlers[0].cast();
            return handlers[side.get3DDataValue()].cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<? extends IItemHandler> handler : handlers) handler.invalidate();
    }
}


