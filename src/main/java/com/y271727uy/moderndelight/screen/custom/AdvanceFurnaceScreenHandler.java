package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.kitchenware.AdvanceFurnaceBlockEntity;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import com.y271727uy.moderndelight.screen.util.OnlyExtractSlot;
import com.y271727uy.moderndelight.screen.util.OnlyExtractSlotContainer;
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
import net.minecraftforge.common.ForgeHooks;
import net.minecraft.world.item.crafting.RecipeType;

public class AdvanceFurnaceScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData propertyDelegate;
    public final AdvanceFurnaceBlockEntity blockEntity;
    public AdvanceFurnaceScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf){
        this(syncId, inventory, inventory.player.level().getBlockEntity(buf.readBlockPos()),
                new SimpleContainerData(8));
    }

    public AdvanceFurnaceScreenHandler(int syncId, Inventory playerInventory,
                                       net.minecraft.world.level.block.entity.BlockEntity blockEntity, ContainerData arrayPropertyDelegate){
        super(ModScreenHandlers.ADVANCE_FURNACE_SCREEN_HANDLER.get(),syncId);
        checkContainerSize((Container) blockEntity,9);
        this.inventory = (Container) blockEntity;
        this.inventory.startOpen(playerInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = ((AdvanceFurnaceBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory,0,44,16));
        this.addSlot(new Slot(inventory,1,80,16));
        this.addSlot(new Slot(inventory,2,116,16));
        this.addSlot(new Slot(inventory,3,152,16));
        this.addSlot(new OnlyExtractSlotContainer(inventory,4,44,60));
        this.addSlot(new OnlyExtractSlotContainer(inventory,5,80,60));
        this.addSlot(new OnlyExtractSlotContainer(inventory,6,116,60));
        this.addSlot(new OnlyExtractSlotContainer(inventory,7,152,60));
        this.addSlot(new Slot(inventory,8,8,28));

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);

        addDataSlots(arrayPropertyDelegate);
    }

    public boolean isCrafting(int slot){
        return switch (slot){
            case 0 -> propertyDelegate.get(0) > 0;
            case 1 -> propertyDelegate.get(1) > 0;
            case 2 -> propertyDelegate.get(2) > 0;
            case 3 -> propertyDelegate.get(3) > 0;
            default -> false;
        };
    }

    public boolean isBurning(){
        return propertyDelegate.get(5) > 0;
    }
    public int getExperiences(){
        return propertyDelegate.get(7);
    }

    public int getScaledProgress(int slot){
        int progress = this.propertyDelegate.get(slot);
        int maxProgress = this.propertyDelegate.get(4); // Max Progress
        int progressArrowSize = 24;// Arrow's Width

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getScaledBurnTime(){
        int progress = this.propertyDelegate.get(5);
        int maxProgress = this.propertyDelegate.get(6); // Max Progress
        int progressArrowSize = 14;// Arrow's Width
        int result = maxProgress != 0 && progress != 0 ? (progressArrowSize * progress/maxProgress) : 0;
        return progress > 0 && result == 0 ? 2 : result;
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
            } else if (ForgeHooks.getBurnTime(originalStack, null) > 0){
                if (!this.moveItemStackTo(originalStack, 8, 9, false)) {
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
                this.addSlot(new Slot(playerInventory, l + i * 9 +9, 8 +l *18, 90 +i * 18));
            }
        }
    }
    private void addPlayerHotbar(Inventory playerInventory){
        for (int i = 0; i < 9; ++i){
            this.addSlot(new Slot (playerInventory, i, 8 + i * 18, 148));
        }
    }
}
