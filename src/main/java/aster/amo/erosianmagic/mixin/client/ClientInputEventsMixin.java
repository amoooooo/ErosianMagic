package aster.amo.erosianmagic.mixin.client;

import aster.amo.erosianmagic.client.ErosianMagicClient;
import aster.amo.erosianmagic.util.ClientClassUtils;
import io.redspace.ironsspellbooks.player.ClientInputEvents;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientInputEvents.class)
public abstract class ClientInputEventsMixin {
    @Shadow
    private static void update() {
    }

    @Inject(method = "clientTick", at = @At(value = "INVOKE", target = "Lio/redspace/ironsspellbooks/api/util/Utils;getPlayerSpellbookStack(Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true, remap = false)
    private static void clientTick(TickEvent.ClientTickEvent event, CallbackInfo ci) {
        if(ClientClassUtils.isOneOfClasses("Witch", "Cleric")) {
            update();
            ci.cancel();
        }
    }

    @Inject(method = "onKeyInput", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"), cancellable = true, remap = false)
    private static void keyInput(InputEvent.Key event, CallbackInfo ci) {
        if(ClientClassUtils.isOneOfClasses("Witch", "Cleric")) {
            ci.cancel();
        }
    }
}
