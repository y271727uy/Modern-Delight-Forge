package com.y271727uy.moderndelight.networking;

import com.y271727uy.moderndelight.ModernDelightMain;
import com.y271727uy.moderndelight.networking.packet.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ModernDelightMain.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static boolean registered;

    public static void registerC2SPacket(){
        registerPackets();
    }
    public static void registerS2CPacket(){
        registerPackets();
    }

    private static void registerPackets() {
        if (registered) {
            return;
        }
        registered = true;

        ModernDelightMain.LOGGER.info("Registering network packets for {}", ModernDelightMain.MOD_ID);

        int id = 0;
        CHANNEL.messageBuilder(UpdateInventoryC2SPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(UpdateInventoryC2SPacket::encode)
                .decoder(UpdateInventoryC2SPacket::decode)
                .consumerMainThread(UpdateInventoryC2SPacket::handle)
                .add();
        CHANNEL.messageBuilder(SpawnXPC2SPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SpawnXPC2SPacket::encode)
                .decoder(SpawnXPC2SPacket::decode)
                .consumerMainThread(SpawnXPC2SPacket::handle)
                .add();
        CHANNEL.messageBuilder(ChangeBlockEntityDataC2SPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ChangeBlockEntityDataC2SPacket::encode)
                .decoder(ChangeBlockEntityDataC2SPacket::decode)
                .consumerMainThread(ChangeBlockEntityDataC2SPacket::handle)
                .add();
        CHANNEL.messageBuilder(ItemStackSyncS2CPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ItemStackSyncS2CPacket::encode)
                .decoder(ItemStackSyncS2CPacket::decode)
                .consumerMainThread(ItemStackSyncS2CPacket::handle)
                .add();
        CHANNEL.messageBuilder(IntegerSyncS2CPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(IntegerSyncS2CPacket::encode)
                .decoder(IntegerSyncS2CPacket::decode)
                .consumerMainThread(IntegerSyncS2CPacket::handle)
                .add();
        CHANNEL.messageBuilder(FluidSyncS2CPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(FluidSyncS2CPacket::encode)
                .decoder(FluidSyncS2CPacket::decode)
                .consumerMainThread(FluidSyncS2CPacket::handle)
                .add();
    }
}
