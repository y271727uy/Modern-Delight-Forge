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

import java.util.ArrayList;
import java.util.List;

public class ACDCConverterScreen extends AbstractContainerScreen<ACDCConverterScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/acdcc_gui.png");
    public ACDCConverterScreen(ACDCConverterScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }
    public static final String SPEED_TIP = "tooltips.moderndelight.acdcc.speed_tip";
    public static final String PUT_BATTERY_TIP = "tooltips.moderndelight.acdcc.put_battery_tip";
    public static final String AC2DC = "tooltips.moderndelight.acdcc.ac2dc";
    public static final String DC2AC = "tooltips.moderndelight.acdcc.dc2ac";

    boolean bool1;
    boolean bool2;
    boolean bool3;
    boolean bool4;

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
        guiGraphics.blit(TEXTURE, x + 53, y + 27, 176, 75, menu.getScaledWorkSpeed(), 9);
        renderSwitchButton(guiGraphics, mouseX, mouseY, x, y);
        renderPageButton(guiGraphics, mouseX, mouseY, x, y);
        renderPowerLevel(guiGraphics,x,y);
        bool1 = mouseX >= x + 17 && mouseX <= x + 32 && mouseY >= y + 15 && mouseY <= y + 67;
        bool2 = mouseX >= x + 151 && mouseX <= x + 168 && mouseY >= y + 51 && mouseY <= y + 68;
        bool3 = mouseX >= x + 42 && mouseX <= x + 133 && mouseY >= y + 26 && mouseY <= y + 36;
        bool4 = mouseX >= x + 58 && mouseX <= x + 94 && mouseY >= y + 54 && mouseY <= y + 69;
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isSpectator()) {
            return super.mouseClicked(mouseX, mouseY, button);
        }
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        boolean b = mouseX >= x + 42 && mouseY >= y + 56 && mouseX <= x + 56 && mouseY <= y + 66;
        int[] array = new int[2];
        if (menu.isDC2ACMode()){
            if (b && button == 0){
                array[1] = 1;
                Minecraft.getInstance().getSoundManager()
                        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                ChangeBlockEntityDataC2SPacket.send(menu.blockEntity.getBlockPos(),array);
                return true;
            }
        } else {
            if (b && button == 0){
                array[1] = 2;
                Minecraft.getInstance().getSoundManager()
                        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                ChangeBlockEntityDataC2SPacket.send(menu.blockEntity.getBlockPos(),array);
                return true;
            }
        }
        if (menu.getWorkSpeed() != 0){
            if (hasShiftDown()){
                array[0] = 4;
            } else {
                array[0] = 2;
            }
            if (mouseX >= x + 42 && mouseY >= y + 26 && mouseX <= x + 52 && mouseY <= y + 36){
                Minecraft.getInstance().getSoundManager()
                        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                ChangeBlockEntityDataC2SPacket.send(menu.blockEntity.getBlockPos(),array);
                return true;
            }
        }
        if (menu.getWorkSpeed() != menu.getMaxWorkSpeed()){
            if (hasShiftDown()){
                array[0] = 3;
            } else {
                array[0] = 1;
            }
            if (mouseX >= x + 123 && mouseY >= y + 26 && mouseX <= x + 133 && mouseY <= y + 36){
                Minecraft.getInstance().getSoundManager()
                        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                ChangeBlockEntityDataC2SPacket.send(menu.blockEntity.getBlockPos(),array);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    private void renderPageButton(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (menu.getWorkSpeed() != 0){
            guiGraphics.blit(TEXTURE, x + 42, y + 26,176,53,11,11);
            if (mouseX >=x + 42 && mouseY >= y + 26 && mouseX <= x + 52 && mouseY <= y + 36){
                guiGraphics.blit(TEXTURE, x + 42, y + 26,176,64,11,11);
            }
        }
        if (menu.getWorkSpeed() != menu.getMaxWorkSpeed()){
            guiGraphics.blit(TEXTURE, x + 123, y + 26,186,53,11,11);
            if (mouseX >= x + 123 && mouseY >= y + 26 && mouseX <= x + 133 && mouseY <= y + 36){
                guiGraphics.blit(TEXTURE, x + 123, y + 26,186,64,11,11);
            }
        }
    }

    private void renderSwitchButton(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        boolean b = mouseX >= x + 42 && mouseY >= y + 56 && mouseX <= x + 56 && mouseY <= y + 66;
        if (menu.isDC2ACMode()){
            guiGraphics.blit(TEXTURE, x + 42, y + 56,176,84,15,11);
            guiGraphics.blit(TEXTURE, x + 60, y + 56,192,0,33,12);
            guiGraphics.drawString(font,menu.getEfficiency() + "FE/S",x + 45,y + 41,0xffffff,true);
            if (b){
                guiGraphics.blit(TEXTURE, x + 42, y + 56,176,95,15,11);
            }
        } else {
            guiGraphics.drawString(font,"---",x + 45,y + 41,0xffffff,true);
            if (b){
                guiGraphics.blit(TEXTURE, x + 42, y + 56,191,95,15,11);
            }
        }
    }

    private void renderPowerLevel(GuiGraphics guiGraphics, int x, int y) {
        if (menu.getPower() != 0){
            int offset = 53 - menu.getScaledProgress();
            guiGraphics.blit(TEXTURE, x + 17, y + 15 + offset, 176, offset,16, menu.getScaledProgress());
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (bool1){
            guiGraphics.renderComponentTooltip(font,List.of(Component.literal(menu.getPower()+" FE")),mouseX,mouseY);
        }
        if (bool2){
            switch (menu.hasBattery()){
                case 1 -> guiGraphics.renderComponentTooltip(font,List.of(Component.translatable(PUT_BATTERY_TIP)),mouseX,mouseY - 16);
                case 0 -> guiGraphics.renderComponentTooltip(font,List.of(Component.translatable(PUT_BATTERY_TIP)),mouseX,mouseY);
            }
        }
        if (bool3){
            List<Component> texts = new ArrayList<>();
            texts.add(Component.translatable(SPEED_TIP));
            texts.add(Component.literal(menu.getWorkSpeed() + "/" + menu.getMaxWorkSpeed()));
            guiGraphics.renderComponentTooltip(font,texts,mouseX,mouseY);
        }
        if (bool4){
            if (menu.isDC2ACMode()){
                guiGraphics.renderComponentTooltip(font,List.of(Component.translatable(DC2AC)),mouseX,mouseY);
            } else {
                guiGraphics.renderComponentTooltip(font,List.of(Component.translatable(AC2DC)),mouseX,mouseY);
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
