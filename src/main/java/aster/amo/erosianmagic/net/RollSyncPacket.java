package aster.amo.erosianmagic.net;

import aster.amo.erosianmagic.rolls.IRoller;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RollSyncPacket {
    CompoundTag tag;
    int id;

    public RollSyncPacket(CompoundTag tag, int uuid) {
        this.tag = tag;
        this.id = uuid;
    }

    public static void encode(RollSyncPacket object, FriendlyByteBuf buffer) {
        buffer.writeNbt(object.tag);
        buffer.writeInt(object.id);
    }

    public static RollSyncPacket decode(FriendlyByteBuf buffer) {
        return new RollSyncPacket(buffer.readNbt(), buffer.readInt());
    }

    public static void handle(RollSyncPacket object, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(object.id);
            if(entity instanceof LivingEntity livingEntity) {
                livingEntity.getCapability(IRoller.INSTANCE).ifPresent((k) -> {
                    boolean wasDisplayingRoll = k.shouldDisplayRoll();
                    ((INBTSerializable<CompoundTag>) k).deserializeNBT(object.tag);
                    if(!wasDisplayingRoll && k.shouldDisplayRoll()) {
                        entity.playSound(SoundEvent.createVariableRangeEvent(new ResourceLocation("erosianmagic", "roll")), 1.0f, ((LivingEntity) entity).getRandom().nextFloat() * 0.675f + 0.675f);
                    }
                });
            }

        });
        context.get().setPacketHandled(true);
    }
}
