package com.y271727uy.moderndelight.networking.packet;

import com.y271727uy.moderndelight.networking.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpawnXPC2SPacket {
    private final BlockPos pos;

    public SpawnXPC2SPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(SpawnXPC2SPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static SpawnXPC2SPacket decode(FriendlyByteBuf buf) {
        return new SpawnXPC2SPacket(buf.readBlockPos());
    }

    public static void handle(SpawnXPC2SPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) {
                return;
            }
            if (!(player.level() instanceof ServerLevel world)) {
                return;
            }
            BlockEntity blockEntity = world.getBlockEntity(msg.pos);
            if (blockEntity == null) {
                return;
            }
            String simpleName = blockEntity.getClass().getSimpleName();
            if (simpleName.equals("AdvanceFurnaceBlockEntity") || simpleName.equals("OvenBlockEntity") || simpleName.equals("FreezerBlockEntity")) {
                int experience = takeExperience(blockEntity);
                if (experience != 0) {
                    ExperienceOrb xp = new ExperienceOrb(world, msg.pos.getX(), msg.pos.getY() + 1, msg.pos.getZ(), experience);
                    world.addFreshEntity(xp);
                }
            }
        });
        ctx.setPacketHandled(true);
    }

    private static int takeExperience(Object blockEntity) {
        try {
            Object value = blockEntity.getClass().getMethod("takeExperience").invoke(blockEntity);
            return value instanceof Integer integer ? integer : 0;
        } catch (ReflectiveOperationException ignored) {
        }
        try {
            Object value = blockEntity.getClass().getMethod("getExperience").invoke(blockEntity);
            int experience = value instanceof Integer integer ? integer : 0;
            if (experience != 0) {
                setExperience(blockEntity, 0);
            }
            return experience;
        } catch (ReflectiveOperationException ignored) {
            return 0;
        }
    }

    private static void setExperience(Object blockEntity, int experience) {
        try {
            blockEntity.getClass().getMethod("setExperience", int.class).invoke(blockEntity, experience);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    public static void send(BlockPos pos) {
        NetworkHandler.CHANNEL.sendToServer(new SpawnXPC2SPacket(pos));
    }
}
