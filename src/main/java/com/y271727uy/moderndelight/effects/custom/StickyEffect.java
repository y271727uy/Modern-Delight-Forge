package com.y271727uy.moderndelight.effects.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class StickyEffect extends MobEffect {
    public StickyEffect() {
        super(MobEffectCategory.HARMFUL, 0xB4C92A);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.onGround()) {
            Vec3 velocity = entity.getDeltaMovement();
            entity.setDeltaMovement(velocity.x * 0.001D, Math.min(velocity.y, 0.0D), velocity.z * 0.001D);
            entity.setJumping(false);
        }

        if (entity.level() instanceof ServerLevel serverLevel && entity.onGround()) {
            spawnParticles(entity, serverLevel);
        }
    }

    private static void spawnParticles(LivingEntity entity, ServerLevel serverLevel) {
        serverLevel.sendParticles(
                ParticleTypes.FALLING_HONEY,
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                1,
                0.0, 0.0, 0.0,
                0.0
        );
    }
}
