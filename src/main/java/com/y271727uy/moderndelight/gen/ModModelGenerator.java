package com.y271727uy.moderndelight.gen;

import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public final class ModModelGenerator implements DataProvider {

	private static final UnaryOperator<String> NAMESPACE_REWRITE = text -> text.replace("moderndelight", "moderndelight");
	private static final Path BLOCKSTATES = Paths.get("src", "main", "generated", "assets", "moderndelight", "blockstates");
	private static final Path MODELS = Paths.get("src", "main", "generated", "assets", "moderndelight", "models");

	private static final Path OUTPUT_BLOCKSTATES = Paths.get("src", "generated", "resources", "assets", "moderndelight", "blockstates");
	private static final Path OUTPUT_MODELS = Paths.get("src", "generated", "resources", "assets", "moderndelight", "models");

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		return CompletableFuture.allOf(
				LegacyJsonMirror.mirrorJsonTree(cachedOutput, BLOCKSTATES, OUTPUT_BLOCKSTATES, NAMESPACE_REWRITE),
				LegacyJsonMirror.mirrorJsonTree(cachedOutput, MODELS, OUTPUT_MODELS, NAMESPACE_REWRITE)
		);
	}

	@Override
	public String getName() {
		return ModernDelightMain.MOD_ID + " models";
	}
}
