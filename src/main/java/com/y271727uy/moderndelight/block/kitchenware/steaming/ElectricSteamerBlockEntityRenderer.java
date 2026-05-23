package com.y271727uy.moderndelight.block.kitchenware.steaming;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class ElectricSteamerBlockEntityRenderer implements BlockEntityRenderer<ElectricSteamerBlockEntity> {
    public ElectricSteamerBlockEntityRenderer(BlockEntityRendererProvider.Context context){

    }

    @Override
    public void render(ElectricSteamerBlockEntity entity, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        NonNullList<ItemStack> inv = entity.getItems();
        for (int i = 0; i < inv.size(); i++) {
            poseStack.pushPose();
            float y = (i >= 4) ? ((i >= 8)? 0.4375f : 0.625f ) : 0.8125f;
            float x = (i % 4 < 2) ? 0.4375f : 0.625f;
            float z = (i % 2 == 0) ? 0.625f : 0.4375f;

            poseStack.translate(x, y, z);
            poseStack.scale(0.15f, 0.15f, 0.15f);

            itemRenderer.renderStatic(
                    inv.get(i),
                    ItemDisplayContext.GUI,
                    getLightLevel(entity.getLevel(), entity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    bufferSource,
                    entity.getLevel(),
                    1
            );
            poseStack.popPose();
        }
    }

    private int getLightLevel(Level world, BlockPos pos){
        int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = world.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight,skyLight);
    }
}
