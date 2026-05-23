package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.networking.packet.ChangeBlockEntityDataC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.ResourceLocation;

public class TeslaCoilScreen extends AbstractContainerScreen<TeslaCoilScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/tesla_coil_gui.png");

    public TeslaCoilScreen(TeslaCoilScreenHandler menu, Inventory inventory, Component title) {
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
        if (menu.getEfficiency() > 0){
            guiGraphics.blit(TEXTURE,x+9,y+58,176,0,25,12);
        }
        if (menu.getEfficiency() > 0){
            guiGraphics.drawString(font,menu.getEfficiency() + "FE/S",x + 70,y + 38,0xffffff,true);
        } else {
            guiGraphics.drawString(font,menu.getEfficiency() + "FE/S",x + 70,y + 38,0xff0000,true);
        }
        renderSwitchButton(guiGraphics,mouseX,mouseY,x,y);
    }
    private void renderSwitchButton(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        boolean b = mouseX >= x + 152 && mouseY >= y + 58 && mouseX <= x + 166 && mouseY <= y + 68;
        if (menu.getShowParticle()){
            guiGraphics.blit(TEXTURE, x + 152, y + 58,176,84,15,11);
            if (b){
                guiGraphics.blit(TEXTURE, x + 152, y + 58,176,95,15,11);
            }
        } else {
            if (b){
                guiGraphics.blit(TEXTURE, x + 152, y + 58,191,95,15,11);
            }
        }
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isSpectator()) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        boolean b = mouseX >= x + 152 && mouseY >= y + 58 && mouseX <= x + 166 && mouseY <= y + 68;
        int[] array = new int[2];
        if (menu.getShowParticle()){
            if (b && button == 0){
                array[0] = 1;
                Minecraft.getInstance().getSoundManager()
                        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                ChangeBlockEntityDataC2SPacket.send(menu.blockEntity.getBlockPos(),array);
                return true;
            }
        } else {
            if (b && button == 0){
                array[0] = 2;
                Minecraft.getInstance().getSoundManager()
                        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                ChangeBlockEntityDataC2SPacket.send(menu.blockEntity.getBlockPos(),array);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
