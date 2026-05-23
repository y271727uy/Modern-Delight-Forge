package com.y271727uy.moderndelight.util.registry_util;

import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class ModDamageTypes {
    public static final net.minecraft.resources.ResourceKey<DamageType> ELECTROSHOCK = net.minecraft.resources.ResourceKey.create(
            Registries.DAMAGE_TYPE, new ResourceLocation(ModernDelightMain.MOD_ID, "electroshock"));
    public static final net.minecraft.resources.ResourceKey<DamageType> TURNED_TO_ASHES = net.minecraft.resources.ResourceKey.create(
            Registries.DAMAGE_TYPE, new ResourceLocation(ModernDelightMain.MOD_ID, "turned_to_ashes"));
    public static final net.minecraft.resources.ResourceKey<DamageType> SCALDED = net.minecraft.resources.ResourceKey.create(
            Registries.DAMAGE_TYPE, new ResourceLocation(ModernDelightMain.MOD_ID, "scalded"));
    public static DamageSource of(Level world, net.minecraft.resources.ResourceKey<DamageType> key) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
    }
}
