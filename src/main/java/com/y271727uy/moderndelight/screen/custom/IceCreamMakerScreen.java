package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.kitchenware.ice_cream_maker.IceCreamMakerBlockEntity;
import com.y271727uy.moderndelight.networking.packet.ChangeBlockEntityDataC2SPacket;
import com.y271727uy.moderndelight.util.enums.CreamFlavor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.ResourceLocation;

public class IceCreamMakerScreen extends AbstractContainerScreen<IceCreamMakerScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/ice_cream_maker_gui.png");
    public IceCreamMakerScreen(IceCreamMakerScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }
    private IceCreamMakerBlockEntity.IceCream iceCream1;
    private IceCreamMakerBlockEntity.IceCream iceCream2;
    private IceCreamMakerBlockEntity.IceCream iceCream3;

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }
    boolean b1;
    boolean b2;
    boolean b3;
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0,TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        iceCream1 = menu.getIceCream1();
        iceCream2 = menu.getIceCream2();
        iceCream3 = menu.getIceCream3();

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth,imageHeight);
        renderPower(guiGraphics,x,y);
        renderArrow(guiGraphics,x,y);
        renderIceCream1(guiGraphics,x,y,mouseX,mouseY);
        renderIceCream2(guiGraphics,x,y,mouseX,mouseY);
        renderIceCream3(guiGraphics,x,y,mouseX,mouseY);
        b1 = mouseX >= x + 87 && mouseX <= x + 107 && mouseY >= y + 17 && mouseY <= y + 62;
        b2 = mouseX >= x + 114 && mouseX <= x + 134 && mouseY >= y + 17 && mouseY <= y + 62;
        b3 = mouseX >= x + 141 && mouseX <= x + 161 && mouseY >= y + 17 && mouseY <= y + 62;
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (b1){
            guiGraphics.renderTooltip(font,Component.literal(iceCream1.getAmount()+" mL"),
                    mouseX,mouseY);
        }
        if (b2){
            guiGraphics.renderTooltip(font,Component.literal(iceCream2.getAmount()+" mL"),
                    mouseX,mouseY);
        }
        if (b3){
            guiGraphics.renderTooltip(font,Component.literal(iceCream3.getAmount()+" mL"),
                    mouseX,mouseY);
        }
        super.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderPower(GuiGraphics guiGraphics, int x, int y){
        if (menu.isPowered()){
            guiGraphics.blit(TEXTURE,x + 9,y + 30,176,25,12,25);
        }
    }
    private void renderArrow(GuiGraphics guiGraphics,int x,int y){
        guiGraphics.blit(TEXTURE,x + 63,y + 22,176,50,menu.getScaledProgress(),42);
    }
    private void renderIceCream1(GuiGraphics guiGraphics,int x,int y,int mouseX,int mouseY){
        int height = iceCream1.getAmount() * 46 / 1000;
        int fix = 46 - height;
        boolean area = mouseX >= x + 93 && mouseY >= y + 65 && mouseX <= x + 101 && mouseY <= y + 73;
        if (iceCream1.getFlavor() != CreamFlavor.NULL){
            guiGraphics.blit(TEXTURE,x + 87,y + 17 + fix,21 * iceCream1.getFlavor().getId(),166,21,height);
        }
        if (iceCream1.isSelected()){
            guiGraphics.blit(TEXTURE,x+93,y+65,185,0,9,9);
            if (area){
                guiGraphics.blit(TEXTURE,x+93,y+65,194,0,9,9);
            }
        } else {
            if (area){
                guiGraphics.blit(TEXTURE,x+93,y+65,176,0,9,9);
            }
        }
    }
    private void renderIceCream2(GuiGraphics guiGraphics,int x,int y,int mouseX,int mouseY){
        int height = iceCream2.getAmount() * 46 / 1000;
        int fix = 46 - height;
        boolean area = mouseX >= x + 120 && mouseY >= y + 65 && mouseX <= x + 128 && mouseY <= y + 73;
        if (iceCream2.getFlavor() != CreamFlavor.NULL){
            guiGraphics.blit(TEXTURE,x + 114,y + 17 + fix,21 * iceCream2.getFlavor().getId(),166,21,height);
        }
        if (iceCream2.isSelected()){
            guiGraphics.blit(TEXTURE,x+120,y+65,185,0,9,9);
            if (area){
                guiGraphics.blit(TEXTURE,x+120,y+65,194,0,9,9);
            }
        } else {
            if (area){
                guiGraphics.blit(TEXTURE,x+120,y+65,176,0,9,9);
            }
        }
    }
    private void renderIceCream3(GuiGraphics guiGraphics,int x,int y,int mouseX,int mouseY){
        int height = iceCream3.getAmount() * 46 / 1000;
        int fix = 46 - height;
        boolean area = mouseX >= x + 147 && mouseY >= y + 65 && mouseX <= x + 155 && mouseY <= y + 73;
        if (iceCream3.getFlavor() != CreamFlavor.NULL){
            guiGraphics.blit(TEXTURE,x + 141,y + 17 + fix,21 * iceCream3.getFlavor().getId(),166,21,height);
        }
        if (iceCream3.isSelected()){
            guiGraphics.blit(TEXTURE,x+147,y+65,185,0,9,9);
            if (area){
                guiGraphics.blit(TEXTURE,x+147,y+65,194,0,9,9);
            }
        } else {
            if (area){
                guiGraphics.blit(TEXTURE,x+147,y+65,176,0,9,9);
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
        boolean area1 = mouseX >= x + 93 && mouseY >= y + 65 && mouseX <= x + 101 && mouseY <= y + 73;
        boolean area2 = mouseX >= x + 120 && mouseY >= y + 65 && mouseX <= x + 128 && mouseY <= y + 73;
        boolean area3 = mouseX >= x + 147 && mouseY >= y + 65 && mouseX <= x + 155 && mouseY <= y + 73;
        int[] array = new int[1];
        if (area1){
            array[0] = 1;
            Minecraft.getInstance().getSoundManager()
                    .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ChangeBlockEntityDataC2SPacket.send(menu.blockEntity.getBlockPos(),array);
            return true;
        }
        if (area2){
            array[0] = 2;
            Minecraft.getInstance().getSoundManager()
                    .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ChangeBlockEntityDataC2SPacket.send(menu.blockEntity.getBlockPos(),array);
            return true;
        }
        if (area3){
            array[0] = 3;
            Minecraft.getInstance().getSoundManager()
                    .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ChangeBlockEntityDataC2SPacket.send(menu.blockEntity.getBlockPos(),array);
            return true;
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
