package com.y271727uy.moderndelight.block.kitchenware.juice_extractor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class JuiceExtractorBlockEntityRender extends GeoBlockRenderer<JuiceExtractorBlockEntity> {
    public JuiceExtractorBlockEntityRender(BlockEntityRendererProvider.Context context) {
        super(new JuiceExtractorBlockEntityModel());
    }

    @Override
    public void defaultRender(
            PoseStack poseStack,
            JuiceExtractorBlockEntity entity,
            MultiBufferSource bufferSource,
            RenderType renderType,
            com.mojang.blaze3d.vertex.VertexConsumer vertexConsumer,
            float yaw,
            float partialTick,
            int packedLight
    ) {
        super.defaultRender(poseStack, entity, bufferSource, renderType, vertexConsumer, yaw, partialTick, packedLight);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        for (int i = 0; i < 4; i++) {
            ItemStack item = entity.getItem(i);
            if (item.isEmpty()) continue;
            poseStack.pushPose();
            switch (i) {
                case 0 -> {
                    poseStack.translate(0.4f, 0.45f, 0.4f);
                    poseStack.mulPose(Axis.ZP.rotationDegrees(280));
                    poseStack.mulPose(Axis.YP.rotationDegrees(130));
                }
                case 1 -> {
                    poseStack.translate(0.4f, 0.5f, 0.6f);
                    poseStack.mulPose(Axis.ZP.rotationDegrees(190));
                    poseStack.mulPose(Axis.YP.rotationDegrees(160));
                }
                case 2 -> {
                    poseStack.translate(0.6f, 0.45f, 0.4f);
                    poseStack.mulPose(Axis.ZP.rotationDegrees(260));
                    poseStack.mulPose(Axis.YP.rotationDegrees(200));
                }
                case 3 -> {
                    poseStack.translate(0.6f, 0.5f, 0.6f);
                    poseStack.mulPose(Axis.ZP.rotationDegrees(185));
                    poseStack.mulPose(Axis.YP.rotationDegrees(60));
                }
            }
            poseStack.scale(0.4f, 0.4f, 0.4f);
            itemRenderer.renderStatic(item, ItemDisplayContext.GUI, getLightLevel(entity.getLevel(), entity.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.getLevel(), 1);
            poseStack.popPose();
        }
    }

    private int getLightLevel(Level world, BlockPos pos) {
        int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = world.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }
}
