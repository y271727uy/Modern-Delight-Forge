package com.y271727uy.moderndelight.block.food;

import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.util.TextUtil;
import com.y271727uy.moderndelight.util.block_util.Drinkable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

import java.util.List;

public class GlassCupOfTeaBlockItem extends BlockItem implements Drinkable {
    public GlassCupOfTeaBlockItem(Block block) {
        super(block, new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3f).build()));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context) {
        tooltip.add(Component.translatable(TextUtil.CAN_PLACE).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, tooltip, context);
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
        if (user instanceof Player player && !level.isClientSide) {
            drink(level, player);
            player.getInventory().add(new ItemStack(ModBlocks.GLASS_CUP.get().asItem()));
            player.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return stack;
    }

    @Override
    public int getHunger() {
        return ((GlassCupOfTeaBlock) getBlock()).getHunger();
    }

    @Override
    public float getSaturationModifier() {
        return ((GlassCupOfTeaBlock) getBlock()).getSaturationModifier();
    }

    @Override
    public List<MobEffectInstance> getEffects() {
        return ((GlassCupOfTeaBlock) getBlock()).getEffects();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        return getItemStackTypedActionResult(user, hand);
    }
}

