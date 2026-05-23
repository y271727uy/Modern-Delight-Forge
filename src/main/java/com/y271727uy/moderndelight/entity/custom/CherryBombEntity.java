package com.y271727uy.moderndelight.entity.custom;

import com.y271727uy.moderndelight.entity.ModEntities;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.sound.ModSounds;
import com.y271727uy.moderndelight.util.registry_util.ModDamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

public class CherryBombEntity extends ThrowableItemProjectile {
    public CherryBombEntity(net.minecraft.world.entity.EntityType<? extends CherryBombEntity> entityType, Level level) {
        super(entityType, level);
    }

    public CherryBombEntity(Level level, LivingEntity entity) {
        super(ModEntities.CHERRY_BOMB.get(), entity, level);
    }

    public CherryBombEntity(Level level, double x, double y, double z) {
        super(ModEntities.CHERRY_BOMB.get(), x, y, z, level);
    }
    
    @Override
    protected Item getDefaultItem() {
        return ModItems.CHERRY_BOMB.get();
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
        if (!this.level().isClientSide) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.2f, Level.ExplosionInteraction.MOB);
            this.playSound(ModSounds.ENTITY_CHERRY_BOMB_EXPLOSION.get(), 4.0f, (this.random.nextFloat() - this.random.nextFloat()) * 2.f + 1.f);
            this.discard();
        }
        super.onHit(hitResult);
    }
    
    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        entity.hurt(ModDamageTypes.of(entity.level(), ModDamageTypes.TURNED_TO_ASHES), 10.5f);
        this.playSound(ModSounds.ENTITY_CHERRY_BOMB_EXPLOSION.get(), 4.0f, (this.random.nextFloat() - this.random.nextFloat()) * 2.f + 1.f);
    }
}
