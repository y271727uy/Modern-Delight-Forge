package com.y271727uy.moderndelight.item.food;

import com.y271727uy.moderndelight.entity.custom.ButterEntity;
import com.y271727uy.moderndelight.sound.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;

import java.util.List;

public class ButterItem extends Item {
    public ButterItem(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        player.getCooldowns().addCooldown(this, 10);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.ENTITY_BUTTER_SHOOT.get(),
                SoundSource.NEUTRAL, 1.5f, 0.4f / level.random.nextFloat() * 0.4f + 0.8f);
        if (!level.isClientSide) {
            ButterEntity projectile = new ButterEntity(level, player);
            projectile.setItem(heldStack);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(projectile);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            heldStack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(heldStack, level.isClientSide);
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context){
        if(Screen.hasShiftDown()){
            tooltip.add(Component.translatable("moderndelight.tooltips.shift_front").withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("moderndelight.tooltips.butter").withStyle(ChatFormatting.GRAY));
        }else {
            tooltip.add(Component.translatable("moderndelight.tooltips.shift_front").withStyle(ChatFormatting.DARK_GRAY));
        }
        super.appendHoverText(stack, level, tooltip, context);
    }
}
