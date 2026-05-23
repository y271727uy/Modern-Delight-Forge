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
public class OvenScreen extends AbstractContainerScreen<OvenScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/oven_gui.png");
    public OvenScreen(OvenScreenHandler menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
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
        int minX = x + 124;
        int minY = y + 55;
        int maxX = x + 158;
        int maxY = y + 66;
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
        renderBurnTime(guiGraphics, x, y);
        if (mouseX >= x + 124 && mouseX <= x + 158 && mouseY >= y + 55 && mouseY <= y + 66){
            guiGraphics.blit(TEXTURE, x + 123, y + 54, 0, 195, 36,13);
            b = true;
        } else b = false;
        if (menu.getExperiences() <= 9999){
            guiGraphics.drawString(font,String.valueOf(menu.getExperiences()),x+125,y+56, 0x82fd64,true);
        } else {
            guiGraphics.drawString(font,"9999+",x+125,y+56, 0x82fd64,true);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (b){
            guiGraphics.renderTooltip(font,Component.translatable(AdvanceFurnaceScreen.TOOLTIP),x,y);
        }
        super.renderTooltip(guiGraphics, x, y);
    }

    private void renderBurnTime(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isBurning()){
            int offset = 11 - menu.getScaledBurnTime();
            guiGraphics.blit(TEXTURE, x + 47, y + 39 + offset,
                    0, 167 + offset, 18, menu.getScaledBurnTime());
        }
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()){
            guiGraphics.blit(TEXTURE, x + 95, y + 21, 0, 178, menu.getScaledProgress(), 17);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
