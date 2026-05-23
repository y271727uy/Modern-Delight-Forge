package com.y271727uy.moderndelight.event;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.item.food.SeasoningItem;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModernDelightMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ModernDelightClientEvents {
    private ModernDelightClientEvents() {
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        CompoundTag nbt = stack.getTagElement("modern_delight_seasoning");
        if (nbt == null) {
            return;
        }

        event.getToolTip().add(Component.translatable(TextUtil.SEASONING_ADDED).withStyle(ChatFormatting.DARK_GRAY));
        for (int i = 1; i <= SeasoningItem.getMaxSeasoning(); i++) {
            if (!nbt.contains("seasoning_" + i)) {
                continue;
            }

            String registerKey = nbt.getString("seasoning_" + i);
            if (registerKey.isEmpty()) {
                continue;
            }

            ResourceLocation id = ResourceLocation.tryParse(registerKey);
            if (id == null) {
                continue;
            }

            Item item = BuiltInRegistries.ITEM.getOptional(id).orElse(null);
            if (item != null) {
                event.getToolTip().addAll(TextUtil.generateToolTip(Component.translatable(item.getDescriptionId()), 11184810));
            }
        }
    }
}

