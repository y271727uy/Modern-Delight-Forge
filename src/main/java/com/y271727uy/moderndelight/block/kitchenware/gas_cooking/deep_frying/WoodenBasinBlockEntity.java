package com.y271727uy.moderndelight.block.kitchenware.gas_cooking.deep_frying;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.fluid.ModFluid;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.networking.packet.ItemStackSyncS2CPacket;
import com.y271727uy.moderndelight.recipe.custom.SqueezeRecipe;
import com.y271727uy.moderndelight.screen.custom.WoodenBasinScreenHandler;
import com.y271727uy.moderndelight.tag.TagKeys;
import com.y271727uy.moderndelight.util.FluidStack;
import com.y271727uy.moderndelight.util.fluid.FluidConstants;
import com.y271727uy.moderndelight.util.fluid.FluidVariant;
import com.y271727uy.moderndelight.util.fluid.SingleVariantStorage;
import com.y271727uy.moderndelight.util.block_util.FluidStorageAble;
import com.y271727uy.moderndelight.util.block_util.ImplementedInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;

import java.util.Objects;
import java.util.Optional;

public class WoodenBasinBlockEntity extends BlockEntity implements ImplementedInventory, MenuProvider, FluidStorageAble {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(5, ItemStack.EMPTY);
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int FILTER_SLOT = 2;
    private static final int INGREDIENT_SLOT = 3;
    private static final int IMPURITIES_SLOT = 4;
    public static final int MAX_FLUID_LEVEL = 81000;
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

    @Override
    public SingleVariantStorage<FluidVariant> getFluidStorage() {
        return fluidStorage;
    }

