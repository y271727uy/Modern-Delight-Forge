package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.power.TeslaCoilBlockEntity;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class TeslaCoilScreenHandler extends AbstractContainerMenu {
    private final ContainerData propertyDelegate;
    public final TeslaCoilBlockEntity blockEntity;
    public TeslaCoilScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf){
        this(syncId, inventory, inventory.player.level().getBlockEntity(buf.readBlockPos()),
                new SimpleContainerData(2));
    }

    public TeslaCoilScreenHandler(int syncId, Inventory playerInventory,
                                  net.minecraft.world.level.block.entity.BlockEntity blockEntity, ContainerData arrayPropertyDelegate){
        super(ModScreenHandlers.TESLA_COIL_SCREEN_HANDLER.get(),syncId);
        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = ((TeslaCoilBlockEntity) blockEntity);

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);

        addDataSlots(arrayPropertyDelegate);
    }

    public int getEfficiency(){
        return this.propertyDelegate.get(0);
    }

    public boolean getShowParticle(){
        return this.propertyDelegate.get(1) != 0;
    }
    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasItem()) {
            ItemStack itemStack2 = slot2.getItem();
            itemStack = itemStack2.copy();
            if (slot >= 0 && slot < 27) {
                if (!this.moveItemStackTo(itemStack2, 27, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slot >= 27 && slot < 36 && !this.moveItemStackTo(itemStack2, 0, 27, false)) {
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
        }
        return itemStack;
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
