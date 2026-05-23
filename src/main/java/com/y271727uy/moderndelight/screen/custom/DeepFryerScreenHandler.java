package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying.DeepFryerBlockEntity;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import com.y271727uy.moderndelight.screen.util.OnlyShowSlotWrapper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

public class DeepFryerScreenHandler extends AbstractContainerMenu {
    private final net.minecraft.world.Container inventory;
    private final ContainerData propertyDelegate;
    public final DeepFryerBlockEntity blockEntity;
    public DeepFryerScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf){
        this(syncId, inventory, inventory.player.level().getBlockEntity(buf.readBlockPos()),
                new ContainerData() {
                    @Override
                    public int get(int index) { return 0; }
                    @Override
                    public void set(int index, int value) {}
                    @Override
                    public int getCount() { return 6; }
                });
    }
    public DeepFryerScreenHandler(int syncId, Inventory playerInventory,
                                  net.minecraft.world.level.block.entity.BlockEntity blockEntity, ContainerData containerData){
        super(ModScreenHandlers.DEEP_FRYER_SCREEN_HANDLER.get(), syncId);
        checkContainerSize(((net.minecraft.world.Container) blockEntity),4);
        this.inventory = ((net.minecraft.world.Container) blockEntity);
        ((net.minecraft.world.Container) blockEntity).startOpen(playerInventory.player);
        this.propertyDelegate = containerData;
        this.blockEntity = ((DeepFryerBlockEntity) blockEntity);

        this.addSlot(new OnlyShowSlotWrapper(inventory,0,54,21));
        this.addSlot(new OnlyShowSlotWrapper(inventory,1,72,21));
        this.addSlot(new OnlyShowSlotWrapper(inventory,2,90,21));
        this.addSlot(new OnlyShowSlotWrapper(inventory,3,108,21));

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);

        addDataSlots(containerData);
    }
    public boolean isBurning(){
        return propertyDelegate.get(4) > 0;
    }
    public int getScaledProgress(int slot){
        int progress = this.propertyDelegate.get(slot);
        int maxProgress = 300;
        int progressArrowSize = 16;// Arrow's Width

        return progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
    public boolean hasOil(){
        return this.propertyDelegate.get(5) > 0;
    }
    public int getOil(){
        return this.propertyDelegate.get(5);
    }
    public int getScaledOilLevel(){
        int progress = this.propertyDelegate.get(5);
        int maxProgress = DeepFryerBlockEntity.MAX_OIL; // Max Progress
        int progressArrowSize = 24;// Arrow's Width
        int result = progress != 0 ? progressArrowSize * progress/maxProgress : 0;
        return progress > 0 && result == 0 ? 1 : result;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasItem()) {
            ItemStack itemStack2 = slot2.getItem();
            itemStack = itemStack2.copy();
            if (slot >= 4 && slot < 31) {
                if (!this.moveItemStackTo(itemStack2, 31, 40, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slot >= 31 && slot < 40 && !this.moveItemStackTo(itemStack2, 4, 31, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot2.set(ItemStack.EMPTY);
            }
            slot2.setChanged();
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot2.onTake(player, itemStack2);
            this.broadcastChanges();
        }
        return itemStack;
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
