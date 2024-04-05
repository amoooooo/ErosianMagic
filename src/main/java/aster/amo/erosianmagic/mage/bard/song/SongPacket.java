package aster.amo.erosianmagic.mage.bard.song;

import aster.amo.erosianmagic.mage.bard.IBard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SongPacket {
    final ResourceLocation song;
    final boolean perfectTime;

    public SongPacket(ResourceLocation song, boolean perfectTime) {
        this.song = song;
        this.perfectTime = perfectTime;
    }

    public static void encode(SongPacket object, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(object.song);
        buffer.writeBoolean(object.perfectTime);
    }

    public static SongPacket decode(FriendlyByteBuf buffer) {
        return new SongPacket(buffer.readResourceLocation(), buffer.readBoolean());
    }

    public static void consume(SongPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Song song = SongRegistry.get(packet.song);
            ResourceLocation id = SongRegistry.getId(song);
            if(song != null) {
                song.play(ctx.get().getSender(), packet.perfectTime);
                Player player = ctx.get().getSender();
                player.getCapability(IBard.INSTANCE).ifPresent(bard -> {
                    bard.setInspirationTime(120);
                    bard.sync(player);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
