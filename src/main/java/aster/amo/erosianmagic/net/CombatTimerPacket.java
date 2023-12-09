package aster.amo.erosianmagic.net;

import aster.amo.erosianmagic.client.ErosianMagicClient;
import elucent.eidolon.Eidolon;
import elucent.eidolon.client.particle.Particles;
import elucent.eidolon.network.FlameEffectPacket;
import elucent.eidolon.registries.EidolonParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CombatTimerPacket {
    final long time;

    public CombatTimerPacket(long time) {
        this.time = time;
    }

    public static void encode(CombatTimerPacket object, FriendlyByteBuf buffer) {
        buffer.writeLong(object.time);
    }

    public static CombatTimerPacket decode(FriendlyByteBuf buffer) {
        return new CombatTimerPacket(buffer.readLong());
    }

    public static void consume(CombatTimerPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ErosianMagicClient.lastCombatTime = packet.time;
        });
        ctx.get().setPacketHandled(true);
    }
}
