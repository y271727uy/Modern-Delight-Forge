package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.kitchenware.steaming.ElectricSteamerBlock;
import com.y271727uy.moderndelight.block.kitchenware.steaming.ElectricSteamerBlockEntity;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class ElectricSteamerScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    public final ElectricSteamerBlockEntity blockEntity;
    private final ContainerData propertyDelegate;

    public ElectricSteamerScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf){
        this(syncId, inventory, inventory.player.level().getBlockEntity(buf.readBlockPos()), new SimpleContainerData(27));
    }

    public ElectricSteamerScreenHandler(int syncId, Inventory playerInventory,
                                        net.minecraft.world.level.block.entity.BlockEntity blockEntity, ContainerData propertyDelegate){
        super(ModScreenHandlers.ELECTRIC_STEAMER_SCREEN_HANDLER.get(),syncId);
        checkContainerSize((Container) blockEntity, 13);
        this.inventory = (Container) blockEntity;
        this.blockEntity = (ElectricSteamerBlockEntity) blockEntity;
        this.propertyDelegate = propertyDelegate;
        this.inventory.startOpen(playerInventory.player);

        this.addSlot(new Slot(inventory,0,54,8));
        this.addSlot(new Slot(inventory,1,72,8));
        this.addSlot(new Slot(inventory,2,36,26));
        this.addSlot(new Slot(inventory,3,54,26));
        this.addSlot(new Slot(inventory,4,72,26));
        this.addSlot(new Slot(inventory,5,90,26));
        this.addSlot(new Slot(inventory,6,36,44));
        this.addSlot(new Slot(inventory,7,54,44));
        this.addSlot(new Slot(inventory,8,72,44));
        this.addSlot(new Slot(inventory,9,90,44));
        this.addSlot(new Slot(inventory,10,54,62));
        this.addSlot(new Slot(inventory,11,72,62));
        this.addSlot(new Slot(inventory,12,152,61));

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);
        addDataSlots(propertyDelegate);
    }

    public boolean isWorking(){
        if (blockEntity.getLevel() != null){
            return blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).getValue(ElectricSteamerBlock.IS_WORKING);
        }
        return false;
    }

    public int getScaledProgress(int slot){
        int progress = this.propertyDelegate.get(slot);
        int maxProgress = this.propertyDelegate.get(slot+12); // Max Progress
        int progressArrowSize = 16;// Arrow's Width

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getScaledSteamProgress(){
        int progress = this.propertyDelegate.get(26);
        int maxProgress = ElectricSteamerBlockEntity.MAX_STEAM_PROGRESS; // Max Progress
        int progressArrowSize = 24;// Arrow's Width

        return progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getScaledSteam(){
        int progress = this.propertyDelegate.get(25);
        int maxProgress = ElectricSteamerBlockEntity.MAX_WATER_OR_STEAM; // Max Progress
        int progressArrowSize = 70;// Arrow's Width

        return progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getScaledWater(){
        int progress = this.propertyDelegate.get(24);
        int maxProgress = ElectricSteamerBlockEntity.MAX_WATER_OR_STEAM; // Max Progress
        int progressArrowSize = 41;// Arrow's Width

        return progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getWaterAmount(){
        return this.propertyDelegate.get(24);
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
            } else if (originalStack.getItem() == Items.WATER_BUCKET){
                if (!this.moveItemStackTo(originalStack, 12, 13, false)) {
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
