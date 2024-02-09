package aster.amo.erosianmagic.mixin;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSpell.class)
public class AbstractSpellMixin {

    @Inject(method = "needsLearning", at = @At("HEAD"), cancellable = true, remap = false)
    private void needsLearningMixin(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "isLearned", at = @At("HEAD"), cancellable = true, remap = false)
    private void isLearnedMixin(Player player, CallbackInfoReturnable<Boolean> cir) {
        AbstractSpell spell = (AbstractSpell) (Object) this;
        if (player == null) {
            cir.setReturnValue(false);
        } else if (player.level().isClientSide) {
            cir.setReturnValue(ClientMagicData.getSyncedSpellData(player).isSpellLearned(spell));
        } else {
            cir.setReturnValue(MagicData.getPlayerMagicData(player).getSyncedData().isSpellLearned(spell));
        }
    }
}
