package com.y271727uy.moderndelight.block.food;

import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

import javax.annotation.Nullable;

import java.util.List;

public class SteamedPumpkinBlock extends Block {
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 3);
    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 8, 15);

    public SteamedPumpkinBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CAKE));
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
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
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        tooltip.add(Component.translatable(TextUtil.CAN_PLACE).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, world, tooltip, options);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(hand);
        int bites = state.getValue(BITES);

        if (heldStack.is(Items.BOWL)) {
            if (!level.isClientSide) {
                if (heldStack.getCount() == 1) {
                    player.setItemInHand(hand, ModItems.STEAMED_PUMPKIN_IN_BOWL.get().getDefaultInstance());
                } else {
                    heldStack.shrink(1);
                    player.getInventory().add(ModItems.STEAMED_PUMPKIN_IN_BOWL.get().getDefaultInstance());
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.2F, level.random.nextFloat() + 0.6F);
                if (bites < 3) {
                    level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
                } else {
                    level.setBlock(pos, ModBlocks.WOODEN_PLATE.get().defaultBlockState(), 3);
                }
            }
            return InteractionResult.SUCCESS;
        }

        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            player.awardStat(Stats.EAT_CAKE_SLICE);
            player.getFoodData().eat(6, 0.5F);
            level.gameEvent(player, GameEvent.EAT, pos);
            level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 2.3F, level.random.nextFloat() + 0.6F);
            if (bites < 3) {
                level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
            } else {
                level.setBlock(pos, ModBlocks.WOODEN_PLATE.get().defaultBlockState(), 3);
            }
        }
        return InteractionResult.SUCCESS;
    }

    protected boolean canPlaceOnTop(BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.isFaceSturdy(world, pos, Direction.UP);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockPos = pos.below();
        return this.canPlaceOnTop(world.getBlockState(blockPos), world, blockPos);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }
}
