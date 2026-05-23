package com.y271727uy.moderndelight.block.power.alternator.thermal_power;

import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class SterlingEngineBlockItemModel extends DefaultedItemGeoModel<SterlingEngineBlockItem> {
    public SterlingEngineBlockItemModel() {
        super(new ResourceLocation(ModernDelightMain.MOD_ID,"sterling_engine_item"));
    }

    @Override
    public ResourceLocation getModelResource(SterlingEngineBlockItem animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID,"geo/sterling_engine.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SterlingEngineBlockItem animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "textures/block/sterling_engine.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SterlingEngineBlockItem animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "animations/sterling_engine.animation.json");
    }
}
