package aster.amo.erosianmagic.spellsnspellbooks.spells.entity;

import aster.amo.erosianmagic.registry.EntityRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.magic_arrow.MagicArrowProjectile;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PsychicLanceProjectile extends MagicArrowProjectile {
    private final List<Entity> victims;
    private int penetration;
    BlockPos lastHitBlock;
    LivingEntity target;
    @Override

    public void trailParticles() {
        Vec3 vec3 = this.position().subtract(this.getDeltaMovement());
        this.level().addParticle(ParticleHelper.UNSTABLE_ENDER, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
    }
    @Override

    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.level(), ParticleHelper.UNSTABLE_ENDER, x, y, z, 15, 0.1, 0.1, 0.1, 0.5, false);
    }
    @Override

    public float getSpeed() {
        return 1.4F;
    }
    @Override

    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }

    public PsychicLanceProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.victims = new ArrayList();
    }

    public PsychicLanceProjectile(Level levelIn, LivingEntity shooter, LivingEntity target) {
        this((EntityType) EntityRegistry.PSYCHIC_LANCE.get(), levelIn);
        this.setOwner(shooter);
        this.target = target;
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
    }

    @Override
    public void tick() {
        // rotate to face target
        if(target == null) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        Vec3 vec3 = target.position().subtract(this.position()).normalize();
        this.setDeltaMovement(vec3.scale(this.getSpeed()/2f));
        super.tick();
    }
    @Override

    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (!this.victims.contains(entity)) {
            DamageSources.applyDamage(entity, this.damage, ((AbstractSpell) SpellRegistry.MAGIC_ARROW_SPELL.get()).getDamageSource(this, this.getOwner()));
            this.victims.add(entity);
        }

    }
    @Override

    protected void onHit(HitResult result) {
        if (!this.level().isClientSide) {
            BlockPos blockPos = BlockPos.containing(result.getLocation());
            if (result.getType() == HitResult.Type.BLOCK && !blockPos.equals(this.lastHitBlock)) {
                ++this.penetration;
                this.lastHitBlock = blockPos;
            } else if (result.getType() == HitResult.Type.ENTITY) {
                ++this.penetration;
                this.level().playSound((Player)null, BlockPos.containing(this.position()), (SoundEvent) SoundRegistry.FORCE_IMPACT.get(), SoundSource.NEUTRAL, 2.0F, 0.65F);
            }
        }

        super.onHit(result);
    }
    @Override

    protected boolean shouldPierceShields() {
        return true;
    }
}
