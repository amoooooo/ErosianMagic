package aster.amo.erosianmagic.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class MantleOfInspirationEffect extends MobEffect {
    public MantleOfInspirationEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void addAttributeModifiers(LivingEntity p_19478_, AttributeMap p_19479_, int p_19480_) {
        p_19478_.setAbsorptionAmount(p_19478_.getAbsorptionAmount() + 7.0F * (float)(p_19480_ + 1));
        super.addAttributeModifiers(p_19478_, p_19479_, p_19480_);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity p_19469_, AttributeMap p_19470_, int p_19471_) {
        p_19469_.setAbsorptionAmount(p_19469_.getAbsorptionAmount() - 3.0F * (float)(p_19471_ + 1));
        super.removeAttributeModifiers(p_19469_, p_19470_, p_19471_);
    }
}
