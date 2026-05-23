package com.y271727uy.moderndelight.block.kitchenware.decor;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.util.block_util.ImplementedInventory;
import com.y271727uy.moderndelight.screen.custom.CabinetScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;

public class CabinetBlockEntity extends BlockEntity implements MenuProvider, ImplementedInventory {
    public CabinetBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CABINET_BLOCK_ENTITY.get(), pos, state);
    }
    
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(36, ItemStack.EMPTY);
    public static final String CABINET_NAME = "display_name.moderndelight.cabinet_name";
    
    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }
    
    @Override
    public Component getDisplayName() {
        return Component.translatable(CABINET_NAME);
    }
    
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        for (int i = 0; i < inventory.size(); i++) {
            nbt.put("Item" + i, inventory.get(i).save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        for (int i = 0; i < inventory.size(); i++) {
            inventory.set(i, ItemStack.of(nbt.getCompound("Item" + i)));
        }
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
    
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new CabinetScreenHandler(syncId, playerInventory, this);
    }
}
