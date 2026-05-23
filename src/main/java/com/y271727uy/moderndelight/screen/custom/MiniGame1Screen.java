package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.power.ElectriciansDeskBlockEntity;
import com.y271727uy.moderndelight.networking.packet.ChangeBlockEntityDataC2SPacket;
import com.y271727uy.moderndelight.util.Pos2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import org.lwjgl.glfw.GLFW;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class MiniGame1Screen extends Screen {
    private final Screen parent;
    private final ElectriciansDeskBlockEntity blockEntity;
    public MiniGame1Screen(Component title, Screen parent, ElectriciansDeskBlockEntity blockEntity,int goal) {
        super(title);
        this.parent = parent;
        this.blockEntity = blockEntity;
        this.goal = goal;
        spawnARandomNumber();
        spawnARandomNumber();
    }
    private final int backgroundWidth = 176;
    private final int backgroundHeight = 166;
    private int x;
    private int y;
    private final int[][] game = {
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0}
    };
    private boolean advanceMode = false;
    private final int[] spawnNumberPool = {2,4};
    private final int[] advanceSpawnNumberPool = {2,4,8};
    private final int goal;
    private boolean isGameEnd = false;
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/mini_game_1_gui.png");
    private Pos2 getARandomPos(){
        List<Pos2> availablePos = new ArrayList<>();
        for (int i = 0;i < 4;i++){
            for (int j = 0;j < 4;j++){
                if (game[i][j] == 0){
                    Pos2 temp = new Pos2(j,i);
                    availablePos.add(temp);
                }
            }
        }
        if (availablePos.isEmpty()){
            return null;
        } else {
            int k = (int)(Math.random() * availablePos.size());
            return availablePos.get(k);
        }
    }
    private void spawnARandomNumber(){
        int num;
        if (advanceMode){
            num = advanceSpawnNumberPool[(int) (Math.random() * advanceSpawnNumberPool.length)];
        } else {
            num = spawnNumberPool[(int) (Math.random() * spawnNumberPool.length)];
        }
        Pos2 tem = getARandomPos();
        if (tem != null){
            game[tem.getY()][tem.getX()] = num;
        }
    }
    private void spawnRandomNumbers() {
        if (advanceMode){
            if (Math.random() < 0.7)
                spawnARandomNumber();
        } else {
            if (Math.random() < 0.3){
                spawnARandomNumber();
                spawnARandomNumber();
            } else {
                spawnARandomNumber();
            }
        }
    }
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
        renderNumbers(guiGraphics);
        renderGoal(guiGraphics);
        renderDoneButton(guiGraphics,mouseX,mouseY);
        renderControlButton(guiGraphics,mouseX,mouseY);

        RenderSystem.disableDepthTest();
        super.render(guiGraphics, mouseX, mouseY, delta);
    }
    private void renderNumbers(GuiGraphics guiGraphics){
        for (int i = 0;i < 4;i++){
            for (int j = 0;j < 4;j++){
                if (game[i][j] >= goal){
                    isGameEnd = true;
                }
                switch (game[i][j]){
                    case 0 -> {}
                    case 2 -> guiGraphics.blit(TEXTURE,
                            x + 24 + j * 32, y + 11 + i * 32, 0, 188, 32,32);
                    case 4 -> guiGraphics.blit(TEXTURE,
                            x + 24 + j * 32, y + 11 + i * 32, 32, 188, 32,32);
                    case 8 -> guiGraphics.blit(TEXTURE,
                            x + 24 + j * 32, y + 11 + i * 32, 64, 188, 32,32);
                    case 16 -> guiGraphics.blit(TEXTURE,
                            x + 24 + j * 32, y + 11 + i * 32, 96, 188, 32,32);
                    case 32 -> guiGraphics.blit(TEXTURE,
                            x + 24 + j * 32, y + 11 + i * 32, 128, 188, 32,32);
                    case 64 -> guiGraphics.blit(TEXTURE,
                            x + 24 + j * 32, y + 11 + i * 32, 160, 188, 32,32);
                    case 128 -> {
                        guiGraphics.blit(TEXTURE,
                                x + 24 + j * 32, y + 11 + i * 32, 192, 188, 32, 32);
                        advanceMode = true;
                    }
                    case 256 -> guiGraphics.blit(TEXTURE,
                            x + 24 + j * 32, y + 11 + i * 32, 224, 188, 32,32);
                    case 512 -> guiGraphics.blit(TEXTURE,
                            x + 24 + j * 32, y + 11 + i * 32, 0, 220, 32,32);
                    case 1024 -> guiGraphics.blit(TEXTURE,
                            x + 24 + j * 32, y + 11 + i * 32, 32, 220, 32,32);
                    case 2048 -> guiGraphics.blit(TEXTURE,
                            x + 24 + j * 32, y + 11 + i * 32, 64, 220, 32,32);
                    default -> {
                        guiGraphics.blit(TEXTURE,
                                x + 24 + j * 32, y + 11 + i * 32, 96, 220, 32,32);
                        guiGraphics.drawString(font,String.valueOf(game[i][j]),x + 28 + j * 32, y + 23 + i * 32,0xffffff,true);
                    }
                }
            }
        }
    }
    private void renderDoneButton(GuiGraphics guiGraphics, int mouseX, int mouseY){
        if (isGameEnd){
            boolean b = mouseX >= x + 149 && mouseY >= y + 145 && mouseX <= x + 159 && mouseY <= y + 155;
            guiGraphics.blit(TEXTURE, x + 149, y + 145, 0, 166, 11, 11);
            if (b){
                guiGraphics.blit(TEXTURE, x + 149, y + 145, 11, 166, 11,11);
            }
        }
    }
    private void renderControlButton(GuiGraphics guiGraphics, int mouseX, int mouseY){
        boolean bU = mouseX >= x + 40 && mouseY >= y + 145 && mouseX <= x + 50 && mouseY <= y + 155;
        boolean bD = mouseX >= x + 52 && mouseY >= y + 145 && mouseX <= x + 62 && mouseY <= y + 155;
        boolean bL = mouseX >= x + 16 && mouseY >= y + 145 && mouseX <= x + 26 && mouseY <= y + 155;
        boolean bR = mouseX >= x + 28 && mouseY >= y + 145 && mouseX <= x + 38 && mouseY <= y + 155;
        if (bU){
            guiGraphics.blit(TEXTURE, x + 40, y + 145, 22, 177, 11,11);
        } else if (bD) {
            guiGraphics.blit(TEXTURE, x + 52, y + 145, 33, 177, 11,11);
        } else if (bL) {
            guiGraphics.blit(TEXTURE, x + 16, y + 145, 0, 177, 11,11);
        } else if (bR) {
            guiGraphics.blit(TEXTURE, x + 28, y + 145, 11, 177, 11,11);
        }
    }
    private void renderGoal(GuiGraphics guiGraphics){
        if (isGameEnd){
            guiGraphics.drawString(font,String.valueOf(goal),x + 68,y + 146, 0x00ff00,true);
        } else {
            guiGraphics.drawString(font,String.valueOf(goal),x + 68,y + 146, 0xff0000,true);
        }
    }

    private void handleUpButton(){
        Minecraft.getInstance().getSoundManager()
                .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        for (int i = 0; i < 4; i++){
            int count = 1;
            for (int j = 3; j > 0; j--){
                if (j == 3){
                    if (game[3][i] == game[2][i] && game[1][i] == game[0][i]
                            && game[3][i] != 0 && game[1][i] != 0){
                        int tem1 = game[1][i] + game[0][i];
                        int tem2 = game[3][i] + game[2][i];
                        game[0][i] = tem1;
                        game[1][i] = tem2;
                        game[2][i] = 0;
                        game[3][i] = 0;
                        break;
                    }
                }
                if (game[j][i] != 0 && game[j-1][i] == 0){
                    game[j-1][i] = game[j][i];
                    game[j][i] = 0;
                    continue;
                }
                if (game[j][i] != 0 && game[j-1][i] != 0) {
                    if (game[j][i] == game[j-1][i] && count != 0){
                        int temp = game[j][i] + game[j-1][i];
                        game[j-1][i] = temp;
                        game[j][i] = 0;
                        count = 0;
                    }
                }
            }
        }
        for (int k = 0; k < 2; k++){
            for (int i = 0; i < 4; i++){
                for (int j = 3; j > 0; j--){
                    if (game[j][i] != 0 && game[j-1][i] == 0){
                        game[j-1][i] = game[j][i];
                        game[j][i] = 0;
                    }
                }
            }
        }
        spawnRandomNumbers();
    }


    private void handleDownButton(){
        Minecraft.getInstance().getSoundManager()
                .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        for (int i = 0; i < 4; i++){
            int count = 1;
            for (int j = 0; j < 3; j++){
                if (j == 0){
                    if (game[3][i] == game[2][i] && game[1][i] == game[0][i]
                            && game[3][i] != 0 && game[1][i] != 0){
                        int tem1 = game[1][i] + game[0][i];
                        int tem2 = game[3][i] + game[2][i];
                        game[0][i] = 0;
                        game[1][i] = 0;
                        game[2][i] = tem1;
                        game[3][i] = tem2;
                        break;
                    }
                }
                if (game[j][i] != 0 && game[j+1][i] == 0){
                    game[j+1][i] = game[j][i];
                    game[j][i] = 0;
                    continue;
                }
                if (game[j][i] != 0 && game[j+1][i] != 0) {
                    if (game[j][i] == game[j+1][i] && count != 0){
                        int temp = game[j][i] + game[j+1][i];
                        game[j+1][i] = temp;
                        game[j][i] = 0;
                        count = 0;
                    }
                }
            }
        }
        for (int k = 0; k < 2; k++){
            for (int i = 0; i < 4; i++){
                for (int j = 0; j < 3; j++){
                    if (game[j][i] != 0 && game[j+1][i] == 0){
                        game[j+1][i] = game[j][i];
                        game[j][i] = 0;
                    }
                }
            }
        }
        spawnRandomNumbers();
    }
    private void handleLeftButton(){
        Minecraft.getInstance().getSoundManager()
                .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        for (int i = 0; i < 4; i++){
            int count = 1;
            for (int j = 3; j > 0; j--){
                if (j == 3){
                    if (game[i][3] == game[i][2] && game[i][1] == game[i][0]
                            && game[i][3] != 0 && game[i][1] != 0){
                        int tem1 = game[i][1] + game[i][0];
                        int tem2 = game[i][3] + game[i][2];
                        game[i][0] = tem1;
                        game[i][1] = tem2;
                        game[i][2] = 0;
                        game[i][3] = 0;
                        break;
                    }
                }
                if (game[i][j] != 0 && game[i][j-1] == 0){
                    game[i][j-1] = game[i][j];
                    game[i][j] = 0;
                    continue;
                }
                if (game[i][j] != 0 && game[i][j-1] != 0) {
                    if (game[i][j] == game[i][j-1] && count != 0){
                        int temp = game[i][j] + game[i][j-1];
                        game[i][j-1] = temp;
                        game[i][j] = 0;
                        count = 0;
                    }
                }
            }
        }
        for (int k = 0; k < 2; k++){
            for (int i = 0; i < 4; i++){
                for (int j = 3; j > 0; j--){
                    if (game[i][j] != 0 && game[i][j-1] == 0){
                        game[i][j-1] = game[i][j];
                        game[i][j] = 0;
                    }
                }
            }
        }
        spawnRandomNumbers();
    }
    private void handleRightButton(){
        Minecraft.getInstance().getSoundManager()
                .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        for (int i = 0; i < 4; i++){
            int count = 1;
            for (int j = 0; j < 3; j++){
                if (j == 0){
                    if (game[i][3] == game[i][2] && game[i][1] == game[i][0]
                            && game[i][3] != 0 && game[i][1] != 0){
                        int tem1 = game[i][1] + game[i][0];
                        int tem2 = game[i][3] + game[i][2];
                        game[i][0] = 0;
                        game[i][1] = 0;
                        game[i][2] = tem1;
                        game[i][3] = tem2;
                        break;
                    }
                }
                if (game[i][j] != 0 && game[i][j+1] == 0){
                    game[i][j+1] = game[i][j];
                    game[i][j] = 0;
                    continue;
                }
                if (game[i][j] != 0 && game[i][j+1] != 0) {
                    if (game[i][j] == game[i][j+1] && count != 0){
                        int temp = game[i][j] + game[i][j+1];
                        game[i][j+1] = temp;
                        game[i][j] = 0;
                        count = 0;
                    }
                }
            }
        }
        for (int k = 0; k < 2; k++){
            for (int i = 0; i < 4; i++){
                for (int j = 0; j < 3; j++){
                    if (game[i][j] != 0 && game[i][j+1] == 0){
                        game[i][j+1] = game[i][j];
                        game[i][j] = 0;
                    }
                }
            }
        }
        spawnRandomNumbers();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean bU = mouseX >= x + 40 && mouseY >= y + 145 && mouseX <= x + 50 && mouseY <= y + 155;
        boolean bD = mouseX >= x + 52 && mouseY >= y + 145 && mouseX <= x + 62 && mouseY <= y + 155;
        boolean bL = mouseX >= x + 16 && mouseY >= y + 145 && mouseX <= x + 26 && mouseY <= y + 155;
        boolean bR = mouseX >= x + 28 && mouseY >= y + 145 && mouseX <= x + 38 && mouseY <= y + 155;
        boolean b = mouseX >= x + 149 && mouseY >= y + 145 && mouseX <= x + 159 && mouseY <= y + 155;
        if (bU){
            handleUpButton();
            return true;
        }
        if (bD){
            handleDownButton();
            return true;
        }
        if (bL){
            handleLeftButton();
            return true;
        }
        if (bR){
            handleRightButton();
            return true;
        }
        if (isGameEnd){
            if (b){
                int[] array = new int[1];
                array[0] = 1;
                ChangeBlockEntityDataC2SPacket.send(blockEntity.getBlockPos(),array);
                Minecraft.getInstance().getSoundManager()
                        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                this.onClose();
                return true;
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
            if (minecraft.options.keyInventory.matches(keyCode, scanCode)) {
                this.onClose();
                return true;
            }
            if (minecraft.options.keyUp.matches(keyCode, scanCode) || keyCode == GLFW.GLFW_KEY_UP){
                this.handleUpButton();
                return true;
            }
            if (minecraft.options.keyDown.matches(keyCode, scanCode) || keyCode == GLFW.GLFW_KEY_DOWN){
                this.handleDownButton();
                return true;
            }
            if (minecraft.options.keyLeft.matches(keyCode, scanCode) || keyCode == GLFW.GLFW_KEY_LEFT){
                this.handleLeftButton();
                return true;
            }
            if (minecraft.options.keyRight.matches(keyCode, scanCode) || keyCode == GLFW.GLFW_KEY_RIGHT){
                this.handleRightButton();
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
