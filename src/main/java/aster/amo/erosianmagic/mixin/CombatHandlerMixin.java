package aster.amo.erosianmagic.mixin;

import aster.amo.erosianmagic.registry.AttributeRegistry;
import aster.amo.erosianmagic.registry.MobEffectRegistry;
import aster.amo.erosianmagic.rolls.dice.Dice;
import jackiecrazy.cloakanddagger.handlers.CombatHandler;
import jackiecrazy.footwork.utils.StealthUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CombatHandler.class)
public class CombatHandlerMixin {
    @Inject(method = "pain", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/entity/living/LivingHurtEvent;setAmount(F)V"), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private static void pain(LivingHurtEvent e, CallbackInfo ci) {
        if(e.getEntity().level().isClientSide) return;
        ServerLevel world = (ServerLevel) e.getEntity().level();
        LivingEntity target = e.getEntity();
        LivingEntity attacker = null;
        DamageSource ds = e.getSource();
        Entity entity = ds.getEntity();
        if(entity instanceof LivingEntity) {
            attacker = (LivingEntity) entity;
        }
        StealthUtils.Awareness awareness = StealthUtils.INSTANCE.getAwareness(attacker, target);
        if(awareness != StealthUtils.Awareness.ALERT) {
            int sneakAttackDice = (int) attacker.getAttributeValue(AttributeRegistry.SNEAK_ATTACK_BONUS_DICE.get());
            double luck = attacker.getAttributeValue(Attributes.LUCK);
            double sneakAttackFlatBonus = attacker.getAttributeValue(AttributeRegistry.SNEAK_ATTACK_FLAT_BONUS.get());
            e.setAmount(e.getAmount() + Dice.rollMultiple(attacker.level().random, Dice.D6, sneakAttackDice, (float)luck, (int) sneakAttackFlatBonus));
            ParticleOptions type = ParticleTypes.GLOW;
            for(int i = 0; i < 10; i++) {
                world.sendParticles(type, target.getX(), target.getY(), target.getZ(), 1, 0.5, 0.5, 0.5, 0.1);
            }
            // give the target the weakened effect for 10 seconds
            int weakenLevel = (int) target.getAttributeValue(AttributeRegistry.WEAKEN_LEVEL.get());
            target.addEffect(new MobEffectInstance(MobEffectRegistry.WEAKENED.get(), 20*10, weakenLevel));
        }
    }
}
