package aster.amo.erosianmagic.mixin;

import com.mrcrayfish.guns.entity.ProjectileEntity;
import com.mrcrayfish.guns.init.ModDamageTypes;
import foundry.alembic.damage.AlembicDamageHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AlembicDamageHandler.class)
public class AlembicDamageHandlerMixin {
    @Inject(method = "getAttacker", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getAttacker(DamageSource originalSource, CallbackInfoReturnable<LivingEntity> cir) {
        if(originalSource instanceof ModDamageTypes.Sources.BulletDamageSource source) {
            cir.setReturnValue((LivingEntity) source.getEntity());
        }
    }
}
