package com.y271727uy.moderndelight.item.food;

import com.y271727uy.moderndelight.util.enums.CreamFlavor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;

import java.util.List;

public class IceCreamItem extends Item {
    public IceCreamItem() {
        super(new Item.Properties().food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.3f).build()));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, net.minecraft.world.item.TooltipFlag context) {
        var nbt = stack.getTag();
        if(nbt != null && nbt.contains("flavor")){
            MutableComponent text = Component.translatable(CreamFlavor.TRANSLATION_KEY);
            text.append(Component.literal(":"));
            text.withStyle(net.minecraft.ChatFormatting.DARK_GRAY);
            tooltip.add(text);
            int[] array = nbt.getIntArray("flavor");
            for (int i : array){
                tooltip.add(Component.translatable(CreamFlavor.getFlavorByID(i).getTranslationKey()).withStyle(net.minecraft.ChatFormatting.GRAY));
            }
        }
        super.appendHoverText(stack, level, tooltip, context);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (user instanceof Player player){
            if (!level.isClientSide) {
                level.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.PLAYER_BURP,
                        SoundSource.PLAYERS, 1.5f, 0.4f / level.random.nextFloat() * 0.4f + 0.8f);
                var nbt  = stack.getTag();
                int hunger = CreamFlavor.getHungerFromFlavorNBT(nbt);
                if (nbt != null && nbt.contains("flavor")){
                    int[] array = nbt.getIntArray("flavor");
                    for(int i : array){
                        CreamFlavor creamFlavor = CreamFlavor.getFlavorByID(i);
                        if (creamFlavor == CreamFlavor.GOLDEN_APPLE){
                            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
                            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0));
                            break;
                        }
                    }
                }
                player.getFoodData().eat(hunger + 2, 0.3F);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
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
