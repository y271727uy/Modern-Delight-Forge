package com.y271727uy.moderndelight.item.food.instant_noodles;

import com.y271727uy.moderndelight.util.TextUtil;
import com.y271727uy.moderndelight.util.enums.SpecialIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class PackagedInstantNoodlesItem extends Item {
    public PackagedInstantNoodlesItem() {
        super(new Item.Properties());
    }

    public static SpecialIngredient getSpecialIngredient(ItemStack stack) {
        List<Item> items = getStacksFromNbt(stack);
        if (items == null) {
            return null;
        }

        net.minecraft.world.SimpleContainer inventory = new net.minecraft.world.SimpleContainer(items.size());
        for (int i = 0; i < items.size(); i++) {
            inventory.setItem(i, new ItemStack(items.get(i)));
        }
        for (SpecialIngredient ingredient : SpecialIngredient.values()) {
            if (ingredient.match(inventory)) {
                return ingredient;
            }
        }
        return null;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        SpecialIngredient special = getSpecialIngredient(stack);
        if (special != null) {
            return special.toTranslationKey();
        }
        if (isUnhealthy(stack)) {
            return TextUtil.NOODLE_UNHEALTHY;
        }
        return super.getDescriptionId(stack);
    }

    public static boolean isUnhealthy(ItemStack noodleItem) {
        if (getSpecialIngredient(noodleItem) != null) {
            return false;
        }
        List<Item> items = getStacksFromNbt(noodleItem);
        if (items != null) {
            for (Item item : items) {
                if (!item.isEdible()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        setToolTipFromNoodles(stack, tooltip);
        super.appendHoverText(stack, level, tooltip, flag);
    }

    public static void setToolTipFromNoodles(ItemStack noodles, List<Component> tooltip) {
        List<Item> items = getStacksFromNbt(noodles);
        if (items != null) {
            tooltip.add(Component.translatable(TextUtil.INGREDIENTS).withStyle(ChatFormatting.DARK_GRAY));
            for (Item item : items) {
                if (item != Items.AIR) {
                    tooltip.add(Component.translatable(item.getDescriptionId()).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public static List<Item> getStacksFromNbt(ItemStack stack) {
        CompoundTag nbt = stack.getTag() == null ? null : stack.getTag().getCompound("instant_noodles_ingredients");
        if (nbt != null) {
            List<Item> itemStacks = new ArrayList<>();
            for (int i = 1; i <= 7; i++) {
                String key = "ingredients_" + i;
                if (!nbt.contains(key)) {
                    continue;
                }
                String registerKey = nbt.getString(key);
                ResourceLocation id = ResourceLocation.tryParse(registerKey);
                if (id == null) {
                    continue;
                }
                Item item = BuiltInRegistries.ITEM.get(id);
                if (item != Items.AIR) {
                    itemStacks.add(item);
                }
            }
            if (!itemStacks.isEmpty()) {
                return itemStacks;
            }
        }
        return null;
    }

}
