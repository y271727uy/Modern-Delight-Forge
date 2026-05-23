package com.y271727uy.moderndelight.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.networking.packet.ChangeBlockEntityDataC2SPacket;
import com.y271727uy.moderndelight.recipe.custom.CuisineRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import org.apache.commons.compress.utils.Lists;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class CuisineTableScreen extends AbstractContainerScreen<CuisineTableScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModernDelightMain.MOD_ID,
            "textures/gui/cuisine_table_gui.png");
    private int selectedRecipe;
    private List<CuisineRecipe> availableRecipes;
    private int index;
    private int totalPage;
    private boolean prePageBtn;
    private boolean nextPageBtn;
    public CuisineTableScreen(CuisineTableScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.selectedRecipe = -1;
        this.index = 0;
        this.totalPage = 0;
        this.prePageBtn = false;
        this.nextPageBtn = false;
        this.availableRecipes = Lists.newArrayList();
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
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        if (this.hasRecipe()){
            renderRecipeSlot(guiGraphics,x,y);
            renderMouseSelectSlot(guiGraphics,x,y,mouseX,mouseY);
            renderSelectedRecipeSlot(guiGraphics,x,y);
            renderRecipeItem(guiGraphics,x,y);
            popRes();
        } else this.resetAll();
        renderFlipButton(guiGraphics,x,y);
        renderMouseHoverFlipButton(guiGraphics,x,y,mouseX,mouseY);
        if (mouseX >= x + 161 && mouseX <= x + 171 && mouseY >= y + 5 && mouseY <= y + 15){
            guiGraphics.blit(TEXTURE,x+161,y+5,194,13,11,11);
        }
    }
    public boolean hasRecipe(){
        SimpleContainer tempINV = new SimpleContainer(2);
        for (int i=0;i<tempINV.getContainerSize();i++){
            tempINV.setItem(i,menu.blockEntity.getItem(i));
        }
        Optional<CuisineRecipe> match = Objects.requireNonNull(menu.blockEntity.getLevel()).getRecipeManager()
                .getRecipeFor(CuisineRecipe.Type.INSTANCE, tempINV, menu.blockEntity.getLevel());
        if (match.isPresent()){
            this.availableRecipes = Objects.requireNonNull(menu.blockEntity.getLevel())
                    .getRecipeManager().getRecipesFor(
                            CuisineRecipe.Type.INSTANCE, tempINV, menu.blockEntity.getLevel());
            return true;
        } else return false;
    }
    private void renderMouseSelectSlot(GuiGraphics guiGraphics, int x, int y, int mouseX,int mouseY) {
        int count = this.getAvailableRecipeCounts() - 8 * index;
        for (int i = 0; i < Math.min(count, 8); i++) {
            int offsetX = (i % 4) * 18;
            int offsetY = (i / 4) * 18;
            int slotX = x + 48 + offsetX;
            int slotY = y + 16 + offsetY;
            boolean slot = mouseX >= slotX && mouseX <= slotX + 17 && mouseY >= slotY && mouseY <= slotY + 17;
            if (slot) {
                guiGraphics.blit(TEXTURE, slotX, slotY, 36, 166, 18, 18);
            }
        }
    }
    private void renderRecipeSlot(GuiGraphics guiGraphics, int x, int y) {
        int count = this.getAvailableRecipeCounts() - 8 * index;
        for (int i = 0; i < Math.min(count, 8); i++) {
            int offsetX = (i % 4) * 18;
            int offsetY = (i / 4) * 18;
            guiGraphics.blit(TEXTURE, x + 48 + offsetX, y + 16 + offsetY, 18, 166, 18, 18);
        }
    }
    public int getSelectedRecipe(){
        return this.selectedRecipe;
    }
    public List<CuisineRecipe> getAvailableRecipes(){
        return availableRecipes;
    }
    public int getAvailableRecipeCounts(){
        return availableRecipes.size();
    }
    public void resetAll() {
        this.availableRecipes.clear();
        this.selectedRecipe = -1;
        this.nextPageBtn = false;
        this.prePageBtn = false;
        this.index = 0;
        this.totalPage = 0;
        menu.populateResult(ItemStack.EMPTY);
    }
    private void renderRecipeItem(GuiGraphics guiGraphics,int x,int y){
        int count = this.getAvailableRecipeCounts() - 8 * index;
        for (int i = 0; i < Math.min(count, 8); i++) {
            int offsetX = (i % 4) * 18;
            int offsetY = (i / 4) * 18;
            if (isInBounds(i + index * 8)){
                guiGraphics.renderItem(this.getAvailableRecipes().get(i + index * 8).getResultItem(menu.blockEntity.getLevel().registryAccess()).copy(),
                        x + 49 + offsetX, y + 17 + offsetY);
            } else {
                resetAll();
            }
        }
    }
    private void renderSelectedRecipeSlot(GuiGraphics guiGraphics, int x, int y) {
        switch (this.getSelectedRecipe()){
            case 0 -> guiGraphics.blit(TEXTURE, x + 48, y + 16, 0, 166, 18, 18);
            case 1 -> guiGraphics.blit(TEXTURE, x + 66, y + 16, 0, 166, 18, 18);
            case 2 -> guiGraphics.blit(TEXTURE, x + 84, y + 16, 0, 166, 18, 18);
            case 3 -> guiGraphics.blit(TEXTURE, x + 102, y + 16, 0, 166, 18, 18);
            case 4 -> guiGraphics.blit(TEXTURE, x + 48, y + 34, 0, 166, 18, 18);
            case 5 -> guiGraphics.blit(TEXTURE, x + 66, y + 34, 0, 166, 18, 18);
            case 6 -> guiGraphics.blit(TEXTURE, x + 84, y + 34, 0, 166, 18, 18);
            case 7 -> guiGraphics.blit(TEXTURE, x + 102, y + 34, 0, 166, 18, 18);
        }
    }
    private boolean isInBounds(int id){
        return id < this.getAvailableRecipes().size() && id >= 0;
    }
    private void popRes(){
        if (this.getSelectedRecipe() >= 0){
            if (isInBounds(this.getSelectedRecipe() + 8 * index)){
                menu.populateResult(this.getAvailableRecipes().get(this.getSelectedRecipe() + 8 * index).getResultItem(menu.blockEntity.getLevel().registryAccess()).copy());
            } else {
                resetAll();
            }
        } else {
            menu.populateResult(ItemStack.EMPTY);
        }
    }
    private void renderMouseHoverFlipButton(GuiGraphics guiGraphics, int x,int y, int mouseX, int mouseY){
        if (prePageBtn && mouseX >= x + 122 && mouseY >= y + 15 && mouseX <= x + 131 && mouseY <= y + 25){
            guiGraphics.blit(TEXTURE, x + 122, y + 15, 54 ,177, 10 ,11);
        }
        if (nextPageBtn && mouseX >= x + 133 && mouseY >= y + 15 && mouseX <= x + 142 && mouseY <= y + 25){
            guiGraphics.blit(TEXTURE, x + 133, y + 15, 65 ,177, 10 ,11);
        }
    }
    private void renderFlipButton(GuiGraphics guiGraphics, int x, int y) {
        if (this.getAvailableRecipeCounts()>8){
            this.totalPage = this.getAvailableRecipeCounts() / 8;
            if (index == 0) {
                guiGraphics.blit(TEXTURE,x + 122,y + 15,96,166,21,11);
                this.prePageBtn = false;
                this.nextPageBtn = true;
            } else if (index < totalPage) {
                guiGraphics.blit(TEXTURE,x + 122,y + 15,54,166,21,11);
                this.prePageBtn = true;
                this.nextPageBtn = true;
            } else if (index == totalPage){
                guiGraphics.blit(TEXTURE,x + 122,y + 15,75,166,21,11);
                this.prePageBtn = true;
                this.nextPageBtn = false;
            }
        } else {
            this.prePageBtn = false;
            this.nextPageBtn = false;
            this.index = 0;
            this.totalPage = 0;
        }
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.hasRecipe()){
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;
            int count = this.getAvailableRecipeCounts() - 8 * this.index;
            for (int i = 0; i < Math.min(count, 8); i++) {
                int offsetX = (i % 4) * 18;
                int offsetY = (i / 4) * 18;
                int slotX = x + 48 + offsetX;
                int slotY = y + 16 + offsetY;
                boolean slot = mouseX >= slotX && mouseX <= slotX + 17 && mouseY >= slotY && mouseY <= slotY + 17;
                if (slot && button == 0) {
                    Minecraft.getInstance().getSoundManager()
                            .play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.selectedRecipe = i;
                    if (isInBounds(i + 8 * index)){
                        menu.populateResult(this.getAvailableRecipes().get(i + 8 * index).getResultItem(menu.blockEntity.getLevel().registryAccess()).copy());
                    } else resetAll();
                    return true;
                }
            }
            if (mouseX >= x + 122 && mouseY >= y + 15 && mouseX <= x + 131 && mouseY <= y + 25 && button == 0){
                if (this.prePageBtn){
                    this.index--;
                    this.selectedRecipe = -1;
                    Minecraft.getInstance().getSoundManager()
                            .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    return true;
                }
            }
            if (mouseX >= x + 133 && mouseY >= y + 15 && mouseX <= x + 142 && mouseY <= y + 25 && button == 0){
                if (this.nextPageBtn){
                    this.index++;
                    this.selectedRecipe = -1;
                    Minecraft.getInstance().getSoundManager()
                            .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
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

    @Override
    public void onClose() {
        int[] array = new int[1];
        array[0] = 1;
        ChangeBlockEntityDataC2SPacket.send(menu.blockEntity.getBlockPos(), array);
        super.onClose();
    }
}
