package com.y271727uy.moderndelight.block.kitchenware.ice_cream_maker;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.util.MiscUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import javax.annotation.Nullable;

public class IceCreamMakerBlock extends BaseEntityBlock {
    public IceCreamMakerBlock() {
        super(Properties.copy(Blocks.IRON_BARS));
        this.registerDefaultState(this.stateDefinition.any().setValue(START,false));
    }
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty START = BooleanProperty.create("start");
    private static final VoxelShape SHAPE = Block.box(2,0,2,14,16,14);
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
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
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof IceCreamMakerBlockEntity entity){
                for (int i = 0; i < entity.getContainerSize(); i++){
                    Block.popResource(world, pos, entity.getItem(i));
                }
                world.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }
    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING,START);
    }
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING,rotation.rotate(state.getValue(FACING)));
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IceCreamMakerBlockEntity(pos,state);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide){
            return InteractionResult.SUCCESS;
        }
        if (world.getBlockEntity(pos) instanceof IceCreamMakerBlockEntity entity){
            if (player.isShiftKeyDown()){
                if (player instanceof ServerPlayer serverPlayer){
                    NetworkHooks.openScreen(serverPlayer, entity, pos);
                }
            } else if (hit.getDirection().equals(state.getValue(FACING))) {
                entity.tryStart(state, world, player);
            } else if (MiscUtil.isPlayerHoldingCrowbar(player)){
                Direction dir = state.getValue(FACING);
                switch (dir){
                    case EAST -> world.setBlock(pos,state.setValue(FACING,Direction.SOUTH), 3);
                    case SOUTH -> world.setBlock(pos,state.setValue(FACING,Direction.WEST), 3);
                    case WEST -> world.setBlock(pos,state.setValue(FACING,Direction.NORTH), 3);
                    case NORTH -> world.setBlock(pos,state.setValue(FACING,Direction.EAST), 3);
                }
                world.playSound(null,pos, SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundSource.BLOCKS,1.0f,world.random.nextFloat()+0.8f);
            } else if (player instanceof ServerPlayer serverPlayer){
                NetworkHooks.openScreen(serverPlayer, entity, pos);
            }
        }
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.ICE_CREAM_MAKER_BLOCK_ENTITY.get(),
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}



