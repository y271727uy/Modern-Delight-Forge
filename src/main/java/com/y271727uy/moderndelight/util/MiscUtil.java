package com.y271727uy.moderndelight.util;

import com.y271727uy.moderndelight.item.food.SeasoningItem;
import com.y271727uy.moderndelight.tag.TagKeys;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Method;

public class MiscUtil {
    public static boolean isPlayerHoldingCrowbar(Object player) {
        Object stack = invokeFirst(player, new String[]{"getMainHandItem", "getMainHandStack"});
        Object item = invokeFirst(stack, new String[]{"getItem"});
        return matchesTag(item, TagKeys.CROWBARS);
    }

    public static boolean isInk(Object item) {
        return matchesTag(item, TagKeys.INKS);
    }

    public static void applyFoodEffects(Object stack, Object targetEntity) {
        Object nbt = invokeFirst(stack, new String[]{"getTagElement", "getSubNbt", "getOrCreateTagElement"}, "modern_delight_seasoning");
        if (nbt == null) {
            return;
        }

        try {
            for (int i = 1; i <= SeasoningItem.getMaxSeasoning(); i++) {
                if (!invokeBoolean(nbt, "contains", "seasoning_" + i)) {
                    continue;
                }

                String name = String.valueOf(invokeFirst(nbt, new String[]{"getString"}, "seasoning_" + i));
                if (name.isEmpty()) {
                    continue;
                }

                ResourceLocation id = ResourceLocation.tryParse(name);
                if (id == null) {
                    continue;
                }

                Item seasoningItem = BuiltInRegistries.ITEM.getOptional(id).orElse(null);
                if (!(seasoningItem instanceof SeasoningItem seasoning)) {
                    continue;
                }

                for (MobEffectInstance effect : seasoning.getEffects()) {
                    invokeFirst(targetEntity, new String[]{"addEffect", "addStatusEffect"}, new MobEffectInstance(effect));
                }
            }
        } catch (Throwable ignored) {
        }
    }

    private static boolean matchesTag(Object item, TagKey<Item> tag) {
        if (!(item instanceof Item currentItem)) {
            return false;
        }
        return new ItemStack(currentItem).is(tag);
    }

    private static boolean invokeBoolean(Object target, String methodName, Object... args) {
        Object result = invokeFirst(target, new String[]{methodName}, args);
        return result instanceof Boolean b && b;
    }

    private static Object invokeFirst(Object target, String[] names, Object... args) {
        if (target == null) {
            return null;
        }
        for (String name : names) {
            try {
                Method method = findMethod(target.getClass(), name, args.length);
                if (method == null) {
                    continue;
                }
                method.setAccessible(true);
                return method.invoke(target, args);
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    private static Method findMethod(Class<?> type, String name, int argCount) {
        for (Method method : type.getMethods()) {
            if (method.getName().equals(name) && method.getParameterCount() == argCount) {
                return method;
            }
        }
        return null;
    }
}

