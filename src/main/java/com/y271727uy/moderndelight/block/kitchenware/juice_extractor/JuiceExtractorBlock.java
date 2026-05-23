package com.y271727uy.moderndelight.block.kitchenware.juice_extractor;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.util.MiscUtil;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
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
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.client.gui.screens.Screen;
import javax.annotation.Nullable;

import java.util.List;

public class JuiceExtractorBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty IS_WORKING = BooleanProperty.create("is_working");
    public static final BooleanProperty IS_FULL = BooleanProperty.create("is_full");

    private static final VoxelShape SHAPED = Block.box(2,0,2,14,18,14);

    public JuiceExtractorBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        if(Screen.hasShiftDown()){
            tooltip.add((MutableComponent)TextUtil.getShiftText(true));
            tooltip.add((MutableComponent)TextUtil.getAltText(false));
            tooltip.add(Component.literal(" "));
            tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.JUICE_EXTRACTOR)));
        } else if (Screen.hasAltDown()) {
            tooltip.add((MutableComponent)TextUtil.getShiftText(false));
            tooltip.add((MutableComponent)TextUtil.getAltText(true));
            tooltip.add(Component.literal(" "));
            tooltip.add((MutableComponent)TextUtil.getACCom("10"));
        } else {
            tooltip.add((MutableComponent)TextUtil.getShiftText(false));
            tooltip.add((MutableComponent)TextUtil.getAltText(false));
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = this.defaultBlockState();
        LevelReader LevelReader = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        Direction[] directions = ctx.getNearestLookingDirections();
        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction2 = direction.getOpposite();
                blockState = blockState.setValue(FACING, direction2);
                if (blockState.canSurvive(LevelReader, blockPos)) {
                    return blockState;
                }
            }
        }
        return null;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPED;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPED;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(IS_WORKING)){
            for (int i = 0; i < 16; i ++){
                world.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.ORANGE_DYE)),
                        pos.getX() + Math.random(), pos.getY() + .5, pos.getZ() + Math.random(),
                        (Math.random() - .5) / 4, Math.random() / 4, (Math.random() - .5) / 4);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof JuiceExtractorBlockEntity entity){
                Containers.dropContents(world, pos, entity);
                world.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, IS_FULL, IS_WORKING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new JuiceExtractorBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide){
            return InteractionResult.SUCCESS;
        }
        if (MiscUtil.isPlayerHoldingCrowbar(player)){
            Direction dir = state.getValue(FACING);
            switch (dir){
                case EAST -> world.setBlock(pos, state.setValue(FACING, Direction.SOUTH), 3);
                case SOUTH -> world.setBlock(pos, state.setValue(FACING, Direction.WEST), 3);
                case WEST -> world.setBlock(pos, state.setValue(FACING, Direction.NORTH), 3);
                case NORTH -> world.setBlock(pos, state.setValue(FACING, Direction.EAST), 3);
            }
            world.playSound(null, pos, SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundSource.BLOCKS, 1.0f, world.random.nextFloat() + 0.8f);
        } else if (world.getBlockEntity(pos) instanceof JuiceExtractorBlockEntity entity){
            entity.use(world, pos, player);
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.JUICE_EXTRACTOR_BLOCK_ENTITY.get(), JuiceExtractorBlockEntity::tick);
    }
}


