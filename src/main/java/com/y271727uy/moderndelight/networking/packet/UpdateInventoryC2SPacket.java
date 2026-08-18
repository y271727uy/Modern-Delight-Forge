package com.y271727uy.moderndelight.networking.packet;

import com.y271727uy.moderndelight.networking.NetworkHandler;
import com.y271727uy.moderndelight.block.kitchenware.CuisineTableBlockEntity;
import com.y271727uy.moderndelight.block.power.ElectriciansDeskBlockEntity;
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
            if (blockEntity instanceof CuisineTableBlockEntity cuisineTable) {
                cuisineTable.setItem(2, msg.itemStack);
            } else if (blockEntity instanceof ElectriciansDeskBlockEntity electriciansDesk) {
                electriciansDesk.setItem(8, msg.itemStack);
            }
        });
        ctx.setPacketHandled(true);
    }

    public static void send(BlockPos pos, ItemStack itemStack) {
        NetworkHandler.CHANNEL.sendToServer(new UpdateInventoryC2SPacket(pos, itemStack));
    }
}
