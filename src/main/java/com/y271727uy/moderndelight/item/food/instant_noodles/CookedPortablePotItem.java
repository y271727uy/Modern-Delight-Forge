package com.y271727uy.moderndelight.item.food.instant_noodles;

import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;

public class CookedPortablePotItem extends Item {
    public CookedPortablePotItem() {
        super(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(3).saturationMod(0.1f).build()));
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        String noodleType = PortablePotItem.getNoodleTypeKey(stack);
        if (noodleType != null) {
            return noodleType;
        }
        if (isUnhealthy(stack)) {
            return TextUtil.NOODLE_UNHEALTHY;
        }
        return super.getDescriptionId(stack);
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
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("noodles_data")) {
            ItemStack noodles = ItemStack.of(tag.getCompound("noodles_data"));
            List<Item> items = PortablePotItem.getWorldNoodleItems(noodles);
            if (items != null) {
                tooltip.add(Component.translatable(TextUtil.INGREDIENTS).withStyle(ChatFormatting.DARK_GRAY));
                for (Item item : items) {
                    if (item != Items.AIR) {
                        tooltip.add(Component.translatable(item.getDescriptionId()).withStyle(ChatFormatting.GRAY));
                    }
                }
            }
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player && !level.isClientSide) {
            if (PortablePotItem.isUnhealthy(stack)) {
                player.getFoodData().eat(3, 0.1F);
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 0));
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
            } else {
                player.getFoodData().eat(8, 1.5F);
            }
            return new ItemStack(ModItems.PORTABLE_POT.get());
        }
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.canEat(false)) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    public static boolean isUnhealthy(ItemStack potItem) {
        if (PortablePotItem.getNoodleTypeKey(potItem) != null) {
            return false;
        }
        List<Item> items = PortablePotItem.getWorldNoodleItems(potItem);
        if (items == null) {
            return false;
        }
        for (Item item : items) {
            if (!item.isEdible()) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack createCookedPot(List<net.minecraft.world.item.crafting.Ingredient> ingredients) {
        ItemStack noodles = new ItemStack(ModItems.PACKAGED_INSTANT_NOODLES.get());
        CompoundTag noodleIngredients = new CompoundTag();
        for (int i = 1; i <= ingredients.size(); i++) {
            try {
                ItemStack[] stacks = ingredients.get(i - 1).getItems();
                if (stacks.length == 0) {
                    continue;
                }
                ResourceLocation name = BuiltInRegistries.ITEM.getKey(stacks[0].getItem());
                noodleIngredients.putString("ingredients_" + i, name.toString());
            } catch (Exception ignored) {
            }
        }
        noodles.getOrCreateTag().put("instant_noodles_ingredients", noodleIngredients);

        ItemStack cookedPot = new ItemStack(ModItems.COOKED_PORTABLE_POT.get());
        CompoundTag potTag = cookedPot.getOrCreateTag();
        potTag.put("noodles_data", noodles.save(new CompoundTag()));
        return cookedPot;
    }
}
