package aster.amo.erosianmagic.mobeffect;

import aster.amo.erosianmagic.registry.MobEffectRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.tslat.effectslib.api.ExtendedMobEffect;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BeaconOfHopeEffect extends ExtendedMobEffect {
    public BeaconOfHopeEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void onApplication(@Nullable MobEffectInstance effectInstance, @Nullable Entity source, LivingEntity entity, int amplifier) {
        super.onApplication(effectInstance, source, entity, amplifier);
        if(entity.level().isClientSide) return;
        TargetedAreaEntity area = TargetedAreaEntity.createTargetAreaEntity(entity.level(), entity.position(), 2 * amplifier, 0xacac00);
        area.setOwner(entity);
        area.setDuration(effectInstance.getDuration());
    }

    @Override
    public void tick(LivingEntity entity, @Nullable MobEffectInstance effectInstance, int amplifier) {
        if(entity.level().isClientSide) return;
        List<LivingEntity> entities = entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(2 * amplifier));
        for(LivingEntity e : entities) {
            if(Utils.shouldHealEntity(entity, e)){
                e.addEffect(new MobEffectInstance(MobEffectRegistry.HOPEFUL.get(), 2, 0, false, false));
            }
        }
        for (int i = 0; i < 3; i++) {
            double x = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 2 * amplifier;
            double y = Utils.findRelativeGroundLevel(entity.level(), entity.position(), (int) ((entity.getBbHeight()*2.5f) - entity.getY()));
            double z = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 2 * amplifier;
            ((ServerLevel) entity.level()).sendParticles(ParticleRegistry.WISP_PARTICLE.get(), x, y, z, 1, 0, 0, 0.25, 0);
        }
        super.tick(entity, effectInstance, amplifier);
    }

    @Override
    public boolean shouldTickEffect(@Nullable MobEffectInstance effectInstance, @Nullable LivingEntity entity, int ticksRemaining, int amplifier) {
        return true;
    }
}
