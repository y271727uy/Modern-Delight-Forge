package com.y271727uy.moderndelight.block.kitchenware.steaming;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.kitchenware.gas_cooking.gas_cooking_stove.BurningGasCookingStoveBlockEntity;
import com.y271727uy.moderndelight.networking.packet.ItemStackSyncS2CPacket;
import com.y271727uy.moderndelight.recipe.custom.SteamingRecipe;
import com.y271727uy.moderndelight.screen.custom.BambooSteamerScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class BambooGrateBlockEntity extends BlockEntity implements net.minecraft.world.Container, net.minecraft.world.MenuProvider {
    public BambooGrateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BAMBOO_GRATE_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                if (index >= 0 && index <= 15){
                    return BambooGrateBlockEntity.this.progresses[index];
                } else if (index >= 16 && index <=31){
                    return BambooGrateBlockEntity.this.maxProgresses[index-16];
                } else {
                    return switch (index){
                        case 32 -> BambooGrateBlockEntity.this.isCovered;
                        case 33 -> BambooGrateBlockEntity.this.isHeated;
                        case 34 -> BambooGrateBlockEntity.this.currentLayer;
                        default -> 0;
                    };
                }
            }

            @Override
            public void set(int index, int value) {}

            @Override
            public int getCount() {
                return 35;
            }
        };
    }
    public static final String NAME = "display_name.moderndelight.steamer_name";
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(16,ItemStack.EMPTY);
    private final ContainerData propertyDelegate;
    private int isCovered = 0;
    private int isHeated = 0;
    private int currentLayer = 0;
    private final int[] progresses = new int[16];
    private final int[] maxProgresses = new int[16];
    private int availableSlots = 4;
    
    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : inventory) if(!stack.isEmpty()) return false;
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(inventory, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(inventory, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return net.minecraft.world.Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        inventory.clear();
        setChanged();
    }
    
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putInt("bamboo_grate.currentLayer",currentLayer);
        nbt.putInt("bamboo_grate.isHeated",isHeated);
        nbt.putInt("bamboo_grate.isCovered",isCovered);
        nbt.putIntArray("bamboo_grate.progresses",progresses);
    }
    
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.inventory.clear();
        ContainerHelper.loadAllItems(nbt, inventory);
        this.currentLayer = nbt.getInt("bamboo_grate.currentLayer");
        this.isHeated = nbt.getInt("bamboo_grate.isHeated");
        this.isCovered = nbt.getInt("bamboo_grate.isCovered");
        int[] temp = nbt.getIntArray("bamboo_grate.progresses");
        int max = progresses.length;
        if (temp.length < max){
            max = temp.length;
        }
        System.arraycopy(temp, 0, progresses, 0, max);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        if (level != null){
            return new BambooSteamerScreenHandler(syncId, playerInventory, this, level.getBlockState(worldPosition).getValue(BambooGrateBlock.LAYER).intValue(), propertyDelegate);
        } else return null;
    }
    
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (!world.isClientSide){
            return;
        }
        if (world.getGameTime() % 20L == 0L){
            this.availableSlots = state.getValue(BambooGrateBlock.LAYER) * 4;
            if (this.isHeated !=0 && this.isCovered !=0){
                for (int i = 0;i < availableSlots;i++){
                    net.minecraft.world.SimpleContainer inventory = new net.minecraft.world.SimpleContainer(this.getItem(i));
                    Optional<SteamingRecipe> match = Objects.requireNonNull(this.getLevel()).getRecipeManager()
                            .getRecipeFor(SteamingRecipe.Type.INSTANCE, inventory,this.getLevel());
                    if (match.isPresent()){
                        int maxProgress = match.get().getMaxProgress();
                        int count = this.getItem(i).getCount();
                        if (count <= 4){
                            this.maxProgresses[i] = maxProgress;
                        } else {
                            this.maxProgresses[i] = maxProgress * count / 4;
                        }
                        if (this.progresses[i] < this.maxProgresses[i]){
                            this.progresses[i]++;
                        } else {
                            this.progresses[i] = 0;
                            this.setItem(i,new ItemStack(match.get().getResultItem(null).getItem(),count));
                        }
                    } else {
                        this.progresses[i] = 0;
                    }
                }
            }
            if (!(world.getBlockEntity(pos.below()) instanceof BambooGrateBlockEntity) &&
                    world.getBlockEntity(pos.below(2)) instanceof BurningGasCookingStoveBlockEntity &&
                    world.getBlockState(pos.below()).getBlock().equals(Blocks.WATER_CAULDRON)){
                    this.isHeated = 1;
            } else {
                if (world.getBlockEntity(pos.below()) instanceof BambooGrateBlockEntity blockEntity){
                    if (blockEntity.isHeated == 1 && blockEntity.currentLayer != 0 &&
                    world.getBlockState(pos.below()).getValue(BambooGrateBlock.LAYER) == 4){
                        this.isHeated = 1;
                    } else this.isHeated = 0;
                } else this.isHeated = 0;
            }
            if(state.getValue(BambooGrateBlock.COVERED)){
                this.isCovered = 1;
                this.currentLayer = 1;
            } else {
                if (world.getBlockState(pos.above()).getBlock().equals(ModBlocks.BAMBOO_GRATE.get())
                        && state.getValue(BambooGrateBlock.LAYER) == 4){
                    if (world.getBlockState(pos.above()).getValue(BambooGrateBlock.COVERED)){
                        this.isCovered = 1;
                        this.currentLayer = 2;
                    } else {
                        if (world.getBlockState(pos.above(2)).getBlock().equals(ModBlocks.BAMBOO_GRATE.get()) &&
                                world.getBlockState(pos.above()).getValue(BambooGrateBlock.LAYER) == 4){
                            if (world.getBlockState(pos.above(2)).getValue(BambooGrateBlock.COVERED)){
                                this.isCovered = 1;
                                this.currentLayer = 3;
                            } else {
                                this.isCovered = 0;
                                if (world.getBlockState(pos.above(3)).getBlock().equals(ModBlocks.BAMBOO_GRATE.get())){
                                    this.currentLayer = 0;
                                } else this.currentLayer = 3;
                            }
                        } else {
                            this.isCovered = 0;
                            this.currentLayer = 2;
                        }
                    }
                } else {
                    this.isCovered = 0;
                    this.currentLayer = 1;
                }
            }
            setChanged();
        }
    }

    @Override
    public void setChanged() {
        ItemStackSyncS2CPacket.send(worldPosition,getItems(),level);
        super.setChanged();
    }
    
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }
}
