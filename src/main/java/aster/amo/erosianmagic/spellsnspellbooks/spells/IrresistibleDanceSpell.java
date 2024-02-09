package aster.amo.erosianmagic.spellsnspellbooks.spells;

import aster.amo.erosianmagic.ErosianMagic;
import aster.amo.erosianmagic.registry.MobEffectRegistry;
import aster.amo.erosianmagic.spellsnspellbooks.data.CastMultiTargetingData;
import aster.amo.erosianmagic.spellsnspellbooks.spells.entity.FaerieFireAoe;
import aster.amo.erosianmagic.util.TargetingHelper;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.CastTargetingData;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
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
public class IrresistibleDanceSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(ErosianMagic.MODID, "irresistible_dance");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(getRadius(spellLevel, caster), 1))
        );
    }

    public IrresistibleDanceSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 30;
        this.spellPowerPerLevel = 10;
        this.castTime = 50;
        this.baseManaCost = 50;
    }
    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.CLOUD_OF_REGEN_LOOP.get());
    }
    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        TargetingHelper.preCastTargetHelper(level, entity, playerMagicData, this, 32, .35f, false, getRadius(spellLevel, entity));
        return true;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource source, MagicData playerMagicData) {
        int duration = getDuration(spellLevel, entity);
        float radius = getRadius(spellLevel, entity);

        if(playerMagicData.getAdditionalCastData() instanceof CastMultiTargetingData multiTargetingData) {
            multiTargetingData.getTargets().forEach(target -> {
                LivingEntity targetEntity = (LivingEntity) ((ServerLevel)level).getEntity(target);
                if(!Utils.shouldHealEntity(entity, targetEntity) && targetEntity != null) {
                    targetEntity.addEffect(new MobEffectInstance(MobEffectRegistry.IRRISISTIBLE_DANCE.get(), duration, 0, false, false, true));
                }
            });
            LivingEntity mainTarget = (LivingEntity) ((ServerLevel)level).getEntity(multiTargetingData.getMainTarget());
            if(mainTarget != null) {
                mainTarget.addEffect(new MobEffectInstance(MobEffectRegistry.IRRISISTIBLE_DANCE.get(), duration, 0, false, false, true));
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
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
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
        return SpellAnimations.CHARGE_WAVY_ANIMATION;
    }

    @Override
    public Vector3f getTargetingColor() {
        return new Vector3f(1, 1, 0);
    }
}
