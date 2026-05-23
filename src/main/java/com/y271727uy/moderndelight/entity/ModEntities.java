package com.y271727uy.moderndelight.entity;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.entity.custom.ButterEntity;
import com.y271727uy.moderndelight.entity.custom.CherryBombEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ModernDelightMain.MOD_ID);

    public static final RegistryObject<EntityType<ButterEntity>> BUTTER = ENTITY_TYPES.register("butter",
            () -> EntityType.Builder.<ButterEntity>of(ButterEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("butter"));
    public static final RegistryObject<EntityType<CherryBombEntity>> CHERRY_BOMB = ENTITY_TYPES.register("cherry_bomb",
            () -> EntityType.Builder.<CherryBombEntity>of(CherryBombEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("cherry_bomb"));

    public static void registerModEntities(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
        ModernDelightMain.LOGGER.info("Registering Mod Entities for " + ModernDelightMain.MOD_ID);
    }
}
