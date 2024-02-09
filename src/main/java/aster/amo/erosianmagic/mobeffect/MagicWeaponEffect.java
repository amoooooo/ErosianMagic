package aster.amo.erosianmagic.mobeffect;

import foundry.alembic.damage.AlembicDamageHelper;
import foundry.alembic.types.AlembicDamageType;
import foundry.alembic.types.DamageTypeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class MagicWeaponEffect extends MobEffect {
    public MagicWeaponEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        ResourceLocation damageType = new ResourceLocation("alembic:arcane_damage");
        AlembicDamageType type = DamageTypeManager.getDamageType(damageType);
        if(type != null) {
            Attribute damage = type.getAttribute();
            if(!this.getAttributeModifiers().containsKey(damage))
                this.getAttributeModifiers().put(type.getAttribute(), new AttributeModifier("Magic Weapon", 1.0 * pAmplifier, AttributeModifier.Operation.ADDITION));
        }
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }
}
