package aster.amo.erosianmagic.net.classsync;

import aster.amo.erosianmagic.divine.cleric.ICleric;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ClericSyncPacket {
    CompoundTag tag;
    UUID uuid;

    public ClericSyncPacket(CompoundTag tag, UUID uuid) {
        this.tag = tag;
        this.uuid = uuid;
    }

    public static void encode(ClericSyncPacket object, FriendlyByteBuf buffer) {
        buffer.writeNbt(object.tag);
        buffer.writeUUID(object.uuid);
    }

    public static ClericSyncPacket decode(FriendlyByteBuf buffer) {
        return new ClericSyncPacket(buffer.readNbt(), buffer.readUUID());
    }

    public static void handle(ClericSyncPacket object, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = context.get().getSender().level().getPlayerByUUID(object.uuid);
            if(player != null) {
                player.getCapability(ICleric.INSTANCE).ifPresent((k) -> {
                    ((INBTSerializable<CompoundTag>) k).deserializeNBT(object.tag);
                    Minecraft.getInstance().player.sendSystemMessage(Component.nullToEmpty("isChosenClass: " + k.isChosenClass()));
                });
            }
        });
        context.get().setPacketHandled(true);
    }
}
