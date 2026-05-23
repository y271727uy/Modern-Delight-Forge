package com.y271727uy.moderndelight.block.food;

import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.util.block_util.Drinkable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Arrays;
import java.util.List;

public class GlassCupOfTeaBlock extends Block implements Drinkable {
    private static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);
    public List<net.minecraft.world.effect.MobEffectInstance> effects = null;
    public int hunger;
    public float saturationModifier;

    public GlassCupOfTeaBlock(int hunger, float saturationModifier, net.minecraft.world.effect.MobEffectInstance... effects) {
        super(BlockBehaviour.Properties.copy(Blocks.REPEATER).sound(SoundType.GLASS));
        this.hunger = hunger;
        this.saturationModifier = saturationModifier;
        this.effects = Arrays.asList(effects.clone());
    }

    public GlassCupOfTeaBlock(int hunger, float saturationModifier) {
        super(BlockBehaviour.Properties.copy(Blocks.REPEATER).sound(SoundType.GLASS));
        this.hunger = hunger;
        this.saturationModifier = saturationModifier;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!level.isClientSide && level.random.nextFloat() < fallDistance - 0.5f && entity instanceof LivingEntity && entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight() > 0.512f) {
            level.destroyBlock(pos, false);
        }
        super.fallOn(level, state, pos, entity, fallDistance);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            if (player.isCrouching()) {
                if (player.getMainHandItem().isEmpty()) {
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(this));
                } else {
                    player.getInventory().add(new ItemStack(this));
                }
                level.removeBlock(pos, false);
                level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            } else {
                drink(level, player);
                level.setBlock(pos, ModBlocks.GLASS_CUP.get().defaultBlockState(), 3);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public int getHunger() {
        return hunger;
    }

    @Override
    public float getSaturationModifier() {
        return saturationModifier;
    }

    @Override
    public List<net.minecraft.world.effect.MobEffectInstance> getEffects() {
        return effects;
    }

    protected boolean canPlaceOnTop(BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.isFaceSturdy(world, pos, Direction.UP);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockPos = pos.below();
        return this.canPlaceOnTop(world.getBlockState(blockPos), world, blockPos);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }
}
