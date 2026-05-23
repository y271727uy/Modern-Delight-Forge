package com.y271727uy.moderndelight.item.food;

import com.y271727uy.moderndelight.item.ModItems;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SunFlowerSeedItem extends Item {
    public SunFlowerSeedItem() {
        super(new Item.Properties());
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 10;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, net.minecraft.world.entity.LivingEntity user) {
        if (user instanceof Player player){
            player.getCooldowns().addCooldown(this, 8);
            level.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.PLAYER_BURP,
                    SoundSource.PLAYERS, 1.5f, 0.4f / level.random.nextFloat() * 0.4f + 0.8f);
            if (!level.isClientSide) {
                ItemEntity itemEntity = new ItemEntity(level,user.getX(),user.getEyeY(),user.getZ(), new ItemStack(ModItems.SUNFLOWER_SEED_PEEL.get()));
                itemEntity.setDeltaMovement(user.getViewVector(1.0F).scale(0.5));
                itemEntity.setPickUpDelay(20);
                level.addFreshEntity(itemEntity);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            player.addItem(new ItemStack(ModItems.SUNFLOWER_SEED_PULP.get()));
        }
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        if (user.canEat(false)){
            user.startUsingItem(hand);
            return InteractionResultHolder.consume(user.getItemInHand(hand));
        }
        return InteractionResultHolder.fail(user.getItemInHand(hand));
    }
}
