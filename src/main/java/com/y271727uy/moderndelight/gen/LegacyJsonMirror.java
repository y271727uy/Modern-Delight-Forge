package com.y271727uy.moderndelight.gen;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
final class LegacyJsonMirror {

	private LegacyJsonMirror() {
	}

	static CompletableFuture<?> mirrorJsonTree(CachedOutput cachedOutput, Path sourceRoot, Path destinationRoot) {
		return mirrorJsonTree(cachedOutput, sourceRoot, destinationRoot, UnaryOperator.identity());
	}

	static CompletableFuture<?> mirrorJsonTree(CachedOutput cachedOutput, Path sourceRoot, Path destinationRoot, UnaryOperator<String> contentTransformer) {
		if (!Files.exists(sourceRoot)) {
			return CompletableFuture.completedFuture(null);
		}

		List<CompletableFuture<?>> futures = new ArrayList<>();
		try (Stream<Path> paths = Files.walk(sourceRoot)) {
			paths.filter(Files::isRegularFile)
					.filter(path -> path.toString().endsWith(".json"))
					.forEach(path -> {
						Path relative = sourceRoot.relativize(path);
						Path destination = destinationRoot.resolve(relative.toString());
						try {
							Files.createDirectories(destination.getParent());
							String transformed = contentTransformer.apply(Files.readString(path));
							JsonElement json = JsonParser.parseString(transformed);
							futures.add(DataProvider.saveStable(cachedOutput, json, destination));
						} catch (IOException exception) {
							throw new UncheckedIOException("Failed to mirror JSON file: " + path, exception);
						}
					});
		} catch (IOException exception) {
			throw new UncheckedIOException("Failed to scan JSON tree: " + sourceRoot, exception);
		}

		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	static CompletableFuture<?> mirrorJsonFile(CachedOutput cachedOutput, Path sourceFile, Path destinationFile, UnaryOperator<String> contentTransformer) {
		if (!Files.exists(sourceFile)) {
			return CompletableFuture.completedFuture(null);
		}
		try {
			Files.createDirectories(destinationFile.getParent());
			String transformed = contentTransformer.apply(Files.readString(sourceFile));
			JsonElement json = JsonParser.parseString(transformed);
			return DataProvider.saveStable(cachedOutput, json, destinationFile);
		} catch (IOException exception) {
			throw new UncheckedIOException("Failed to mirror JSON file: " + sourceFile, exception);
		}
	}
}



