package com.y271727uy.moderndelight.block.power.alternator.thermal_power;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.kitchenware.AdvanceFurnaceBlock;
import com.y271727uy.moderndelight.block.kitchenware.OvenBlock;
import com.y271727uy.moderndelight.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.RenderUtils;

public class SterlingEngineBlockEntity extends BlockEntity implements GeoBlockEntity {
    public SterlingEngineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STERLING_ENGINE_BLOCK_ENTITY.get(), pos, state);
    }
    
    private static final RawAnimation IDLE = RawAnimation.begin().thenPlay("start").thenLoop("idle");
    private static final RawAnimation STOPPING = RawAnimation.begin().thenPlay("stop");

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private boolean hasStart = false;
    private int ticker = 30;
    
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> {
            Level world1 = state.getAnimatable().getLevel();
            BlockPos pos1 = state.getAnimatable().getBlockPos().below();
            if (world1 != null){
                if (world1.getBlockState(pos1).getBlock() instanceof net.minecraft.world.level.block.FurnaceBlock
                        && world1.getBlockState(pos1).getValue(net.minecraft.world.level.block.FurnaceBlock.LIT)){
                    return state.setAndContinue(IDLE);
                } else if (world1.getBlockState(pos1).getBlock() instanceof net.minecraft.world.level.block.BlastFurnaceBlock &&
                        world1.getBlockState(pos1).getValue(net.minecraft.world.level.block.BlastFurnaceBlock.LIT)) {
                    return state.setAndContinue(IDLE);
                } else if (world1.getBlockState(pos1).getBlock() instanceof net.minecraft.world.level.block.SmokerBlock &&
                        world1.getBlockState(pos1).getValue(net.minecraft.world.level.block.SmokerBlock.LIT)) {
                    return state.setAndContinue(IDLE);
                } else if (world1.getBlockState(pos1).getBlock() instanceof OvenBlock &&
                        world1.getBlockState(pos1).getValue(OvenBlock.OVEN_BURNING)) {
                    return state.setAndContinue(IDLE);
                } else if (world1.getBlockState(pos1).getBlock() instanceof AdvanceFurnaceBlock &&
                        world1.getBlockState(pos1).getValue(AdvanceFurnaceBlock.BURNING)) {
                    return state.setAndContinue(IDLE);
                } else if (state.isCurrentAnimation(IDLE)) {
                    return state.setAndContinue(STOPPING);
                } else if (!state.isCurrentAnimation(STOPPING)){
                    return state.setAndContinue(RawAnimation.begin());
                }
            }
            return state.setAndContinue(RawAnimation.begin());
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    
    @Override
    public double getTick(Object blockEntity) {
        return RenderUtils.getCurrentTick();
    }
    
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (!world.isClientSide){
            return;
        }
        boolean small_sound = state.getValue(SterlingEngineBlock.SMALL_SOUND);
        if (state.getValue(SterlingEngineBlock.IS_WORKING)){
            if (!hasStart){
                ticker = 30;
                if (small_sound){
                    world.playLocalSound(pos, ModSounds.BLOCK_STERLING_ENGINE_START.get(), SoundSource.BLOCKS, 0.15f, 1.0f, false);
                } else {
                    world.playLocalSound(pos, ModSounds.BLOCK_STERLING_ENGINE_START.get(), SoundSource.BLOCKS, 2.3f, 1.0f, false);
                }
            }
            hasStart = true;
            if (ticker == 0){
                if (small_sound){
                    if (world.getGameTime() % 3L == 0){
                        world.playLocalSound(pos, ModSounds.BLOCK_STERLING_ENGINE.get(), SoundSource.BLOCKS, 0.15f, 1.0f, false);
                    }
                } else {
                    if (world.getGameTime() % 3L == 0){
                        world.playLocalSound(pos, ModSounds.BLOCK_STERLING_ENGINE.get(), SoundSource.BLOCKS, 2.3f, 1.0f, false);
                    }
                }
            } else {
                ticker--;
            }
        } else {
            if (hasStart){
                if (small_sound){
                    world.playLocalSound(pos, ModSounds.BLOCK_STERLING_ENGINE_STOP.get(), SoundSource.BLOCKS, 0.15f, 1.0f, false);
                } else {
                    world.playLocalSound(pos, ModSounds.BLOCK_STERLING_ENGINE_STOP.get(), SoundSource.BLOCKS, 2.3f, 1.0f, false);
                }
            }
            hasStart = false;
        }
        if (world.getBlockState(pos).getBlock() instanceof SterlingEngineBlock){
            if (world.getBlockState(pos.below()).getBlock() instanceof net.minecraft.world.level.block.FurnaceBlock){
                world.setBlock(pos, state.setValue(SterlingEngineBlock.IS_WORKING,
                        world.getBlockState(pos.below()).getValue(net.minecraft.world.level.block.FurnaceBlock.LIT)), 3);
            } else if (world.getBlockState(pos.below()).getBlock() instanceof net.minecraft.world.level.block.BlastFurnaceBlock){
                world.setBlock(pos, state.setValue(SterlingEngineBlock.IS_WORKING,
                        world.getBlockState(pos.below()).getValue(net.minecraft.world.level.block.BlastFurnaceBlock.LIT)), 3);
            } else if (world.getBlockState(pos.below()).getBlock() instanceof net.minecraft.world.level.block.SmokerBlock){
                world.setBlock(pos, state.setValue(SterlingEngineBlock.IS_WORKING,
                        world.getBlockState(pos.below()).getValue(net.minecraft.world.level.block.SmokerBlock.LIT)), 3);
            } else if (world.getBlockState(pos.below()).getBlock() instanceof OvenBlock){
                world.setBlock(pos, state.setValue(SterlingEngineBlock.IS_WORKING,
                        world.getBlockState(pos.below()).getValue(OvenBlock.OVEN_BURNING)), 3);
            } else if (world.getBlockState(pos.below()).getBlock() instanceof AdvanceFurnaceBlock){
                world.setBlock(pos, state.setValue(SterlingEngineBlock.IS_WORKING,
                        world.getBlockState(pos.below()).getValue(AdvanceFurnaceBlock.BURNING)), 3);
            }
        }
    }
}
