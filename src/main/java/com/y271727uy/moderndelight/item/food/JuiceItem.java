package com.y271727uy.moderndelight.item.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

public class JuiceItem extends Item {
    public List<MobEffectInstance> effects = null;
    public int hunger;
    public float saturationModifier;
    public Item juice_container;
    public JuiceItem(int hunger, float saturationModifier, Item juice_container, MobEffectInstance... effects) {
        super(new Item.Properties().stacksTo(16).craftRemainder(juice_container).food((new FoodProperties.Builder()).nutrition(hunger).saturationMod(saturationModifier).build()));
        this.hunger = hunger;
        this.saturationModifier = saturationModifier;
        this.juice_container = juice_container;
        this.effects = Arrays.asList(effects.clone());
    }
    public JuiceItem(int hunger, float saturationModifier, Item juice_container) {
        super(new Item.Properties().stacksTo(16).craftRemainder(juice_container));
        this.hunger = hunger;
        this.saturationModifier = saturationModifier;
        this.juice_container = juice_container;
    }
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (user instanceof Player player) {
            if (!level.isClientSide) {
                if (effects != null) {
                    for (MobEffectInstance effect : effects) {
                        player.addEffect(new MobEffectInstance(effect));
                    }
                }
                player.getInventory().add(juice_container.getDefaultInstance());
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            player.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(this));
        }
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        if (user.canEat(false)) {
            user.startUsingItem(hand);
            return InteractionResultHolder.consume(user.getItemInHand(hand));
        }
        return InteractionResultHolder.fail(user.getItemInHand(hand));
    }
}
