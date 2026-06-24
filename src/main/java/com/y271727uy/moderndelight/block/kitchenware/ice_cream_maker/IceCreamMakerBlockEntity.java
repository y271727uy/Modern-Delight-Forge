package com.y271727uy.moderndelight.block.kitchenware.ice_cream_maker;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.util.block_util.power_util.ACConsumer;
import com.y271727uy.moderndelight.util.block_util.ImplementedInventory;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.item.food.CreamItem;
import com.y271727uy.moderndelight.screen.custom.IceCreamMakerScreenHandler;
import com.y271727uy.moderndelight.util.enums.CreamFlavor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import javax.annotation.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IceCreamMakerBlockEntity extends BlockEntity implements GeoBlockEntity, ACConsumer, ImplementedInventory, MenuProvider {
    public IceCreamMakerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ICE_CREAM_MAKER_BLOCK_ENTITY.get(), pos, state);
        propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0 -> IceCreamMakerBlockEntity.this.isPowered;
                    case 1 -> IceCreamMakerBlockEntity.this.progress;

                    case 2 -> IceCreamMakerBlockEntity.this.iceCream1.getFlavor().getId();
                    case 3 -> IceCreamMakerBlockEntity.this.iceCream1.getAmount();
                    case 4 -> IceCreamMakerBlockEntity.this.iceCream1.getSelected();

                    case 5 -> IceCreamMakerBlockEntity.this.iceCream2.getFlavor().getId();
                    case 6 -> IceCreamMakerBlockEntity.this.iceCream2.getAmount();
                    case 7 -> IceCreamMakerBlockEntity.this.iceCream2.getSelected();

                    case 8 -> IceCreamMakerBlockEntity.this.iceCream3.getFlavor().getId();
                    case 9 -> IceCreamMakerBlockEntity.this.iceCream3.getAmount();
                    case 10 -> IceCreamMakerBlockEntity.this.iceCream3.getSelected();
                    default -> 0;
                };
            }
            @Override
            public void set(int index, int value) {}
            @Override
            public int getCount() {
                return 11;
            }
        };
    }
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final ContainerData propertyDelegate;
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation START = RawAnimation.begin().thenPlay("start");
    private int isPowered = 0;
    private final IceCream iceCream1 = new IceCream();
    private final IceCream iceCream2 = new IceCream();
    private final IceCream iceCream3 = new IceCream();
    private int progress = 0;

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(5, ItemStack.EMPTY);
    private int ticker = 0;
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> {
            if (Objects.requireNonNull(state.getAnimatable().getBlockState().getValue(IceCreamMakerBlock.START))) {
                return state.setAndContinue(START);
            } else {
                return state.setAndContinue(IDLE);
            }
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide){
            return;
        }
        if (isPowered > 0){
            this.isPowered--;
        } else {
            if (this.progress > 0){
                progress--;
            }
        }
        if (state.getValue(IceCreamMakerBlock.START)){
            ticker++;
            if (ticker >= 60){
                Block.popResource(world,pos, this.getItem(4));
                this.setItem(4,ItemStack.EMPTY);
                world.setBlock(pos,state.setValue(IceCreamMakerBlock.START,false), 3);
            }
        } else ticker = 0;
        if (world.getGameTime() %20L == 0L){
            if (this.getItem(0).getItem() instanceof CreamItem cream){
                CreamFlavor creamFlavor = cream.getFlavor();
                int findNull = findNull();
                int findSameFlavor = findSameFlavor(creamFlavor);
                if (hasRecipe() && (findNull != 0 || findSameFlavor != 0)){
                    progress++;
                    int maxProgress = 10;
                    if (progress >= maxProgress){
                        if (findSameFlavor != 0){
                            switch (findSameFlavor){
                                case 1 -> this.iceCream1.changeAmount(100);
                                case 2 -> this.iceCream2.changeAmount(100);
                                case 3 -> this.iceCream3.changeAmount(100);
                            }
                        } else {
                            switch (findNull){
                                case 1 -> {
                                    this.iceCream1.setFlavor(creamFlavor);
                                    this.iceCream1.setAmount(100);
                                }
                                case 2 -> {
                                    this.iceCream2.setFlavor(creamFlavor);
                                    this.iceCream2.setAmount(100);
                                }
                                case 3 -> {
                                    this.iceCream3.setFlavor(creamFlavor);
                                    this.iceCream3.setAmount(100);
                                }
                            }
                        }
                        this.removeItem(0, 1);
                        this.removeItem(1, 1);
                        this.removeItem(2, 1);
                        ItemStack stack3 = this.getItem(3);
                        int count = stack3.getCount();
                        if (stack3.isEmpty()){
                            this.setItem(3,Items.BOWL.getDefaultInstance());
                        } else {
                            if (stack3.getItem() == Items.BOWL && count < stack3.getMaxStackSize()){
                                this.setItem(3,new ItemStack(Items.BOWL,count+1));
                            } else Block.popResource(world,pos,new ItemStack(Items.BOWL));
                        }
                        progress = 0;
                    }
                } else progress = 0;
            } else progress = 0;
            setChanged();
        }
    }
    private int findSameFlavor(CreamFlavor creamFlavor){
        if (this.iceCream1.getFlavor() == creamFlavor && this.iceCream1.getAmount() != 1000){
            return 1;
        }
        if (this.iceCream2.getFlavor() == creamFlavor && this.iceCream2.getAmount() != 1000) {
            return 2;
        }
        if (this.iceCream3.getFlavor() == creamFlavor && this.iceCream3.getAmount() != 1000) {
            return 3;
        }
        return 0;
    }
    private int findNull(){
        if (this.iceCream1.getFlavor().getId() == -1){
            return 1;
        }
        if (this.iceCream2.getFlavor().getId() == -1) {
            return 2;
        }
        if (this.iceCream3.getFlavor().getId() == -1) {
            return 3;
        }
        return 0;
    }
    private boolean hasRecipe(){
        return this.isPowered != 0 &&
                this.getItem(1).getItem() == Items.SUGAR &&
                this.getItem(2).getItem() == Items.EGG;
    }
    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        // Save inventory items
        for (int i = 0; i < inventory.size(); i++) {
            nbt.put("Item" + i, inventory.get(i).save(new CompoundTag()));
        }
        nbt.putInt("ice_cream_maker.progress",this.progress);

        nbt.putInt("ice_cream_maker.ice_cream1.flavor",this.iceCream1.getFlavor().getId());
        nbt.putInt("ice_cream_maker.ice_cream1.amount",this.iceCream1.getAmount());
        nbt.putBoolean("ice_cream_maker.ice_cream1.selected",this.iceCream1.isSelected());

        nbt.putInt("ice_cream_maker.ice_cream2.flavor",this.iceCream2.getFlavor().getId());
        nbt.putInt("ice_cream_maker.ice_cream2.amount",this.iceCream2.getAmount());
        nbt.putBoolean("ice_cream_maker.ice_cream2.selected",this.iceCream2.isSelected());

        nbt.putInt("ice_cream_maker.ice_cream3.flavor",this.iceCream3.getFlavor().getId());
        nbt.putInt("ice_cream_maker.ice_cream3.amount",this.iceCream3.getAmount());
        nbt.putBoolean("ice_cream_maker.ice_cream3.selected",this.iceCream3.isSelected());
    }
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        // Load inventory items
        for (int i = 0; i < inventory.size(); i++) {
            if (nbt.contains("Item" + i)) {
                inventory.set(i, ItemStack.of(nbt.getCompound("Item" + i)));
            }
        }
        this.progress = nbt.getInt("ice_cream_maker.progress");

        this.iceCream1.setFlavor(CreamFlavor.getFlavorByID(nbt.getInt("ice_cream_maker.ice_cream1.flavor")));
        this.iceCream1.setAmount(nbt.getInt("ice_cream_maker.ice_cream1.amount"));
        this.iceCream1.setSelected(nbt.getBoolean("ice_cream_maker.ice_cream1.selected"));

        this.iceCream2.setFlavor(CreamFlavor.getFlavorByID(nbt.getInt("ice_cream_maker.ice_cream2.flavor")));
        this.iceCream2.setAmount(nbt.getInt("ice_cream_maker.ice_cream2.amount"));
        this.iceCream2.setSelected(nbt.getBoolean("ice_cream_maker.ice_cream2.selected"));

        this.iceCream3.setFlavor(CreamFlavor.getFlavorByID(nbt.getInt("ice_cream_maker.ice_cream3.flavor")));
        this.iceCream3.setAmount(nbt.getInt("ice_cream_maker.ice_cream3.amount"));
        this.iceCream3.setSelected(nbt.getBoolean("ice_cream_maker.ice_cream3.selected"));
    }
    @Override
    public long getConsumedValue() {
        return 20;
    }

    @Override
    public boolean isWorking() {
        return true;
    }

    @Override
    public void energize() {
        this.isPowered = 100;
    }

    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    public void writeScreenOpeningData(Player player, FriendlyByteBuf buf) {
        buf.writeBlockPos(worldPosition);
    }

    @Override
    public Component getDisplayName() {
        return ModBlocks.ICE_CREAM_MAKER.get().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new IceCreamMakerScreenHandler(syncId,playerInventory,this,(ContainerData)propertyDelegate);
    }
    public static final String NEED_CONE = "moderndelight.ice_cream_maker.need_cone";
    public static final String NEED_SELECT = "moderndelight.ice_cream_maker.need_select";

    public void tryStart(BlockState state, Level world, Player player) {
        if (!state.getValue(IceCreamMakerBlock.START)){
            if (player.getMainHandItem().getItem() != ModItems.ICE_CREAM_CONE.get()){
                player.displayClientMessage(Component.translatable(NEED_CONE),true);
                return;
            }
            List<CreamFlavor> creamFlavors = new ArrayList<>();
            if (iceCream1.isSelected() && iceCream1.getFlavor() != CreamFlavor.NULL && iceCream1.getAmount() >= 50){
                creamFlavors.add(iceCream1.getFlavor());
                iceCream1.changeAmount(-50);
            }
            if (iceCream2.isSelected() && iceCream2.getFlavor() != CreamFlavor.NULL && iceCream2.getAmount() >= 50){
                creamFlavors.add(iceCream2.getFlavor());
                iceCream2.changeAmount(-50);
            }
            if (iceCream3.isSelected() && iceCream3.getFlavor() != CreamFlavor.NULL && iceCream3.getAmount() >= 50){
                creamFlavors.add(iceCream3.getFlavor());
                iceCream3.changeAmount(-50);
            }
            if (creamFlavors.isEmpty()){
                player.displayClientMessage(Component.translatable(NEED_SELECT),true);
                return;
            }
            ItemStack iceCream = new ItemStack(ModItems.ICE_CREAM.get());
            for (CreamFlavor creamFlavor : creamFlavors){
                CreamFlavor.addFlavorToFood(iceCream, creamFlavor);
            }
            this.setItem(4,iceCream);
            player.getMainHandItem().shrink(1);
            world.setBlock(worldPosition,state.setValue(IceCreamMakerBlock.START,true), 3);
        }
    }

    public void changeIceCream1() {
        this.iceCream1.setSelected(!this.iceCream1.isSelected());
    }
    public void changeIceCream2() {
        this.iceCream2.setSelected(!this.iceCream2.isSelected());
    }
    public void changeIceCream3() {
        this.iceCream3.setSelected(!this.iceCream3.isSelected());
    }

    public static class IceCream {
        public IceCream(CreamFlavor creamFlavor, int amount){
            this.creamFlavor = creamFlavor;
            this.amount = amount;
        }
        public IceCream(){
            this.creamFlavor = CreamFlavor.NULL;
            this.amount = 0;
        }
        public CreamFlavor getFlavor() {
            return creamFlavor;
        }
        public int getSelected(){
            return isSelected ? 1 : 0;
        }
        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
        public void setSelected(int value){
            isSelected = value != 0;
        }

        public void setFlavor(CreamFlavor creamFlavor) {
            this.creamFlavor = creamFlavor;
            if (creamFlavor == CreamFlavor.NULL){
                this.amount = 0;
            }
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            if (this.creamFlavor != CreamFlavor.NULL){
                if (amount > MAX_AMOUNT){
                    this.amount = MAX_AMOUNT;
                } else {
                    this.amount = Math.max(amount, 0);
                }
                if (this.amount == 0){
                    this.creamFlavor = CreamFlavor.NULL;
                }
            }
        }
        public void changeAmount(int value){
            if (this.creamFlavor != CreamFlavor.NULL){
                if (this.amount + value <= 0){
                    this.amount = 0;
                    this.creamFlavor = CreamFlavor.NULL;
                } else if (this.amount + value >= MAX_AMOUNT){
                    this.amount = MAX_AMOUNT;
                } else this.amount += value;
            }
        }

        private CreamFlavor creamFlavor;
        private int amount;
        private final int MAX_AMOUNT = 1000;
        private boolean isSelected = false;
    }
}



