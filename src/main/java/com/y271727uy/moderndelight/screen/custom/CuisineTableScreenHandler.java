package com.y271727uy.moderndelight.screen.custom;

import com.y271727uy.moderndelight.block.kitchenware.CuisineTableBlockEntity;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import com.y271727uy.moderndelight.sound.ModSounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CuisineTableScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    public final CuisineTableBlockEntity blockEntity;
    
    public CuisineTableScreenHandler(int syncId, Inventory inventory, FriendlyByteBuf buf){
        this(syncId, inventory, inventory.player.level().getBlockEntity(buf.readBlockPos()));
    }
    
    public CuisineTableScreenHandler(int syncId, Inventory playerInventory,
                                     net.minecraft.world.level.block.entity.BlockEntity blockEntity){
        super(ModScreenHandlers.CUISINE_TABLE_SCREEN_HANDLER.get(), syncId);
        checkContainerSize(((Container) blockEntity), 3);
        this.inventory = ((Container) blockEntity);
        ((Container) blockEntity).startOpen(playerInventory.player);
        this.blockEntity = ((CuisineTableBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory, 0, 24, 49));
        this.addSlot(new Slot(inventory, 1, 24, 28));
        this.addSlot(new Slot(inventory, 2, 136, 49) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                consumeInputs(player);
                super.onTake(player, stack);
            }
        });

        addPlayerHotbar(playerInventory);
        addPlayerInventory(playerInventory);
    }
    
    public void populateResult(ItemStack stack){
        com.y271727uy.moderndelight.networking.packet.UpdateInventoryC2SPacket.send(this.blockEntity.getBlockPos(), stack);
    }
    
    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot == 2) {
                originalStack.getItem().onCraftedBy(originalStack, player.level(), player);
                if (!this.moveItemStackTo(originalStack, 3, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                consumeInputs(player);
                slot.onQuickCraft(originalStack, newStack);
            } else if (invSlot < 2) {
                if (!this.moveItemStackTo(originalStack, 3, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, 2, false)) {
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

    private void consumeInputs(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        ItemStack tool = this.inventory.getItem(1);
        Level level = serverPlayer.level();
        BlockPos pos = this.blockEntity.getBlockPos();
        this.inventory.removeItem(0, 1);
        if (tool.isDamageableItem()) {
            if (tool.getMaxDamage() > tool.getDamageValue() + 1) {
                tool.setDamageValue(tool.getDamageValue() + 1);
                this.inventory.setItem(1, tool);
            } else {
                this.inventory.setItem(1, ItemStack.EMPTY);
                level.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        } else {
            this.inventory.removeItem(1, 1);
        }
        this.inventory.setChanged();
        level.playSound(null, pos, ModSounds.ITEM_STONE_MORTAR_WORKING.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public void removed(Player player) {
        if (!player.level().isClientSide && player.level().getBlockEntity(this.blockEntity.getBlockPos()) instanceof CuisineTableBlockEntity entity) {
            entity.setItem(2, ItemStack.EMPTY);
            entity.setCanOpen(true);
        }
        super.removed(player);
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
