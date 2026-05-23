package com.y271727uy.moderndelight.block.kitchenware.decor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.Objects;

public class WoodenPlateBlockEntityRenderer implements BlockEntityRenderer<WoodenPlateBlockEntity> {
    public WoodenPlateBlockEntityRenderer(BlockEntityRendererProvider.Context context){
    }

    @Override
    public void render(WoodenPlateBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack renderStack = entity.getRenderStack();
        int rotate = entity.getRotate();
        matrices.pushPose();
        matrices.translate(0.5f, 0.1f, 0.5f);
        matrices.scale(0.8f, 0.8f, 0.8f);
        matrices.mulPose(Axis.XP.rotationDegrees(90));
        matrices.mulPose(Axis.ZP.rotationDegrees(rotate));
        itemRenderer.renderStatic(renderStack, ItemDisplayContext.FIXED, getLightLevel(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos()), OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.getLevel(), 1);
        matrices.popPose();
    }

    private int getLightLevel(Level world, BlockPos pos){
        int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = world.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }
}
