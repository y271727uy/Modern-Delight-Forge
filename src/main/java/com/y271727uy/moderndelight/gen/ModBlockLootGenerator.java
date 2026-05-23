package com.y271727uy.moderndelight.gen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class ModBlockLootGenerator implements DataProvider {

	private final PackOutput.PathProvider output;
	private final List<Block> blocks;

	public ModBlockLootGenerator(PackOutput packOutput) {
		this.output = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "loot_tables");
		this.blocks = collectLootableBlocks();
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		List<CompletableFuture<?>> futures = new ArrayList<>();
		for (Block block : blocks) {
			ResourceLocation blockId = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(block);
			ResourceLocation itemId = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(block.asItem());
			if (blockId == null || itemId == null) {
				continue;
			}
			JsonObject lootTable = createSelfDropLootTable(itemId);
			Path path = output.json(new ResourceLocation(blockId.getNamespace(), "blocks/" + blockId.getPath()));
			futures.add(DataProvider.saveStable(cachedOutput, lootTable, path));
		}
		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	@Override
	public String getName() {
		return ModernDelightMain.MOD_ID + " block loot tables";
	}

	private List<Block> collectLootableBlocks() {
		List<Block> lootable = new ArrayList<>();
		for (Field field : ModBlocks.class.getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers()) || !Modifier.isFinal(field.getModifiers())) {
				continue;
			}
			try {
				Object value = field.get(null);
				if (value instanceof net.minecraftforge.registries.RegistryObject<?> registryObject) {
					Object blockObj = registryObject.get();
					if (blockObj instanceof Block block && block.asItem() != Items.AIR) {
						lootable.add(block);
					}
				} else if (value instanceof Block block && block.asItem() != Items.AIR) {
					lootable.add(block);
				}
			} catch (IllegalAccessException exception) {
				throw new UncheckedIOException(new java.io.IOException("Failed to inspect block field: " + field.getName(), exception));
			}
		}
		return lootable;
	}

	private JsonObject createSelfDropLootTable(ResourceLocation itemId) {
		JsonObject table = new JsonObject();
		table.addProperty("type", "minecraft:block");

		JsonArray pools = new JsonArray();
		JsonObject pool = new JsonObject();
		pool.addProperty("rolls", 1);

		JsonArray entries = new JsonArray();
		JsonObject entry = new JsonObject();
		entry.addProperty("type", "minecraft:item");
		entry.addProperty("name", itemId.toString());
		entries.add(entry);
		pool.add("entries", entries);

		JsonArray conditions = new JsonArray();
		JsonObject survivesExplosion = new JsonObject();
		survivesExplosion.addProperty("condition", "minecraft:survives_explosion");
		conditions.add(survivesExplosion);
		pool.add("conditions", conditions);

		pools.add(pool);
		table.add("pools", pools);
		return table;
	}
}
