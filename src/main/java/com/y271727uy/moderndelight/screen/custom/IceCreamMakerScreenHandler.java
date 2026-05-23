package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.kitchenware.ice_cream_maker.IceCreamMakerBlockEntity;
import com.y271727uy.moderndelight.item.food.CreamItem;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import com.y271727uy.moderndelight.screen.util.OnlyExtractSlot;
import com.y271727uy.moderndelight.util.enums.CreamFlavor;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class IceCreamMakerScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;
    public final IceCreamMakerBlockEntity blockEntity;
    public IceCreamMakerScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf){
        this(syncId, inventory, (IceCreamMakerBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos()),
                new SimpleContainerData(11));
    }
    public IceCreamMakerScreenHandler(int syncId, Inventory playerInventory,
                                      IceCreamMakerBlockEntity blockEntity, ContainerData arrayPropertyDelegate){
        super(ModScreenHandlers.ICE_CREAM_MAKER_SCREEN_HANDLER.get(),syncId);
        checkContainerSize(blockEntity,5);
        this.inventory = blockEntity;
        blockEntity.startOpen(playerInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = blockEntity;

        this.addSlot(new Slot(inventory,0,44,17));
        this.addSlot(new Slot(inventory,1,44,35));
        this.addSlot(new Slot(inventory,2,44,53));
        this.addSlot(new OnlyExtractSlot(inventory,3,25,53));


        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);

        addDataSlots(arrayPropertyDelegate);
    }
    public int getScaledProgress(){
        int progress = this.propertyDelegate.get(1);
        int maxProgress = 10; // Max Progress
        int progressArrowSize = 19;// Arrow's Width
        return progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
    public boolean isPowered(){
        return this.propertyDelegate.get(0) != 0;
    }
    public IceCreamMakerBlockEntity.IceCream getIceCream1(){
        IceCreamMakerBlockEntity.IceCream iceCream = new IceCreamMakerBlockEntity.IceCream(
                CreamFlavor.getFlavorByID(this.propertyDelegate.get(2)),this.propertyDelegate.get(3)
        );
        iceCream.setSelected(this.propertyDelegate.get(4));
        return iceCream;
    }
    public IceCreamMakerBlockEntity.IceCream getIceCream2(){
        IceCreamMakerBlockEntity.IceCream iceCream = new IceCreamMakerBlockEntity.IceCream(
                CreamFlavor.getFlavorByID(this.propertyDelegate.get(5)),this.propertyDelegate.get(6)
        );
        iceCream.setSelected(this.propertyDelegate.get(7));
        return iceCream;
    }
    public IceCreamMakerBlockEntity.IceCream getIceCream3(){
        IceCreamMakerBlockEntity.IceCream iceCream = new IceCreamMakerBlockEntity.IceCream(
                CreamFlavor.getFlavorByID(this.propertyDelegate.get(8)),this.propertyDelegate.get(9)
        );
        iceCream.setSelected(this.propertyDelegate.get(10));
        return iceCream;
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
            } else if (originalStack.getItem() instanceof CreamItem) {
                if (!this.moveItemStackTo(originalStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.getItem() == Items.SUGAR) {
                if (!this.moveItemStackTo(originalStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.getItem() == Items.EGG) {
                if (!this.moveItemStackTo(originalStack, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }
            } else return ItemStack.EMPTY;

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
        Vec3 v = new Vec3(pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5);
        return !blockEntity.isRemoved() && v.distanceToSqr(player.position()) <= 64.0;
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
