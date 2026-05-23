package com.y271727uy.moderndelight.util.registry_util;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.item.ModItems;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModernDelightMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModFuels {
    public static void registerFuels(){
        ModernDelightMain.LOGGER.info("Registering Mod Fuels for " + ModernDelightMain.MOD_ID);
    }

    @SubscribeEvent
    public static void onFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().is(ModItems.KNEADING_STICK.get())) {
            event.setBurnTime(200);
        } else if (event.getItemStack().is(ModItems.SUNFLOWER_SEED_PEEL.get())) {
            event.setBurnTime(60);
        } else if (event.getItemStack().is(ModBlocks.WOODEN_BASIN.get().asItem())) {
            event.setBurnTime(1350);
        } else if (event.getItemStack().is(ModItems.FILTER.get())) {
            event.setBurnTime(160);
        } else if (event.getItemStack().is(ModItems.PACKAGING_BAG.get())) {
            event.setBurnTime(100);
        } else if (event.getItemStack().is(ModItems.DIRTY_PACKAGING_BAG.get())) {
            event.setBurnTime(120);
        } else if (event.getItemStack().is(ModItems.MULTIFUNCTIONAL_WRAPPING_PAPER.get())) {
            event.setBurnTime(100);
        } else if (event.getItemStack().is(ModItems.DIRTY_WRAPPING_PAPER.get())) {
            event.setBurnTime(80);
        }
    }
}
