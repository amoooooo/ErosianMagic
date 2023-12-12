package aster.amo.erosianmagic.net;

import aster.amo.erosianmagic.client.ErosianMagicClient;
import aster.amo.erosianmagic.spellsnspellbooks.data.ClientMultiTargetingData;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class ClientboundClassPacket {
    private final String clazz;

    public ClientboundClassPacket(String clazz) {
        this.clazz = clazz;
    }

    public static void encode(ClientboundClassPacket object, FriendlyByteBuf buf) {
        buf.writeUtf(object.clazz);
    }

    public static ClientboundClassPacket decode (FriendlyByteBuf buf) {
        return new ClientboundClassPacket(buf.readUtf());
    }

    public static void handle(ClientboundClassPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ErosianMagicClient.CLASS = packet.clazz;
        });
        ctx.get().setPacketHandled(true);

    }
}
