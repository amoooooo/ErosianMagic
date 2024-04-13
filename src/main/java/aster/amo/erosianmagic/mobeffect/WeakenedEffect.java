package aster.amo.erosianmagic.mobeffect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.tslat.effectslib.api.ExtendedMobEffect;

public class WeakenedEffect extends ExtendedMobEffect {
    public WeakenedEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public float modifyIncomingAttackDamage(LivingEntity entity, MobEffectInstance effectInstance, DamageSource source, float baseAmount) {
        // increase base amount by mobeffect 10% per level
        return baseAmount * (1 + 0.1f * effectInstance.getAmplifier());
    }
}
