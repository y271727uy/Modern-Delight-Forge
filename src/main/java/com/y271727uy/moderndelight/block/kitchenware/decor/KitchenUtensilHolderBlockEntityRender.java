package com.y271727uy.moderndelight.block.kitchenware.decor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.Objects;

public class KitchenUtensilHolderBlockEntityRender implements BlockEntityRenderer<KitchenUtensilHolderBlockEntity> {
    public KitchenUtensilHolderBlockEntityRender(BlockEntityRendererProvider.Context context){
    }

    @Override
    public void render(KitchenUtensilHolderBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        NonNullList<ItemStack> items = entity.getItems();

        Direction dir = entity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        switch(dir) {
            case NORTH:
                //1
                int i = 0;
                while(i < 4) {
                    matrices.pushPose();
                    matrices.translate(0.8f - i * 0.2f, 0.65f, 0.9f);
                    matrices.scale(0.35f, 0.38f, 0.35f);
                    matrices.mulPose(Axis.XP.rotationDegrees(180));
                    matrices.mulPose(Axis.ZP.rotationDegrees(30));
                    BlockPos pos = new BlockPos(entity.getBlockPos().getX(), entity.getBlockPos().getY() + 1, entity.getBlockPos().getZ());
                    itemRenderer.renderStatic(items.get(i), ItemDisplayContext.GUI, getLightLevel(Objects.requireNonNull(entity.getLevel()), pos), OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.getLevel(), 1);
                    matrices.popPose();
                    i++;
                }
                break;
            case SOUTH:
                // 2
                int j = 0;
                while(j < 4) {
                    matrices.pushPose();
                    matrices.translate(0.2f + j * 0.2f, 0.65f, 0.1f);
                    matrices.scale(0.35f, 0.4f, 0.35f);
                    matrices.mulPose(Axis.XP.rotationDegrees(180));
                    matrices.mulPose(Axis.ZP.rotationDegrees(60));
                    BlockPos pos = new BlockPos(entity.getBlockPos().getX(), entity.getBlockPos().getY() + 1, entity.getBlockPos().getZ());
                    itemRenderer.renderStatic(items.get(j), ItemDisplayContext.GUI, getLightLevel(Objects.requireNonNull(entity.getLevel()), pos), OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.getLevel(), 1);
                    matrices.popPose();
                    j++;
                }
                break;
            case EAST:
                //3
                int u = 0;
                while(u < 4) {
                    matrices.pushPose();
                    matrices.translate(0.1, 0.65f, 0.8f - u * 0.2f);
                    matrices.scale(0.35f, 0.4f, 0.35f);
                    matrices.mulPose(Axis.XP.rotationDegrees(115));
                    matrices.mulPose(Axis.YP.rotationDegrees(270));
                    BlockPos pos = new BlockPos(entity.getBlockPos().getX(), entity.getBlockPos().getY() + 1, entity.getBlockPos().getZ());
                    itemRenderer.renderStatic(items.get(u), ItemDisplayContext.GUI, getLightLevel(Objects.requireNonNull(entity.getLevel()), pos), OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.getLevel(), 1);
                    matrices.popPose();
                    u++;
                }
                break;
            case WEST:
                //4
                int k = 0;
                while(k < 4) {
                    matrices.pushPose();
                    matrices.translate(0.9f, 0.65f, 0.2f + k * 0.2f);
                    matrices.scale(0.35f, 0.4f, 0.35f);
                    matrices.mulPose(Axis.XP.rotationDegrees(145));
                    matrices.mulPose(Axis.YP.rotationDegrees(270));
                    BlockPos pos = new BlockPos(entity.getBlockPos().getX(), entity.getBlockPos().getY() + 1, entity.getBlockPos().getZ());
                    itemRenderer.renderStatic(items.get(k), ItemDisplayContext.GUI, getLightLevel(Objects.requireNonNull(entity.getLevel()), pos), OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.getLevel(), 1);
                    matrices.popPose();
                    k++;
                }
                break;
        }
    }
    
    private int getLightLevel(Level world, BlockPos pos){
        int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = world.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }
}
