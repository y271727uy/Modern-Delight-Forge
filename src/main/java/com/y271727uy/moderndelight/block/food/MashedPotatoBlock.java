package com.y271727uy.moderndelight.block.food;

import com.y271727uy.moderndelight.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MashedPotatoBlock extends Block {
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, 9);
    private static final VoxelShape LEVEL_9 = Block.box(0, 0, 0, 16, 16, 16);
    private static final VoxelShape LEVEL_8 = Block.box(0, 0, 0, 16, 14, 16);
    private static final VoxelShape LEVEL_7 = Block.box(0, 0, 0, 16, 12, 16);
    private static final VoxelShape LEVEL_6 = Block.box(0, 0, 0, 16, 10, 16);
    private static final VoxelShape LEVEL_5 = Block.box(0, 0, 0, 16, 8, 16);
    private static final VoxelShape LEVEL_4 = Block.box(0, 0, 0, 16, 6, 16);
    private static final VoxelShape LEVEL_3 = Block.box(0, 0, 0, 16, 4, 16);
    private static final VoxelShape LEVEL_2 = Block.box(0, 0, 0, 16, 2, 16);
    private static final VoxelShape LEVEL_1 = Block.box(0, 0, 0, 16, 1, 16);

    public MashedPotatoBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 9));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(LEVEL)) {
            case 8 -> LEVEL_8;
            case 7 -> LEVEL_7;
            case 6 -> LEVEL_6;
            case 5 -> LEVEL_5;
            case 4 -> LEVEL_4;
            case 3 -> LEVEL_3;
            case 2 -> LEVEL_2;
            case 1 -> LEVEL_1;
            default -> LEVEL_9;
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
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return state.getValue(LEVEL);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        int bites = state.getValue(LEVEL);

        if (heldStack.is(Items.BOWL)) {
            if (!level.isClientSide) {
                if (heldStack.getCount() == 1) {
                    player.setItemInHand(hand, ModItems.MASHED_POTATO.get().getDefaultInstance());
                } else {
                    heldStack.shrink(1);
                    player.getInventory().add(ModItems.MASHED_POTATO.get().getDefaultInstance());
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, level.random.nextFloat() + 0.5F);
                if (bites > 1) {
                    level.setBlock(pos, state.setValue(LEVEL, bites - 1), 3);
                } else {
                    level.removeBlock(pos, false);
                    level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
                }
            }
            return InteractionResult.SUCCESS;
        }

        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            player.awardStat(Stats.EAT_CAKE_SLICE);
            player.getFoodData().eat(1, 0.3F);
            level.gameEvent(player, GameEvent.EAT, pos);
            level.playSound(null, pos, SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 2.3F, level.random.nextFloat() + 0.8F);
            if (bites > 1) {
                level.setBlock(pos, state.setValue(LEVEL, bites - 1), 3);
            } else {
                level.removeBlock(pos, false);
                level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
