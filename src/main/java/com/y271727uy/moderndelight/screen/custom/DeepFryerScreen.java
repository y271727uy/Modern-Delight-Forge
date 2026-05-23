package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

public class DeepFryerScreen extends AbstractContainerScreen<DeepFryerScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/deep_fryer_gui.png");
    public DeepFryerScreen(DeepFryerScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }
    boolean b;
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0,TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow1(guiGraphics, x, y);
        renderProgressArrow2(guiGraphics, x, y);
        renderProgressArrow3(guiGraphics, x, y);
        renderProgressArrow4(guiGraphics, x, y);
        renderOilLevel(guiGraphics,x,y);
        if (menu.isBurning()){
            guiGraphics.blit(TEXTURE, x + 80, y + 48, 0, 167, 18, 11);
        }
        if (mouseX >= x + 161 && mouseX <= x + 171 && mouseY >= y + 5 && mouseY <= y + 15){
            guiGraphics.blit(TEXTURE,x+161,y+5,194,13,11,11);
        }
        b = mouseX >= x + 126 && mouseX <= x + 131 && mouseY >= y + 20 && mouseY <= y + 45;
    }
    private void renderOilLevel(GuiGraphics guiGraphics, int x, int y) {
        if (menu.hasOil()){
            int offset = 24 - menu.getScaledOilLevel();
            guiGraphics.blit(TEXTURE, x + 127, y + 21 + offset,
                    176, offset, 4, menu.getScaledOilLevel());
        }
    }
    private void renderProgressArrow1(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(TEXTURE, x + 54, y + 41, 0, 178, menu.getScaledProgress(0), 4);
    }
    private void renderProgressArrow2(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(TEXTURE, x + 72, y + 41, 0, 178, menu.getScaledProgress(1), 4);
    }
    private void renderProgressArrow3(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(TEXTURE, x + 90, y + 41, 0, 178, menu.getScaledProgress(2), 4);
    }
    private void renderProgressArrow4(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(TEXTURE, x + 108, y + 41, 0, 178, menu.getScaledProgress(3), 4);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (b){
            guiGraphics.renderTooltip(font,Component.literal(menu.getOil()+" mB"),x,y);
        }
        super.renderTooltip(guiGraphics, x, y);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
