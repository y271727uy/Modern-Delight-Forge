package com.y271727uy.moderndelight;

import com.y271727uy.moderndelight.gen.ModBlockTagGenerator;
import com.y271727uy.moderndelight.gen.ModBlockLootGenerator;
import com.y271727uy.moderndelight.gen.ModAdvancementGenerator;
import com.y271727uy.moderndelight.gen.ModFluidTagGenerator;
import com.y271727uy.moderndelight.gen.ModItemTagGenerator;
import com.y271727uy.moderndelight.gen.ModLangCNGenerator;
import com.y271727uy.moderndelight.gen.ModLangGenerator;
import com.y271727uy.moderndelight.gen.ModModelGenerator;
import com.y271727uy.moderndelight.gen.ModRecipeGenerator;
import com.y271727uy.moderndelight.gen.ModWorldGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModernDelightMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModernDelightDataGenerator {
	private ModernDelightDataGenerator() {
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		ModernDelightMain.LOGGER.info("Forge data generation hook registered for {}", ModernDelightMain.MOD_ID);
		event.getGenerator().addProvider(event.includeClient(), new ModModelGenerator());
		event.getGenerator().addProvider(event.includeClient(), new ModLangGenerator());
		event.getGenerator().addProvider(event.includeClient(), new ModLangCNGenerator());
		event.getGenerator().addProvider(event.includeServer(), new ModItemTagGenerator());
		event.getGenerator().addProvider(event.includeServer(), new ModFluidTagGenerator());
		event.getGenerator().addProvider(event.includeServer(), new ModBlockTagGenerator());
		event.getGenerator().addProvider(event.includeServer(), new ModBlockLootGenerator(event.getGenerator().getPackOutput()));
		event.getGenerator().addProvider(event.includeServer(), new ModAdvancementGenerator());
		event.getGenerator().addProvider(event.includeServer(), new ModWorldGenerator());
		event.getGenerator().addProvider(event.includeServer(), new ModRecipeGenerator(event.getGenerator().getPackOutput()));
	}
}
