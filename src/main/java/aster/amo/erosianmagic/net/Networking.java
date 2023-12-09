package aster.amo.erosianmagic.net;

import aster.amo.erosianmagic.bard.SongPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Networking {
    private static final String PROTOCOL_VERSION = "1.0";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("erosianmagic", "erosianmagic"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    static int id = 0;

    public static void init() {
        INSTANCE.registerMessage(id++, CombatTimerPacket.class, CombatTimerPacket::encode, CombatTimerPacket::decode, CombatTimerPacket::consume);
        INSTANCE.registerMessage(id++, SongPacket.class, SongPacket::encode, SongPacket::decode, SongPacket::consume);
    }

    public static <MSG> void sendToDimension(Level world, MSG msg, ResourceKey<Level> dimension) {
        INSTANCE.send(PacketDistributor.DIMENSION.with(() -> {
            return dimension;
        }), msg);
    }

    public static <MSG> void sendToTracking(Level world, BlockPos pos, MSG msg) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> {
            return world.getChunkAt(pos);
        }), msg);
    }

    public static <MSG> void sendTo(Player entity, MSG msg) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> {
            return (ServerPlayer)entity;
        }), msg);
    }

    public static <MSG> void sendToServer(MSG msg) {
        INSTANCE.sendToServer(msg);
    }
}
