package com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.util.MiscUtil;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.world.Containers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
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

import java.util.List;

public class DeepFryerBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_OIL = BooleanProperty.create("has_oil");
    public static final BooleanProperty RUNNING = BooleanProperty.create("running");
    
    public DeepFryerBlock() {
        super(Properties.copy(Blocks.BRICKS).noOcclusion());
        registerDefaultState(this.getStateDefinition().any()
                .setValue(HAS_OIL, false).setValue(RUNNING, false));
    }

    private static final VoxelShape TYPE_WEST = Block.box(1,1,0,15,11,16);
    private static final VoxelShape TYPE_EAST = Block.box(1,1,0,15,11,16);
    private static final VoxelShape TYPE_SOUTH = Block.box(0,1,1,16,11,15);
    private static final VoxelShape TYPE_NORTH = Block.box(0,1,1,16,11,15);
    
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
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }
    
    @Override
    public BlockState mirror(BlockState state, net.minecraft.world.level.block.Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
    
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DeepFryerBlockEntity){
                Containers.dropContents(world, pos, (DeepFryerBlockEntity)blockEntity);
                world.updateNeighbourForOutputSignal(pos,this);
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }
    
    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, net.minecraft.util.RandomSource random) {
        if (state.getValue(DeepFryerBlock.HAS_OIL) && state.getValue(DeepFryerBlock.RUNNING)){
            if (world.random.nextFloat() < 0.1f){
                double d = (double)pos.getX() + world.random.nextDouble();
                double e = (double)pos.getY() +  world.random.nextDouble() * 0.5 + 0.8;
                double f = (double)pos.getZ() +  world.random.nextDouble();
                world.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, d, e, f, 0.0, 0.0, 0.0);
            }
        }
        super.animateTick(state, world, pos, random);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING,HAS_OIL,RUNNING);
    }
    
    @Override
    public BlockState rotate(BlockState state, net.minecraft.world.level.block.Rotation rotation) {
        return state.setValue(FACING,rotation.rotate(state.getValue(FACING)));
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Nullable
    @Override
    public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DeepFryerBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof DeepFryerBlockEntity entity){
            if (MiscUtil.isPlayerHoldingCrowbar(player)){
                Direction dir = state.getValue(FACING);
                switch (dir){
                    case EAST -> world.setBlock(pos,state.setValue(FACING,Direction.SOUTH), 3);
                    case SOUTH -> world.setBlock(pos,state.setValue(FACING,Direction.WEST), 3);
                    case WEST -> world.setBlock(pos,state.setValue(FACING,Direction.NORTH), 3);
                    case NORTH -> world.setBlock(pos,state.setValue(FACING,Direction.EAST), 3);
                }
                world.playSound(null,pos, net.minecraft.sounds.SoundEvents.WOODEN_TRAPDOOR_OPEN, net.minecraft.sounds.SoundSource.BLOCKS,1.0f,world.random.nextFloat()+0.8f);
            } else if (player.isShiftKeyDown()){
                if (!world.isClientSide && player instanceof ServerPlayer serverPlayer){
                    NetworkHooks.openScreen(serverPlayer, entity, pos);
                }
            } else {
                if (hit.getDirection().equals(state.getValue(FACING))){
                    entity.useOnButton(state, world);
                } else if (hit.getDirection().equals(Direction.UP)){
                    entity.use(state,world,pos,player);
                } else {
                    if (!world.isClientSide && player instanceof ServerPlayer serverPlayer){
                        NetworkHooks.openScreen(serverPlayer, entity, pos);
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, net.minecraft.world.item.TooltipFlag options) {
        if(net.minecraft.client.gui.screens.Screen.hasShiftDown()){
            tooltip.add(TextUtil.getShiftText(true));
            tooltip.add(Component.literal(" "));
            tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.DEEP_FRYER)));
        } else {
            tooltip.add(TextUtil.getShiftText(false));
        }
        super.appendHoverText(stack, world, tooltip, options);
    }
    
    @Nullable
    @Override
    public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level world, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.DEEP_FRYER_BLOCK_ENTITY.get(),
                (world1, pos, state1, blockEntity) -> ((DeepFryerBlockEntity)blockEntity).tick(world1, state1, (DeepFryerBlockEntity)blockEntity));
    }
}