    public static final String WOODEN_BASIN_NAME = "display_name.moderndelight.wooden_basin_name";
    
    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }
    
    public WoodenBasinBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WOODEN_BASIN_BLOCK_ENTITY.get(), pos, state);
    }
    
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        for (int i = 0; i < inventory.size(); i++) {
            nbt.put("Item" + i, inventory.get(i).save(new CompoundTag()));
        }
        nbt.putLong("wooden_basin.fluid_amount", fluidStorage.amount);
        nbt.put("wooden_basin.fluid_variant",fluidStorage.variant.toNbt());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        for (int i = 0; i < inventory.size(); i++) {
            inventory.set(i, ItemStack.of(nbt.getCompound("Item" + i)));
        }
        fluidStorage.variant = FluidVariant.fromNbt((CompoundTag) nbt.get("wooden_basin.fluid_variant"));
        fluidStorage.amount = nbt.getLong("wooden_basin.fluid_amount");
    }
    

    
    public void playSound(SoundEvent sound, float volume, boolean isRandom) {
        if (isRandom){
            Objects.requireNonNull(level).playSound(null, worldPosition.getX() + .5f, worldPosition.getY() + .5f, worldPosition.getZ() + .5f, sound, SoundSource.BLOCKS, volume, level.random.nextFloat()+0.8f);
        } else {
            Objects.requireNonNull(level).playSound(null, worldPosition.getX() + .5f, worldPosition.getY() + .5f, worldPosition.getZ() + .5f, sound, SoundSource.BLOCKS, volume, 1.0f);
        }
    }
    


    @Override
    public Component getDisplayName() {
        return Component.translatable(WOODEN_BASIN_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new WoodenBasinScreenHandler(syncId, (net.minecraft.world.entity.player.Inventory)playerInventory,this);
    }
    
    public static boolean isFilter(Item item){
        for (var registryEntry : ForgeRegistries.ITEMS.tags().getTag(TagKeys.FILTERS)){
            if (item == registryEntry){
                return true;
            }
        }
        return false;
    }
    
    private boolean isOil(Fluid fluid){
        for (var registryEntry : ForgeRegistries.FLUIDS.tags().getTag(TagKeys.OIL)){
            if (fluid == registryEntry){
                return true;
            }
        }
        return false;
    }
    
    public void onLandedUpon(Level world, LivingEntity entity) {
        if (!world.isClientSide){
            return;
        }
        SimpleContainer inv = new SimpleContainer(1);
        inv.setItem(0,getItem(INGREDIENT_SLOT));
        Optional<SqueezeRecipe> match = Objects.requireNonNull(this.level).getRecipeManager()
                .getRecipeFor(SqueezeRecipe.Type.INSTANCE, inv, this.level);
        if (match.isPresent()){
            FluidStack outputFluid = match.get().getOutputFluid();
            if (
                    (fluidStorage.variant.isOf(outputFluid.getFluid()) ||
                            fluidStorage.variant.isBlank())
                            && fluidStorage.amount != MAX_FLUID_LEVEL
                            && isFilter(getItem(FILTER_SLOT).getItem())
            ){
                fluidStorage.variant = outputFluid.getVariant();
                if ((fluidStorage.amount + outputFluid.getAmount()) <= MAX_FLUID_LEVEL){
                    fluidStorage.amount += outputFluid.getAmount();
                } else {
                    fluidStorage.amount = MAX_FLUID_LEVEL;
                }
                ItemStack outputStack = match.get().getResultItem(level.registryAccess());
                int damage = getItem(FILTER_SLOT).getDamageValue();
                if (damage < getItem(FILTER_SLOT).getMaxDamage()){
                    getItem(FILTER_SLOT).setDamageValue(damage+1);
                } else {
                    playSound(SoundEvents.ITEM_BREAK,1.2f,true);
                    setItem(FILTER_SLOT,ItemStack.EMPTY);
                }
                playSound(SoundEvents.HONEY_BLOCK_BREAK,1.8f,true);
                removeItem(INGREDIENT_SLOT,1);
                if (getItem(IMPURITIES_SLOT).isEmpty()){
                    setItem(IMPURITIES_SLOT,outputStack);
                } else if (getItem(IMPURITIES_SLOT).getItem().equals(outputStack.getItem()) &&
                        getItem(IMPURITIES_SLOT).getCount()+outputStack.getCount()<=getItem(IMPURITIES_SLOT).getMaxStackSize()) {
                    int count = getItem(IMPURITIES_SLOT).getCount() + outputStack.getCount();
                    setItem(IMPURITIES_SLOT, new ItemStack(outputStack.getItem(),count));
                } else {
                    Containers.dropItemStack(world,worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),outputStack.copy());
                }
                if (match.get().isDanger()){
                    entity.hurt(world.damageSources().cactus(),1.5f);
                }
                if (match.get().doCreateFire()){
                    entity.setSecondsOnFire(5);
                    createFire(world);
                }
            }
        }
        setChanged();
    }
    
    private boolean canLightFire(Level world, BlockPos pos) {
        Direction[] dirs = Direction.values();
        for (Direction direction : dirs) {
            if (this.hasBlock(world, pos.relative(direction))) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasBlock(Level world, BlockPos pos) {
        return (pos.getY() < world.getMinBuildHeight() || pos.getY() >= world.getMaxBuildHeight() || world.hasChunkAt(pos)) && !world.getBlockState(pos).isAir();
    }
    
    private void createFire(Level world){
        if (world.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            BlockPos blockPos = worldPosition;
            for(int j = 0; j < 5; ++j) {
                blockPos = blockPos.offset(world.random.nextInt(3) - 1, 1, world.random.nextInt(3) - 1);
                if (!world.mayInteract(world.players().get(0), blockPos)) {
                    return;
                }
                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.isAir()) {
                    if (this.canLightFire(world, blockPos)) {
                        world.setBlock(blockPos, FireBlock.getState(world, blockPos), 3);
                        return;
                    }
                } else if (blockState.blocksMotion()) {
                    return;
                }
            }
            for(int k = 0; k < 5; ++k) {
                BlockPos blockPos2 = worldPosition.offset(world.random.nextInt(3) - 1, 0, world.random.nextInt(3) - 1);
                if (!world.mayInteract(world.players().get(0), blockPos2)) {
                    return;
                }
                if (world.isEmptyBlock(blockPos2.above()) && this.hasBlock(world, blockPos2)) {
                    world.setBlock(blockPos2.above(), FireBlock.getState(world, blockPos2), 3);
                }
            }
        }
    }
    
    @Override
    public void setChanged() {
        if (level != null) {
            level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
            ItemStackSyncS2CPacket.send(worldPosition,getItems(),level);
            sendFluidPacket(level,worldPosition);
        }
        super.setChanged();
    }
    
    public void tick(Level world, BlockPos pos, BlockState state) {
        if(!world.isClientSide){
            return;
        }
        if (getItem(INPUT_SLOT).getItem().equals(Items.BUCKET) &&
                fluidStorage.amount== MAX_FLUID_LEVEL &&
                getItem(OUTPUT_SLOT).isEmpty()){
            removeItem(INPUT_SLOT,1);
            setItem(OUTPUT_SLOT,fluidStorage.variant.getFluid().getBucket().getDefaultInstance());
            fluidStorage.amount = 0;
            fluidStorage.variant = FluidVariant.blank();
            setChanged();
        } else if (isVegetableOil()){
            fluidStorage.amount -= 27000;
            if (fluidStorage.amount == 0){
                fluidStorage.variant = FluidVariant.blank();
            }
            removeItem(INPUT_SLOT,1);
            int count = getItem(OUTPUT_SLOT).getCount();
            setItem(OUTPUT_SLOT,new ItemStack(ModItems.VEGETABLE_OIL_BOTTLE.get(),count+1));
            setChanged();
        } else if (isWater()){
            fluidStorage.amount -= 27000;
            if (fluidStorage.amount == 0){
                fluidStorage.variant = FluidVariant.blank();
            }
            removeItem(INPUT_SLOT,1);
            ItemStack waterBottle = new ItemStack(Items.POTION);
            waterBottle.addTagElement("Potion", net.minecraft.nbt.StringTag.valueOf("minecraft:water"));
            setItem(OUTPUT_SLOT,waterBottle);
            setChanged();
        }
    }

    private boolean isVegetableOil() {
        return getItem(INPUT_SLOT).getItem().equals(Items.GLASS_BOTTLE)
                && fluidStorage.amount >= 27000
                && fluidStorage.variant.isOf(ModFluid.STILL_VEGETABLE_OIL.get())
                && (getItem(OUTPUT_SLOT).isEmpty()
                    || (getItem(OUTPUT_SLOT).getItem().equals(ModItems.VEGETABLE_OIL_BOTTLE.get())
                        && getItem(OUTPUT_SLOT).getCount() < ModItems.VEGETABLE_OIL_BOTTLE.get().getMaxStackSize()
                    )
                );
    }
    
    private boolean isWater() {
        return getItem(INPUT_SLOT).getItem().equals(Items.GLASS_BOTTLE)
                && fluidStorage.amount >= 27000
                && fluidStorage.variant.isOf(Fluids.WATER)
                && getItem(OUTPUT_SLOT).isEmpty();
    }

    public ItemStack getRendererStack() {
        return getItem(FILTER_SLOT);
    }

    public ItemStack getRendererStack2() {
        return getItem(INGREDIENT_SLOT);
    }
    
    public FluidStack getFluidStackCopy(){
        return new FluidStack(fluidStorage.variant,fluidStorage.amount);
    }
}