package com.y271727uy.moderndelight.item.food;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;

public class PackagedItem extends Item {
    private final ResourceLocation packageItemId;
    public PackagedItem(ResourceLocation packageItemId, Properties properties) {
        super(properties);
        this.packageItemId = packageItemId;
    }

    public Item getPackageItem() {
        return ForgeRegistries.ITEMS.getValue(packageItemId);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        Item packageItem = getPackageItem();
        if (packageItem != null && user instanceof Player player) {
            if (stack.getCount() == 1) {
                player.setItemInHand(player.getUsedItemHand(), packageItem.getDefaultInstance());
            } else {
                player.getInventory().add(packageItem.getDefaultInstance());
            }
        }
        return super.finishUsingItem(stack, level, user);
    }
}
