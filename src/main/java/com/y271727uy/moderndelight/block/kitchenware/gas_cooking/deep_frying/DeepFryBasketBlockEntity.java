package com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.networking.packet.ItemStackSyncS2CPacket;
import com.y271727uy.moderndelight.util.TextUtil;
import com.y271727uy.moderndelight.util.block_util.ImplementedInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DeepFryBasketBlockEntity extends BlockEntity implements ImplementedInventory {
    public DeepFryBasketBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DEEP_FRY_BASKET_BLOCK_ENTITY.get(), pos, state);
    }
    public final NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    
    public void use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.isEmpty()){
            for(int i = 0; i < getItems().size(); i ++){
                if (!getItem(i).isEmpty()){
                    net.minecraft.world.entity.item.ItemEntity itemEntity = new net.minecraft.world.entity.item.ItemEntity(world, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, getItem(i).copy());
                    world.addFreshEntity(itemEntity);
                    setItem(i,ItemStack.EMPTY);
                    world.playSound(null,worldPosition, SoundEvents.ITEM_PICKUP, net.minecraft.sounds.SoundSource.BLOCKS,1.0f,world.random.nextFloat()+0.8f);
                    setChanged();
                    break;
                }
            }
        } else {
            CompoundTag nbtCompound = BlockItem.getBlockEntityData(itemStack);
            if (nbtCompound != null) {
                if (nbtCompound.contains("Items", 9)) {
                    player.sendSystemMessage(Component.translatable(TextUtil.PUN));
                    return;
                }
            }
            for(int i = 0; i < getItems().size(); i ++){
                if (getItem(i).isEmpty()){
                    setItem(i,itemStack.copy());
                    player.setItemInHand(hand,ItemStack.EMPTY);
                    world.playSound(null,worldPosition, SoundEvents.ITEM_PICKUP, net.minecraft.sounds.SoundSource.BLOCKS,1.0f,world.random.nextFloat()+1.2f);
                    setChanged();
                    break;
                }
            }
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        if (nbt.contains("Items", 9)) {
            ContainerHelper.loadAllItems(nbt, inventory);
        } else {
            for (int i = 0; i < inventory.size(); i++) {
                inventory.set(i, ItemStack.of(nbt.getCompound("Item" + i)));
            }
        }
        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        ContainerHelper.saveAllItems(nbt, inventory);
        super.saveAdditional(nbt);
    }

    @Override
    public void setChanged() {
        if (level != null) {
            ItemStackSyncS2CPacket.send(worldPosition,getItems(),level);
        }
        super.setChanged();
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }
}
