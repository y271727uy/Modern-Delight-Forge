package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.power.alternator.ACDCConverterBlockEntity;
import com.y271727uy.moderndelight.block.power.batteries.AbstractBatteryBlock;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class ACDCConverterScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;
    public final ACDCConverterBlockEntity blockEntity;
    
    public ACDCConverterScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf){
        this(syncId, inventory, inventory.player.level().getBlockEntity(buf.readBlockPos()),
                new SimpleContainerData(6));
    }
    
    public ACDCConverterScreenHandler(int syncId, Inventory playerInventory,
                                      net.minecraft.world.level.block.entity.BlockEntity blockEntity, ContainerData arrayPropertyDelegate){
        super(ModScreenHandlers.ACDC_CONVERTER_SCREEN_HANDLER.get(), syncId);
        checkContainerSize(((Container) blockEntity), 1);
        this.inventory = ((Container) blockEntity);
        ((Container) blockEntity).startOpen(playerInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = ((ACDCConverterBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory, 0, 152, 52));

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);

        addDataSlots(arrayPropertyDelegate);
    }
    
    public boolean isDC2ACMode(){
        return propertyDelegate.get(2) != 0;
    }
    
    public int getEfficiency(){
        return propertyDelegate.get(5);
    }
    
    public int getPower(){
        return this.propertyDelegate.get(0);
    }
    
    public int getScaledProgress(){
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1); // Max Progress
        int progressArrowSize = 53;// Arrow's Width
        return progress != 0 && maxProgress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
    
    public int getScaledWorkSpeed(){
        int progress = this.propertyDelegate.get(3);
        int maxProgress = this.propertyDelegate.get(4); // Max Progress
        int progressArrowSize = 70;// Arrow's Width
        return progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
    
    public int getWorkSpeed(){
        return this.propertyDelegate.get(3);
    }
    
    public int getMaxWorkSpeed(){
        return this.propertyDelegate.get(4);
    }
    
    public int hasBattery(){
        Item item = blockEntity.getItem(0).getItem();
        if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractBatteryBlock){
            return 2;
        }
        if (item != Items.AIR){
            return 1;
        }
        return 0;
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