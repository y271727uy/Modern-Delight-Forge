package com.y271727uy.moderndelight.block.biogas;

import com.y271727uy.moderndelight.util.MiscUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkHooks;
import javax.annotation.Nullable;
import java.util.List;

public class GasCanisterBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPED = Block.box(3, 0, 3, 13, 16, 13);

    public GasCanisterBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion().pushReaction(PushReaction.DESTROY));
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return SHAPED;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return SHAPED;
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
        if (MiscUtil.isPlayerHoldingCrowbar(player)) {
            Direction dir = state.getValue(FACING);
            level.setBlock(pos, state.setValue(FACING, switch (dir) {
                case EAST -> Direction.SOUTH;
                case SOUTH -> Direction.WEST;
                case WEST -> Direction.NORTH;
                default -> Direction.EAST;
            }), 3);
            return InteractionResult.CONSUME;
        }
        if (player.getMainHandItem().is(Items.FLINT_AND_STEEL) || player.getOffhandItem().is(Items.FLINT_AND_STEEL)
                || player.getMainHandItem().is(Items.FIRE_CHARGE) || player.getOffhandItem().is(Items.FIRE_CHARGE)) {
            if (level.getBlockEntity(pos) instanceof GasCanisterBlockEntity blockEntity) {
                blockEntity.use(player, level);
            }
            return InteractionResult.CONSUME;
        }
        if (level.getBlockEntity(pos) instanceof GasCanisterBlockEntity blockEntity && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, blockEntity, pos);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == net.minecraft.world.level.material.Fluids.WATER);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof GasCanisterBlockEntity gasCanisterBlockEntity) {
            ItemStack stack = new ItemStack(com.y271727uy.moderndelight.block.ModBlocks.GAS_CANISTER_ITEM.get());
            writeBlockEntityData(stack, gasCanisterBlockEntity);
            return List.of(stack);
        }
        return super.getDrops(state, builder);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        ItemStack stack = new ItemStack(com.y271727uy.moderndelight.block.ModBlocks.GAS_CANISTER_ITEM.get());
        if (level.getBlockEntity(pos) instanceof GasCanisterBlockEntity blockEntity) {
            writeBlockEntityData(stack, blockEntity);
        }
        return stack;
    }

    private static void writeBlockEntityData(ItemStack stack, GasCanisterBlockEntity blockEntity) {
        CompoundTag tag = blockEntity.saveWithoutMetadata();
        BlockItem.setBlockEntityData(stack, com.y271727uy.moderndelight.block.ModBlockEntities.GAS_CANISTER_BLOCK_ENTITY.get(), tag);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GasCanisterBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, com.y271727uy.moderndelight.block.ModBlockEntities.GAS_CANISTER_BLOCK_ENTITY.get(),
                (world1, pos1, state1, be) -> be.tick(world1, pos1, state1, be));
    }
}
