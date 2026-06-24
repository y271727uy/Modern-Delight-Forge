package com.y271727uy.moderndelight.block.kitchenware.steaming;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.util.MiscUtil;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class BambooGrateBlock extends BaseEntityBlock {
    public BambooGrateBlock() {
        super(Properties.copy(Blocks.BAMBOO_PLANKS).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LAYER, 1).setValue(COVERED,false));
    }
    public static final IntegerProperty LAYER = IntegerProperty.create("layer",1,4);
    public static final BooleanProperty COVERED = BooleanProperty.create("covered");
    public static final VoxelShape SHAPED_1 = Block.box(1,0,1,15,4,15);
    public static final VoxelShape SHAPED_2 = Block.box(1,0,1,15,8,15);
    public static final VoxelShape SHAPED_3 = Block.box(1,0,1,15,12,15);
    public static final VoxelShape SHAPED_4 = Block.box(1,0,1,15,16,15);
    public static final VoxelShape SHAPED_5 = Block.box(1,0,1,15,20,15);
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYER,COVERED);
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        if(Screen.hasShiftDown()){
            tooltip.add(TextUtil.getShiftText(true));
            tooltip.add(Component.literal(" "));
            tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.BAMBOO_STEAMER)));

        } else {
            tooltip.add(TextUtil.getShiftText(false));
        }
        super.appendHoverText(stack, world, tooltip, options);
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(COVERED)){
            return switch (state.getValue(LAYER)){
                case 2 -> SHAPED_3;
                case 3 -> SHAPED_4;
                case 4 -> SHAPED_5;
                default -> SHAPED_2;
            };
        } else {
            return switch (state.getValue(LAYER)){
                case 2 -> SHAPED_2;
                case 3 -> SHAPED_3;
                case 4 -> SHAPED_4;
                default -> SHAPED_1;
            };
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(COVERED)){
            return switch (state.getValue(LAYER)){
                case 2 -> SHAPED_3;
                case 3 -> SHAPED_4;
                case 4 -> SHAPED_5;
                default -> SHAPED_2;
            };
        } else {
            return switch (state.getValue(LAYER)){
                case 2 -> SHAPED_2;
                case 3 -> SHAPED_3;
                case 4 -> SHAPED_4;
                default -> SHAPED_1;
            };
        }
    }

    @Override
    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (!world.isClientSide){
            int layer = state.getValue(LAYER);
            if (state.getValue(COVERED)){
                Containers.dropItemStack(world,pos.getX(),pos.getY(),pos.getZ(),new ItemStack(ModBlocks.BAMBOO_COVER.get()));
            }
            if (layer > 1){
                Containers.dropItemStack(world,pos.getX(),pos.getY(),pos.getZ(),new ItemStack(ModBlocks.BAMBOO_GRATE.get(),layer-1));
            }
            if (blockEntity instanceof BambooGrateBlockEntity entity){
                Containers.dropContents(world, pos, entity);
            }
        }
        super.playerDestroy(world, player, pos, state, blockEntity, tool);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        world.scheduleTick(pos,this,1);
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (state.getValue(LAYER) == 4 && state.getValue(COVERED) && !world.getBlockState(pos.above()).isAir()){
            if (world.getBlockState(pos.above()).getBlock().equals(ModBlocks.BAMBOO_COVER.get())){
                world.setBlock(pos,state.setValue(COVERED,true), 3);
                world.setBlock(pos.above(),Blocks.AIR.defaultBlockState(), 3);
                Containers.dropItemStack(world,pos.getX(),pos.getY(),pos.getZ(),new ItemStack(ModBlocks.BAMBOO_COVER.get()));
                return;
            }
            world.setBlock(pos,state.setValue(COVERED,false), 3);
            Containers.dropItemStack(world,pos.getX(),pos.getY(),pos.getZ(),new ItemStack(ModBlocks.BAMBOO_COVER.get()));
            world.playSound(null,pos, SoundEvents.BAMBOO_WOOD_BREAK, SoundSource.BLOCKS,1.0f,world.random.nextFloat() + 0.5f);
        } else if (state.getValue(LAYER) == 4 && !state.getValue(COVERED) && world.getBlockState(pos.above()).getBlock().equals(ModBlocks.BAMBOO_COVER.get())) {
            world.setBlock(pos,state.setValue(COVERED,true), 3);
            world.setBlock(pos.above(),Blocks.AIR.defaultBlockState(), 3);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!world.isClientSide){
            if(world.getBlockEntity(pos) instanceof BambooGrateBlockEntity blockEntity){
                if (MiscUtil.isPlayerHoldingCrowbar(player)){
                    if (player.isShiftKeyDown()){
                        world.destroyBlock(pos,true);
                    } else {
                        if (state.getValue(COVERED)){
                            world.setBlock(pos,state.setValue(COVERED,false), 3);
                            Containers.dropItemStack(world,pos.getX(),pos.getY(),pos.getZ(),new ItemStack(ModBlocks.BAMBOO_COVER.get()));
                            world.playSound(null,pos, SoundEvents.BAMBOO_WOOD_BREAK, SoundSource.BLOCKS,1.0f,world.random.nextFloat() + 0.5f);
                        } else if (state.getValue(LAYER) > 1) {
                            for (int i = 1;i <= 4;i++){
                                Containers.dropItemStack(world,pos.getX(),pos.getY(),pos.getZ(),blockEntity.getItem(4 * state.getValue(LAYER) - i));
                                blockEntity.setItem(4 * state.getValue(LAYER) - i,ItemStack.EMPTY);
                            }
                            world.setBlock(pos,state.setValue(LAYER,state.getValue(LAYER) - 1), 3);
                            Containers.dropItemStack(world,pos.getX(),pos.getY(),pos.getZ(),new ItemStack(ModBlocks.BAMBOO_GRATE.get()));
                            world.playSound(null,pos, SoundEvents.BAMBOO_WOOD_BREAK, SoundSource.BLOCKS,1.0f,world.random.nextFloat() + 0.5f);
                        } else {
                            for (int i = 0;i < 4;i++){
                                Containers.dropItemStack(world,pos.getX(),pos.getY(),pos.getZ(),blockEntity.getItem(i));
                            }
                            world.destroyBlock(pos,true);
                        }
                    }
                } else if (player.getItemInHand(hand).getItem().equals(ModBlocks.BAMBOO_COVER.get().asItem())){
                    if (!state.getValue(COVERED)){
                        if (state.getValue(LAYER) < 4){
                            world.setBlock(pos,state.setValue(COVERED,true), 3);
                            player.getItemInHand(hand).shrink(1);
                            world.playSound(null,pos, SoundEvents.BAMBOO_WOOD_PLACE, SoundSource.BLOCKS,1.0f,world.random.nextFloat() + 0.5f);
                        } else if (world.getBlockState(pos.above()).isAir()){
                            world.setBlock(pos,state.setValue(COVERED,true), 3);
                            player.getItemInHand(hand).shrink(1);
                            world.playSound(null,pos, SoundEvents.BAMBOO_WOOD_PLACE, SoundSource.BLOCKS,1.0f,world.random.nextFloat() + 0.5f);
                        }
                    }
                } else if (player.getItemInHand(hand).getItem().equals(ModBlocks.BAMBOO_GRATE.get().asItem())) {
                    if (state.getValue(LAYER) < 4){
                        if (state.getValue(LAYER) == 3 && !world.getBlockState(pos.above()).isAir() && state.getValue(COVERED)){
                            return InteractionResult.SUCCESS;
                        }
                        world.setBlock(pos,state.setValue(LAYER,state.getValue(LAYER) + 1), 3);
                        player.getItemInHand(hand).shrink(1);
                        world.playSound(null,pos, SoundEvents.BAMBOO_WOOD_PLACE, SoundSource.BLOCKS,1.0f,world.random.nextFloat() + 0.5f);
                    } else if (world.getBlockState(pos.above()).isAir()){
                        player.getItemInHand(hand).shrink(1);
                        world.playSound(null,pos, SoundEvents.BAMBOO_WOOD_PLACE, SoundSource.BLOCKS,1.0f,world.random.nextFloat() + 0.5f);
                        if (!state.getValue(COVERED)){
                            world.setBlock(pos.above(),ModBlocks.BAMBOO_GRATE.get().defaultBlockState(), 3);
                        } else {
                            world.setBlock(pos,state.setValue(COVERED,false), 3);
                            world.setBlock(pos.above(),ModBlocks.BAMBOO_GRATE.get().defaultBlockState().setValue(COVERED,true), 3);
                        }
                    }
                } else if (player instanceof ServerPlayer serverPlayer) {
                    NetworkHooks.openScreen(serverPlayer, blockEntity, buf -> {
                        buf.writeBlockPos(pos);
                        buf.writeInt(state.getValue(LAYER));
                    });
                }
                blockEntity.setChanged();
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BambooGrateBlockEntity(pos,state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.BAMBOO_GRATE_BLOCK_ENTITY.get(),
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}
