package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

public class ElectricSteamerScreen extends AbstractContainerScreen<ElectricSteamerScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/electric_steamer_gui.png");

    public ElectricSteamerScreen(ElectricSteamerScreenHandler handler, Inventory inventory, Component title) {
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

        if (menu.isWorking()){
            guiGraphics.blit(TEXTURE,x+4,y+4,176,0,25,12);
        }
        if (mouseX >= x + 161 && mouseX <= x + 171 && mouseY >= y + 5 && mouseY <= y + 15){
            guiGraphics.blit(TEXTURE,x+161,y+5,194,13,11,11);
        }
        renderProgressArrow(guiGraphics,x + 54,y + 8,0);
        renderProgressArrow(guiGraphics,x + 72,y + 8,1);
        renderProgressArrow(guiGraphics,x + 36,y + 26,2);
        renderProgressArrow(guiGraphics,x + 54,y + 26,3);
        renderProgressArrow(guiGraphics,x + 72,y + 26,4);
        renderProgressArrow(guiGraphics,x + 90,y + 26,5);
        renderProgressArrow(guiGraphics,x + 36,y + 44,6);
        renderProgressArrow(guiGraphics,x + 54,y + 44,7);
        renderProgressArrow(guiGraphics,x + 72,y + 44,8);
        renderProgressArrow(guiGraphics,x + 90,y + 44,9);
        renderProgressArrow(guiGraphics,x + 54,y + 62,10);
        renderProgressArrow(guiGraphics,x + 72,y + 62,11);

        renderSteamProgressArrow(guiGraphics,x,y);
        renderSteamArrow(guiGraphics,x,y);
        renderWaterArrow(guiGraphics,x,y);
        b = mouseX >= x + 152 && mouseY >= y + 18 && mouseX <= x + 167 && mouseY <= y + 58;
    }
    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y, int slot) {
        if (menu.getScaledProgress(slot) != 0){
            guiGraphics.blit(TEXTURE, x, y, 176, 123,menu.getScaledProgress(slot),16);
        }
    }
    private void renderSteamProgressArrow(GuiGraphics guiGraphics, int x, int y){
        if (menu.getScaledSteamProgress() != 0){
            int offset = 24 - menu.getScaledSteamProgress();
            guiGraphics.blit(TEXTURE,x+126+offset,y+36,194+offset,24,menu.getScaledSteamProgress(),9);
        }
    }
    private void renderSteamArrow(GuiGraphics guiGraphics, int x, int y){
        if (menu.getScaledSteam() != 0){
            int offset = 70 - menu.getScaledSteam();
            guiGraphics.blit(TEXTURE,x+111,y+9+offset,176,53+offset,14,menu.getScaledSteam());
        }
    }
    private void renderWaterArrow(GuiGraphics guiGraphics, int x, int y){
        if (menu.getScaledWater() != 0){
            int offset = 41 - menu.getScaledWater();
            guiGraphics.blit(TEXTURE,x+152,y+18+offset,176,12+offset,16,menu.getScaledWater());
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (b){
            guiGraphics.renderTooltip(font,Component.literal(menu.getWaterAmount()+" mB"),x,y);
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
