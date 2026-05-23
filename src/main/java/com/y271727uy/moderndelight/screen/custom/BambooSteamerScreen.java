package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BambooSteamerScreen extends AbstractContainerScreen<BambooSteamerScreenHandler> {
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/bamboo_steamer_gui.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/bamboo_steamer2_gui.png");
    private static final ResourceLocation TEXTURE_3 = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/bamboo_steamer3_gui.png");
    private static final ResourceLocation TEXTURE_4 = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/bamboo_steamer4_gui.png");
    public BambooSteamerScreen(BambooSteamerScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }


    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        int layer = menu.getCurrentLayer();
        switch (layer){
            case 1 -> {
                RenderSystem.setShaderTexture(0,getTexture(layer));
                guiGraphics.blit(getTexture(layer), x, y, 0, 0, imageWidth,imageHeight);
                if (menu.isCovered()){
                    guiGraphics.blit(getTexture(layer), x + 151, y + 57, 176, 11, 18,2);
                }
                renderProgressArrow(guiGraphics,x+71,y+25,0,layer);
                renderProgressArrow(guiGraphics,x+89,y+25,1,layer);
                renderProgressArrow(guiGraphics,x+71,y+43,2,layer);
                renderProgressArrow(guiGraphics,x+89,y+43,3,layer);
            }
            case 2 -> {
                RenderSystem.setShaderTexture(0,getTexture(layer));
                guiGraphics.blit(getTexture(layer), x, y, 0, 0, imageWidth,imageHeight);
                if (menu.isCovered()){
                    guiGraphics.blit(getTexture(layer), x + 151, y + 53, 176, 11, 18,2);
                }
                renderProgressArrow(guiGraphics,x+52,y+25,0,layer);
                renderProgressArrow(guiGraphics,x+70,y+25,1,layer);
                renderProgressArrow(guiGraphics,x+52,y+43,2,layer);
                renderProgressArrow(guiGraphics,x+70,y+43,3,layer);
                renderProgressArrow(guiGraphics,x+91,y+25,4,layer);
                renderProgressArrow(guiGraphics,x+109,y+25,5,layer);
                renderProgressArrow(guiGraphics,x+91,y+43,6,layer);
                renderProgressArrow(guiGraphics,x+109,y+43,7,layer);
            }
            case 3 -> {
                RenderSystem.setShaderTexture(0,getTexture(layer));
                guiGraphics.blit(getTexture(layer), x, y, 0, 0, imageWidth,imageHeight);
                if (menu.isCovered()){
                    guiGraphics.blit(getTexture(layer), x + 151, y + 49, 176, 11, 18,2);
                }
                renderProgressArrow(guiGraphics,x+33,y+25,0,layer);
                renderProgressArrow(guiGraphics,x+51,y+25,1,layer);
                renderProgressArrow(guiGraphics,x+33,y+43,2,layer);
                renderProgressArrow(guiGraphics,x+51,y+43,3,layer);
                renderProgressArrow(guiGraphics,x+72,y+25,4,layer);
                renderProgressArrow(guiGraphics,x+90,y+25,5,layer);
                renderProgressArrow(guiGraphics,x+72,y+43,6,layer);
                renderProgressArrow(guiGraphics,x+90,y+43,7,layer);
                renderProgressArrow(guiGraphics,x+111,y+25,8,layer);
                renderProgressArrow(guiGraphics,x+129,y+25,9,layer);
                renderProgressArrow(guiGraphics,x+111,y+43,10,layer);
                renderProgressArrow(guiGraphics,x+129,y+43,11,layer);
            }
            case 4 -> {
                RenderSystem.setShaderTexture(0,getTexture(layer));
                guiGraphics.blit(getTexture(layer), x, y, 0, 0, imageWidth,imageHeight);
                if (menu.isCovered()){
                    guiGraphics.blit(getTexture(layer), x + 151, y + 45, 176, 11, 18,2);
                }
                renderProgressArrow(guiGraphics,x+7,y+25,0,layer);
                renderProgressArrow(guiGraphics,x+25,y+25,1,layer);
                renderProgressArrow(guiGraphics,x+7,y+43,2,layer);
                renderProgressArrow(guiGraphics,x+25,y+43,3,layer);
                renderProgressArrow(guiGraphics,x+43,y+25,4,layer);
                renderProgressArrow(guiGraphics,x+61,y+25,5,layer);
                renderProgressArrow(guiGraphics,x+43,y+43,6,layer);
                renderProgressArrow(guiGraphics,x+61,y+43,7,layer);
                renderProgressArrow(guiGraphics,x+79,y+25,8,layer);
                renderProgressArrow(guiGraphics,x+97,y+25,9,layer);
                renderProgressArrow(guiGraphics,x+79,y+43,10,layer);
                renderProgressArrow(guiGraphics,x+97,y+43,11,layer);
                renderProgressArrow(guiGraphics,x+115,y+25,12,layer);
                renderProgressArrow(guiGraphics,x+133,y+25,13,layer);
                renderProgressArrow(guiGraphics,x+115,y+43,14,layer);
                renderProgressArrow(guiGraphics,x+133,y+43,15,layer);
            }
        }
        if (menu.isHeated()){
            guiGraphics.blit(getTexture(layer), x + 151, y + 65, 176, 0, 18,11);
        }
        if (menu.getLayer() != 0){
            guiGraphics.drawString(font,String.valueOf(menu.getLayer()),x + 157, y + 10,0xffffff,true);
        } else {
            guiGraphics.drawString(font,String.valueOf(0),x + 157, y + 10,0xff0000,true);
        }
    }
    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y, int slot, int currentLayer) {
        if (menu.getScaledProgress(slot) != 0){
            guiGraphics.blit(getTexture(currentLayer), x, y, 176, 123,menu.getScaledProgress(slot),16);
        }
    }
    private ResourceLocation getTexture(int currentLayer){
        return switch (currentLayer){
            case 2 -> TEXTURE_2;
            case 3 -> TEXTURE_3;
            case 4 -> TEXTURE_4;
            default -> TEXTURE_1;
        };
    }
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
