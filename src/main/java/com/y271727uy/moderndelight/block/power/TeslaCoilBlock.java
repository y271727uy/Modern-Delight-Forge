package com.y271727uy.moderndelight.block.power;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.util.MiscUtil;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class TeslaCoilBlock extends BaseEntityBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty SHOW_PARTICLE = BooleanProperty.create("show_particle");
    public static final BooleanProperty IS_OVERLOADED = BooleanProperty.create("is_overloaded");
    
    private static final VoxelShape UP = Block.box(5,2,5,11,16,11);
    private static final VoxelShape DOWN = Block.box(5,0,5,11,14,11);
    private static final VoxelShape EAST = Block.box(0,5,5,14,11,11);
    private static final VoxelShape WEST = Block.box(2,5,5,16,11,11);
    private static final VoxelShape SOUTH = Block.box(5,5,0,11,11,14);
    private static final VoxelShape NORTH = Block.box(5,5,2,11,11,16);

    public TeslaCoilBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.STONE).sound(SoundType.NETHERITE_BLOCK).lightLevel(state -> state.getValue(IS_OVERLOADED) ? 0 : 7));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WATERLOGGED, false).setValue(FACING, Direction.DOWN).setValue(SHOW_PARTICLE, false).setValue(IS_OVERLOADED, true));
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        if(Screen.hasShiftDown()){
            tooltip.add(TextUtil.getShiftText(true));
            tooltip.add(Component.literal(" "));
            tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.TESLA_COIL)));
        } else {
            tooltip.add(TextUtil.getShiftText(false));
        }
        super.appendHoverText(stack, world, tooltip, options);
    }
    
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)){
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case NORTH -> NORTH;
            case UP -> UP;
            default -> DOWN;
        };
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, net.minecraft.util.RandomSource random) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        
        // 展示粒子效果
        if (state.getValue(SHOW_PARTICLE)){
            for(int i = 0; i < 2; ++i) {
                for (int j = 1; j <= 8; j++){
                    world.addParticle(ParticleTypes.END_ROD, x + j, y, z, 0.0, 0.0, 0.0);
                    world.addParticle(ParticleTypes.END_ROD, x, y + j, z, 0.0, 0.0, 0.0);
                    world.addParticle(ParticleTypes.END_ROD, x, y, z + j, 0.0, 0.0, 0.0);
                    world.addParticle(ParticleTypes.END_ROD, x - j, y, z, 0.0, 0.0, 0.0);
                    world.addParticle(ParticleTypes.END_ROD, x, y - j, z, 0.0, 0.0, 0.0);
                    world.addParticle(ParticleTypes.END_ROD, x, y, z - j, 0.0, 0.0, 0.0);
                }
            }
        }
        
        if (state.getValue(IS_OVERLOADED)){
            for (int i = 0; i < 3; ++i){
                world.addParticle(new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 1.5f), 
                        x + world.random.nextDouble()/2, y + 0.2, z + world.random.nextDouble()/2, 0.0, 0.0, 0.0);
            }
        } else {
            AABB box = new AABB(pos).inflate(1.2, 1.2, 1.2);
            List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, box);
            for (LivingEntity entity : entities) {
                if (entity != null){
                    for (int j = 0; j < 3; j++){
                        world.addParticle(ParticleTypes.WAX_OFF,
                                x-0.5+world.random.nextFloat(), y, z-0.5+world.random.nextFloat(),
                                (world.random.nextFloat()-0.5)*3, 1.0 + world.random.nextFloat(), (world.random.nextFloat()-0.5)*3);
                        world.addParticle(ParticleTypes.WAX_OFF,
                                entity.getX()-0.5+world.random.nextFloat(), entity.getY()-0.5 + world.random.nextFloat(), entity.getZ()-0.5+world.random.nextFloat(),
                                (world.random.nextFloat()-0.5)*3, 3.5 + world.random.nextFloat(), (world.random.nextFloat()-0.5)*3);
                    }
                }
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)){
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case NORTH -> NORTH;
            case UP -> UP;
            default -> DOWN;
        };
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING, SHOW_PARTICLE, IS_OVERLOADED);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        BlockState blockState = this.defaultBlockState();
        LevelReader levelView = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        Direction[] directions = ctx.getNearestLookingDirections();
        
        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()){
                Direction direction2 = direction.getOpposite();
                blockState = blockState.setValue(FACING, direction2);
                if (blockState.canSurvive(levelView, blockPos)) {
                    return blockState.setValue(WATERLOGGED, fluidState.is(Fluids.WATER));
                }
            } else {
                blockState = blockState.setValue(FACING, direction);
                if (blockState.canSurvive(levelView, blockPos)) {
                    return blockState.setValue(WATERLOGGED, fluidState.is(Fluids.WATER));
                }
            }
        }
        return this.defaultBlockState().setValue(WATERLOGGED, fluidState.is(Fluids.WATER));
    }
    
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return direction == Direction.DOWN && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, newState, world, pos, posFrom);
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }
    
    @Nullable
    @Override
    public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TeslaCoilBlockEntity(pos, state);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide){
            if (MiscUtil.isPlayerHoldingCrowbar(player)){
                Direction dir = state.getValue(FACING);
                switch (dir){
                    case DOWN -> {
                        switch (hit.getDirection()){
                            case SOUTH -> world.setBlock(pos, state.setValue(FACING, Direction.EAST), 3);
                            case WEST -> world.setBlock(pos, state.setValue(FACING, Direction.SOUTH), 3);
                            case NORTH -> world.setBlock(pos, state.setValue(FACING, Direction.WEST), 3);
                            case EAST -> world.setBlock(pos, state.setValue(FACING, Direction.NORTH), 3);
                        }
                    }
                    case UP -> {
                        switch (hit.getDirection()){
                            case SOUTH -> world.setBlock(pos, state.setValue(FACING, Direction.WEST), 3);
                            case WEST -> world.setBlock(pos, state.setValue(FACING, Direction.NORTH), 3);
                            case NORTH -> world.setBlock(pos, state.setValue(FACING, Direction.EAST), 3);
                            case EAST -> world.setBlock(pos, state.setValue(FACING, Direction.SOUTH), 3);
                        }
                    }
                    case EAST -> {
                        switch (hit.getDirection()){
                            case SOUTH -> world.setBlock(pos, state.setValue(FACING, Direction.UP), 3);
                            case UP -> world.setBlock(pos, state.setValue(FACING, Direction.SOUTH), 3);
                            case NORTH -> world.setBlock(pos, state.setValue(FACING, Direction.DOWN), 3);
                            case DOWN -> world.setBlock(pos, state.setValue(FACING, Direction.NORTH), 3);
                        }
                    }
                    case WEST -> {
                        switch (hit.getDirection()){
                            case SOUTH -> world.setBlock(pos, state.setValue(FACING, Direction.DOWN), 3);
                            case UP -> world.setBlock(pos, state.setValue(FACING, Direction.NORTH), 3);
                            case NORTH -> world.setBlock(pos, state.setValue(FACING, Direction.UP), 3);
                            case DOWN -> world.setBlock(pos, state.setValue(FACING, Direction.SOUTH), 3);
                        }
                    }
                    case SOUTH -> {
                        switch (hit.getDirection()){
                            case WEST -> world.setBlock(pos, state.setValue(FACING, Direction.UP), 3);
                            case UP -> world.setBlock(pos, state.setValue(FACING, Direction.WEST), 3);
                            case EAST -> world.setBlock(pos, state.setValue(FACING, Direction.DOWN), 3);
                            case DOWN -> world.setBlock(pos, state.setValue(FACING, Direction.EAST), 3);
                        }
                    }
                    case NORTH -> {
                        switch (hit.getDirection()){
                            case WEST -> world.setBlock(pos, state.setValue(FACING, Direction.DOWN), 3);
                            case UP -> world.setBlock(pos, state.setValue(FACING, Direction.EAST), 3);
                            case EAST -> world.setBlock(pos, state.setValue(FACING, Direction.UP), 3);
                            case DOWN -> world.setBlock(pos, state.setValue(FACING, Direction.WEST), 3);
                        }
                    }
                }
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundSource.BLOCKS, 1.0f, world.random.nextFloat()+0.8f);
            } else if (world.getBlockEntity(pos) instanceof TeslaCoilBlockEntity entity){
                player.openMenu(entity);
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }
    
    @Nullable
    @Override
    public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level world, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.TESLA_COIL_BLOCK_ENTITY.get(),
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}
