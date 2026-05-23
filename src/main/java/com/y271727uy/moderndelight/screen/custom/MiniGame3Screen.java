package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.power.ElectriciansDeskBlockEntity;
import com.y271727uy.moderndelight.networking.packet.ChangeBlockEntityDataC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class MiniGame3Screen extends Screen {
    private final Screen parent;
    private final ElectriciansDeskBlockEntity blockEntity;
    public MiniGame3Screen(Component title, Screen parent, ElectriciansDeskBlockEntity blockEntity, int goal) {
        super(title);
        this.parent = parent;
        this.blockEntity = blockEntity;
        if (goal >= 6){
            this.goal = 6;
        } else if (goal <= 0){
            this.goal = 1;
        } else this.goal = goal;
        for (int i = 0; i < goal;i++){
            Lines[0][i] = goal - i;
        }
    }
    private final int backgroundWidth = 176;
    private final int backgroundHeight = 166;
    private int x;
    private int y;
    private final int goal;
    private boolean isGameEnd = false;
    private final int[][] Lines = new int[3][6];
    private final boolean[] mouseLine = new boolean[3];
    private boolean isSelectedMode = false;
    private final boolean[] selectedLine = {false,false,false};
    private int selectedNum = 0;
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/mini_game_3_gui.png");
    @Override
    protected void init() {
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
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
        guiGraphics.blit(TEXTURE, x, y, 0, 0, backgroundWidth,backgroundHeight);
        renderDoneButton(guiGraphics,mouseX,mouseY);
        renderBlocks(guiGraphics,mouseX,mouseY);
        renderSelectedNum(guiGraphics,mouseX,mouseY);



        RenderSystem.disableDepthTest();
        super.render(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0.0F);
    }
    private void renderDoneButton(GuiGraphics guiGraphics, int mouseX, int mouseY){
        if (isGameEnd){
            boolean b = mouseX >= x + 149 && mouseY >= y + 133 && mouseX <= x + 159 && mouseY <= y + 143;
            guiGraphics.blit(TEXTURE, x + 149, y + 133, 0, 166, 11, 11);
            if (b){
                guiGraphics.blit(TEXTURE, x + 149, y + 133, 11, 166, 11,11);
            }
        }
    }
    private void renderBlocks(GuiGraphics guiGraphics,int mouseX,int mouseY){
        mouseLine[0] = mouseX >= x + 14 && mouseY >= y + 48 && mouseX <= x + 49 && mouseY <= y + 122;
        mouseLine[1] = mouseX >= x + 70 && mouseY >= y + 48 && mouseX <= x + 105 && mouseY <= y + 122;
        mouseLine[2] = mouseX >= x + 126 && mouseY >= y + 48 && mouseX <= x + 161 && mouseY <= y + 122;
        for (int k = 0;k < 3; k++){
            if (k==2){
                boolean b = true;
                for (int i = 0; i < goal;i++){
                    if (Lines[k][i] != goal - i){
                        b = false;
                        break;
                    }
                }
                isGameEnd = b;
            }
            int min = goal;
            int counter = -1;
            for (int i = 0; i < 6; i++){
                if (Lines[k][i] != 0){
                    counter ++;
                    guiGraphics.blit(TEXTURE, x + 14 + 56 * k,y + 118 - i * 5, 22, 166 + (6 - Lines[k][i]) * 5, 36,5);
                    if (Lines[k][i] < min){
                        min = Lines[k][i];
                    }
                }
            }
            if (isSelectedMode){
                if (selectedLine[k]){
                    guiGraphics.blit(TEXTURE, x + 24 + 56 * k,y + 48, 176, 48, 16,75 - (counter + 1) * 5);
                    guiGraphics.blit(TEXTURE, x + 14 + 56 * k,y + 118 - counter * 5, 63, 166 + (6 - min) * 5, 36,5);
                } else {
                    if (mouseLine[k]){
                        if (selectedNum < min) {
                            guiGraphics.blit(TEXTURE, x + 24 + 56 * k,y + 48, 176, 48, 16,75 - (counter + 1) * 5);
                        } else {
                            guiGraphics.blit(TEXTURE, x + 24 + 56 * k,y + 48, 192, 48, 16,75 - (counter + 1) * 5);
                        }
                    }
                }
            } else {
                if (mouseLine[k]){
                    guiGraphics.blit(TEXTURE, x + 24 + 56 * k,y + 48, 176, 48, 16,75 - (counter + 1) * 5);
                    if (counter != -1){
                        guiGraphics.blit(TEXTURE, x + 14 + 56 * k,y + 118 - counter * 5, 63, 166 + (6 - min) * 5, 36,5);
                    }
                }
            }
        }
    }
    private void renderSelectedNum(GuiGraphics guiGraphics,int mouseX,int mouseY){
        if (isSelectedMode){
            guiGraphics.blit(TEXTURE, mouseX - 18,mouseY - 3, 22, 166 + (6 - selectedNum) * 5, 36,5);
        }
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean gameEndButton = mouseX >= x + 149 && mouseY >= y + 133 && mouseX <= x + 159 && mouseY <= y + 143;
        if (isGameEnd){
            if (gameEndButton){
                int[] array = new int[1];
                array[0] = 1;
                ChangeBlockEntityDataC2SPacket.send(blockEntity.getBlockPos(),array);
                Minecraft.getInstance().getSoundManager()
                        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                this.onClose();
                return true;
            }
        }
        for (int i = 0; i < 3; i++){
            if (mouseLine[i]){
                if (isSelectedMode){
                    if (selectedLine[i]){
                        selectedNum = 0;
                        isSelectedMode = false;
                        selectedLine[i] = false;
                        Minecraft.getInstance().getSoundManager()
                                .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        return true;
                    } else {
                        int min = goal;
                        int counter = 0;
                        for (int j = 0;j < 6;j++){
                            if (Lines[i][j] != 0){
                                counter++;
                                if (Lines[i][j] < min){
                                    min = Lines[i][j];
                                }
                            }
                        }
                        if (counter == 0){
                            Lines[i][0] = selectedNum;
                            for (int k = 0;k < 3;k++){
                                if (selectedLine[k]){
                                    int counter2 = 0;
                                    for (int j = 0;j < 6;j++){
                                        if (Lines[k][j] != 0){
                                            counter2 ++;
                                        }
                                    }
                                    Lines[k][counter2-1] = 0;
                                    selectedLine[k] = false;
                                }
                            }
                            selectedNum = 0;
                            isSelectedMode = false;
                            Minecraft.getInstance().getSoundManager()
                                    .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                            return true;
                        } else {
                            if (min > selectedNum){
                                Lines[i][counter] = selectedNum;
                                for (int k = 0;k < 3;k++){
                                    if (selectedLine[k]){
                                        int counter2 = 0;
                                        for (int j = 0;j < 6;j++){
                                            if (Lines[k][j] != 0){
                                                counter2 ++;
                                            }
                                        }
                                        Lines[k][counter2-1] = 0;
                                        selectedLine[k] = false;
                                    }
                                }
                                selectedNum = 0;
                                isSelectedMode = false;
                                Minecraft.getInstance().getSoundManager()
                                        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                                return true;
                            }
                        }
                    }
                } else {
                    int min = goal;
                    int counter = 0;
                    for (int j = 0;j < 6;j++){
                        if (Lines[i][j] != 0){
                            counter++;
                            if (Lines[i][j] < min){
                                min = Lines[i][j];
                            }
                        }
                    }
                    if (counter != 0){
                        selectedNum = min;
                        isSelectedMode = true;
                        selectedLine[i] = true;
                        Minecraft.getInstance().getSoundManager()
                                .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        return true;
                    }
                }
            }
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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (minecraft != null){
            if (isGameEnd){
                if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER){
                    int[] array = new int[1];
                    array[0] = 1;
                    ChangeBlockEntityDataC2SPacket.send(blockEntity.getBlockPos(),array);
                    Minecraft.getInstance().getSoundManager()
                            .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    this.onClose();
                    return true;
                }
            }
            if (this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
                this.onClose();
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        int[] array = new int[1];
        array[0] = 4;
        ChangeBlockEntityDataC2SPacket.send(blockEntity.getBlockPos(),array);
        Objects.requireNonNull(minecraft).setScreen(parent);
    }
}
