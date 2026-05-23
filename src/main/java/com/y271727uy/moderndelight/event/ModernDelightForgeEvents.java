package com.y271727uy.moderndelight.event;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.util.MiscUtil;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ModernDelightMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ModernDelightForgeEvents {
    private ModernDelightForgeEvents() {
    }

    @SubscribeEvent
    public static void onFinishUsingItem(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        MiscUtil.applyFoodEffects(event.getItem(), event.getEntity());
    }
}
