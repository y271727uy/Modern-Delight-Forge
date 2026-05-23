package com.y271727uy.moderndelight.block.food.fish_and_chips;

import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class FishAndChipsBlockEntityModel extends DefaultedBlockGeoModel<FishAndChipsBlockEntity> {
    public FishAndChipsBlockEntityModel() {
        super(new ResourceLocation(ModernDelightMain.MOD_ID, "fish_and_chips"));
    }

    @Override
    public ResourceLocation getModelResource(FishAndChipsBlockEntity animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "geo/fish_and_chips.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FishAndChipsBlockEntity animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "textures/block/fish_and_chips.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FishAndChipsBlockEntity animatable) {
        return new ResourceLocation(ModernDelightMain.MOD_ID, "animations/fish_and_chips.animation.json");
    }
}
