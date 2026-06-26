package com.y271727uy.moderndelight.block.food.pizza;

import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import javax.annotation.Nullable;
import java.util.List;

public class RawPizzaBlock extends AbstractPizzaBlock {
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RawPizzaBlockEntity(pos, state);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof RawPizzaBlockEntity rawPizzaBlockEntity) {
            ItemStack itemStack = new ItemStack(ModBlocks.RAW_PIZZA_ITEM.get());
            CompoundTag tag = rawPizzaBlockEntity.saveWithoutMetadata();
            BlockItem.setBlockEntityData(itemStack, ModBlockEntities.RAW_PIZZA_BLOCK_ENTITY.get(), tag);
            return List.of(itemStack);
        }
        return super.getDrops(state, builder);
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
