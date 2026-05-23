package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.kitchenware.FreezerBlock;
import com.y271727uy.moderndelight.block.kitchenware.FreezerBlockEntity;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import com.y271727uy.moderndelight.screen.util.OnlyExtractSlot;
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

public class FreezerScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;
    public final FreezerBlockEntity blockEntity;
    public FreezerScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf){
        this(syncId, inventory, (FreezerBlockEntity) inventory.player.level().getBlockEntity(buf.readBlockPos()),
                new SimpleContainerData(5));
    }
    public FreezerScreenHandler(int syncId, Inventory playerInventory,
                                net.minecraft.world.level.block.entity.BlockEntity blockEntity, ContainerData arrayPropertyDelegate){
        super(ModScreenHandlers.FREEZER_SCREEN_HANDLER.get(),syncId);
        checkContainerSize(((Container) blockEntity),20);
        this.inventory = ((Container) blockEntity);
        inventory.startOpen(playerInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = ((FreezerBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory,0,116,16));
        this.addSlot(new Slot(inventory,1,134,16));
        this.addSlot(new Slot(inventory,2,152,16));
        this.addSlot(new Slot(inventory,3,116,52));
        this.addSlot(new OnlyExtractSlot(inventory,4,152,52));
        for (int i = 0; i < 3; ++i){
            for (int l = 0; l < 5; ++l){
                this.addSlot(new Slot(inventory, l + i * 5 +5, 8 +l *18, 16 +i * 18));
            }
        }
        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);

        addDataSlots(arrayPropertyDelegate);
    }
    public int getExperiences() {
        return this.propertyDelegate.get(4);
    }
    public boolean isCooling(){
        return propertyDelegate.get(2) > 0;
    }
    public boolean isCrafting(){
        return propertyDelegate.get(0) > 0;
    }

    public int getScaledProgress(){
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1); // Max Progress
        int progressArrowSize = 16;// Arrow's Width

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
    public int getScaledCoolTime(){
        int progress = this.propertyDelegate.get(2);
        int maxProgress = this.propertyDelegate.get(3); // Max Progress
        int progressArrowSize = 16;// Arrow's Width
        int result = maxProgress != 0 && progress != 0 ? (progressArrowSize * progress/maxProgress) : 0;
        return progress > 0 && result == 0 ? 1 : result;
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
            } else if (blockEntity.canUseAsIce(originalStack)) {
                if (!this.moveItemStackTo(originalStack, 3, 4, true)) {
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
        Vec3 v = new Vec3(pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5);
        return !blockEntity.isRemoved() && v.closerThan(player.position(), 8.0) && blockEntity.getBlockState().getValue(FreezerBlock.IS_OPEN);
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
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
