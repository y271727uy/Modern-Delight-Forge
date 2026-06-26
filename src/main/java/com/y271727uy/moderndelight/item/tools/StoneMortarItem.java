package com.y271727uy.moderndelight.item.tools;

import com.y271727uy.moderndelight.enchantment.ModEnchantments;
import com.y271727uy.moderndelight.recipe.custom.GrindingRecipe;
import com.y271727uy.moderndelight.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.SimpleContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StoneMortarItem extends Item {
    public StoneMortarItem(Tier material, Properties properties) {
        super(properties.durability(material.getUses()));
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == ModEnchantments.FINE_GRINDING.get() || enchantment == Enchantments.UNBREAKING;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public SoundEvent getEatingSound() {
        return ModSounds.ITEM_STONE_MORTAR_WORKING.get();
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 40;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (user instanceof Player player && !level.isClientSide){
            CompoundTag nbt = stack.getTagElement("crafting_stack");
            if (nbt != null){
                ItemStack input = ItemStack.of(nbt);
                int grindingLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.FINE_GRINDING.get(), stack);
                List<ItemStack> outputs = craft(input,level,grindingLevel);
                for (ItemStack itemStack : outputs){
                    player.getInventory().add(itemStack);
                }
            }
            stack.removeTagKey("crafting_stack");
            stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(net.minecraft.world.entity.EquipmentSlot.MAINHAND));
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        return stack;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int remainingUseTicks) {
        if (!level.isClientSide && user instanceof Player player){
            CompoundTag nbt = stack.getTagElement("crafting_stack");
            if (nbt != null){
                ItemStack input = ItemStack.of(nbt);
                player.getInventory().add(input);
                BlockPos pos = user.blockPosition();
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,1.0f,level.random.nextFloat() + 0.8f);
            }
            stack.removeTagKey("crafting_stack");
        }
        super.releaseUsing(stack, level, user, remainingUseTicks);
    }

    private static List<ItemStack> craft(ItemStack stack, Level level, int fine_grindingLevel){
        SimpleContainer inventory = new SimpleContainer(1);
        inventory.setItem(0,stack);
        Optional<GrindingRecipe> match = level.getRecipeManager()
                .getRecipeFor(GrindingRecipe.Type.INSTANCE, inventory, level);
        List<ItemStack> outputs = new ArrayList<>();
        if (match.isPresent()){
            float luck = fine_grindingLevel * 0.2f;
            ItemStack out1 = match.get().getOutput(level.registryAccess()).copy();
            ItemStack out2 = match.get().getChancedOutput().copy();
            if (Math.random() < luck){
                int count1 = out1.getCount() + level.random.nextInt(fine_grindingLevel + 1) + 1;
                int count2 = out2.getCount() + level.random.nextInt(fine_grindingLevel + 1) + 1;
                out1.setCount(Math.min(count1, out1.getMaxStackSize()));
                out2.setCount(Math.min(count2, out2.getMaxStackSize()));
            }
            outputs.add(out1);
            float chance = match.get().getChance();
            if (Math.random() < chance){
                outputs.add(out2);
            }
        }
        return outputs;
    }
    private static boolean hasRecipe(ItemStack stack, Level level) {
        SimpleContainer inventory = new SimpleContainer(1);
        inventory.setItem(0,stack);
        Optional<GrindingRecipe> match = level.getRecipeManager()
                .getRecipeFor(GrindingRecipe.Type.INSTANCE, inventory, level);
        return match.isPresent();
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack thisStack = user.getItemInHand(hand);
        ItemStack cachedStack = getInsideStack(thisStack);
        if (cachedStack.isEmpty()){
            ItemStack input;
            if (hand == InteractionHand.MAIN_HAND){
                input = user.getOffhandItem();
            } else {
                input = user.getMainHandItem();
            }
            if (hasRecipe(input,level)){
                if (!level.isClientSide){
                    CompoundTag nbt = thisStack.getOrCreateTagElement("crafting_stack");
                    ItemStack newInput = input.copy();
                    newInput.setCount(1);
                    newInput.save(nbt);
                    input.shrink(1);
                    user.startUsingItem(hand);
                }
                return InteractionResultHolder.consume(user.getItemInHand(hand));
            }
        } else {
            if (!level.isClientSide){
                user.getInventory().add(cachedStack);
                thisStack.removeTagKey("crafting_stack");
                BlockPos pos = user.blockPosition();
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,1.0f,level.random.nextFloat() + 0.8f);
            }
            return InteractionResultHolder.sidedSuccess(user.getItemInHand(hand), level.isClientSide);
        }
        return InteractionResultHolder.fail(user.getItemInHand(hand));
    }
    public static ItemStack getInsideStack(ItemStack stack){
        CompoundTag nbt = stack.getTagElement("crafting_stack");
        if (nbt != null){
            try {
                return ItemStack.of(nbt);
            } catch (Exception ignored){}
        }
        return ItemStack.EMPTY;
    }
}
