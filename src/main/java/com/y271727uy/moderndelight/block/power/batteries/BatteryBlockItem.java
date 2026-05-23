package com.y271727uy.moderndelight.block.power.batteries;

import com.y271727uy.moderndelight.util.energy.ItemStackEnergyCapabilityProvider;
import com.y271727uy.moderndelight.util.energy.StackEnergyStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;

public class BatteryBlockItem extends BlockItem {
    final AbstractBatteryBlock batteryBlock;
    
    public BatteryBlockItem(AbstractBatteryBlock block) {
        super(block, new Item.Properties().stacksTo(1));
        this.batteryBlock = block;
    }
    
    public static final String TOOLTIP_TEXT = "toolTipText.moderndelight.battery_name";
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag options) {
        CompoundTag nbtCompound = BlockItem.getBlockEntityData(stack);
        if (nbtCompound != null) {
            if (nbtCompound.contains(AbstractBatteryBlock.BATTERY_POWER_TAG)) {
                int energy = AbstractBatteryBlock.getBatteryEnergy(stack);
                int maxEnergy = AbstractBatteryBlock.getBatteryMaxEnergy(stack);
                tooltip.add(Component.translatable(TOOLTIP_TEXT).withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
                tooltip.add(Component.literal(energy + "/" + maxEnergy + " FE").withStyle(net.minecraft.ChatFormatting.GRAY));
            }
        }
        super.appendHoverText(stack, world, tooltip, options);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int maxEnergy = AbstractBatteryBlock.getBatteryMaxEnergy(stack);
        if (maxEnergy > 0) {
            return (int) Math.min(13L * AbstractBatteryBlock.getBatteryEnergy(stack) / maxEnergy, 13);
        }
        return 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xffffff;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }


    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        int capacity = AbstractBatteryBlock.getBatteryMaxEnergy(stack);
        return new ItemStackEnergyCapabilityProvider(new StackEnergyStorage(stack, capacity, capacity, capacity) {
            @Override
            protected int readEnergy() {
                return AbstractBatteryBlock.getBatteryEnergy(stack);
            }

            @Override
            protected void writeEnergy(int energy) {
                AbstractBatteryBlock.setBatteryEnergy(stack, energy);
            }
        });
    }
}
