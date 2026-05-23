package com.y271727uy.moderndelight.networking.packet;

import com.y271727uy.moderndelight.networking.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class IntegerSyncS2CPacket {
    private final int value;
    private final BlockPos pos;

    public IntegerSyncS2CPacket(int value, BlockPos pos) {
        this.value = value;
        this.pos = pos;
    }

    public static void encode(IntegerSyncS2CPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.value);
        buf.writeBlockPos(msg.pos);
    }

    public static IntegerSyncS2CPacket decode(FriendlyByteBuf buf) {
        return new IntegerSyncS2CPacket(buf.readInt(), buf.readBlockPos());
    }

    public static void handle(IntegerSyncS2CPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                Object blockEntity = Minecraft.getInstance().level.getBlockEntity(msg.pos);
                if (blockEntity != null && "WoodenPlateBlockEntity".equals(blockEntity.getClass().getSimpleName())){
                    try {
                        blockEntity.getClass().getMethod("setRotate", int.class).invoke(blockEntity, msg.value);
                    } catch (ReflectiveOperationException ignored) {
                    }
                }
            }
        });
        ctx.setPacketHandled(true);
    }
    public static void send(BlockPos pos, int i, Level world) {
        if (!world.isClientSide) {
            NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new IntegerSyncS2CPacket(i, pos));
        }
    }
}
