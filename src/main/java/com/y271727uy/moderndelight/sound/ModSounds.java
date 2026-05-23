package com.y271727uy.moderndelight.sound;

import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ModernDelightMain.MOD_ID);
    
    public static final RegistryObject<SoundEvent> ENTITY_BUTTER_HIT = registerSound("entity_butter_hit");
    public static final RegistryObject<SoundEvent> ENTITY_BUTTER_SHOOT = registerSound("entity_butter_shoot");
    public static final RegistryObject<SoundEvent> ENTITY_CHERRY_BOMB_EXPLOSION = registerSound("entity_cherry_bomb_explosion");
    public static final RegistryObject<SoundEvent> ENTITY_CHERRY_BOMB_SHOOT = registerSound("entity_cherry_bomb_shoot");

    public static final RegistryObject<SoundEvent> BLOCK_FREEZER_RUNNING = registerSound("block_freezer_running");
    public static final RegistryObject<SoundEvent> BLOCK_FREEZER_OPEN = registerSound("block_freezer_open");
    public static final RegistryObject<SoundEvent> BLOCK_FREEZER_CLOSE = registerSound("block_freezer_close");
    public static final RegistryObject<SoundEvent> BLOCK_GLASS_BOWL_WHISKING = registerSound("block_glass_bowl_whisking");
    public static final RegistryObject<SoundEvent> BLOCK_FOOD_FRYING = registerSound("block_food_frying");
    public static final RegistryObject<SoundEvent> BLOCK_GAS_CANISTER_FILLING = registerSound("block_gas_canister_filling");
    public static final RegistryObject<SoundEvent> BLOCK_GAS_COOKING_STOVE_IGNITE = registerSound("block_gas_cooking_stove_ignite");
    public static final RegistryObject<SoundEvent> BLOCK_STERLING_ENGINE = registerSound("block_sterling_engine");
    public static final RegistryObject<SoundEvent> BLOCK_STERLING_ENGINE_START = registerSound("block_sterling_engine_start");
    public static final RegistryObject<SoundEvent> BLOCK_STERLING_ENGINE_STOP = registerSound("block_sterling_engine_stop");
    public static final RegistryObject<SoundEvent> BLOCK_TESLA_COIL = registerSound("block_tesla_coil");
    public static final RegistryObject<SoundEvent> BLOCK_JUICE_EXTRACTOR_WORKING = registerSound("block_juice_extractor_working");
    public static final RegistryObject<SoundEvent> BLOCK_JUICE_EXTRACTOR_SCREAM = registerSound("block_juice_extractor_scream");
    public static final RegistryObject<SoundEvent> BLOCK_JUICE_EXTRACTOR_STOP = registerSound("block_juice_extractor_stop");

    public static final RegistryObject<SoundEvent> ITEM_CROWBAR_HIT = registerSound("item_crowbar_hit");
    public static final RegistryObject<SoundEvent> ITEM_CROWBAR_ATTACK = registerSound("item_crowbar_attack");
    public static final RegistryObject<SoundEvent> ITEM_ELECTRIC_WHISK_WORKING = registerSound("item_electric_whisk_working");
    public static final RegistryObject<SoundEvent> ITEM_STONE_MORTAR_WORKING = registerSound("item_stone_mortar_working");

    public static RegistryObject<SoundEvent> registerSound(String name){
        ResourceLocation id = new ResourceLocation(ModernDelightMain.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
    
    public static void registerModSounds(){
        ModernDelightMain.LOGGER.info("Registering Mod Sounds for " + ModernDelightMain.MOD_ID);
        SOUND_EVENTS.register(ModernDelightMain.EVENT_BUS);
    }
}
