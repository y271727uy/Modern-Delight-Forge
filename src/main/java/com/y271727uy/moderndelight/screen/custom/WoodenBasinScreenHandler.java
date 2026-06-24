package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying.WoodenBasinBlockEntity;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import com.y271727uy.moderndelight.screen.util.OnlyExtractSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

public class WoodenBasinScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    public final WoodenBasinBlockEntity blockEntity;
    public WoodenBasinScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf){
        this(syncId, inventory, (WoodenBasinBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos()));
    }
    public WoodenBasinScreenHandler(int syncId, Inventory playerInventory, net.minecraft.world.level.block.entity.BlockEntity blockEntity){
        super(ModScreenHandlers.WOODEN_BASIN_SCREEN_HANDLER.get(),syncId);
        checkContainerSize(((Container) blockEntity),5);
        this.inventory = ((Container) blockEntity);
        ((Container) blockEntity).startOpen(playerInventory.player);
        this.blockEntity = ((WoodenBasinBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory,0,20,21) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() == Items.BUCKET || stack.getItem() == Items.GLASS_BOTTLE;
            }
        });
        this.addSlot(new OnlyExtractSlot( inventory,1,20,52));
        this.addSlot(new Slot(inventory,2,95,53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return WoodenBasinBlockEntity.isFilter(stack.getItem());
            }
        });
        this.addSlot(new Slot(inventory,3,95,21) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() != Items.BUCKET
                        && stack.getItem() != Items.GLASS_BOTTLE
                        && !WoodenBasinBlockEntity.isFilter(stack.getItem());
            }
        });
        this.addSlot(new OnlyExtractSlot( inventory,4,141,53));

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.getItem() == Items.BUCKET ||
            originalStack.getItem() == Items.GLASS_BOTTLE) {
                if (!this.moveItemStackTo(originalStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (WoodenBasinBlockEntity.isFilter(originalStack.getItem())) {
                if (!this.moveItemStackTo(originalStack, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 3, 4, false)) {
                return ItemStack.EMPTY;
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
        return !blockEntity.isRemoved() && player.distanceToSqr(pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5) <= 64.0;
    }
    private void addPlayerInventory(Inventory playerInventory){
        for (int i = 0; i < 3; ++i){
            for (int l = 0; l < 9; ++l){
                this.addSlot(new Slot(playerInventory, l + i * 9 +9, 8 +l *18, 84 +i * 18));
            }
        }
    }
    private void addPlayerHotbar(Inventory playerInventory){
        for (int i = 0; i < 9; ++i){
            this.addSlot(new Slot (playerInventory, i, 8 + i * 18, 142));
        }
    }

}
