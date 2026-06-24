package com.y271727uy.moderndelight.block.kitchenware.decor;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.networking.packet.IntegerSyncS2CPacket;
import com.y271727uy.moderndelight.networking.packet.ItemStackSyncS2CPacket;
import com.y271727uy.moderndelight.util.block_util.ImplementedInventory;
import com.y271727uy.moderndelight.util.enums.ShowAbleItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;

public class WoodenPlateBlockEntity extends BlockEntity implements ImplementedInventory {
    public WoodenPlateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WOODEN_PLATE_BLOCK_ENTITY.get(), pos, state);
    }
    
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    int rotate = 0;
    
    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        for (int i = 0; i < inventory.size(); i++) {
            inventory.set(i, ItemStack.of(nbt.getCompound("Item" + i)));
        }
        rotate = nbt.getInt("wooden_plate_rotate");
        updateShowingState();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        for (int i = 0; i < inventory.size(); i++) {
            nbt.put("Item" + i, inventory.get(i).save(new CompoundTag()));
        }
        nbt.putInt("wooden_plate_rotate", rotate);
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    @Override
    public void setChanged() {
        if (level != null) {
            if (!level.isClientSide){
                if (!getItem(0).isEmpty()){
                    if (getItem(0).getCount() > 1){
                        ItemStack copy = getItem(0).copy();
                        copy.setCount(getItem(0).getCount()-1);
                        setItem(0, copy.copyWithCount(1));
                        Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), copy);
                    }
                }
            }
            level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
            if (!level.isClientSide) {
                updateShowingState();
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
            ItemStackSyncS2CPacket.send(worldPosition, getItems(), level);
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

    private void updateShowingState() {
        if (level != null && !level.isClientSide && getBlockState().hasProperty(WoodenPlateBlock.SHOWING_ITEM)) {
            ShowAbleItems value = getItem(0).isEmpty() ? ShowAbleItems.EMPTY : ShowAbleItems.getValue(getItem(0).getItem());
            if (getBlockState().getValue(WoodenPlateBlock.SHOWING_ITEM) != value) {
                level.setBlock(worldPosition, getBlockState().setValue(WoodenPlateBlock.SHOWING_ITEM, value), 3);
            }
        }
    }
    
    public ItemStack getRenderStack(){
        if (getBlockState().getValue(WoodenPlateBlock.SHOWING_ITEM) == ShowAbleItems.EMPTY){
            return getItem(0);
        }
        return ItemStack.EMPTY;
    }
    
    public void use(Level world, BlockState state, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.isEmpty()){
            if (!getItem(0).isEmpty()){
                if (player.isShiftKeyDown()){
                    rotate += 45;
                    if (rotate >= 360){
                        rotate = 0;
                    }
                    world.playSound(null, worldPosition, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0f, 0.8f + world.random.nextFloat()/2);
                    IntegerSyncS2CPacket.send(worldPosition, rotate, world);
                } else {
                    world.setBlock(worldPosition, state.setValue(WoodenPlateBlock.SHOWING_ITEM, ShowAbleItems.EMPTY), 3);
                    Containers.dropItemStack(world, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), getItem(0));
                    setItem(0, ItemStack.EMPTY);
                    world.playSound(null, worldPosition, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0f, 0.8f);
                }
            }
        } else {
            if (getItem(0).isEmpty()){
                world.setBlock(worldPosition, state.setValue(WoodenPlateBlock.SHOWING_ITEM, ShowAbleItems.getValue(itemStack.getItem())), 3);
                setItem(0, itemStack.split(1));
                world.playSound(null, worldPosition, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0f, 0.8f);
            }
        }
        setChanged();
    }
}
