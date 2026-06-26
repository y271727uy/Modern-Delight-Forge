package com.y271727uy.moderndelight.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.item.tools.HolderItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ModernDelightItemRenderer extends BlockEntityWithoutLevelRenderer {
    public ModernDelightItemRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;

        if (stack.is(ModItems.HOLDER.get())) {
            renderItemModel(ModItems.HOLDER_BASE.get().getDefaultInstance(), poseStack, buffer, light, overlay, level);
            renderHolderContents(stack, displayContext, poseStack, buffer, light, overlay, level);
            return;
        }

        renderItemModel(stack, poseStack, buffer, light, overlay, level);
    }

    private void renderHolderContents(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, Level level) {
        ItemStack holdingStack = HolderItem.getHoldingStack(stack);
        if (!holdingStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.2D, 0.2D, 0.015D);
            poseStack.scale(0.75F, 0.75F, 0.75F);
            renderItemModel(holdingStack, poseStack, buffer, light, overlay, level);
            poseStack.popPose();
        }

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0D, 0.03D);
        renderItemModel(ModItems.HOLDER_UP.get().getDefaultInstance(), poseStack, buffer, light, overlay, level);
        poseStack.popPose();
    }

    private void renderItemModel(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, Level level) {
        Minecraft minecraft = Minecraft.getInstance();
        BakedModel model = minecraft.getItemRenderer().getModel(stack, level, null, 0);
        RenderType renderType = ItemBlockRenderTypes.getRenderType(stack, true);
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, renderType, true, stack.hasFoil());
        minecraft.getItemRenderer().renderModelLists(model, stack, light, OverlayTexture.NO_OVERLAY, poseStack, vertexConsumer);
    }
}


