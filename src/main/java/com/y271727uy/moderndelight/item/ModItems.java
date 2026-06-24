package com.y271727uy.moderndelight.item;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.fluid.ModFluid;
import com.y271727uy.moderndelight.item.food.*;
import com.y271727uy.moderndelight.item.food.instant_noodles.CookedPortablePotItem;
import com.y271727uy.moderndelight.item.food.instant_noodles.PackagedInstantNoodlesItem;
import com.y271727uy.moderndelight.item.food.instant_noodles.PortablePotItem;
import com.y271727uy.moderndelight.item.tools.*;
import com.y271727uy.moderndelight.util.TextUtil;
import com.y271727uy.moderndelight.util.enums.CreamFlavor;
import com.y271727uy.moderndelight.util.enums.ModToolMaterials;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModernDelightMain.MOD_ID);
    
    public static final RegistryObject<Item> EGG_TART = ITEMS.register("egg_tart", () -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(5).saturationMod(0.3F).build())));
    public static final RegistryObject<Item> MASHED_POTATO = ITEMS.register("mashed_potato", () -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).build()).craftRemainder(Items.BOWL)));
    public static final RegistryObject<Item> IRON_WHISK = registerItem("iron_whisk",
            () -> new WhiskItem(1.5F, -3.3F, Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> APPLE_PETAL = registerItem("apple_petal", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(1).saturationMod(0.1F).build())));
    public static final RegistryObject<Item> APPLE_CREAM = registerItem("apple_cream", () -> new CreamItem(CreamFlavor.APPLE,new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).build())
            .craftRemainder(Items.BOWL)
            .stacksTo(16)));
    public static final RegistryObject<Item> BUTTER = registerItem("butter", () -> new ButterItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> BUTTER_BREAD_SLICE = registerItem("butter_bread_slice", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.8F).build())));
    public static final RegistryObject<Item> BREAD_SLICE = registerItem("bread_slice", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1F).build())));
    public static final RegistryObject<Item> CHEESE = registerItem("cheese", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).build())));
    public static final RegistryObject<Item> CHERRY = registerItem("cherry", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1F).build())));
    public static final RegistryObject<Item> CHERRY_CREAM = registerItem("cherry_cream", () -> new CreamItem(CreamFlavor.CHERRY,new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).build())
            .craftRemainder(Items.BOWL)
            .stacksTo(16)));
    public static final RegistryObject<Item> CHERRY_MOUSSE = registerItem("cherry_mousse", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(5).saturationMod(0.8F).build())));
    public static final RegistryObject<Item> CHOCOLATE_CREAM = registerItem("chocolate_cream", () -> new CreamItem(CreamFlavor.CHOCOLATE,new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(5).saturationMod(0.8F).build())
            .craftRemainder(Items.BOWL)
            .stacksTo(16)));
    public static final RegistryObject<Item> CHOCOLATE_MOUSSE = registerItem("chocolate_mousse", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8F).build())));
    public static final RegistryObject<Item> PRAWN = registerItem("prawn", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2F).build())));
    public static final RegistryObject<Item> CREAM = registerItem("cream", () -> new CreamItem(CreamFlavor.PLAIN,new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).build())
            .stacksTo(16)));

    public static final RegistryObject<Item> CREAM_MOUSSE = registerItem("cream_mousse", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.6F).build())));
    public static final RegistryObject<Item> CRYSTAL_DUMPLING = ITEMS.register("crystal_dumpling", () ->
            new PackagedItem(new ResourceLocation(ModernDelightMain.MOD_ID, "wooden_plate"),new Item.Properties().stacksTo(16).craftRemainder(ModBlocks.WOODEN_PLATE.get().asItem())
            .food(new FoodProperties.Builder().nutrition(9).saturationMod(0.6F).build())));
    public static final RegistryObject<Item> GLOW_SQUID = registerItem("glow_squid", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2F)
                    .effect(() -> new MobEffectInstance(MobEffects.GLOWING, 400, 0), 1.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.HUNGER,200, 0), 0.4F).build())));
    public static final RegistryObject<Item> GRILLED_STARCH_SAUSAGE = registerItem("grilled_starch_sausage", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(9).saturationMod(0.8F).build())));
    public static final RegistryObject<Item> GRILLED_SAUSAGE = registerItem("grilled_sausage", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(6).saturationMod(1.0F).build())));
    public static final RegistryObject<Item> LITTLE_OCTOPUS_SAUSAGE = registerItem("little_octopus_sausage", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(10).saturationMod(0.5F).build())));
    public static final RegistryObject<Item> GOLDEN_APPLE_CREAM = registerItem("golden_apple_cream", () -> new CreamItem(CreamFlavor.GOLDEN_APPLE,new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8F)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 1), 1.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0), 1.0F)
                    .build())
            .craftRemainder(Items.BOWL)
            .stacksTo(16)));
    public static final RegistryObject<Item> GOLDEN_APPLE_MOUSSE = registerItem("golden_apple_mousse", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(10).saturationMod(0.8F)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 2), 1.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 4800, 1), 1.0F)
                    .build())
            .stacksTo(16)));
    public static final RegistryObject<Item> MATCHA_CREAM = registerItem("matcha_cream", () -> new CreamItem(CreamFlavor.MATCHA,new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.5F)
                    .effect(() -> new MobEffectInstance(MobEffects.LUCK, 600, 0), 0.7F).build())
            .craftRemainder(Items.BOWL)
            .stacksTo(16)));
    public static final RegistryObject<Item> MATCHA_MOUSSE = registerItem("matcha_mousse", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(5).saturationMod(0.8F)
                    .effect(() -> new MobEffectInstance(MobEffects.LUCK, 1200, 0), 0.7F)
                    .build())));
    public static final RegistryObject<Item> MIXED_DOUGH = registerItem("mixed_dough", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(1).saturationMod(0.1F)
                    .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.4F).build())));
    public static final RegistryObject<Item> POTATO_STARCH = registerItem("potato_starch", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(1).saturationMod(0.1F).build())));
    public static final RegistryObject<Item> PUMPKIN_CREAM = registerItem("pumpkin_cream", () -> new CreamItem(CreamFlavor.PUMPKIN,new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.4F).build())
            .craftRemainder(Items.BOWL)
            .stacksTo(16)));
    public static final RegistryObject<Item> PUMPKIN_MOUSSE = registerItem("pumpkin_mousse", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(5).saturationMod(0.7F).build())));
    public static final RegistryObject<Item> ROASTED_GLOW_SQUID = registerItem("roasted_glow_squid", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8F)
                    .effect(() -> new MobEffectInstance(MobEffects.GLOWING, 1200, 0), 1.0F)
                    .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION,1200, 0), 1.0F).build())));
    public static final RegistryObject<Item> ROASTED_SQUID = registerItem("roasted_squid", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).build())));
    public static final RegistryObject<Item> SAUCE_MASHED_POTATO = registerItem("sauce_mashed_potato", () -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(8).saturationMod(0.6F).build())
            .craftRemainder(Items.BOWL)
            .stacksTo(16)));
    public static final RegistryObject<Item> SQUID = registerItem("squid", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2F).build())));
    public static final RegistryObject<Item> STARCH_SAUSAGE = registerItem("starch_sausage", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).build())));
    public static final RegistryObject<Item> SAUSAGE = registerItem("sausage", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.4F).build())));
    public static final RegistryObject<Item> BLACK_TRUFFLE = registerItem("black_truffle", () -> new SeasoningItem(new Item.Properties(),
            new MobEffectInstance(MobEffects.ABSORPTION,15 * 20,0)){
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context){
            if(Screen.hasShiftDown()){
                tooltip.add(TextUtil.getShiftText(true));
                tooltip.add(Component.literal(" "));
                tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.TRUFFLE)));
            }else {
                tooltip.add(TextUtil.getShiftText(false));
            }
            super.appendHoverText(stack, level, tooltip, context);
        }
    });
    public static final RegistryObject<Item> WHITE_TRUFFLE = registerItem("white_truffle", () -> new SeasoningItem(new Item.Properties(),
            new MobEffectInstance(MobEffects.ABSORPTION,8 * 20,1)){
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context){
            if(Screen.hasShiftDown()){
                tooltip.add(TextUtil.getShiftText(true));
                tooltip.add(Component.literal(" "));
                tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.TRUFFLE)));
            }else {
                tooltip.add(TextUtil.getShiftText(false));
            }
            super.appendHoverText(stack, level, tooltip, context);
        }
    });
    public static final RegistryObject<Item> CUTTLEBONE = registerItem("cuttlebone", () -> new SeasoningItem(new Item.Properties(),
            new MobEffectInstance(MobEffects.REGENERATION,8 * 20,0),
            new MobEffectInstance(MobEffects.WATER_BREATHING, 30 * 20,0)){
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context){
            if(Screen.hasShiftDown()){
                tooltip.add(TextUtil.getShiftText(true));
                tooltip.add(Component.literal(" "));
                tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.CUTTLEBONE)));
            }else {
                tooltip.add(TextUtil.getShiftText(false));
            }
            super.appendHoverText(stack, level, tooltip, context);
        }
    });
    public static final RegistryObject<Item> GLOW_CUTTLEBONE = registerItem("glow_cuttlebone", () -> new SeasoningItem(new Item.Properties(),
            new MobEffectInstance(MobEffects.REGENERATION,8 * 20,1),
            new MobEffectInstance(MobEffects.WATER_BREATHING, 45 * 20,0),
            new MobEffectInstance(MobEffects.GLOWING,180 * 20,0)){
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context){
            if(Screen.hasShiftDown()){
                tooltip.add(TextUtil.getShiftText(true));
                tooltip.add(Component.literal(" "));
                tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.CUTTLEBONE)));
            }else {
                tooltip.add(TextUtil.getShiftText(false));
            }
            super.appendHoverText(stack, level, tooltip, context);
        }
    });
    public static final RegistryObject<Item> COPPER_KNIFE = registerItem("copper_knife",
            () -> new KnifeItem(ModToolMaterials.COPPER,0, -2.0f, new Item.Properties()));
    public static final RegistryObject<Item> AMETHYST_KNIFE = registerItem("amethyst_knife",
            () -> new KnifeItem(ModToolMaterials.AMETHYST,0.5f, -2.0f, new Item.Properties()));
    public static final RegistryObject<Item> BLACK_PEPPER_CORN = ITEMS.register("black_pepper_corn", () -> new BlockItem(ModBlocks.BLACK_PEPPER_CROP.get(),
            new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(1).build())));
    public static final RegistryObject<Item> BLACK_PEPPER_DUST = registerItem("black_pepper_dust", () -> new SeasoningItem(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(1).build()),
            new MobEffectInstance(MobEffects.FIRE_RESISTANCE,10 * 20,0)));
    public static final RegistryObject<Item> CREAM_BUCKET = registerItem("cream_bucket",
            () -> new BucketItem(ModFluid.STILL_CREAM::get,new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> CHERRY_BOMB = registerItem("cherry_bomb",() -> new CherryBombItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> APPLE_PUDDING = registerItem("apple_pudding", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4F).build())));
    public static final RegistryObject<Item> BRAISED_SHRIMP_BALL = registerItem("braised_shrimp_ball", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(9).saturationMod(0.8F).build())));
    public static final RegistryObject<Item> MATCHA_PUDDING = registerItem("matcha_pudding", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F)
                    .effect(() -> new MobEffectInstance(MobEffects.LUCK,600,0),1.0f).build())));
    public static final RegistryObject<Item> SUNFLOWER_SEED = registerItem("sunflower_seed", () -> new SunFlowerSeedItem());
    public static final RegistryObject<Item> TRUFFLE_EGG_TART = ITEMS.register("truffle_egg_tart", () -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).
            effect(() -> new MobEffectInstance(MobEffects.ABSORPTION,25*20,0),1.0f).build())));
    public static final RegistryObject<Item> ICE_BRICK = registerItem("ice_brick", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PUDDING_WIP_1 = registerItem("pudding_wip_1", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).build())));
    public static final RegistryObject<Item> PUDDING_WIP_2 = registerItem("pudding_wip_2", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(3).saturationMod(0.3F).build())));
    public static final RegistryObject<Item> MOUSSE_WIP = registerItem("mousse_wip", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(3).saturationMod(0.3F).build())));
    public static final RegistryObject<Item> KNEADING_STICK = registerItem("kneading_stick", () -> new KneadingStickItem(Tiers.WOOD,2.5f,-2.5f,
            new Item.Properties()));
    public static final RegistryObject<Item> WHEAT_FLOUR = registerItem("wheat_flour", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(1).saturationMod(0.2F)
                    .effect(() -> new MobEffectInstance(MobEffects.HUNGER,200,0),0.5f).build())));
    public static final RegistryObject<Item> SUNFLOWER_SEED_PEEL = registerItem("sunflower_seed_peel", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SECTIONED_SAUSAGE = registerItem("sectioned_sausage", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.5F).build())));
    public static final RegistryObject<Item> SUNFLOWER_SEED_PULP = registerItem("sunflower_seed_pulp", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(1).saturationMod(0.1F).build())));
    public static final RegistryObject<Item> CROWBAR = registerItem("crowbar", () -> new CrowbarItem(Tiers.IRON,7.5f,-3.6f));
    public static final RegistryObject<Item> SPATULA = registerItem("spatula", () -> new SpatulaItem(Tiers.IRON,2.5f,-2.8f));
    public static final RegistryObject<Item> ROASTED_SUNFLOWER_SEED = registerItem("roasted_sunflower_seed", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2f).build())));
    public static final RegistryObject<Item> FILTER = registerItem("filter", () -> new Item(new Item.Properties()
            .durability(ModToolMaterials.STRING.getUses())){
        @Override
        public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
            return ModToolMaterials.STRING.getRepairIngredient().test(repairCandidate)
                    || super.isValidRepairItem(stack, repairCandidate);
        }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
            if(Screen.hasShiftDown()){
                tooltip.add(TextUtil.getShiftText(true));
                tooltip.add(Component.literal(" "));
                tooltip.addAll(TextUtil.generateToolTip(Component.translatable(TextUtil.FILTER)));
            } else {
                tooltip.add(TextUtil.getShiftText(false));
            }
            super.appendHoverText(stack, world, tooltip, context);
        }
    });
    public static final RegistryObject<Item> OIL_IMPURITY = registerItem("oil_impurity",() -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VEGETABLE_OIL_BOTTLE = registerItem("vegetable_oil_bottle",() -> new Item(new Item.Properties()
            .stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));
    public static final RegistryObject<Item> VEGETABLE_OIL_BUCKET = registerItem("vegetable_oil_bucket",() -> new BucketItem(ModFluid.STILL_VEGETABLE_OIL::get,new Item.Properties()
            .stacksTo(1).craftRemainder(Items.BUCKET)));
    public static final RegistryObject<Item> EMPTY_CAKE = registerItem("empty_cake", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.1f).build())
    ));
    public static final RegistryObject<Item> BLUE_ORCHID_FLOWER_CAKE = registerItem("blue_orchid_flower_cake", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2f)
                    .effect(() -> new MobEffectInstance(MobEffects.SATURATION,10,0),1.0f).build())
    ));
    public static final RegistryObject<Item> CHERRY_CAKE = registerItem("cherry_cake", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2f)
                    .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST,200,0),1.0f).build())
    ));
    public static final RegistryObject<Item> LILAC_CAKE = registerItem("lilac_cake", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2f)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,200,0),1.0f).build())
    ));
    public static final RegistryObject<Item> ORANGE_TULIP_CAKE = registerItem("orange_tulip_cake", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2f)
                    .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE,200,0),1.0f).build())
    ));
    public static final RegistryObject<Item> OXEYE_DAISY_CAKE = registerItem("oxeye_daisy_cake", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2f)
                    .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION,200,0),1.0f).build())
    ));
    public static final RegistryObject<Item> PINK_TULIP_CAKE = registerItem("pink_tulip_cake", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2f)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,100,1),1.0f).build())
    ));
    public static final RegistryObject<Item> ROSE_CAKE = registerItem("rose_cake", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2f)
                    .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST,100,1),1.0f).build())
    ));
    public static final RegistryObject<Item> SUNFLOWER_CAKE = registerItem("sunflower_cake", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2f)
                    .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE,300,0),1.0f).build())
    ));
    public static final RegistryObject<Item> WHITE_TULIP_CAKE = registerItem("white_tulip_cake", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2f)
                    .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS,200,0),1.0f).build())
    ));
    public static final RegistryObject<Item> WITHER_ROSE_CAKE = registerItem("wither_rose_cake", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(0.5f)
                    .effect(() -> new MobEffectInstance(MobEffects.WITHER,100,0),1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,300,1),1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,400,1),1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE,600,0),1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS,40,0),1.0f)
                    .build())
    ));
    public static final RegistryObject<Item> RAW_ONION_RING = registerItem("raw_onion_ring", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.1f).build())
    ));
    public static final RegistryObject<Item> ONION_RING = registerItem("onion_ring", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.2f).build())
    ));
    public static final RegistryObject<Item> FRIED_COD = registerItem("fried_cod", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(0.3f).build())
    ));
    public static final RegistryObject<Item> FRIED_SALMON = registerItem("fried_salmon", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(11).saturationMod(0.4f).build())
    ));
    public static final RegistryObject<Item> FRIED_MILK_WIP = registerItem("fried_milk_wip", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).build())
    ));
    public static final RegistryObject<Item> FRIED_MILK = registerItem("fried_milk", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.3f).build())
    ));
    public static final RegistryObject<Item> FRIED_APPLE = registerItem("fried_apple", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3f).build())
    ));
    public static final RegistryObject<Item> RAW_POTATO_CHIP = registerItem("raw_potato_chip", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).build())
    ));
    public static final RegistryObject<Item> POTATO_CHIP = registerItem("potato_chip", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).build())
    ));
    public static final RegistryObject<Item> CHEESE_BALL = registerItem("cheese_ball", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2f).build())
    ));
    public static final RegistryObject<Item> FRIED_DOUGH_STICK = registerItem("fried_dough_stick", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).build())
    ));
    public static final RegistryObject<Item> RAW_CHICKEN_FILLET = registerItem("raw_chicken_fillet",() -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.4f).build())
    ));
    public static final RegistryObject<Item> CHICKEN_FILLET = registerItem("chicken_fillet",() -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.4f).build())
    ));
    public static final RegistryObject<Item> ANCIENT_SCRAP = registerItem("ancient_scrap",() -> new Item(
            new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> SILICON_INGOT = registerItem("silicon_ingot",() -> new Item(
            new Item.Properties())
    );
    public static final RegistryObject<Item> SILICON_COMPONENT = registerItem("silicon_component",() -> new Item(
            new Item.Properties())
    );
    public static final RegistryObject<Item> REDSTONE_COMPONENT = registerItem("redstone_component",() -> new Item(
            new Item.Properties())
    );
    public static final RegistryObject<Item> DIAMOND_COMPONENT = registerItem("diamond_component",() -> new Item(
            new Item.Properties())
    );
    public static final RegistryObject<Item> RAW_ICE_CREAM_CONE = registerItem("raw_ice_cream_cone",() -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).build())
    ));
    public static final RegistryObject<Item> ICE_CREAM_CONE = registerItem("ice_cream_cone",() -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.1f).build())
    ));
    public static final RegistryObject<Item> ICE_CREAM = registerItem("ice_cream",() -> new IceCreamItem());
    public static final RegistryObject<Item> STEAMED_BUN = registerItem("steamed_bun",() -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationMod(0.1f).build())
    ));
    public static final RegistryObject<Item> DEEP_FRIED_BUN = registerItem("deep_fried_bun",() -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.2f).build())
    ));
    public static final RegistryObject<Item> EGG_BOWL = registerItem("egg_bowl",() -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),
            new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).build())
    ));
    public static final RegistryObject<Item> CHERRY_EGG = registerItem("cherry_egg",() -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),
            new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(new FoodProperties.Builder().nutrition(4).saturationMod(0.1f).build())
    ));
    public static final RegistryObject<Item> FISH_EGG = registerItem("fish_egg",() -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),
            new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(new FoodProperties.Builder().nutrition(5).saturationMod(0.1f).build())
    ));
    public static final RegistryObject<Item> STEAMED_EGG = registerItem("steamed_egg",() -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),
            new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(new FoodProperties.Builder().nutrition(5).saturationMod(0.3f).build())
    ));
    public static final RegistryObject<Item> STEAMED_CHERRY_EGG = registerItem("steamed_cherry_egg",() -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),
            new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(new FoodProperties.Builder().nutrition(7).saturationMod(0.3f).build())
    ));
    public static final RegistryObject<Item> STEAMED_FISH_EGG = registerItem("steamed_fish_egg",() -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),
            new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(new FoodProperties.Builder().nutrition(10).saturationMod(0.4f).build())
    ));
    public static final RegistryObject<Item> BEEF_TOMATO_CUP = ITEMS.register("beef_tomato_cup", () -> new PackagedItem(new ResourceLocation(ModernDelightMain.MOD_ID, "wooden_plate"),
            new Item.Properties().stacksTo(16).craftRemainder(ModBlocks.WOODEN_PLATE.get().asItem())
                    .food(new FoodProperties.Builder().nutrition(13).saturationMod(0.6f).build())
    ));
    public static final RegistryObject<Item> BUTTERFLY_CRISP = registerItem("butterfly_crisp",() -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.1f).build())
    ));
    public static final RegistryObject<Item> JAR = registerItem("jar",() -> new Item(
            new Item.Properties().stacksTo(16))
    );
    public static final RegistryObject<Item> CARAMEL = ITEMS.register("caramel", () -> new PackagedItem(new ResourceLocation(ModernDelightMain.MOD_ID, "jar"),
            new Item.Properties().stacksTo(16).craftRemainder(ModItems.JAR.get()).food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2f).build()))
    );
    public static final RegistryObject<Item> CARAMEL_PUDDING = ITEMS.register("caramel_pudding", () -> new BlockItem(ModBlocks.CARAMEL_PUDDING.get(),new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(6).saturationMod(0.4F).build())));
    public static final RegistryObject<Item> CHEESE_BAKED_POTATO = registerItem("cheese_baked_potato", () -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(8).saturationMod(0.4F).build())));
    public static final RegistryObject<Item> CHEESE_RICE_BALL = registerItem("cheese_rice_ball", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(9).saturationMod(0.5F).build())));
    public static final RegistryObject<Item> CHOCOLATE_SAUCE = ITEMS.register("chocolate_sauce", () -> new PackagedItem(new ResourceLocation(ModernDelightMain.MOD_ID, "jar"),new Item.Properties()
            .stacksTo(16).craftRemainder(ModItems.JAR.get()).food(new FoodProperties.Builder().nutrition(5).saturationMod(0.3F).build())));
    public static final RegistryObject<Item> SHRIMP_PASTE = registerItem("shrimp_paste", () -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),new Item.Properties()
            .stacksTo(16).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1F).build())));
    public static final RegistryObject<Item> MIXED_SHRIMP_PASTE = registerItem("mixed_shrimp_paste", () -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),new Item.Properties()
            .craftRemainder(Items.BOWL).stacksTo(16).food(new FoodProperties.Builder().nutrition(4).saturationMod(0.1F).build())));
    public static final RegistryObject<Item> DEEP_FRIED_SHRIMP_CAKE = registerItem("deep_fried_shrimp_cake", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(9).saturationMod(0.3F).build())));
    public static final RegistryObject<Item> SEAWEED_FRIED_SHRIMP_CAKE = registerItem("seaweed_fried_shrimp_cake", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(11).saturationMod(0.4F).build())));
    public static final RegistryObject<Item> DONUT = registerItem("donut", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(5).saturationMod(0.2F).build())));
    public static final RegistryObject<Item> CHERRY_PUDDING = registerItem("cherry_pudding", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(5).saturationMod(0.3F).build())));
    public static final RegistryObject<Item> ICE_LOLLY = registerItem("ice_lolly", () -> new PackagedItem(new ResourceLocation("minecraft", "stick"),new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2F).build())));
    public static final RegistryObject<Item> PACKAGING_BAG = registerItem("packaging_bag", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIRTY_PACKAGING_BAG = registerItem("dirty_packaging_bag", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POTATO_CHIPS = registerItem("potato_chips", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(1).saturationMod(0.1F).build())));
    public static final RegistryObject<Item> DEEP_FRIED_POTATO_CHIPS = registerItem("deep_fried_potato_chips", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).build())));
    public static final RegistryObject<Item> FRENCH_FRIES = ITEMS.register("french_fries", () -> new PackagedItem(new ResourceLocation(ModernDelightMain.MOD_ID, "dirty_packaging_bag"),
            new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.3F).build())));
    public static final RegistryObject<Item> FRIED_CHICKEN = registerItem("fried_chicken", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(11).saturationMod(0.5F).build())));
    public static final RegistryObject<Item> FRIED_BROWN_MUSHROOM = registerItem("fried_brown_mushroom", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2F).build())));
    public static final RegistryObject<Item> FRIED_RED_MUSHROOM = registerItem("fried_red_mushroom", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(3).saturationMod(0.3F).build())));
    public static final RegistryObject<Item> CREAM_OF_MUSHROOM_SOUP = registerItem("cream_of_mushroom_soup", () -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),new Item.Properties()
            .stacksTo(16).food(new FoodProperties.Builder().nutrition(12).saturationMod(0.5F).build())));
    public static final RegistryObject<Item> FRENCH_ONION_SOUP = registerItem("french_onion_soup", () -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),new Item.Properties()
            .stacksTo(16).food(new FoodProperties.Builder().nutrition(10).saturationMod(0.6F).build())));
    public static final RegistryObject<Item> CHERRY_ICE_LOLLY = registerItem("cherry_ice_lolly", () -> new PackagedItem(new ResourceLocation("minecraft", "stick"),new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(6).saturationMod(0.3F).build())));
    public static final RegistryObject<Item> MATCHA_ICE_LOLLY = registerItem("matcha_ice_lolly", () -> new PackagedItem(new ResourceLocation("minecraft", "stick"),new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(6).saturationMod(0.3F)
                    .effect(() -> new MobEffectInstance(MobEffects.LUCK,30*20,0),1.0f)
                    .build())));
    public static final RegistryObject<Item> WITHER_ICE_LOLLY = registerItem("wither_ice_lolly", () -> new PackagedItem(new ResourceLocation("minecraft", "stick"),new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(6).saturationMod(0.3F)
                    .effect(() -> new MobEffectInstance(MobEffects.WITHER,200,0),1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,300,1),1.0f)
                    .build())));
    public static final RegistryObject<Item> GLOW_SQUID_TENTACLE = registerItem("glow_squid_tentacle", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F)
                    .effect(() -> new MobEffectInstance(MobEffects.GLOWING,100,0),1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION,100,0),1.0f)
                    .build())));
    public static final RegistryObject<Item> GLOW_SQUID_TENTACLE_KEBABS = registerItem("glow_squid_tentacle_kebabs", () -> new PackagedItem(new ResourceLocation("minecraft", "stick"),new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(8).saturationMod(0.3F)
                    .effect(() -> new MobEffectInstance(MobEffects.GLOWING,20*20,0),1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION,20*20,0),1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,20*20,0),1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING,20*20,0),1.0f)
                    .build())));
    public static final RegistryObject<Item> RAW_GLOW_SQUID_TENTACLE_KEBABS = registerItem("raw_glow_squid_tentacle_kebabs", () -> new PackagedItem(new ResourceLocation("minecraft", "stick"),new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2F)
                    .effect(() -> new MobEffectInstance(MobEffects.GLOWING,10*20,0),1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION,10*20,0),1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,10*20,0),1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING,10*20,0),1.0f)
                    .build())));
    public static final RegistryObject<Item> SQUID_TENTACLE = registerItem("squid_tentacle", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3F)
                    .build())));
    public static final RegistryObject<Item> SQUID_TENTACLE_KEBABS = registerItem("squid_tentacle_kebabs", () -> new PackagedItem(new ResourceLocation("minecraft", "stick"),new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(8).saturationMod(0.3F)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,20*20,0),1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING,20*20,0),1.0f)
                    .build())));
    public static final RegistryObject<Item> RAW_SQUID_TENTACLE_KEBABS = registerItem("raw_squid_tentacle_kebabs", () -> new PackagedItem(new ResourceLocation("minecraft", "stick"),new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2F)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,10*20,0),1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING,10*20,0),1.0f)
                    .build())));
    public static final RegistryObject<Item> STREAKY_PORK = registerItem("streaky_pork", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.2F)
                    .build())));

    public static final RegistryObject<Item> TURNIP = registerItem("turnip",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1F)
                            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.BAD_OMEN,100,0),0.03f)
                            .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS,100,0),0.2f)
                            .effect(() -> new MobEffectInstance(MobEffects.CONDUIT_POWER,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.DARKNESS,100,0),0.2f)
                            .effect(() -> new MobEffectInstance(MobEffects.DOLPHINS_GRACE,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.GLOWING,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.HUNGER,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.JUMP,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.LUCK,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.LEVITATION,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.DIG_SLOWDOWN,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.POISON,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE,100,0),0.03f)
                            .effect(() -> new MobEffectInstance(MobEffects.UNLUCK,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.WITHER,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS,100,0),0.1f)
                            .effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING,100,0),0.1f)
                            .build())){
                @Override
                public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag context) {
                    tooltip.add(Component.translatable(TextUtil.TURNIP).withStyle(ChatFormatting.DARK_RED));
                    super.appendHoverText(stack, level, tooltip, context);
                }
                @Override
                public InteractionResult useOn(net.minecraft.world.item.context.UseOnContext context) {
                    Level level = context.getLevel();
                    BlockPos pos = context.getClickedPos();
                    if (level.getBlockState(pos).getBlock() instanceof ComposterBlock){
                        Objects.requireNonNull(context.getPlayer()).getMainHandItem().shrink(1);
                        level.explode(context.getPlayer(), pos.getX(), pos.getY(), pos.getZ(),1.2f,true, net.minecraft.world.level.Level.ExplosionInteraction.BLOCK);
                    }
                    return super.useOn(context);
                }
            });
    public static final RegistryObject<Item> CHEESE_BURGER = registerItem("cheese_burger", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(14).saturationMod(0.4F)
                    .build())));
    public static final RegistryObject<Item> RAW_DONUT = registerItem("raw_donut", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2F)
                    .build())));
    public static final RegistryObject<Item> CHOCOLATE_DONUT = registerItem("chocolate_donut", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(8).saturationMod(0.3F)
                    .build())));
    public static final RegistryObject<Item> ROAST_STREAKY_PORK = registerItem("roast_streaky_pork", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(10).saturationMod(0.5F)
                    .build())));
    public static final RegistryObject<Item> PORK_CHOP_BURGER = registerItem("pork_chop_burger", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(18).saturationMod(0.5F)
                    .build())));
    public static final RegistryObject<Item> FRIED_COD_NUGGET = registerItem("fried_cod_nugget", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(6).saturationMod(0.3F)
                    .build())));
    public static final RegistryObject<Item> PORK_RIBS = registerItem("pork_ribs", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1F)
                    .build())));
    public static final RegistryObject<Item> PORK_HOOF = registerItem("pork_hoof", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(3).saturationMod(0.1F)
                    .build())));
    public static final RegistryObject<Item> GARLIC = ITEMS.register("garlic", () -> new BlockItem(ModBlocks.GARLIC_CROP.get(),
            new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).build())));
    public static final RegistryObject<Item> GARLIC_PETAL = registerItem("garlic_petal", () -> new Item(
            new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3f).build())));
    public static final RegistryObject<Item> ELECTRIC_WHISK = registerItem("electric_whisk",() -> new ElectricWhiskItem());
    public static final RegistryObject<Item> SWEET_BERRIES_JUICE = registerItem("sweet_berries_juice",
            () -> new JuiceItem(4,0.3f,Items.GLASS_BOTTLE,
            new MobEffectInstance(MobEffects.REGENERATION,100,0)));
    public static final RegistryObject<Item> TOMATO_JUICE = registerItem("tomato_juice",
            () -> new JuiceItem(4,0.3f,Items.GLASS_BOTTLE,
                    new MobEffectInstance(MobEffects.ABSORPTION,100,0)));
    public static final RegistryObject<Item> LIQUEFIED_BIOGAS_BUCKET = registerItem("liquefied_biogas_bucket",() -> new BucketItem(ModFluid.STILL_LIQUEFIED_BIOGAS::get,new Item.Properties()
            .stacksTo(1).craftRemainder(Items.BUCKET)));
    public static final RegistryObject<Item> FRIED_NOODLES = registerItem("fired_noodles",() -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(3).saturationMod(0.1f).build())));
    public static final RegistryObject<Item> MULTIFUNCTIONAL_WRAPPING_PAPER = registerItem("multifunctional_wrapping_paper",() -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> QUICKLIME = registerItem("quicklime",() -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PACKAGED_INSTANT_NOODLES = registerItem("packaged_instant_noodles",() -> new PackagedInstantNoodlesItem());
    public static final RegistryObject<Item> PORTABLE_POT = registerItem("portable_pot",() -> new PortablePotItem());
    public static final RegistryObject<Item> COOKED_PORTABLE_POT = registerItem("cooked_portable_pot",() -> new CookedPortablePotItem());
    public static final RegistryObject<Item> DIRTY_WRAPPING_PAPER = registerItem("dirty_wrapping_paper",() -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HONEY_CRYSTALLIZATION =  registerItem("honey_crystallization",() -> new SeasoningItem(new Item.Properties(),
            new MobEffectInstance(MobEffects.REGENERATION,10 * 20,0)));
    public static final RegistryObject<Item> SEA_SALT =  ITEMS.register("sea_salt", () -> new SeasoningItem(new Item.Properties().craftRemainder(JAR.get()),
            new MobEffectInstance(MobEffects.DOLPHINS_GRACE,30 * 20,0)));
    public static final RegistryObject<Item> GARLIC_PUREE =  registerItem("garlic_puree",() -> new SeasoningItem(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1F).build()),
            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,30 * 20,0)));
    public static final RegistryObject<Item> STONE_MORTAR =  registerItem("stone_mortar",() -> new StoneMortarItem(ModToolMaterials.STONE,new Item.Properties()));
    public static final RegistryObject<Item> MATCHA =  registerItem("matcha",() -> new SeasoningItem(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1F)
                    .effect(() -> new MobEffectInstance(MobEffects.LUCK,20 * 20,0),0.8f).build()),
            new MobEffectInstance(MobEffects.LUCK,30 * 20,0)));
    public static final RegistryObject<Item> SEA_SALT_LEMON = ITEMS.register("sea_salt_lemon", () ->
            new JuiceItem(5,0.3f,ModBlocks.GLASS_CUP.get().asItem(),
                    new MobEffectInstance(MobEffects.WATER_BREATHING,30 * 20,0),
                    new MobEffectInstance(MobEffects.JUMP,15 * 20,0)));
    public static final RegistryObject<Item> MANGO_MILK_TEA = ITEMS.register("mango_milk_tea", () ->
            new JuiceItem(5,0.3f,ModBlocks.GLASS_CUP.get().asItem(),
                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED,30 * 20,0),
                    new MobEffectInstance(MobEffects.NIGHT_VISION,30 * 20,0)));
    public static final RegistryObject<Item> STEAMED_PUMPKIN_IN_BOWL = registerItem("steamed_pumpkin_in_bowl",
            () -> new PackagedItem(new ResourceLocation("minecraft", "bowl"),new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL)
                    .food(new FoodProperties.Builder().nutrition(6).saturationMod(0.5f).build())));
    public static final RegistryObject<Item> STEAMED_PUMPKIN_WIP = ITEMS.register("steamed_pumpkin_wip", () ->
            new PackagedItem(new ResourceLocation(ModernDelightMain.MOD_ID, "wooden_plate"),new Item.Properties().stacksTo(16).craftRemainder(ModBlocks.WOODEN_PLATE.get().asItem())
                    .food(new FoodProperties.Builder().nutrition(6).saturationMod(0.3f)
                            .effect(() -> new MobEffectInstance(MobEffects.HUNGER,10*20,0),0.5f).build())));
    public static final RegistryObject<Item> CHOCOLATE_CRUNCH_ICE_LOLLY = registerItem("chocolate_crunch_ice_lolly",
            () -> new PackagedItem(new ResourceLocation("minecraft", "stick"),new Item.Properties().craftRemainder(Items.STICK)
                    .food(new FoodProperties.Builder().nutrition(7).saturationMod(0.4f).build())));
    public static final RegistryObject<Item> STEAMED_STUFFED_BUN_WIP = ITEMS.register("steamed_stuffed_bun_wip", () ->
            new PackagedItem(new ResourceLocation(ModernDelightMain.MOD_ID, "wooden_plate"),new Item.Properties().craftRemainder(ModBlocks.WOODEN_PLATE.get().asItem())
                    .stacksTo(16).food(new FoodProperties.Builder().nutrition(3).saturationMod(0.2f).build())));
    public static final RegistryObject<Item> STEAMED_STUFFED_BUN = ITEMS.register("steamed_stuffed_bun", () ->
            new PackagedItem(new ResourceLocation(ModernDelightMain.MOD_ID, "wooden_plate"),new Item.Properties().craftRemainder(ModBlocks.WOODEN_PLATE.get().asItem())
                    .stacksTo(16).food(new FoodProperties.Builder().nutrition(7).saturationMod(0.3f).build())));
    public static final RegistryObject<Item> VEGETABLE_STEAMED_STUFFED_BUN_WIP = ITEMS.register("vegetable_steamed_stuffed_bun_wip", () ->
            new PackagedItem(new ResourceLocation(ModernDelightMain.MOD_ID, "wooden_plate"),new Item.Properties().craftRemainder(ModBlocks.WOODEN_PLATE.get().asItem())
                    .stacksTo(16).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).build())));
    public static final RegistryObject<Item> VEGETABLE_STEAMED_STUFFED_BUN = ITEMS.register("vegetable_steamed_stuffed_bun", () ->
            new PackagedItem(new ResourceLocation(ModernDelightMain.MOD_ID, "wooden_plate"),new Item.Properties().craftRemainder(ModBlocks.WOODEN_PLATE.get().asItem())
                    .stacksTo(16).food(new FoodProperties.Builder().nutrition(6).saturationMod(0.3f).build())));
    public static final RegistryObject<Item> SHAOMAI_WIP = ITEMS.register("shaomai_wip", () ->
            new PackagedItem(new ResourceLocation(ModernDelightMain.MOD_ID, "wooden_plate"),new Item.Properties().craftRemainder(ModBlocks.WOODEN_PLATE.get().asItem())
                    .stacksTo(16).food(new FoodProperties.Builder().nutrition(3).saturationMod(0.3f).build())));
    public static final RegistryObject<Item> SHAOMAI = ITEMS.register("shaomai", () ->
            new PackagedItem(new ResourceLocation(ModernDelightMain.MOD_ID, "wooden_plate"),new Item.Properties().craftRemainder(ModBlocks.WOODEN_PLATE.get().asItem())
                    .stacksTo(16).food(new FoodProperties.Builder().nutrition(9).saturationMod(0.5f).build())));
    public static final RegistryObject<Item> BLACK_PEPPER_STEAK = ITEMS.register("black_pepper_steak", () ->
            new PackagedItem(new ResourceLocation(ModernDelightMain.MOD_ID, "wooden_plate"),new Item.Properties().craftRemainder(ModBlocks.WOODEN_PLATE.get().asItem())
                    .stacksTo(16).food(new FoodProperties.Builder().nutrition(11).saturationMod(0.4f).build())));
    public static final RegistryObject<Item> HOLDER = registerItem("holder", () -> new HolderItem());
    public static final RegistryObject<Item> HOLDER_UP = registerItem("holder_up", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GUIDE_BOOK = registerItem("guide_book", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> RAW_NOODLES = registerItem("raw_noodles", () -> new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).effect(() -> new MobEffectInstance(MobEffects.HUNGER,10 * 20,0),0.5f).build())));
    public static final RegistryObject<Item> SWEETENED_WATER_BUCKET = registerItem("sweetened_water_bucket",() -> new BucketItem(ModFluid.STILL_SWEETENED_WATER::get,new Item.Properties()
            .stacksTo(1).craftRemainder(Items.BUCKET)));
    public static RegistryObject<Item> registerItem(String name, java.util.function.Supplier<Item> itemSupplier){
        return ITEMS.register(name, itemSupplier);
    }

    public static void registerModItems(){
        ModernDelightMain.LOGGER.info("Registering Mod Items for " + ModernDelightMain.MOD_ID);
        ITEMS.register(ModernDelightMain.EVENT_BUS);
    }
}
