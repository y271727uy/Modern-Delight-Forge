package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.networking.packet.ChangeBlockEntityDataC2SPacket;
import com.y271727uy.moderndelight.networking.packet.UpdateInventoryC2SPacket;
import com.y271727uy.moderndelight.recipe.custom.AssemblyRecipe;
import com.y271727uy.moderndelight.util.MiscUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;

import java.util.Objects;
import java.util.Optional;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ElectriciansDeskScreen extends AbstractContainerScreen<ElectriciansDeskScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/electricians_desk_gui.png");
    public ElectriciansDeskScreen(ElectriciansDeskScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }
    private ItemStack outputItem;
    private int miniGmeType = 0;
    private int goal = 0;
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
        renderButton(guiGraphics,x,y,mouseX,mouseY);
        if (this.hasRecipe()){
            guiGraphics.renderItem(this.outputItem,x + 114,y + 26);
        }
    }
    public boolean hasRecipe(){
        SimpleContainer tempINV = new SimpleContainer(6);
        for (int i=0;i<tempINV.getContainerSize();i++){
            tempINV.setItem(i,menu.blockEntity.getItem(i));
        }
        Optional<AssemblyRecipe> match = Objects.requireNonNull(menu.blockEntity.getLevel()).getRecipeManager()
                .getRecipeFor(AssemblyRecipe.Type.INSTANCE, tempINV, menu.blockEntity.getLevel());
        if (match.isPresent()){
            this.outputItem = new ItemStack(match.get().getResultItem(null).getItem(),
                    match.get().getResultItem(null).getCount());
            this.miniGmeType = match.get().getMiniGameType();
            this.goal = match.get().getGoal();
            return true;
        } else {
            this.outputItem = ItemStack.EMPTY;
            this.miniGmeType = 0;
            this.goal = 0;
            return false;
        }
    }
    private void renderButton(GuiGraphics guiGraphics, int x, int y, int mouseX, int mouseY) {
        boolean b = mouseX >= x + 112 && mouseY >= y + 24 && mouseX <= x + 131 && mouseY <= y + 43;
        if (menu.isOccupied()){
            guiGraphics.blit(TEXTURE, x + 112, y + 24, 176, 40, 20,20);
        } else {
            if (menu.canCraft()){
                if (!this.hasRecipe()){
                    int[] array = new int[1];
                    array[0] = 5;
                    ChangeBlockEntityDataC2SPacket.send(menu.blockEntity.getBlockPos(),array);
                } else {
                    guiGraphics.blit(TEXTURE, x + 112, y + 24, 176, 0, 20 ,20);
                    if (b){
                        guiGraphics.blit(TEXTURE,x + 112,y + 24, 196, 0, 20, 20);
                    }
                }
            } else {
                if (this.hasPaperAndInk() && this.hasRecipe() && menu.blockEntity.getItem(8).isEmpty()){
                    guiGraphics.blit(TEXTURE,x + 112,y + 24, 176, 20, 20, 20);
                    if (b){
                        guiGraphics.blit(TEXTURE,x + 112,y + 24, 196, 20, 20, 20);
                    }
                }
            }
        }
    }
    private boolean hasPaperAndInk(){
        return menu.blockEntity.getItem(6).is(Items.PAPER) &&
                MiscUtil.isInk(menu.blockEntity.getItem(7).getItem());
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        boolean b = mouseX >= x + 112 && mouseY >= y + 24 && mouseX <= x + 131 && mouseY <= y + 43;
        if (b){
            if (menu.canCraft()){

                int[] array = new int[1];
                array[0] = 2;
                UpdateInventoryC2SPacket.send(menu.blockEntity.getBlockPos(),this.outputItem);
                ChangeBlockEntityDataC2SPacket.send(menu.blockEntity.getBlockPos(),array);
                Minecraft.getInstance().getSoundManager()
                        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            } else {
                if (this.hasRecipe() && this.hasPaperAndInk() && menu.blockEntity.getItem(8).isEmpty() && !menu.isOccupied()){
                    Minecraft.getInstance().getSoundManager()
                            .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    Screen currentScreen = Minecraft.getInstance().screen;
                    int [] array = new int[1];
                    array[0] = 3;
                    ChangeBlockEntityDataC2SPacket.send(menu.blockEntity.getBlockPos(),array);
                    switch (this.miniGmeType){
                        case 1 -> Minecraft.getInstance().setScreen(
                                new MiniGame1Screen(ModBlocks.ELECTRICIANS_DESK.get().getName(),
                                currentScreen,menu.blockEntity,goal));
                        case 2 -> Minecraft.getInstance().setScreen(
                                new MiniGame2Screen(ModBlocks.ELECTRICIANS_DESK.get().getName(),
                                        currentScreen,menu.blockEntity,goal));
                        case 3 -> Minecraft.getInstance().setScreen(
                                new MiniGame3Screen(ModBlocks.ELECTRICIANS_DESK.get().getName(),
                                        currentScreen,menu.blockEntity,goal));
                    }
                    return true;
                }
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
