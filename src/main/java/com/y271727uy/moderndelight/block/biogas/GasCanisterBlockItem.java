package com.y271727uy.moderndelight.block.biogas;

import com.y271727uy.moderndelight.util.FluidStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

public class GasCanisterBlockItem extends BlockItem {
    public GasCanisterBlockItem(Block block) {
        super(block, new Item.Properties().stacksTo(16));
    }

    @Override
    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(@Nonnull ItemStack stack) {
        CompoundTag nbtCompound = BlockItem.getBlockEntityData(stack);
        if (nbtCompound != null && nbtCompound.contains("gas_canister.fluid_amount")) {
            long fluidAmount = nbtCompound.getLong("gas_canister.fluid_amount");
            return (int) Math.min(13.0f * (float) FluidStack.convertDropletsToMb(fluidAmount) / (float) GasCanisterBlockEntity.getMaxCapacity(), 13);
        }
        return 0;
    }

    @Override
    public int getBarColor(@Nonnull ItemStack stack) {
        CompoundTag nbtCompound = BlockItem.getBlockEntityData(stack);
        if (nbtCompound != null && nbtCompound.contains("gas_canister.fluid_amount")) {
            long fluidAmount = nbtCompound.getLong("gas_canister.fluid_amount");
            float f = (float) FluidStack.convertDropletsToMb(fluidAmount) / (float) GasCanisterBlockEntity.getMaxCapacity();
            return Mth.color(f, 1.0f - f, 0.0f);
        }
        return 0x00ff00;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        CompoundTag nbtCompound = BlockItem.getBlockEntityData(stack);
        if (nbtCompound != null && nbtCompound.contains("gas_canister.fluid_amount")) {
            long mb = FluidStack.convertDropletsToMb(nbtCompound.getLong("gas_canister.fluid_amount"));
            tooltip.add(Component.literal("Fluid: ").append(Component.literal(String.valueOf(mb)).withStyle(net.minecraft.ChatFormatting.GRAY))
                    .append(Component.literal("mB").withStyle(net.minecraft.ChatFormatting.GRAY)));
        }
    }
}
