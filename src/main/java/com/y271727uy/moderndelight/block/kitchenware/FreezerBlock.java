package com.y271727uy.moderndelight.block.kitchenware;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.sound.ModSounds;
import com.y271727uy.moderndelight.util.MiscUtil;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.Containers;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.network.NetworkHooks;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class FreezerBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty IS_OPEN = BooleanProperty.create("is_open");
    private static final VoxelShape TYPE_WEST = Block.box(2,0,0,15,16,16);
    private static final VoxelShape TYPE_EAST = Block.box(1,0,0,14,16,16);
    private static final VoxelShape TYPE_SOUTH = Block.box(0,0,1,16,16,14);
    private static final VoxelShape TYPE_NORTH = Block.box(0,0,2,16,16,15);
    public static final String FAIL_TO_OPEN = "moderndelight.freezer_message.fail_to_open";

    public FreezerBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(IS_OPEN, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        if(Screen.hasShiftDown()){
            tooltip.add(TextUtil.getShiftText(true));
            tooltip.add(TextUtil.getAltText(false));
            tooltip.add(Component.literal(" "));
            tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.FREEZER)));
        } else if (Screen.hasAltDown()) {
            tooltip.add(TextUtil.getShiftText(false));
            tooltip.add(TextUtil.getAltText(true));
            tooltip.add(Component.literal(" "));
            tooltip.add(TextUtil.getACCom("15"));
        } else {
            tooltip.add(TextUtil.getShiftText(false));
            tooltip.add(TextUtil.getAltText(false));
        }
        super.appendHoverText(stack, world, tooltip, options);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, IS_OPEN);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        if (direction == Direction.WEST){
            return TYPE_WEST;
        } else if (direction == Direction.EAST) {
            return TYPE_EAST;
        } else if (direction == Direction.NORTH){
            return TYPE_NORTH;
        } else {
            return TYPE_SOUTH;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        if (direction == Direction.WEST){
            return TYPE_WEST;
        } else if (direction == Direction.EAST) {
            return TYPE_EAST;
        } else if (direction == Direction.NORTH){
            return TYPE_NORTH;
        } else {
            return TYPE_SOUTH;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FreezerBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof FreezerBlockEntity){
                if (((FreezerBlockEntity) blockEntity).getExperience() != 0){
                    ExperienceOrb xp =
                            new ExperienceOrb(world, pos.getX(), pos.getY(), pos.getZ(),
                                    ((FreezerBlockEntity) blockEntity).getExperience());
                    world.addFreshEntity(xp);
                }
                Containers.dropContents(world, pos, (FreezerBlockEntity)blockEntity);
                world.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        world.setBlock(pos, state.setValue(IS_OPEN, false), 3);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (!world.getBlockState(getSideBlock(state, pos)).isAir() && state.getValue(IS_OPEN)){
            world.scheduleTick(pos, this, 1);
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide){
            if (MiscUtil.isPlayerHoldingCrowbar(player)){
                Direction dir = state.getValue(FACING);
                switch (dir){
                    case EAST -> world.setBlock(pos, state.setValue(FACING, Direction.SOUTH), 3);
                    case SOUTH -> world.setBlock(pos, state.setValue(FACING, Direction.WEST), 3);
                    case WEST -> world.setBlock(pos, state.setValue(FACING, Direction.NORTH), 3);
                    case NORTH -> world.setBlock(pos, state.setValue(FACING, Direction.EAST), 3);
                }
                world.playSound(null, pos, SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundSource.BLOCKS, 1.0f, world.random.nextFloat() + 0.8f);
            } else {
                Direction direction = state.getValue(FACING);
                BlockPos facingBlock = pos;
                BlockPos sideBlock = pos;
                switch (direction){
                    case EAST: {
                        facingBlock = pos.east(1);
                        sideBlock = pos.east(1).south(1);
                        break;
                    }
                    case SOUTH: {
                        facingBlock = pos.south(1);
                        sideBlock = pos.south(1).west(1);
                        break;
                    }
                    case WEST: {
                        facingBlock = pos.west(1);
                        sideBlock = pos.west(1).north(1);
                        break;
                    }
                    case NORTH: {
                        facingBlock = pos.north(1);
                        sideBlock = pos.north(1).east(1);
                        break;
                    }
                }
                if (!world.getBlockState(getSideBlock(state, pos)).isAir()){
                    world.setBlock(pos, state.setValue(IS_OPEN, false), 3);
                    player.displayClientMessage(Component.translatable(FAIL_TO_OPEN), true);
                }
                if (state.getValue(IS_OPEN)){
                    if (world.getBlockState(facingBlock).isAir()){
                        if (player.isShiftKeyDown()){
                            if (world.getBlockState(sideBlock).isAir()){
                                world.setBlock(pos, state.setValue(IS_OPEN, false), 3);
                                world.playSound(null, pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f, ModSounds.BLOCK_FREEZER_CLOSE.get(), SoundSource.BLOCKS, 1.7f, world.random.nextFloat() + 0.8f);
                            } else {
                                player.displayClientMessage(Component.translatable(FAIL_TO_OPEN), true);
                            }
                        } else {
                            MenuProvider screenHandlerFactory = (FreezerBlockEntity) world.getBlockEntity(pos);
                            if (screenHandlerFactory != null && player instanceof ServerPlayer serverPlayer){
                                NetworkHooks.openScreen(serverPlayer, screenHandlerFactory, pos);
                            }
                        }
                    } else {
                        player.displayClientMessage(Component.translatable(FAIL_TO_OPEN), true);
                    }
                } else {
                    if (world.getBlockState(facingBlock).isAir() && world.getBlockState(sideBlock).isAir()){
                        world.setBlock(pos, state.setValue(IS_OPEN, true), 3);
                        world.playSound(null, pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f, ModSounds.BLOCK_FREEZER_OPEN.get(), SoundSource.BLOCKS, 1.0f, world.random.nextFloat() + 0.8f);
                    } else {
                        player.displayClientMessage(Component.translatable(FAIL_TO_OPEN), true);
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    private static BlockPos getSideBlock(BlockState state, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos sideBlock = pos;
        switch (direction){
            case EAST: {
                sideBlock = pos.east(1).south(1);
                break;
            }
            case SOUTH: {
                sideBlock = pos.south(1).west(1);
                break;
            }
            case WEST: {
                sideBlock = pos.west(1).north(1);
                break;
            }
            case NORTH: {
                sideBlock = pos.north(1).east(1);
                break;
            }
        }
        return sideBlock;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.FREEZER_ENTITY.get(), FreezerBlockEntity::tick);
    }
}


