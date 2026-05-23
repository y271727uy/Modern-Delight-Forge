package com.y271727uy.moderndelight.block.power.alternator.wind_power;

import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class FanBladeBlockEntityModel extends DefaultedBlockGeoModel<FanBladeBlockEntity> {
    public FanBladeBlockEntityModel() {
        super(new ResourceLocation(ModernDelightMain.MOD_ID,"fan_blade"));
    }
    
    @Override
    public ResourceLocation getModelResource(FanBladeBlockEntity animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID,"geo/fan_blade.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FanBladeBlockEntity animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "textures/block/fan_blade.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FanBladeBlockEntity animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "animations/fan_blade.animation.json");
    }
}
