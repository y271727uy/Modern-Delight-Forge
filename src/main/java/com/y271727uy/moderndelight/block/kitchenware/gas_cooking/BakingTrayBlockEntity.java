package com.y271727uy.moderndelight.block.kitchenware.gas_cooking;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.networking.packet.ItemStackSyncS2CPacket;
import com.y271727uy.moderndelight.sound.ModSounds;
import com.y271727uy.moderndelight.tag.TagKeys;
import com.y271727uy.moderndelight.util.block_util.ImplementedInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BakingTrayBlockEntity extends BlockEntity implements ImplementedInventory {
    public BakingTrayBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BAKING_TRAY_BLOCK_ENTITY.get(), pos, state);
    }
    
    public final NonNullList<ItemStack> INV = NonNullList.withSize(4, ItemStack.EMPTY);
    private int coolTime = 0;
    private int stir_fry_times = 0;
    private final int max_stir_fry_times = 5;

    @Override
    public NonNullList<ItemStack> getItems() {
        return INV;
    }
    
    public void playSound(SoundEvent sound, float volume, boolean isRandom) {
        if (isRandom){
            Objects.requireNonNull(level).playSound(null, worldPosition.getX() + .5f, worldPosition.getY() + .5f, worldPosition.getZ() + .5f, sound, SoundSource.BLOCKS, volume, level.random.nextFloat()+0.8f);
        } else {
            Objects.requireNonNull(level).playSound(null, worldPosition.getX() + .5f, worldPosition.getY() + .5f, worldPosition.getZ() + .5f, sound, SoundSource.BLOCKS, volume, 1.0f);
        }
    }
    
    public void use(Player player, Level world) {
        if (world.isClientSide){
            return;
        }
        boolean isMainHand = player.getOffhandItem().isEmpty();
        if (isHeated(world, worldPosition) && isSpatulaItem(player) &&
                (!this.getItem(0).isEmpty() ||
                !this.getItem(1).isEmpty() ||
                !this.getItem(2).isEmpty() ||
                !this.getItem(3).isEmpty())){
            if (stir_fry_times != max_stir_fry_times && coolTime == 0){
                stir_fry_times++;
                coolTime = 5;
                NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
                for (int i = 0; i < 4; i++){
                    if (i != 3){
                        inventory.set(i, this.getItem(i + 1));
                    } else {
                        inventory.set(i, this.getItem(0));
                    }
                }
                for (int i = 0; i < 4; i++){
                    this.setItem(i, inventory.get(i));
                }
                playSound(ModSounds.BLOCK_FOOD_FRYING.get(), 1.0f, true);
                player.getMainHandItem().hurtAndBreak(1, (LivingEntity) player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
            }
        } else {
            if (player.isShiftKeyDown()){
                if (!this.getItem(3).isEmpty()){
                    spawnItem(world, 3);
                } else if (!this.getItem(2).isEmpty()) {
                    spawnItem(world, 2);
                } else if (!this.getItem(1).isEmpty()) {
                    spawnItem(world, 1);
                } else if (!this.getItem(0).isEmpty()) {
                    spawnItem(world, 0);
                }
            } else {
                if ((player.getMainHandItem().isEmpty() &&
                        player.getOffhandItem().isEmpty()) ||
                isSpatulaItem(player)){
                    setChanged();
                    return;
                }
                if (this.getItem(0).isEmpty()){
                    splitItem(player, isMainHand, 0);
                } else if(this.getItem(1).isEmpty()){
                    splitItem(player, isMainHand, 1);
                } else if(this.getItem(2).isEmpty()){
                    splitItem(player, isMainHand, 2);
                } else if(this.getItem(3).isEmpty()){
                    splitItem(player, isMainHand, 3);
                }
            }
        }
        setChanged();
    }

    private boolean isSpatulaItem(Player player) {
        for (var registryEntry : ForgeRegistries.ITEMS.tags().getTag(TagKeys.SPATULAS)){
            if (player.getMainHandItem().getItem() == registryEntry){
                return true;
            }
        }
        return false;
    }

    private void spawnItem(Level world, int slot){
        if (world.isClientSide){
            return;
        }
        net.minecraft.world.Containers.dropItemStack(world, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), this.getItem(slot));
        this.setItem(slot, ItemStack.EMPTY);
        playSound(SoundEvents.ITEM_PICKUP, 1.0f, true);
        setChanged();
    }

    private void splitItem(Player player, boolean isMainHand, int inv) {
        if (isMainHand){
            INV.set(inv, player.getMainHandItem().split(1));
        } else {
            INV.set(inv, player.getOffhandItem().split(1));
        }
        if (isHeated(Objects.requireNonNull(level), worldPosition)){
            playSound(ModSounds.BLOCK_FOOD_FRYING.get(), 1.0f, true);
        } else {
            playSound(SoundEvents.ITEM_PICKUP, 1.0f, true);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        for (int i = 0; i < INV.size(); i++) {
            nbt.put("Item" + i, INV.get(i).save(new CompoundTag()));
        }
        nbt.putInt("baking_tray.stir_fry_times", stir_fry_times);
    }
    
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        for (int i = 0; i < INV.size(); i++) {
            INV.set(i, ItemStack.of(nbt.getCompound("Item" + i)));
        }
        stir_fry_times = nbt.getInt("baking_tray.stir_fry_times");
    }
    
    @Override
    public void setChanged() {
        if (level != null) {
            level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
            ItemStackSyncS2CPacket.send(worldPosition, getItems(), level);
        }
        super.setChanged();
    }
    
    private boolean isHeated(Level world, BlockPos pos){
        return world.getBlockState(pos.below()).getBlock() == ModBlocks.BURNING_GAS_COOKING_STOVE.get();
    }

    public ItemStack getRendererStack1() {
        return this.getItem(0);
    }
    public ItemStack getRendererStack2() {
        return this.getItem(1);
    }
    public ItemStack getRendererStack3() {
        return this.getItem(2);
    }
    public ItemStack getRendererStack4() {
        return this.getItem(3);
    }
    
    private boolean isFlat(Item item) {
        List<Item> items = new ArrayList<>();
        for (var registryEntry : ForgeRegistries.ITEMS.tags().getTag(TagKeys.FLAT_ON_BAKING_TRAY)){
            items.add(registryEntry);
        }
        return items.contains(item);
    }
    
    public void tick(Level world, BlockPos pos) {
        if (world.isClientSide){
            return;
        }
        if (coolTime != 0){
            coolTime--;
        }
        if (isHeated(world, pos)){
            if (!isFlat(this.getItem(0).getItem())){
                if (stir_fry_times == max_stir_fry_times){
                    if (hasCampfireRecipe(this.getItem(0))){
                        craftCampfireItem(this.getItem(0), 0, world);
                    }
                    if (hasCampfireRecipe(this.getItem(1))){
                        craftCampfireItem(this.getItem(1), 1, world);
                    }
                    if (hasCampfireRecipe(this.getItem(2))){
                        craftCampfireItem(this.getItem(2), 2, world);
                    }
                    if (hasCampfireRecipe(this.getItem(3))){
                        craftCampfireItem(this.getItem(3), 3, world);
                    }
                    stir_fry_times = 0;
                }
            }
        }
    }

    private void craftCampfireItem(ItemStack stack, int slot, Level world) {
        net.minecraft.world.SimpleContainer inventory = new net.minecraft.world.SimpleContainer(1);
        inventory.setItem(0, stack);
        Optional<CampfireCookingRecipe> match = Objects.requireNonNull(this.level).getRecipeManager()
                .getRecipeFor(RecipeType.CAMPFIRE_COOKING, inventory, world);
        if (match.isPresent()) {
            net.minecraft.world.Containers.dropItemStack(world, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
                    new ItemStack(match.get().getResultItem(world.registryAccess()).getItem()));
            this.getItem(slot).shrink(1);
            int exp = (int) match.get().getExperience();
            if (exp == 0) exp = 1;
            ExperienceOrb xp = new ExperienceOrb(world, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), exp);
            world.addFreshEntity(xp);
            setChanged();
        }
    }
    
    private boolean hasCampfireRecipe(ItemStack stack) {
        net.minecraft.world.SimpleContainer inventory = new net.minecraft.world.SimpleContainer(1);
        inventory.setItem(0, stack);
        Optional<CampfireCookingRecipe> match = Objects.requireNonNull(this.level).getRecipeManager()
                .getRecipeFor(RecipeType.CAMPFIRE_COOKING, inventory, level);
        return match.isPresent();
    }
}
