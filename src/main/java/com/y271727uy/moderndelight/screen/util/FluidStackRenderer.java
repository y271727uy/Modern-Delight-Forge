package com.y271727uy.moderndelight.screen.util;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.util.FluidStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

import java.util.ArrayList;
import java.util.List;

/**
 * Originally by Flandre923
 **/
public class FluidStackRenderer {
    // 容积
    public final long capacityDrop;
    private final int width;
    private final int height;

    public FluidStackRenderer(){
        this(FluidStack.convertDropletsToMb(8100L),16,16);
    }

    public FluidStackRenderer(long capacityDrop, int width, int height){
        Preconditions.checkArgument(capacityDrop >0,"capacity must be > 0");
        Preconditions.checkArgument(width>0,"width must be > 0");
        Preconditions.checkArgument(height>0,"height must be > 0");
        this.capacityDrop = capacityDrop;
        this.width = width;
        this.height = height;
    }
    // 绘制流体
    public void drawFluid(GuiGraphics context, FluidStack fluidStack, int x, int y, int width, int height){
        if (fluidStack == null || fluidStack.getVariant().isBlank()){
            return;
        }
        y+=height;
        net.minecraftforge.fluids.FluidType fluidType = fluidStack.stack.getFluid().getFluidType();
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluidType);
        ResourceLocation sprite = extensions.getStillTexture();
        int color = extensions.getTintColor();

        final int drawHeight = (int)(fluidStack.getAmount()/(capacityDrop*1f)*height);
        if (sprite != null) {
            int offsetHeight = drawHeight;
            RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, 1F);
            int iteration = 0;
            while(offsetHeight != 0){
                final int curHeight = Math.min(offsetHeight, height);

                context.blit(sprite, x, y - offsetHeight, 0, 0, width, curHeight, width, curHeight);
                offsetHeight -= curHeight;
                iteration ++;
                if (iteration>50){
                    break;
                }
            }
            RenderSystem.setShaderColor(1f,1f,1f,1f);
        }
    }

    public List<Component> getTooltip(FluidStack fluidStack) {
        // 返回的提示信息
        List<Component> tooltip = new ArrayList<>();
        if (fluidStack == null || fluidStack.getVariant().isBlank()){
            return tooltip;
        }
        // 构建第一个信息是显示的流体的类型是什么
        tooltip.add(Component.translatable(fluidStack.stack.getFluid().getFluidType().getDescriptionId()));

        long amount = fluidStack.getAmount();
        // 显示流体的数据
        tooltip.add(Component.literal(FluidStack.convertDropletsToMb(amount)+" mB"));
        return tooltip;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
