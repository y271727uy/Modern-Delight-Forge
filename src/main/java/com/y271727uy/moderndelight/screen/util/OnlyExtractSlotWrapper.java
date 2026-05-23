package com.y271727uy.moderndelight.screen.util;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * 一个只允许提取物品的槽位，用于 Container 类型
 */
public class OnlyExtractSlotWrapper extends Slot {
    public OnlyExtractSlotWrapper(Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(Player player) {
        return true;
    }
}
