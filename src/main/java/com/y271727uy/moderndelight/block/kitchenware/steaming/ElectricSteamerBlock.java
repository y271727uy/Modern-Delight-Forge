package com.y271727uy.moderndelight.block.kitchenware.steaming;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.util.MiscUtil;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class ElectricSteamerBlock extends BaseEntityBlock {
    public ElectricSteamerBlock() {
        super(Properties.copy(Blocks.IRON_BARS));
        this.registerDefaultState(this.stateDefinition.any().setValue(IS_WORKING,false));
    }
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty IS_WORKING = BooleanProperty.create("is_working");
    private static final VoxelShape SHAPED = Block.box(2,0,2,14,16,14);
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        if(Screen.hasShiftDown()){
            tooltip.add(TextUtil.getShiftText(true));
            tooltip.add(TextUtil.getAltText(false));
            tooltip.add(Component.literal(" "));
            tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.ELECTRIC_STEAMER)));
        } else if (Screen.hasAltDown()) {
            tooltip.add(TextUtil.getShiftText(false));
            tooltip.add(TextUtil.getAltText(true));
            tooltip.add(Component.literal(" "));
            tooltip.add(TextUtil.getACCom("10"));
        } else {
            tooltip.add(TextUtil.getShiftText(false));
            tooltip.add(TextUtil.getAltText(false));
        }
        super.appendHoverText(stack, world, tooltip, options);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPED;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPED;
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
    public BlockState rotate(BlockState state, net.minecraft.world.level.block.Rotation rotation) {
        return state.setValue(FACING,rotation.rotate(state.getValue(FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING,IS_WORKING);
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ElectricSteamerBlockEntity entity){
                Containers.dropContents(world, pos, entity);
                world.updateNeighbourForOutputSignal(pos,this);
            }
        }
        super.onRemove(state, world, pos, newState, moved);
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ElectricSteamerBlockEntity(pos,state);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide){
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
            world.playSound(null,pos, SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundSource.BLOCKS,1.0f,world.random.nextFloat()+0.8f);
        } else if (world.getBlockEntity(pos) instanceof ElectricSteamerBlockEntity blockEntity){
            if (player.getItemInHand(hand).getItem().equals(Items.WATER_BUCKET)){
                if (blockEntity.fillWater(world,pos)){
                    player.setItemInHand(hand,Items.BUCKET.getDefaultInstance());
                }
            } else {
                NetworkHooks.openScreen((ServerPlayer) player, blockEntity, buf -> buf.writeBlockPos(pos));
            }
        }
        return InteractionResult.CONSUME;
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.ELECTRIC_STEAMER_BLOCK_ENTITY.get(),
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}
