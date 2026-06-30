package com.y271727uy.moderndelight.item.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.y271727uy.moderndelight.sound.ModSounds;
import com.y271727uy.moderndelight.tag.TagKeys;
import com.y271727uy.moderndelight.util.TextUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.UUID;

public class CrowbarItem extends Item {
    private static final UUID BASE_ATTACK_DAMAGE_UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
    private static final UUID BASE_ATTACK_SPEED_UUID = UUID.fromString("22653B89-116E-49DC-9B6B-9971489B5BE5");
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    public static final java.util.Set<net.minecraft.world.item.enchantment.Enchantment> ALLOWED_ENCHANTMENTS = java.util.Set.of(
            net.minecraft.world.item.enchantment.Enchantments.SHARPNESS,
            net.minecraft.world.item.enchantment.Enchantments.SMITE,
            net.minecraft.world.item.enchantment.Enchantments.BANE_OF_ARTHROPODS,
            net.minecraft.world.item.enchantment.Enchantments.KNOCKBACK,
            net.minecraft.world.item.enchantment.Enchantments.FIRE_ASPECT,
            net.minecraft.world.item.enchantment.Enchantments.MOB_LOOTING,
            net.minecraft.world.item.enchantment.Enchantments.UNBREAKING);
    public CrowbarItem(Tier material, float attackDamage, float attackSpeed) {
        super(new Item.Properties().durability(material.getUses()));
        float attackDamage1 = attackDamage + material.getAttackDamageBonus();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(BASE_ATTACK_DAMAGE_UUID,
                        "Weapon modifier", attackDamage1, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED,
                new AttributeModifier(BASE_ATTACK_SPEED_UUID,
                        "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.world.item.enchantment.Enchantment enchantment) {
        return ALLOWED_ENCHANTMENTS.contains(enchantment);
    }

    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag context){
        if(Screen.hasShiftDown()){
            tooltip.add(TextUtil.getShiftText(true));
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("moderndelight.tooltips.crowbar").withStyle(ChatFormatting.GRAY));
        }else {
            tooltip.add(TextUtil.getShiftText(false));
        }
        super.appendHoverText(stack, level, tooltip, context);
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide){
            return InteractionResult.SUCCESS;
        }
        BlockPos blockPos = context.getClickedPos();
        BlockState targetState = level.getBlockState(blockPos);
        if (context.getPlayer() == null) {
            return InteractionResult.PASS;
        }
        context.getItemInHand().hurtAndBreak(1, context.getPlayer(), (player) -> player.broadcastBreakEvent(context.getHand()));
        if (targetState.is(BlockTags.NEEDS_IRON_TOOL) || targetState.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
            level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                    ModSounds.ITEM_CROWBAR_HIT.get(),
                    SoundSource.BLOCKS,
                    1.0f, level.random.nextFloat()+1.0f);
            return InteractionResult.CONSUME;
        }
        if (targetState.is(TagKeys.CROWBAR_DESTROYABLE)){
            if (level.random.nextDouble() < 0.35){
                level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                        ModSounds.ITEM_CROWBAR_HIT.get(),
                        SoundSource.BLOCKS,
                        0.3f, level.random.nextFloat()+2.0f);
                level.destroyBlock(blockPos, true, context.getPlayer());
            } else {
                level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                        ModSounds.ITEM_CROWBAR_HIT.get(),
                        SoundSource.BLOCKS,
                        0.6f, level.random.nextFloat()+1.0f);
            }
        }
        return InteractionResult.CONSUME;
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(3, attacker, (e) -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!level.isClientSide && state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(2, miner, (e) -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(slot);
    }
}
