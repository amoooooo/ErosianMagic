package aster.amo.erosianmagic.spellsnspellbooks.spells;

import aster.amo.erosianmagic.ErosianMagic;
import aster.amo.erosianmagic.cleric.ICleric;
import aster.amo.erosianmagic.util.IExtendedTargetArea;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.TargetAreaCastData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static io.redspace.ironsspellbooks.api.util.Utils.preCastTargetHelper;

@AutoSpellConfig
public class WordOfRecallSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ErosianMagic.MODID, "word_of_recall");

    public WordOfRecallSpell() {
        this.manaCostPerLevel = 20;
        this.baseSpellPower = 20;
        this.spellPowerPerLevel = 10;
        this.castTime = 100;
        this.baseManaCost = 350;
    }
    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.TELEKINESIS_LOOP.get());
    }
    @Override
    public void onServerPreCast(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        super.onServerPreCast(level, spellLevel, entity, playerMagicData);
        if (playerMagicData == null)
            return;
        if(entity instanceof Player player) {
            player.getCapability(ICleric.INSTANCE).ifPresent(cleric -> {
                if(!cleric.isChosenClass()) return;
                if(cleric.getRecallPos() != null) {
                    TargetedAreaEntity targetedAreaEntity = TargetedAreaEntity.createTargetAreaEntity(level, entity.position(), 12.0f, 0xFFFF00);
                    targetedAreaEntity.setOwner(entity);
                    playerMagicData.setAdditionalCastData(new TargetAreaCastData(entity.position(), targetedAreaEntity));
                }
            });
        }
    }

    @Override
    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
        if(entity == null) return 200;
        AtomicInteger castTime = new AtomicInteger(this.castTime);
        if(entity instanceof Player player) {
            player.getCapability(ICleric.INSTANCE).ifPresent(cleric -> {
                if(!cleric.isChosenClass()) return;
                if(cleric.getRecallPos() == null) {
                    castTime.set(30);
                }
            });
        }
        return castTime.get();
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return true;
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        if(entity instanceof Player player){
            player.getCapability(ICleric.INSTANCE).ifPresent(cleric -> {
                if (!cleric.isChosenClass()) return;
                if(cleric.getRecallPos() != null) {
                    for(int i = 0; i < 10; i++) {
                        //get random pos in the radius and spawn wisp particle
                        double x = player.getX() + (Math.random() * 24) - 12;
                        double y = player.getY() + (Math.random() * 24) - 12;
                        double z = player.getZ() + (Math.random() * 24) - 12;
                        double vx = (Math.random() * 0.2) - 0.1;
                        double vy = (Math.random() * 0.2) - 0.1;
                        double vz = (Math.random() * 0.2) - 0.1;
                        ((ServerLevel)level).sendParticles(ParticleRegistry.WISP_PARTICLE.get(), x, y, z, 1, vx, vy, vz, 0.1);
                    }
                }
            });
        }
        super.onServerCastTick(level, spellLevel, entity, playerMagicData);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource source, MagicData playerMagicData) {
        if(entity instanceof Player player) {
            player.getCapability(ICleric.INSTANCE).ifPresent(cleric -> {
                if(!cleric.isChosenClass()) return;
                if(cleric.getRecallPos() == null) {
                    cleric.setRecallPos(player.blockPosition());
                    player.sendSystemMessage(Component.literal("Word of Recall set!"));
                    TargetedAreaEntity target = TargetedAreaEntity.createTargetAreaEntity(level, player.position(), 2.0f, 0xFFFF00);
                    target.setOwner(player);
                    ((IExtendedTargetArea)target).erosianMagic$setEndless(true);
                } else {
                    player.teleportTo(cleric.getRecallPos().getX(), cleric.getRecallPos().getY(), cleric.getRecallPos().getZ());
                    List<LivingEntity> players = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(12.0), p -> Utils.shouldHealEntity(player, p));
                    players.forEach(p -> p.teleportTo(cleric.getRecallPos().getX(), cleric.getRecallPos().getY(), cleric.getRecallPos().getZ()));
                    List<TargetedAreaEntity> targets = player.level().getEntitiesOfClass(TargetedAreaEntity.class, new AABB(cleric.getRecallPos()).inflate(3.0), p -> p.getOwner() == player && ((IExtendedTargetArea)p).erosianMagic$isEndless());
                    targets.forEach(Entity::discard);
                    cleric.setRecallPos(null);
                    player.sendSystemMessage(Component.literal("Word of Recall used!"));
                }
            });
        }
        super.onCast(level, spellLevel, entity, source, playerMagicData);
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(300)
            .build();
    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }


    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_ANIMATION;
    }

    @Nullable
    private LivingEntity findTarget(LivingEntity caster) {
        var target = Utils.raycastForEntity(caster.level(), caster, 32, true, 0.35f);
        if (target instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity livingTarget) {
            return livingTarget;
        } else {
            return null;
        }
    }
}
