package com.y271727uy.moderndelight.item.food;

import com.y271727uy.moderndelight.util.enums.CreamFlavor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CreamItem extends PackagedItem {
    public CreamItem(CreamFlavor creamFlavor, Item.Properties properties) {
        super(new ResourceLocation("minecraft", "bowl"), properties);
        this.creamFlavor = creamFlavor;
    }

    public CreamFlavor getFlavor() {
        return creamFlavor;
    }

    private final CreamFlavor creamFlavor;
}
