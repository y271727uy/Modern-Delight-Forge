package com.y271727uy.moderndelight.item.tools;

import com.y271727uy.moderndelight.block.power.batteries.BatteryBlockItem;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.item.food.PackagedItem;
import com.y271727uy.moderndelight.recipe.custom.WhiskingRecipe;
import com.y271727uy.moderndelight.sound.ModSounds;
import com.y271727uy.moderndelight.util.TextUtil;
import com.y271727uy.moderndelight.util.energy.ItemStackEnergyCapabilityProvider;
import com.y271727uy.moderndelight.util.energy.StackEnergyStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ElectricWhiskItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final RawAnimation WORKING = RawAnimation.begin().thenPlay("working");
    
    public ElectricWhiskItem() {
        super(new Item.Properties().stacksTo(1));
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    public static final int MAX_ENERGY = 5000;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private volatile boolean isWorking = false;

    public static void addStoredEnergy(ItemStack itemStack, int value){
        if (itemStack.is(ModItems.ELECTRIC_WHISK.get())){
            if (value >= 0){
                setStoredEnergy(itemStack, getStoredEnergy(itemStack) + value);
            }
        }
    }

    public static void reduceStoredEnergy(ItemStack itemStack, int value){
        if (itemStack.is(ModItems.ELECTRIC_WHISK.get())){
            if (value >= 0){
                setStoredEnergy(itemStack, getStoredEnergy(itemStack) - value);
            }
        }
    }

    public static int getStoredEnergy(ItemStack itemStack) {
        if (!itemStack.is(ModItems.ELECTRIC_WHISK.get())) {
            return 0;
        }
        CompoundTag tag = itemStack.getOrCreateTag();
        if (tag.contains("energy")) {
            return Math.max(0, Math.min(tag.getInt("energy"), MAX_ENERGY));
        }
        if (tag.contains("power")) {
            int migratedEnergy = Math.max(0, Math.min((int) Math.min(Integer.MAX_VALUE, tag.getLong("power") * 10L), MAX_ENERGY));
            tag.putInt("energy", migratedEnergy);
            tag.putLong("power", migratedEnergy / 10L);
            return migratedEnergy;
        }
        tag.putInt("energy", 0);
        tag.putLong("power", 0);
        return 0;
    }

    public static void setStoredEnergy(ItemStack itemStack, int energy) {
        if (!itemStack.is(ModItems.ELECTRIC_WHISK.get())) {
            return;
        }
        CompoundTag tag = itemStack.getOrCreateTag();
        int clampedEnergy = Math.max(0, Math.min(energy, MAX_ENERGY));
        tag.putInt("energy", clampedEnergy);
        tag.putLong("power", clampedEnergy / 10L);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, net.minecraft.world.item.TooltipFlag context) {
        tooltip.add(Component.translatable(BatteryBlockItem.TOOLTIP_TEXT).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.literal(getStoredEnergy(stack) + "/" + MAX_ENERGY + " FE").withStyle(ChatFormatting.GRAY));
        if (Screen.hasShiftDown()) {
            tooltip.add(TextUtil.getShiftText(true));
            tooltip.add(TextUtil.getAltText(false));
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("moderndelight.tooltips.electric_whisk").withStyle(ChatFormatting.GRAY));
        } else if (Screen.hasAltDown()) {
            tooltip.add(TextUtil.getShiftText(false));
            tooltip.add(TextUtil.getAltText(true));
            tooltip.add(Component.literal(" "));
            tooltip.add(TextUtil.getDCCom("20"));
            tooltip.add(TextUtil.getDCSto(String.valueOf(MAX_ENERGY)));
        } else {
            tooltip.add(TextUtil.getShiftText(false));
            tooltip.add(TextUtil.getAltText(false));
        }
        super.appendHoverText(stack, level, tooltip, context);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    public void modifyNearbyItemEntities(Player player, ItemStack stack, Level level) {
        Vec3 eyePosition = player.getEyePosition();
        Vec3 lookDirection = player.getLookAngle().normalize();
        Vec3 endVec = eyePosition.add(lookDirection.scale(5.0));
        if (level.clip(new net.minecraft.world.level.ClipContext(eyePosition, endVec, net.minecraft.world.level.ClipContext.Block.OUTLINE, net.minecraft.world.level.ClipContext.Fluid.NONE, player)).getType() == net.minecraft.world.phys.HitResult.Type.MISS) {
            return;
        }

        List<ItemEntity> itemEntities = level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(5.0), ItemEntity::isAlive);
        for (ItemEntity itemEntity : itemEntities) {
            Vec3 toItem = itemEntity.position().subtract(eyePosition).normalize();
            if (lookDirection.dot(toItem) > 0.99) {
                tryModifyItemEntity(itemEntity, stack, level, player);
            }
        }
    }

    private void tryModifyItemEntity(ItemEntity itemEntity, ItemStack stack, Level level, Player player) {
        if (!stack.is(ModItems.ELECTRIC_WHISK.get())){
            return;
        }
        ItemStack oldStack = itemEntity.getItem();
        int energy = getStoredEnergy(stack);
        int count = oldStack.getCount();
        if (count * 20 > energy){
            player.displayClientMessage(Component.translatable("moderndelight.msg.electric_whisk"), true);
            return;
        }
        BlockPos blockPos = itemEntity.blockPosition();
        net.minecraft.world.SimpleContainer inventory = new net.minecraft.world.SimpleContainer(1);
        inventory.setItem(0, oldStack);
        Optional<WhiskingRecipe> match = level.getRecipeManager().getRecipeFor(WhiskingRecipe.Type.INSTANCE, inventory, level);
        if (match.isPresent()){
            ItemStack result = match.get().getResultItem(level.registryAccess());
            ItemStack newStack = new ItemStack(result.getItem(), count);
            if (newStack.getItem() instanceof PackagedItem){
                player.displayClientMessage(Component.translatable("moderndelight.msg.electric_whisk.need_bowl"), true);
                return;
            }
            playAnimation(stack);
            level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ModSounds.ITEM_ELECTRIC_WHISK_WORKING.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            itemEntity.setItem(newStack);
            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().x - 0.125 + level.random.nextDouble() / 4,
                    0.2,
                    itemEntity.getDeltaMovement().z - 0.125 + level.random.nextDouble() / 4);
            if (oldStack.getItem() instanceof PackagedItem packagedItem){
                level.addFreshEntity(new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(packagedItem.getPackageItem(), count)));
            }
            reduceStoredEnergy(stack, count * 20);
        }
    }

    public static void playAnimation(ItemStack stack){
        if (stack.getItem() instanceof ElectricWhiskItem item){
            if (!item.isWorking()){
                item.setWorking(true);
            }
            scheduler.schedule(() -> item.setWorking(false), 1, TimeUnit.SECONDS);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        if (level.isClientSide) {
            playAnimation(user.getItemInHand(hand));
        } else {
            modifyNearbyItemEntities(user, user.getItemInHand(hand), level);
        }
        return InteractionResultHolder.sidedSuccess(user.getItemInHand(hand), level.isClientSide);
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return new ItemStackEnergyCapabilityProvider(new StackEnergyStorage(stack, MAX_ENERGY, MAX_ENERGY, MAX_ENERGY) {
            @Override
            protected int readEnergy() {
                return getStoredEnergy(stack);
            }

            @Override
            protected void writeEnergy(int energy) {
                setStoredEnergy(stack, energy);
            }
        });
    }


    @Override
    public int getBarWidth(ItemStack stack) {
        if (getStoredEnergy(stack) == 0){
            return 0;
        }
        return (int) Math.min(1 + 12.0 * getStoredEnergy(stack) / MAX_ENERGY, 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float f = getStoredEnergy(stack) / (float)MAX_ENERGY;
        return Mth.color(1 - f, f, 0);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private ElectricWhiskItemRenderer renderer;

            @Override
            public ElectricWhiskItemRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new ElectricWhiskItemRenderer();
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, state -> {
            if (isWorking) {
                return state.setAndContinue(WORKING);
            }
            return state.setAndContinue(RawAnimation.begin().thenPlay("idle"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
