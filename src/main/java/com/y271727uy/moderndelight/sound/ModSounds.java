package com.y271727uy.moderndelight.sound;

import com.y271727uy.moderndelight.ModernDelightMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ModernDelightMain.MOD_ID);
    
    public static final RegistryObject<SoundEvent> ENTITY_BUTTER_HIT = registerVariableRangeSound("entity_butter_hit");
    public static final RegistryObject<SoundEvent> ENTITY_BUTTER_SHOOT = registerVariableRangeSound("entity_butter_shoot");
    public static final RegistryObject<SoundEvent> ENTITY_CHERRY_BOMB_EXPLOSION = registerVariableRangeSound("entity_cherry_bomb_explosion");
    public static final RegistryObject<SoundEvent> ENTITY_CHERRY_BOMB_SHOOT = registerVariableRangeSound("entity_cherry_bomb_shoot");

    public static final RegistryObject<SoundEvent> BLOCK_FREEZER_RUNNING = registerFixedRangeSound("block_freezer_running");
    public static final RegistryObject<SoundEvent> BLOCK_FREEZER_OPEN = registerFixedRangeSound("block_freezer_open");
    public static final RegistryObject<SoundEvent> BLOCK_FREEZER_CLOSE = registerFixedRangeSound("block_freezer_close");
    public static final RegistryObject<SoundEvent> BLOCK_GLASS_BOWL_WHISKING = registerFixedRangeSound("block_glass_bowl_whisking");
    public static final RegistryObject<SoundEvent> BLOCK_FOOD_FRYING = registerFixedRangeSound("block_food_frying");
    public static final RegistryObject<SoundEvent> BLOCK_GAS_CANISTER_FILLING = registerFixedRangeSound("block_gas_canister_filling");
    public static final RegistryObject<SoundEvent> BLOCK_GAS_COOKING_STOVE_IGNITE = registerFixedRangeSound("block_gas_cooking_stove_ignite");
    public static final RegistryObject<SoundEvent> BLOCK_STERLING_ENGINE = registerFixedRangeSound("block_sterling_engine");
    public static final RegistryObject<SoundEvent> BLOCK_STERLING_ENGINE_START = registerFixedRangeSound("block_sterling_engine_start");
    public static final RegistryObject<SoundEvent> BLOCK_STERLING_ENGINE_STOP = registerFixedRangeSound("block_sterling_engine_stop");
    public static final RegistryObject<SoundEvent> BLOCK_TESLA_COIL = registerFixedRangeSound("block_tesla_coil");
    public static final RegistryObject<SoundEvent> BLOCK_JUICE_EXTRACTOR_WORKING = registerFixedRangeSound("block_juice_extractor_working");
    public static final RegistryObject<SoundEvent> BLOCK_JUICE_EXTRACTOR_SCREAM = registerFixedRangeSound("block_juice_extractor_scream");
    public static final RegistryObject<SoundEvent> BLOCK_JUICE_EXTRACTOR_STOP = registerFixedRangeSound("block_juice_extractor_stop");

    public static final RegistryObject<SoundEvent> ITEM_CROWBAR_HIT = registerVariableRangeSound("item_crowbar_hit");
    public static final RegistryObject<SoundEvent> ITEM_CROWBAR_ATTACK = registerVariableRangeSound("item_crowbar_attack");
    public static final RegistryObject<SoundEvent> ITEM_ELECTRIC_WHISK_WORKING = registerFixedRangeSound("item_electric_whisk_working");
    public static final RegistryObject<SoundEvent> ITEM_STONE_MORTAR_WORKING = registerVariableRangeSound("item_stone_mortar_working");

    public static RegistryObject<SoundEvent> registerVariableRangeSound(String name){
        ResourceLocation id = new ResourceLocation(ModernDelightMain.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static RegistryObject<SoundEvent> registerFixedRangeSound(String name){
        ResourceLocation id = new ResourceLocation(ModernDelightMain.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createFixedRangeEvent(id, 16.0F));
    }
    
    public static void registerModSounds(){
        ModernDelightMain.LOGGER.info("Registering Mod Sounds for " + ModernDelightMain.MOD_ID);
        SOUND_EVENTS.register(ModernDelightMain.EVENT_BUS);
    }
}
