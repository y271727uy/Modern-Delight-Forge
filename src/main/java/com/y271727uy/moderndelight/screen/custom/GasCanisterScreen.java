package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.biogas.GasCanisterBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

public class GasCanisterScreen extends AbstractContainerScreen<GasCanisterScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/gas_canister_gui.png");
    public GasCanisterScreen(GasCanisterScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) * 4 / 7;
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

        int gasValue = menu.getGasValue();
        int maxGasValue = GasCanisterBlockEntity.getMaxCapacity();
        if (gasValue>0 && gasValue<maxGasValue/18){
            guiGraphics.blit(TEXTURE,x+81,y+24,176,13,51,28);
        } else if (gasValue >= maxGasValue/18 && gasValue < maxGasValue*2/18) {
            guiGraphics.blit(TEXTURE,x+81,y+24,176,41,51,28);
        } else if (gasValue >= maxGasValue*2/18 && gasValue < maxGasValue*3/18) {
            guiGraphics.blit(TEXTURE,x+81,y+24,176,69,51,28);
        } else if (gasValue >= maxGasValue*3/18 && gasValue < maxGasValue*4/18) {
            guiGraphics.blit(TEXTURE,x+81,y+24,176,97,51,28);
        } else if (gasValue >= maxGasValue*4/18 && gasValue < maxGasValue*5/18) {
            guiGraphics.blit(TEXTURE,x+81,y+24,176,125,51,28);
        } else if (gasValue >= maxGasValue*5/18 && gasValue < maxGasValue*6/18) {
            guiGraphics.blit(TEXTURE,x+81,y+24,0,166,51,28);
        } else if (gasValue >= maxGasValue*6/18 && gasValue < maxGasValue*7/18) {
            guiGraphics.blit(TEXTURE,x+81,y+24,51,166,51,28);
        } else if (gasValue >= maxGasValue*7/18 && gasValue < maxGasValue*8/18) {
            guiGraphics.blit(TEXTURE,x+81,y+24,102,166,51,28);
        } else if (gasValue >= maxGasValue*8/18 && gasValue < maxGasValue*9/18) {
            guiGraphics.blit(TEXTURE,x+81,y+24,153,166,51,28);
        } else if (gasValue >= maxGasValue*9/18 && gasValue < maxGasValue*10/18) {
            guiGraphics.blit(TEXTURE,x+81,y+24,204,166,51,28);
        } else if (gasValue >= maxGasValue*10/18 && gasValue < maxGasValue*11/18) {
            guiGraphics.blit(TEXTURE,x+81,y+24,0,194,51,28);
        } else if (gasValue >= maxGasValue*11/18 && gasValue < maxGasValue*12/18) {
            guiGraphics.blit(TEXTURE,x+81,y+24,51,194,51,28);
        } else if (gasValue >= maxGasValue*12/18 && gasValue < maxGasValue*13/18) {
            guiGraphics.blit(TEXTURE,x+81,y+24,102,194,51,28);
        } else if (gasValue >= maxGasValue*13/18 && gasValue < maxGasValue*14/18) {
            guiGraphics.blit(TEXTURE,x+81,y+24,153,194,51,28);
        } else if (gasValue >= maxGasValue*14/18 && gasValue < maxGasValue*15/18) {
            guiGraphics.blit(TEXTURE,x+81,y+24,204,194,51,28);
        } else if (gasValue >= maxGasValue*15/18 && gasValue < maxGasValue*16/18) {
            guiGraphics.blit(TEXTURE,x+81,y+24,0,222,51,28);
        } else if (gasValue >= maxGasValue*16/18){
            int cycle = menu.getCycleInt();
            switch (cycle){
                case 0: guiGraphics.blit(TEXTURE,x+81,y+24,51,222,51,28);break;
                case 1: guiGraphics.blit(TEXTURE,x+81,y+24,102,222,51,28);break;
                case 2: guiGraphics.blit(TEXTURE,x+81,y+24,153,222,51,28);break;
                case 3: guiGraphics.blit(TEXTURE,x+81,y+24,204,222,51,28);
            }
        }
        b = mouseX >= x + 82 && mouseX <= x + 130 && mouseY >= y + 25 && mouseY <= y + 50;
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (b){
            int gasValue = menu.getGasValue();
            int maxGasValue = GasCanisterBlockEntity.getMaxCapacity();
            if (gasValue<maxGasValue/6){
                guiGraphics.renderTooltip(font,Component.literal(gasValue+" mB"),
                        mouseX,mouseY);
            } else if (gasValue<maxGasValue/2) {
                guiGraphics.renderTooltip(font,Component.literal(gasValue+" mB"),
                        mouseX,mouseY);
            } else if (gasValue<maxGasValue*5/6) {
                guiGraphics.renderTooltip(font,Component.literal(gasValue+" mB"),
                        mouseX,mouseY);
            } else {
                guiGraphics.renderTooltip(font,Component.literal(gasValue+" mB"),
                        mouseX,mouseY);
            }
        }
        super.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
