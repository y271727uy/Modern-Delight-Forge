package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

public class WindTurbineControllerScreen extends AbstractContainerScreen<WindTurbineControllerScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/wind_turbine_controller_gui.png");

    public WindTurbineControllerScreen(WindTurbineControllerScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
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
        RenderSystem.setShaderTexture(0,TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth,imageHeight);
        if (menu.getYLevel() >= 200){
            guiGraphics.drawString(font,String.valueOf(menu.getYLevel()),x + 81,y + 22,0x1fff00,true);
        } else if (menu.getYLevel() <= 0) {
            guiGraphics.drawString(font,String.valueOf(menu.getYLevel()),x + 81,y + 22,0xe50000,true);
        } else {
            guiGraphics.drawString(font,String.valueOf(menu.getYLevel()),x + 81,y + 22,0xffffff,true);
        }
        guiGraphics.drawString(font,menu.getEfficiency() + "FE/S",x + 70,y + 38,0xffffff,true);
        if (menu.getChecked()==1){
            guiGraphics.blit(TEXTURE,x+9,y+58,176,0,25,12);
        }
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
