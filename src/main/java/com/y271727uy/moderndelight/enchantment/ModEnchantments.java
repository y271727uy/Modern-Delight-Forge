package com.y271727uy.moderndelight.enchantment;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.enchantment.custom.FineGrindingEnchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ModernDelightMain.MOD_ID);
    
    public static final RegistryObject<Enchantment> FINE_GRINDING = registerEnchantment("fine_grinding", new FineGrindingEnchantment());
    
    private static RegistryObject<Enchantment> registerEnchantment(String name, Enchantment enchant){
        return ENCHANTMENTS.register(name, () -> enchant);
    }
    
    public static void registerModEnchantments(){
        ModernDelightMain.LOGGER.info("Registering Mod Enchantments for " + ModernDelightMain.MOD_ID);
        ENCHANTMENTS.register(ModernDelightMain.EVENT_BUS);
    }
}
