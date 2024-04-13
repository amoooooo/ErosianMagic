package aster.amo.erosianmagic.net.classsync;

import aster.amo.erosianmagic.fighter.barbarian.IBarbarian;
import aster.amo.erosianmagic.fighter.paladin.IPaladin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PaladinSyncPacket {
    CompoundTag tag;
    UUID uuid;

    public PaladinSyncPacket(CompoundTag tag, UUID uuid) {
        this.tag = tag;
        this.uuid = uuid;
    }

    public static void encode(PaladinSyncPacket object, FriendlyByteBuf buffer) {
        buffer.writeNbt(object.tag);
        buffer.writeUUID(object.uuid);
    }

    public static PaladinSyncPacket decode(FriendlyByteBuf buffer) {
        return new PaladinSyncPacket(buffer.readNbt(), buffer.readUUID());
    }

    public static void handle(PaladinSyncPacket object, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = context.get().getSender().level().getPlayerByUUID(object.uuid);
            if(player != null) {
                player.getCapability(IPaladin.INSTANCE).ifPresent((k) -> ((INBTSerializable<CompoundTag>) k).deserializeNBT(object.tag));
            }
        });
        context.get().setPacketHandled(true);
    }
}
