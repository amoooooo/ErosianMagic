package aster.amo.erosianmagic.net;

import aster.amo.erosianmagic.bard.IBard;
import aster.amo.erosianmagic.cleric.ICleric;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ClericSyncPacket {
    CompoundTag tag;
    UUID uuid;

    public ClericSyncPacket(CompoundTag tag, UUID uuid) {
        this.tag = tag;
        this.uuid = uuid;
    }

    public static void encode(ClericSyncPacket object, FriendlyByteBuf buffer) {
        buffer.writeNbt(object.tag);
        buffer.writeUUID(object.uuid);
    }

    public static ClericSyncPacket decode(FriendlyByteBuf buffer) {
        return new ClericSyncPacket(buffer.readNbt(), buffer.readUUID());
    }

    public static void handle(ClericSyncPacket object, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = context.get().getSender().level().getPlayerByUUID(object.uuid);
            if(player != null) {
                player.getCapability(ICleric.INSTANCE, null).ifPresent((k) -> ((INBTSerializable<CompoundTag>) k).deserializeNBT(object.tag));
            }
        });
        context.get().setPacketHandled(true);
    }
}
