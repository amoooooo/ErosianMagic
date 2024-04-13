package aster.amo.erosianmagic.net.classsync;

import aster.amo.erosianmagic.mage.wizard.IWizard;
import aster.amo.erosianmagic.rogue.charlatan.ICharlatan;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CharlatanSyncPacket {
    CompoundTag tag;
    UUID uuid;

    public CharlatanSyncPacket(CompoundTag tag, UUID uuid) {
        this.tag = tag;
        this.uuid = uuid;
    }

    public static void encode(CharlatanSyncPacket object, FriendlyByteBuf buffer) {
        buffer.writeNbt(object.tag);
        buffer.writeUUID(object.uuid);
    }

    public static CharlatanSyncPacket decode(FriendlyByteBuf buffer) {
        return new CharlatanSyncPacket(buffer.readNbt(), buffer.readUUID());
    }

    public static void handle(CharlatanSyncPacket object, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = context.get().getSender().level().getPlayerByUUID(object.uuid);
            if(player != null) {
                player.getCapability(ICharlatan.INSTANCE).ifPresent((k) -> ((INBTSerializable<CompoundTag>) k).deserializeNBT(object.tag));
            }
        });
        context.get().setPacketHandled(true);
    }
}
