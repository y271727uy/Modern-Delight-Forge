package com.y271727uy.moderndelight.item.food;

import com.y271727uy.moderndelight.entity.custom.CherryBombEntity;
import com.y271727uy.moderndelight.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CherryBombItem extends Item {
    public CherryBombItem(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        player.getCooldowns().addCooldown(this, 30);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ENTITY_CHERRY_BOMB_SHOOT.get(),
                SoundSource.NEUTRAL, 1.5f, 2.0f / level.random.nextFloat() * .4f + .8f);
        if (!level.isClientSide) {
            CherryBombEntity projectile = new CherryBombEntity(level, player);
            projectile.setItem(heldStack);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.8F, 1.0F);
            level.addFreshEntity(projectile);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            heldStack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(heldStack, level.isClientSide);
    }
}
