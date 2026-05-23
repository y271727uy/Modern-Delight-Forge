package com.y271727uy.moderndelight.block.kitchenware.decor;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.networking.packet.ItemStackSyncS2CPacket;
import com.y271727uy.moderndelight.util.block_util.ImplementedInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nonnull;
import java.util.Objects;

public class KitchenUtensilHolderBlockEntity extends BlockEntity implements ImplementedInventory {
    public KitchenUtensilHolderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KITCHEN_UTENSIL_HOLDER_BLOCK_ENTITY.get(), pos, state);
    }
    
    public final NonNullList<ItemStack> INV = NonNullList.withSize(4, ItemStack.EMPTY);

    public void use(@Nonnull Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if(!item.isEmpty()) {
            int item_index = 0;
            while (item_index < 4) {
                if (getItem(item_index).isEmpty()) {
                    setItem(item_index, item.split(1));
                    playSound(SoundEvents.ITEM_PICKUP, 0.4F);
                    break;
                }
                else{
                    item_index++;
                }
            }
        } else{
            int item_index = 3;
            while (item_index > -1) {
                if (!getItem(item_index).isEmpty()) {
                    spawnItem(item_index);
                    playSound(SoundEvents.ITEM_PICKUP, 0.8F);
                    break;
                }
                else{
                    item_index--;
                }
            }
        }
        setChanged();
    }

    @Override
    public void setChanged() {
        if (level != null) {
            ItemStackSyncS2CPacket.send(worldPosition, getItems(), level);
        }
        super.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        for (int i = 0; i < INV.size(); i++) {
            nbt.put("Item" + i, INV.get(i).save(new CompoundTag()));
        }
    }
    
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        for (int i = 0; i < INV.size(); i++) {
            INV.set(i, ItemStack.of(nbt.getCompound("Item" + i)));
        }
    }

    public NonNullList<ItemStack> getItems() {
        return INV;
    }

    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    private void spawnItem(int slot){
        Containers.dropItemStack(Objects.requireNonNull(this.level), this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), INV.get(slot));
        INV.set(slot, ItemStack.EMPTY);
    }
    
    public void playSound(SoundEvent sound, float volume) {
        Objects.requireNonNull(level).playSound(null, worldPosition.getX() + .5f, worldPosition.getY() + .5f, worldPosition.getZ() + .5f, sound, SoundSource.BLOCKS, volume, level.random.nextFloat()+0.1f);
    }
}
