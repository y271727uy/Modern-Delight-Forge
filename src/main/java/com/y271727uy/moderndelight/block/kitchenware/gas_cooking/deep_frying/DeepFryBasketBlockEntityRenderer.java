package com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.client.renderer.texture.OverlayTexture;

import java.util.Objects;

public class DeepFryBasketBlockEntityRenderer implements BlockEntityRenderer<DeepFryBasketBlockEntity>{
    public DeepFryBasketBlockEntityRenderer(BlockEntityRendererProvider.Context context){
    }
    
    @Override
    public void render(DeepFryBasketBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        NonNullList<ItemStack> items = entity.getItems();
        var direction = entity.getBlockState().getValue(DeepFryBasketBlock.FACING);

        matrices.pushPose();
        rotateToFacing(matrices, direction);
        matrices.translate(0.35f, 0.3f,0.65f);
        matrices.scale(0.3f,0.3f,0.3f);
        matrices.mulPose(Axis.YP.rotationDegrees(200));
        matrices.mulPose(Axis.XP.rotationDegrees(220));
        itemRenderer.renderStatic(items.get(0), ItemDisplayContext.GUI,
                getLightLevel(Objects.requireNonNull(entity.getLevel()),entity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, matrices, vertexConsumers,entity.getLevel(),1);
        matrices.popPose();

        matrices.pushPose();
        rotateToFacing(matrices, direction);
        matrices.translate(0.35f, 0.3f,0.35f);
        matrices.scale(0.3f,0.3f,0.3f);
        matrices.mulPose(Axis.YP.rotationDegrees(120));
        matrices.mulPose(Axis.XP.rotationDegrees(60));
        itemRenderer.renderStatic(items.get(1), ItemDisplayContext.GUI,
                getLightLevel(Objects.requireNonNull(entity.getLevel()),entity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, matrices, vertexConsumers,entity.getLevel(),1);
        matrices.popPose();

        matrices.pushPose();
        rotateToFacing(matrices, direction);
        matrices.translate(0.65f, 0.3f,0.35f);
        matrices.scale(0.3f,0.3f,0.3f);
        matrices.mulPose(Axis.YP.rotationDegrees(20));
        matrices.mulPose(Axis.XP.rotationDegrees(230));
        itemRenderer.renderStatic(items.get(2), ItemDisplayContext.GUI,
                getLightLevel(Objects.requireNonNull(entity.getLevel()),entity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, matrices, vertexConsumers,entity.getLevel(),1);
        matrices.popPose();

        matrices.pushPose();
        rotateToFacing(matrices, direction);
        matrices.translate(0.65f, 0.3f,0.65f);
        matrices.scale(0.3f,0.3f,0.3f);
        matrices.mulPose(Axis.YP.rotationDegrees(250));
        matrices.mulPose(Axis.XP.rotationDegrees(30));
        itemRenderer.renderStatic(items.get(3), ItemDisplayContext.GUI,
                getLightLevel(Objects.requireNonNull(entity.getLevel()),entity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, matrices, vertexConsumers,entity.getLevel(),1);
        matrices.popPose();
    }
    
    private int getLightLevel(Level world, BlockPos pos){
        int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = world.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight,skyLight);
    }

    private void rotateToFacing(PoseStack matrices, net.minecraft.core.Direction direction) {
        matrices.translate(0.5f, 0.0f, 0.5f);
        matrices.mulPose(Axis.YP.rotationDegrees(switch (direction) {
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
            default -> 0;
        }));
        matrices.translate(-0.5f, 0.0f, -0.5f);
    }
}
