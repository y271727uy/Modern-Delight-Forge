package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.networking.packet.SpawnXPC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FreezerScreen extends AbstractContainerScreen<FreezerScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/freezer_gui.png");
    public FreezerScreen(FreezerScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        int minX = x + 116;
        int minY = y + 35;
        int maxX = x + 150;
        int maxY = y + 46;
        if (mouseX >= minX && mouseX <= maxX &&
                mouseY >= minY && mouseY <= maxY && button == 0){
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && !mc.player.isSpectator()) {
                if (menu.getExperiences() != 0){
                    mc.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(),1.0f,1.0f);
                    SpawnXPC2SPacket.send(menu.blockEntity.getBlockPos());
                } else {
                    return false;
                }
            }
            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
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
        renderCoolTime(guiGraphics, x, y);
        if (mouseX >= x + 116 && mouseX <= x + 150 && mouseY >= y + 35 && mouseY <= y + 46){
            guiGraphics.blit(TEXTURE, x + 115, y + 34, 191, 0, 36,13);
            b = true;
        } else b = false;
        if (menu.getExperiences() <= 9999){
            guiGraphics.drawString(font,String.valueOf(menu.getExperiences()),x+117,y+36, 0x82fd64,true);
        } else {
            guiGraphics.drawString(font,"9999+",x+117,y+36, 0x82fd64,true);
        }
    }

    private void renderCoolTime(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCooling()){
            int offset = 16 - menu.getScaledCoolTime();
            guiGraphics.blit(TEXTURE, x + 136, y + 52 + offset, 188, offset, 3, menu.getScaledCoolTime());
        }
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()){
            guiGraphics.blit(TEXTURE, x + 154, y + 34, 176, 0 , 12, menu.getScaledProgress());
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (b){
            guiGraphics.renderTooltip(font,Component.translatable(AdvanceFurnaceScreen.TOOLTIP),x,y);
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
