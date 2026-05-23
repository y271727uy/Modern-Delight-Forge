package com.y271727uy.moderndelight.block.food.pizza;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class PizzaBlock extends AbstractPizzaBlock {
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 6);

    public PizzaBlock() {
        super();
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof PizzaBlockEntity entity) {
                player.awardStat(Stats.EAT_CAKE_SLICE);
                player.getFoodData().eat(entity.getHunger(), 0.3F);
                int bites = state.getValue(BITES);
                level.gameEvent(player, GameEvent.EAT, pos);
                level.playSound(null, pos, SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 2.3F, level.random.nextFloat() + 0.6F);
                if (bites < 6) {
                    level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
                } else {
                    level.removeBlock(pos, false);
                    level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PizzaBlockEntity(pos, state);
    }
}
