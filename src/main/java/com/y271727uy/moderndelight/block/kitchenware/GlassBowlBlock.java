package com.y271727uy.moderndelight.block.kitchenware;

import com.y271727uy.moderndelight.item.food.PackagedItem;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.GameRules;
import javax.annotation.Nullable;

import java.util.List;

public class GlassBowlBlock extends BaseEntityBlock implements SimpleWaterloggedBlock{
    public static final BooleanProperty HAS_ITEM = BooleanProperty.create("has_item");
    public static final BooleanProperty HAS_WATER = BooleanProperty.create("has_water");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public GlassBowlBlock(BlockBehaviour.Properties settings) {
        super(settings);
        registerDefaultState(this.getStateDefinition().any()
                .setValue(HAS_ITEM, false).setValue(WATERLOGGED,false).setValue(HAS_WATER, false));
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        if(Screen.hasShiftDown()){
            tooltip.add(TextUtil.getShiftText(true));
            tooltip.add(Component.literal(" "));
            tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.GLASS_BOWL)));
        } else {
            tooltip.add(TextUtil.getShiftText(false));
        }
        super.appendHoverText(stack, world, tooltip, options);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_ITEM, WATERLOGGED, HAS_WATER);
    }
    private static final VoxelShape SHAPED = Block.box(3,0,3,13,5,13);
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPED;
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPED;
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GlassBowlBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof GlassBowlBlockEntity container) {
            updateBlock(state,world,pos);
            container.use(player, state, world);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()){
            if (world.getBlockEntity(pos) instanceof GlassBowlBlockEntity blockEntity){
                Containers.dropContents(world, pos, blockEntity);
                if (!(blockEntity.getOutputStack().getItem() instanceof PackagedItem)) {
                    Block.popResource(world, pos, blockEntity.getOutputStack());
                }
                world.updateNeighbourForOutputSignal(pos, this);
            }
        } else {
            boolean wasWaterlogged = state.getValue(WATERLOGGED);
            boolean isWaterlogged = newState.getValue(WATERLOGGED);
            if (wasWaterlogged != isWaterlogged) {
                updateBlock(newState, world, pos);
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        boolean isInWater = fluidState.getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(WATERLOGGED, isInWater).setValue(HAS_WATER, isInWater);
    }
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos,
                                                BlockPos posFrom) {
        if (Boolean.TRUE.equals(state.getValue(WATERLOGGED))) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        updateBlock(state, (Level) world,pos);
        return direction == Direction.DOWN && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState()
                : state;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        updateBlock(state,world,pos);
        super.randomTick(state, world, pos, random);
    }

    private static void updateBlock(BlockState state, Level world, BlockPos pos) {
        if (world.isClientSide){
            return;
        }
        if (world.getBlockEntity(pos) instanceof GlassBowlBlockEntity blockEntity){
            if (state.getValue(WATERLOGGED)){
                world.setBlock(pos, state.setValue(HAS_WATER,true), 3);
                if (!blockEntity.getItem(0).isEmpty()){
                    Block.popResource(world, pos, blockEntity.getItem(0));
                    blockEntity.setItem(0, ItemStack.EMPTY);
                    blockEntity.playSound(SoundEvents.ITEM_PICKUP, 0.8F);
                }
                if (!blockEntity.getOutputStack().isEmpty()){
                    if (!(blockEntity.getOutputStack().getItem() instanceof PackagedItem)) {
                        Block.popResource(world, pos, blockEntity.getOutputStack());
                        blockEntity.playSound(SoundEvents.ITEM_PICKUP, 0.8F);
                    }
                    blockEntity.setOutputStack(ItemStack.EMPTY);
                    world.setBlock(pos, state.setValue(HAS_ITEM,false), 3);
                }
            }
            if (state.getValue(HAS_WATER)){
                if (!blockEntity.getOutputStack().isEmpty()){
                    if (!(blockEntity.getOutputStack().getItem() instanceof PackagedItem)) {
                        Block.popResource(world, pos, blockEntity.getOutputStack());
                        blockEntity.playSound(SoundEvents.ITEM_PICKUP, 0.8F);
                    }
                    blockEntity.setOutputStack(ItemStack.EMPTY);
                    world.setBlock(pos, state.setValue(HAS_ITEM,false), 3);
                }
            }
            blockEntity.setChanged();
        }
    }

    public static void destroyGlassBowl(Level world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.4f, 1.6f + world.random.nextFloat() * 0.2f);
        world.destroyBlock(pos, true);
    }
    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!world.isClientSide && world.random.nextFloat() < fallDistance - 0.5f && entity instanceof LivingEntity && (entity instanceof Player || world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) && entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight() > 0.512f) {
            destroyGlassBowl(world, pos);
       }
        super.fallOn(world, state, pos, entity, fallDistance);
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).isFaceSturdy(world, pos.below(), Direction.UP);
    }
}
