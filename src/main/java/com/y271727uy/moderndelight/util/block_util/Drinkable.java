package com.y271727uy.moderndelight.util.block_util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public interface Drinkable {
    int getHunger();
    float getSaturationModifier();
    List<MobEffectInstance> getEffects();

    default void drink(Level level, Player player) {
        if (getEffects() != null) {
            for (MobEffectInstance effect : getEffects()) {
                player.addEffect(new MobEffectInstance(effect));
            }
        }
        player.getFoodData().eat(getHunger(), getSaturationModifier());
    }

    default InteractionResultHolder<ItemStack> getItemStackTypedActionResult(Player user, InteractionHand hand) {
        if (user.canEat(false)) {
            user.startUsingItem(hand);
            return InteractionResultHolder.consume(user.getItemInHand(hand));
        }
        return InteractionResultHolder.fail(user.getItemInHand(hand));
    }
}
