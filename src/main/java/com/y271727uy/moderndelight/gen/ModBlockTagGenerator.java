package com.y271727uy.moderndelight.gen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class ModBlockTagGenerator implements DataProvider {

	private static final Path OUTPUT_ROOT = Paths.get("src", "generated", "resources", "data");

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		List<CompletableFuture<?>> futures = List.of(
				writeTag(cachedOutput, "moderndelight", "whisk_mineable", List.of(
						ModBlocks.MASHED_POTATO_BLOCK.get(),
						ModBlocks.GLASS_BOWL.get(),
						ModBlocks.WHEAT_DOUGH.get(),
						ModBlocks.PIZZA_WIP.get(),
						ModBlocks.RAW_PIZZA.get(),
						ModBlocks.PIZZA.get(),
						ModBlocks.BAKING_TRAY.get(),
						ModBlocks.DEEP_FRY_BASKET.get(),
						ModBlocks.WOODEN_PLATE.get(),
						ModBlocks.GLASS_CUP.get(),
						ModBlocks.JUICE_EXTRACTOR.get(),
						ModBlocks.ICE_CREAM_MAKER.get(),
						ModBlocks.STEAMED_PUMPKIN.get(),
						ModBlocks.CHERRY_MILK_TEA.get(),
						ModBlocks.ROSE_ICE_TEA.get()
				)),
				writeTag(cachedOutput, "moderndelight", "crowbar_destroyable", List.of(
						ModBlocks.GLASS_BOWL.get(),
						ModBlocks.GLASS_CUP.get(),
						ModBlocks.BAMBOO_GRATE.get(),
						ModBlocks.BAMBOO_COVER.get(),
						ModBlocks.FAN_BLADE.get(),
						ModBlocks.DEEP_FRY_BASKET.get(),
						ModBlocks.KITCHEN_UTENSIL_HOLDER.get(),
						ModBlocks.WOODEN_PLATE.get(),
						ModBlocks.GAS_CANISTER.get(),
						ModBlocks.CUISINE_TABLE.get(),
						ModBlocks.ANDESITE_CABINET.get(),
						ModBlocks.DIORITE_CABINET.get(),
						ModBlocks.GRANITE_CABINET.get(),
						ModBlocks.DEEPSLATE_CABINET.get(),
						ModBlocks.BLACKSTONE_CABINET.get(),
						ModBlocks.BASALT_CABINET.get(),
						ModBlocks.OBSIDIAN_CABINET.get(),
						ModBlocks.ICE_CREAM_MAKER.get(),
						ModBlocks.JUICE_EXTRACTOR.get()
				)),
				writeTag(cachedOutput, "moderndelight", "can_pick", List.of(
						ModBlocks.GLASS_BOWL.get(),
						ModBlocks.GLASS_CUP.get(),
						ModBlocks.WOODEN_PLATE.get(),
						ModBlocks.KITCHEN_UTENSIL_HOLDER.get(),
						ModBlocks.CUISINE_TABLE.get(),
						ModBlocks.ANDESITE_CABINET.get(),
						ModBlocks.DIORITE_CABINET.get(),
						ModBlocks.GRANITE_CABINET.get(),
						ModBlocks.DEEPSLATE_CABINET.get(),
						ModBlocks.BLACKSTONE_CABINET.get(),
						ModBlocks.BASALT_CABINET.get(),
						ModBlocks.OBSIDIAN_CABINET.get(),
						ModBlocks.BAKING_TRAY.get(),
						ModBlocks.DEEP_FRY_BASKET.get(),
						ModBlocks.JUICE_EXTRACTOR.get(),
						ModBlocks.ICE_CREAM_MAKER.get(),
						ModBlocks.GAS_CANISTER.get(),
						ModBlocks.ELECTRIC_STEAMER.get(),
						ModBlocks.OVEN.get(),
						ModBlocks.FREEZER.get(),
						ModBlocks.BURNING_GAS_COOKING_STOVE.get(),
						ModBlocks.GAS_COOKING_STOVE.get(),
						ModBlocks.WOODEN_BASIN.get(),
						ModBlocks.STEAMED_PUMPKIN.get(),
						ModBlocks.CHERRY_MILK_TEA.get(),
						ModBlocks.ROSE_ICE_TEA.get()
				)),
				writeTag(cachedOutput, "moderndelight", "danger_blocks", List.of(
						Blocks.FIRE,
						Blocks.SOUL_FIRE,
						Blocks.MAGMA_BLOCK,
						Blocks.CAMPFIRE,
						Blocks.SOUL_CAMPFIRE,
						Blocks.LAVA,
						Blocks.LAVA_CAULDRON,
						Blocks.TNT,
						Blocks.CACTUS,
						Blocks.SWEET_BERRY_BUSH,
						ModBlocks.BURNING_GAS_COOKING_STOVE.get()
				)),
				writeTag(cachedOutput, "create", "wrench_pickup", List.of(
						ModBlocks.BAKING_TRAY.get(),
						ModBlocks.DEEP_FRY_BASKET.get(),
						ModBlocks.ICE_CREAM_MAKER.get(),
						ModBlocks.JUICE_EXTRACTOR.get(),
						ModBlocks.ELECTRIC_STEAMER.get(),
						ModBlocks.OVEN.get(),
						ModBlocks.FREEZER.get(),
						ModBlocks.FAN_BLADE.get(),
						ModBlocks.CHARGING_POST.get(),
						ModBlocks.ELECTRICIANS_DESK.get(),
						ModBlocks.PHOTOVOLTAIC_GENERATOR.get(),
						ModBlocks.AC_DC_CONVERTER.get(),
						ModBlocks.WIND_TURBINE_CONTROLLER.get(),
						ModBlocks.FARADAY_GENERATOR.get(),
						ModBlocks.TESLA_COIL.get(),
						ModBlocks.STERLING_ENGINE.get(),
						ModBlocks.SIMPLE_BATTERY.get(),
						ModBlocks.INTERMEDIATE_BATTERY.get(),
						ModBlocks.ADVANCE_BATTERY.get(),
						ModBlocks.DIMENSION_BATTERY.get(),
						ModBlocks.GAS_CANISTER.get(),
						ModBlocks.BURNING_GAS_COOKING_STOVE.get(),
						ModBlocks.GAS_COOKING_STOVE.get()
				)),
				writeTag(cachedOutput, "create", "passive_boiler_heaters", List.of(
						ModBlocks.OVEN.get(),
						ModBlocks.BURNING_GAS_COOKING_STOVE.get()
				)),
				writeTag(cachedOutput, "farmersdelight", "heat_sources", List.of(
						Blocks.FIRE,
						Blocks.SOUL_FIRE,
						Blocks.CAMPFIRE,
						Blocks.SOUL_CAMPFIRE,
						Blocks.MAGMA_BLOCK,
						Blocks.LAVA,
						ModBlocks.OVEN.get(),
						ModBlocks.BURNING_GAS_COOKING_STOVE.get()
				))
		);
		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	@Override
	public String getName() {
		return ModernDelightMain.MOD_ID + " block tags";
	}

	private CompletableFuture<?> writeTag(CachedOutput cachedOutput, String namespace, String path, List<Block> blocks) {
		JsonObject tag = new JsonObject();
		tag.addProperty("replace", false);
		JsonArray values = new JsonArray();
		for (Block block : blocks) {
			ResourceLocation blockId = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(block);
			if (blockId != null) {
				values.add(blockId.toString());
			}
		}
		tag.add("values", values);

		Path file = OUTPUT_ROOT.resolve(namespace).resolve("tags").resolve("blocks").resolve(path + ".json");
		try {
			Files.createDirectories(file.getParent());
		} catch (IOException exception) {
			throw new UncheckedIOException("Failed to create tag output directory", exception);
		}
		return DataProvider.saveStable(cachedOutput, tag, file);
	}
}
