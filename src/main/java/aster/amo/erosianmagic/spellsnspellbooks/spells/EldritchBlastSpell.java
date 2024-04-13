package aster.amo.erosianmagic.spellsnspellbooks.spells;

import aster.amo.erosianmagic.net.ClientboundEldritchBlastPacket;
import aster.amo.erosianmagic.net.Networking;
import aster.amo.erosianmagic.util.DelayHandler;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.eldritch_blast.EldritchBlastVisualEntity;
import io.redspace.ironsspellbooks.network.ClientboundSyncAnimation;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.spells.eldritch.AbstractEldritchSpell;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Max;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class EldritchBlastSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation("erosianmagic", "e_blast");
    private final DefaultConfig defaultConfig;

    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage",
                Utils.stringTruncation((double)this.getDamage(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.blast_count", spellLevel),
                Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation((double)getRange(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.casting_time", this.getEffectiveCastTime(spellLevel, caster))
        );
    }

    public EldritchBlastSpell() {
        this.defaultConfig = (new DefaultConfig()).setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE).setMaxLevel(5).setCooldownSeconds(1.2).build();
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 2;
        this.castTime = 10;
        this.baseManaCost = 3;
    }

    public int getCastTime(int spellLevel) {
        return this.castTime - spellLevel;
    }

    public CastType getCastType() {
        return CastType.INSTANT;
    }

    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of((SoundEvent) SoundRegistry.RAY_OF_FROST.get());
    }

    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource source, MagicData playerMagicData) {
        fireBlast(level, spellLevel, entity);
        int delay = this.getEffectiveCastTime(spellLevel, entity) * 5;
        for(int i = 0; i < spellLevel-1; i++) {
            DelayHandler.addDelay(i * delay, () -> {
                fireBlast(level, spellLevel, entity);
            });
        }
        super.onCast(level, spellLevel, entity, source, playerMagicData);
    }

    private void fireBlast(Level level, int spellLevel, LivingEntity entity) {
        Networking.sendToTracking(level, entity.blockPosition(), new ClientboundEldritchBlastPacket(entity.getUUID()));
        HitResult hitResult = Utils.raycastForEntity(level, entity, getRange(spellLevel, entity), true, 0.15F);
        level.addFreshEntity(new EldritchBlastVisualEntity(level, entity.position().add(0.0, 0.5, 0.0), hitResult.getLocation(), entity));
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            Entity target = ((EntityHitResult)hitResult).getEntity();
            if (target instanceof LivingEntity) {
                DamageSources.applyDamage(target, this.getDamage(spellLevel, entity), this.getDamageSource(entity));
            }
        }
        MagicManager.spawnParticles(level, ParticleHelper.UNSTABLE_ENDER, hitResult.getLocation().x, hitResult.getLocation().y, hitResult.getLocation().z, 50, 0.0, 0.0, 0.0, 0.3, false);
    }

    public static float getRange(int level, LivingEntity caster) {
        return 30.0F;
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, caster);
    }

    private int getFreezeTime(int spellLevel, LivingEntity caster) {
        return (int)(this.getSpellPower(spellLevel, caster) * 30.0F);
    }
}