package aster.amo.erosianmagic.net;

import aster.amo.erosianmagic.client.ErosianMagicClient;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundUpdateSpellsPacket {

    public ClientboundUpdateSpellsPacket() {

    }

    public static void encode(ClientboundUpdateSpellsPacket object, FriendlyByteBuf buf) {

    }

    public static ClientboundUpdateSpellsPacket decode (FriendlyByteBuf buf) {
        return new ClientboundUpdateSpellsPacket();
    }

    public static void handle(ClientboundUpdateSpellsPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(ClientMagicData::updateSpellSelectionManager);
        ctx.get().setPacketHandled(true);
    }
}
