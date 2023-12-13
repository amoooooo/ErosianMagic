package aster.amo.erosianmagic.mixin;

import aster.amo.erosianmagic.util.IExtendedTargetArea;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TargetedAreaEntity.class)
public class TargetedAreaEntityMixin implements IExtendedTargetArea {
    @Unique
    private boolean erosianMagic$endless = false;
    @Override
    public boolean erosianMagic$isEndless() {
        return erosianMagic$endless;
    }

    @Override
    public void erosianMagic$setEndless(boolean endless) {
        this.erosianMagic$endless = endless;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo info) {
        if (erosianMagic$isEndless()) {
            info.cancel();
        }
    }
}
