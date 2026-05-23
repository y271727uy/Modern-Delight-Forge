package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.power.alternator.PhotovoltaicGeneratorBlockEntity;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class PhotovoltaicGeneratorScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;
    public final PhotovoltaicGeneratorBlockEntity blockEntity;
    public PhotovoltaicGeneratorScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf){
        this(syncId, inventory, inventory.player.level().getBlockEntity(buf.readBlockPos()),
                new SimpleContainerData(4));
    }

    public PhotovoltaicGeneratorScreenHandler(int syncId, Inventory playerInventory,
                                              net.minecraft.world.level.block.entity.BlockEntity blockEntity, ContainerData arrayPropertyDelegate){
        super(ModScreenHandlers.PHOTOVOLTAIC_GENERATOR_SCREEN_HANDLER.get(),syncId);
        checkContainerSize((Container) blockEntity,1);
        this.inventory = (Container) blockEntity;
        this.inventory.startOpen(playerInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = ((PhotovoltaicGeneratorBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory,0,143,52));

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);

        addDataSlots(arrayPropertyDelegate);
    }

    public boolean isWorking(){
        return propertyDelegate.get(0) != 0;
    }

    public boolean isInSlowMode(){
        return propertyDelegate.get(3) != 0;
    }

    public int getYValue(){
        return blockEntity.getBlockPos().getY();
    }

    public int getPower(){
        return this.propertyDelegate.get(1);
    }

    public int getScaledProgress(){
        int progress = this.propertyDelegate.get(1);
        int maxProgress = this.propertyDelegate.get(2); // Max Progress
        int progressArrowSize = 53;// Arrow's Width
        return progress != 0 && maxProgress != 0 ? progress * progressArrowSize / maxProgress : 0;
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
        Vec3 v = Vec3.atCenterOf(pos);
        return !blockEntity.isRemoved() && player.position().closerThan(v, 8.0);
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
