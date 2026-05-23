package com.y271727uy.moderndelight.item.food;

import com.y271727uy.moderndelight.util.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

public class SeasoningItem extends Item {
    public List<MobEffectInstance> effects;
    public SeasoningItem(Properties properties, MobEffectInstance... effects) {
        super(properties);
        this.effects = Arrays.asList(effects.clone());
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, net.minecraft.world.item.TooltipFlag context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("moderndelight.tooltips.shift_front").withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("moderndelight.tooltips.condiment_tip").withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("moderndelight.tooltips.shift_front").withStyle(ChatFormatting.DARK_GRAY));
        }
        super.appendHoverText(stack, level, tooltip, context);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack foodItem;
        ItemStack thisStack = user.getItemInHand(hand);
        if (hand == InteractionHand.OFF_HAND){
            foodItem = user.getMainHandItem();
        } else {
            foodItem = user.getOffhandItem();
        }
        if (foodItem.isEmpty()){
            if (this.isEdible()) {
                ItemStack itemStack = user.getItemInHand(hand);
                if (user.canEat(this.isEdible())) {
                    user.startUsingItem(hand);
                    return InteractionResultHolder.consume(itemStack);
                }
            }
        }
        if (foodItem.getItem().isEdible()){
            if (level.isClientSide){
                return InteractionResultHolder.sidedSuccess(user.getItemInHand(hand), true);
            } else {
                user.getCooldowns().addCooldown(this, 20);
                if (foodItem.getItem() instanceof SeasoningItem){
                    user.displayClientMessage(Component.translatable("moderndelight.tooltips.failed_seasoning"), true);
                } else {
                    int foodCount = foodItem.getCount();
                    int thisCount = thisStack.getCount();
                    boolean count = thisCount > foodCount;
                    ItemStack newFood = foodItem.copy();
                    newFood.setCount(count ? foodCount : thisCount);
                    CompoundTag nbt = newFood.getOrCreateTagElement("modern_delight_seasoning");
                    boolean hasAdded = false;
                    for (int i = 1; i <= getMaxSeasoning();i++){
                        if (nbt.contains("seasoning_"+i)){
                            continue;
                        }
                        String name = BuiltInRegistries.ITEM.getKey(thisStack.getItem()).toString();
                        nbt.putString("seasoning_"+i,name);
                        hasAdded = true;
                        break;
                    }
                    if (hasAdded){
                        if (thisStack.getItem().hasCraftingRemainingItem()){
                            ItemStack recipeRemainder = new ItemStack(thisStack.getItem().getCraftingRemainingItem(),newFood.getCount());
                            user.getInventory().add(recipeRemainder);
                        }
                        foodItem.shrink(newFood.getCount());
                        thisStack.shrink(newFood.getCount());
                        user.getInventory().add(newFood);
                        level.playSound(null,user.getX(),user.getY(),user.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,1.0f,level.random.nextFloat() + 0.8f);
                    } else {
                        user.displayClientMessage(Component.translatable("moderndelight.tooltips.failed_seasoning"), true);
                    }
                }
                return InteractionResultHolder.consume(user.getItemInHand(hand));
            }
        } else {
            user.displayClientMessage(Component.translatable("moderndelight.tooltips.need_food"),true);
            return InteractionResultHolder.pass(user.getItemInHand(hand));
        }
    }
    public static int getMaxSeasoning(){
        try {
            int m = ModConfig.getMaxSeasonings();
            if (m > 0){
                return m;
            } else return 5;
        } catch (Throwable e){
            return 5;
        }
    }
    public List<MobEffectInstance> getEffects() {
        return effects;
    }
}
