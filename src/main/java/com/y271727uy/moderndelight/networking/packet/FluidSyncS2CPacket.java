package com.y271727uy.moderndelight.networking.packet;

import com.y271727uy.moderndelight.networking.NetworkHandler;
import com.y271727uy.moderndelight.util.FluidStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class FluidSyncS2CPacket {
    private final FluidStack fluid;
    private final BlockPos pos;

    public FluidSyncS2CPacket(FluidStack fluid, BlockPos pos) {
        this.fluid = fluid;
        this.pos = pos;
    }

    public static void encode(FluidSyncS2CPacket msg, FriendlyByteBuf buf) {
        buf.writeLong(msg.fluid.amount_droplets);
        buf.writeUtf(BuiltInRegistries.FLUID.getKey(msg.fluid.getFluid()).toString());
        buf.writeBlockPos(msg.pos);
    }

    public static FluidSyncS2CPacket decode(FriendlyByteBuf buf) {
        long amount = buf.readLong();
        String s = buf.readUtf();
        var fluid = BuiltInRegistries.FLUID.get(new ResourceLocation(s));
        return new FluidSyncS2CPacket(new FluidStack(new net.minecraftforge.fluids.FluidStack(fluid, (int) FluidStack.convertDropletsToMb(amount)), amount), buf.readBlockPos());
    }

    public static void handle(FluidSyncS2CPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                Object blockEntity = Minecraft.getInstance().level.getBlockEntity(msg.pos);
                if (blockEntity != null) {
                    try {
                        blockEntity.getClass().getMethod("setFluid", FluidStack.class).invoke(blockEntity, msg.fluid);
                    } catch (ReflectiveOperationException ignored) {
                    }
                }
            }
        });
        ctx.setPacketHandled(true);
    }

    public static void send(BlockPos pos, FluidStack fluid, Level world) {
        if (!world.isClientSide) {
            NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new FluidSyncS2CPacket(fluid, pos));
        }
    }
}
