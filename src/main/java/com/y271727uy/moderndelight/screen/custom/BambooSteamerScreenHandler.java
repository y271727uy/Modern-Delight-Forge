package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.kitchenware.steaming.BambooGrateBlock;
import com.y271727uy.moderndelight.block.kitchenware.steaming.BambooGrateBlockEntity;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BambooSteamerScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    public final BambooGrateBlockEntity blockEntity;
    public int currentLayer;
    private final ContainerData containerData;
    
    public BambooSteamerScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf){
        this(syncId, inventory, inventory.player.level().getBlockEntity(buf.readBlockPos()), buf.readInt(), new SimpleContainerData(35));
    }
    
    public BambooSteamerScreenHandler(int syncId, Inventory playerInventory,
                                      net.minecraft.world.level.block.entity.BlockEntity blockEntity, int currentLayer, ContainerData containerData){
        super(ModScreenHandlers.BAMBOO_STEAMER_SCREEN_HANDLER.get(), syncId);
        this.currentLayer = currentLayer;
        checkContainerSize(((Container) blockEntity), 16);
        this.inventory = ((Container) blockEntity);
        this.inventory.startOpen(playerInventory.player);
        this.blockEntity = ((BambooGrateBlockEntity) blockEntity);
        this.containerData = containerData;
        
        switch (currentLayer){
            case 1 -> {
                quickAddSlot(0, 71, 25);
                quickAddSlot(1, 89, 25);
                quickAddSlot(2, 71, 43);
                quickAddSlot(3, 89, 43);
            }
            case 2 -> {
                quickAddSlot(0, 52, 25);
                quickAddSlot(1, 70, 25);
                quickAddSlot(2, 52, 43);
                quickAddSlot(3, 70, 43);
                quickAddSlot(4, 91, 25);
                quickAddSlot(5, 109, 25);
                quickAddSlot(6, 91, 43);
                quickAddSlot(7, 109, 43);
            }
            case 3 -> {
                quickAddSlot(0, 33, 25);
                quickAddSlot(1, 51, 25);
                quickAddSlot(2, 33, 43);
                quickAddSlot(3, 51, 43);
                quickAddSlot(4, 72, 25);
                quickAddSlot(5, 90, 25);
                quickAddSlot(6, 72, 43);
                quickAddSlot(7, 90, 43);
                quickAddSlot(8, 111, 25);
                quickAddSlot(9, 129, 25);
                quickAddSlot(10, 111, 43);
                quickAddSlot(11, 129, 43);
            }
            case 4 -> {
                quickAddSlot(0, 7, 25);
                quickAddSlot(1, 25, 25);
                quickAddSlot(2, 7, 43);
                quickAddSlot(3, 25, 43);
                quickAddSlot(4, 43, 25);
                quickAddSlot(5, 61, 25);
                quickAddSlot(6, 43, 43);
                quickAddSlot(7, 61, 43);
                quickAddSlot(8, 79, 25);
                quickAddSlot(9, 97, 25);
                quickAddSlot(10, 79, 43);
                quickAddSlot(11, 97, 43);
                quickAddSlot(12, 115, 25);
                quickAddSlot(13, 133, 25);
                quickAddSlot(14, 115, 43);
                quickAddSlot(15, 133, 43);
            }
        }

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);
        addDataSlots(containerData);
    }
    
    private void quickAddSlot(int index, int x, int y){
        this.addSlot(new Slot(inventory, index, x, y));
    }
    
    public boolean isHeated(){
        return containerData.get(33) != 0;
    }
    
    public boolean isCovered(){
        return containerData.get(32) != 0;
    }
    
    public int getLayer(){
        return containerData.get(34);
    }
    
    public int getCurrentLayer() {
        return blockEntity.getBlockState().getValue(BambooGrateBlock.LAYER);
    }

    public int getScaledProgress(int slot){
        int progress = this.containerData.get(slot);
        int maxProgress = this.containerData.get(slot + 16);
        int progressArrowSize = 16;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
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
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
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
        Vec3 v = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        Level world = player.level();
        return !blockEntity.isRemoved() && v.closerThan(player.position(), 8.0) &&
                world.getBlockState(pos).getValue(BambooGrateBlock.LAYER) == this.currentLayer;
    }
    
    private void addPlayerInventory(Inventory playerInventory){
        for (int i = 0; i < 3; ++i){
            for (int l = 0; l < 9; ++l){
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }
    
    private void addPlayerHotbar(Inventory playerInventory){
        for (int i = 0; i < 9; ++i){
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
