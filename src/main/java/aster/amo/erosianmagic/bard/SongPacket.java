package aster.amo.erosianmagic.bard;

import aster.amo.erosianmagic.bard.song.Song;
import aster.amo.erosianmagic.bard.song.SongRegistry;
import aster.amo.erosianmagic.client.ErosianMagicClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SongPacket {
    final ResourceLocation song;

    public SongPacket(ResourceLocation song) {
        this.song = song;
    }

    public static void encode(SongPacket object, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(object.song);
    }

    public static SongPacket decode(FriendlyByteBuf buffer) {
        return new SongPacket(buffer.readResourceLocation());
    }

    public static void consume(SongPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Song song = SongRegistry.get(packet.song);
            if(song != null) {
                song.play(ctx.get().getSender());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
