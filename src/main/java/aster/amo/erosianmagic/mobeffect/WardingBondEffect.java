package aster.amo.erosianmagic.mobeffect;

import aster.amo.erosianmagic.registry.MobEffectRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.tslat.effectslib.api.ExtendedMobEffect;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WardingBondEffect extends ExtendedMobEffect {
    public WardingBondEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public float modifyIncomingAttackDamage(LivingEntity entity, MobEffectInstance effectInstance, DamageSource source, float baseAmount) {
        List<LivingEntity> entities = entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(12.0f), e -> Utils.shouldHealEntity(entity, e) && e.hasEffect(MobEffectRegistry.WARDING_BOND.get()));
        baseAmount = baseAmount / Math.max(1, entities.size());
        for(LivingEntity e : entities) {
            if(Utils.shouldHealEntity(entity, e)){
                e.hurt(source, baseAmount);
                ((ServerLevel)entity.level()).sendParticles(ParticleTypes.ELECTRIC_SPARK, e.getX(), e.getEyeY(), e.getZ(), 3, 0.5, 0.5, 0.5, 0.1);
            }
        }
        return super.modifyIncomingAttackDamage(entity, effectInstance, source, baseAmount);
    }
}
