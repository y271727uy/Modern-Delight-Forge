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
public final class ModFluidTagGenerator implements DataProvider {

	private static final UnaryOperator<String> NAMESPACE_REWRITE = text -> text.replace("moderndelight", "moderndelight");
	private static final Path BAKINGDELIGHT_TAGS = Paths.get("src", "main", "generated", "data", "moderndelight", "tags", "fluids");
	private static final Path COMMON_TAGS = Paths.get("src", "main", "generated", "data", "c", "tags", "fluids");

	private static final Path MOD_OUTPUT = Paths.get("src", "generated", "resources", "data", "moderndelight", "tags", "fluids");
	private static final Path COMMON_OUTPUT = Paths.get("src", "generated", "resources", "data", "c", "tags", "fluids");

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		return CompletableFuture.allOf(
				LegacyJsonMirror.mirrorJsonTree(cachedOutput, BAKINGDELIGHT_TAGS, MOD_OUTPUT, NAMESPACE_REWRITE),
				LegacyJsonMirror.mirrorJsonTree(cachedOutput, COMMON_TAGS, COMMON_OUTPUT, NAMESPACE_REWRITE)
		);
	}

	@Override
	public String getName() {
		return ModernDelightMain.MOD_ID + " fluid tags";
	}
}
