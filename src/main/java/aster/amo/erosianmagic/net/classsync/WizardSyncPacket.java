package aster.amo.erosianmagic.net.classsync;

import aster.amo.erosianmagic.fighter.champion.IChampion;
import aster.amo.erosianmagic.mage.wizard.IWizard;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class WizardSyncPacket {
    CompoundTag tag;
    UUID uuid;

    public WizardSyncPacket(CompoundTag tag, UUID uuid) {
        this.tag = tag;
        this.uuid = uuid;
    }

    public static void encode(WizardSyncPacket object, FriendlyByteBuf buffer) {
        buffer.writeNbt(object.tag);
        buffer.writeUUID(object.uuid);
    }

    public static WizardSyncPacket decode(FriendlyByteBuf buffer) {
        return new WizardSyncPacket(buffer.readNbt(), buffer.readUUID());
    }

    public static void handle(WizardSyncPacket object, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Player player = context.get().getSender().level().getPlayerByUUID(object.uuid);
            if(player != null) {
                player.getCapability(IWizard.INSTANCE).ifPresent((k) -> ((INBTSerializable<CompoundTag>) k).deserializeNBT(object.tag));
            }
        });
        context.get().setPacketHandled(true);
    }
}
