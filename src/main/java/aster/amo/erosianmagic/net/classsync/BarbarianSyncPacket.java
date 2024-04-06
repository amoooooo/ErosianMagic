package aster.amo.erosianmagic.net.classsync;

import aster.amo.erosianmagic.fighter.IFighter;
import aster.amo.erosianmagic.fighter.barbarian.IBarbarian;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class BarbarianSyncPacket {
    CompoundTag tag;
    UUID uuid;

    public BarbarianSyncPacket(CompoundTag tag, UUID uuid) {
        this.tag = tag;
        this.uuid = uuid;
    }

    public static void encode(BarbarianSyncPacket object, FriendlyByteBuf buffer) {
        buffer.writeNbt(object.tag);
        buffer.writeUUID(object.uuid);
    }

    public static BarbarianSyncPacket decode(FriendlyByteBuf buffer) {
        return new BarbarianSyncPacket(buffer.readNbt(), buffer.readUUID());
    }

    public static void handle(BarbarianSyncPacket object, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = context.get().getSender().level().getPlayerByUUID(object.uuid);
            if(player != null) {
                player.getCapability(IBarbarian.INSTANCE).ifPresent((k) -> ((INBTSerializable<CompoundTag>) k).deserializeNBT(object.tag));
            }
        });
        context.get().setPacketHandled(true);
    }
}
