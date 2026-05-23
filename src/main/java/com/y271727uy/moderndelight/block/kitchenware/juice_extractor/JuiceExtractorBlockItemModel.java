package com.y271727uy.moderndelight.block.kitchenware.juice_extractor;

import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class JuiceExtractorBlockItemModel extends DefaultedItemGeoModel<JuiceExtractorBlockItem> {
    public JuiceExtractorBlockItemModel() {
        super(new ResourceLocation(ModernDelightMain.MOD_ID,"juice_extractor_item"));
    }

    @Override
    public ResourceLocation getModelResource(JuiceExtractorBlockItem animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID,"geo/juice_extractor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(JuiceExtractorBlockItem animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "textures/block/juice_extractor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(JuiceExtractorBlockItem animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "animations/juice_extractor.animation.json");
    }
}
