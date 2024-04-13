package aster.amo.erosianmagic.net;

import aster.amo.erosianmagic.mage.bard.song.SongPacket;
import aster.amo.erosianmagic.net.classsync.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class Networking {
    private static final String PROTOCOL_VERSION = "1.0";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("erosianmagic", "erosianmagic"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    static int id = 0;

    public static void init() {
        INSTANCE.registerMessage(id++, CombatTimerPacket.class, CombatTimerPacket::encode, CombatTimerPacket::decode, CombatTimerPacket::consume, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, SongPacket.class, SongPacket::encode, SongPacket::decode, SongPacket::consume, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(id++, BardSyncPacket.class, BardSyncPacket::encode, BardSyncPacket::decode, BardSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, ClericSyncPacket.class, ClericSyncPacket::encode, ClericSyncPacket::decode, ClericSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, WitchSyncPacket.class, WitchSyncPacket::encode, WitchSyncPacket::decode, WitchSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, ClientboundSyncMultiTargetingData.class, ClientboundSyncMultiTargetingData::encode, ClientboundSyncMultiTargetingData::decode, ClientboundSyncMultiTargetingData::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, ClientboundClassPacket.class, ClientboundClassPacket::encode, ClientboundClassPacket::decode, ClientboundClassPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, RollSyncPacket.class, RollSyncPacket::encode, RollSyncPacket::decode, RollSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, ClientboundEldritchBlastPacket.class, ClientboundEldritchBlastPacket::encode, ClientboundEldritchBlastPacket::decode, ClientboundEldritchBlastPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, MachinistSyncPacket.class, MachinistSyncPacket::encode, MachinistSyncPacket::decode, MachinistSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, MageSyncPacket.class, MageSyncPacket::encode, MageSyncPacket::decode, MageSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, RogueSyncPacket.class, RogueSyncPacket::encode, RogueSyncPacket::decode, RogueSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, ClientboundInsultPacket.class, ClientboundInsultPacket::encode, ClientboundInsultPacket::decode, ClientboundInsultPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, FighterSyncPacket.class, FighterSyncPacket::encode, FighterSyncPacket::decode, FighterSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, BarbarianSyncPacket.class, BarbarianSyncPacket::encode, BarbarianSyncPacket::decode, BarbarianSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, ClientboundUpdateSpellsPacket.class, ClientboundUpdateSpellsPacket::encode, ClientboundUpdateSpellsPacket::decode, ClientboundUpdateSpellsPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, PaladinSyncPacket.class, PaladinSyncPacket::encode, PaladinSyncPacket::decode, PaladinSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, ChampionSyncPacket.class, ChampionSyncPacket::encode, ChampionSyncPacket::decode, ChampionSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, DivineSyncPacket.class, DivineSyncPacket::encode, DivineSyncPacket::decode, DivineSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, WizardSyncPacket.class, WizardSyncPacket::encode, WizardSyncPacket::decode, WizardSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, CharlatanSyncPacket.class, CharlatanSyncPacket::encode, CharlatanSyncPacket::decode, CharlatanSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, MonkSyncPacket.class, MonkSyncPacket::encode, MonkSyncPacket::decode, MonkSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(id++, RangerSyncPacket.class, RangerSyncPacket::encode, RangerSyncPacket::decode, RangerSyncPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
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
