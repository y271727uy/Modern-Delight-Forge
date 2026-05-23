package com.y271727uy.moderndelight.entity.custom;

import com.y271727uy.moderndelight.effects.ModEffectsAndPotions;
import com.y271727uy.moderndelight.entity.ModEntities;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.sound.ModSounds;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class ButterEntity extends ThrowableItemProjectile {
    public ButterEntity(net.minecraft.world.entity.EntityType<? extends ButterEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ButterEntity(Level level, LivingEntity entity) {
        super(ModEntities.BUTTER.get(), entity, level);
    }

    public ButterEntity(Level level, double x, double y, double z) {
        super(ModEntities.BUTTER.get(), x, y, z, level);
    }
    
    @Override
    protected Item getDefaultItem() {
        return ModItems.BUTTER.get();
    }
    
    @Override
    public void handleEntityEvent(byte status) {
        ItemStack entityStack = new ItemStack(this.getDefaultItem());
        if (status == 3) {
            for (int i = 0; i < 12; ++i) {
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, entityStack),
                        this.getX(), this.getY(), this.getZ(),
                        ((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F,
                        ((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F + 0.1F,
                        ((double) this.random.nextFloat() * 2.0D - 1.0D) * 0.1F);
            }
        }
    }
    
    @Override
    protected void onHit(HitResult hitResult) {
        Entity entity = hitResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) hitResult).getEntity() : null;
        if (!this.level().isClientSide) {
            AABB box = this.getBoundingBox().inflate(3.0, 2.0, 3.0);
            List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, box);
            Entity entity2 = this.getOwner();
            MobEffectInstance sticky = new MobEffectInstance(ModEffectsAndPotions.STICKY.get(), 80, 0);
            MobEffectInstance miningFatigue = new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 80, 9);
            MobEffectInstance weakness = new MobEffectInstance(MobEffects.WEAKNESS, 80, 9);
            
            for (LivingEntity livingEntity : list) {
                double d;
                if (!livingEntity.isAffectedByPotions() || !((d = this.distanceToSqr(livingEntity)) < 16.0))
                    continue;
                double e = livingEntity == entity ? 1.0 : 1.0 - Math.sqrt(d) / 4.0;
                
                int i2 = (int) (e * sticky.getDuration() + 0.5);
                int i3 = (int) (e * miningFatigue.getDuration() + 0.5);
                int i4 = (int) (e * weakness.getDuration() + 0.5);
                
                MobEffectInstance Sticky = new MobEffectInstance(ModEffectsAndPotions.STICKY.get(), i2, sticky.getAmplifier(), sticky.isAmbient(), true);
                MobEffectInstance MiningFatigue = new MobEffectInstance(MobEffects.DIG_SLOWDOWN, i3, miningFatigue.getAmplifier(), miningFatigue.isAmbient(), false);
                MobEffectInstance Weakness = new MobEffectInstance(MobEffects.WEAKNESS, i4, weakness.getAmplifier(), weakness.isAmbient(), false);
                
                if (Sticky.getDuration() < 20 ||
                        MiningFatigue.getDuration() < 20 ||
                        Weakness.getDuration() < 20) continue;
                
                livingEntity.addEffect(Sticky, entity2);
                livingEntity.addEffect(MiningFatigue, entity2);
                livingEntity.addEffect(Weakness, entity2);
            }
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
        super.onHit(hitResult);
    }
    
    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), 0);
        this.playSound(ModSounds.ENTITY_BUTTER_HIT.get(), 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 2.f + 1.f);
    }
    
    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.playSound(ModSounds.ENTITY_BUTTER_HIT.get(), 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 2.f + 1.f);
            this.discard();
        }
    }
}
