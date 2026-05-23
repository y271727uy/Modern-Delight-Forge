package com.y271727uy.moderndelight.enchantment.custom;

import com.y271727uy.moderndelight.item.tools.StoneMortarItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class FineGrindingEnchantment extends Enchantment {

    public FineGrindingEnchantment() {
        super(Rarity.COMMON, EnchantmentCategory.BREAKABLE, new EquipmentSlot[] {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }
    @Override
    public int getMinCost(int level) {
        return 1 + (level - 1) * 10;
    }
    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 15;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof StoneMortarItem;
    }
}
