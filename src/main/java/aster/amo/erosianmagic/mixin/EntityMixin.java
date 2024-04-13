package aster.amo.erosianmagic.mixin;

import aster.amo.erosianmagic.rogue.charlatan.ICharlatan;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "isIgnoringBlockTriggers", at = @At("HEAD"), cancellable = true)
    private void isIgnoringBlockTriggers(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if(entity instanceof Player pl) {
            pl.getCapability(ICharlatan.INSTANCE).ifPresent(charlatan -> {
                if(charlatan.isChosenClass())
                    if(charlatan.getLevel() >= 5 && pl.isCrouching())
                        cir.setReturnValue(true);
            });
        }
    }
}
