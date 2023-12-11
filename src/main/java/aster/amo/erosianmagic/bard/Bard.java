package aster.amo.erosianmagic.bard;

import aster.amo.erosianmagic.client.ErosianMagicClient;
import aster.amo.erosianmagic.net.BardSyncPacket;
import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.registry.MobEffectRegistry;
import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.client.handlers.party.PartyClientApiImpl;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.spells.TargetAreaCastData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public class Bard implements IBard, INBTSerializable<CompoundTag> {
    int inspirationTime;
    boolean inspiring;
    boolean isBard;
    int entityId;

    @Override
    public int getInspirationTime() {
        return inspirationTime;
    }

    @Override
    public void setInspirationTime(int time) {
        inspirationTime = time;
    }

    @Override
    public boolean isInspiring() {
        return inspiring;
    }

    @Override
    public void sync(Player player) {
        if(!player.level().isClientSide()) Networking.sendToTracking(player.level(), player.blockPosition(), new BardSyncPacket(serializeNBT(), player.getUUID()));
    }

    @Override
    public void tick(Player bard) {
        if(inspirationTime > 0) {
            inspiring = true;
            inspirationTime--;
        } else {
            inspiring = false;
            if(entityId != 0) {
                Entity e = bard.level().getEntity(entityId);
                if(e != null) {
                    e.discard();
                }
                entityId = 0;
            }
        }
        if(inspiring) {
            // get all nearby players within 8 blocks
            List<Player> players = bard.level().getEntitiesOfClass(Player.class, bard.getBoundingBox().inflate(12));
            Party party = PartyClientApiImpl.API.getPlayerParty(bard.getUUID());
            if(party == null) {
                players.forEach(player -> {
                    player.addEffect(new MobEffectInstance(MobEffectRegistry.BARDIC_INSPIRATION.get(), 120, 0, false, false, true));
                });
            } else {
                players.forEach(player -> {
                    if(party.members().isMember(player.getUUID())) {
                        player.addEffect(new MobEffectInstance(MobEffectRegistry.BARDIC_INSPIRATION.get(), 120, 0, false, false, true));
                    }
                });
            }
            // spawn note particles randomly inside the radius
            for (int i = 0; i < 3; i++) {
                double x = bard.getX() + (bard.getRandom().nextDouble() - 0.5) * 12;
                double y = Utils.findRelativeGroundLevel(bard.level(), bard.position(), (int) ((bard.getBbHeight()*2.5f) - bard.getY()));
                double z = bard.getZ() + (bard.getRandom().nextDouble() - 0.5) * 12;
                ((ServerLevel) bard.level()).sendParticles(ParticleTypes.NOTE, x, y, z, 1, 0, 0, 0, 0);
            }
            if(entityId == 0) {
                TargetedAreaEntity targetedAreaEntity = TargetedAreaEntity.createTargetAreaEntity(bard.level(), bard.position(), 12.0f, 16239960);
                targetedAreaEntity.setOwner(bard);
                bard.level().addFreshEntity(targetedAreaEntity);
                entityId = targetedAreaEntity.getId();
            }
        }
        sync(bard);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isBard", isBard);
        tag.putInt("inspirationTime", inspirationTime);
        tag.putBoolean("inspiring", inspiring);
        tag.putInt("entityId", entityId);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        isBard = nbt.getBoolean("isBard");
        inspirationTime = nbt.getInt("inspirationTime");
        inspiring = nbt.getBoolean("inspiring");
        entityId = nbt.getInt("entityId");
    }

    @Override
    public void setChosenClass(boolean isClass) {
        isBard = isClass;
    }

    @Override
    public boolean isChosenClass() {
        return isBard;
    }
}
