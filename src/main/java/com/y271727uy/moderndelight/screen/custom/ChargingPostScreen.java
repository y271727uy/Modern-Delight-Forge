package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

public class ChargingPostScreen extends AbstractContainerScreen<ChargingPostScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/charging_post_gui.png");
    public ChargingPostScreen(ChargingPostScreenHandler handler, Inventory inventory, Component title) {
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

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth,imageHeight);
        renderProgressArrow(guiGraphics, x, y);
        renderPowerLevel(guiGraphics,x,y);
        b = mouseX >= x + 7 && mouseY >= y + 16 && mouseX <= x + 24 && mouseY <= y + 70;
    }
    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isWorking()){
            guiGraphics.blit(TEXTURE, x + 62, y + 40, 0, 166, menu.getScaledProgress(), 15);
        }
    }
    private void renderPowerLevel(GuiGraphics guiGraphics, int x, int y) {
        if (menu.getPower() != 0){
            int offset = 53 - menu.getScaledPower();
            guiGraphics.blit(TEXTURE, x + 8, y + 17 + offset, 176, offset,16, menu.getScaledPower());
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (b){
            guiGraphics.renderTooltip(font,Component.literal(String.valueOf(menu.getPower() + " FE")),x,y);
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
