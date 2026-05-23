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
public final class ModLangCNGenerator implements DataProvider {

	private static final UnaryOperator<String> NAMESPACE_REWRITE = text -> text.replace("moderndelight", "moderndelight");
	private static final Path SOURCE = Paths.get("src", "main", "generated", "assets", "moderndelight", "lang", "zh_cn.json");
	private static final Path DESTINATION = Paths.get("src", "generated", "resources", "assets", "moderndelight", "lang", "zh_cn.json");

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		return LegacyJsonMirror.mirrorJsonFile(cachedOutput, SOURCE, DESTINATION, NAMESPACE_REWRITE);
	}

	@Override
	public String getName() {
		return ModernDelightMain.MOD_ID + " zh_cn language";
	}
}
