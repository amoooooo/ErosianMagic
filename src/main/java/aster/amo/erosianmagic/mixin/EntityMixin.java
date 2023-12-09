package aster.amo.erosianmagic.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    private void isInvisibleTo(Player p_20178_, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof Player pls) {
            if (pls.hasEffect(MobEffects.INVISIBILITY)) {
                if(Minecraft.getInstance().player != null && pls == Minecraft.getInstance().player) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
