package aster.amo.erosianmagic.net;

import aster.amo.erosianmagic.client.ErosianMagicClient;
import aster.amo.erosianmagic.spellsnspellbooks.SpellRegistry;
import aster.amo.erosianmagic.util.ClientUtil;
import io.redspace.ironsspellbooks.player.ClientSpellCastHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ClientboundEldritchBlastPacket {
    private final UUID player;

    public ClientboundEldritchBlastPacket(UUID player) {
        this.player = player;
    }

    public static void encode(ClientboundEldritchBlastPacket object, FriendlyByteBuf buf) {
        buf.writeUUID(object.player);
    }

    public static ClientboundEldritchBlastPacket decode (FriendlyByteBuf buf) {
        return new ClientboundEldritchBlastPacket(buf.readUUID());
    }

    public static void handle(ClientboundEldritchBlastPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().level.getPlayerByUUID(packet.player);
            if (player != null) {
                SpellRegistry.ELDRITCH_BLAST.get().getCastStartAnimation().getForPlayer().ifPresent(anim -> {
                    ClientUtil.animatePlayerStart(player, anim);
                    player.playSound(SpellRegistry.ELDRITCH_BLAST.get().getCastFinishSound().get(), 0.9f, player.getRandom().nextFloat() * 0.625f + 0.625f);
                });
            }
        });
        ctx.get().setPacketHandled(true);

    }
}
