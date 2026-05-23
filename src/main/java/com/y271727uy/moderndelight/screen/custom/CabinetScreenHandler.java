package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.kitchenware.decor.CabinetBlockEntity;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CabinetScreenHandler extends AbstractContainerMenu {
    private final CabinetBlockEntity blockEntity;
    
    public CabinetScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf){
        this(syncId, inventory, (CabinetBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public CabinetScreenHandler(int syncId, Inventory playerInventory, CabinetBlockEntity blockEntity){
        super(ModScreenHandlers.CABINET_SCREEN_HANDLER.get(), syncId);
        this.blockEntity = blockEntity;
        
        // Add cabinet slots (4x9 grid)
        for (int i = 0; i < 4; ++i){
            for (int l = 0; l < 9; ++l){
                this.addSlot(new Slot(blockEntity, l + i * 9, 8 + l * 18, 16 + i * 18));
            }
        }
        
        // Add player inventory
        addPlayerInventory(playerInventory);
        // Add player hotbar
        addPlayerHotbar(playerInventory);
    }
    
    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < 36) {
                // Move from cabinet to player inventory
                if (!this.moveItemStackTo(originalStack, 36, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Move from player inventory to cabinet
                if (!this.moveItemStackTo(originalStack, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        BlockPos pos = blockEntity.getBlockPos();
        return !blockEntity.isRemoved() && player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }
    
    private void addPlayerInventory(Inventory playerInventory){
        for (int i = 0; i < 3; ++i){
            for (int l = 0; l < 9; ++l){
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 102 + i * 18));
            }
        }
    }
    
    private void addPlayerHotbar(Inventory playerInventory){
        for (int i = 0; i < 9; ++i){
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 160));
        }
    }

    @Override
    public void removed(Player player) {
        if (!player.level().isClientSide){
            player.level().playSound(null, player.blockPosition(), net.minecraft.sounds.SoundEvents.WOODEN_DOOR_CLOSE, net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        super.removed(player);
    }
}
