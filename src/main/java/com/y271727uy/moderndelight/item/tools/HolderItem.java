package com.y271727uy.moderndelight.item.tools;

import com.y271727uy.moderndelight.client.render.ModernDelightItemRenderer;
import com.y271727uy.moderndelight.tag.TagKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.function.Consumer;

public class HolderItem extends Item {
    public HolderItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide){
            return InteractionResult.SUCCESS;
        }
        BlockPos blockPos = context.getClickedPos();
        ItemStack holder = context.getItemInHand();
        Player player = context.getPlayer();
        BlockState aimedState = level.getBlockState(blockPos);
        if (!aimedState.is(TagKeys.CAN_PICK) || player == null) {
            return InteractionResult.PASS;
        }
        if (level.getBlockEntity(blockPos) instanceof Container container && container.stillValid(player)) {
            int slots = container.getContainerSize();
            ItemStack holdingStack = getHoldingStack(holder);
            if (holdingStack.isEmpty()) {
                for (int i = 0; i < slots; i++) {
                    ItemStack stack = container.getItem(i);
                    if (!stack.isEmpty()) {
                        setHoldingStack(stack.copy(), holder);
                        container.setItem(i, ItemStack.EMPTY);
                        container.setChanged();
                        level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0f, level.random.nextFloat() + 0.8f);
                        break;
                    }
                }
            } else {
                for (int i = 0; i < slots; i++) {
                    ItemStack stack = container.getItem(i);
                    if (stack.isEmpty()) {
                        container.setItem(i, holdingStack);
                        removeHoldingStack(holder);
                        container.setChanged();
                        level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0f, level.random.nextFloat() + 0.3f);
                        break;
                    }
                    if (holdingStack.is(stack.getItem()) && !stack.hasTag()) {
                        int maxStackSize = stack.getMaxStackSize();
                        int space = maxStackSize - stack.getCount();
                        if (space > 0) {
                            int move = Math.min(space, holdingStack.getCount());
                            stack.grow(move);
                            holdingStack.shrink(move);
                            if (holdingStack.isEmpty()) {
                                removeHoldingStack(holder);
                            } else {
                                setHoldingStack(holdingStack, holder);
                            }
                            container.setChanged();
                            level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0f, level.random.nextFloat() + 0.3f);
                            return InteractionResult.CONSUME;
                        }
                    }
                }
            }
        }
        return InteractionResult.CONSUME;
    }

    public static ItemStack getHoldingStack(ItemStack holder) {
        CompoundTag nbt = holder.getTagElement("holding_stack");
        if (nbt != null){
            return ItemStack.of(nbt);
        }
        return ItemStack.EMPTY;
    }
    public static void setHoldingStack(ItemStack holdingStack, ItemStack holder){
        CompoundTag nbt = holder.getOrCreateTagElement("holding_stack");
        holdingStack.save(nbt);
    }
    public static void removeHoldingStack(ItemStack holder){
        holder.removeTagKey("holding_stack");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack holder = user.getItemInHand(hand);
        if (level.isClientSide){
            return InteractionResultHolder.sidedSuccess(holder, true);
        }
        ItemStack otherStack;
        if (hand == InteractionHand.MAIN_HAND){
            otherStack = user.getOffhandItem();
        } else {
            otherStack = user.getMainHandItem();
        }
        ItemStack holdingStack = getHoldingStack(holder);
        if (!holdingStack.isEmpty()){
            user.getInventory().add(holdingStack);
            removeHoldingStack(holder);
            level.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,1.0f,level.random.nextFloat()+0.8f);
        } else if (!otherStack.isEmpty()){
            CompoundTag nbtCompound = BlockItem.getBlockEntityData(otherStack);
            if (otherStack.getItem() == this){
                ItemStack otherStackHolding = getHoldingStack(otherStack);
                if (otherStackHolding.getItem() == this){
                    user.displayClientMessage(Component.translatable("moderndelight.tooltips.prohibit_unlimited_nesting"), true);
                    return InteractionResultHolder.consume(holder);
                }
            }
            if (nbtCompound != null) {
                if (nbtCompound.contains("Items", 9)) {
                    user.displayClientMessage(Component.translatable("moderndelight.tooltips.prohibit_unlimited_nesting"), true);
                    return InteractionResultHolder.consume(holder);
                }
            }
            setHoldingStack(otherStack.copy(),holder);
            otherStack.shrink(otherStack.getCount());
            level.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS,1.0f,level.random.nextFloat()+0.8f);
        }
        return InteractionResultHolder.consume(holder);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag options) {
        if(Screen.hasShiftDown()){
            tooltip.add(Component.translatable("moderndelight.tooltips.shift_front").withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("moderndelight.tooltips.holder").withStyle(ChatFormatting.GRAY));

        } else {
            tooltip.add(Component.translatable("moderndelight.tooltips.shift_front").withStyle(ChatFormatting.DARK_GRAY));
        }
        super.appendHoverText(stack, level, tooltip, options);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new ModernDelightItemRenderer(
                    Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                    Minecraft.getInstance().getEntityModels()
            );

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }
}
