package com.y271727uy.moderndelight.block.kitchenware.gas_cooking.gas_cooking_stove;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.biogas.GasCanisterBlock;
import com.y271727uy.moderndelight.block.biogas.GasCanisterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BurningGasCookingStoveBlockEntity extends BlockEntity {
    public BurningGasCookingStoveBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BURNING_GAS_COOKING_STOVE_BLOCK_ENTITY.get(), pos, state);
    }
    public void tick(Level world, BlockPos pos,BlockState state) {
        if (world.isClientSide){
            return;
        }
        if (world.random.nextFloat() < 0.15f && world.getGameTime() % 240L == 0L){
            if (world.getBlockState(pos.above()).getBlock() instanceof LayeredCauldronBlock){
                int level = world.getBlockState(pos.above()).getValue(LayeredCauldronBlock.LEVEL);
                if (level > 1){
                    world.setBlock(pos.above(),world.getBlockState(pos.above()).setValue(LayeredCauldronBlock.LEVEL,level-1), 3);
                    world.playSound(null,pos.getX(),pos.getY(),pos.getZ(),net.minecraft.sounds.SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS,1.0f,1.0f);
                } else world.setBlock(pos.above(), Blocks.CAULDRON.defaultBlockState(), 3);
            }
        }
        if (world.getGameTime() % 20L == 0L){
            world.playSound(null, pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f,
                    net.minecraft.sounds.SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS,
                    1.0f, world.random.nextFloat()+0.5f);
        }
        boolean hasGas = false;
        Direction[] directions = {Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH};

        for (Direction direction : directions) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = world.getBlockState(neighborPos);

            if (neighborState.getBlock() instanceof GasCanisterBlock) {
                if (neighborState.getValue(GasCanisterBlock.FACING) == direction.getOpposite()) {
                    BlockEntity blockEntity = world.getBlockEntity(neighborPos);
                    if (blockEntity instanceof GasCanisterBlockEntity entity && entity.reduceGas()) {
                        hasGas = true;
                        break;
                    }
                }
            }
        }
        if (!hasGas){
            world.playSound(null,
                    pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f,
                    net.minecraft.sounds.SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS,
                    1.0f, world.random.nextFloat()+0.5f);
            if (state.getValue(BurningGasCookingStoveBlock.HAS_BRACKET)){
                world.setBlock(pos, ModBlocks.GAS_COOKING_STOVE.get().defaultBlockState().setValue(GasCookingStoveBlock.HAS_BRACKET,true), 3);
            } else {
                world.setBlock(pos, ModBlocks.GAS_COOKING_STOVE.get().defaultBlockState(), 3);
            }
        }

    }

}


