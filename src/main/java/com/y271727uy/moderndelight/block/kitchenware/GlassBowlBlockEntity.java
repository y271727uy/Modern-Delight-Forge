package com.y271727uy.moderndelight.block.kitchenware;

import com.google.common.collect.Lists;
import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.networking.packet.ItemStackSyncS2CPacket;
import com.y271727uy.moderndelight.util.block_util.ImplementedInventory;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.item.food.PackagedItem;
import com.y271727uy.moderndelight.item.tools.ElectricWhiskItem;
import com.y271727uy.moderndelight.recipe.custom.MixWithWaterRecipe;
import com.y271727uy.moderndelight.recipe.custom.WhiskingRecipe;
import com.y271727uy.moderndelight.sound.ModSounds;
import com.y271727uy.moderndelight.tag.TagKeys;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.ContainerHelper;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static com.y271727uy.moderndelight.block.kitchenware.GlassBowlBlock.*;

public class GlassBowlBlockEntity extends BlockEntity implements ImplementedInventory {
    public static final String WHISK_FAIL = "moderndelight.glass_bowl_message.whisk_fail";
    public static final String NEED_PACKAGE = "moderndelight.glass_bowl_message.need_package";
    public GlassBowlBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GLASS_BOWL_ENTITY.get(), pos, state);
    }
    public final NonNullList<ItemStack> GLASS_BOWL_INV = NonNullList.withSize(1, ItemStack.EMPTY);
    public ItemStack outputStack = ItemStack.EMPTY;

    public ItemStack getOutputStack() {
        return outputStack;
    }

    public void setOutputStack(ItemStack outputStack) {
        this.outputStack = outputStack;
    }

    public void use(@Nonnull Player player, BlockState state, Level world){
        if (world.isClientSide){
            return;
        }
        Item offHandItem = player.getOffhandItem().getItem();
        Item mainHandItem = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
        // Check Water
        if (world.getBlockState(worldPosition).getValue(HAS_WATER)) {
            SimpleContainer inventory = new SimpleContainer(this.getContainerSize());
            boolean isMainHand;
            if (offHandItem == Items.AIR){
                inventory.setItem(0, mainHandItem.getDefaultInstance());
                isMainHand = true;
            } else {
                inventory.setItem(0, offHandItem.getDefaultInstance());
                isMainHand = false;
            }
            // Mix
            Optional<MixWithWaterRecipe> match = Objects.requireNonNull(this.getLevel()).getRecipeManager()
                    .getRecipeFor(MixWithWaterRecipe.Type.INSTANCE, inventory,this.getLevel());
            if (match.isPresent()){
                Block.popResource(this.level, this.worldPosition,
                        new ItemStack(match.get().getResultItem(this.getLevel().registryAccess()).getItem(),1));
                if (isMainHand){
                    player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                } else {
                    player.getOffhandItem().shrink(1);
                }
                world.setBlock(worldPosition,state.setValue(HAS_WATER, false), 3);
                playSound(SoundEvents.BUCKET_FILL,1.0f);
            } else if (mainHandItem == Items.GLASS_BOTTLE){
                player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                player.getInventory().add(Items.POTION.getDefaultInstance());
                world.setBlock(worldPosition,state.setValue(HAS_WATER, false), 3);
                playSound(SoundEvents.BUCKET_FILL,1.0f);
            } else
            if (mainHandItem == Items.BUCKET) {
                player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                player.getInventory().add(Items.WATER_BUCKET.getDefaultInstance());
                world.setBlock(worldPosition,state.setValue(HAS_WATER, false), 3);
                playSound(SoundEvents.BUCKET_FILL,1.0f);
            }
        } else {
            // Take the Output
            if (!outputStack.isEmpty()){
                if (outputStack.getItem() instanceof PackagedItem packagedItem){
                    if (mainHandItem == packagedItem.getPackageItem()){
                        player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                        getResultItem(world,state,player,false);
                    } else {
                        MutableComponent text = Component.translatable(NEED_PACKAGE);
                        text.append(Component.literal(" "));
                        text.append(Component.translatable(packagedItem.getPackageItem().getDescriptionId()));
                        player.sendSystemMessage(text);
                    }
                } else {
                    getResultItem(world,state,player,true);
                }
            } else {
                // Storage Water
                if (getItem(0).isEmpty() &&
                        !world.getBlockState(worldPosition).getValue(HAS_ITEM) &&
                        mainHandItem == Items.POTION){
                    player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                    player.setItemInHand(player.getUsedItemHand(),Items.GLASS_BOTTLE.getDefaultInstance());
                    world.setBlock(worldPosition,state.setValue(HAS_WATER, true), 3);
                    playSound(SoundEvents.BUCKET_EMPTY,1.0f);
                } else if (getItem(0).isEmpty() &&
                        !world.getBlockState(worldPosition).getValue(HAS_ITEM) &&
                        mainHandItem == Items.WATER_BUCKET) {
                    player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                    player.setItemInHand(player.getUsedItemHand(),Items.BUCKET.getDefaultInstance());
                    world.setBlock(worldPosition,state.setValue(HAS_WATER, true), 3);
                    playSound(SoundEvents.BUCKET_EMPTY,1.0f);
                } else {
                    // Storage Items
                    if(getItem(0).isEmpty()){
                        if (offHandItem == Items.AIR){
                            setItem(0, player.getItemInHand(InteractionHand.MAIN_HAND).split(1));
                            if (mainHandItem != Items.AIR){
                                playSound(SoundEvents.ITEM_PICKUP, 0.4F);
                            }
                        } else {
                            setItem(0, player.getOffhandItem().split(1));
                            playSound(SoundEvents.ITEM_PICKUP, 0.4F);
                        }
                    } else {
                        if (isWhisk(player.getItemInHand(InteractionHand.MAIN_HAND), world,player)) {
                            if (hasRecipe()){
                                // Spawn Empty Bowl
                                if (getItem(0).getItem() instanceof PackagedItem packagedItem){
                                    Block.popResource(this.level, this.worldPosition,
                                            packagedItem.getPackageItem().getDefaultInstance());
                                }
                                craft(world);
                                player.getItemInHand(InteractionHand.MAIN_HAND).hurtAndBreak(1, (LivingEntity) player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                                setItem(0, ItemStack.EMPTY);
                                playSound(ModSounds.BLOCK_GLASS_BOWL_WHISKING.get(), 1.5F);
                                world.setBlock(worldPosition,state.setValue(HAS_ITEM,true), 3);
                            }
                        }
                        spawnItem(world);
                    }
                }
            }
        }
        setChanged();
    }
    private void getResultItem(Level world, BlockState state, Player player, boolean spawn){
        if (spawn){
            Block.popResource(world, this.worldPosition, outputStack);
        } else {
            player.getInventory().add(outputStack);
        }
        outputStack = ItemStack.EMPTY;
        playSound(SoundEvents.ITEM_PICKUP, 0.8F);
        setChanged();
        world.setBlock(worldPosition,state.setValue(GlassBowlBlock.HAS_ITEM,false), 3);
    }
    private void spawnItem(Level world){
        Block.popResource(world, this.worldPosition,
                getItems().get(0));
        GLASS_BOWL_INV.clear();
        playSound(SoundEvents.ITEM_PICKUP, 0.8F);
        setChanged();
    }
    public void playSound(SoundEvent sound, float volume) {
        Objects.requireNonNull(level).playSound(null, worldPosition.getX() + .5f, worldPosition.getY() + .5f, worldPosition.getZ() + .5f, sound, SoundSource.BLOCKS, volume, level.random.nextFloat()+0.1f);
    }
    private boolean isWhisk(@Nonnull ItemStack stack, Level world, Player player) {
        if (stack.is(ModItems.ELECTRIC_WHISK.get())){
            if (ElectricWhiskItem.getStoredEnergy(stack) >= 20){
                ElectricWhiskItem.reduceStoredEnergy(stack, 20);
                ElectricWhiskItem.playAnimation(stack);
                world.playSound(null,worldPosition.getX(),worldPosition.getY(),worldPosition.getZ(),
                        ModSounds.ITEM_ELECTRIC_WHISK_WORKING.get(), SoundSource.PLAYERS,1.0f,1.0f);
                return true;
            } else {
                player.sendSystemMessage(Component.translatable(TextUtil.ELECTRIC_WHISK_MSG));
                return false;
            }
        }
        ArrayList<Item> list = Lists.newArrayList();
        for (net.minecraft.core.Holder<Item> registryEntry : BuiltInRegistries.ITEM.getTagOrEmpty(TagKeys.WHISKS)) {
            list.add(registryEntry.value());
        }
        return list.contains(stack.getItem());
    }
    private void craft(Level world){
        SimpleContainer inventory = new SimpleContainer(this.getContainerSize());
        inventory.setItem(0,this.getItems().get(0));
        Optional<WhiskingRecipe> match = Objects.requireNonNull(this.level).getRecipeManager()
                .getRecipeFor(WhiskingRecipe.Type.INSTANCE, inventory,this.level);
        outputStack = match.get().getResultItem(this.level.registryAccess()).copy();
        if (getItems().get(0).getCount() > 1){
            ItemStack tmp = getItems().get(0).copy();
            tmp.setCount(getItems().get(0).getCount() - 1);
            Block.popResource(world, worldPosition, tmp);
        }
        setChanged();
    }
    private boolean hasRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.getContainerSize());
        inventory.setItem(0,this.getItems().get(0));
        Optional<WhiskingRecipe> match = Objects.requireNonNull(this.level).getRecipeManager()
                .getRecipeFor(WhiskingRecipe.Type.INSTANCE, inventory,this.level);
        return match.isPresent();
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, GLASS_BOWL_INV);
        nbt.putString("glass_bowl_output_item",BuiltInRegistries.ITEM.getKey(outputStack.getItem()).toString());
        nbt.putInt("glass_bowl_output_count",outputStack.getCount());
    }
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        ContainerHelper.loadAllItems(nbt, GLASS_BOWL_INV);
        String s = nbt.getString("glass_bowl_output_item");
        int c = nbt.getInt("glass_bowl_output_count");
        try {
            Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(s));
            outputStack = new ItemStack(item,c);
        } catch (Exception ignored){}
    }
    public void playSound(SoundEvent sound, float volume, float pitch) {
        Objects.requireNonNull(level).playSound(null, worldPosition.getX() + .5f, worldPosition.getY() + .5f, worldPosition.getZ() + .5f, sound, SoundSource.BLOCKS, volume, pitch);
    }
    @Override
    public NonNullList<ItemStack> getItems() {
        return GLASS_BOWL_INV;
    }
    public ItemStack getRendererStack(){
        return this.getItems().get(0);
    }
    @Override
    public void setChanged() {
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            ItemStackSyncS2CPacket.send(worldPosition,getItems(),level);
        }
        super.setChanged();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

}
