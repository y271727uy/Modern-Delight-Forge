package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.power.ChargingPostBlockEntity;
import com.y271727uy.moderndelight.block.power.batteries.BatteryBlockItem;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class ChargingPostScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;
    public final ChargingPostBlockEntity blockEntity;
    
    public ChargingPostScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf){
        this(syncId, inventory, inventory.player.level().getBlockEntity(buf.readBlockPos()),
                new SimpleContainerData(4));
    }
    
    public ChargingPostScreenHandler(int syncId, Inventory playerInventory,
                                     net.minecraft.world.level.block.entity.BlockEntity blockEntity, ContainerData arrayPropertyDelegate){
        super(ModScreenHandlers.CHARGING_POST_SCREEN_HANDLER.get(), syncId);
        checkContainerSize(((Container) blockEntity), 3);
        this.inventory = ((Container) blockEntity);
        ((Container) blockEntity).startOpen(playerInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = ((ChargingPostBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory, 0, 35, 29));
        this.addSlot(new Slot(inventory, 1, 35, 47));
        this.addSlot(new Slot(inventory, 2, 125, 39));

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);

        addDataSlots(arrayPropertyDelegate);
    }
    
    public int getPower(){
        return this.propertyDelegate.get(2);
    }
    
    public int getScaledPower(){
        int progress = this.propertyDelegate.get(2);
        int maxProgress = this.propertyDelegate.get(3); // Max Progress
        int progressArrowSize = 53;// Arrow's Width
        return progress != 0 && maxProgress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
    
    public boolean isWorking(){
        return propertyDelegate.get(0) != 0;
    }
    
    public int getScaledProgress(){
        int progress = this.propertyDelegate.get(1);
        int maxProgress = 10; // Max Progress
        int progressArrowSize = 59;// Arrow's Width
        return progress != 0 ? progress * progressArrowSize / maxProgress : 0;
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
            } else if (!(originalStack.getItem() instanceof BatteryBlockItem)){
                if (!this.moveItemStackTo(originalStack, 2, 3, false)) {
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
        Vec3 v = Vec3.atCenterOf(pos);
        return !blockEntity.isRemoved() && player.position().closerThan(v, 8.0);
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