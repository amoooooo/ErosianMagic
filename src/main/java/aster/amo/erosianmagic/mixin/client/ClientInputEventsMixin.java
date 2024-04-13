package aster.amo.erosianmagic.mixin.client;

import aster.amo.erosianmagic.client.ErosianMagicClient;
import aster.amo.erosianmagic.util.ClientClassUtils;
import com.cstav.genshinstrument.item.InstrumentItem;
import io.redspace.ironsspellbooks.player.ClientInputEvents;
import io.redspace.ironsspellbooks.player.KeyMappings;
import io.redspace.ironsspellbooks.player.KeyState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import org.spongepowered.asm.mixin.Final;
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

    @Inject(method = "handleInputEvent", at = @At("HEAD"), cancellable = true, remap = false)
    private static void clientTick(int button, int action, CallbackInfo ci) {
        if(Minecraft.getInstance().player == null) {
            return;
        }
        if((ClientClassUtils.isOneOfClasses("Witch", "Cleric") && button != KeyMappings.SPELL_WHEEL_KEYMAP.getKey().getValue())
                || (ClientClassUtils.isOneOfClasses("Bard") && !(Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof InstrumentItem))
                || !ClientClassUtils.isOneOfClasses("Machinist", "Barbarian", "Paladin")
        ) {
            update();
            ci.cancel();
        }
    }
}
