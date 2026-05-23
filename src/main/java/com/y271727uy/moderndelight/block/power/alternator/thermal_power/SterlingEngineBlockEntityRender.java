package com.y271727uy.moderndelight.block.power.alternator.thermal_power;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SterlingEngineBlockEntityRender extends GeoBlockRenderer<SterlingEngineBlockEntity> {
    public SterlingEngineBlockEntityRender(BlockEntityRendererProvider.Context context) {
        super(new SterlingEngineBlockEntityModel());
    }
}
