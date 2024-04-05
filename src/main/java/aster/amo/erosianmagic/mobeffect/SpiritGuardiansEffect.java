package aster.amo.erosianmagic.mobeffect;

import com.aetherteam.aether.client.particle.AetherParticleTypes;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import net.tslat.effectslib.api.ExtendedMobEffect;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SpiritGuardiansEffect extends ExtendedMobEffect {
    public SpiritGuardiansEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean doClientSideEffectTick(MobEffectInstance effectInstance, LivingEntity entity) {
        return super.doClientSideEffectTick(effectInstance, entity);
    }

    @Override
    public void tick(LivingEntity entity, @Nullable MobEffectInstance effectInstance, int strength) {
        entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(strength), entity1 -> entity1 instanceof LivingEntity).forEach(entity1 -> {
            if(!Utils.shouldHealEntity(entity, entity1)) {
                entity.hurt(DamageSources.get(entity.level(), ISSDamageTypes.HOLY_MAGIC), strength);
            }
        });
        super.tick(entity, effectInstance, strength);
    }

    @Override
    public boolean shouldTickEffect(@Nullable MobEffectInstance effectInstance, @Nullable LivingEntity entity, int ticksRemaining, int amplifier) {
        return true;
    }
}
