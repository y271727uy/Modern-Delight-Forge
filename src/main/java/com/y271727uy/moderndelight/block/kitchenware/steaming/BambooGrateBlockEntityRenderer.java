package com.y271727uy.moderndelight.block.kitchenware.steaming;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.y271727uy.moderndelight.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

import java.util.ArrayList;
import java.util.List;

public class BambooGrateBlockEntityRenderer implements BlockEntityRenderer<BambooGrateBlockEntity> {
    public BambooGrateBlockEntityRenderer(BlockEntityRendererProvider.Context context){
    }
    
    @Override
    public void render(BambooGrateBlockEntity entity, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = ModBlocks.BAMBOO_COVER.get().asItem().getDefaultInstance();
        int layer = entity.getBlockState().getValue(BambooGrateBlock.LAYER);
        boolean covered = entity.getBlockState().getValue(BambooGrateBlock.COVERED);
        if (covered){
            poseStack.pushPose();
            switch (layer){
                case 1 -> poseStack.translate(0.5f, 0.5f,0.5f);
                case 2 -> poseStack.translate(0.5f, 0.75f,0.5f);
                case 3 -> poseStack.translate(0.5f, 1.0f,0.5f);
                case 4 -> poseStack.translate(0.5f, 1.25f,0.5f);
            }
            poseStack.scale(2,2,2);
            itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND,
                    getLightLevel(entity.getLevel(),entity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, poseStack, bufferSource,entity.getLevel(),1);
            poseStack.popPose();
        } else {
            if (entity.getLevel()!=null){
                if (layer == 4 && !entity.getLevel().isEmptyBlock(entity.getBlockPos().above())) return;
            } else return;
            List<ItemStack> renderStacks = new ArrayList<>();
            switch (layer){
                case 1 -> {
                    for (int i=0;i<4;i++){
                        renderStacks.add(entity.getItem(i));
                    }
                }
                case 2 -> {
                    for (int i=4;i<8;i++){
                        renderStacks.add(entity.getItem(i));
                    }
                }
                case 3 -> {
                    for (int i=8;i<12;i++){
                        renderStacks.add(entity.getItem(i));
                    }
                }
                case 4 -> {
                    for (int i=12;i<16;i++){
                        renderStacks.add(entity.getItem(i));
                    }
                }
            }
            float y = (float) (0.15 + 0.25 * (layer - 1));

            poseStack.pushPose();
            poseStack.translate(0.3f, y,0.3f);
            poseStack.scale(0.3f,0.3f,0.3f);
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
            poseStack.mulPose(Axis.XP.rotationDegrees(270));
            itemRenderer.renderStatic(renderStacks.get(0), ItemDisplayContext.GUI,
                    getLightLevel(entity.getLevel(),entity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, poseStack, bufferSource,entity.getLevel(),1);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0.3f, y,0.7f);
            poseStack.scale(0.3f,0.3f,0.3f);
            poseStack.mulPose(Axis.YP.rotationDegrees(270));
            poseStack.mulPose(Axis.XP.rotationDegrees(270));
            itemRenderer.renderStatic(renderStacks.get(1), ItemDisplayContext.GUI,
                    getLightLevel(entity.getLevel(),entity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, poseStack, bufferSource,entity.getLevel(),1);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0.7f, y,0.3f);
            poseStack.scale(0.3f,0.3f,0.3f);
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
            poseStack.mulPose(Axis.XP.rotationDegrees(270));
            itemRenderer.renderStatic(renderStacks.get(2), ItemDisplayContext.GUI,
                    getLightLevel(entity.getLevel(),entity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, poseStack, bufferSource,entity.getLevel(),1);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0.7f, y,0.7f);
            poseStack.scale(0.3f,0.3f,0.3f);
            poseStack.mulPose(Axis.XP.rotationDegrees(270));
            itemRenderer.renderStatic(renderStacks.get(3), ItemDisplayContext.GUI,
                    getLightLevel(entity.getLevel(),entity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, poseStack, bufferSource,entity.getLevel(),1);
            poseStack.popPose();
            renderStacks.clear();
        }
    }
    
    private int getLightLevel(Level world, BlockPos pos){
        int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = world.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight,skyLight);
    }
}
