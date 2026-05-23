package com.y271727uy.moderndelight.block.kitchenware.ice_cream_maker;

import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class IceCreamMakerBlockItemModel extends DefaultedItemGeoModel<IceCreamMakerBlockItem> {
    public IceCreamMakerBlockItemModel() {
        super(new ResourceLocation(ModernDelightMain.MOD_ID,"ice_cream_maker_item"));
    }

    @Override
    public ResourceLocation getModelResource(IceCreamMakerBlockItem animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID,"geo/ice_cream_maker.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(IceCreamMakerBlockItem animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "textures/block/ice_cream_maker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(IceCreamMakerBlockItem animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "animations/ice_cream_maker.animation.json");
    }
}
