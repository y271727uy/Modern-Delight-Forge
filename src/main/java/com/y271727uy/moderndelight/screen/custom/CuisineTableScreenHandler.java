package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.kitchenware.CuisineTableBlockEntity;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import com.y271727uy.moderndelight.screen.util.OnlyShowSlotWrapper;
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

public class CuisineTableScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;
    public final CuisineTableBlockEntity blockEntity;
    
    public CuisineTableScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf){
        this(syncId, inventory, inventory.player.level().getBlockEntity(buf.readBlockPos()),
                new SimpleContainerData(1));
    }
    
    public CuisineTableScreenHandler(int syncId, Inventory playerInventory,
                                     net.minecraft.world.level.block.entity.BlockEntity blockEntity, ContainerData arrayPropertyDelegate){
        super(ModScreenHandlers.CUISINE_TABLE_SCREEN_HANDLER.get(), syncId);
        checkContainerSize(((Container) blockEntity), 10);
        this.inventory = ((Container) blockEntity);
        ((Container) blockEntity).startOpen(playerInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = ((CuisineTableBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory, 0, 25, 26));
        this.addSlot(new Slot(inventory, 1, 25, 47));
        this.addSlot(new OnlyShowSlotWrapper(inventory, 9, 134, 35));

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);

        addDataSlots(arrayPropertyDelegate);
    }
    
    public void populateResult(ItemStack stack){
        this.inventory.setItem(9, stack);
    }
    
    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < 2) {
                if (!this.moveItemStackTo(originalStack, 3, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (invSlot >= 3 && !this.moveItemStackTo(originalStack, 0, 2, false)) {
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
