package aster.amo.erosianmagic.mixin;

import aster.amo.erosianmagic.mage.machinist.MachinistClient;
import com.flansmod.common.actions.ActionStack;
import com.flansmod.common.actions.EActionResult;
import com.flansmod.common.actions.contexts.ActionGroupContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ActionStack.class)
public class ActionStackMixin {
    @Inject(method = "TryStartGroupInstance", at = @At("HEAD"), cancellable = true, remap = false)
    private void machinist$cancelFirearm(ActionGroupContext groupContext, boolean doInitialTrigger, CallbackInfoReturnable<EActionResult> cir){
        ActionStack stack = (ActionStack)(Object)this;
        if(stack.IsClient){
            cir.setReturnValue(MachinistClient.shouldCancelShoot());
        }
    }
}
