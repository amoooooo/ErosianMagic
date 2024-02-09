package aster.amo.erosianmagic.util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class BossUtil {
    public static void sendBossDefeatedTitle(ServerPlayer player, BlockPos pos) {
        ServerGamePacketListenerImpl connection = player.connection;
        connection.send(new ClientboundSetTitlesAnimationPacket(60, 100, 60));
        connection.send(new ClientboundSetSubtitleTextPacket(Component.literal("LIFE EXTINGUISHED").withStyle(s -> s.withColor(ChatFormatting.DARK_RED))));
        connection.send(new ClientboundSetTitleTextPacket(Component.literal("0-./").withStyle(s -> s.withFont(new ResourceLocation("runelic:runelic")).withColor(ChatFormatting.DARK_RED))));
        connection.send(new ClientboundSoundPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.RAVAGER_ROAR), SoundSource.MASTER, pos.getX(), pos.getY(), pos.getZ(), 5.0f, 0.2f, 42L));
    }
}
