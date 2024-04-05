package aster.amo.erosianmagic.net;

import aster.amo.erosianmagic.client.ErosianMagicClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundInsultPacket {
    private final String insult;

    public ClientboundInsultPacket(String clazz) {
        this.insult = clazz;
    }

    public static void encode(ClientboundInsultPacket object, FriendlyByteBuf buf) {
        buf.writeUtf(object.insult);
    }

    public static ClientboundInsultPacket decode (FriendlyByteBuf buf) {
        return new ClientboundInsultPacket(buf.readUtf());
    }

    public static void handle(ClientboundInsultPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft.getInstance().getConnection().sendChat(packet.insult);
        });
        ctx.get().setPacketHandled(true);

    }
}
