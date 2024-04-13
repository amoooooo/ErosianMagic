package aster.amo.erosianmagic.net.classsync;

import aster.amo.erosianmagic.fighter.champion.IChampion;
import aster.amo.erosianmagic.fighter.paladin.IPaladin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ChampionSyncPacket {
    CompoundTag tag;
    UUID uuid;

    public ChampionSyncPacket(CompoundTag tag, UUID uuid) {
        this.tag = tag;
        this.uuid = uuid;
    }

    public static void encode(ChampionSyncPacket object, FriendlyByteBuf buffer) {
        buffer.writeNbt(object.tag);
        buffer.writeUUID(object.uuid);
    }

    public static ChampionSyncPacket decode(FriendlyByteBuf buffer) {
        return new ChampionSyncPacket(buffer.readNbt(), buffer.readUUID());
    }

    public static void handle(ChampionSyncPacket object, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = context.get().getSender().level().getPlayerByUUID(object.uuid);
            if(player != null) {
                player.getCapability(IChampion.INSTANCE).ifPresent((k) -> ((INBTSerializable<CompoundTag>) k).deserializeNBT(object.tag));
            }
        });
        context.get().setPacketHandled(true);
    }
}
