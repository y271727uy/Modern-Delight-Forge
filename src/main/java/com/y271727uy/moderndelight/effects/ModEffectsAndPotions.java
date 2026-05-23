package com.y271727uy.moderndelight.effects;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.effects.custom.StickyEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffectsAndPotions {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ModernDelightMain.MOD_ID);
    
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, ModernDelightMain.MOD_ID);
    
    public static final RegistryObject<MobEffect> STICKY = EFFECTS.register("sticky", StickyEffect::new);
    
    public static final RegistryObject<Potion> STICKY_POTION = POTIONS.register("sticky_potion",
            () -> new Potion(new MobEffectInstance(STICKY.get(), 22 * 20, 0)));
    
    public static final RegistryObject<Potion> STICKY_LONG_POTION = POTIONS.register("sticky_long_potion",
            () -> new Potion(new MobEffectInstance(STICKY.get(), 42 * 20, 0)));
    
    public static final RegistryObject<Potion> SQUID_POWER_POTION = POTIONS.register("squid_power_potion",
            () -> new Potion(
                    new MobEffectInstance(MobEffects.REGENERATION, 30 * 20, 0),
                    new MobEffectInstance(MobEffects.WATER_BREATHING, 120 * 20, 0)
            ));
    
    public static final RegistryObject<Potion> SQUID_POWER_LONG_POTION = POTIONS.register("squid_power_long_potion",
            () -> new Potion(
                    new MobEffectInstance(MobEffects.REGENERATION, 60 * 20, 0),
                    new MobEffectInstance(MobEffects.WATER_BREATHING, 400 * 20, 0)
            ));
    
    public static final RegistryObject<Potion> SQUID_POWER_STRONG_POTION = POTIONS.register("squid_power_strong_potion",
            () -> new Potion(
                    new MobEffectInstance(MobEffects.REGENERATION, 4 * 20, 3),
                    new MobEffectInstance(MobEffects.WATER_BREATHING, 90 * 20, 0)
            ));
    
    public static final RegistryObject<Potion> GLOW_SQUID_POWER_POTION = POTIONS.register("glow_squid_power_potion",
            () -> new Potion(
                    new MobEffectInstance(MobEffects.REGENERATION, 22 * 20, 1),
                    new MobEffectInstance(MobEffects.WATER_BREATHING, 180 * 20, 0),
                    new MobEffectInstance(MobEffects.GLOWING, 180 * 20, 0)
            ));
    
    public static final RegistryObject<Potion> GLOW_SQUID_POWER_LONG_POTION = POTIONS.register("glow_squid_power_long_potion",
            () -> new Potion(
                    new MobEffectInstance(MobEffects.REGENERATION, 60 * 20, 0),
                    new MobEffectInstance(MobEffects.WATER_BREATHING, 500 * 20, 0),
                    new MobEffectInstance(MobEffects.GLOWING, 8 * 60 * 20, 0)
            ));
    
    public static final RegistryObject<Potion> GLOW_SQUID_POWER_STRONG_POTION = POTIONS.register("glow_squid_power_strong_potion",
            () -> new Potion(
                    new MobEffectInstance(MobEffects.REGENERATION, 8 * 20, 3),
                    new MobEffectInstance(MobEffects.WATER_BREATHING, 120 * 20, 0),
                    new MobEffectInstance(MobEffects.GLOWING, 10 * 60 * 20, 0)
            ));
    
    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
        POTIONS.register(eventBus);
        ModernDelightMain.LOGGER.info("Registering Mod Effects and Potions for " + ModernDelightMain.MOD_ID);
    }
}
