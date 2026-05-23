package com.y271727uy.moderndelight.block.kitchenware.ice_cream_maker;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class IceCreamMakerBlockEntityRender extends GeoBlockRenderer<IceCreamMakerBlockEntity> {
    public IceCreamMakerBlockEntityRender(BlockEntityRendererProvider.Context context) {
        super(new IceCreamMakerBlockEntityModel());
    }

}
