package com.y271727uy.moderndelight.block.kitchenware.gas_cooking.gas_cooking_stove;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.util.registry_util.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import javax.annotation.Nullable;

public class BurningGasCookingStoveBlock extends AbstractGasCookingStoveBlock {
    public BurningGasCookingStoveBlock() {
        super(Properties.copy(Blocks.IRON_BLOCK).lightLevel(state -> 12).noOcclusion());
    }
    
    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, net.minecraft.util.RandomSource random) {
        for(int i = 0; i < 3; ++i) {
            double d = (double)pos.getX() + world.random.nextDouble();
            double e = (double)pos.getY() +  world.random.nextDouble() * 0.5 + 1;
            double f = (double)pos.getZ() +  world.random.nextDouble();
            world.addParticle(ParticleTypes.LARGE_SMOKE, d, e, f, 0.0, 0.0, 0.0);
            world.addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);
        }
        if (world.getBlockState(pos.above()).getBlock() instanceof LayeredCauldronBlock){
            for (int i = 0;i < 3; ++i){
                double d = (double)pos.getX() + world.random.nextDouble();
                double e = (double)pos.getY() +  world.random.nextDouble() * 0.5 + 2;
                double f = (double)pos.getZ() +  world.random.nextDouble();
                world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, d, e, f, -0.0625 + world.random.nextDouble()/8, world.random.nextDouble()/4, -0.0625 + world.random.nextDouble()/8);
            }
        }
    }
    
    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.fireImmune()) {
            entity.setSecondsOnFire(entity.getRemainingFireTicks() + 2);
            if (entity.getRemainingFireTicks() == 0) {
                entity.setSecondsOnFire(10);
            }
        }
        entity.hurt(ModDamageTypes.of(world,ModDamageTypes.TURNED_TO_ASHES), 2);
        super.stepOn(world, pos, state, entity);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide){
            world.playSound(null, pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f,
                    net.minecraft.sounds.SoundEvents.FIRE_EXTINGUISH, net.minecraft.sounds.SoundSource.BLOCKS,
                    1.2f, world.random.nextFloat()+0.5f);
            if (state.getValue(HAS_BRACKET)){
                world.setBlock(pos, ModBlocks.GAS_COOKING_STOVE.get().defaultBlockState().setValue(GasCookingStoveBlock.HAS_BRACKET,true), 3);
            } else {
                world.setBlock(pos, ModBlocks.GAS_COOKING_STOVE.get().defaultBlockState(), 3);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BurningGasCookingStoveBlockEntity(pos, state);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level world, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.BURNING_GAS_COOKING_STOVE_BLOCK_ENTITY.get(),
                (world1, pos, state1, blockEntity) -> ((BurningGasCookingStoveBlockEntity)blockEntity).tick(world1, pos, state1));
    }
}
