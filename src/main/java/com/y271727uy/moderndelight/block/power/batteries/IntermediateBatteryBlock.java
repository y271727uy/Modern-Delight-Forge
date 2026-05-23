package com.y271727uy.moderndelight.block.power.batteries;

import com.y271727uy.moderndelight.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class IntermediateBatteryBlock extends AbstractBatteryBlock {
    public IntermediateBatteryBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    protected Block getBlock() {
        return ModBlocks.INTERMEDIATE_BATTERY.get();
    }
    
    @Override
    public long getMaxPower(){
        return 15000;
    }

    private static final VoxelShape SHAPED = Block.box(2,0,2,14,16,14);
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPED;
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPED;
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BatteryBlockEntity(pos, state, getMaxPower());
    }
}
