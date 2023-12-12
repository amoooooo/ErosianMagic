package aster.amo.erosianmagic.mobeffect;

import elucent.eidolon.client.particle.Particles;
import elucent.eidolon.registries.EidolonParticles;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.tslat.effectslib.api.ExtendedMobEffect;
import org.jetbrains.annotations.Nullable;

public class ManaRegenEffect extends ExtendedMobEffect {
    public ManaRegenEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void tick(LivingEntity entity, @Nullable MobEffectInstance effectInstance, int amplifier) {
//        if(entity instanceof Player player && !player.level().isClientSide && player.level().getGameTime() % 10 == 0) {
//            MagicData data = MagicData.getPlayerMagicData(player);
//            data.addMana(1.0f * amplifier);
//        }
        super.tick(entity, effectInstance, amplifier);
    }
    @Override
    public boolean shouldTickEffect(@Nullable MobEffectInstance effectInstance, @Nullable LivingEntity entity, int ticksRemaining, int amplifier) {
        return true;
    }
}
