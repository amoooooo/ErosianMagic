package aster.amo.erosianmagic.util;

import aster.amo.erosianmagic.registry.EntityRegistry;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.function.Consumer;

public class AoeEffectEntity extends AoeEntity {
    private final Consumer<LivingEntity> effect;
    private final int color;
    private int particleCount;
    private ParticleOptions particle;
    public AoeEffectEntity(Level pLevel, int color, int particleCount, ParticleOptions particle, Consumer<LivingEntity> effect) {
        super(EntityRegistry.AOE_EFFECT.get(), pLevel);
        this.effect = effect;
        this.color = color;
        this.particleCount = particleCount;
        this.particle = particle;
    }

    public AoeEffectEntity(EntityType<AoeEffectEntity> aoeEffectEntityEntityType, Level level) {
        super(aoeEffectEntityEntityType, level);
        this.effect = null;
        this.color = 0;
        this.particleCount = 0;
        this.particle = null;
    }

    public void setup() {
        TargetedAreaEntity area = TargetedAreaEntity.createTargetAreaEntity(level(), this.position(), getRadius(), color);
        area.setOwner(this);
        area.setDuration(this.duration);
    }
    @Override
    public void applyEffect(LivingEntity target) {
        effect.accept(target);
    }

    @Override
    public float getParticleCount() {
        return particleCount;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.ofNullable(particle);
    }

    @Override
    protected void defineSynchedData() {
    }
}
