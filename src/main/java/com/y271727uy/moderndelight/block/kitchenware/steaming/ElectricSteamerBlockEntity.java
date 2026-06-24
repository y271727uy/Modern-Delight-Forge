package com.y271727uy.moderndelight.block.kitchenware.steaming;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.networking.packet.ItemStackSyncS2CPacket;
import com.y271727uy.moderndelight.recipe.custom.SteamingRecipe;
import com.y271727uy.moderndelight.screen.custom.ElectricSteamerScreenHandler;
import com.y271727uy.moderndelight.util.FluidStack;
import com.y271727uy.moderndelight.util.fluid.FluidConstants;
import com.y271727uy.moderndelight.util.fluid.FluidVariant;
import com.y271727uy.moderndelight.util.fluid.SingleVariantStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.SimpleContainer;
import com.y271727uy.moderndelight.util.block_util.power_util.ACConsumer;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class ElectricSteamerBlockEntity extends BlockEntity implements MenuProvider, net.minecraft.world.Container, ACConsumer {
    public ElectricSteamerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ELECTRIC_STEAMER_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                if (index>=0 && index <= 11){
                    return ElectricSteamerBlockEntity.this.progresses[index];
                } else if (index>=12 && index <=23) {
                    return ElectricSteamerBlockEntity.this.maxProgresses[index-12];
                } else {
                    return switch (index){
                        case 24 -> ElectricSteamerBlockEntity.this.water;
                        case 25 -> ElectricSteamerBlockEntity.this.steam;
                        case 26 -> ElectricSteamerBlockEntity.this.steamProgress;
                        default -> 0;
                    };
                }
            }

            @Override
            public void set(int index, int value) {}

            @Override
            public int getCount() {
                return 27;
            }
        };
    }
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(13, ItemStack.EMPTY);
    private int cachedPower = 0;
    private final int[] progresses = new int[12];
    private final int[] maxProgresses = new int[12];
    private int water = 0;
    private int steam = 0;
    private int steamProgress = 0;
    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.of(net.minecraft.world.level.material.Fluids.WATER);
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidConstants.BUCKET;
        }
        @Override
        protected void onFinalCommit() {
            setChanged();
        }
    };
    protected final ContainerData propertyDelegate;
    public static final int MAX_STEAM_PROGRESS = 60;
    public static final int MAX_WATER_OR_STEAM = 1000;
    public static final int WATER_SLOT = 12;
    
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
        nbt.putInt("electric_steamer.cachedPower",cachedPower);
        nbt.putIntArray("electric_steamer.progresses",progresses);
        nbt.put("electric_steamer.fluid_variant",fluidStorage.variant.toNbt());
        nbt.putLong("electric_steamer.fluid_amount",fluidStorage.amount);
        nbt.putInt("electric_steamer.steam",steam);
        nbt.putInt("electric_steamer.steamProgress",steamProgress);
    }
    
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.inventory.clear();
        ContainerHelper.loadAllItems(nbt, inventory);
        cachedPower = nbt.getInt("electric_steamer.cachedPower");
        int[] temp = nbt.getIntArray("electric_steamer.progresses");
        int max = progresses.length;
        if (temp.length < max){
            max = temp.length;
        }
        System.arraycopy(temp, 0, progresses, 0, max);
        fluidStorage.variant = FluidVariant.fromNbt((CompoundTag) nbt.get("electric_steamer.fluid_variant"));
        fluidStorage.amount = nbt.getLong("electric_steamer.fluid_amount");
        steam = nbt.getInt("electric_steamer.steam");
        steamProgress = nbt.getInt("electric_steamer.steamProgress");
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
    
    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide){
            return;
        }
        if (fluidStorage.variant.getFluid() == net.minecraft.world.level.material.Fluids.WATER){
            water = (int) FluidStack.convertDropletsToMb(fluidStorage.amount);
        } else water = 0;
        if (cachedPower > 0){
            cachedPower--;
            world.setBlock(pos,state.setValue(ElectricSteamerBlock.IS_WORKING,true), 3);
            if (fluidStorage.amount > FluidStack.convertMbToDroplets(50) &&
                    steam < MAX_WATER_OR_STEAM &&
                    fluidStorage.variant.getFluid() == net.minecraft.world.level.material.Fluids.WATER){
                steamProgress++;
                if (steamProgress >= MAX_STEAM_PROGRESS){
                    steamProgress = 0;
                    fluidStorage.amount-= FluidStack.convertMbToDroplets(50);
                    if (steam + 80 < MAX_WATER_OR_STEAM){
                        steam+=80;
                    } else {
                        steam = MAX_WATER_OR_STEAM;
                    }
                }
            } else {
                steamProgress = 0;
            }
        } else world.setBlock(pos,state.setValue(ElectricSteamerBlock.IS_WORKING,false), 3);
        if (getItem(WATER_SLOT).getItem().equals(Items.WATER_BUCKET)){
            if (fillWater(world,pos)){
                setItem(WATER_SLOT,Items.BUCKET.getDefaultInstance());
            }
        }
        if (world.getGameTime() %20L == 0L){
            if (steam > 5){
                for (int i = 0; i < 12; i++){
                    SimpleContainer inventory = new SimpleContainer(this.getItem(i));
                    Optional<SteamingRecipe> match = Objects.requireNonNull(this.getLevel()).getRecipeManager()
                            .getRecipeFor(SteamingRecipe.Type.INSTANCE, inventory,this.getLevel());
                    if (match.isPresent()){
                        int maxProgress = match.get().getMaxProgress();
                        int count = this.getItem(i).getCount();
                        if (count <= 16){
                            this.maxProgresses[i] = maxProgress;
                        } else {
                            this.maxProgresses[i] = maxProgress * count / 16;
                        }
                        if (this.progresses[i] < this.maxProgresses[i]){
                            this.progresses[i]++;
                            this.steam -=5;
                        } else {
                            this.progresses[i] = 0;
                            this.maxProgresses[i] = 0;
                            this.setItem(i,new ItemStack(match.get().getResultItem(null).getItem(),count));
                        }
                    } else {
                        this.progresses[i] = 0;
                        this.maxProgresses[i] = 0;
                    }
                }
                setChanged();
            }
        }
    }

    @Override
    public void setChanged() {
        ItemStackSyncS2CPacket.send(worldPosition,getItems(),level);
        super.setChanged();
    }

    public boolean fillWater(Level world, BlockPos pos) {
        if (fluidStorage.variant.isBlank() ||
                (fluidStorage.amount < fluidStorage.getCapacity() &&
                        fluidStorage.variant.getFluid() == net.minecraft.world.level.material.Fluids.WATER)){
            world.playSound(null,pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS,1.0f,1.0f);
            fluidStorage.variant = FluidVariant.of(net.minecraft.world.level.material.Fluids.WATER);
            fluidStorage.amount = fluidStorage.getCapacity();
            return true;
        } else return false;
    }

    @Override
    public long getConsumedValue() {
        return 10;
    }

    @Override
    public boolean isWorking() {
        return true;
    }

    @Override
    public void energize() {
        cachedPower = 60;
    }

    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal(" ");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ElectricSteamerScreenHandler(syncId,playerInventory,this,propertyDelegate);
    }
}
