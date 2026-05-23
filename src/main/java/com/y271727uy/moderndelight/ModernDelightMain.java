package com.y271727uy.moderndelight;

import com.y271727uy.moderndelight.block.ModBlockEntities;
import com.y271727uy.moderndelight.block.ModBlocks;
import com.y271727uy.moderndelight.block.biogas.GasCanisterBlock;
import com.y271727uy.moderndelight.block.biogas.GasCanisterBlockEntity;
import com.y271727uy.moderndelight.effects.ModEffectsAndPotions;
import com.y271727uy.moderndelight.enchantment.ModEnchantments;
import com.y271727uy.moderndelight.entity.ModEntities;
import com.y271727uy.moderndelight.entity.custom.ButterEntity;
import com.y271727uy.moderndelight.entity.custom.CherryBombEntity;
import com.y271727uy.moderndelight.fluid.ModFluid;
import com.y271727uy.moderndelight.item.ModItemGroups;
import com.y271727uy.moderndelight.item.ModItems;
import com.y271727uy.moderndelight.networking.NetworkHandler;
import com.y271727uy.moderndelight.recipe.ModRecipes;
import com.y271727uy.moderndelight.screen.ModScreenHandlers;
import com.y271727uy.moderndelight.sound.ModSounds;
import com.y271727uy.moderndelight.util.ModConfig;
import com.y271727uy.moderndelight.util.registry_util.ModBrewingRecipe;
import com.y271727uy.moderndelight.util.registry_util.ModCompostingChances;
import com.y271727uy.moderndelight.util.registry_util.ModFuels;
import com.y271727uy.moderndelight.util.registry_util.ModLootTableModifies;
import com.y271727uy.moderndelight.world.gen.ModWorldGeneration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ModernDelightMain.MOD_ID)
public class ModernDelightMain {

	public static final String MOD_ID = "moderndelight";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final IEventBus EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

	public ModernDelightMain() {
		
		// Register all mod components
		ModFluid.registerModFluid(EVENT_BUS);
		ModItems.registerModItems();
		ModBlocks.register(EVENT_BUS);
		ModItemGroups.registerCreativeModeTabs();
		ModBlockEntities.register(EVENT_BUS);
		ModEntities.registerModEntities(EVENT_BUS);
		ModSounds.registerModSounds();
		ModScreenHandlers.registerScreenHandlers();
		ModRecipes.register(EVENT_BUS);
		ModEnchantments.registerModEnchantments();
		ModEffectsAndPotions.register(EVENT_BUS);
		ModWorldGeneration.generateModWorldGen();
		NetworkHandler.registerC2SPacket();
		
		// Register config
		ModConfig.register();
		
		// Register common events
		MinecraftForge.EVENT_BUS.register(this);
		
		// Register dispenser behaviors in FMLCommonSetupEvent (after all registrations are complete)
		EVENT_BUS.addListener(this::commonSetup);
	}
	
	@SubscribeEvent
	public void onServerAboutToStart(ServerAboutToStartEvent event) {
		// Try to register dispenser behaviors when server is about to start
		registerDispenserBehaviors();
	}
	
	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(this::registerDispenserBehaviors);
	}
	
	private void registerDispenserBehaviors() {
		// Check if registry objects are present before registering
		if (!ModItems.BUTTER.isPresent() || !ModItems.CHERRY_BOMB.isPresent() || !ModBlocks.GAS_CANISTER_ITEM.isPresent()) {
			LOGGER.warn("Registry objects not present, skipping dispenser behavior registration");
			return;
		}
		
		try {
			DispenserBlock.registerBehavior(ModItems.BUTTER.get(), new net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior() {
				@Override
				protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
					return new ButterEntity(level, position.x(), position.y(), position.z());
				}
			});
			
			DispenserBlock.registerBehavior(ModItems.CHERRY_BOMB.get(), new net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior() {
				@Override
				protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
					return new CherryBombEntity(level, position.x(), position.y(), position.z());
				}
			});
			
			DispenserBlock.registerBehavior(ModBlocks.GAS_CANISTER_ITEM.get(), new net.minecraft.core.dispenser.DefaultDispenseItemBehavior() {
				private final net.minecraft.core.dispenser.DefaultDispenseItemBehavior defaultDispenseItemBehavior = new net.minecraft.core.dispenser.DefaultDispenseItemBehavior();
				
				@Override
				public ItemStack execute(net.minecraft.core.BlockSource source, ItemStack stack) {
					Level world = source.getLevel();
					Direction dispenserFacing = source.getBlockState().getValue(DispenserBlock.FACING);
					BlockPos pos = source.getPos().relative(dispenserFacing);
					
					if (world.isEmptyBlock(pos)) {
						if (dispenserFacing == Direction.UP || dispenserFacing == Direction.DOWN) {
							world.setBlock(pos, ModBlocks.GAS_CANISTER.get().defaultBlockState(), 3);
						} else {
							world.setBlock(pos, ModBlocks.GAS_CANISTER.get().defaultBlockState().setValue(GasCanisterBlock.FACING, dispenserFacing), 3);
						}
						
						if (world.getBlockEntity(pos) instanceof GasCanisterBlockEntity blockEntity) {
							CompoundTag nbtCompound = BlockItem.getBlockEntityData(stack);
							if (nbtCompound != null) {
								blockEntity.load(nbtCompound);
							}
						}
						stack.shrink(1);
					}
					return stack;
				}
			});
		} catch (Exception e) {
			LOGGER.error("Failed to register dispenser behaviors", e);
		}
	}
}
