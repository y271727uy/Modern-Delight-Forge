package com.y271727uy.moderndelight.block.kitchenware;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.util.MiscUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class AdvanceFurnaceBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty BURNING = BooleanProperty.create("burning");

    public AdvanceFurnaceBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public AdvanceFurnaceBlock() {
        this(BlockBehaviour.Properties.copy(Blocks.BRICKS).lightLevel(state -> state.getValue(BURNING) ? 15 : 0));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(BURNING, false);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BURNING);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AdvanceFurnaceBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AdvanceFurnaceBlockEntity furnaceBE){
                Containers.dropContents(world ,pos, furnaceBE);
                if (furnaceBE.getExperience() != 0){
                    ExperienceOrb xp =
                            new ExperienceOrb(world,pos.getX(),pos.getY(),pos.getZ(),
                                    furnaceBE.getExperience());
                    world.addFreshEntity(xp);
                    world.updateNeighbourForOutputSignal(pos,this);
                }
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(BURNING)){
            double d = (double)pos.getX() + 0.5;
            double e = pos.getY();
            double f = (double)pos.getZ() + 0.5;
            if (random.nextDouble() < 0.1) {
                world.playLocalSound(d, e, f, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = state.getValue(FACING);
             Direction.Axis axis = direction.getAxis();
            double h = random.nextDouble() * 0.6 - 0.3;
            double i = axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52 : h;
            double j = random.nextDouble() * 6.0 / 16.0;
            double k = axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52 : h;
            world.addParticle(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0, 0.0, 0.0);
            world.addParticle(ParticleTypes.FLAME, d + i, e + j, f + k, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide){
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof AdvanceFurnaceBlockEntity furnaceBE){
                if (isBakingTrayBlock(player)){
                    furnaceBE.useFurnace(world, state);
                    return InteractionResult.SUCCESS;
                } else if (MiscUtil.isPlayerHoldingCrowbar(player)){
                    Direction dir = state.getValue(FACING);
                    world.setBlock(pos, state.setValue(FACING, dir.getClockWise()), 3);
                    world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundSource.BLOCKS, 1.0f, world.random.nextFloat() + 0.8f, false);
                    return InteractionResult.SUCCESS;
                } else {
                    NetworkHooks.openScreen((ServerPlayer) player, furnaceBE, pos);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    private boolean isBakingTrayBlock(Player player) {
        if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ModBlocks.BAKING_TRAY.get().asItem())){
            player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.ADVANCE_FURNACE_BLOCK_ENTITY.get(),
                AdvanceFurnaceBlockEntity::tick);
    }

    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
        return p_152134_ == p_152133_ ? (BlockEntityTicker<A>)p_152135_ : null;
    }
}


