package com.y271727uy.moderndelight.world.gen;

import com.y271727uy.moderndelight.ModernDelightMain;

public class WildCropGeneration {
    public static void generate(){
        // Note: In Forge 1.20.1, biome modification is done through JSON files or BiomeModifiers
        // The BiomeLoadingEvent approach from earlier Forge versions has been removed.
        // 
        // For world generation, you should use one of these methods:
        // 1. Create JSON files in data/moderndelight/worldgen/placed_feature/
        // 2. Use BiomeModifiers (data/moderndelight/forge/biome_modifier/)
        // 3. Register features using DeferredRegister
        //
        // Example JSON approach:
        // - Create configured_feature JSON in data/moderndelight/worldgen/configured_feature/
        // - Create placed_feature JSON in data/moderndelight/worldgen/placed_feature/
        // - Create biome_modifier JSON in data/moderndelight/forge/biome_modifier/
        
        ModernDelightMain.LOGGER.info("World generation setup for {} (use JSON files or BiomeModifiers)", ModernDelightMain.MOD_ID);
    }
}
