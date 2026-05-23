package com.y271727uy.moderndelight.util.registry_util;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModernDelightMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModLootTableModifies {
    public static final ResourceLocation SQUID_ID = new ResourceLocation("minecraft", "entities/squid");
    public static final ResourceLocation GLOW_SQUID_ID = new ResourceLocation("minecraft", "entities/glow_squid");
    public static final ResourceLocation PODZOL_ID = new ResourceLocation("minecraft", "blocks/podzol");
    public static final ResourceLocation CHERRY_LEAVES_ID = new ResourceLocation("minecraft", "blocks/cherry_leaves");
    public static final ResourceLocation SHIPWRECK_SUPPLY_ID = new ResourceLocation("minecraft", "chests/shipwreck_supply");
    public static final ResourceLocation VILLAGE_DESERT_HOUSE_ID = new ResourceLocation("minecraft", "chests/village/village_desert_house");
    public static final ResourceLocation VILLAGE_SAVANNA_HOUSE_ID = new ResourceLocation("minecraft", "chests/village/village_savanna_house");
    public static final ResourceLocation ABANDONED_MINESHAFT_ID = new ResourceLocation("minecraft", "chests/abandoned_mineshaft");
    public static final ResourceLocation FISH_ID = new ResourceLocation("minecraft", "gameplay/fishing/fish");
    public static final ResourceLocation BLUE_ICE_ID = new ResourceLocation("minecraft", "blocks/blue_ice");
    public static final ResourceLocation ICE_ID = new ResourceLocation("minecraft", "blocks/ice");
    public static final ResourceLocation PACKED_ICE_ID = new ResourceLocation("minecraft", "blocks/packed_ice");
    public static final ResourceLocation VILLAGE_BUTCHER_ID = new ResourceLocation("minecraft", "chests/village/village_butcher");
    public static final ResourceLocation VILLAGE_PLAINS_HOUSE_ID = new ResourceLocation("minecraft", "chests/village/village_plains_house");
    public static final ResourceLocation PILLAGER_OUTPOST_ID = new ResourceLocation("minecraft", "chests/pillager_outpost");

    public static void modifyLootTables() {
        ModernDelightMain.LOGGER.info("Registering loot table modifiers for " + ModernDelightMain.MOD_ID);
    }

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        modifyLootTable(event.getName(), event.getTable());
    }

    private static void modifyLootTable(ResourceLocation id, LootTable table) {
        if (SQUID_ID.equals(id)) {
            addPool(table, 1.0F, ModItems.SQUID.get(), UniformGenerator.between(1.0F, 1.0F));
        }
        if (SQUID_ID.equals(id)) {
            addPool(table, 0.20F, ModItems.CUTTLEBONE.get(), UniformGenerator.between(1.0F, 1.0F));
        }
        if (GLOW_SQUID_ID.equals(id)) {
            addPool(table, 1.0F, ModItems.GLOW_SQUID.get(), UniformGenerator.between(1.0F, 1.0F));
        }
        if (GLOW_SQUID_ID.equals(id)) {
            addPool(table, 0.20F, ModItems.GLOW_CUTTLEBONE.get(), UniformGenerator.between(1.0F, 1.0F));
        }
        if (PODZOL_ID.equals(id)) {
            addPool(table, 0.10F, ModItems.BLACK_TRUFFLE.get(), UniformGenerator.between(1.0F, 1.0F));
        }
        if (PODZOL_ID.equals(id)) {
            addPool(table, 0.03F, ModItems.WHITE_TRUFFLE.get(), UniformGenerator.between(1.0F, 1.0F));
        }
        if (ICE_ID.equals(id)) {
            addPool(table, 0.30F, ModItems.ICE_BRICK.get(), UniformGenerator.between(1.0F, 2.0F));
        }
        if (PACKED_ICE_ID.equals(id)) {
            addPool(table, 0.50F, ModItems.ICE_BRICK.get(), UniformGenerator.between(2.0F, 3.0F));
        }
        if (BLUE_ICE_ID.equals(id)) {
            addPool(table, 0.70F, ModItems.ICE_BRICK.get(), UniformGenerator.between(6.0F, 10.0F));
        }
        if (CHERRY_LEAVES_ID.equals(id)) {
            addPool(table, 0.10F, ModItems.CHERRY.get(), UniformGenerator.between(1.0F, 1.0F));
        }
        if (SHIPWRECK_SUPPLY_ID.equals(id)) {
            addChoicePool(table, 1.0F, 2, ModItems.BLACK_PEPPER_CORN.get(), ModItems.BUTTER.get());
        }
        if (VILLAGE_SAVANNA_HOUSE_ID.equals(id)) {
            addChoicePool(table, 0.80F, 1, ModItems.BLACK_PEPPER_CORN.get(), ModItems.GARLIC.get(), ModItems.BUTTER.get());
        }
        if (VILLAGE_DESERT_HOUSE_ID.equals(id)) {
            addChoicePool(table, 0.80F, 1, ModItems.BLACK_PEPPER_CORN.get(), ModItems.BUTTER.get());
        }
        if (ABANDONED_MINESHAFT_ID.equals(id)) {
            addPool(table, 0.40F, ModItems.IRON_WHISK.get(), UniformGenerator.between(1.0F, 1.0F));
        }
        if (FISH_ID.equals(id)) {
            addPool(table, 0.40F, ModItems.PRAWN.get(), UniformGenerator.between(1.0F, 1.0F));
        }
        if (VILLAGE_BUTCHER_ID.equals(id)) {
            addChoicePool(table, 0.80F, 1, ModItems.GARLIC.get(), ModItems.BUTTER.get());
        }
        if (VILLAGE_PLAINS_HOUSE_ID.equals(id)) {
            addChoicePool(table, 0.70F, 1, ModItems.GARLIC.get(), ModItems.GARLIC_PETAL.get(), ModItems.BUTTER.get());
        }
        if (PILLAGER_OUTPOST_ID.equals(id)) {
            addChoicePool(table, 0.50F, 2, ModItems.REDSTONE_COMPONENT.get(), ModItems.GARLIC_PETAL.get(), ModItems.SILICON_INGOT.get());
        }
    }

    private static void addPool(LootTable table, float chance, net.minecraft.world.level.ItemLike item, UniformGenerator count) {
        table.addPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .add(LootItem.lootTableItem(item))
                .apply(SetItemCountFunction.setCount(count))
                .build());
    }

    private static void addChoicePool(LootTable table, float chance, float rolls, net.minecraft.world.level.ItemLike... items) {
        LootPool.Builder builder = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(rolls))
                .when(LootItemRandomChanceCondition.randomChance(chance));
        for (net.minecraft.world.level.ItemLike item : items) {
            builder.add(LootItem.lootTableItem(item));
        }
        table.addPool(builder.build());
    }
}
