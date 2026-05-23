package com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.biogas.GasCanisterBlock;
import com.y271727uy.moderndelight.block.biogas.GasCanisterBlockEntity;
import com.y271727uy.moderndelight.fluid.ModFluid;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.item.tools.HolderItem;
import com.y271727uy.moderndelight.networking.packet.ItemStackSyncS2CPacket;
import com.y271727uy.moderndelight.recipe.custom.DeepFryingRecipe;
import com.y271727uy.moderndelight.screen.custom.DeepFryerScreenHandler;
import com.y271727uy.moderndelight.sound.ModSounds;
import com.y271727uy.moderndelight.tag.TagKeys;
import com.y271727uy.moderndelight.util.FluidStack;
import com.y271727uy.moderndelight.util.block_util.ImplementedInventory;
import com.y271727uy.moderndelight.util.registry_util.ModDamageTypes;
import com.y271727uy.moderndelight.util.fluid.FluidConstants;
import com.y271727uy.moderndelight.util.fluid.FluidVariant;
import com.y271727uy.moderndelight.util.fluid.SingleVariantStorage;
import com.y271727uy.moderndelight.util.fluid.Transaction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.MenuProvider;
import javax.annotation.Nullable;

import java.util.Objects;
import java.util.Optional;

public class DeepFryerBlockEntity extends BlockEntity implements ImplementedInventory, MenuProvider {
    public DeepFryerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DEEP_FRYER_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new net.minecraft.world.inventory.ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> DeepFryerBlockEntity.this.progress1;
                    case 1 -> DeepFryerBlockEntity.this.progress2;
                    case 2 -> DeepFryerBlockEntity.this.progress3;
                    case 3 -> DeepFryerBlockEntity.this.progress4;
                    case 4 -> DeepFryerBlockEntity.this.isHeated;
                    case 5 -> DeepFryerBlockEntity.this.oilLevel;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0 :{
                        DeepFryerBlockEntity.this.progress1 = value;break;
                    }
                    case 1 :{
                        DeepFryerBlockEntity.this.progress2 = value;break;
                    }
                    case 2 :{
                        DeepFryerBlockEntity.this.progress3 = value;break;
                    }
                    case 3 :{
                        DeepFryerBlockEntity.this.progress4 = value;break;
                    }
                    case 4 :{
                        DeepFryerBlockEntity.this.isHeated = value;break;
                    }
                    case 5 :{
                        DeepFryerBlockEntity.this.oilLevel = value;break;
                    }
                }
            }

            @Override
            public int getCount() {
                return 6;
            }
        };
    }
    private int progress1 = 0;
    private int progress2 = 0;
    private int progress3 = 0;
    private int progress4 = 0;
    private int oilLevel = 0;
    public static final int MAX_OIL = 1000;
    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
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
    private int isHeated = 0;

    protected final net.minecraft.world.inventory.ContainerData propertyDelegate;
    public static final String DEEP_FRYER_NAME = "display_name.moderndelight.deep_fryer_name";
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(4,ItemStack.EMPTY);
    public static final String ADD_OIL = "moderndelight.deep_fryer_message.need_oil";
    public static final String TOO_HOT = "moderndelight.deep_fryer_message.too_hot";
    
    public void useOnButton(BlockState state, Level world){
        if (!world.isClientSide){
            return;
        }
        if (!isHeated(state)){
            playSound(SoundEvents.STONE_BUTTON_CLICK_ON,1.0f,false);
            world.setBlock(worldPosition,state.setValue(DeepFryerBlock.RUNNING,true), 3);
        } else {
            stopRunning(world, state);
        }
        setChanged();
        world.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
    }
    
    public boolean isBottleVegetableOil(Item item){
        for (var registryEntry : net.minecraftforge.registries.ForgeRegistries.ITEMS.tags().getTag(TagKeys.BOTTLE_VEGETABLE_OIL)){
            if (item == registryEntry){
                return true;
            }
        }
        return false;
    }
    
    public boolean isBucketVegetableOil(Item item){
        return ModFluid.STILL_VEGETABLE_OIL.get().getBucket() == item;
    }
    
    public void use(BlockState state, Level world, BlockPos pos, Player player) {
        if (!world.isClientSide){
            return;
        }
        ItemStack mainHandStack = player.getItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND);
        ItemStack offHandStack = player.getItemInHand(net.minecraft.world.InteractionHand.OFF_HAND);
        if (!state.getValue(DeepFryerBlock.HAS_OIL)){
            if (isBottleVegetableOil(offHandStack.getItem())){
                splitOilItem(world, player,false, Items.GLASS_BOTTLE);
            } else if (isBottleVegetableOil(mainHandStack.getItem())){
                splitOilItem(world, player,true,Items.GLASS_BOTTLE);
            } else if (isBucketVegetableOil(offHandStack.getItem())){
                splitOilItem(world, player,false,Items.BUCKET);
            } else if (isBucketVegetableOil(mainHandStack.getItem())){
                splitOilItem(world, player,true,Items.BUCKET);
            } else {
                if (getItem(0).isEmpty() && getItem(1).isEmpty() && getItem(2).isEmpty() && getItem(3).isEmpty()){
                    player.sendSystemMessage(Component.translatable(ADD_OIL));
                } else {
                    if (mainHandStack.getItem() == ModItems.HOLDER.get()){
                        setItemOnHolder(mainHandStack);
                    } else if (offHandStack.getItem() == ModItems.HOLDER.get()){
                        setItemOnHolder(offHandStack);
                    } else {
                        spawnItemAndTryDamage(world,player,state);
                    }
                }
            }
        } else if (mainHandStack.getItem() == ModItems.HOLDER.get()){
            setItemOnHolder(mainHandStack);
        } else if (offHandStack.getItem() == ModItems.HOLDER.get()){
            setItemOnHolder(offHandStack);
        } else {
            if (player.getOffhandItem().isEmpty()){
                if (player.getMainHandItem().isEmpty()){
                    spawnItemAndTryDamage(world,player,state);
                } else {
                    checkAndPut(true,player,world);
                }
            } else {
                checkAndPut(false,player,world);
            }
        }
        setChanged();
        world.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
    }
    
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        for (int i = 0; i < inventory.size(); i++) {
            nbt.put("Item" + i, inventory.get(i).save(new CompoundTag()));
        }
        nbt.putInt("deep_fryer.progress1",progress1);
        nbt.putInt("deep_fryer.progress2",progress2);
        nbt.putInt("deep_fryer.progress3",progress3);
        nbt.putInt("deep_fryer.progress4",progress4);
        nbt.putLong("deep_fryer.fluid_amount",fluidStorage.amount);
        nbt.put("deep_fryer.fluid_variant",fluidStorage.variant.toNbt());
        nbt.putInt("deep_fryer.isHeated",isHeated);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        for (int i = 0; i < inventory.size(); i++) {
            inventory.set(i, ItemStack.of(nbt.getCompound("Item" + i)));
        }
        progress1 = nbt.getInt("deep_fryer.progress1");
        progress2 = nbt.getInt("deep_fryer.progress2");
        progress3 = nbt.getInt("deep_fryer.progress3");
        progress4 = nbt.getInt("deep_fryer.progress4");
        fluidStorage.variant = FluidVariant.fromNbt((CompoundTag) nbt.get("deep_fryer.fluid_variant"));
        fluidStorage.amount = nbt.getLong("deep_fryer.fluid_amount");
        isHeated = nbt.getInt("deep_fryer.isHeated");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(DEEP_FRYER_NAME);
    }
    
    public void playSound(SoundEvent sound, float volume, boolean isRandom) {
        if (isRandom){
            Objects.requireNonNull(level).playSound(null, worldPosition.getX() + .5f, worldPosition.getY() + .5f, worldPosition.getZ() + .5f, sound, SoundSource.BLOCKS, volume, level.random.nextFloat()+0.8f);
        } else {
            Objects.requireNonNull(level).playSound(null, worldPosition.getX() + .5f, worldPosition.getY() + .5f, worldPosition.getZ() + .5f, sound, SoundSource.BLOCKS, volume, 1.0f);
        }
    }

    @Nullable
    @Override
    public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new DeepFryerScreenHandler(syncId,(net.minecraft.world.entity.player.Inventory)playerInventory,this,this.propertyDelegate);
    }
    
    private void spawnItemAndTryDamage(Level world, Player player, BlockState state){
        if (isHeated(state)){
            player.hurt(ModDamageTypes.of(world,ModDamageTypes.SCALDED),2.0f);
            player.sendSystemMessage(Component.translatable(TOO_HOT));
        } else if (state.getValue(DeepFryerBlock.HAS_OIL)) {
            player.hurt(ModDamageTypes.of(world,ModDamageTypes.SCALDED),1.0f);
            player.sendSystemMessage(Component.translatable(TOO_HOT));
        }
        spawnItem(world);
    }
    
    private void setItemOnHolder(ItemStack holder){
        if (HolderItem.getHoldingStack(holder).isEmpty()){
            for(int i=3;i>=0;i--){
                if (!getItem(i).isEmpty()){
                    ItemStack stack = getItem(i).copy();
                    setItem(i,ItemStack.EMPTY);
                    playSound(SoundEvents.ITEM_PICKUP,1.0f,false);
                    HolderItem.setHoldingStack(stack,holder);
                    break;
                }
            }
        }
        setChanged();
    }
    
    private void spawnItem(Level world){
        for(int i=3;i>=0;i--){
            if (!getItem(i).isEmpty()){
                spawnItem(i,world);
                break;
            }
        }
    }
    
    private void spawnItem(int slot,Level world){
        Containers.dropItemStack(world,worldPosition.getX()+0.5,worldPosition.getY()+0.8,worldPosition.getZ()+0.5,
                getItem(slot));
        this.setItem(slot,ItemStack.EMPTY);
        playSound(SoundEvents.ITEM_PICKUP,1.3f,world.random.nextFloat()+0.4f);
        setChanged();
    }
    
    private void checkAndPut(boolean isMainHand, Player player, Level world){
        for (int i=0;i<4;i++){
            if (getItem(i).isEmpty()){
                checkHandAndSplit(isMainHand,player,i,world);
                playSound(SoundEvents.ITEM_PICKUP,1.0f,world.random.nextFloat()+0.3f);
                break;
            }
        }
    }
    
    private void checkHandAndDecrement(boolean isMainHand,Player player){
        if (isMainHand){
            player.getMainHandItem().shrink(1);
        } else {
            player.getOffhandItem().shrink(1);
        }
    }
    
    private void checkHandAndSplit(boolean isMainHand,Player player,int slot, Level world){
        if (isMainHand){
            setItem(slot,player.getMainHandItem().split(1));
        } else {
            setItem(slot,player.getOffhandItem().split(1));
        }
        setChanged();
    }
    
    private void splitOilItem(Level world, Player player, boolean isMainHand, Item item) {
        checkHandAndDecrement(isMainHand,player);
        playSound(SoundEvents.BUCKET_EMPTY,1.0f, world.random.nextFloat()+0.3f);
        player.addItem(item.getDefaultInstance());
        if (item.equals(Items.GLASS_BOTTLE)){
            try(Transaction transaction = Transaction.openOuter()){
                fluidStorage.insert(FluidVariant.of(ModFluid.STILL_VEGETABLE_OIL.get()),
                        fluidStorage.getCapacity() / 3,transaction);
                transaction.commit();
            }
        } else if (item.equals(Items.BUCKET)){
            fluidStorage.variant = FluidVariant.of(ModFluid.STILL_VEGETABLE_OIL.get());
            fluidStorage.amount = fluidStorage.getCapacity();
        }
    }
    
    int maxProgress = 300;
    
    public void tick(Level world, BlockState state, DeepFryerBlockEntity blockEntity) {
        if (!world.isClientSide){
            return;
        }
        if (hasOil()){
            world.setBlock(worldPosition, state.setValue(DeepFryerBlock.HAS_OIL,true), 3);
            this.oilLevel = (int) FluidStack.convertDropletsToMb(fluidStorage.amount);
        } else {
            this.oilLevel = 0;
            world.setBlock(worldPosition, state.setValue(DeepFryerBlock.HAS_OIL,false), 3);
        }
        if (world.getGameTime()%5==0&&(progress1 != 0 || progress2 !=0 || progress3 != 0 || progress4 != 0)){
            playSound(ModSounds.BLOCK_FOOD_FRYING.get(),0.4f,false);
        }
        if (isHeated(state)){
            isHeated = 1;
            if (world.getGameTime()%5==0){
                playSound(SoundEvents.FIRE_AMBIENT,0.3f,false);
            }
            Direction dir = state.getValue(DeepFryerBlock.FACING);
            BlockState neighborState = Blocks.AIR.defaultBlockState();
            BlockPos neighborPos = worldPosition;
            switch (dir){
                case EAST -> {
                    neighborPos = worldPosition.relative(Direction.WEST);
                    neighborState = world.getBlockState(neighborPos);
                }
                case SOUTH -> {
                    neighborPos = worldPosition.relative(Direction.NORTH);
                    neighborState = world.getBlockState(neighborPos);
                }
                case WEST -> {
                    neighborPos = worldPosition.relative(Direction.EAST);
                    neighborState = world.getBlockState(neighborPos);
                }
                case NORTH -> {
                    neighborPos = worldPosition.relative(Direction.SOUTH);
                    neighborState = world.getBlockState(neighborPos);
                }
            }
            if (neighborState.getBlock() instanceof GasCanisterBlock) {
                if (neighborState.getValue(GasCanisterBlock.FACING) == dir) {
                    BlockEntity neighborBlockEntity = world.getBlockEntity(neighborPos);
                    if (!(neighborBlockEntity instanceof GasCanisterBlockEntity entity) || !entity.reduceGas()) {
                        stopRunning(world, state);
                    }
                } else {
                    stopRunning(world, state);
                }
            } else {
                stopRunning(world, state);
            }
            if (state.getValue(DeepFryerBlock.HAS_OIL)){
                if (hasRecipe(0)){
                    blockEntity.progress1++;
                    if (blockEntity.progress1 == maxProgress){
                        craft(0, world);
                    }
                } else {
                    blockEntity.progress1 = 0;
                }
                if (hasRecipe(1)){
                    blockEntity.progress2++;
                    if (blockEntity.progress2 == maxProgress){
                        craft(1, world);
                    }
                } else {
                    blockEntity.progress2 = 0;
                }
                if (hasRecipe(2)){
                    blockEntity.progress3++;
                    if (blockEntity.progress3 == maxProgress){
                        craft(2, world);
                    }
                } else {
                    blockEntity.progress3 = 0;
                }
                if (hasRecipe(3)) {
                    blockEntity.progress4++;
                    if (blockEntity.progress4 == maxProgress) {
                        craft(3, world);
                    }
                } else {
                    blockEntity.progress4 = 0;
                }
            } else {
                resetAllProgress();
            }
        } else {
            resetAllProgress();
            isHeated = 0;
        }
    }

    private boolean hasOil() {
        Fluid fluid = fluidStorage.variant.getFluid();
        for(var f:net.minecraftforge.registries.ForgeRegistries.FLUIDS.tags().getTag(TagKeys.OIL)){
            if (fluid == f){
                return true;
            }
        }
        return false;
    }

    private void craft(int slot, Level world) {
        net.minecraft.world.Container inventory = new net.minecraft.world.SimpleContainer(1);
        inventory.setItem(0,this.getItem(slot));
        var match = Objects.requireNonNull(this.level).getRecipeManager()
                .getRecipeFor(DeepFryingRecipe.Type.INSTANCE, inventory, this.level);
        if (match.isEmpty()) return;
        var recipe = match.get();
        if (!this.getItem(slot).getCraftingRemainingItem().isEmpty()){
            Containers.dropItemStack(world,worldPosition.getX(),worldPosition.getY(),worldPosition.getZ(),this.getItem(slot).getCraftingRemainingItem().copy());
        }
        this.setItem(slot, new ItemStack(recipe.getResultItem(level.registryAccess()).getItem(),
                recipe.getResultItem(level.registryAccess()).getCount()));
        decreaseOilLevel();
        if (world.getGameTime()%5==0){
            playSound(ModSounds.BLOCK_FOOD_FRYING.get(), 1.0f, false);
        }
        setChanged();
    }

    private void stopRunning(Level world, BlockState state) {
        playSound(SoundEvents.STONE_BUTTON_CLICK_OFF,1.0f,false);
        world.setBlock(worldPosition, state.setValue(DeepFryerBlock.RUNNING,false), 3);
    }

    private void resetAllProgress(){
        progress1 = 0;
        progress2 = 0;
        progress3 = 0;
        progress4 = 0;
        setChanged();
    }
    
    public void playSound(SoundEvent sound, float volume, float pitch) {
        Objects.requireNonNull(level).playSound(null, worldPosition.getX() + .5f, worldPosition.getY() + .5f, worldPosition.getZ() + .5f, sound, SoundSource.BLOCKS, volume, pitch);
    }
    
    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }
    
    private boolean isHeated(BlockState state){
        return state.getValue(DeepFryerBlock.RUNNING);
    }
    
    private void decreaseOilLevel(){
        if (fluidStorage.amount - FluidStack.convertMbToDroplets(50) > 0){
            fluidStorage.amount-= FluidStack.convertMbToDroplets(50);
        } else {
            fluidStorage.amount = 0;
            fluidStorage.variant = FluidVariant.blank();
        }
    }
    
    private boolean hasRecipe(int slot) {
        net.minecraft.world.Container inventory = new net.minecraft.world.SimpleContainer(1);
        inventory.setItem(0,this.getItem(slot));
        var match = Objects.requireNonNull(this.level).getRecipeManager()
                .getRecipeFor(DeepFryingRecipe.Type.INSTANCE, inventory, this.level);
        return !match.isEmpty();
    }

    @Override
    public void setChanged() {
        if (level != null) {
            level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
            ItemStackSyncS2CPacket.send(worldPosition,getItems(),level);
        }
        super.setChanged();
    }

}
