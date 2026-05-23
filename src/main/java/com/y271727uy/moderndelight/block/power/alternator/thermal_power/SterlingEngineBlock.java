package com.y271727uy.moderndelight.block.power.alternator.thermal_power;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.kitchenware.AdvanceFurnaceBlock;
import com.y271727uy.moderndelight.block.kitchenware.OvenBlock;
import com.y271727uy.moderndelight.util.MiscUtil;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class SterlingEngineBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPED = Block.box(0,0,0,16,3,16);
    public static final BooleanProperty SMALL_SOUND = BooleanProperty.create("small_sound");
    public static final BooleanProperty IS_WORKING = BooleanProperty.create("is_working");

    public SterlingEngineBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BARS).sound(SoundType.GRASS));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SMALL_SOUND, false).setValue(IS_WORKING, false));
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        if(Screen.hasShiftDown()){
            tooltip.add(TextUtil.getShiftText(true));
            tooltip.add(Component.literal(" "));
            tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.STERLING_ENGINE)));
        } else {
            tooltip.add(TextUtil.getShiftText(false));
        }
        super.appendHoverText(stack, world, tooltip, options);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(SterlingEngineBlock.IS_WORKING)){
            Direction dir = state.getValue(SterlingEngineBlock.FACING);
            double varX = 0;
            double varZ = 0;
            switch (dir){
                case NORTH -> {
                    varX = 0.5;
                    varZ = -0.5;
                }
                case SOUTH -> {
                    varX = 0.5;
                    varZ = 1.5;
                }
                case EAST -> {
                    varX = 1.5;
                    varZ = 0.5;
                }
                case WEST -> {
                    varX = -0.5;
                    varZ = 0.5;
                }
            }
            double x = pos.getX() + varX;
            double y = pos.getY() + 0.3;
            double z = pos.getZ() + varZ;
            for (int i = 0; i < 5; i++){
                world.addParticle(ParticleTypes.POOF, x, y, z,
                        (random.nextFloat()-0.5)/3,
                        (random.nextFloat()-0.5)/5,
                        (random.nextFloat()-0.5)/3);
            }
        }
        super.animateTick(state, world, pos, random);
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable net.minecraft.world.level.block.entity.BlockEntity blockEntity, ItemStack tool) {
        if (!world.isClientSide){
            if (state.getValue(SMALL_SOUND)){
                net.minecraft.world.Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.SPONGE));
            }
        }
        super.playerDestroy(world, player, pos, state, blockEntity, tool);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(SMALL_SOUND, false).setValue(IS_WORKING, false);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPED;
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPED;
    }
    
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Block block = world.getBlockState(pos.below()).getBlock();
        return block instanceof AbstractFurnaceBlock ||
                block instanceof OvenBlock ||
                block instanceof AdvanceFurnaceBlock;
    }
    
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }
    
    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (MiscUtil.isPlayerHoldingCrowbar(player)){
            if (!world.isClientSide){
                if (state.getValue(SMALL_SOUND)){
                    world.setBlock(pos, state.setValue(SMALL_SOUND, false), 3);
                    world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundType.GRASS.getBreakSound(), SoundSource.BLOCKS, 1.0f, world.random.nextFloat()+0.8f);
                    net.minecraft.world.Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), Items.SPONGE.getDefaultInstance());
                } else {
                    Direction dir = state.getValue(FACING);
                    switch (dir){
                        case EAST -> world.setBlock(pos, state.setValue(FACING, Direction.SOUTH), 3);
                        case SOUTH -> world.setBlock(pos, state.setValue(FACING, Direction.WEST), 3);
                        case WEST -> world.setBlock(pos, state.setValue(FACING, Direction.NORTH), 3);
                        case NORTH -> world.setBlock(pos, state.setValue(FACING, Direction.EAST), 3);
                    }
                }
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundSource.BLOCKS, 1.0f, world.random.nextFloat()+0.8f);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.SUCCESS;
        } else if (player.getItemInHand(hand).is(Items.SPONGE) && !state.getValue(SMALL_SOUND)) {
            if (!world.isClientSide){
                player.getItemInHand(hand).shrink(1);
                world.setBlock(pos, state.setValue(SMALL_SOUND, true), 3);
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundType.GRASS.getPlaceSound(), SoundSource.BLOCKS, 1.0f, world.random.nextFloat()+0.8f);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SMALL_SOUND, IS_WORKING);
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    
    @Nullable
    @Override
    public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SterlingEngineBlockEntity(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends net.minecraft.world.level.block.entity.BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level world, BlockState state, net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.STERLING_ENGINE_BLOCK_ENTITY.get(),
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}
