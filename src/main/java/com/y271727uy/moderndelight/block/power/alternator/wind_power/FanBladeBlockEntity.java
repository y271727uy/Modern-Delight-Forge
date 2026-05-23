package com.y271727uy.moderndelight.block.power.alternator.wind_power;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.Objects;

public class FanBladeBlockEntity extends BlockEntity implements GeoBlockEntity {
    public FanBladeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FAN_BLADE_BLOCK_ENTITY.get(), pos, state);
    }
    
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation IDLE_FAST = RawAnimation.begin().thenPlay("start").thenLoop("idle_fast");

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> {
            if (state.getAnimatable().getBlockPos().getY() >= 200) {
                state.setControllerSpeed((float) state.getAnimatable().getBlockPos().getY() / 100);
                Level world = state.getAnimatable().getLevel();
                if (world != null && world.isThundering()){
                    state.setControllerSpeed(5);
                } else if (world != null && world.isRaining()) {
                    state.setControllerSpeed(3);
                }
                return state.setAndContinue(IDLE_FAST);
            } else {
                Level world = state.getAnimatable().getLevel();
                if (world != null && world.isThundering()){
                    state.setControllerSpeed(5);
                } else if (world != null && world.isRaining()) {
                    state.setControllerSpeed(3);
                }
                return state.setAndContinue(IDLE);
            }
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
