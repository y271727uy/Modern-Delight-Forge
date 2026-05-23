package com.y271727uy.moderndelight.item.tools;

import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class ElectricWhiskItemModel extends DefaultedItemGeoModel<ElectricWhiskItem> {
    public ElectricWhiskItemModel() {
        super(new ResourceLocation(ModernDelightMain.MOD_ID,"electric_whisk"));
    }

    @Override
    public ResourceLocation getModelResource(ElectricWhiskItem animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID,"geo/electric_whisk.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ElectricWhiskItem animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "textures/item/electric_whisk.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ElectricWhiskItem animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "animations/electric_whisk.animation.json");
    }
}
