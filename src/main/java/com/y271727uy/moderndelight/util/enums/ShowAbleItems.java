package com.y271727uy.moderndelight.util.enums;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.util.StringRepresentable;

public enum ShowAbleItems implements StringRepresentable {
    EMPTY("empty",getKey(Items.AIR)),
    POTATO("potato",getKey(Items.POTATO)),
    POISONOUS_POTATO("poisonous_potato",getKey(Items.POISONOUS_POTATO)),
    SWEET_BERRIES("sweet_berries",getKey(Items.SWEET_BERRIES)),
    COD("cod",getKey(Items.COD)),
    COOKED_COD("cooked_cod",getKey(Items.COOKED_COD)),
    SALMON("salmon",getKey(Items.SALMON)),
    COOKED_SALMON("cooked_salmon",getKey(Items.COOKED_SALMON)),
    APPLE("apple",getKey(Items.APPLE)),
    GOLDEN_APPLE("golden_apple",getKey(Items.GOLDEN_APPLE)),
    ENCHANTED_GOLDEN_APPLE("enchanted_golden_apple",getKey(Items.ENCHANTED_GOLDEN_APPLE));

    final String name;
    final ResourceLocation id;
    
    ShowAbleItems(String name, ResourceLocation id) {
        this.name = name;
        this.id = id;
    }
    
    public static ResourceLocation getKey(Item item){
        return ForgeRegistries.ITEMS.getKey(item);
    }
    
    public static ShowAbleItems getValue(Item item){
        ResourceLocation identifier = ForgeRegistries.ITEMS.getKey(item);
        for (ShowAbleItems showAbleItems : ShowAbleItems.values()){
            if (showAbleItems.id.toString().equals(identifier.toString())){
                return showAbleItems;
            }
        }
        return EMPTY;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
