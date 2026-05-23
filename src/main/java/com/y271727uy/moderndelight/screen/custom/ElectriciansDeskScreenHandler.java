package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.power.ElectriciansDeskBlockEntity;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import com.y271727uy.moderndelight.screen.util.OnlyExtractSlotWrapper;
import com.y271727uy.moderndelight.util.MiscUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class ElectriciansDeskScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;
    public final ElectriciansDeskBlockEntity blockEntity;

    public ElectriciansDeskScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf) {
        this(syncId, inventory, inventory.player.level().getBlockEntity(buf.readBlockPos()),
                new SimpleContainerData(2));
    }

    public ElectriciansDeskScreenHandler(int syncId, Inventory playerInventory,
                                         net.minecraft.world.level.block.entity.BlockEntity blockEntity, ContainerData arrayPropertyDelegate) {
        super(ModScreenHandlers.ELECTRICIANS_DESK_SCREEN_HANDLER.get(), syncId);
        checkContainerSize(((Container) blockEntity), 9);
        this.inventory = ((Container) blockEntity);
        ((Container) blockEntity).startOpen(playerInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = ((ElectriciansDeskBlockEntity) blockEntity);
        
        this.addSlot(new Slot(inventory, 0, 24, 17));
        this.addSlot(new Slot(inventory, 1, 42, 17));
        this.addSlot(new Slot(inventory, 2, 60, 17));
        this.addSlot(new Slot(inventory, 3, 24, 35));
        this.addSlot(new Slot(inventory, 4, 42, 35));
        this.addSlot(new Slot(inventory, 5, 60, 35));

        this.addSlot(new Slot(inventory, 6, 135, 17));
        this.addSlot(new Slot(inventory, 7, 135, 35));

        this.addSlot(new OnlyExtractSlotWrapper(inventory, 8, 114, 56));

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);

        addDataSlots(arrayPropertyDelegate);
    }

    public boolean canCraft() {
        return this.propertyDelegate.get(0) != 0;
    }

    public boolean isOccupied() {
        return this.propertyDelegate.get(1) != 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize() - 1) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize() - 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (MiscUtil.isInk(originalStack.getItem())) {
                if (!this.moveItemStackTo(originalStack, 7, 8, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (originalStack.getItem() == Items.PAPER) {
                if (!this.moveItemStackTo(originalStack, 6, 7, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize() - 1, false)) {
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

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
