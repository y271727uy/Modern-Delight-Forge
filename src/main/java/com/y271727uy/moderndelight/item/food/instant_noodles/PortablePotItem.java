package com.y271727uy.moderndelight.item.food.instant_noodles;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.tag.TagKeys;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.List;

public class PortablePotItem extends net.minecraft.world.item.Item {
    public PortablePotItem() {
        super(new net.minecraft.world.item.Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = net.minecraft.world.item.Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }

        BlockPos pos = hit.getBlockPos();
        if (!level.getFluidState(pos).is(FluidTags.WATER)) {
            return InteractionResultHolder.pass(stack);
        }

        if (hasWater(stack)) {
            return InteractionResultHolder.pass(stack);
        }

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean("has_water", true);
        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        level.playSound(null, pos, net.minecraft.sounds.SoundEvents.BOTTLE_FILL, net.minecraft.sounds.SoundSource.PLAYERS, 0.8F, 0.8F + level.getRandom().nextFloat() * 0.4F);

        if (hasNoodle(stack) && hasQuicklime(stack)) {
            ItemStack cooked = new ItemStack(ModItems.COOKED_PORTABLE_POT.get());
            CompoundTag cookedTag = cooked.getOrCreateTag();
            cookedTag.put("noodles_data", getOrCreateNoodlesData(tag));
            player.getInventory().placeItemBackInInventory(new ItemStack(ModItems.DIRTY_WRAPPING_PAPER.get()));
            player.setItemInHand(hand, cooked);
            return InteractionResultHolder.consume(cooked);
        }

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        String noodleType = getNoodleTypeKey(stack);
        if (noodleType != null) {
            return noodleType;
        }
        if (isUnhealthy(stack)) {
            return TextUtil.NOODLE_UNHEALTHY;
        }
        return super.getDescriptionId(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(hasQuicklime(stack) ? TextUtil.POT_HAS_QUICKLIME : TextUtil.POT_MISS_QUICKLIME)
                .withStyle(hasQuicklime(stack) ? ChatFormatting.GRAY : ChatFormatting.DARK_RED));
        tooltip.add(Component.translatable(hasWater(stack) ? TextUtil.POT_HAS_WATER : TextUtil.POT_MISS_WATER)
                .withStyle(hasWater(stack) ? ChatFormatting.AQUA : ChatFormatting.DARK_RED));

        List<Item> noodles = getWorldNoodleItems(stack);
        if (noodles != null) {
            tooltip.add(Component.translatable(TextUtil.INGREDIENTS).withStyle(ChatFormatting.DARK_GRAY));
            for (Item item : noodles) {
                if (item != Items.AIR) {
                    tooltip.add(Component.translatable(item.getDescriptionId()).withStyle(ChatFormatting.GRAY));
                }
            }
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }

    public static boolean hasNoodle(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("noodles_data");
    }

    public static boolean hasWater(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("has_water") && tag.getBoolean("has_water");
    }

    public static boolean hasQuicklime(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("has_quicklime") && tag.getBoolean("has_quicklime");
    }

    public static String getNoodleTypeKey(ItemStack stack) {
        List<Item> items = getWorldNoodleItems(stack);
        if (items == null) {
            return null;
        }
        if (containsAnyTagged(items, TagKeys.RAW_BEEF) && containsAnyItem(items, Items.CARROT) && containsAnyTagged(items, TagKeys.CABBAGE)) {
            return translationKey("braised_beef_noodle_soup");
        }
        if (containsAnyItem(items, Items.BROWN_MUSHROOM) && containsAnyItem(items, Items.CHICKEN) && containsAnyTagged(items, TagKeys.CABBAGE)) {
            return translationKey("stewed_chicken_noodle_with_mushroom");
        }
        if (containsAnyTagged(items, TagKeys.RAW_PORK) && containsAnyItem(items, Items.DRIED_KELP) && containsAnyItem(items, Items.EGG)) {
            return translationKey("tonkotsu_ramen");
        }
        return null;
    }

    static List<Item> getWorldNoodleItems(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("noodles_data")) {
            return null;
        }

        CompoundTag noodleTag = tag.getCompound("noodles_data");
        ItemStack noodles = ItemStack.of(noodleTag);
        CompoundTag ingredientTag = noodles.getTag() == null ? null : noodles.getTag().getCompound("instant_noodles_ingredients");
        if (ingredientTag == null) {
            return null;
        }

        List<Item> items = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            String key = "ingredients_" + i;
            if (!ingredientTag.contains(key)) {
                continue;
            }
            String id = ingredientTag.getString(key);
            if (id.isEmpty()) {
                continue;
            }
            ResourceLocation location = ResourceLocation.tryParse(id);
            if (location == null) {
                continue;
            }
            Item item = BuiltInRegistries.ITEM.get(location);
            if (item != Items.AIR) {
                items.add(item);
            }
        }
        return items.isEmpty() ? null : items;
    }

    public static boolean isUnhealthy(ItemStack stack) {
        if (getNoodleTypeKey(stack) != null) {
            return false;
        }
        List<Item> items = getWorldNoodleItems(stack);
        if (items == null) {
            return false;
        }
        for (Item item : items) {
            if (!item.isEdible()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isQuicklime(Item item) {
        return new ItemStack(item).is(TagKeys.QUICKLIMES);
    }

    private static boolean containsAnyItem(List<Item> items, Item required) {
        for (Item item : items) {
            if (item == required) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsAnyTagged(List<Item> items, net.minecraft.tags.TagKey<Item> tag) {
        for (Item item : items) {
            if (new ItemStack(item).is(tag)) {
                return true;
            }
        }
        return false;
    }

    private static CompoundTag getOrCreateNoodlesData(CompoundTag potTag) {
        if (potTag.contains("noodles_data")) {
            return potTag.getCompound("noodles_data").copy();
        }
        return new CompoundTag();
    }

    private static String translationKey(String id) {
        return "items." + ModernDelightMain.MOD_ID + ".packaged_instant_noodles." + id;
    }
}
