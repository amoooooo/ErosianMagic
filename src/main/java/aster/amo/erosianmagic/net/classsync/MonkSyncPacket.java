package aster.amo.erosianmagic.net.classsync;

import aster.amo.erosianmagic.mage.wizard.IWizard;
import aster.amo.erosianmagic.rogue.monk.IMonk;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MonkSyncPacket {
    CompoundTag tag;
    UUID uuid;

    public MonkSyncPacket(CompoundTag tag, UUID uuid) {
        this.tag = tag;
        this.uuid = uuid;
    }

    public static void encode(MonkSyncPacket object, FriendlyByteBuf buffer) {
        buffer.writeNbt(object.tag);
        buffer.writeUUID(object.uuid);
    }

    public static MonkSyncPacket decode(FriendlyByteBuf buffer) {
        return new MonkSyncPacket(buffer.readNbt(), buffer.readUUID());
    }

    public static void handle(MonkSyncPacket object, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = context.get().getSender().level().getPlayerByUUID(object.uuid);
            if(player != null) {
                player.getCapability(IMonk.INSTANCE).ifPresent((k) -> ((INBTSerializable<CompoundTag>) k).deserializeNBT(object.tag));
            }
        });
        context.get().setPacketHandled(true);
    }
}
