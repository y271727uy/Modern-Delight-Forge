package com.y271727uy.moderndelight.gen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.ParametersAreNonnullByDefault;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class ModRecipeGenerator implements DataProvider {

	private static final Path SOURCE_ROOT = Paths.get("src", "main", "resources", "data", "moderndelight", "recipes");

	private final PackOutput.PathProvider output;

	public ModRecipeGenerator(PackOutput packOutput) {
		this.output = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "recipes");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		if (!Files.exists(SOURCE_ROOT)) {
			return CompletableFuture.completedFuture(null);
		}

		List<CompletableFuture<?>> futures = new ArrayList<>();
		try (Stream<Path> paths = Files.walk(SOURCE_ROOT)) {
			paths.filter(Files::isRegularFile)
					.filter(path -> path.toString().endsWith(".json"))
					.forEach(path -> {
						ResourceLocation id = toOutputId(path);
						JsonElement transformed = rewriteRecipe(readJson(path));
						futures.add(DataProvider.saveStable(cachedOutput, transformed, output.json(id)));
					});
		} catch (IOException exception) {
			throw new UncheckedIOException("Failed to scan legacy moderndelight recipes", exception);
		}

		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	@Override
	public String getName() {
		return ModernDelightMain.MOD_ID + " legacy recipe migration";
	}

	private JsonElement readJson(Path path) {
		try (Reader reader = Files.newBufferedReader(path)) {
			return JsonParser.parseReader(reader);
		} catch (IOException exception) {
			throw new UncheckedIOException("Failed to read recipe JSON: " + path, exception);
		}
	}

	private ResourceLocation toOutputId(Path sourcePath) {
		Path relative = SOURCE_ROOT.relativize(sourcePath);
		String path = relative.toString().replace('\\', '/');
		if (path.endsWith(".json")) {
			path = path.substring(0, path.length() - 5);
		}
		return new ResourceLocation(ModernDelightMain.MOD_ID, path);
	}

	private JsonElement rewriteRecipe(JsonElement element) {
		if (element.isJsonNull()) {
			return element;
		}
		if (element.isJsonObject()) {
			return rewriteObject(element.getAsJsonObject());
		}
		if (element.isJsonArray()) {
			JsonArray rewritten = new JsonArray();
			for (JsonElement child : element.getAsJsonArray()) {
				rewritten.add(rewriteRecipe(child));
			}
			return rewritten;
		}
		if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
			return new com.google.gson.JsonPrimitive(element.getAsString().replace("moderndelight:", "moderndelight:"));
		}
		return element;
	}

	private JsonObject rewriteObject(JsonObject input) {
		JsonObject output = new JsonObject();
		List<JsonObject> convertedConditions = new ArrayList<>();

		if (input.has("conditions") && input.get("conditions").isJsonArray()) {
			for (JsonElement condition : input.getAsJsonArray("conditions")) {
				convertedConditions.addAll(rewriteCondition(condition));
			}
		}
		if (input.has("fabric:load_conditions") && input.get("fabric:load_conditions").isJsonArray()) {
			for (JsonElement condition : input.getAsJsonArray("fabric:load_conditions")) {
				convertedConditions.addAll(rewriteCondition(condition));
			}
		}

		for (String key : input.keySet()) {
			if ("fabric:load_conditions".equals(key) || "conditions".equals(key)) {
				continue;
			}
			output.add(key, rewriteRecipe(input.get(key)));
		}

		if (!convertedConditions.isEmpty()) {
			JsonArray conditions = new JsonArray();
			for (JsonObject condition : convertedConditions) {
				conditions.add(condition);
			}
			output.add("conditions", conditions);
		}

		return output;
	}

	private List<JsonObject> rewriteCondition(JsonElement conditionElement) {
		List<JsonObject> rewritten = new ArrayList<>();
		if (!conditionElement.isJsonObject()) {
			return rewritten;
		}

		JsonObject condition = conditionElement.getAsJsonObject();
		String type = condition.has("condition") ? condition.get("condition").getAsString() : condition.has("type") ? condition.get("type").getAsString() : "";
		if ("fabric:all_mods_loaded".equals(type) || "fabric:any_mod_loaded".equals(type)) {
			JsonArray values = condition.has("values") && condition.get("values").isJsonArray() ? condition.getAsJsonArray("values") : new JsonArray();
			JsonArray nested = new JsonArray();
			for (JsonElement value : values) {
				if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()) {
					JsonObject modLoaded = new JsonObject();
					modLoaded.addProperty("type", "forge:mod_loaded");
					modLoaded.addProperty("modid", value.getAsString());
					nested.add(modLoaded);
				}
			}
			if (nested.size() == 1) {
				rewritten.add(nested.get(0).getAsJsonObject());
			} else if (nested.size() > 1) {
				JsonObject group = new JsonObject();
				group.addProperty("type", "fabric:any_mod_loaded".equals(type) ? "forge:or" : "forge:and");
				group.add("values", nested);
				rewritten.add(group);
			}
			return rewritten;
		}

		JsonObject copy = new JsonObject();
		for (String key : condition.keySet()) {
			JsonElement value = condition.get(key);
			copy.add("condition".equals(key) ? "type" : key, rewriteRecipe(value));
		}
		if (!copy.has("type") && condition.has("condition")) {
			copy.addProperty("type", condition.get("condition").getAsString().replace("fabric:", "forge:"));
		}
		rewritten.add(copy);
		return rewritten;
	}
}
