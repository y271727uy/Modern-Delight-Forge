package com.y271727uy.moderndelight.block.kitchenware;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.Objects;

public class FreezerBlockEntityRenderer implements BlockEntityRenderer<FreezerBlockEntity> {
    public FreezerBlockEntityRenderer(BlockEntityRendererProvider.Context context){
    }

    @Override
    public void render(FreezerBlockEntity entity, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack ice_slot = entity.getItem(3);
        ItemStack craft_slot = entity.getItem(4);
        ItemStack slot_1 = entity.getItem(0);
        ItemStack slot_2 = entity.getItem(1);

        poseStack.pushPose();
        poseStack.translate(0.7f, 0.25f,0.3f);
        poseStack.scale(0.4f,0.4f,0.4f);
        poseStack.mulPose(Axis.YP.rotationDegrees(45));
        itemRenderer.renderStatic(ice_slot,
                ItemDisplayContext.GUI,
                getLightLevel(Objects.requireNonNull(entity.getLevel()),entity.getBlockPos()),
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,entity.getLevel(),
                1);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.3f, 0.25f,0.7f);
        poseStack.scale(0.4f,0.4f,0.4f);
        poseStack.mulPose(Axis.YP.rotationDegrees(45));
        itemRenderer.renderStatic(craft_slot,
                ItemDisplayContext.GUI,
                getLightLevel(Objects.requireNonNull(entity.getLevel()),entity.getBlockPos()),
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,entity.getLevel(),
                1);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.3f, 0.7f,0.3f);
        poseStack.scale(0.4f,0.4f,0.4f);
        poseStack.mulPose(Axis.YP.rotationDegrees(45));
        itemRenderer.renderStatic(slot_1,
                ItemDisplayContext.GUI,
                getLightLevel(Objects.requireNonNull(entity.getLevel()),entity.getBlockPos()),
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,entity.getLevel(),
                1);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.7f, 0.7f,0.7f);
        poseStack.scale(0.4f,0.4f,0.4f);
        poseStack.mulPose(Axis.YP.rotationDegrees(45));
        itemRenderer.renderStatic(slot_2,
                ItemDisplayContext.GUI,
                getLightLevel(Objects.requireNonNull(entity.getLevel()),entity.getBlockPos()),
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,entity.getLevel(),
                1);
        poseStack.popPose();
    }
    private int getLightLevel(Level world, BlockPos pos){
        int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = world.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight,skyLight);
    }
}
