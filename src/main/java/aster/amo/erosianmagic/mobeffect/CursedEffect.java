package aster.amo.erosianmagic.mobeffect;

import com.aetherteam.aether.client.particle.AetherParticleTypes;
import elucent.eidolon.client.particle.GenericParticleData;
import elucent.eidolon.client.particle.Particles;
import elucent.eidolon.client.particle.WispParticle;
import elucent.eidolon.client.particle.WispParticleType;
import elucent.eidolon.registries.EidolonParticles;
import elucent.eidolon.registries.Registry;
import foundry.alembic.damage.AlembicDamageHandler;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.tslat.effectslib.api.ExtendedMobEffect;
import org.jetbrains.annotations.Nullable;

public class CursedEffect extends ExtendedMobEffect {
    public CursedEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void afterIncomingAttack(LivingEntity entity, MobEffectInstance effectInstance, DamageSource source, float amount) {
        if(entity.level().isClientSide) {
            super.afterIncomingAttack(entity, effectInstance, source, amount);
            return;
        }
        if(source.getEntity() instanceof LivingEntity le) {
            Holder<DamageType> type = entity.level().registryAccess().registry(Registries.DAMAGE_TYPE).flatMap(registry -> registry.getHolder(ISSDamageTypes.ELDRITCH_MAGIC)).get();
            entity.hurt(new DamageSource(type, le), amount * 0.2f);
            // this entity bounds
            for(int i = 0; i < 25; i++){
                int modifier = (int) Math.floor(Math.random()) == 0 ? -1 : 1;
                Vec3 pos = entity.position().add(entity.getBoundingBox().getXsize() * modifier, entity.getBoundingBox().getYsize() * modifier, entity.getBoundingBox().getZsize() * modifier);
                Particles.create(EidolonParticles.WISP_PARTICLE)
                        .setAlpha(0.5f, 0.0f).setScale(0.3f)
                        .setLifetime(20).setColor(0.5f, 0.0f, 1.0f)
                        .enableGravity().randomVelocity(0.1f)
                        .spawn(((ServerLevel)entity.level()), pos.x, pos.y, pos.z);
            }
        }
        super.afterIncomingAttack(entity, effectInstance, source, amount);
    }

    @Override
    public void tick(LivingEntity entity, @Nullable MobEffectInstance effectInstance, int amplifier) {
        if(entity.level().isClientSide) {
            super.tick(entity, effectInstance, amplifier);
            return;
        }
        for(int i = 0; i < 5; i++){
            float angle = (float) (Math.random() * 2 * Math.PI);
            Vec3 pos = entity.position().add(Math.cos(angle), 0, Math.sin(angle));
            // get random position around the entity's bounding box
            AABB box = entity.getBoundingBox();
            Vec3 offset = new Vec3(Math.random() * box.getXsize(), Math.random() * box.getYsize(), Math.random() * box.getZsize());
            pos = pos.add(offset);
            Particles.create(EidolonParticles.WISP_PARTICLE)
                    .setAlpha(0.5f, 0.0f).setScale(0.3f)
                    .setLifetime(3).setColor(0.5f, 0.0f, 1.0f)
                    .spawn((ServerLevel)entity.level(), pos.x, pos.y, pos.z);
        }
        super.tick(entity, effectInstance, amplifier);
    }


    @Override
    public boolean shouldTickEffect(@Nullable MobEffectInstance effectInstance, @Nullable LivingEntity entity, int ticksRemaining, int amplifier) {
        return true;
    }
}
