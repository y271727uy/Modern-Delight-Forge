package com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.world.Containers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkHooks;
import javax.annotation.Nullable;

import java.util.List;

public class WoodenBasinBlock extends BaseEntityBlock implements SimpleWaterloggedBlock{
    private static final String LAST_PRESS_TICK_KEY = "moderndelight.last_wooden_basin_press_tick";

    public WoodenBasinBlock() {
        super(Properties.copy(Blocks.BARREL).noOcclusion());
        registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED,false));
    }
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPED = Block.box(0,0,0,16,6,16);
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPED;
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPED;
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }
    
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos,
                                                BlockPos posFrom) {
        if (Boolean.TRUE.equals(state.getValue(WATERLOGGED))) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return direction == Direction.DOWN && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, newState, world, pos, posFrom);
    }
    
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WoodenBasinBlockEntity){
                Containers.dropContents(world, pos, (WoodenBasinBlockEntity)blockEntity);
                world.updateNeighbourForOutputSignal(pos,this);
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }
    
    @Override
    public FluidState getFluidState(BlockState state) {
        return Boolean.TRUE.equals(state.getValue(WATERLOGGED)) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
    
    @Nullable
    @Override
    public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WoodenBasinBlockEntity(pos, state);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide){
            var screenHandlerFactory = ((WoodenBasinBlockEntity) world.getBlockEntity(pos));
            if (screenHandlerFactory != null && player instanceof ServerPlayer serverPlayer){
                NetworkHooks.openScreen(serverPlayer, screenHandlerFactory, buf -> buf.writeBlockPos(pos));
            }
        }
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!world.isClientSide && world.random.nextFloat() < fallDistance - 0.5f && entity instanceof LivingEntity && (entity instanceof Player || world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) && entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight() > 0.512f) {
            if (world.getBlockEntity(pos) instanceof WoodenBasinBlockEntity blockEntity) {
                blockEntity.onLandedUpon(world, (LivingEntity) entity);
            }
        }
        super.fallOn(world, state, pos, entity, fallDistance);
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClientSide && canPress(world, entity)) {
            long gameTime = world.getGameTime();
            long lastPressTick = entity.getPersistentData().getLong(LAST_PRESS_TICK_KEY);
            if (gameTime - lastPressTick >= 20 && world.getBlockEntity(pos) instanceof WoodenBasinBlockEntity blockEntity
                    && blockEntity.onLandedUpon(world, (LivingEntity) entity)) {
                entity.getPersistentData().putLong(LAST_PRESS_TICK_KEY, gameTime);
            }
        }
        super.stepOn(world, pos, state, entity);
    }

    private boolean canPress(Level world, Entity entity) {
        return entity instanceof LivingEntity
                && (entity instanceof Player || world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING))
                && entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight() > 0.512f;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, net.minecraft.world.item.TooltipFlag options) {
        if(net.minecraft.client.gui.screens.Screen.hasShiftDown()){
            tooltip.add(TextUtil.getShiftText(true));
            tooltip.add(Component.literal(" "));
            tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.WOODEN_BASIN)));
        } else {
            tooltip.add(TextUtil.getShiftText(false));
        }
        super.appendHoverText(stack, world, tooltip, options);
    }
    
    @Nullable
    @Override
    public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level world, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.WOODEN_BASIN_BLOCK_ENTITY.get(),
                (world1, pos, state1, blockEntity) -> ((WoodenBasinBlockEntity)blockEntity).tick(world1, pos, state1));
    }
}
