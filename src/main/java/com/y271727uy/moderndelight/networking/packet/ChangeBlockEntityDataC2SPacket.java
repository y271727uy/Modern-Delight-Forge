package com.y271727uy.moderndelight.networking.packet;

import com.y271727uy.moderndelight.networking.NetworkHandler;
import com.y271727uy.moderndelight.block.power.ElectriciansDeskBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChangeBlockEntityDataC2SPacket {
    private final BlockPos pos;
    private final int[] array;

    public ChangeBlockEntityDataC2SPacket(BlockPos pos, int[] array) {
        this.pos = pos;
        this.array = array;
    }

    public static void encode(ChangeBlockEntityDataC2SPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeVarIntArray(msg.array);
    }

    public static ChangeBlockEntityDataC2SPacket decode(FriendlyByteBuf buf) {
        return new ChangeBlockEntityDataC2SPacket(buf.readBlockPos(), buf.readVarIntArray());
    }

    public static void handle(ChangeBlockEntityDataC2SPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            var player = ctx.getSender();
            if (player == null) {
                return;
            }
            BlockEntity blockEntity = player.level().getBlockEntity(msg.pos);
            if (blockEntity == null) {
                return;
            }
            if (blockEntity instanceof ElectriciansDeskBlockEntity electriciansDesk) {
                handleElectriciansDesk(player, msg.pos, msg.array, electriciansDesk);
            } else {
                String simpleName = blockEntity.getClass().getSimpleName();
                if ("TeslaCoilBlockEntity".equals(simpleName)) {
                    if (msg.array[0] == 1) {
                        invoke(blockEntity, "setShowParticle", false);
                    } else if (msg.array[0] == 2) {
                        invoke(blockEntity, "setShowParticle", true);
                    }
                } else if ("ACDCConverterBlockEntity".equals(simpleName)) {
                    if (msg.array[0] == 1) {
                        invoke(blockEntity, "addWorkSpeed", 1);
                    } else if (msg.array[0] == 2) {
                        invoke(blockEntity, "reduceWorkSpeed", 1);
                    } else if (msg.array[0] == 3) {
                        invoke(blockEntity, "addWorkSpeed", 5);
                    } else if (msg.array[0] == 4) {
                        invoke(blockEntity, "reduceWorkSpeed", 5);
                    }
                    if (msg.array[1] == 1) {
                        invoke(blockEntity, "setACMode", false);
                    } else if (msg.array[1] == 2) {
                        invoke(blockEntity, "setACMode", true);
                    }
                    blockEntity.setChanged();
                } else if ("IceCreamMakerBlockEntity".equals(simpleName)) {
                    if (msg.array[0] == 1) {
                        invoke(blockEntity, "changeIceCream1");
                    } else if (msg.array[0] == 2) {
                        invoke(blockEntity, "changeIceCream2");
                    } else if (msg.array[0] == 3) {
                        invoke(blockEntity, "changeIceCream3");
                    }
                } else if ("CuisineTableBlockEntity".equals(simpleName)) {
                    if (msg.array[0] == 1) {
                        invoke(blockEntity, "setCanOpen", true);
                    }
                }
            }
        });
        ctx.setPacketHandled(true);
    }

    private static void handleElectriciansDesk(ServerPlayer player, BlockPos pos, int[] array,
                                                ElectriciansDeskBlockEntity electriciansDesk) {
        if (array.length == 0) {
            return;
        }
        switch (array[0]) {
            case 1 -> electriciansDesk.setCanCraft(true);
            case 2 -> {
                electriciansDesk.setCanCraft(false);
                for (int i = 0; i < 6; i++) {
                    electriciansDesk.removeItem(i, 1);
                }
                player.level().playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            case 3 -> {
                electriciansDesk.removeItem(6, 1);
                electriciansDesk.removeItem(7, 1);
                electriciansDesk.setOccupied(true);
            }
            case 4 -> electriciansDesk.setOccupied(false);
            case 5 -> electriciansDesk.setCanCraft(false);
            default -> {
                return;
            }
        }
        electriciansDesk.setChanged();
    }

    private static void invoke(Object target, String methodName, Object... args) {
        try {
            Class<?>[] types = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i] instanceof Integer ? int.class : args[i] instanceof Boolean ? boolean.class : args[i].getClass();
            }
            target.getClass().getMethod(methodName, types).invoke(target, args);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    public static void send(BlockPos pos, int[] array) {
        NetworkHandler.CHANNEL.sendToServer(new ChangeBlockEntityDataC2SPacket(pos, array));
    }
}
