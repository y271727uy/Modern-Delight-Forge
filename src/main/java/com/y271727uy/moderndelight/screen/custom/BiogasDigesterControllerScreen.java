package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class BiogasDigesterControllerScreen extends AbstractContainerScreen<BiogasDigesterControllerScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/biogas_digester_controller_gui.png");
    public static final String UNAVAILABLE = "tooltips.moderndelight.biogas_digester_controller.unavailable";
    public static final String SIZE = "tooltips.moderndelight.biogas_digester_controller.size";
    public static final String GAS_VALUE = "tooltips.moderndelight.biogas_digester_controller.gas_value";
    public static final String MAX_GAS_VALUE = "tooltips.moderndelight.biogas_digester_controller.max_gas_value";

    public BiogasDigesterControllerScreen(BiogasDigesterControllerScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }
    boolean b1;
    boolean b2;
    boolean b3;
    boolean b4;
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0,TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth,imageHeight);

        guiGraphics.drawString(font,String.valueOf(menu.getSize()),x+70,y+22,0xffffff,true);
        guiGraphics.drawString(font,String.valueOf(menu.getGasValue()),x+70,y+38,0x00ff00,true);
        guiGraphics.drawString(font,String.valueOf(menu.getSize() * 1000),x+70,y+54,0xff0000,true);
        b1 = mouseX >= x + 52 && mouseY >= y + 19 && mouseX <= x + 65 && mouseY <= y + 33;
        b2 = mouseX >= x + 52 && mouseY >= y + 35 && mouseX <= x + 65 && mouseY <= y + 49;
        b3 = mouseX >= x + 52 && mouseY >= y + 51 && mouseX <= x + 65 && mouseY <= y + 65;

        if (menu.getChecked()==1){
            guiGraphics.blit(TEXTURE,x+9,y+58,176,0,25,12);
            b4 = false;
        } else {
            b4 = mouseX >= x + 9 && mouseY >= y + 58 && mouseX <= x + 33 && mouseY <= y + 69;
        }
        if (mouseX >= x + 161 && mouseX <= x + 171 && mouseY >= y + 5 && mouseY <= y + 15){
            guiGraphics.blit(TEXTURE,x+161,y+5,194,13,11,11);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (b1){
            guiGraphics.renderComponentTooltip(font,List.of(Component.translatable(SIZE)),mouseX,mouseY);
        }
        if (b2){
            guiGraphics.renderComponentTooltip(font,List.of(Component.translatable(GAS_VALUE)),mouseX,mouseY);
        }
        if (b3){
            guiGraphics.renderComponentTooltip(font,List.of(Component.translatable(MAX_GAS_VALUE)),mouseX,mouseY);
        }
        if (b4){
            guiGraphics.renderComponentTooltip(font,List.of(Component.translatable(UNAVAILABLE)),mouseX,mouseY);
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
