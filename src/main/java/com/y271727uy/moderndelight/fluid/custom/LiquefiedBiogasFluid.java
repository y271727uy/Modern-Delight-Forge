package com.y271727uy.moderndelight.fluid.custom;

import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.biogas.GasCanisterBlockEntity;
import com.y271727uy.moderndelight.fluid.ModAbstractFluid;
import com.y271727uy.moderndelight.fluid.ModFluid;
import com.y271727uy.moderndelight.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public abstract class LiquefiedBiogasFluid extends ModAbstractFluid {
    public Fluid getFlowing() {
        return ModFluid.FLOWING_LIQUEFIED_BIOGAS.get();
    }

    public Fluid getStill() {
        return ModFluid.STILL_LIQUEFIED_BIOGAS.get();
    }

    public Item getBucketItem() {
        return ModItems.LIQUEFIED_BIOGAS_BUCKET.get();
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return 3;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return ModBlocks.LIQUEFIED_BIOGAS_FLUID_BLOCK.get().defaultBlockState().setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.LEVEL, getLegacyLevel(state));
    }

    @Override
    public Fluid getSource() {
        return ModFluid.STILL_LIQUEFIED_BIOGAS.get();
    }

    @Override
    protected boolean isRandomlyTicking() {
        return true;
    }

    @Override
    protected float getExplosionResistance() {
        return 2.0F;
    }

    @Override
    public void tick(Level level, BlockPos pos, FluidState state) {
        AABB box = new AABB(pos).inflate(1.2);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, box);
        for (LivingEntity entity : entities) {
            if (entity != null) {
                entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 40 * 20, 0));
                entity.addEffect(new MobEffectInstance(MobEffects.POISON, 30 * 20, 0));
            }
        }
        if (level.dimensionType().ultraWarm()) {
            createExplode(level, pos);
        }
        super.tick(level, pos, state);
    }

    @Override
    public void randomTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
        if (!level.isClientSide) {
            for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.values()) {
                BlockPos offsetPos = pos.relative(dir);
                if (GasCanisterBlockEntity.isDangerBlock(level.getBlockState(offsetPos).getBlock())) {
                    createExplode(level, offsetPos);
                }
            }
            this.tick(level, pos, state);
        }
        super.randomTick(level, pos, state, random);
    }

    private static void createExplode(Level level, BlockPos pos) {
        level.explode(null, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 4.0f, Level.ExplosionInteraction.BLOCK);
        level.removeBlock(pos, false);
    }

    @Override
    public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
        if (random.nextDouble() < 0.5 && level.getBlockState(pos.above()).isAir()) {
            double d = (double) pos.getX() + level.random.nextDouble();
            double e = (double) pos.getY() + level.random.nextDouble() * 0.5;
            double f = (double) pos.getZ() + level.random.nextDouble();
            level.addParticle(ParticleTypes.LARGE_SMOKE, d, e, f, 0.0, 0.3 * random.nextDouble(), 0.0);
            level.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.5 * random.nextDouble(), 0.0);
        }
        super.animateTick(level, pos, state, random);
    }

    public static class Flowing extends LiquefiedBiogasFluid {
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return fluidState.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return false;
        }

        @Override
        public Item getBucket() {
            return ModItems.LIQUEFIED_BIOGAS_BUCKET.get();
        }
    }

    public static class Still extends LiquefiedBiogasFluid {
        @Override
        public int getAmount(FluidState fluidState) {
            return 3;
        }

        @Override
        public void randomTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
            if (!level.isClientSide) {
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 0.5f + random.nextFloat());
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
            super.randomTick(level, pos, state, random);
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return true;
        }

        @Override
        public Item getBucket() {
            return ModItems.LIQUEFIED_BIOGAS_BUCKET.get();
        }
    }
}
