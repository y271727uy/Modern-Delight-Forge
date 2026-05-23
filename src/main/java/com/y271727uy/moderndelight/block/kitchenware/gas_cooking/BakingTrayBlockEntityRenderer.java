package com.y271727uy.moderndelight.block.kitchenware.gas_cooking;

import com.y271727uy.moderndelight.tag.TagKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BakingTrayBlockEntityRenderer implements BlockEntityRenderer<BakingTrayBlockEntity> {
    public BakingTrayBlockEntityRenderer(BlockEntityRendererProvider.Context context){
    }

    @Override
    public void render(BakingTrayBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack1 = entity.getRendererStack1();
        ItemStack stack2 = entity.getRendererStack2();
        ItemStack stack3 = entity.getRendererStack3();
        ItemStack stack4 = entity.getRendererStack4();

        if (!isFlat(stack1.getItem())){
            matrices.pushPose();
            matrices.translate(0.25f, 0.15f, 0.25f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            matrices.mulPose(Axis.YP.rotationDegrees(180));
            matrices.mulPose(Axis.XP.rotationDegrees(270));
            itemRenderer.renderStatic(stack1, ItemDisplayContext.GUI, getLightLevel(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos()), OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.getLevel(), 1);
            matrices.popPose();

            matrices.pushPose();
            matrices.translate(0.25f, 0.15f, 0.75f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            matrices.mulPose(Axis.YP.rotationDegrees(270));
            matrices.mulPose(Axis.XP.rotationDegrees(270));
            itemRenderer.renderStatic(stack2, ItemDisplayContext.GUI, getLightLevel(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos()), OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.getLevel(), 1);
            matrices.popPose();

            matrices.pushPose();
            matrices.translate(0.75f, 0.15f, 0.25f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            matrices.mulPose(Axis.YP.rotationDegrees(90));
            matrices.mulPose(Axis.XP.rotationDegrees(270));
            itemRenderer.renderStatic(stack3, ItemDisplayContext.GUI, getLightLevel(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos()), OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.getLevel(), 1);
            matrices.popPose();

            matrices.pushPose();
            matrices.translate(0.75f, 0.15f, 0.75f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            matrices.mulPose(Axis.XP.rotationDegrees(270));
            itemRenderer.renderStatic(stack4, ItemDisplayContext.GUI, getLightLevel(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos()), OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.getLevel(), 1);
            matrices.popPose();
        } else {
            matrices.pushPose();
            matrices.translate(0.5f, 0.25f, 0.5f);
            matrices.scale(1.0f, 1.0f, 1.0f);
            itemRenderer.renderStatic(stack1, ItemDisplayContext.FIXED, getLightLevel(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos()), OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.getLevel(), 1);
            matrices.popPose();

            matrices.pushPose();
            matrices.translate(0.25f, 0.3f, 0.5f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            matrices.mulPose(Axis.XP.rotationDegrees(270));
            itemRenderer.renderStatic(stack2, ItemDisplayContext.GUI, getLightLevel(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos()), OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.getLevel(), 1);
            matrices.popPose();

            matrices.pushPose();
            matrices.translate(0.75f, 0.3f, 0.25f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            matrices.mulPose(Axis.YP.rotationDegrees(90));
            matrices.mulPose(Axis.XP.rotationDegrees(270));
            itemRenderer.renderStatic(stack3, ItemDisplayContext.GUI, getLightLevel(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos()), OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.getLevel(), 1);
            matrices.popPose();

            matrices.pushPose();
            matrices.translate(0.75f, 0.3f, 0.75f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            matrices.mulPose(Axis.XP.rotationDegrees(270));
            itemRenderer.renderStatic(stack4, ItemDisplayContext.GUI, getLightLevel(Objects.requireNonNull(entity.getLevel()), entity.getBlockPos()), OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.getLevel(), 1);
            matrices.popPose();
        }
    }

    private boolean isFlat(Item item) {
        List<Item> items = new ArrayList<>();
        for (var registryEntry : ForgeRegistries.ITEMS.tags().getTag(TagKeys.FLAT_ON_BAKING_TRAY)){
            items.add(registryEntry);
        }
        return items.contains(item);
    }

    private int getLightLevel(Level world, BlockPos pos){
        int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = world.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }
}
