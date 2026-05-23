package com.y271727uy.moderndelight.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public class TextUtil {
    public static final int MAX_TOOLTIP_WIDTH = 250;

    public static final String SHIFT_FRONT = "moderndelight.tooltips.shift_front";
    public static final String SHIFT_END = "moderndelight.tooltips.shift_end";
    public static final String ALT_END = "moderndelight.tooltips.alt_end";
    public static final String WHISK = "moderndelight.tooltips.whisk";
    public static final String BUTTER = "moderndelight.tooltips.butter";
    public static final String CUTTLEBONE = "moderndelight.tooltips.cuttlebone";
    public static final String TRUFFLE = "moderndelight.tooltips.truffle";
    public static final String FILTER = "moderndelight.tooltips.filter";
    public static final String KNEADING_STICK = "moderndelight.tooltips.kneading_stick";
    public static final String SPATULA = "moderndelight.tooltips.spatula";
    public static final String BDC = "moderndelight.tooltips.bdc";
    public static final String BDI = "moderndelight.tooltips.bdi";
    public static final String GAS_COOKING_STOVE = "moderndelight.tooltips.gas_cooking_stove";
    public static final String CROWBAR = "moderndelight.tooltips.crowbar";
    public static final String INGREDIENTS = "moderndelight.tooltips.ingredients";
    public static final String BAKING_TRAY = "moderndelight.tooltips.baking_tray";
    public static final String DEEP_FRYER = "moderndelight.tooltips.deep_fryer";
    public static final String WOODEN_BASIN = "moderndelight.tooltips.wooden_basin";
    public static final String HOLDER = "moderndelight.tooltips.holder";
    public static final String ACDCC = "moderndelight.tooltips.acdcc";
    public static final String ALT_ACGen = "moderndelight.tooltips.ac_gen";
    public static final String ALT_ACCom = "moderndelight.tooltips.ac_com";
    public static final String ALT_DCSto = "moderndelight.tooltips.dc_sto";
    public static final String ALT_DCGen = "moderndelight.tooltips.dc_gen";
    public static final String ALT_DCCom = "moderndelight.tooltips.dc_com";
    public static final String BAMBOO_STEAMER = "moderndelight.tooltips.bamboo_steamer";
    public static final String CUISINE_TABLE = "moderndelight.tooltips.cuisine_table";
    public static final String ELECTRICIANS_DESK = "moderndelight.tooltips.electricians_desk";
    public static final String ELECTRIC_STEAMER = "moderndelight.tooltips.electric_steamer";
    public static final String FAN_BLADE = "moderndelight.tooltips.fan_blade";
    public static final String FARADAY_GENERATOR = "moderndelight.tooltips.faraday_generator";
    public static final String FREEZER = "moderndelight.tooltips.freezer";
    public static final String GLASS_BOWL = "moderndelight.tooltips.glass_bowl";
    public static final String PGEN = "moderndelight.tooltips.p_gen";
    public static final String STERLING_ENGINE = "moderndelight.tooltips.sterling_engine";
    public static final String TESLA_COIL = "moderndelight.tooltips.tesla_coil";
    public static final String WTC = "moderndelight.tooltips.wtc";
    public static final String ICE_CREAM_MAKER = "moderndelight.tooltips.ice_cream_maker";
    public static final String TURNIP = "moderndelight.tooltips.turnip";
    public static final String ELECTRIC_WHISK_MSG = "moderndelight.msg.electric_whisk";
    public static final String ELECTRIC_WHISK_NEED_BOWL = "moderndelight.msg.electric_whisk.need_bowl";
    public static final String ELECTRIC_WHISK = "moderndelight.tooltips.electric_whisk";
    public static final String CHARGING_POST = "moderndelight.tooltips.charging_post";
    public static final String JUICE_EXTRACTOR = "moderndelight.tooltips.juice_extractor";
    public static final String POT_HAS_QUICKLIME = "moderndelight.tooltips.pot_has_quicklime";
    public static final String POT_MISS_QUICKLIME = "moderndelight.tooltips.pot_miss_quicklime";
    public static final String POT_HAS_WATER = "moderndelight.tooltips.pot_has_water";
    public static final String POT_MISS_WATER = "moderndelight.tooltips.pot_miss_water";
    public static final String NOODLE_UNHEALTHY = "moderndelight.tooltips.noodle_unhealthy";
    public static final String ANYTHING = "moderndelight.tooltips.anything";
    public static final String NEED_FOOD = "moderndelight.tooltips.need_food";
    public static final String SEASONING_TIP = "moderndelight.tooltips.condiment_tip";
    public static final String FAILED_SEASONING = "moderndelight.tooltips.failed_seasoning";
    public static final String SEASONING_ADDED = "moderndelight.tooltips.seasoning_added";
    public static final String CAN_PLACE = "moderndelight.tooltips.can_place";
    public static final String PUN = "moderndelight.tooltips.prohibit_unlimited_nesting";
    public static final String WOODEN_PLATE = "moderndelight.tooltips.wooden_plate";

    public static List<Component> generateToolTip(Component text, Style style){
        List<Component> result = new ArrayList<>();
        Minecraft client = Minecraft.getInstance();
        if (client == null) return result;
        Font textRenderer = client.font;
        List<FormattedCharSequence> wrappedLines = textRenderer.split(text, MAX_TOOLTIP_WIDTH);
        for (FormattedCharSequence wrappedLine : wrappedLines) {
            MutableComponent lineText = Component.empty();
            wrappedLine.accept((index, charStyle, codePoint) -> {
                MutableComponent charText = Component.literal(new String(Character.toChars(codePoint)));
                charText.setStyle(charStyle.applyTo(style));
                lineText.append(charText);
                return true;
            });
            result.add(lineText);
        }
        return result;
    }

    public static List<Component> generateToolTip(Component text, int rgb){
        return generateToolTip(text,Style.EMPTY.withColor(rgb));
    }

    public static List<Component> generateToolTip(Component text){
        return generateToolTip(text,16755200);
    }

    public static MutableComponent getShiftText(boolean hasDown){
        MutableComponent mutableText = Component.translatable(SHIFT_FRONT).withStyle(ChatFormatting.DARK_GRAY);
        mutableText.append(Component.literal("[").withStyle(ChatFormatting.DARK_GRAY));
        if (hasDown){
            mutableText.append(Component.literal("Shift").withStyle(ChatFormatting.WHITE));
        } else {
            mutableText.append(Component.literal("Shift").withStyle(ChatFormatting.GRAY));
        }
        mutableText.append(Component.literal("]").withStyle(ChatFormatting.DARK_GRAY));
        mutableText.append(Component.translatable(SHIFT_END).withStyle(ChatFormatting.DARK_GRAY));
        return mutableText;
    }

    public static MutableComponent getAltText(boolean hasDown){
        MutableComponent mutableText = Component.translatable(SHIFT_FRONT).withStyle(ChatFormatting.DARK_GRAY);
        mutableText.append(Component.literal("[").withStyle(ChatFormatting.DARK_GRAY));
        if (hasDown){
            mutableText.append(Component.literal("Alt").withStyle(ChatFormatting.WHITE));
        } else {
            mutableText.append(Component.literal("Alt").withStyle(ChatFormatting.GRAY));
        }
        mutableText.append(Component.literal("]").withStyle(ChatFormatting.DARK_GRAY));
        mutableText.append(Component.translatable(ALT_END).withStyle(ChatFormatting.DARK_GRAY));
        return mutableText;
    }

    public static MutableComponent getACGen(String count){
        MutableComponent mutableText = Component.translatable(ALT_ACGen).withStyle(ChatFormatting.DARK_GREEN);
        mutableText.append(Component.literal(" " + count + " ").withStyle(ChatFormatting.WHITE));
        mutableText.append(Component.literal("FE/s").withStyle(ChatFormatting.GRAY));
        return mutableText;
    }

    public static MutableComponent getACCom(String count){
        MutableComponent mutableText = Component.translatable(ALT_ACCom).withStyle(ChatFormatting.DARK_GREEN);
        mutableText.append(Component.literal(" " + count + " ").withStyle(ChatFormatting.WHITE));
        mutableText.append(Component.literal("FE/s").withStyle(ChatFormatting.GRAY));
        return mutableText;
    }

    public static MutableComponent getDCSto(String count){
        MutableComponent mutableText = Component.translatable(ALT_DCSto).withStyle(ChatFormatting.DARK_GREEN);
        mutableText.append(Component.literal(" " + count + " ").withStyle(ChatFormatting.WHITE));
        mutableText.append(Component.literal("FE").withStyle(ChatFormatting.GRAY));
        return mutableText;
    }

    public static MutableComponent getDCGen(String count){
        MutableComponent mutableText = Component.translatable(ALT_DCGen).withStyle(ChatFormatting.DARK_GREEN);
        mutableText.append(Component.literal(" " + count + " ").withStyle(ChatFormatting.WHITE));
        mutableText.append(Component.literal("FE/s").withStyle(ChatFormatting.GRAY));
        return mutableText;
    }

    public static MutableComponent getDCCom(String count){
        MutableComponent mutableText = Component.translatable(ALT_DCCom).withStyle(ChatFormatting.DARK_GREEN);
        mutableText.append(Component.literal(" " + count + " ").withStyle(ChatFormatting.WHITE));
        mutableText.append(Component.literal("FE/s").withStyle(ChatFormatting.GRAY));
        return mutableText;
    }
}
