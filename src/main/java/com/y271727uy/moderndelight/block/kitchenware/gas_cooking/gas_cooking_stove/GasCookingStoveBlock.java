package com.y271727uy.moderndelight.block.kitchenware.gas_cooking.gas_cooking_stove;

import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.sound.ModSounds;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import javax.annotation.Nullable;

import java.util.List;

public class GasCookingStoveBlock extends AbstractGasCookingStoveBlock {
    public GasCookingStoveBlock() {
        super(Properties.copy(Blocks.IRON_BLOCK).noOcclusion());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, net.minecraft.world.item.TooltipFlag options) {
        if(net.minecraft.client.gui.screens.Screen.hasShiftDown()){
            tooltip.add(TextUtil.getShiftText(true));
            tooltip.add(Component.literal(" "));
            tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.GAS_COOKING_STOVE)));
        }else {
            tooltip.add(TextUtil.getShiftText(false));
        }
        super.appendHoverText(stack, world, tooltip, options);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide){
            if (world.random.nextFloat()>0.5){
                world.playSound(null, pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f,
                        ModSounds.BLOCK_GAS_COOKING_STOVE_IGNITE.get(), net.minecraft.sounds.SoundSource.BLOCKS,
                        2.0f, world.random.nextFloat()+0.5f);
            } else {
                world.playSound(null, pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f,
                        net.minecraft.sounds.SoundEvents.FLINTANDSTEEL_USE, net.minecraft.sounds.SoundSource.BLOCKS,
                        1.0f, world.random.nextFloat()+0.5f);
                if (state.getValue(HAS_BRACKET)){
                    world.setBlock(pos, ModBlocks.BURNING_GAS_COOKING_STOVE.get().defaultBlockState().setValue(BurningGasCookingStoveBlock.HAS_BRACKET,true), 3);
                } else {
                    world.setBlock(pos, ModBlocks.BURNING_GAS_COOKING_STOVE.get().defaultBlockState(), 3);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
