package com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying;

import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.util.MiscUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import javax.annotation.Nullable;

import java.util.List;

public class DeepFryBasketBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public DeepFryBasketBlock() {
        super(Properties.copy(Blocks.DIRT)
                .noOcclusion().destroyTime(0.0f).sound(net.minecraft.world.level.block.SoundType.LANTERN));
    }
    private static final VoxelShape TYPE_WEST = Block.box(1,0,1,16,8,15);
    private static final VoxelShape TYPE_EAST = Block.box(0,0,1,15,8,15);
    private static final VoxelShape TYPE_SOUTH = Block.box(1,0,0,15,8,15);
    private static final VoxelShape TYPE_NORTH = Block.box(1,0,1,15,8,16);
    
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
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, net.minecraft.world.item.TooltipFlag options) {
        CompoundTag nbtCompound = BlockItem.getBlockEntityData(stack);
        if (nbtCompound != null) {
            if (nbtCompound.contains("Items", 9)) {
                net.minecraft.core.NonNullList<ItemStack> defaultedList = net.minecraft.core.NonNullList.withSize(4, ItemStack.EMPTY);
                net.minecraft.world.ContainerHelper.loadAllItems(nbtCompound, defaultedList);
                for (ItemStack itemStack : defaultedList) {
                    if (!itemStack.isEmpty()) {
                        Component mutableText = itemStack.getHoverName().copy();
                        tooltip.add(Component.literal(mutableText.getString() + " x" + itemStack.getCount()));
                    }
                }
            }
        }
        super.appendHoverText(stack, world, tooltip, options);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        net.minecraft.world.level.block.entity.BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof DeepFryBasketBlockEntity deepFryBasketBlockEntity) {
            ItemStack itemStack = new ItemStack(ModBlocks.DEEP_FRY_BASKET_ITEM.get());
            deepFryBasketBlockEntity.saveToItem(itemStack);
            return List.of(itemStack);
        }
        return super.getDrops(state, builder);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        ItemStack itemStack = new ItemStack(ModBlocks.DEEP_FRY_BASKET_ITEM.get());
        if (level.getBlockEntity(pos) instanceof DeepFryBasketBlockEntity blockEntity) {
            blockEntity.saveToItem(itemStack);
        }
        return itemStack;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    
    @Override
    public BlockState rotate(BlockState state, net.minecraft.world.level.block.Rotation rotation) {
        return state.setValue(FACING,rotation.rotate(state.getValue(FACING)));
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide){
            return InteractionResult.SUCCESS;
        }
        if (MiscUtil.isPlayerHoldingCrowbar(player)){
            Direction dir = state.getValue(FACING);
            switch (dir){
                case EAST -> world.setBlock(pos,state.setValue(FACING,Direction.SOUTH), 3);
                case SOUTH -> world.setBlock(pos,state.setValue(FACING,Direction.WEST), 3);
                case WEST -> world.setBlock(pos,state.setValue(FACING,Direction.NORTH), 3);
                case NORTH -> world.setBlock(pos,state.setValue(FACING,Direction.EAST), 3);
            }
            world.playSound(null,pos, net.minecraft.sounds.SoundEvents.WOODEN_TRAPDOOR_OPEN, net.minecraft.sounds.SoundSource.BLOCKS,1.0f,world.random.nextFloat()+0.8f);
        } else {
            if (world.getBlockEntity(pos) instanceof DeepFryBasketBlockEntity blockEntity){
                blockEntity.use(world,player,hand);
            }
        }
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DeepFryBasketBlockEntity(pos,state);
    }
}
