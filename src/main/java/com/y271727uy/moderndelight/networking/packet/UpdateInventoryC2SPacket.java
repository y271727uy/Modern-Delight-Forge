package com.y271727uy.moderndelight.networking.packet;

import com.y271727uy.moderndelight.networking.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateInventoryC2SPacket {
    private final BlockPos pos;
    private final ItemStack itemStack;

    public UpdateInventoryC2SPacket(BlockPos pos, ItemStack itemStack) {
        this.pos = pos;
        this.itemStack = itemStack;
    }

    public static void encode(UpdateInventoryC2SPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeItem(msg.itemStack);
    }

    public static UpdateInventoryC2SPacket decode(FriendlyByteBuf buf) {
        return new UpdateInventoryC2SPacket(buf.readBlockPos(), buf.readItem());
    }

    public static void handle(UpdateInventoryC2SPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) {
                return;
            }
            BlockEntity blockEntity = player.level().getBlockEntity(msg.pos);
            if (blockEntity == null) {
                return;
            }
            String simpleName = blockEntity.getClass().getSimpleName();
            if ("CuisineTableBlockEntity".equals(simpleName)) {
                setItem(blockEntity, 2, msg.itemStack);
            } else if ("ElectriciansDeskBlockEntity".equals(simpleName)) {
                setItem(blockEntity, 8, msg.itemStack);
            }
        });
        ctx.setPacketHandled(true);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void setItem(BlockEntity blockEntity, int slot, ItemStack stack) {
        try {
            Object items = blockEntity.getClass().getMethod("getItems").invoke(blockEntity);
            if (items instanceof java.util.List list) {
                list.set(slot, stack);
            }
            blockEntity.getClass().getMethod("setChanged").invoke(blockEntity);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    public static void send(BlockPos pos, ItemStack itemStack) {
        NetworkHandler.CHANNEL.sendToServer(new UpdateInventoryC2SPacket(pos, itemStack));
    }
}
