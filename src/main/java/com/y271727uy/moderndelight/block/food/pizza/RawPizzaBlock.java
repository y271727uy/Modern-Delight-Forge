package com.y271727uy.moderndelight.block.food.pizza;

import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;

public class RawPizzaBlock extends AbstractPizzaBlock {
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RawPizzaBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock() && level.getBlockEntity(pos) instanceof RawPizzaBlockEntity blockEntity && !level.isClientSide) {
            ItemStack itemStack = new ItemStack(ModBlocks.RAW_PIZZA_ITEM.get());
            CompoundTag tag = blockEntity.saveWithoutMetadata();
            BlockItem.setBlockEntityData(itemStack, ModBlockEntities.RAW_PIZZA_BLOCK_ENTITY.get(), tag);
            ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, itemStack);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, net.minecraft.world.phys.HitResult target, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.world.entity.player.Player player) {
        ItemStack itemStack = new ItemStack(ModBlocks.RAW_PIZZA_ITEM.get());
        if (level.getBlockEntity(pos) instanceof RawPizzaBlockEntity blockEntity) {
            CompoundTag tag = blockEntity.saveWithoutMetadata();
            BlockItem.setBlockEntityData(itemStack, ModBlockEntities.RAW_PIZZA_BLOCK_ENTITY.get(), tag);
        }
        return itemStack;
    }
}
