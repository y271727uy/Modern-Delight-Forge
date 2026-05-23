package com.y271727uy.moderndelight.block.kitchenware.juice_extractor;

import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class JuiceExtractorBlockEntityModel extends DefaultedBlockGeoModel<JuiceExtractorBlockEntity> {
    public JuiceExtractorBlockEntityModel() {
        super(new ResourceLocation(ModernDelightMain.MOD_ID,"juice_extractor"));

    }

    @Override
    public ResourceLocation getModelResource(JuiceExtractorBlockEntity animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID,"geo/juice_extractor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(JuiceExtractorBlockEntity animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "textures/block/juice_extractor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(JuiceExtractorBlockEntity animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "animations/juice_extractor.animation.json");
    }
    @Override
    public RenderType getRenderType(JuiceExtractorBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(this.getTextureResource(animatable));
    }
}
