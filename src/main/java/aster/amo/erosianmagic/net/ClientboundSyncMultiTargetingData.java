package aster.amo.erosianmagic.net;

import aster.amo.erosianmagic.spellsnspellbooks.data.ClientMultiTargetingData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class ClientboundSyncMultiTargetingData {
    private final List<UUID> targets;
    private final String spellId;

    public ClientboundSyncMultiTargetingData(List<UUID> targets, String spellId) {
        this.targets = targets;
        this.spellId = spellId;
    }

    public static void encode(ClientboundSyncMultiTargetingData object, FriendlyByteBuf buf) {
        buf.writeCollection(object.targets, FriendlyByteBuf::writeUUID);
        buf.writeUtf(object.spellId);
    }

    public static ClientboundSyncMultiTargetingData decode (FriendlyByteBuf buf) {
        return new ClientboundSyncMultiTargetingData(buf.readList(FriendlyByteBuf::readUUID), buf.readUtf());
    }

    public static void handle(ClientboundSyncMultiTargetingData packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientMultiTargetingData data = new ClientMultiTargetingData(packet.targets, packet.spellId);
            ClientMagicData.setTargetingData(data);
        });
        ctx.get().setPacketHandled(true);

    }
}
