package aster.amo.erosianmagic.spellsnspellbooks.spells;

import aster.amo.erosianmagic.ErosianMagic;
import aster.amo.erosianmagic.spellsnspellbooks.spells.entity.FaerieFireAoe;
import aster.amo.erosianmagic.util.TargetingHelper;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.CastTargetingData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class ViciousMockerySpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ErosianMagic.MODID, "vicious_mockery");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 1)));
    }
    private float getDamage(int spellLevel, LivingEntity entity) {
        return getSpellPower(spellLevel, entity) * .5f;
    }
    public ViciousMockerySpell() {
        this.manaCostPerLevel = 2;
        this.baseSpellPower = 2;
        this.spellPowerPerLevel = 2;
        this.castTime = 2;
        this.baseManaCost = 5;
    }
    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundEvents.ILLUSIONER_CAST_SPELL);
    }
    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        TargetingHelper.preCastTargetHelper(level, entity, playerMagicData, this, 32, .35f, true, getRadius(spellLevel, entity));
        return true;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource source, MagicData playerMagicData) {
        if (playerMagicData.getAdditionalCastData() instanceof CastTargetingData castTargetingData) {
            LivingEntity target = castTargetingData.getTarget((ServerLevel) level);
            if (target != null) {
                DamageSources.applyDamage(target, getDamage(spellLevel, entity), getDamageSource(entity));
                for(int i = 0; i < spellLevel * 5; i++) {
                    ((ServerLevel) level).sendParticles(ParticleRegistry.DRAGON_FIRE_PARTICLE.get(), target.getX(), target.getY() + 1, target.getZ(), 3, 0.1, 0.25, 0.1, 0.1);
                    ((ServerLevel) level).sendParticles(ParticleRegistry.UNSTABLE_ENDER_PARTICLE.get(), target.getX(), target.getY() + 1, target.getZ(), 1, 0.1, 0.25, 0.1, 0.1);
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
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SchoolRegistry.ENDER_RESOURCE)
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
        return SpellAnimations.SPIT_FINISH_ANIMATION;
    }

    @Override
    public Vector3f getTargetingColor() {
        return new Vector3f(1, 1, 0);
    }
}
