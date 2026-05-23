package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.power.alternator.PhotovoltaicGeneratorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class PhotovoltaicGeneratorScreen extends AbstractContainerScreen<PhotovoltaicGeneratorScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/photovoltaic_generator_gui.png");
    public PhotovoltaicGeneratorScreen(PhotovoltaicGeneratorScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }
    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
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
        renderYText(guiGraphics, x, y);
        renderPowerLevel(guiGraphics,x,y);
        renderDayNightIcon(guiGraphics, x, y);
        if (menu.isWorking()){
            guiGraphics.blit(TEXTURE, x + 148,y + 14,192,0,12,12);
        }
        if (menu.isInSlowMode()){
            guiGraphics.blit(TEXTURE, x + 135,y + 14,192,12,12,12);
        }
        b = mouseX >= x + 17 && mouseX <= x + 32 && mouseY >= y + 15 && mouseY <= y + 67;
        if (mouseX >= x + 161 && mouseX <= x + 171 && mouseY >= y + 5 && mouseY <= y + 15){
            guiGraphics.blit(TEXTURE,x+161,y+5,192,37,11,11);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (b){
            guiGraphics.renderTooltip(font,Component.literal(String.valueOf(menu.getPower()+" FE")),
                    x,y);
        }
        super.renderTooltip(guiGraphics, x, y);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        if (mouseX >= x + 161 && mouseX <= x + 171 && mouseY >= y + 5 && mouseY <= y + 15 && button == 0){
            Minecraft.getInstance().setScreen(new TipScreen(ModBlocks.ELECTRICIANS_DESK.get().getName(),
                    Minecraft.getInstance().screen,menu.blockEntity));
            Minecraft.getInstance().getSoundManager()
                    .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void renderYText(GuiGraphics guiGraphics, int x, int y) {
        if (menu.getYValue() > 0){
            guiGraphics.drawString(font,String.valueOf(menu.getYValue()), x + 70, y + 57,0xffffff,true);
        } else {
            guiGraphics.drawString(font,String.valueOf(menu.getYValue()), x + 70, y + 57,0xff0000,true);
        }
    }

    private void renderDayNightIcon(GuiGraphics guiGraphics, int x, int y) {
        if (!Objects.requireNonNull(menu.blockEntity.getLevel()).dimensionType().fixedTime().isEmpty()){
            if (PhotovoltaicGeneratorBlockEntity.isEarlyMorningOrTwilight(Objects.requireNonNull(menu.blockEntity.getLevel())) &&
                    menu.blockEntity.getLevel().isThundering()){
                guiGraphics.blit(TEXTURE, x + 38, y + 54,204,67,14,14);
            } else if (PhotovoltaicGeneratorBlockEntity.isEarlyMorningOrTwilight(Objects.requireNonNull(menu.blockEntity.getLevel())) &&
                    menu.blockEntity.getLevel().isRaining()) {
                guiGraphics.blit(TEXTURE, x + 38, y + 54,190,67,14,14);
            } else if (PhotovoltaicGeneratorBlockEntity.isEarlyMorningOrTwilight(Objects.requireNonNull(menu.blockEntity.getLevel()))) {
                guiGraphics.blit(TEXTURE, x + 38, y + 54,176,67,14,14);
            } else if ((PhotovoltaicGeneratorBlockEntity.isMorningOrAfternoon(Objects.requireNonNull(menu.blockEntity.getLevel())) ||
                    PhotovoltaicGeneratorBlockEntity.isNoon(Objects.requireNonNull(menu.blockEntity.getLevel())))
                    && menu.blockEntity.getLevel().isThundering()){
                guiGraphics.blit(TEXTURE, x + 38, y + 54,204,53,14,14);
            } else if ((PhotovoltaicGeneratorBlockEntity.isMorningOrAfternoon(Objects.requireNonNull(menu.blockEntity.getLevel())) ||
                    PhotovoltaicGeneratorBlockEntity.isNoon(Objects.requireNonNull(menu.blockEntity.getLevel())))
                    && menu.blockEntity.getLevel().isRaining()){
                guiGraphics.blit(TEXTURE, x + 38, y + 54,190,53,14,14);
            } else if ((PhotovoltaicGeneratorBlockEntity.isMorningOrAfternoon(Objects.requireNonNull(menu.blockEntity.getLevel())) ||
                    PhotovoltaicGeneratorBlockEntity.isNoon(Objects.requireNonNull(menu.blockEntity.getLevel())))){
                guiGraphics.blit(TEXTURE, x + 38, y + 54,176,53,14,14);
            } else if (menu.blockEntity.getLevel().isThundering()) {
                guiGraphics.blit(TEXTURE, x + 38, y + 54,204,81,14,14);
            } else if (menu.blockEntity.getLevel().isRaining()) {
                guiGraphics.blit(TEXTURE, x + 38, y + 54,190,81,14,14);
            } else {
                guiGraphics.blit(TEXTURE, x + 38, y + 54,176,81,14,14);
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
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
    public static class TipScreen extends Screen{
        private final Screen parent;
        private final net.minecraft.world.level.block.entity.BlockEntity blockEntity;
        protected TipScreen(Component title, Screen parent, net.minecraft.world.level.block.entity.BlockEntity blockEntity) {
            super(title);
            this.parent = parent;
            this.blockEntity = blockEntity;
        }
        private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
                "textures/gui/photovoltaic_generator_gui.png");
        private final int imageWidth = 176;
        private final int imageHeight = 77;
        private int x;
        private int y;
        public static final String GREEN_TIP_1 = "moderndelight.tooltips.green_tip_1";
        public static final String GREEN_TIP_2 = "moderndelight.tooltips.green_tip_2";
        public static final String GREEN_TIP_3 = "moderndelight.tooltips.green_tip_3";
        public static final String YELLOW_TIP_1 = "moderndelight.tooltips.yellow_tip_1";
        public static final String YELLOW_TIP_2 = "moderndelight.tooltips.yellow_tip_2";
        public static final String YELLOW_TIP_3 = "moderndelight.tooltips.yellow_tip_3";

        @Override
        protected void init() {
            this.x = (this.width - this.imageWidth) / 2;
            this.y = (this.height - this.imageHeight) / 2;
        }
        @Override
        public boolean isPauseScreen() {
            return false;
        }
        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1f,1f,1f,1f);
            RenderSystem.setShaderTexture(0,TEXTURE);
            guiGraphics.blit(TEXTURE, x, y, 0, 166, imageWidth,imageHeight);
            if (mouseX >= x + 161 && mouseX <= x + 171 && mouseY >= y + 5 && mouseY <= y + 15){
                guiGraphics.blit(TEXTURE,x+161,y+5,176,171,11,11);
            }
            guiGraphics.drawString(font,Component.translatable(GREEN_TIP_1).getString(),
                    x+19,y+6,0xffffff,true);
            guiGraphics.drawString(font,Component.translatable(GREEN_TIP_2).getString(),
                    x+19,y+16,0xffffff,true);
            guiGraphics.drawString(font,Component.translatable(GREEN_TIP_3).getString(),
                    x+19,y+26,0xffffff,true);
            guiGraphics.drawString(font,Component.translatable(YELLOW_TIP_1).getString(),
                    x+19,y+38,0xffffff,true);
            guiGraphics.drawString(font,Component.translatable(YELLOW_TIP_2).getString(),
                    x+19,y+48,0xffffff,true);
            guiGraphics.drawString(font,Component.translatable(YELLOW_TIP_3).getString(),
                    x+19,y+58,0xffffff,true);
            super.render(guiGraphics, mouseX, mouseY, delta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (mouseX >= x + 161 && mouseX <= x + 171 && mouseY >= y + 5 && mouseY <= y + 15 && button == 0){
                Minecraft.getInstance().getSoundManager()
                        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                this.onClose();
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }
        @Override
        public final void tick() {
            if (minecraft != null && minecraft.player != null){
                if (!this.minecraft.player.isAlive() && this.minecraft.player.isRemoved() && canUse()) {
                    this.minecraft.setScreen(null);
                }
            }
        }
        private boolean canUse(){
            if (minecraft != null && minecraft.player != null){
                BlockPos pos1 = minecraft.player.blockPosition();
                BlockPos pos2 = blockEntity.getBlockPos();
                double distance = Math.sqrt(Math.pow(pos2.getX()-pos1.getX(),2)+Math.pow(pos2.getY()-pos1.getY(),2)+Math.pow(pos2.getZ()-pos1.getZ(),2));
                return distance < 7 && !blockEntity.isRemoved();
            } else return false;
        }
        @Override
        public void onClose() {
            Objects.requireNonNull(minecraft).setScreen(parent);
        }
    }
}
