package com.y271727uy.moderndelight.block.food.fish_and_chips;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FishAndChipsBlockEntityRender extends GeoBlockRenderer<FishAndChipsBlockEntity> {
    public FishAndChipsBlockEntityRender(BlockEntityRendererProvider.Context context) {
        super(new FishAndChipsBlockEntityModel());
    }

}
