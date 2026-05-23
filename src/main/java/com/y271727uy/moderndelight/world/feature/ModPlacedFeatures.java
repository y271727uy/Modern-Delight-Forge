package com.y271727uy.moderndelight.world.feature;

import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.DeferredRegister;

public class ModPlacedFeatures {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = 
            DeferredRegister.create(Registries.PLACED_FEATURE, ModernDelightMain.MOD_ID);
    
    public static final ResourceKey<PlacedFeature> WILD_BLACK_PEPPER_PLACED = 
            createKey("wild_black_pepper_p");
    public static final ResourceKey<PlacedFeature> WILD_GARLIC_PLACED = 
            createKey("wild_garlic_p");
    
    private static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, 
                new ResourceLocation(ModernDelightMain.MOD_ID, name));
    }
    
    // Note: In Forge 1.20.1, placed features are typically registered through JSON files
    // or using DeferredRegister. The bootstrap approach from Fabric is not directly applicable.
    // For now, we keep the keys for reference in WildCropGeneration, but actual registration 
    // should be done via:
    // 1. Data generators (if using data gen)
    // 2. JSON files in data/moderndelight/worldgen/placed_feature/
    // 3. Or register directly using BiomeLoadingEvent (see WildCropGeneration)
}
