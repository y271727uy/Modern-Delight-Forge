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
public final class ModAdvancementGenerator implements DataProvider {

	private static final UnaryOperator<String> NAMESPACE_REWRITE = text -> text.replace("moderndelight", "moderndelight");
	private static final Path HAND_AUTHORED = Paths.get("src", "main", "resources", "data", "moderndelight", "advancements");
	private static final Path LEGACY_GENERATED = Paths.get("src", "main", "generated", "data", "minecraft", "advancements", "moderndelight");
	private static final Path OUTPUT = Paths.get("src", "generated", "resources", "data", "moderndelight", "advancements");

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		return CompletableFuture.allOf(
				LegacyJsonMirror.mirrorJsonTree(cachedOutput, HAND_AUTHORED, OUTPUT, NAMESPACE_REWRITE),
				LegacyJsonMirror.mirrorJsonTree(cachedOutput, LEGACY_GENERATED, OUTPUT, NAMESPACE_REWRITE)
		);
	}

	@Override
	public String getName() {
		return ModernDelightMain.MOD_ID + " advancements";
	}
}
