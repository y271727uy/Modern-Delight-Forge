package com.y271727uy.moderndelight.block.crops;

import com.y271727uy.moderndelight.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class GarlicCropBlock extends CropBlock {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 5);

    public GarlicCropBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 5;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.GARLIC.get();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int age = state.getValue(AGE);
        boolean isMature = age == 5;
        if (!isMature && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if (isMature) {
            int quantity = 1 + level.random.nextInt(2);
            Block.popResource(level, pos, new ItemStack(ModItems.GARLIC.get(), quantity));
            level.setBlock(pos, state.setValue(AGE, 0), 3);
            player.playSound(SoundEvents.CROP_BREAK, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        } else {
            return super.use(state, level, pos, player, hand, hit);
        }
    }
}
