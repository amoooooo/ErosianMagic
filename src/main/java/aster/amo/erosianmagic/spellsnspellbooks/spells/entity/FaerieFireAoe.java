package aster.amo.erosianmagic.spellsnspellbooks.spells.entity;

import aster.amo.erosianmagic.registry.EntityRegistry;
import aster.amo.erosianmagic.registry.MobEffectRegistry;
import io.redspace.ironsspellbooks.api.events.SpellHealEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

import java.util.UUID;

public class FaerieFireAoe extends AoeEntity implements AntiMagicSusceptible {
    public FaerieFireAoe(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FaerieFireAoe(Level level){
        super(EntityRegistry.FAERIE_FIRE_AOE.get(), level);
    }

    @Override
    public Boolean isCircular() {
        return true;
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        discard();
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return !pTarget.isSpectator() && pTarget.isAlive() && pTarget.isPickable();
    }

    public static final UUID FAERIE_FIRE_UUID = UUID.fromString("e3f5f3a0-3b7a-4f4a-9c3a-5a5e8a9a4b7e");
    @Override
    public void applyEffect(LivingEntity target) {
        if (getOwner() instanceof LivingEntity owner && !Utils.shouldHealEntity(owner, target)) {
            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0));
            target.addEffect(new MobEffectInstance(MobEffectRegistry.FAERIE_FIRE.get(), 200, 0));
        }
    }

    @Override
    public float getParticleCount() {
        return 0.25f;
    }

    @Override
    public ParticleOptions getParticle() {
        return ParticleRegistry.WISP_PARTICLE.get();
    }
}
