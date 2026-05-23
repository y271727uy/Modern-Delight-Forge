package com.y271727uy.moderndelight.world.feature;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraftforge.registries.DeferredRegister;

import java.util.List;

public class ModConfiguredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = 
            DeferredRegister.create(Registries.CONFIGURED_FEATURE, ModernDelightMain.MOD_ID);
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_BLACK_PEPPER = 
            createKey("wild_black_pepper_cf");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_GARLIC = 
            createKey("wild_garlic_cf");
    
    private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, 
                new ResourceLocation(ModernDelightMain.MOD_ID, name));
    }
    
    // Note: In Forge 1.20.1, configured features are typically registered through JSON files
    // or using DeferredRegister. The bootstrap approach from Fabric is not directly applicable.
    // For now, we keep the keys for reference, but actual registration should be done via:
    // 1. Data generators (if using data gen)
    // 2. JSON files in data/moderndelight/worldgen/configured_feature/
    // 3. Or register directly in ModWorldGeneration
}
