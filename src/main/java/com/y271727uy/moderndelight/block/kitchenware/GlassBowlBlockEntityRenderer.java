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

public class GlassBowlBlockEntityRenderer implements BlockEntityRenderer<GlassBowlBlockEntity> {
    public GlassBowlBlockEntityRenderer(BlockEntityRendererProvider.Context context)                         {}
    @Override
    public void render(GlassBowlBlockEntity entity, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = entity.getRendererStack();
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.15f,0.5f);
        poseStack.scale(0.35f,0.35f,0.35f);
        poseStack.mulPose(Axis.XP.rotationDegrees(220));
        itemRenderer.renderStatic(stack, ItemDisplayContext.GUI, getLightLevel(Objects.requireNonNull(entity.getLevel()),entity.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, bufferSource,entity.getLevel(),1);
        poseStack.popPose();
    }
    private int getLightLevel(Level world, BlockPos pos){
        int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = world.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight,skyLight);
    }
}

