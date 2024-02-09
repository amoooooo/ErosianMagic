package aster.amo.erosianmagic.spellsnspellbooks.spells;

import aster.amo.erosianmagic.ErosianMagic;
import aster.amo.erosianmagic.registry.MobEffectRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.CastTargetingData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static io.redspace.ironsspellbooks.api.util.Utils.preCastTargetHelper;

@AutoSpellConfig
public class SacredFlameSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ErosianMagic.MODID, "sacred_flame");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 1)));
    }

    public SacredFlameSpell() {
        this.manaCostPerLevel = 2;
        this.baseSpellPower = 12;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 10;
    }
    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.HOLY_CAST.get());
    }
    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return preCastTargetHelper(level, entity, playerMagicData, this, 32, .35f);
    }
    private float getDamage(int spellLevel, LivingEntity entity) {
        return getSpellPower(spellLevel, entity) * .5f;
    }
    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource source, MagicData playerMagicData) {
        if (playerMagicData.getAdditionalCastData() instanceof CastTargetingData castTargetingData) {
            LivingEntity target = castTargetingData.getTarget((ServerLevel) level);
            if (target != null) {
                DamageSources.applyDamage(target, getDamage(spellLevel, entity), getDamageSource(entity));
                for(int i = 0; i < spellLevel * 5; i++) {
                    ((ServerLevel) level).sendParticles(ParticleRegistry.EMBER_PARTICLE.get(), target.getX(), target.getY() + 1, target.getZ(), 3, 0.1, 0.25, 0.1, 0.1);
                    ((ServerLevel) level).sendParticles(ParticleRegistry.WISP_PARTICLE.get(), target.getX(), target.getY() + 1, target.getZ(), 1, 0.1, 0.25, 0.1, 0.1);
                }
            }
        }
        super.onCast(level, spellLevel, entity, source, playerMagicData);
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(2)
            .build();
    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }


    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ANIMATION_INSTANT_CAST;
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
