package com.y271727uy.moderndelight.block.kitchenware.decor;

import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.GameRules;
import javax.annotation.Nullable;

import java.util.List;

public class GlassCupBlock extends Block {
    public static final IntegerProperty CUPS = IntegerProperty.create("cups", 1, 4);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape ONE_CUP_SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);
    protected static final VoxelShape TWO_CUPS_SHAPE = Block.box(1.0, 0.0, 5.0, 15.0, 10.0, 11.0);
    protected static final VoxelShape THREE_CUPS_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 10.0, 15.0);
    protected static final VoxelShape FOUR_CUPS_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 10.0, 15.0);

    public GlassCupBlock() {
        super(Properties.copy(ModBlocks.GLASS_BOWL.get()));
        registerDefaultState(stateDefinition.any().setValue(CUPS, 1).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CUPS, WATERLOGGED);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            int cups = state.getValue(CUPS);
            Block.popResource(level, pos, new ItemStack(ModBlocks.GLASS_CUP.get(), cups));
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
        tooltip.add(Component.translatable(TextUtil.CAN_PLACE).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if (state.is(this)) {
            return state.setValue(CUPS, Math.min(4, state.getValue(CUPS) + 1));
        }
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return context.getItemInHand().is(ModBlocks.GLASS_CUP.get().asItem()) && state.getValue(CUPS) < 4 || super.canBeReplaced(state, context);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!level.isClientSide && level.random.nextFloat() < fallDistance - 0.5f && entity instanceof LivingEntity && (entity instanceof Player || level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) && entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight() > 0.512f) {
            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.GLASS_BREAK, net.minecraft.sounds.SoundSource.BLOCKS, 1.4f, 1.6f + level.random.nextFloat() * 0.2f);
            level.destroyBlock(pos, true);
        }
        super.fallOn(level, state, pos, entity, fallDistance);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(CUPS)) {
            case 2 -> TWO_CUPS_SHAPE;
            case 3 -> THREE_CUPS_SHAPE;
            case 4 -> FOUR_CUPS_SHAPE;
            default -> ONE_CUP_SHAPE;
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getCollisionShape(state, level, pos, context);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return direction == Direction.DOWN && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }
}
