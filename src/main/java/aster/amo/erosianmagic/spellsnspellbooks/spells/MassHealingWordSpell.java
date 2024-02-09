package aster.amo.erosianmagic.spellsnspellbooks.spells;

import aster.amo.erosianmagic.ErosianMagic;
import aster.amo.erosianmagic.registry.MobEffectRegistry;
import aster.amo.erosianmagic.spellsnspellbooks.data.CastMultiTargetingData;
import aster.amo.erosianmagic.util.TargetingHelper;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class MassHealingWordSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ErosianMagic.MODID, "mass_healing_word");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.healing", getSpellPower(spellLevel, caster) * 0.05f),
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(getRadius(spellLevel, caster), 1))
        );
    }

    public MassHealingWordSpell() {
        this.manaCostPerLevel = 20;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 2;
        this.castTime = 0;
        this.baseManaCost = 100;
    }
    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.HOLY_CAST.get());
    }
    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        TargetingHelper.preCastTargetHelperFriendly(level, entity, playerMagicData, this, 32, .35f, false, getRadius(spellLevel, entity));
        return true;
    }
    private float getDamage(int spellLevel, LivingEntity entity) {
        return getSpellPower(spellLevel, entity) * .5f;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource source, MagicData playerMagicData) {
        if (playerMagicData.getAdditionalCastData() instanceof CastMultiTargetingData multiData) {
            multiData.getTargets().forEach(t -> {
                LivingEntity target = ((LivingEntity)((ServerLevel)level).getEntity(t));
                target.heal(getDamage(spellLevel, entity));
                for(int i = 0; i < spellLevel * 5; i++) {
                    ((ServerLevel) level).sendParticles(ParticleRegistry.WISP_PARTICLE.get(), target.getX(), target.getY() + 1, target.getZ(), 4, 0.1, 0.25, 0.1, 0.1);
                    ((ServerLevel) level).sendParticles(ParticleTypes.SCRAPE, target.getX(), target.getY() + 1, target.getZ(), 2, 0.1, 0.25, 0.1, 0.1);
                }
            });
            LivingEntity mainTarget = ((LivingEntity)((ServerLevel)level).getEntity(multiData.getMainTarget()));
            if(mainTarget != null){
                mainTarget.heal(getDamage(spellLevel, entity));
                for(int i = 0; i < spellLevel * 5; i++) {
                    ((ServerLevel) level).sendParticles(ParticleRegistry.WISP_PARTICLE.get(), mainTarget.getX(), mainTarget.getY() + 1, mainTarget.getZ(), 4, 0.1, 0.25, 0.1, 0.1);
                    ((ServerLevel) level).sendParticles(ParticleTypes.SCRAPE, mainTarget.getX(), mainTarget.getY() + 1, mainTarget.getZ(), 2, 0.1, 0.25, 0.1, 0.1);
                }
            }
        }



        super.onCast(level, spellLevel, entity, source, playerMagicData);
    }

    private float getRadius(int spellLevel, LivingEntity caster) {
        return 1 + spellLevel * 2;
    }

    private int getDuration(int spellLevel, LivingEntity caster) {
        return 200 + spellLevel * 20;
    }
    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(25)
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

    @Override
    public Vector3f getTargetingColor() {
        return new Vector3f(1, 1, 0);
    }
}
