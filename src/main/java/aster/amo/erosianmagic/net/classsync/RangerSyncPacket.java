package aster.amo.erosianmagic.net.classsync;

import aster.amo.erosianmagic.mage.wizard.IWizard;
import aster.amo.erosianmagic.rogue.ranger.IRanger;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RangerSyncPacket {
    CompoundTag tag;
    UUID uuid;

    public RangerSyncPacket(CompoundTag tag, UUID uuid) {
        this.tag = tag;
        this.uuid = uuid;
    }

    public static void encode(RangerSyncPacket object, FriendlyByteBuf buffer) {
        buffer.writeNbt(object.tag);
        buffer.writeUUID(object.uuid);
    }

    public static RangerSyncPacket decode(FriendlyByteBuf buffer) {
        return new RangerSyncPacket(buffer.readNbt(), buffer.readUUID());
    }

    public static void handle(RangerSyncPacket object, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = context.get().getSender().level().getPlayerByUUID(object.uuid);
            if(player != null) {
                player.getCapability(IRanger.INSTANCE).ifPresent((k) -> ((INBTSerializable<CompoundTag>) k).deserializeNBT(object.tag));
            }
        });
        context.get().setPacketHandled(true);
    }
}
