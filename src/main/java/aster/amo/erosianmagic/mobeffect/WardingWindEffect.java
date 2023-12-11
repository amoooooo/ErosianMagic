package aster.amo.erosianmagic.mobeffect;

import com.aetherteam.aether.client.particle.AetherParticleTypes;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class WardingWindEffect extends MobEffect {
    public WardingWindEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int strength) {
        // spawn particles in an hourglass shape around the entity
        for(int i = 0; i < 4; i++) {
            Vec3 pos = entity.position();
            ParticleOptions particleOptions = AetherParticleTypes.PASSIVE_WHIRLWIND.get();
            float x = (float) (pos.x + (Math.random() * 2 - 1) * strength);
            float y = (float) (pos.y + (Math.random()) * strength);
            float z = (float) (pos.z + (Math.random() * 2 - 1) * strength);
            entity.level().addParticle(particleOptions, x, y, z, 0, 0, 0);
        }
        entity.level().getEntitiesOfClass(Projectile.class, entity.getBoundingBox().inflate(strength), entity1 -> entity1 instanceof Projectile).forEach(entity1 -> {
            if(entity1.getOwner() instanceof LivingEntity) {
                if(entity1.getOwner() == entity || Utils.shouldHealEntity(entity, (LivingEntity) entity1.getOwner())) {
                    Vec3 motion = entity1.getDeltaMovement();
                    float drag = 1 + (0.05f * strength);
                    entity1.setDeltaMovement(motion.x * drag, motion.y * drag, motion.z * drag);
                    return;
                }
            }
            Vec3 motion = entity1.getDeltaMovement();
            float drag = 1 - (0.05f * strength);
            entity1.setDeltaMovement(motion.x * drag, motion.y * drag, motion.z * drag);
        });
        super.applyEffectTick(entity, strength);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
