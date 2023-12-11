package aster.amo.erosianmagic.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class IrresistibleDanceEffect extends MobEffect {
    public IrresistibleDanceEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int p_19468_) {
//        if(entity.onGround()) {
//            entity.setJumping(true);
//        }
        entity.yBodyRot += 10;
        entity.hurtMarked = true;
        super.applyEffectTick(entity, p_19468_);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
