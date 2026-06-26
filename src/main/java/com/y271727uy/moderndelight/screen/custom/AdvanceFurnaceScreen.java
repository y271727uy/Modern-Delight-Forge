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
public class AdvanceFurnaceScreen extends AbstractContainerScreen<AdvanceFurnaceScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/advance_furnace_gui.png");
    public static final String TOOLTIP = "tooltips.moderndelight.advance_furnace_exp_tooltip";
    public AdvanceFurnaceScreen(AdvanceFurnaceScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
        titleLabelY -= 3;
        inventoryLabelY += 6;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (width - imageWidth) / 2;
        int y = (height - 172) / 2;
        int minX = x + 7;
        int minY = y + 51;
        int maxX = x + 41;
        int maxY = y + 62;
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
        int y = (height - 172) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth,175);
        renderProgressArrow1(guiGraphics, x, y);
        renderProgressArrow2(guiGraphics, x, y);
        renderProgressArrow3(guiGraphics, x, y);
        renderProgressArrow4(guiGraphics, x, y);
        renderBurnTime(guiGraphics, x, y);
        if (mouseX >= x + 7 && mouseX <= x + 41 && mouseY >= y + 51 && mouseY <= y + 62){
            guiGraphics.blit(TEXTURE, x + 6, y + 50, 176, 38, 36,13);
            b = true;
        } else b = false;
        if (menu.getExperiences() <= 9999){
            guiGraphics.drawString(font,String.valueOf(menu.getExperiences()),x+8,y+52, 0x82fd64,true);
        } else {
            guiGraphics.drawString(font,"9999+",x+8,y+52, 0x82fd64,true);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (b){
            guiGraphics.renderTooltip(this.font,Component.translatable(TOOLTIP),mouseX,mouseY);
        }
        super.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderBurnTime(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isBurning()){
            int offset = 14 - menu.getScaledBurnTime();
            guiGraphics.blit(TEXTURE, x + 8, y + 15 + offset, 176, 24 + offset,
                    16 ,menu.getScaledBurnTime());
        }
    }

    private void renderProgressArrow1(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting(0)){
            guiGraphics.blit(TEXTURE, x + 43, y + 37, 176, 0,18, menu.getScaledProgress(0));
        }
    }
    private void renderProgressArrow2(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting(1)){
            guiGraphics.blit(TEXTURE, x + 79, y + 37, 176, 0,18 , menu.getScaledProgress(1));
        }
    }
    private void renderProgressArrow3(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting(2)){
            guiGraphics.blit(TEXTURE, x + 115, y + 37, 176, 0,18 ,menu.getScaledProgress(2) );
        }
    }
    private void renderProgressArrow4(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting(3)){
            guiGraphics.blit(TEXTURE, x + 151, y + 37, 176, 0, 18, menu.getScaledProgress(3));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
