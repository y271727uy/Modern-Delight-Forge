package com.y271727uy.moderndelight.networking.packet;

import com.y271727uy.moderndelight.networking.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class ItemStackSyncS2CPacket {
    private final ItemStack[] list;
    private final BlockPos pos;

    public ItemStackSyncS2CPacket(ItemStack[] list, BlockPos pos) {
        this.list = list;
        this.pos = pos;
    }

    public static void encode(ItemStackSyncS2CPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.list.length);
        for (ItemStack itemStack : msg.list) {
            buf.writeItem(itemStack);
        }
        buf.writeBlockPos(msg.pos);
    }

    public static ItemStackSyncS2CPacket decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        ItemStack[] list = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            list[i] = buf.readItem();
        }
        return new ItemStackSyncS2CPacket(list, buf.readBlockPos());
    }

    public static void handle(ItemStackSyncS2CPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                Object blockEntity = Minecraft.getInstance().level.getBlockEntity(msg.pos);
                if (blockEntity != null) {
                    try {
                        Object items = blockEntity.getClass().getMethod("getItems").invoke(blockEntity);
                        if (items instanceof java.util.List list) {
                            for (int i = 0; i < msg.list.length && i < list.size(); i++) {
                                list.set(i, msg.list[i]);
                            }
                        }
                    } catch (ReflectiveOperationException ignored) {
                    }
                }
            }
        });
        ctx.setPacketHandled(true);
    }
    public static void send(BlockPos pos, java.util.List<ItemStack> list, Level world) {
        if (!world.isClientSide) {
            NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new ItemStackSyncS2CPacket(list.toArray(ItemStack[]::new), pos));
        }
    }
}
