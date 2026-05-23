package com.y271727uy.moderndelight.block.power;

import com.y271727uy.moderndelight.block.ModBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

import java.util.Objects;

public class ChargingPostBlockEntityRenderer implements BlockEntityRenderer<ChargingPostBlockEntity> {
    public ChargingPostBlockEntityRenderer(BlockEntityRendererProvider.Context context){

    }

    @Override
    public void render(ChargingPostBlockEntity entity, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = entity.getRendererStack();
        Direction dir = entity.getBlockState().getValue(ChargingPostBlock.FACING);
        
        if (stack.is(ModBlocks.CHARGING_POST.get().asItem())){
            poseStack.pushPose();
            switch (dir){
                case NORTH -> poseStack.translate(0.5f, 0.25f, 0.25f);
                case EAST -> poseStack.translate(0.75f, 0.25f, 0.5f);
                case WEST -> poseStack.translate(0.25f, 0.25f, 0.5f);
                case SOUTH -> poseStack.translate(0.5f, 0.25f, 0.75f);
            }
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180));
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, 
                    getLightLevel(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos()), 
                    OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.getLevel(), 1);
            poseStack.popPose();
        } else {
            poseStack.pushPose();
            switch (dir){
                case NORTH -> poseStack.translate(0.5f, 0.25f, 0.25f);
                case EAST -> poseStack.translate(0.75f, 0.25f, 0.5f);
                case WEST -> poseStack.translate(0.25f, 0.25f, 0.5f);
                case SOUTH -> poseStack.translate(0.5f, 0.25f, 0.75f);
            }
            poseStack.scale(0.3f, 0.3f, 0.3f);
            poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180));
            itemRenderer.renderStatic(stack, ItemDisplayContext.GUI, 
                    getLightLevel(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos()), 
                    OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.getLevel(), 1);
            poseStack.popPose();
        }
    }

    private int getLightLevel(Level world, BlockPos pos){
        int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = world.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }
}
