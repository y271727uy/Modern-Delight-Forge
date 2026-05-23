package com.y271727uy.moderndelight.block.power.alternator.wind_power;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FanBladeBlockEntityRender extends GeoBlockRenderer<FanBladeBlockEntity> {
    public FanBladeBlockEntityRender(BlockEntityRendererProvider.Context context) {
        super(new FanBladeBlockEntityModel());
    }
}
