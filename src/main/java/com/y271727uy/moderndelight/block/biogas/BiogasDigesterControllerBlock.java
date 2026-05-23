package com.y271727uy.moderndelight.block.biogas;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;

import java.util.List;

public class BiogasDigesterControllerBlock extends BaseEntityBlock {
    public BiogasDigesterControllerBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.LAPIS_BLOCK));
    }
    @Nullable
    @Override
    public BiogasDigesterControllerBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BiogasDigesterControllerBlockEntity(pos,state);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            if (world.getBlockEntity(pos) instanceof BiogasDigesterControllerBlockEntity blockEntity){
                if (blockEntity.getGasValue() > 1000){
                    blockEntity.createExplode(world);
                } else if (blockEntity.getGasValue() > 500){
                    world.setBlock(pos.below(), ModBlocks.LIQUEFIED_BIOGAS_FLUID_BLOCK.get().defaultBlockState(), 3);
                }
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        if (Screen.hasShiftDown()){
            tooltip.add(TextUtil.getShiftText(true));
            tooltip.add(Component.literal(" "));
            tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.BDC)));
        } else {
            tooltip.add(TextUtil.getShiftText(false));
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            if (world.getBlockEntity(pos) instanceof BiogasDigesterControllerBlockEntity blockEntity) {
                player.openMenu(blockEntity);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.BIOGAS_DIGESTER_CONTROLLER_BLOCK_ENTITY.get(),
                (world1, pos1, state1, blockEntity) -> blockEntity.tick(world1, pos1));
    }
}
