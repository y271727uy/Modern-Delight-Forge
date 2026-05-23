package com.y271727uy.moderndelight.block.kitchenware.ice_cream_maker;

import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class IceCreamMakerBlockEntityModel extends DefaultedBlockGeoModel<IceCreamMakerBlockEntity> {
    public IceCreamMakerBlockEntityModel() {
        super(new ResourceLocation(ModernDelightMain.MOD_ID,"ice_cream_maker"));
    }

    @Override
    public ResourceLocation getModelResource(IceCreamMakerBlockEntity animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID,"geo/ice_cream_maker.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(IceCreamMakerBlockEntity animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "textures/block/ice_cream_maker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(IceCreamMakerBlockEntity animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "animations/ice_cream_maker.animation.json");
    }
}
