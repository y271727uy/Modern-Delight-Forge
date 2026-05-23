package com.y271727uy.moderndelight.event;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.util.registry_util.ModBrewingRecipe;
import com.y271727uy.moderndelight.util.registry_util.ModCompostingChances;
import com.y271727uy.moderndelight.util.registry_util.ModFuels;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = ModernDelightMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModernDelightCommonEvents {
    private ModernDelightCommonEvents() {
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        // Register fuels, composting chances, and brewing recipes during common setup
        ModFuels.registerFuels();
        ModCompostingChances.registerCompostingChances();
        ModBrewingRecipe.registerModBrewingRecipe();
    }
}

