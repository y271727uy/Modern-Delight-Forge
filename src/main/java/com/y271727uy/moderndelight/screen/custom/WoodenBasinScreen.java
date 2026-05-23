package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying.WoodenBasinBlockEntity;
import com.y271727uy.moderndelight.screen.util.FluidStackRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class WoodenBasinScreen extends AbstractContainerScreen<WoodenBasinScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/wooden_basin_gui.png");
    public WoodenBasinScreen(WoodenBasinScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }
    private FluidStackRenderer fluidStackRenderer;
    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
        assignFluidStackRenderer();
    }
    boolean b;
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0,TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth,imageHeight);

        renderFluid(guiGraphics, x, y);
        b = mouseX >= x + 46 && mouseX <= x + 65 && mouseY >= y + 20 && mouseY <= y + 68;
        if (mouseX >= x + 161 && mouseX <= x + 171 && mouseY >= y + 5 && mouseY <= y + 15){
            guiGraphics.blit(TEXTURE,x+161,y+5,194,13,11,11);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (b){
            guiGraphics.renderComponentTooltip(font,fluidStackRenderer.getTooltip(menu.blockEntity.getFluidStackCopy()),
                    x,y);
        }
        super.renderTooltip(guiGraphics, x, y);
    }
    private void assignFluidStackRenderer(){
        fluidStackRenderer = new FluidStackRenderer(WoodenBasinBlockEntity.MAX_FLUID_LEVEL,18,47);
    }
    private void renderFluid(GuiGraphics guiGraphics, int x, int y) {
        fluidStackRenderer.drawFluid(guiGraphics,menu.blockEntity.getFluidStackCopy(),
                x+47,y+21,18,47);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
