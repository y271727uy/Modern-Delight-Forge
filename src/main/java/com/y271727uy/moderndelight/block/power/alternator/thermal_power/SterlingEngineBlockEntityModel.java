package com.y271727uy.moderndelight.block.power.alternator.thermal_power;

import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class SterlingEngineBlockEntityModel extends DefaultedBlockGeoModel<SterlingEngineBlockEntity> {
    public SterlingEngineBlockEntityModel() {
        super(new ResourceLocation(ModernDelightMain.MOD_ID,"sterling_engine"));
    }

    @Override
    public ResourceLocation getModelResource(SterlingEngineBlockEntity animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID,"geo/sterling_engine.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SterlingEngineBlockEntity animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "textures/block/sterling_engine.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SterlingEngineBlockEntity animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "animations/sterling_engine.animation.json");
    }
}
