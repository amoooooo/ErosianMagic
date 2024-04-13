package aster.amo.erosianmagic.mage.bard;

import aster.amo.erosianmagic.net.classsync.BardSyncPacket;
import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.registry.MobEffectRegistry;
import aster.amo.erosianmagic.spellsnspellbooks.ClassSpells;
import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.client.handlers.party.PartyClientApiImpl;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.function.Supplier;

public class Bard implements IBard, INBTSerializable<CompoundTag> {
    int inspirationTime;
    boolean inspiring;
    boolean isBard;
    int entityId;
    int level = 1;

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
        if(!player.level().isClientSide())
            Networking.sendToTracking(player.level(), player.blockPosition(), new BardSyncPacket(serializeNBT(), player.getUUID()));
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
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
        tag.putBoolean("isBard", isBard);
        tag.putInt("level", level);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        isBard = nbt.getBoolean("isBard");
        inspirationTime = nbt.getInt("inspirationTime");
        inspiring = nbt.getBoolean("inspiring");
        entityId = nbt.getInt("entityId");
        isBard = nbt.getBoolean("isBard");
        level = nbt.getInt("level");
    }

    @Override
    public void setChosenClass(boolean isClass, Player player) {
        isBard = isClass;
        if(isClass) {
            List<Supplier<AbstractSpell>> spells = ClassSpells.CLASS_SPELLS.get("Bard");
            MagicData.getPlayerMagicData(player).getSyncedData().forgetAllSpells();
//            spells.forEach(spell -> MagicData.getPlayerMagicData(player).getSyncedData().learnSpell(spell.get()));
        }
    }

    @Override
    public boolean isChosenClass() {
        return isBard;
    }
}
