package com.y271727uy.moderndelight.block.food.pizza;

import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.tag.TagKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.y271727uy.moderndelight.block.food.pizza.PizzaWIPBlockEntity.NEED_CHEESE;

@SuppressWarnings("deprecation")

public class WheatDoughBlock extends Block {
    public static final IntegerProperty CRAFT_STATE = IntegerProperty.create("craft_state", 0, 3);
    private static final VoxelShape SHAPE_0 = Block.box(3, 0, 3, 13, 6, 13);
    private static final VoxelShape SHAPE_1 = Block.box(3, 0, 3, 13, 4, 13);
    private static final VoxelShape SHAPE_2 = Block.box(2, 0, 2, 14, 2, 14);
    private static final VoxelShape SHAPE_3 = Block.box(1, 0, 1, 15, 1, 15);

    public WheatDoughBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(CRAFT_STATE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CRAFT_STATE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(CRAFT_STATE)) {
            case 1 -> SHAPE_1;
            case 2 -> SHAPE_2;
            case 3 -> SHAPE_3;
            default -> SHAPE_0;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getShape(state, world, pos, context);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ItemStack handStack = player.getItemInHand(hand);
        Item item = handStack.getItem();
        int currentState = state.getValue(CRAFT_STATE);

        if (isKneadingStick(item) && currentState < 3) {
            if (level.random.nextDouble() < 0.4D) {
                level.setBlock(pos, state.setValue(CRAFT_STATE, currentState + 1), 3);
            }
            handStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            level.playSound(null, pos, SoundEvents.HONEY_BLOCK_BREAK, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() + 0.1F);
            return InteractionResult.SUCCESS;
        }

        if (currentState == 3) {
            if (handStack.is(ModItems.CHEESE.get())) {
                handStack.shrink(1);
                level.playSound(null, pos, SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() + 0.1F);
                level.setBlock(pos, ModBlocks.PIZZA_WIP.get().defaultBlockState(), 3);
            } else {
                player.displayClientMessage(Component.translatable(NEED_CHEESE), true);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    private boolean isKneadingStick(Item item) {
        return new ItemStack(item).is(TagKeys.KNEADING_STICKS);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).isFaceSturdy(world, pos.below(), Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }
}
