package com.y271727uy.moderndelight.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.item.tools.HolderItem;
import com.y271727uy.moderndelight.item.tools.StoneMortarItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
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
        minecraft.getItemRenderer().renderStatic(stack, displayContext, light, overlay, poseStack, buffer, level, 0);

        if (stack.is(ModItems.STONE_MORTAR.get())) {
            renderStoneMortarContents(stack, displayContext, poseStack, buffer, light, overlay, level);
            return;
        }

        if (stack.is(ModItems.HOLDER.get())) {
            renderHolderContents(stack, displayContext, poseStack, buffer, light, overlay, level);
        }
    }

    private void renderStoneMortarContents(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, Level level) {
        ItemStack insideStack = StoneMortarItem.getInsideStack(stack);
        if (insideStack.isEmpty()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        poseStack.pushPose();
        poseStack.translate(-0.2D, 0.25D, 0.015D);
        poseStack.scale(0.75F, 0.75F, 0.75F);
        minecraft.getItemRenderer().renderStatic(insideStack, displayContext, light, overlay, poseStack, buffer, level, 0);
        poseStack.popPose();
    }

    private void renderHolderContents(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, Level level) {
        Minecraft minecraft = Minecraft.getInstance();
        ItemStack holdingStack = HolderItem.getHoldingStack(stack);
        if (!holdingStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.2D, 0.2D, 0.015D);
            poseStack.scale(0.75F, 0.75F, 0.75F);
            minecraft.getItemRenderer().renderStatic(holdingStack, displayContext, light, overlay, poseStack, buffer, level, 0);
            poseStack.popPose();
        }

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0D, 0.03D);
        minecraft.getItemRenderer().renderStatic(ModItems.HOLDER_UP.get().getDefaultInstance(), displayContext, light, overlay, poseStack, buffer, level, 0);
        poseStack.popPose();
    }
}


