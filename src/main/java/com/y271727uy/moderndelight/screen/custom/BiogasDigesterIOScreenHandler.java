package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.biogas.BiogasDigesterIOBlockEntity;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import com.y271727uy.moderndelight.screen.util.OnlyExtractSlotWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class BiogasDigesterIOScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData data;
    public final BiogasDigesterIOBlockEntity blockEntity;

    public BiogasDigesterIOScreenHandler(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncId, playerInventory,
                (BiogasDigesterIOBlockEntity) playerInventory.player.level().getBlockEntity(buf.readBlockPos()),
                new SimpleContainerData(5));
    }

    public BiogasDigesterIOScreenHandler(int syncId, Inventory playerInventory, Container blockEntity, ContainerData data) {
        super(ModScreenHandlers.BIOGAS_DIGESTER_IO_SCREEN_HANDLER.get(), syncId);
        checkContainerSize(blockEntity, 12);
        this.inventory = blockEntity;
        this.data = data;
        this.blockEntity = (BiogasDigesterIOBlockEntity) blockEntity;

        blockEntity.startOpen(playerInventory.player);

        this.addSlot(new Slot(inventory,0,8,19));
        this.addSlot(new Slot(inventory,1,26,19));
        this.addSlot(new Slot(inventory,2,44,19));
        this.addSlot(new Slot(inventory,3,8,37));
        this.addSlot(new Slot(inventory,4,26,37));
        this.addSlot(new Slot(inventory,5,44,37));
        this.addSlot(new Slot(inventory,6,8,55));
        this.addSlot(new Slot(inventory,7,26,55));
        this.addSlot(new Slot(inventory,8,44,55));
        
        // 创建只提取槽位（使用自定义逻辑）
        this.addSlot(new OnlyExtractSlotWrapper(inventory,9,152,19));
        this.addSlot(new OnlyExtractSlotWrapper(inventory,10,152,37));
        this.addSlot(new OnlyExtractSlotWrapper(inventory,11,152,55));

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getGasValue() {
        return (data.get(4) << 16) | (data.get(2) & 0xFFFF);
    }

    public boolean isChecked() {
        return data.get(3) != 0;
    }

    public int getScaledProgress() {
        int progress = data.get(0);
        int maxProgress = data.get(1);
        int progressArrowSize = 84;
        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (index < this.inventory.getContainerSize()) {
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
        Vec3 center = Vec3.atCenterOf(pos);
        return !blockEntity.isRemoved() && player.position().closerThan(center, 8.0);
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
