package aster.amo.erosianmagic.mobeffect;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.tslat.effectslib.api.ExtendedMobEffect;

public class RageEffect extends ExtendedMobEffect {
    public RageEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    public static final int[] RAGE_DURATION = new int[] {
            20*5, 20*10, 20*15, 20*20, 20*25
    };
    @Override
    public float modifyIncomingAttackDamage(LivingEntity entity, MobEffectInstance effectInstance, DamageSource source, float baseAmount) {
        // reduce base amount by mobeffect 10% per level
        effectInstance.duration = RAGE_DURATION[effectInstance.getAmplifier()];
        return baseAmount * (1 - 0.1f * effectInstance.getAmplifier());
    }

    @Override
    public float modifyOutgoingAttackDamage(LivingEntity entity, LivingEntity target, MobEffectInstance effectInstance, DamageSource source, float baseAmount) {
        // extend the MobEffectInstance time by 10% of the base duration per level
        effectInstance.duration = RAGE_DURATION[effectInstance.getAmplifier()];
        return baseAmount * (1 + 0.1f * effectInstance.getAmplifier());
    }
}
